--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3
-- Dumped by pg_dump version 16.3

-- Started on 2025-11-16 14:32:40

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

--
-- TOC entry 4815 (class 0 OID 164300)
-- Dependencies: 216
-- Data for Name: card; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.card (validity_period, balance, currency_id, id, status_card_id, user_id, number, is_blocked) FROM stdin;
\.


--
-- TOC entry 4823 (class 0 OID 164345)
-- Dependencies: 224
-- Data for Name: card_transfer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.card_transfer (id, amount_of_money, description, is_transfered, transfer_date, receiver_card_id, sender_card_id) FROM stdin;
\.


--
-- TOC entry 4817 (class 0 OID 164306)
-- Dependencies: 218
-- Data for Name: currency; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.currency (id, name, sign) FROM stdin;
\.


--
-- TOC entry 4819 (class 0 OID 164314)
-- Dependencies: 220
-- Data for Name: status_card; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.status_card (id, name) FROM stdin;
\.


--
-- TOC entry 4821 (class 0 OID 164322)
-- Dependencies: 222
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."user" (id, login, name, password, patronymic, surname) FROM stdin;
\.


--
-- TOC entry 4829 (class 0 OID 0)
-- Dependencies: 215
-- Name: card_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.card_id_seq', 1, false);


--
-- TOC entry 4830 (class 0 OID 0)
-- Dependencies: 223
-- Name: card_transfer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.card_transfer_id_seq', 1, false);


--
-- TOC entry 4831 (class 0 OID 0)
-- Dependencies: 217
-- Name: currency_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.currency_id_seq', 1, false);


--
-- TOC entry 4832 (class 0 OID 0)
-- Dependencies: 219
-- Name: status_card_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.status_card_id_seq', 1, false);


--
-- TOC entry 4833 (class 0 OID 0)
-- Dependencies: 221
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_id_seq', 1, false);


-- Completed on 2025-11-16 14:32:41

--
-- PostgreSQL database dump complete
--

