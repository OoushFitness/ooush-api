package com.ooush.api.security;

import com.ooush.api.entity.LoginToken;
import com.ooush.api.entity.Users;
import com.ooush.api.repository.LoginTokenRepository;
import com.ooush.api.service.users.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

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

	public void save(LoginToken loginToken) {
		userTokens.put(loginToken.getUsers().getUsersId(), loginToken);
		LOGGER.debug("Persisting token for user {}", loginToken.getUsers().getUserName());
		loginTokenRepository.save(loginToken);
	}

	public void delete(LoginToken loginToken) {
		userTokens.remove(loginToken.getUsers().getUsersId());
		loginTokenRepository.deleteById(loginToken.getTokenId());
	}

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

	public String getUserToken(String username) {
		LoginToken token = null;
		Users users = userService.findByUserName(username);
		if (users != null && findByUser(users) != null) {
			token = findByUser(users);
			final DateTime tokenExpiry = token.getExpiry();
			if (tokenExpiry.isBeforeNow()) {
				LOGGER.debug("Token for user: {}, already created but has expired, deleting token...", username);
				delete(token);
				token = null;
			}
		}

		if (token == null) {
			LOGGER.debug("Creating new token for {}", username);
			Map<String, Object> claims = new HashMap<>();
			claims.put("sub", username);
			claims.put("type", "user");

			final Date expDate = this.generateExpirationDate();
			token = new LoginToken(users, this.generateUserToken(claims, expDate), new DateTime(expDate));
			save(token);
		}
		else {
			LOGGER.debug("Returning existing token for {}", username);
		}
		return token.getToken();
	}

	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + Integer.parseInt("604800") * 1000);
	}

	private String generateUserToken(Map<String, Object> claims, Date expDate) {
		return Jwts.builder().setClaims(claims).setExpiration(expDate).signWith(SignatureAlgorithm.HS512, "Shhhhh!").compact();
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

	public synchronized void logout(String token) {
		LoginToken loginToken = findByToken(token);
		if(loginToken == null) {
			LOGGER.warn("Token was not found");
		}
		else {
			LOGGER.debug("Expiring token");
			removeTimeoutForUser(token);
			expireToken(token);
		}
	}

	public LoginToken findByToken(String tokenString) {
		for(LoginToken token : userTokens.values()) {
			if (token.getToken().equals(tokenString)) {
				return token;
			}
		}
		LoginToken token = loginTokenRepository.findByToken(tokenString);
		if (token != null) {
			LOGGER.debug("Found persisted token using token string");
		}
		return token;
	}

	public void expireToken(String token) {
		String username = this.getUsernameFromToken(token);
		LOGGER.debug("Expiring {}'s token", username);
		LoginToken userToken = findByToken(token);
		if (userToken != null) {
			delete(userToken);
		}
		final Claims claims = this.getClaimsFromOoushToken(token);
		if(claims != null) {
			claims.setExpiration(new Date(System.currentTimeMillis() - (long) Integer.parseInt("24") * 1000 * 10000));
		}
	}
}
