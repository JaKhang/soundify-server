package com.soundify.server.account.application.queries.handler;

import com.soundify.server.account.application.dto.PrincipalResponse;
import com.soundify.server.account.application.mapper.AccountMapper;
import com.soundify.server.account.application.mapper.DeviceMapper;
import com.soundify.server.account.application.queries.GetPrincipalQuery;
import com.soundify.server.account.infrastructure.persistence.AccountJPARepository;
import com.soundify.server.account.infrastructure.persistence.DeviceJPARepository;
import com.soundify.server.shared.mediator.Handler;
import com.soundify.server.shared.mediator.RequestHandler;
import lombok.RequiredArgsConstructor;

@Handler
@RequiredArgsConstructor
public class GetPrincipalQueryHandler implements RequestHandler<GetPrincipalQuery, PrincipalResponse> {
    private final AccountJPARepository repository;
    private final AccountMapper mapper;
    @Override
    public PrincipalResponse handle(GetPrincipalQuery request) {
        return null;
    }
}
