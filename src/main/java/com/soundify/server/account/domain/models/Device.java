package com.soundify.server.account.domain.models;

import com.soundify.server.shared.domain.AbstractEntity;
import com.soundify.server.shared.domain.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_devices")
@NoArgsConstructor
@Getter
public class Device extends AbstractEntity {
    private String os;
    private String ip;
    private String platform;
    private LocalDateTime loginAt;

    Device(Id id, String os, String ip, String platform, LocalDateTime loginAt, Account account) {
        super(id);
        this.os = os;
        this.ip = ip;
        this.platform = platform;
        this.loginAt = loginAt;
        this.account = account;
    }

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false) // Links device to an account
    private Account account;
}