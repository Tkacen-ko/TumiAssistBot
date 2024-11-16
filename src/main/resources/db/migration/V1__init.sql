-- Создание основной таблицы с пользователями Telegram
create table if not exists user_telegram(
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT UNIQUE NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    user_name VARCHAR(255)
);

-- Создание таблицы организаций
CREATE TABLE IF NOT EXISTS organization (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) unique not null
);

-- Создание таблицы типов счетов
CREATE TABLE IF NOT EXISTS account_type (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) unique not null
);

-- Создание таблицы классификаций
CREATE TABLE IF NOT EXISTS classification (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) unique not null
);

-- Создание таблицы стран
CREATE TABLE IF NOT EXISTS country (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) unique not null
);

-- Создание таблицы счетов пользователей
CREATE TABLE IF NOT EXISTS account (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) unique not null,
    user_id BIGINT REFERENCES user_telegram(id) ON DELETE CASCADE,
    total_money REAL NOT NULL,
    organization_id BIGINT REFERENCES organization(id),
    type_id BIGINT REFERENCES account_type(id),
    classification_id BIGINT REFERENCES classification(id),
    country_id BIGINT REFERENCES country(id)
);

-- Предзаполнение таблицы организаций
INSERT INTO organization (title) VALUES ('ТДС'), ('Тинёк'), ('Freedom'), ('Kaspi'), ('ByBit'), ('OKX')
ON CONFLICT DO NOTHING;

-- Предзаполнение таблицы типов счетов
INSERT INTO account_type (title) VALUES ('Наличка'), ('Счета Банк'), ('Счёт %'), ('Крипта'), ('ETF')
ON CONFLICT DO NOTHING;

-- Предзаполнение таблицы классификаций
INSERT INTO classification (title) VALUES ('Текучка'), ('Подушка'), ('Спекуляции'), ('Инвестиции')
ON CONFLICT DO NOTHING;

-- Предзаполнение таблицы стран
INSERT INTO country (title) VALUES ('РФ'), ('США'), ('Казахстан'), ('ОАЭ'), ('Гонконг')
ON CONFLICT DO NOTHING;