package com.soundify.server.account.domain.models;

import com.soundify.server.account.domain.events.*;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.AggregateRoot;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.exceptions.AuthenticationException;
import com.soundify.server.shared.exceptions.ErrorCode;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table(name = "accounts")
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends AggregateRoot {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String displayName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "account_avatars", joinColumns = @JoinColumn(name = "account_id"))
    private List<Image> avatar = new ArrayList<>();

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Locale locale;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private LocalDateTime verifiedAt;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Token> verificationTokens = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Token> resetPasswordTokens = new HashSet<>();

    private Token authenticationToken;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "account_roles", joinColumns = @JoinColumn(name = "account_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Device> devices = new HashSet<>();

    public Account(Id id, String username, String email, String password, String displayName, List<Image> avatar, LocalDate dateOfBirth, Gender gender, Locale locale, Provider provider, LocalDateTime verifiedAt, Set<Role> roles, AccountStatus status) {
        super(id);
        this.username = username;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.avatar = avatar;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.locale = locale;
        this.provider = provider;
        this.verifiedAt = verifiedAt;
        this.roles = roles;
        this.status = status;
    }

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public Device registerDevice(String os, String ip, String platform) {
        // Verify input
        if (os == null || os.isBlank()) {
            throw new IllegalArgumentException("OS cannot be null or empty.");
        }
        if (ip == null || ip.isBlank()) {
            throw new IllegalArgumentException("IP address cannot be null or empty.");
        }
        if (platform == null || platform.isBlank()) {
            throw new IllegalArgumentException("Platform cannot be null or empty.");
        }

        // Create and register device
        Device device = new Device(Id.fast(), os, ip, platform, LocalDateTime.now(), this);
        this.devices.add(device);
        registerEvents(new DeviceRegisteredEvent(this.getId().toString(), os, ip, platform, device.getLoginAt()));
        return device;
    }

    public void unregisterDevice(Id id) {
        Device deviceToRemove = this.devices.stream()
                .filter(device -> device.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Not found device"));

        this.devices.remove(deviceToRemove);
        registerEvents(new DeviceUnregisteredEvent(this.getId().toString(), id));
    }

    public void changePassword(String currentPassword, String newPassword, PasswordEncoder passwordEncoder) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters long.");
        }

        if (!passwordEncoder.matches(currentPassword, this.password)) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        this.password = passwordEncoder.encode(newPassword);
        registerEvents(new PasswordChangedEvent(this.getId().toString()));
    }

    public void updateProfile(String newDisplayName, List<Image> newAvatars, LocalDate newDateOfBirth, Gender newGender) {
        boolean isUpdated = false;

        if (newDisplayName != null && !newDisplayName.isBlank()) {
            this.displayName = newDisplayName;
            isUpdated = true;
        }

        if (newAvatars != null && !newAvatars.isEmpty()) {
            this.avatar = newAvatars;
            isUpdated = true;
        }

        if (newDateOfBirth != null && !newDateOfBirth.isAfter(LocalDate.now())) {
            this.dateOfBirth = newDateOfBirth;
            isUpdated = true;
        }

        if (newGender != null) {
            this.gender = newGender;
            isUpdated = true;
        }

        if (isUpdated) {
            registerEvents(new ProfileUpdatedEvent(this.getId().toString(), this.displayName, this.avatar, this.dateOfBirth, this.gender));
        }
    }

    public void verifyEmail(String tokenValue) {
        if (isVerified()) {
            throw new AuthenticationException(ErrorCode.INVALID_REQUEST);
        }
        Iterator<Token> iterator = verificationTokens.iterator();
        while (iterator.hasNext()) {
            Token token = iterator.next();
            if (token.value().equals(tokenValue) && token.isValid()) {
                this.verifiedAt = LocalDateTime.now();
                iterator.remove();
                registerEvents(new AccountVerifedEvent(id, username, email, this.verifiedAt));
                return;
            }
        }
        throw new AuthenticationException(ErrorCode.TOKEN_INVALID);
    }

    public void AuthenticateByToken(String tokenValue) {
        if (authenticationToken != null && authenticationToken.value().equals(tokenValue)) {
            authenticationToken = null;
            return;
        }
        throw new AuthenticationException(ErrorCode.TOKEN_INVALID);
    }

    public void addVerificationToken(String tokenValue, int age) {
        verificationTokens.add(new Token(tokenValue, LocalDateTime.now(), age));
        registerEvents(new VerifyTokenAddedEvent(getId(), email, tokenValue, age));
    }

    public void addResetPasswordToken(String tokenValue, int age) {
        resetPasswordTokens.add(new Token(tokenValue, LocalDateTime.now(), age));
        registerEvents(new ResetPasswordTokenAddedEvent(getId(), email, tokenValue, age));
    }

    public void setAuthenticationToken(String value, int age) {
        authenticationToken = new Token(value, LocalDateTime.now(), age);
        registerEvents(new AuthenticationTokenAddedEvent(getId(), email, value, age));
    }

    private boolean isVerified() {
        return verifiedAt != null;
    }

    public void changeAvatar(List<Image> avatar) {
        this.avatar = avatar;
        registerEvents(new AvatarChangedEvents(this.getId(), avatar));
    }

    public Set<Device> getDevices() {
        return UnmodifiableSet.unmodifiableSet(devices);
    }
}