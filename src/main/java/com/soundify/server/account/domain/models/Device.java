package com.soundify.server.account.domain.models;

import com.soundify.server.shared.domain.AbstractEntity;
import lombok.Setter;

import java.time.LocalDateTime;

public class Device extends AbstractEntity {
    String os;
    String ip;
    String platform;
    LocalDateTime loginAt;
}
