-- ALTER USER postgres PASSWORD '7nd2M487dMM7kLD4';
CREATE USER dml_user PASSWORD 'xmrtuKNbeE7O1kU';
CREATE SCHEMA IF NOT EXISTS eshop;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO dml_user;
-- grant all privileges on database eshop to postgres;