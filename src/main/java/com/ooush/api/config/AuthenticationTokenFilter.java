package com.ooush.api.config;

import com.ooush.api.security.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private TokenUtils tokenUtils;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String authToken = httpRequest.getHeader("X-Auth-Token");
		String username;
		if (authToken != null) {
			// get the type of the token and validate depending on this type
			String type = this.tokenUtils.getTypeFromToken(authToken);
			// pull username out of token
			username = this.tokenUtils.getUsernameFromToken(authToken);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				// find user in our database
				try {
					UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
					if (this.tokenUtils.validateToken(authToken, userDetails)) {
						authenticateUser(httpRequest, userDetails);
					}
					else {
						LOGGER.debug("Unable to validate token user {} token {}", username, authToken);
					}
				}
				catch (UsernameNotFoundException e) {
					LoggerFactory.getLogger("SecurityLogger").info("Token login attempted with unknown user {}", username);
				}
			}
			if (username != null) {
				this.tokenUtils.resetTimeoutForUser(authToken.trim(), username);
			}
		}
		chain.doFilter(request, response);
	}

	private void authenticateUser(HttpServletRequest httpRequest, UserDetails userDetails) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
