package com.tkachenko.BasicTelegramBot.service.init;

import com.tkachenko.BasicTelegramBot.model.finance.expenses.ExpenseType;
import com.tkachenko.BasicTelegramBot.model.finance.general.Currency;
import com.tkachenko.BasicTelegramBot.model.finance.organization.AccountType;
import com.tkachenko.BasicTelegramBot.model.finance.organization.Country;
import com.tkachenko.BasicTelegramBot.model.finance.organization.FinancialOrganization;
import com.tkachenko.BasicTelegramBot.model.finance.organization.TypeOrganization;
import com.tkachenko.BasicTelegramBot.repository.finance.expenses.ExpenseTypeRepository;
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
    private final ExpenseTypeRepository expenseTypeRepository;

    @PostConstruct
    public void loadDefaultData() {
        loadCurrencies();
        loadCountries();
        loadTypeOrganizations();
        loadAccountTypes();
        loadFinancialOrganizations();
        loadExpenseTypes();
    }

    private void loadCurrencies() {
        if (currencyRepository.count() == 0) {
            List<Currency> currencies = List.of(
                    new Currency(null, "USD"),
                    new Currency(null, "EUR"),
                    new Currency(null, "RUB"),
                    new Currency(null, "KZT")
            );
            currencyRepository.saveAll(currencies);
        }
    }

    private void loadCountries() {
        if (countryRepository.count() == 0) {
            List<Country> countries = List.of(
                    new Country(null, "Russia"),
                    new Country(null, "USA"),
                    new Country(null, "Kazakhstan"),
                    new Country(null, "Georgia"),
                    new Country(null, "China"),
                    new Country(null, "Cyprus"),
                    new Country(null, "Germany")

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
                    // Банки России
                    new FinancialOrganization(null, "Тинькофф", new TypeOrganization(1L, null), new Country(1L, null), "Tin"),
                    new FinancialOrganization(null, "Сбербанк", new TypeOrganization(1L, null), new Country(1L, null), "Sber"),
                    new FinancialOrganization(null, "Альфа-Банк", new TypeOrganization(1L, null), new Country(1L, null), "Alpha"),
                    new FinancialOrganization(null, "ВТБ", new TypeOrganization(1L, null), new Country(1L, null), "VTB"),
                    new FinancialOrganization(null, "Газпромбанк", new TypeOrganization(1L, null), new Country(1L, null), "Gaz"),
                    new FinancialOrganization(null, "Росбанк", new TypeOrganization(1L, null), new Country(1L, null), "Ros"),
                    new FinancialOrganization(null, "Открытие", new TypeOrganization(1L, null), new Country(1L, null), "Otk"),

                    // Казахстан
                    new FinancialOrganization(null, "Freedom Finance", new TypeOrganization(3L, null), new Country(3L, null), "Free"),
                    new FinancialOrganization(null, "Kaspi", new TypeOrganization(1L, null), new Country(3L, null), "Kaspi"),

                    // Криптобиржи
                    new FinancialOrganization(null, "Binance", new TypeOrganization(2L, null), new Country(5L, null), "Bi"),
                    new FinancialOrganization(null, "OKX", new TypeOrganization(2L, null), new Country(5L, null), "OKX"),
                    new FinancialOrganization(null, "ByBit", new TypeOrganization(2L, null), new Country(5L, null), "ByB"),

                    // Брокеры
                    new FinancialOrganization(null, "J2T", new TypeOrganization(3L, null), new Country(4L, null), "J2T"),
                    new FinancialOrganization(null, "Interactive Brokers", new TypeOrganization(3L, null), new Country(7L, null), "IBKR"),
                    new FinancialOrganization(null, "Charles Schwab", new TypeOrganization(3L, null), new Country(2L, null), "CS")
            );
            financialOrganizationRepository.saveAll(organizations);
        }
    }

    private void loadExpenseTypes() {
        if (expenseTypeRepository.count() == 0) {
            List<ExpenseType> expenseTypes = List.of(
                    new ExpenseType(null, "Продукты питания", "П"),
                    new ExpenseType(null, "Развлечения", "Р"),
                    new ExpenseType(null, "Одежда", "О"),
                    new ExpenseType(null, "Коммунальные платежи", "Ком"),
                    new ExpenseType(null, "Транспорт", "Тр"),
                    new ExpenseType(null, "Медицина", "М"),
                    new ExpenseType(null, "Прочее", "П")
            );
            expenseTypeRepository.saveAll(expenseTypes);
        }
    }

}