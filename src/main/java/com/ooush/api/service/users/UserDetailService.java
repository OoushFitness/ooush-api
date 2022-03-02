package com.ooush.api.service.users;

import com.ooush.api.entity.Users;
import com.ooush.api.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("UserDetailService")
public class UserDetailService implements UserDetailsService {

	@Autowired
	private UserRespository userRepo;

	@Override
	public UserDetails loadUserByUsername(String userName) {
		Users user = userRepo.findByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Username " + userName + " not found");
		}

		return new UserDetailsImpl(user);
	}

	public static final class UserDetailsImpl implements UserDetails {

		private static final long serialVersionUID = 1L;

		private Users user;

		private UserDetailsImpl(Users user) {
			this.user = user;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			List<GrantedAuthority> authorities = new ArrayList<>();
			return authorities;
		}

		public Users getUser() {
			return this.user;
		}

		@Override
		public String getUsername() {
			return user.getUserName();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return user.isActive();
		}

		@Override
		public String getPassword() {
			return user.getPasswordHash();
		}

		public int getUserId() {
			return user.getUsersId();
		}
	}

}
