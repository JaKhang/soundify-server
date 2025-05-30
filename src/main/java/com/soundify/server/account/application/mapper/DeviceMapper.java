package com.soundify.server.account.application.mapper;

import com.soundify.server.account.application.dto.DeviceResponse;
import com.soundify.server.account.domain.models.Device;
import com.soundify.server.shared.domain.Id;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(target = "id", source = "device.id")
    @Mapping(target = "ip", source = "device.ip")
    @Mapping(target = "os", source = "device.os")
    @Mapping(target = "platform", source = "device.platform")
    @Mapping(target = "isCurrent", source = ".", qualifiedByName = "isCurrent")
    DeviceResponse toResponse(Device device, @Context Id currentDevice);

    Set<DeviceResponse> toResponses(Set<Device> devices, @Context Id currentDevice);

    @Named("isCurrent")
    default boolean isCurrent(Device device, @Context Id currentDevice) {
        return device.getId().equals(currentDevice);
    }
}