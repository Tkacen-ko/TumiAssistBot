package com.tkachenko.BasicTelegramBot.repository;

import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.finance.financialAccount.FinancialAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserTelegram, Long> {
    Optional<UserTelegram> findByChatIdAndUserName(Long chatId, String userName);

    @Query("SELECT u FROM UserTelegram u " +
            "JOIN FETCH u.financialAccounts f " +
            "JOIN FETCH f.financialOrganization " +
            "WHERE u.chatId = :chatId")
    Optional<UserTelegram> findByChatIdWithAccountsAndOrganizations(@Param("chatId") Long chatId);

    Optional<UserTelegram> findByChatId(Long chatId);

    @Query("SELECT fa FROM UserTelegram ut " +
            "JOIN ut.financialAccounts fa " +
            "JOIN fa.currency cu " +
            "JOIN fa.financialOrganization fo " +
            "WHERE ut.id = :userId AND fo.title = :organizationTitle")
    Optional<FinancialAccount> findFinancialAccountByUserAndOrganizationTitle(@Param("userId") Long userId,
                                                                              @Param("organizationTitle") String organizationTitle);
}