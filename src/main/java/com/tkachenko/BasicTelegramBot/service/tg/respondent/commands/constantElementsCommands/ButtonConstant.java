package com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands;

import com.tkachenko.BasicTelegramBot.service.tg.ConstantTgBot;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ButtonConstant {
    public static final String CANCEL_CREATION_COMMANDS = "@cancelCreation";

    public static final String GET_INFO_FINANCE_BUTTON_CODE = "@getInfoFinance";
    public static final String GET_INFO_HEALTH_BUTTON_CODE = "@getInfoHealth";
    public static final String GET_INFO_MENTAL_BUTTON_CODE = "@getInfoMental";

    public static final String CHANGE_ACCOUNT_CODE = "@changeAccount";
    public static final String ENTERING_INCOME_EXPENSE_CODE = "@enteringIncomeExpense";
    public static final String ANALYSIS_FINANCE_CODE = "@analysisFinance";

    public static final String ADD_HEALTH_DATA_CODE = "@addHealthData";
    public static final String AUTOMATE_DATA_COLLECTION_CODE = "@automateDataCollection";
    public static final String HEALTH_ANALYSIS_CODE = "@analysisHealth";

    public static final String THOUGHT_RECORDINGS_CODE = "@thoughtRecordings";
    public static final String EMOTIONAL_DIARY_CODE = "@emotionalDiary";
    public static final String ANALYSIS_AND_SUPPORT_CODE = "@analysisAndSupport";

    public static final String CREATE_ACCOUNT_COMMANDS = "@createAccount";
    public static final String DELETE_ACCOUNT_COMMANDS = "@deleteAccount";
    public static final String EDIT_ACCOUNT_COMMANDS = "@editAccount";

    public static final String WEEK_ANALYSIS_FINANCE_COMMANDS = "@weekAnalysis";
    public static final String MONTH_ANALYSIS_FINANCE_COMMANDS = "@monthAnalysis";
    public static final String YEAR_ANALYSIS_FINANCE_COMMANDS = "@yearAnalysis";
    public static final String AI_ANALYSIS_FINANCE_COMMANDS = "@AIFinanceAnalysis";

    static {
        Map<String, String> cancelButton = new LinkedHashMap<>();
        Map<String, String> startButton = new LinkedHashMap<>();
        Map<String, String> financeButton = new LinkedHashMap<>();
        Map<String, String> physicalHealthButton = new LinkedHashMap<>();
        Map<String, String> mentalHealthButton = new LinkedHashMap<>();
        Map<String, String> changeAccount = new LinkedHashMap<>();
        Map<String, String> analyzingExpensesAndIncome = new LinkedHashMap<>();

        cancelButton.put(TitleButtonConstant.CANCEL_TITLE, CANCEL_CREATION_COMMANDS);

        startButton.put(TitleButtonConstant.FINANCE, GET_INFO_FINANCE_BUTTON_CODE);
        startButton.put(TitleButtonConstant.PHYSICAL_HEALTH, GET_INFO_HEALTH_BUTTON_CODE);
        startButton.put(TitleButtonConstant.MENTAL_HEALTH, GET_INFO_MENTAL_BUTTON_CODE);

        financeButton.put(TitleButtonConstant.CHANGE_ACCOUNT, CHANGE_ACCOUNT_CODE);
        financeButton.put(TitleButtonConstant.ENTERING_INCOME_EXPENSE, ENTERING_INCOME_EXPENSE_CODE);
        financeButton.put(TitleButtonConstant.ANALYSIS_FINANCE, ANALYSIS_FINANCE_CODE);

        physicalHealthButton.put(TitleButtonConstant.ADD_HEALTH_DATA, ADD_HEALTH_DATA_CODE);
        physicalHealthButton.put(TitleButtonConstant.AUTOMATE_DATA_COLLECTION, AUTOMATE_DATA_COLLECTION_CODE);
        physicalHealthButton.put(TitleButtonConstant.HEALTH_ANALYSIS, HEALTH_ANALYSIS_CODE);

        mentalHealthButton.put(TitleButtonConstant.THOUGHT_RECORDINGS, THOUGHT_RECORDINGS_CODE);
        mentalHealthButton.put(TitleButtonConstant.EMOTIONAL_DIARY, EMOTIONAL_DIARY_CODE);
        mentalHealthButton.put(TitleButtonConstant.ANALYSIS_AND_SUPPORT, ANALYSIS_AND_SUPPORT_CODE);

        changeAccount.put(TitleButtonConstant.CREATE_ACCOUNT, CREATE_ACCOUNT_COMMANDS);
        changeAccount.put(TitleButtonConstant.DELETE_ACCOUNT, DELETE_ACCOUNT_COMMANDS);
        changeAccount.put(TitleButtonConstant.EDIT_ACCOUNT_COMMANDS, EDIT_ACCOUNT_COMMANDS);

        analyzingExpensesAndIncome.put(TitleButtonConstant.WEEK, WEEK_ANALYSIS_FINANCE_COMMANDS);
        analyzingExpensesAndIncome.put(TitleButtonConstant.MONTH, MONTH_ANALYSIS_FINANCE_COMMANDS);
        analyzingExpensesAndIncome.put(TitleButtonConstant.YEAR, YEAR_ANALYSIS_FINANCE_COMMANDS);
        analyzingExpensesAndIncome.put(TitleButtonConstant.AI_ANALYSIS_FINANCE, AI_ANALYSIS_FINANCE_COMMANDS);

        CANCEL_CREATION = Collections.unmodifiableMap(startButton);
        START_BLOCK = Collections.unmodifiableMap(startButton);
        FINANCE_BLOCK = Collections.unmodifiableMap(financeButton);
        PHYSICAL_HEALTH = Collections.unmodifiableMap(physicalHealthButton);
        MENTAL_HEALTH = Collections.unmodifiableMap(mentalHealthButton);
        CHANGE_ACCOUNT = Collections.unmodifiableMap(changeAccount);
        OPTIONS_FOR_ANALYZING_EXPENSES_AND_INCOME = Collections.unmodifiableMap(analyzingExpensesAndIncome);
    }

    public static final Map<String, String> CANCEL_CREATION;

    public static final Map<String, String> START_BLOCK;

    public static final Map<String, String> FINANCE_BLOCK;

    public static final Map<String, String> PHYSICAL_HEALTH;

    public static final Map<String, String> MENTAL_HEALTH;

    public static final Map<String, String> CHANGE_ACCOUNT;

    public static final Map<String, String> OPTIONS_FOR_ANALYZING_EXPENSES_AND_INCOME;
}
