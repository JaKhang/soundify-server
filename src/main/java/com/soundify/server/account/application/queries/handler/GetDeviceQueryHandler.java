package com.soundify.server.account.application.queries.handler;

import com.soundify.server.account.application.dto.DeviceResponse;
import com.soundify.server.account.application.mapper.DeviceMapper;
import com.soundify.server.account.application.queries.GetDeviceQuery;
import com.soundify.server.account.domain.models.Device;
import com.soundify.server.account.infrastructure.persistence.DeviceJPARepository;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.RequestHandler;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Handler
@RequiredArgsConstructor
public class GetDeviceQueryHandler implements RequestHandler<GetDeviceQuery, Set<DeviceResponse>> {
    private final DeviceJPARepository repository;
    private final DeviceMapper mapper;

    @Override
    public Set<DeviceResponse> handle(GetDeviceQuery request) {
        Set<Device> devices = repository.findAllByAccountId(request.accountId());
        return mapper.toResponses(devices, request.currentDeviceId());
    }
}
