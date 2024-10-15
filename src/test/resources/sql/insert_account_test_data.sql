INSERT INTO public.users
(id, "name", date_of_birth, "password")
VALUES(9999999998, 'QWErty321', '2020-01-01', '$2a$12$dotEPCjYcWNZAo35lG9IWO8SQiG3zsfzeh06/9NDQID6B3q06cvrO');
INSERT INTO public.users
(id, "name", date_of_birth, "password")
VALUES(9999999999, 'QWErty123', '2000-09-18', '$2a$12$v.q96/a49k2hk6EPO00/suHBUqetCU5G7rAWsd6fVP6L.12DZjBP2');

INSERT INTO public.account
(id, user_id, balance, init_balance)
VALUES(9999999998, 9999999998, 2070.00, 1000.00);
INSERT INTO public.account
(id, user_id, balance, init_balance)
VALUES(9999999999, 9999999999, 0, 1000.00);