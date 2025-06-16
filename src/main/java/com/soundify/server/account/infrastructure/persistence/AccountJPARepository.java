package com.soundify.server.account.infrastructure.persistence;

import com.soundify.server.account.domain.models.Account;
import com.soundify.server.account.domain.models.AccountDomainRepository;
import com.soundify.server.shared.domain.Id;
import com.soundify.server.account.application.exceptions.AuthenticationException;
import com.soundify.server.shared.exceptions.ErrorCode;
import com.soundify.server.shared.exceptions.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;


public interface AccountJPARepository extends JpaRepository<Account, Id>, AccountDomainRepository {

    @Query("SELECT a FROM Account a " +
            "LEFT JOIN FETCH a.avatar " +
            "LEFT JOIN FETCH a.verificationTokens " +
            "LEFT JOIN FETCH a.resetPasswordTokens " +
            "LEFT JOIN FETCH a.roles " +
            "LEFT JOIN FETCH a.devices " +
            "WHERE a.id = :id")
    Optional<Account> findByIdWithRelationships(Id id);

    @Override
    default Account findAggregate(Id id) {
        return findByIdWithRelationships(id).orElseThrow(() -> new ResourceNotFoundException("Not fount account with account.id:" + id ));
    }

    @Override
    default void deleteAggregate(Id id){

    }

    @Override
    default Account findAggregateByUsernameOrEmail(String s) {
        return findByUsernameOrEmail(s).orElseThrow(() -> new UsernameNotFoundException("Not fount account with username or email:" + s));
    }

    @Override
    default Account saveAggregate(Account account) {
        return this.save(account);
    }

    @Query("SELECT a FROM Account a " +
            "LEFT JOIN FETCH a.roles " +
            "LEFT JOIN FETCH a.devices " +
            "WHERE a.username = :usernameOrEmail or a.email = :usernameOrEmail")
    Optional<Account> findByUsernameOrEmail(String usernameOrEmail);

    boolean existsByUsername(String username);
}
