INSERT INTO users ("name", date_of_birth, "password") VALUES('QWErty321', '2020-01-01', '$2a$12$dotEPCjYcWNZAo35lG9IWO8SQiG3zsfzeh06/9NDQID6B3q06cvrO');
INSERT INTO users ("name", date_of_birth, "password") VALUES('QWErty123', '2000-09-18', '$2a$12$v.q96/a49k2hk6EPO00/suHBUqetCU5G7rAWsd6fVP6L.12DZjBP2');

INSERT INTO account (user_id, balance, init_balance) VALUES(1, 100.00, 100.00);
INSERT INTO account (user_id, balance, init_balance) VALUES(2, 10.00, 10.00);

INSERT INTO email_data (user_id, email) VALUES(1, 'test2@ya.ru');
INSERT INTO email_data (user_id, email) VALUES(2, 'string@test.com');

INSERT INTO phone_data (user_id, phone) VALUES(1, '79601255339');
INSERT INTO phone_data (user_id, phone) VALUES(2, '79356543216');