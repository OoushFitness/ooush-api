package com.ooush.api.security;

import com.ooush.api.entity.LoginToken;
import com.ooush.api.entity.Users;
import com.ooush.api.repository.LoginTokenRepository;
import com.ooush.api.service.users.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;

@Transactional
@Service
public class TokenUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtils.class);

	@Autowired
	private UserService userService;

	@Autowired
	private LoginTokenRepository loginTokenRepository;

	private static final Map<Integer, LoginToken> userTokens = new ConcurrentHashMap<>();

	private static final Map<String, Timer> sessionLogoutTimers = new ConcurrentHashMap<>();

	/**
	 * using a private claim name of 'type' to distinguish token types
	 * @param token
	 * @return claim string of 'type'
	 */
	public String getTypeFromToken(String token) {
		final Claims claims = this.getClaimsFromOoushToken(token);
		if (claims == null) {
			return null;
		}
		else {
			String type = (String)claims.get("type");
			if (type == null) {
				type = "user";
			}
			return type;
		}
	}

	private Claims getClaimsFromOoushToken(String token) {
		return getClaimsFromToken(token, "Shhhhh!");
	}

	public Claims getClaimsFromToken(String token, String key) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		}
		catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	public String getUsernameFromToken(String token) {
		final Claims claims = this.getClaimsFromOoushToken(token);
		if (claims == null) {
			return null;
		}
		else {
			return claims.getSubject();
		}
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		Users users = userService.findUserByUserName(userDetails.getUsername());
		final LoginToken loginToken = findByUser(users);
		if (!users.isActive()) {
			LOGGER.debug("User {} is not active", users.getUsersId());
			return false;
		}
		if (loginToken == null) {
			LOGGER.debug("User {} does not have a token", users.getUsersId());
			return false;
		}
		if (!loginToken.getToken().equals(token)) {
			LOGGER.debug("User {} supplied token does not match stored token", users.getUsersId());
			return false;
		}
		final DateTime tokenExpiry = loginToken.getExpiry();
		final DateTime now = new DateTime();
		if(tokenExpiry.isEqual(now) || tokenExpiry.isBefore(now)) {
			LOGGER.debug("User {} token has expired", users.getUsersId());
			return false;
		}

		if (!this.isIdentityConfirmed(users)) {
			LOGGER.debug("User {} has not confirmed their identity", users.getUsersId());
			return false;
		}

		return true;
	}

	public Boolean isIdentityConfirmed(Users user) {
		Boolean identityConfirmed = Boolean.TRUE;
		DateTime verificationTime = user.getIdentityVerificationTime();
		DateTime currentDate = new DateTime();
		Days daysBetween = Days.daysBetween(verificationTime, currentDate);
		if (daysBetween.getDays() >= Integer.parseInt("90")) {
			identityConfirmed = Boolean.FALSE;
		}
		return identityConfirmed;
	}

	public LoginToken findByUser(Users user) {
		LoginToken token = userTokens.get(user.getUsersId());
		if (token == null && user.getLoginTokens() != null && !user.getLoginTokens().isEmpty()) {
			LOGGER.debug("Found persisted token for user {}", user.getUserName());
			token = user.getLoginTokens().get(0);
			userTokens.put(user.getUsersId(), token);
		}
		return token;
	}

	public synchronized void setTimeoutForUser(String token, String userName) {
		if (sessionLogoutTimers.containsKey(token.trim())) {
			this.resetTimeoutForUser(token, userName);
		}
		else {
			createTimeoutTask(token, userName);
		}
	}

	public synchronized void resetTimeoutForUser(String token, String userName) {
		if (sessionLogoutTimers.containsKey(token.trim())) {
			removeTimeoutForUser(token);
		}
		else {
			LOGGER.debug("No timer found for {}, setting timer now", userName);
		}
		createTimeoutTask(token, userName);
	}

	public synchronized void removeTimeoutForUser(String token) {
		if (sessionLogoutTimers.containsKey(token.trim())) {
			Timer timer = sessionLogoutTimers.remove(token);
			timer.cancel();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Removed timer for {}", getUsernameFromToken(token));
			}
		}
		else {
			LOGGER.debug("No timer found for username {} token {}", getUsernameFromToken(token), token);
		}
	}

	private synchronized void createTimeoutTask(String token, String userName) {
		LOGGER.debug("Scheduling inactive logout for {}", userName);
		Timer timer = new Timer(true);
		TimerTask timerTask = new InactiveTimeoutLogoutTask(token);
		timer.schedule(timerTask, (Integer.parseInt("60") * 60L) * 1000);
		sessionLogoutTimers.put(token.trim(), timer);
		LOGGER.debug("Inactive logout scheduled for {} mins", "60");
	}

	private class InactiveTimeoutLogoutTask extends TimerTask {
		private final String token;

		public InactiveTimeoutLogoutTask(String token) {
			this.token = token;
		}

		@Override
		public void run() {
			logout(token);
		}
	}

}
