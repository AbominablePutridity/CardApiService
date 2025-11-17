--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3
-- Dumped by pg_dump version 16.3

-- Started on 2025-11-17 13:17:50

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
-- TOC entry 4839 (class 0 OID 164424)
-- Dependencies: 216
-- Data for Name: card; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.card (id, balance, is_blocked, number, validity_period, currency_id, status_card_id, user_id) FROM stdin;
\.


--
-- TOC entry 4841 (class 0 OID 164430)
-- Dependencies: 218
-- Data for Name: card_transfer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.card_transfer (id, amount_of_money, description, is_transfered, transfer_date, receiver_card_id, sender_card_id) FROM stdin;
\.


--
-- TOC entry 4843 (class 0 OID 164436)
-- Dependencies: 220
-- Data for Name: currency; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.currency (id, name, sign) FROM stdin;
\.


--
-- TOC entry 4845 (class 0 OID 164444)
-- Dependencies: 222
-- Data for Name: operation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.operation (id, name) FROM stdin;
\.


--
-- TOC entry 4847 (class 0 OID 164450)
-- Dependencies: 224
-- Data for Name: status_card; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.status_card (id, name) FROM stdin;
\.


--
-- TOC entry 4849 (class 0 OID 164456)
-- Dependencies: 226
-- Data for Name: task_request; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task_request (id, description, operation_id, task_status_id, user_id) FROM stdin;
\.


--
-- TOC entry 4851 (class 0 OID 164462)
-- Dependencies: 228
-- Data for Name: task_status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task_status (id, name) FROM stdin;
\.


--
-- TOC entry 4853 (class 0 OID 164468)
-- Dependencies: 230
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."user" (id, login, name, password, patronymic, surname) FROM stdin;
\.


--
-- TOC entry 4859 (class 0 OID 0)
-- Dependencies: 215
-- Name: card_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.card_id_seq', 1, false);


--
-- TOC entry 4860 (class 0 OID 0)
-- Dependencies: 217
-- Name: card_transfer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.card_transfer_id_seq', 1, false);


--
-- TOC entry 4861 (class 0 OID 0)
-- Dependencies: 219
-- Name: currency_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.currency_id_seq', 1, false);


--
-- TOC entry 4862 (class 0 OID 0)
-- Dependencies: 221
-- Name: operation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.operation_id_seq', 1, false);


--
-- TOC entry 4863 (class 0 OID 0)
-- Dependencies: 223
-- Name: status_card_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.status_card_id_seq', 1, false);


--
-- TOC entry 4864 (class 0 OID 0)
-- Dependencies: 225
-- Name: task_request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.task_request_id_seq', 1, false);


--
-- TOC entry 4865 (class 0 OID 0)
-- Dependencies: 227
-- Name: task_status_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.task_status_id_seq', 1, false);


--
-- TOC entry 4866 (class 0 OID 0)
-- Dependencies: 229
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_id_seq', 1, false);


-- Completed on 2025-11-17 13:17:50

--
-- PostgreSQL database dump complete
--

