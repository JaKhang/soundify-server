package com.soundify.server.account.application.mapper;

import com.soundify.server.account.application.dto.PrincipalResponse;
import com.soundify.server.account.domain.models.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "dob", source = "account.dateOfBirth")
    @Mapping(target = "name", source = "account.displayName")
    PrincipalResponse toPrincipal(Account account);


}
