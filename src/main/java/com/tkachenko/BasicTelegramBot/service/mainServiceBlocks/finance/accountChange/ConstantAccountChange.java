package com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.finance.accountChange;

public class ConstantAccountChange {
    public final static String FINANCIAL_ACCOUNT = "financialAccount";
    public final static String EXPENSE_TYPE = "expenseType";
    public final static String ERROR_CHANGING_ACCOUNT_BALANCE = """
*Ошибка изменения баланса счета*:

Символы `+` и `-` зарезервированы для операций пополнения и снятия средств.  
Команда была введена некорректно, возможно, присутствуют лишние символы.

*Пример корректного ввода для изменения баланса*:
- `+100` — пополнение счета на 100 единиц.
- `-3444` — списание 3444 единиц.

*Расширенный формат команды*:
- Вы можете указать дополнительные параметры для быстрого изменения баланса. Например:  
  `-351 Ti П`  
  - `-351` — сумма изменения на счёте.  
  - `Ti` — короткое название компании.  
  - `П` — категория расхода (например, "Покупка продуктов").

В приведённом примере команда обозначает покупку продуктов картой Тинькофф на сумму 351 единица. Валюта определяется в соответствии с указанной для счета.

Попробуйте выполнить операцию ещё раз или введите другую команду.  

Спасибо за активность в работе с ботом! 🙂
""";

    public final static String SELECT_ACCOUNT = "Выберете из списка ниже, счёт, баланс которого вы бы хотели изменить:\n";
    public final static String SELECT_EXPENSE_TYPE = "Выберете из списка ниже, тип расхода:\n";
}
