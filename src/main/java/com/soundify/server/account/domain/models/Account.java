package com.soundify.server.account.domain.models;

import com.soundify.server.account.domain.events.DeviceRegisteredEvent;
import com.soundify.server.account.domain.events.DeviceUnregisteredEvent;
import com.soundify.server.account.domain.events.ProfileUpdatedEvent;
import com.soundify.server.shared.domain.AggregateRoot;
import com.soundify.server.shared.domain.Id;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;

@Entity
@Table(name = "account")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends AggregateRoot {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String displayName;
    private String avatar;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Locale locale;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private LocalDateTime verifiedAt;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "account_roles", joinColumns = @JoinColumn(name = "account_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Device> devices = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public Device registerDevice(String os, String ip, String platform) {
        if (this.devices.stream().anyMatch(d -> d.getIp().equals(ip))) {
            throw new IllegalStateException("Device with this IP is already registered.");
        }

        Device device = new Device(Id.fast() ,os, ip, platform, LocalDateTime.now(), this);
        this.devices.add(device);
        registerEvents(new DeviceRegisteredEvent(this.getId().toString(), os, ip, platform, device.getLoginAt()));
        return device;
    }

    public void unregisterDevice(Id id) {
        Device deviceToRemove = this.devices.stream()
                .filter(device -> device.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Device not found"));

        this.devices.remove(deviceToRemove);
        registerEvents(new DeviceUnregisteredEvent(this.getId().toString(), id));
    }


    public void changePassword(String currentPassword, String newPassword, PasswordEncoder passwordEncoder) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters long.");
        }

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, this.password)) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        // Encrypt and update password
        this.password = passwordEncoder.encode(newPassword);

        // Publish event
        registerEvents(new PasswordChangedEvent(this.getId().toString()));
    }



    public void updateProfile(String newDisplayName, String newAvatarUrl, LocalDate newDateOfBirth, Gender newGender) {
        boolean isUpdated = false;

        if (newDisplayName != null && !newDisplayName.isBlank()) {
            this.displayName = newDisplayName;
            isUpdated = true;
        }

        if (newAvatarUrl != null && !newAvatarUrl.isBlank()) {
            if (!newAvatarUrl.startsWith("http://") && !newAvatarUrl.startsWith("https://")) {
                throw new IllegalArgumentException("Invalid avatar URL format.");
            }
            this.avatar = newAvatarUrl;
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

        // ✅ Publish event only if changes were made
        if (isUpdated) {
            registerEvents(new ProfileUpdatedEvent(this.getId().toString(), this.displayName, this.avatar, this.dateOfBirth, this.gender));
        }
    }
}
