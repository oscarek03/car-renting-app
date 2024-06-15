--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3
-- Dumped by pg_dump version 16.3

-- Started on 2024-06-15 18:13:06

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 224 (class 1259 OID 16462)
-- Name: account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.account (
    account_id integer NOT NULL,
    login character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    role character varying(20) NOT NULL
);

ALTER TABLE public.account OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16419)
-- Name: car; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.car (
    car_id integer NOT NULL,
    brand character varying(50) NOT NULL,
    model character varying(50) NOT NULL,
    year_of_production integer NOT NULL,
    registration_number character varying(15) NOT NULL,
    condition character varying(20) NOT NULL,
    daily_rate numeric(10,2) NOT NULL
);

ALTER TABLE public.car OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16426)
-- Name: client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client (
    client_id integer NOT NULL,
    name character varying(50) NOT NULL,
    surname character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    phone_number character varying(15) NOT NULL,
    address character varying(255) NOT NULL,
    social_security_number character varying(11)
);

ALTER TABLE public.client OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16425)
-- Name: client_client_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.client_client_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.client_client_id_seq OWNER TO postgres;

--
-- TOC entry 4884 (class 0 OID 0)
-- Dependencies: 217
-- Name: client_client_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.client_client_id_seq OWNED BY public.client.client_id;

--
-- TOC entry 220 (class 1259 OID 16433)
-- Name: rental; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rental (
    rental_id integer NOT NULL,
    client_id integer NOT NULL,
    car_id integer NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL
);

ALTER TABLE public.rental OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16432)
-- Name: rental_rental_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.rental_rental_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.rental_rental_id_seq OWNER TO postgres;

--
-- TOC entry 4885 (class 0 OID 0)
-- Dependencies: 219
-- Name: rental_rental_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rental_rental_id_seq OWNED BY public.rental.rental_id;

--
-- TOC entry 215 (class 1259 OID 16418)
-- Name: car_car_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.car_car_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.car_car_id_seq OWNER TO postgres;

--
-- TOC entry 4886 (class 0 OID 0)
-- Dependencies: 215
-- Name: car_car_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.car_car_id_seq OWNED BY public.car.car_id;

--
-- TOC entry 222 (class 1259 OID 16450)
-- Name: transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transactions (
    transaction_id integer NOT NULL,
    rental_id integer NOT NULL,
    amount numeric(10,2) NOT NULL,
    transaction_date date NOT NULL,
    payment_method character varying(20) NOT NULL
);

ALTER TABLE public.transactions OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16449)
-- Name: transactions_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transactions_transaction_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.transactions_transaction_id_seq OWNER TO postgres;

--
-- TOC entry 4887 (class 0 OID 0)
-- Dependencies: 221
-- Name: transactions_transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transactions_transaction_id_seq OWNED BY public.transactions.transaction_id;

--
-- TOC entry 223 (class 1259 OID 16461)
-- Name: account_account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.account_account_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.account_account_id_seq OWNER TO postgres;

--
-- TOC entry 4888 (class 0 OID 0)
-- Dependencies: 223
-- Name: account_account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.account_account_id_seq OWNED BY public.account.account_id;

--
-- TOC entry 4712 (class 2604 OID 16465)
-- Name: account account_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account ALTER COLUMN account_id SET DEFAULT nextval('public.account_account_id_seq'::regclass);

--
-- TOC entry 4708 (class 2604 OID 16422)
-- Name: car car_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.car ALTER COLUMN car_id SET DEFAULT nextval('public.car_car_id_seq'::regclass);

--
-- TOC entry 4709 (class 2604 OID 16429)
-- Name: client client_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client ALTER COLUMN client_id SET DEFAULT nextval('public.client_client_id_seq'::regclass);

--
-- TOC entry 4710 (class 2604 OID 16436)
-- Name: rental rental_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rental ALTER COLUMN rental_id SET DEFAULT nextval('public.rental_rental_id_seq'::regclass);

--
-- TOC entry 4711 (class 2604 OID 16453)
-- Name: transactions transaction_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions ALTER COLUMN transaction_id SET DEFAULT nextval('public.transactions_transaction_id_seq'::regclass);

--
-- TOC entry 4878 (class 0 OID 16462)
-- Dependencies: 224
-- Data for Name: account; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.account (account_id, login, password, role) FROM stdin;
1	ob131409	$2a$10$/R3Q6wbSd1YTMPMut.fT0uwa9/r2LSxzrG.KbEWReIOEUEwqtihjO	ADMIN
\.

--
-- TOC entry 4870 (class 0 OID 16419)
-- Dependencies: 216
-- Data for Name: car; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.car (car_id, brand, model, year_of_production, registration_number, condition, daily_rate) FROM stdin;
1	Ferrari	488 Spider	2018	WE12345	Excellent	2500.00
2	Lamborghini	Huracan Evo	2020	WE23456	Excellent	3000.00
3	Porsche	911 Carrera	2019	WE34567	Excellent	2200.00
4	McLaren	720S	2019	WE45678	Excellent	2800.00
5	Audi	R8	2020	WE56789	Excellent	2600.00
6	BMW	M4	2021	WE67890	Excellent	2000.00
7	Mercedes-Benz	AMG GT	2019	WE78901	Excellent	2700.00
8	Aston Martin	Vantage	2020	WE89012	Excellent	2900.00
\.

--
-- TOC entry 4872 (class 0 OID 16426)
-- Dependencies: 218
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.client (client_id, name, surname, email, phone_number, address, social_security_number) FROM stdin;
1	John	Doe	john.doe@email.com	1234567890	123 Main St, Anytown, USA	123-45-6789
\.

--
-- TOC entry 4871 (class 0 OID 16433)
-- Dependencies: 220
-- Data for Name: rental; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rental (rental_id, client_id, car_id, start_date, end_date) FROM stdin;
1	1	1	2024-06-01	2024-06-10
\.

--
-- TOC entry 4873 (class 0 OID 16450)
-- Dependencies: 222
-- Data for Name: transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transactions (transaction_id, rental_id, amount, transaction_date, payment_method) FROM stdin;
1	1	25000.00	2024-06-10	Credit Card
\.

--
-- TOC entry 4879 (class 2606 OID 16467)
-- Name: account account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (account_id);

--
-- TOC entry 4874 (class 2606 OID 16421)
-- Name: car car_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.car
    ADD CONSTRAINT car_pkey PRIMARY KEY (car_id);

--
-- TOC entry 4875 (class 2606 OID 16428)
-- Name: client client_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (client_id);

--
-- TOC entry 4876 (class 2606 OID 16435)
-- Name: rental rental_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rental
    ADD CONSTRAINT rental_pkey PRIMARY KEY (rental_id);

--
-- TOC entry 4877 (class 2606 OID 16452)
-- Name: transactions transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (transaction_id);

--
-- TOC entry 4882 (class 2606 OID 16459)
-- Name: rental rental_car_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rental
    ADD CONSTRAINT rental_car_id_fkey FOREIGN KEY (car_id) REFERENCES public.car(car_id);

--
-- TOC entry 4881 (class 2606 OID 16457)
-- Name: rental rental_client_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rental
    ADD CONSTRAINT rental_client_id_fkey FOREIGN KEY (client_id) REFERENCES public.client(client_id);

--
-- TOC entry 4883 (class 2606 OID 16460)
-- Name: transactions transactions_rental_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_rental_id_fkey FOREIGN KEY (rental_id) REFERENCES public.rental(rental_id);

--
-- PostgreSQL database dump complete
--


