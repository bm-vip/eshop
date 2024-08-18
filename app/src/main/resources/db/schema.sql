ALTER USER postgres PASSWORD 'password';
CREATE SCHEMA IF NOT EXISTS eshop;
grant all privileges on database eshop to postgres;