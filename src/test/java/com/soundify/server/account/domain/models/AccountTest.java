package com.soundify.server.account.domain.models;

import com.soundify.server.account.domain.events.*;
import com.soundify.server.account.domain.models.*;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.AuthenticationException;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    // -------------------- registerDevice() --------------------

    @Test
    void givenValidData_whenRegisterDevice_thenDeviceAdded() {
        // When
        Device device = account.registerDevice("Android", "192.168.1.1", "Mobile");

        // Then
        assertEquals(1, account.getDevices().size());
        assertNotNull(device);
        assertEquals("Android", device.getOs());
    }

    @Test
    void givenInvalidData_whenRegisterDevice_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> account.registerDevice("", "192.168.1.1", "Mobile"));
        assertThrows(IllegalArgumentException.class, () -> account.registerDevice("Android", "", "Mobile"));
        assertThrows(IllegalArgumentException.class, () -> account.registerDevice("Android", "192.168.1.1", ""));
    }

    // -------------------- unregisterDevice() --------------------

    @Test
    void givenExistingDevice_whenUnregisterDevice_thenDeviceRemoved() {
        // Given
        Device device = account.registerDevice("Android", "192.168.1.1", "Mobile");

        // When
        account.unregisterDevice(device.getId());

        // Then
        assertTrue(account.getDevices().isEmpty());
    }

    @Test
    void givenNonExistingDevice_whenUnregisterDevice_thenThrowException() {
        Id invalidDeviceId = Id.fast();
        assertThrows(ResourceNotFoundException.class, () -> account.unregisterDevice(invalidDeviceId));
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
        account.addVerificationToken("validToken", 5);

        account.verifyEmail("validToken");

        assertNotNull(account.getVerifiedAt());
    }

    @Test
    void givenInvalidToken_whenVerifyEmail_thenThrowException() {
        account.addVerificationToken("validToken", 5);

        assertThrows(AuthenticationException.class, () -> account.verifyEmail("wrongToken"));
    }

    // -------------------- authenticateByToken() --------------------

    @Test
    void givenValidToken_whenAuthenticateByToken_thenAuthenticationSuccess() {
        account.setAuthenticationToken("validAuthToken", 10);
        account.AuthenticateByToken("validAuthToken");

        assertNull(account.getAuthenticationToken());
    }

    @Test
    void givenInvalidToken_whenAuthenticateByToken_thenThrowException() {
        account.setAuthenticationToken("validAuthToken", 10);

        assertThrows(AuthenticationException.class, () -> account.AuthenticateByToken("invalidToken"));
    }

    // -------------------- addVerificationToken() --------------------

    @Test
    void givenValidToken_whenAddVerificationToken_thenTokenAdded() {
        account.addVerificationToken("newToken", 5);

        assertEquals(1, account.getVerificationTokens().size());
    }

    // -------------------- addResetPasswordToken() --------------------

    @Test
    void givenValidToken_whenAddResetPasswordToken_thenTokenAdded() {
        account.addResetPasswordToken("resetToken", 5);

        assertEquals(1, account.getResetPasswordTokens().size());
    }

    // -------------------- setAuthenticationToken() --------------------

    @Test
    void givenValidToken_whenSetAuthenticationToken_thenTokenSet() {
        account.setAuthenticationToken("authToken", 10);

        assertNotNull(account.getAuthenticationToken());
    }

    // -------------------- changeAvatar() --------------------

    @Test
    void givenNewAvatar_whenChangeAvatar_thenAvatarUpdated() {
        List<Image> newAvatars = List.of(new Image("avatar1.jpg", 300, 300), new Image("avatar2.jpg", 300, 300));

        account.changeAvatar(newAvatars);

        assertEquals(2, account.getAvatar().size());
    }

    // -------------------- getDevices() --------------------

    @Test
    void givenDevices_whenGetDevices_thenUnmodifiableSetReturned() {
        Device device = account.registerDevice("Android", "192.168.1.1", "Mobile");

        Set<Device> devices = account.getDevices();
        assertThrows(UnsupportedOperationException.class, () -> devices.add(device)); // Should be unmodifiable
    }
}
