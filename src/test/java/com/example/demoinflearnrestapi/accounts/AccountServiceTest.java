package com.example.demoinflearnrestapi.accounts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("이메일로 사용자 찾기")
    @Test
    public void findByUserEmail() {
        // Given
        String email = "test@email.com";
        String password = "test1234";
        Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(account);

        // When
        UserDetails userDetails = accountService.loadUserByUsername(email);

        // Then
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }

    @DisplayName("이메일로 사용자 찾기 실패")
    @Test
    public void findByUserEmailFail() {
        // Nothing given

        // When && Then
        String email = "test@email.com";
        try {
            accountService.loadUserByUsername(email);
            fail("supposed to be failed");
        } catch (UsernameNotFoundException exception) {
            assertThat(exception.getMessage()).containsSequence(email);
        }
    }
}