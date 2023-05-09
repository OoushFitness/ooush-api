package com.ooush.api.service.users;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ooush.api.entity.Users;
import com.ooush.api.repository.UserRepository;

class UserDetailServiceTest {

    private final static String USER_NAME = "test_user_1@ooushfitness.com";
    private final static String PASSWORD_HASH = "$2a$08$svfVbtVTSEDcBtp1mWvYceVQLl3xH5ACBaE3bpKHdN8roN1FJMxD2";
    private final static int USER_ID = 1;
    private final static boolean ACTIVE = true;

    @Mock
    UserRepository mockUserRepo;

    @InjectMocks
    private UserDetailService target = new UserDetailService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("shouldLoadUserDetailsImplClassPopulatedWithUserDetailsIfUserFound")
    @Test
    void shouldLoadUserDetailsImplClassPopulatedWithUserDetailsIfUserFound() {
        Users user = new Users();
        user.setUserName(USER_NAME);
        user.setActive(ACTIVE);
        user.setPasswordHash(PASSWORD_HASH);
        user.setUsersId(USER_ID);

        when(mockUserRepo.findByUserName(USER_NAME)).thenReturn(user);

        UserDetailService.UserDetailsImpl userDetails = (UserDetailService.UserDetailsImpl) target.loadUserByUsername(USER_NAME);

        assertThat(userDetails.getUser(), Matchers.equalToObject(user));
        assertThat(userDetails.getUsername(), Matchers.equalTo(USER_NAME));
        assertThat(userDetails.isEnabled(), Matchers.equalTo(ACTIVE));
        assertThat(userDetails.getPassword(), Matchers.equalTo(PASSWORD_HASH));
        assertThat(userDetails.getUserId(), Matchers.equalTo(USER_ID));
        assertThat(userDetails.isAccountNonExpired(), Matchers.equalTo(true));
        assertThat(userDetails.isAccountNonLocked(), Matchers.equalTo(true));
        assertThat(userDetails.isCredentialsNonExpired(), Matchers.equalTo(true));
        assertThat(userDetails.getAuthorities(), Matchers.equalTo(new ArrayList<GrantedAuthority>()));
    }

    @DisplayName("shouldThrowExceptionIfUserNotFound")
    @Test
    void shouldThrowExceptionIfUserNotFound() {
        when(mockUserRepo.findByUserName(anyString())).thenReturn(null);

        try {
            target.loadUserByUsername(USER_NAME);
            fail("Should throw exception");
        } catch (UsernameNotFoundException e) {
            // THEN should throw an exception
            assertEquals("Username test_user_1@ooushfitness.com not found", e.getMessage());
        }
    }
}