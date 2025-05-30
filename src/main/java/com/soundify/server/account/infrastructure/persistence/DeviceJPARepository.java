package com.soundify.server.account.infrastructure.persistence;

import com.soundify.server.account.domain.models.Device;
import com.soundify.server.shared.domain.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DeviceJPARepository extends JpaRepository<Device, Id> {


    Set<Device> findAllByAccountId(Id accountId);
}
