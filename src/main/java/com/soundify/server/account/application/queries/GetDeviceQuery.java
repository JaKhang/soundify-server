package com.soundify.server.account.application.queries;

import com.soundify.server.account.application.dto.DeviceResponse;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.mediator.MediatorRequest;

import java.util.Set;

public record GetDeviceQuery(
        Id accountId,
        Id currentDeviceId
) implements MediatorRequest<Set<DeviceResponse>> {
}
