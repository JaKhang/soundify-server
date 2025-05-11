package com.soundify.server.account.domain.models;

import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.account.application.exceptions.AuthenticationException;
import com.soundify.server.shared.exceptions.DomainException;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountTest {

    private Account account;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Given: A valid account instance
        Id accountId = Id.fast();
        String username = "testUser";
        String email = "test@example.com";
        String password = "$2a$10$abcdefghi"; // Mocked encoded password
        String displayName = "Test User";
        List<Image> avatar = Collections.emptyList();
        LocalDate dateOfBirth = LocalDate.of(1995, 5, 10);
        Gender gender = Gender.MALE;
        Locale locale = Locale.US;
        Provider provider = Provider.SOUNDIFY;
        LocalDateTime verifiedAt = null;
        Set<Role> roles = new HashSet<>(Collections.singletonList(Role.USER));
        AccountStatus status = AccountStatus.ACTIVE;

        account = new Account(accountId, username, email, password, displayName, avatar, dateOfBirth, gender, locale, provider, verifiedAt, roles, status);
        passwordEncoder = mock(PasswordEncoder.class);
    }


    // -------------------- registerDevice() ------------------

    @Test
    void givenValidInput_whenRegisterDevice_thenDeviceIsRegistered() {
        // Given
        String os = "Android";
        String ip = "192.168.1.1";
        String platform = "Mobile";
        int age = 7;

        // When
        Device device = account.registerDevice(os, ip, platform, age, ChronoUnit.DAYS);

        // Then
        assertNotNull(device);
        assertEquals(os, device.getOs());
        assertEquals(ip, device.getIp());
        assertEquals(platform, device.getPlatform());
        assertTrue(device.getExpiredAt().isAfter(LocalDateTime.now()));
        assertTrue(account.getDevices().contains(device));
    }

    @Test
    void givenInvalidInput_whenRegisterDevice_thenThrowException() {
        // Given
        String os = null;
        String ip = "";
        String platform = "Mobile";
        int age = 7;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> account.registerDevice(os, "192.168.1.1", platform, age, ChronoUnit.DAYS));
        assertThrows(IllegalArgumentException.class, () -> account.registerDevice("Windows", ip, platform, age, ChronoUnit.DAYS));
        assertThrows(IllegalArgumentException.class, () -> account.registerDevice("Windows", "192.168.1.1", "", age, ChronoUnit.DAYS));
    }

    @Test
    void givenDeviceExpired_whenCheckIsValidDevice_thenReturnFalse() {
        // Given
        String os = "iOS";
        String ip = "10.0.0.1";
        String platform = "Tablet";
        int age = 0; // Thiết bị đã hết hạn

        Device device = account.registerDevice(os, ip, platform, age, ChronoUnit.DAYS);

        // When
        boolean isValid = account.isValidDevice(device.getId());

        // Then
        assertFalse(isValid);
    }

    @Test
    void givenValidDevice_whenCheckIsValidDevice_thenReturnTrue() {
        // Given
        String os = "Linux";
        String ip = "127.0.0.1";
        String platform = "Desktop";
        int age = 10;

        Device device = account.registerDevice(os, ip, platform, age, ChronoUnit.DAYS);

        // When
        boolean isValid = account.isValidDevice(device.getId());

        // Then
        assertTrue(isValid);
    }

    // -------------------- changePassword() --------------------



    @Test
    void givenIncorrectCurrentPassword_whenChangePassword_thenThrowException() {
        when(passwordEncoder.matches("WrongPass", account.getPassword())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> account.changePassword("WrongPass", "NewPass@456", passwordEncoder));
    }

    @Test
    void givenShortNewPassword_whenChangePassword_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> account.changePassword("Current@123", "short", passwordEncoder));
    }

    // -------------------- updateProfile() --------------------

    @Test
    void givenValidProfileUpdates_whenUpdateProfile_thenUpdatesApplied() {
        account.updateProfile("New Name", List.of(new Image("avatar.jpg", 200, 200)), LocalDate.of(1998, 3, 25), Gender.FEMALE);

        assertEquals("New Name", account.getDisplayName());
        assertEquals(1, account.getAvatar().size());
        assertEquals(Gender.FEMALE, account.getGender());
    }

    @Test
    void givenFutureDateOfBirth_whenUpdateProfile_thenNoUpdate() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        account.updateProfile(null, null, futureDate, null);
        assertNotEquals(futureDate, account.getDateOfBirth()); // Date should remain unchanged
    }

    // -------------------- verifyEmail() --------------------

    @Test
    void givenValidToken_whenVerifyEmail_thenAccountVerified() {
        account.addVerificationToken("validToken", 5, ChronoUnit.HOURS);

        account.verifyEmail("validToken");

        assertNotNull(account.getVerifiedAt());
    }

    @Test
    void givenInvalidToken_whenVerifyEmail_thenThrowException() {
        account.addVerificationToken("validToken", 5, ChronoUnit.HOURS);

        assertThrows(AuthenticationException.class, () -> account.verifyEmail("wrongToken"));
    }

    // -------------------- authenticateByToken() --------------------

    @Test
    void givenValidToken_whenAuthenticateByToken_thenAuthenticationSuccess() {
        account.setAuthenticationToken("validAuthToken", 10, ChronoUnit.HOURS);
        account.AuthenticateByToken("validAuthToken");

        assertNull(account.getAuthenticationToken());
    }

    @Test
    void givenInvalidToken_whenAuthenticateByToken_thenThrowException() {
        account.setAuthenticationToken("validAuthToken", 10, ChronoUnit.HOURS);

        assertThrows(AuthenticationException.class, () -> account.AuthenticateByToken("invalidToken"));
    }

    // -------------------- addVerificationToken() --------------------

    @Test
    void givenValidTokenValue_whenAddVerificationToken_thenTokenIsAdded() {
        // Given
        String tokenValue = "valid-token";
        int age = 7;

        // When
        account.addVerificationToken(tokenValue, age, ChronoUnit.DAYS);

        // Then
        assertTrue(account.getVerificationTokens().stream()
                .anyMatch(token -> token.value().equals(tokenValue) &&
                        token.expiredAt().isAfter(LocalDateTime.now())));
    }

    @Test
    void givenInvalidTokenValue_whenAddVerificationToken_thenThrowException() {
        // Given
        String invalidTokenValue = " ";
        int age = 7;

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> account.addVerificationToken(invalidTokenValue, age, ChronoUnit.DAYS));

        assertThrows(IllegalArgumentException.class,
                () -> account.addVerificationToken(null, age, ChronoUnit.DAYS));
    }

    @Test
    void givenInvalidAge_whenAddVerificationToken_thenThrowException() {
        // Given
        String tokenValue = "valid-token";
        int invalidAge = -1;

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> account.addVerificationToken(tokenValue, invalidAge, ChronoUnit.DAYS));
    }

    @Test
    void givenInvalidChronoUnit_whenAddVerificationToken_thenThrowException() {
        // Given
        String tokenValue = "valid-token";
        int age = 7;

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> account.addVerificationToken(tokenValue, age, null));
    }

    @Test
    void givenMaxTokensExceeded_whenAddVerificationToken_thenThrowException() {
        // Given
        int maxTokens = 50;
        for (int i = 0; i < maxTokens; i++) {
            account.addVerificationToken("token-" + i, 7, ChronoUnit.DAYS);
        }

        // When & Then
        assertThrows(DomainException.class,
                () -> account.addVerificationToken("exceed-token", 7, ChronoUnit.DAYS));
    }

    // -------------------- addResetPasswordToken() --------------------

    @Test
    void givenValidTokenValue_whenAddResetPasswordToken_thenTokenIsAdded() {
        // Given
        String tokenValue = "valid-reset-token";
        int age = 7;

        // When
        account.addResetPasswordToken(tokenValue, age, ChronoUnit.DAYS);

        // Then
        assertTrue(account.getResetPasswordTokens().stream()
                .anyMatch(token -> token.value().equals(tokenValue) &&
                        token.expiredAt().isAfter(LocalDateTime.now())));
    }

    @Test
    void givenInvalidTokenValue_whenAddResetPasswordToken_thenThrowException() {
        // Given
        String invalidTokenValue = " ";
        int age = 7;

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> account.addResetPasswordToken(invalidTokenValue, age, ChronoUnit.DAYS));

        assertThrows(IllegalArgumentException.class,
                () -> account.addResetPasswordToken(null, age, ChronoUnit.DAYS));
    }

    @Test
    void givenInvalidAge_whenAddResetPasswordToken_thenThrowException() {
        // Given
        String tokenValue = "valid-reset-token";
        int invalidAge = -1;

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> account.addResetPasswordToken(tokenValue, invalidAge, ChronoUnit.DAYS));
    }

    @Test
    void givenInvalidChronoUnit_whenAddResetPasswordToken_thenThrowException() {
        // Given
        String tokenValue = "valid-reset-token";
        int age = 7;

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> account.addResetPasswordToken(tokenValue, age, null));
    }

    @Test
    void givenMaxTokensExceeded_whenAddResetPasswordToken_thenThrowException() {
        // Given
        int maxTokens = 50;
        for (int i = 0; i < maxTokens; i++) {
            account.addResetPasswordToken("reset-token-" + i, 7, ChronoUnit.DAYS);
        }

        System.out.println(account.getResetPasswordTokens().size());
        // When & Then
        assertThrows(DomainException.class,
                () -> account.addResetPasswordToken("exceed-reset-token", 7, ChronoUnit.DAYS));
    }

    // -------------------- setAuthenticationToken() --------------------


    @Test
    void givenValidToken_whenSetAuthenticationToken_thenTokenIsSet() {
        // Given
        String tokenValue = "valid-auth-token";
        int age = 7;

        // When
        account.setAuthenticationToken(tokenValue, age, ChronoUnit.DAYS);

        // Then
        assertNotNull(account.getAuthenticationToken());
        assertEquals(tokenValue, account.getAuthenticationToken().value());
        assertTrue(account.getAuthenticationToken().expiredAt().isAfter(LocalDateTime.now()));
    }

    @Test
    void givenInvalidTokenValue_whenSetAuthenticationToken_thenThrowException() {
        // Given
        String invalidTokenValue = " ";

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> account.setAuthenticationToken(invalidTokenValue, 7, ChronoUnit.DAYS));

        assertThrows(IllegalArgumentException.class,
                () -> account.setAuthenticationToken(null, 7, ChronoUnit.DAYS));
    }

    @Test
    void givenInvalidAge_whenSetAuthenticationToken_thenThrowException() {
        // Given
        String tokenValue = "valid-auth-token";
        int invalidAge = -1;

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> account.setAuthenticationToken(tokenValue, invalidAge, ChronoUnit.DAYS));
    }

    @Test
    void givenInvalidChronoUnit_whenSetAuthenticationToken_thenThrowException() {
        // Given
        String tokenValue = "valid-auth-token";

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> account.setAuthenticationToken(tokenValue, 7, null));
    }

    @Test
    void givenExistingToken_whenSetAuthenticationToken_thenTokenIsOverwritten() {
        // Given
        String oldTokenValue = "old-auth-token";
        String newTokenValue = "new-auth-token";
        account.setAuthenticationToken(oldTokenValue, 7, ChronoUnit.DAYS);

        // When
        account.setAuthenticationToken(newTokenValue, 7, ChronoUnit.DAYS);

        // Then
        assertNotNull(account.getAuthenticationToken());
        assertEquals(newTokenValue, account.getAuthenticationToken().value());
        assertTrue(account.getAuthenticationToken().expiredAt().isAfter(LocalDateTime.now()));
    }

    // -------------------- changeAvatar() --------------------

    @Test
    void givenValidAvatarList_whenChangeAvatar_thenAvatarIsUpdated() {
        // Given
        List<Image> newAvatars = List.of(
                new Image("avatar1.jpg", 300, 300),
                new Image("avatar2.jpg", 300, 300)
        );

        // When
        account.changeAvatar(newAvatars);

        // Then
        assertNotNull(account.getAvatar());
        assertEquals(newAvatars, account.getAvatar());
        assertEquals(2, account.getAvatar().size());
        assertEquals("avatar1.jpg", account.getAvatar().get(0).getUrl());
        assertEquals("avatar2.jpg", account.getAvatar().get(1).getUrl());
    }

    @Test
    void givenInvalidAvatarList_whenChangeAvatar_thenThrowException() {
        // Given
        List<Image> invalidAvatars = List.of(
                new Image(" ", 300, 300), // URL rỗng hoặc không hợp lệ
                new Image(null, 300, 300) // URL null
        );

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> account.changeAvatar(invalidAvatars));
    }



    @Test
    void givenExistingAvatarList_whenChangeAvatar_thenAvatarIsUpdated() {
        // Given
        List<Image> oldAvatars = List.of(
                new Image("old-avatar1.jpg", 300, 300),
                new Image("old-avatar2.jpg", 300, 300)
        );
        List<Image> newAvatars = List.of(
                new Image("new-avatar1.jpg", 300, 300),
                new Image("new-avatar2.jpg", 300, 300)
        );
        account.changeAvatar(oldAvatars); // Thiết lập danh sách avatar ban đầu

        // When
        account.changeAvatar(newAvatars);

        // Then
        assertNotNull(account.getAvatar());
        assertEquals(newAvatars, account.getAvatar());
        assertEquals(2, account.getAvatar().size());
        assertEquals("new-avatar1.jpg", account.getAvatar().get(0).getUrl());
        assertEquals("new-avatar2.jpg", account.getAvatar().get(1).getUrl());
    }

    // -------------------- unregisterDevices() --------------------

    @Test
    void givenExistingDevice_whenUnregisterDevice_thenDeviceIsRemoved() {
        // Given
        // Given
        String os = "Linux";
        String ip = "127.0.0.1";
        String platform = "Desktop";
        int age = 10;

        Device device = account.registerDevice(os, ip, platform, age, ChronoUnit.DAYS);
        Id deviceId = device.getId();

        // When
        account.unregisterDevice(deviceId);

        // Then
        assertFalse(account.getDevices().contains(deviceId));
        assertEquals(0, account.getDevices().size());
    }

    @Test
    void givenNonExistingDevice_whenUnregisterDevice_thenThrow() {
        // Given
        String os = "Linux";
        String ip = "127.0.0.1";
        String platform = "Desktop";
        int age = 10;

        Device device = account.registerDevice(os, ip, platform, age, ChronoUnit.DAYS);
        Id deviceId = device.getId();

        // When Then
        assertThrows(ResourceNotFoundException.class, () -> account.unregisterDevice(Id.fast()));

    }

    @Test
    void givenNullDeviceId_whenUnregisterDevice_thenThrowException() {
        // Given
        Id deviceId = null;

        // When & Then
        assertThrows(DomainException.class, () -> account.unregisterDevice(deviceId));
    }



}
