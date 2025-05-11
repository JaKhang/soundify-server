package com.soundify.server.account.domain.models;

import com.soundify.server.account.domain.events.*;
import com.soundify.server.account.domain.exceptions.TokenInvalidException;
import com.soundify.server.shared.data.Image;
import com.soundify.server.shared.domain.AggregateRoot;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.account.application.exceptions.AuthenticationException;
import com.soundify.server.shared.exceptions.IllegalDomainArgumentException;
import com.soundify.server.shared.exceptions.ErrorCode;
import com.soundify.server.shared.exceptions.IllegalDomainStateException;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@Table(name = "accounts")
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends AggregateRoot {

    private static final int MAX_TOKENS_SIZE = 50;
    private static final int MAX_DEVICE = 5;

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

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Token> verificationTokens = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
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
        
        if (username == null || username.isBlank()) {
            throw new IllegalDomainArgumentException("Username cannot be null or empty");
        }
        if (username.length() < 3 || username.length() > 30) {
            throw new IllegalDomainArgumentException("Username must be between 3 and 30 characters");
        }

        this.username = username;

        // Email validation
        if (email == null || email.isBlank()) {
            throw new IllegalDomainArgumentException("Email cannot be null or empty");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            throw new IllegalDomainArgumentException("Invalid email format");
        }
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

    public Device registerDevice(String os, String ip, String platform, int age, ChronoUnit unit) {
        // Verify input
        if (os == null || os.isBlank()) {
            throw new IllegalDomainArgumentException("OS cannot be null or empty.");
        }
        if (ip == null || ip.isBlank()) {
            throw new IllegalDomainArgumentException("IP address cannot be null or empty.");
        }
        if (platform == null || platform.isBlank()) {
            throw new IllegalDomainArgumentException("Platform cannot be null or empty.");
        }

        LocalDateTime now = LocalDateTime.now();
        // Create and register device
        Device device = new Device(Id.fast(), os, ip, platform, now, now.plus(age, unit) , this);
        this.devices.add(device);
        registerEvents(new DeviceRegisteredEvent(this.getId().toString(), os, ip, platform, device.getLoginAt()));
        return device;
    }

    public boolean isValidDevice(Id deviceId){
        Device device = devices.stream().filter(d -> d.getId().equals(deviceId)).findFirst().orElse(null);
        if (device == null) return false;
        return device.getExpiredAt().isAfter(LocalDateTime.now());
    }

    public void unregisterDevice(Id id) {
        if (id == null)
            throw new IllegalDomainArgumentException("Device id must not be null");
        Device deviceToRemove = this.devices.stream()
                .filter(device -> device.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Not found device"));

        this.devices.remove(deviceToRemove);
        registerEvents(new DeviceUnregisteredEvent(this.getId().toString(), id));
    }

    public void changePassword(String currentPassword, String newPassword, PasswordEncoder passwordEncoder) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalDomainArgumentException("New password must be at least 8 characters long.");
        }

        if (!passwordEncoder.matches(currentPassword, this.password)) {
            throw new IllegalDomainArgumentException("Current password is incorrect.");
        }

        this.password = passwordEncoder.encode(newPassword);
        registerEvents(new PasswordChangedEvent(this.getId().toString()));
    }

    public void updateProfile(String newDisplayName, List<Image> newAvatars, LocalDate newDateOfBirth, Gender newGender) {
        boolean isUpdated = false;

        // Display name validation and update
        if (newDisplayName != null && !newDisplayName.isBlank()) {
            if (newDisplayName.length() > 50) {
                throw new IllegalDomainArgumentException("Display name cannot exceed 50 characters");
            }
            this.displayName = newDisplayName;
            isUpdated = true;
        }

        // Avatar validation and update
        if (newAvatars != null) {
            if (newAvatars.size() > 5) {
                throw new IllegalDomainArgumentException("Cannot have more than 5 avatars");
            }
            for (Image avatar : newAvatars) {
                if (avatar == null) {
                    throw new IllegalDomainArgumentException("Avatar cannot be null");
                }
                if (avatar.getWidth() > 2000 || avatar.getHeight() > 2000) {
                    throw new IllegalDomainArgumentException("Avatar dimensions cannot exceed 2000x2000 pixels");
                }
            }
            this.avatar = new ArrayList<>(newAvatars);
            isUpdated = true;
        }

        // Date of birth validation and update
        if (newDateOfBirth != null) {
            if (newDateOfBirth.isAfter(LocalDate.now())) {
                throw new IllegalDomainArgumentException("Date of birth cannot be in the future");
            }
            if (ChronoUnit.YEARS.between(newDateOfBirth, LocalDate.now()) < 13) {
                throw new IllegalDomainArgumentException("User must be at least 13 years old");
            }
            this.dateOfBirth = newDateOfBirth;
            isUpdated = true;
        }

        // Gender validation and update
        if (newGender != null) {
            this.gender = newGender;
            isUpdated = true;
        }

        // Register event if any updates were made
        if (isUpdated) {
            registerEvents(new ProfileUpdatedEvent(
                this.getId().toString(),
                this.displayName,
                this.avatar,
                this.dateOfBirth,
                this.gender
            ));
        }
    }

    public void verifyEmail(String tokenValue) {
        if (isVerified()) {
            throw new AuthenticationException("Account is already verified.", ErrorCode.INVALID_REQUEST);
        }
        Iterator<Token> iterator = verificationTokens.iterator();
        while (iterator.hasNext()) {
            Token token = iterator.next();
            if (token.value().equals(tokenValue)) {
                if (token.isValid()) {
                    this.verifiedAt = LocalDateTime.now();
                    iterator.remove();
                    registerEvents(new AccountVerifiedEvent(this.getId(), this.username, this.email, this.verifiedAt));
                    return;
                } else {
                    throw new AuthenticationException("Token is expired.", ErrorCode.TOKEN_INVALID);
                }
            }
        }
        throw new TokenInvalidException("Invalid verification token.");
    }
    public void AuthenticateByToken(String tokenValue) {
        if (authenticationToken != null && authenticationToken.value().equals(tokenValue)) {
            authenticationToken = null;
            return;
        }
        throw new TokenInvalidException("Invalid authentication token");
    }

    public void addVerificationToken(String tokenValue, int age, ChronoUnit unit) {
        if (age < 0) throw new IllegalDomainArgumentException("Token age must greater than 0");
        if (verificationTokens.size() >= MAX_TOKENS_SIZE)
            throw new IllegalDomainStateException("Verification tokens is overflow");
        LocalDateTime now = LocalDateTime.now();
        verificationTokens.add(new Token(tokenValue, now, now.plus(age, unit)));
        registerEvents(new VerifyTokenAddedEvent(getId(), email, tokenValue, age));
    }

    public void addResetPasswordToken(String tokenValue, int age, ChronoUnit unit) {
        if (age < 0) throw new IllegalDomainArgumentException("Token age must greater than 0");
        if (resetPasswordTokens.size() >= MAX_TOKENS_SIZE)
            throw new IllegalDomainStateException("Verification tokens is overflow");
        LocalDateTime now = LocalDateTime.now();
        resetPasswordTokens.add(new Token(tokenValue, now, now.plus(age, unit)));
        registerEvents(new ResetPasswordTokenAddedEvent(getId(), email, tokenValue, age));
    }

    public void setAuthenticationToken(String tokenValue, int age, ChronoUnit unit) {

        if (age < 0) throw new IllegalDomainArgumentException("Token age must greater than 0");
        LocalDateTime now = LocalDateTime.now();
        authenticationToken = new Token(tokenValue, now, now.plus(age, unit));
        registerEvents(new AuthenticationTokenAddedEvent(getId(), email, tokenValue, age));
    }

    private boolean isVerified() {
        return verifiedAt != null;
    }

    public void changeAvatar(List<Image> avatar) {
        Assert.notNull(avatar, "Avatar must not be null");
        this.avatar = avatar;
        registerEvents(new AvatarChangedEvents(this.getId(), avatar));
    }

    public Set<Device> getDevices() {
        return UnmodifiableSet.unmodifiableSet(devices);
    }

    public Collection<? extends GrantedAuthority> authorities() {
        return roles.stream().map(Role::getGrantedAuthorities).flatMap(Collection::stream).collect(Collectors.toList())  ;
    }
}