CREATE TABLE users (
    id bigserial NOT NULL,
    "name" varchar(500) NOT NULL,
    date_of_birth date NOT NULL,
    "password" varchar(500) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE account (
    id bigserial NOT NULL,
    user_id int8 NOT NULL,
    balance numeric(19, 2) NOT NULL,
    init_balance numeric(19, 2) NOT NULL,
    "version" int8 NOT NULL DEFAULT 0,
    CONSTRAINT account_pkey PRIMARY KEY (id),
    CONSTRAINT account_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE UNIQUE INDEX account_user_id_idx ON public.account USING btree (user_id);

CREATE TABLE email_data (
    id bigserial NOT NULL,
    user_id int8 NOT NULL,
    email varchar(200) NOT NULL,
    CONSTRAINT email_data_email_key UNIQUE (email),
    CONSTRAINT email_data_pkey PRIMARY KEY (id),
    CONSTRAINT email_data_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX email_data_email_idx ON public.email_data USING btree (email);

CREATE TABLE phone_data (
    id bigserial NOT NULL,
    user_id int8 NOT NULL,
    phone varchar(13) NOT NULL,
    CONSTRAINT phone_data_phone_key UNIQUE (phone),
    CONSTRAINT phone_data_pkey PRIMARY KEY (id),
    CONSTRAINT phone_data_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX phone_data_phone_idx ON public.phone_data USING btree (phone);