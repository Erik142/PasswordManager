-- Table: public.Accounts

-- DROP TABLE public."Accounts";

-- Role: passwordmanager
-- DROP ROLE passwordmanager;

CREATE ROLE passwordmanager WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  ENCRYPTED PASSWORD 'SCRAM-SHA-256$4096:kmLjSakmDNS+abzkptvAYQ==$MBJUrXuOfEBSg1bs5MVaFc4zKSosfSZ5y/9WjCAUo2g=:Jae1jm0YTRa3qImgxnatgVpetmF5SWhplDdAqpWy1kg=';

-- Database: passwordmanager

-- DROP DATABASE passwordmanager;

CREATE DATABASE passwordmanager
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Connect to passwordmanager database
\c passwordmanager 

CREATE TABLE public."Accounts"
(
    "Email" text COLLATE pg_catalog."default" NOT NULL,
    "Password" text COLLATE pg_catalog."default",
    CONSTRAINT "Accounts_pkey" PRIMARY KEY ("Email")
)

TABLESPACE pg_default;

ALTER TABLE public."Accounts"
    OWNER to postgres;

GRANT DELETE, INSERT, SELECT, UPDATE ON TABLE public."Accounts" TO passwordmanager WITH GRANT OPTION;

GRANT ALL ON TABLE public."Accounts" TO postgres;

-- Table: public.Credentials

-- DROP TABLE public."Credentials";

CREATE TABLE public."Credentials"
(
    "User" text COLLATE pg_catalog."default" NOT NULL,
    "URL" text COLLATE pg_catalog."default" NOT NULL,
    "Username" text COLLATE pg_catalog."default" NOT NULL,
    "Password" text COLLATE pg_catalog."default",
    CONSTRAINT "Credentials_pkey" PRIMARY KEY ("User", "URL", "Username"),
    CONSTRAINT "Credentials_User_fkey" FOREIGN KEY ("User")
        REFERENCES public."Accounts" ("Email") MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public."Credentials"
    OWNER to postgres;

GRANT DELETE, INSERT, SELECT, UPDATE ON TABLE public."Credentials" TO passwordmanager WITH GRANT OPTION;

GRANT ALL ON TABLE public."Credentials" TO postgres;

-- Table: public.ResetRequests

-- DROP TABLE public."ResetRequests";

CREATE TABLE public."ResetRequests"
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    email text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT resetrequests_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public."ResetRequests"
    OWNER to postgres;

GRANT DELETE, INSERT, SELECT, UPDATE ON TABLE public."ResetRequests" TO passwordmanager WITH GRANT OPTION;

GRANT ALL ON TABLE public."ResetRequests" TO postgres;