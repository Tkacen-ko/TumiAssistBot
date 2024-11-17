package com.tkachenko.BasicTelegramBot.service.init;

import com.tkachenko.BasicTelegramBot.model.finance.general.Currency;
import com.tkachenko.BasicTelegramBot.model.finance.organization.AccountType;
import com.tkachenko.BasicTelegramBot.model.finance.organization.Country;
import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import com.tkachenko.BasicTelegramBot.model.finance.organization.TypeOrganization;
import com.tkachenko.BasicTelegramBot.repository.finance.general.CurrencyRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.AccountTypeRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.CountryRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.FinancialOrganizationRepository;
import com.tkachenko.BasicTelegramBot.repository.finance.organization.TypeOrganizationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultDataLoader {

    private final CurrencyRepository currencyRepository;
    private final CountryRepository countryRepository;
    private final TypeOrganizationRepository typeOrganizationRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final FinancialOrganizationRepository financialOrganizationRepository;

    @PostConstruct
    public void loadDefaultData() {
        loadCurrencies();
        loadCountries();
        loadTypeOrganizations();
        loadAccountTypes();
        loadFinancialOrganizations();
    }

    private void loadCurrencies() {
        if (currencyRepository.count() == 0) {
            List<Currency> currencies = List.of(
                    new Currency(null, "USD"),
                    new Currency(null, "EUR"),
                    new Currency(null, "RUB")
            );
            currencyRepository.saveAll(currencies);
        }
    }

    private void loadCountries() {
        if (countryRepository.count() == 0) {
            List<Country> countries = List.of(
                    new Country(null, "USA"),
                    new Country(null, "Germany"),
                    new Country(null, "Russia")
            );
            countryRepository.saveAll(countries);
        }
    }

    private void loadTypeOrganizations() {
        if (typeOrganizationRepository.count() == 0) {
            List<TypeOrganization> types = List.of(
                    new TypeOrganization(null, "Bank"),
                    new TypeOrganization(null, "Crypto Exchange"),
                    new TypeOrganization(null, "Brokerage")
            );
            typeOrganizationRepository.saveAll(types);
        }
    }

    private void loadAccountTypes() {
        if (accountTypeRepository.count() == 0) {
            List<AccountType> accountTypes = List.of(
                    new AccountType(null, "Savings Account"),
                    new AccountType(null, "Brokerage Account"),
                    new AccountType(null, "Debit Account")
            );
            accountTypeRepository.saveAll(accountTypes);
        }
    }

    private void loadFinancialOrganizations() {
        if (financialOrganizationRepository.count() == 0) {
            List<FinancialOrganization> organizations = List.of(
                    new FinancialOrganization(null, "Bank of America", new TypeOrganization(1L, null), new Country(1L, null)),
                    new FinancialOrganization(null, "Binance", new TypeOrganization(2L, null), new Country(2L, null)),
                    new FinancialOrganization(null, "Charles Schwab", new TypeOrganization(3L, null), new Country(1L, null))
            );
            financialOrganizationRepository.saveAll(organizations);
        }
    }
}