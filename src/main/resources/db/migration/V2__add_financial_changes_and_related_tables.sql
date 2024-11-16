-- Создание таблицы с валютами
create table if not exists currency(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) unique not null
);

-- Добавление начальных данных в таблицу валют
INSERT INTO currency (title) VALUES
('USD'),
('EUR'),
('RUB') ON CONFLICT (title) DO NOTHING;

-- Создание таблицы с типами трат
CREATE TABLE if not exists expense_type (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) unique not null
);

-- Добавление начальных данных в таблицу типов трат
INSERT INTO expense_type (title) VALUES
('Продукты'),
('Развлечения'),
('Зарплата')
ON CONFLICT DO NOTHING;

-- Создание таблицы финансовых изменений
CREATE TABLE financial_change (
    id BIGSERIAL PRIMARY KEY,
    amount REAL NOT NULL, -- Сумма изменений
    user_id BIGINT NOT NULL, -- Ссылка на пользователя
    currency_id BIGINT NOT NULL, -- Ссылка на валюту
    expense_type_id BIGINT NOT NULL, -- Тип траты
    FOREIGN KEY (user_id) REFERENCES user_telegram (id) ON DELETE CASCADE,
    FOREIGN KEY (currency_id) REFERENCES currency (id),
    FOREIGN KEY (expense_type_id) REFERENCES expense_type (id)
);

-- Изменение таблицы user_account: добавление ссылок на пользователя и валюту
ALTER TABLE account ADD COLUMN currency_id BIGINT NOT NULL,
ADD CONSTRAINT fk_currency FOREIGN KEY (currency_id) REFERENCES currency (id);