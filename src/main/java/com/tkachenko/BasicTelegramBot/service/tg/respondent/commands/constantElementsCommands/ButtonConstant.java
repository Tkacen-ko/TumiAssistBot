package com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ButtonConstant {
    public static final String CANCEL_CREATION_BUTTON_CODE = "@cancelCreation";

    public static final String GET_INFO_FINANCE_BUTTON_CODE = "@getInfoFinance";
    public static final String GET_INFO_HEALTH_BUTTON_CODE = "@getInfoHealth";
    public static final String GET_INFO_MENTAL_BUTTON_CODE = "@getInfoMental";

    public static final String CHANGE_ACCOUNT_BUTTON_CODE = "@changeAccount";
    public static final String ENTERING_INCOME_EXPENSE_BUTTON_CODE = "@enteringIncomeE`xpense";
    public static final String ANALYSIS_FINANCE_BUTTON_CODE = "@analysisFinance";

    public static final String ADD_HEALTH_DATA_BUTTON_CODE = "@addHealthData";
    public static final String AUTOMATE_DATA_COLLECTION_BUTTON_CODE = "@automateDataCollection";
    public static final String HEALTH_ANALYSIS_BUTTON_CODE = "@analysisHealth";

    public static final String THOUGHT_RECORDINGS_BUTTON_CODE = "@thoughtRecordings";
    public static final String EMOTIONAL_DIARY_BUTTON_CODE = "@emotionalDiary";
    public static final String ANALYSIS_AND_SUPPORT_BUTTON_CODE = "@analysisAndSupport";

    public static final String CREATE_ACCOUNT_BUTTON_CODE = "@createAccount";
    public static final String DELETE_ACCOUNT_BUTTON_CODE = "@deleteAccount";
    public static final String EDIT_ACCOUNT_BUTTON_CODE = "@editAccount";

    public static final String WEEK_ANALYSIS_FINANCE_BUTTON_CODE = "@weekAnalysis";
    public static final String MONTH_ANALYSIS_FINANCE_BUTTON_CODE = "@monthAnalysis";
    public static final String YEAR_ANALYSIS_FINANCE_BUTTON_CODE = "@yearAnalysis";
    public static final String AI_ANALYSIS_FINANCE_BUTTON_CODE = "@AIFinanceAnalysis";

    public static final String SAVE_AS_THOUGHT_BUTTON_CODE = "@saveAsThought";
    public static final String QUESTION_TO_AI_BUTTON_CODE = "@questionToAI";
    public static final String INFO_BUTTON_CODE = "@info";

    public static final String SAVE_THOUGHT_DEFAULT_BUTTON_CODE = "@saveThoughtDefault";
    public static final String ADD_TAGS_AND_EMOTIONS_BUTTON_CODE = "@addTagsAndEmotions";

    static {
        Map<String, String> cancelButton = new LinkedHashMap<>();
        Map<String, String> startButton = new LinkedHashMap<>();
        Map<String, String> financeButton = new LinkedHashMap<>();
        Map<String, String> physicalHealthButton = new LinkedHashMap<>();
        Map<String, String> mentalHealthButton = new LinkedHashMap<>();
        Map<String, String> changeAccount = new LinkedHashMap<>();
        Map<String, String> analyzingExpensesAndIncome = new LinkedHashMap<>();
        Map<String, String> unknownMessage = new LinkedHashMap<>();
        Map<String, String> saveDefaultThought = new LinkedHashMap<>();

        cancelButton.put(TitleButtonConstant.CANCEL_TITLE, CANCEL_CREATION_BUTTON_CODE);

        startButton.put(TitleButtonConstant.FINANCE, GET_INFO_FINANCE_BUTTON_CODE);
        startButton.put(TitleButtonConstant.PHYSICAL_HEALTH, GET_INFO_HEALTH_BUTTON_CODE);
        startButton.put(TitleButtonConstant.MENTAL_HEALTH, GET_INFO_MENTAL_BUTTON_CODE);

        financeButton.put(TitleButtonConstant.CHANGE_ACCOUNT, CHANGE_ACCOUNT_BUTTON_CODE);
        financeButton.put(TitleButtonConstant.ENTERING_INCOME_EXPENSE, ENTERING_INCOME_EXPENSE_BUTTON_CODE);
        financeButton.put(TitleButtonConstant.ANALYSIS_FINANCE, ANALYSIS_FINANCE_BUTTON_CODE);

        physicalHealthButton.put(TitleButtonConstant.ADD_HEALTH_DATA, ADD_HEALTH_DATA_BUTTON_CODE);
        physicalHealthButton.put(TitleButtonConstant.AUTOMATE_DATA_COLLECTION, AUTOMATE_DATA_COLLECTION_BUTTON_CODE);
        physicalHealthButton.put(TitleButtonConstant.HEALTH_ANALYSIS, HEALTH_ANALYSIS_BUTTON_CODE);

        mentalHealthButton.put(TitleButtonConstant.THOUGHT_RECORDINGS, THOUGHT_RECORDINGS_BUTTON_CODE);
        mentalHealthButton.put(TitleButtonConstant.EMOTIONAL_DIARY, EMOTIONAL_DIARY_BUTTON_CODE);
        mentalHealthButton.put(TitleButtonConstant.ANALYSIS_AND_SUPPORT, ANALYSIS_AND_SUPPORT_BUTTON_CODE);

        changeAccount.put(TitleButtonConstant.CREATE_ACCOUNT, CREATE_ACCOUNT_BUTTON_CODE);
        changeAccount.put(TitleButtonConstant.DELETE_ACCOUNT, DELETE_ACCOUNT_BUTTON_CODE);
        changeAccount.put(TitleButtonConstant.EDIT_ACCOUNT_COMMANDS, EDIT_ACCOUNT_BUTTON_CODE);

        analyzingExpensesAndIncome.put(TitleButtonConstant.WEEK, WEEK_ANALYSIS_FINANCE_BUTTON_CODE);
        analyzingExpensesAndIncome.put(TitleButtonConstant.MONTH, MONTH_ANALYSIS_FINANCE_BUTTON_CODE);
        analyzingExpensesAndIncome.put(TitleButtonConstant.YEAR, YEAR_ANALYSIS_FINANCE_BUTTON_CODE);
        analyzingExpensesAndIncome.put(TitleButtonConstant.AI_ANALYSIS_FINANCE, AI_ANALYSIS_FINANCE_BUTTON_CODE);

        unknownMessage.put(TitleButtonConstant.SAVE_AS_THOUGHT, SAVE_AS_THOUGHT_BUTTON_CODE);
        unknownMessage.put(TitleButtonConstant.QUESTION_TO_AI, QUESTION_TO_AI_BUTTON_CODE);
        unknownMessage.put(TitleButtonConstant.INFO, INFO_BUTTON_CODE);

        saveDefaultThought.put(TitleButtonConstant.SAVE_THOUGHT_DEFAULT, SAVE_THOUGHT_DEFAULT_BUTTON_CODE);

        CANCEL_CREATION = Collections.unmodifiableMap(cancelButton);
        START_BLOCK = Collections.unmodifiableMap(startButton);
        FINANCE_BLOCK = Collections.unmodifiableMap(financeButton);
        PHYSICAL_HEALTH = Collections.unmodifiableMap(physicalHealthButton);
        MENTAL_HEALTH = Collections.unmodifiableMap(mentalHealthButton);
        CHANGE_ACCOUNT = Collections.unmodifiableMap(changeAccount);
        OPTIONS_FOR_ANALYZING_EXPENSES_AND_INCOME = Collections.unmodifiableMap(analyzingExpensesAndIncome);
        UNKNOWN_MESSAGE_FROM_USER = Collections.unmodifiableMap(unknownMessage);
        SAVE_THOUGHT_DEFAULT_TAGS = Collections.unmodifiableMap(saveDefaultThought);
    }

    public static final Map<String, String> CANCEL_CREATION;

    public static final Map<String, String> START_BLOCK;

    public static final Map<String, String> FINANCE_BLOCK;

    public static final Map<String, String> PHYSICAL_HEALTH;

    public static final Map<String, String> MENTAL_HEALTH;

    public static final Map<String, String> CHANGE_ACCOUNT;

    public static final Map<String, String> OPTIONS_FOR_ANALYZING_EXPENSES_AND_INCOME;

    public static final Map<String, String> UNKNOWN_MESSAGE_FROM_USER;

    public static final Map<String, String> SAVE_THOUGHT_DEFAULT_TAGS;
}
