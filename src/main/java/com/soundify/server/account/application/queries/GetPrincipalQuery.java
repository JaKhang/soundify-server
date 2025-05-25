package com.soundify.server.account.application.queries;

import com.soundify.server.account.application.dto.PrincipalResponse;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.shared.mediator.MediatorRequest;

public record GetPrincipalQuery(
        Id accountId
) implements MediatorRequest<PrincipalResponse> {
}
