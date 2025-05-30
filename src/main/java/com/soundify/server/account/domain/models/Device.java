package com.soundify.server.account.domain.models;

import com.soundify.server.shared.domain.AbstractEntity;
import com.soundify.server.shared.domain.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "account_devices")
@NoArgsConstructor
@Getter
public class Device extends AbstractEntity {
    private String os;
    private String ip;
    private String platform;
    private LocalDateTime loginAt;
    private LocalDateTime expiredAt;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Device(Id id, String os, String ip, String platform, LocalDateTime loginAt, LocalDateTime expiredAt, Account account) {
        super(id);
        validateConstructorParameters(os, ip, platform, loginAt, expiredAt, account);
        this.os = os;
        this.ip = ip;
        this.platform = platform;
        this.loginAt = loginAt;
        this.expiredAt = expiredAt;
        this.account = account;
    }



    private void validateConstructorParameters(String os, String ip, String platform, 
                                            LocalDateTime loginAt, LocalDateTime expiredAt, 
                                            Account account) {
        // ID validation is handled by AbstractEntity constructor
        
        // OS validation
        Assert.hasText(os, "OS cannot be null or empty");
        if (os.length() > 50) {
            throw new IllegalArgumentException("OS name cannot exceed 50 characters");
        }

        // IP validation
        Assert.hasText(ip, "IP address cannot be null or empty");
        String ipv4Regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        String ipv6Regex = "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";
        if (!ip.matches(ipv4Regex) && !ip.matches(ipv6Regex)) {
            throw new IllegalArgumentException("Invalid IP address format");
        }

        // Platform validation
        Assert.hasText(platform, "Platform cannot be null or empty");
        if (platform.length() > 50) {
            throw new IllegalArgumentException("Platform name cannot exceed 50 characters");
        }

        // Login time validation
        Assert.notNull(loginAt, "Login time cannot be null");
        if (loginAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Login time cannot be in the future");
        }

        // Expiration time validation
        Assert.notNull(expiredAt, "Expiration time cannot be null");
        if (expiredAt.isBefore(loginAt)) {
            throw new IllegalArgumentException("Expiration time must be after login time");
        }

        // Account validation
        Assert.notNull(account, "Account cannot be null");
    }

    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiredAt);
    }

    public boolean isExpired() {
        return !isValid();
    }

    public void extendValidity(LocalDateTime newExpirationTime) {
        Assert.notNull(newExpirationTime, "New expiration time cannot be null");
        if (newExpirationTime.isBefore(loginAt)) {
            throw new IllegalArgumentException("New expiration time must be after login time");
        }
        this.expiredAt = newExpirationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(getId(), device.getId()) &&
               Objects.equals(os, device.os) &&
               Objects.equals(ip, device.ip) &&
               Objects.equals(platform, device.platform) &&
               Objects.equals(loginAt, device.loginAt) &&
               Objects.equals(expiredAt, device.expiredAt) &&
               Objects.equals(account.getId(), device.account.getId());
    }

    @Override
    public String toString() {
        return id.toString();
    }
}