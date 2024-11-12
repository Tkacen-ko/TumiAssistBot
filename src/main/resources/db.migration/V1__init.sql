-- Создание основной таблицы с пользователями Telegram
create table if not exists "public".user_telegram(
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT UNIQUE NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    user_name VARCHAR(255)
);

-- Создание таблицы организаций
CREATE TABLE IF NOT EXISTS "public".organization (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) unique not null
);

-- Создание таблицы типов счетов
CREATE TABLE IF NOT EXISTS "public".account_type (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) unique not null
);

-- Создание таблицы классификаций
CREATE TABLE IF NOT EXISTS "public".classification (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) unique not null
);

-- Создание таблицы стран
CREATE TABLE IF NOT EXISTS "public".country (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) unique not null
);

-- Создание таблицы счетов пользователей
CREATE TABLE IF NOT EXISTS "public".user_account (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) unique not null,
    user_id BIGINT REFERENCES "public".user_telegram(id) ON DELETE CASCADE,
    total_money DECIMAL(15, 2) NOT NULL,
    organization_id BIGINT REFERENCES "public".organization(id),
    account_type_id BIGINT REFERENCES "public".account_type(id),
    classification_id BIGINT REFERENCES "public".classification(id),
    country_id BIGINT REFERENCES "public".country(id)
);

-- Предзаполнение таблицы организаций
INSERT INTO "public".organization (title) VALUES ('ТДС'), ('Тинёк'), ('Freedom'), ('Kaspi'), ('ByBit'), ('OKX')
ON CONFLICT DO NOTHING;

-- Предзаполнение таблицы типов счетов
INSERT INTO "public".account_type (title) VALUES ('Наличка'), ('Счета_Банк'), ('Счёт_%'), ('Крипта'), ('ETF')
ON CONFLICT DO NOTHING;

-- Предзаполнение таблицы классификаций
INSERT INTO "public".classification (title) VALUES ('Текучка'), ('Подушка'), ('Спекуляции'), ('Инвестиции')
ON CONFLICT DO NOTHING;

-- Предзаполнение таблицы стран
INSERT INTO "public".country (title) VALUES ('РФ'), ('США'), ('Казахстан'), ('ОАЭ'), ('Гонконг')
ON CONFLICT DO NOTHING;