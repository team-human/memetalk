DROP USER IF EXISTS memetalk_dev;
CREATE USER memetalk_dev PASSWORD '1234';

DROP DATABASE IF EXISTS memetalk;
CREATE DATABASE memetalk;
\c memetalk;

CREATE TABLE meme (
  id SERIAL PRIMARY KEY NOT NULL,
  url TEXT,
  -- We want to replace `url` with `image`.
  image BYTEA
);

INSERT INTO meme (url) VALUES ('https://example.png');
