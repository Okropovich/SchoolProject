-- ============================================
-- Домашнее задание 4.2, задание 2
-- Создание таблиц: человек (Person) и машина (Car)
-- Связь Many-to-Many: один человек может иметь несколько машин,
-- одна машина может принадлежать нескольким людям
-- ============================================


CREATE TABLE IF NOT EXISTS person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER CHECK (age >= 0),
    has_driver_license BOOLEAN DEFAULT FALSE
);


CREATE TABLE IF NOT EXISTS car (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) CHECK (price >= 0)
);


CREATE TABLE IF NOT EXISTS person_car (
    person_id INTEGER REFERENCES person(id) ON DELETE CASCADE,
    car_id INTEGER REFERENCES car(id) ON DELETE CASCADE,
    PRIMARY KEY (person_id, car_id)
);