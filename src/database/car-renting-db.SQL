-- pgAdmin SQL Dump

-- Tworzenie tabel
BEGIN;

CREATE TABLE public.samochody (
  samochod_id SERIAL PRIMARY KEY,
  marka VARCHAR(50) NOT NULL,
  model VARCHAR(50) NOT NULL,
  rok_produkcji INT NOT NULL,
  numer_rejestracyjny VARCHAR(15) NOT NULL,
  stan VARCHAR(20) NOT NULL,
  cena_dzienna DECIMAL(10,2) NOT NULL
);

CREATE TABLE public.klient (
  klient_id SERIAL PRIMARY KEY,
  imie VARCHAR(50) NOT NULL,
  nazwisko VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  telefon VARCHAR(15) NOT NULL,
  adres VARCHAR(255) NOT NULL
);

CREATE TABLE public.rezerwacje (
  rezerwacja_id SERIAL PRIMARY KEY,
  klient_id INT NOT NULL,
  samochod_id INT NOT NULL,
  data_rozpoczecia DATE NOT NULL,
  data_zakonczenia DATE NOT NULL,
  status VARCHAR(20) NOT NULL,
  FOREIGN KEY (klient_id) REFERENCES public.klient (klient_id),
  FOREIGN KEY (samochod_id) REFERENCES public.samochody (samochod_id)
);

CREATE TABLE public.transakcje (
  transakcja_id SERIAL PRIMARY KEY,
  rezerwacja_id INT NOT NULL,
  kwota DECIMAL(10,2) NOT NULL,
  data_transakcji DATE NOT NULL,
  metoda_platnosci VARCHAR(20) NOT NULL,
  FOREIGN KEY (rezerwacja_id) REFERENCES public.rezerwacje (rezerwacja_id)
);

CREATE TABLE public.uzytkownicy (
  uzytkownik_id SERIAL PRIMARY KEY,
  login VARCHAR(50) NOT NULL,
  haslo VARCHAR(255) NOT NULL,
  rola VARCHAR(20) NOT NULL
);

-- Wstawianie danych

INSERT INTO public.klient (imie, nazwisko, email, telefon, adres) VALUES
('Jan', 'Kowalski', 'jan.kowalski@example.com', '123456789', 'ul. Warszawska 1, 00-001 Warszawa'),
('Anna', 'Nowak', 'anna.nowak@example.com', '987654321', 'ul. Krakowska 2, 00-002 Kraków'),
('Piotr', 'Wiśniewski', 'piotr.wisniewski@example.com', '123123123', 'ul. Poznańska 3, 00-003 Poznań'),
('Katarzyna', 'Wójcik', 'katarzyna.wojcik@example.com', '321321321', 'ul. Gdańska 4, 00-004 Gdańsk'),
('Michał', 'Kowalczyk', 'michal.kowalczyk@example.com', '456456456', 'ul. Łódzka 5, 00-005 Łódź');

INSERT INTO public.samochody (marka, model, rok_produkcji, numer_rejestracyjny, stan, cena_dzienna) VALUES
('Ferrari', '488 Spider', 2018, 'WE12345', 'Doskonały', '2500.00'),
('Lamborghini', 'Huracan Evo', 2020, 'WE23456', 'Doskonały', '3000.00'),
('Porsche', '911 Carrera', 2019, 'WE34567', 'Doskonały', '2200.00'),
('McLaren', '720S', 2019, 'WE45678', 'Doskonały', '2800.00'),
('Audi', 'R8', 2020, 'WE56789', 'Doskonały', '2600.00'),
('BMW', 'i8', 2018, 'WE67890', 'Doskonały', '2400.00'),
('Mercedes-Benz', 'AMG GT', 2021, 'WE78901', 'Doskonały', '2700.00'),
('Chevrolet', 'Corvette Stingray', 2021, 'WE89012', 'Doskonały', '2300.00'),
('Aston Martin', 'DB11', 2019, 'WE90123', 'Doskonały', '2500.00'),
('Nissan', 'GT-R', 2020, 'WE01234', 'Doskonały', '2400.00');

COMMIT;