DROP DATABASE IF EXISTS memetalk;
CREATE DATABASE memetalk;
\c memetalk;

CREATE TABLE meme (
  id SERIAL PRIMARY KEY NOT NULL,
  url TEXT
);

INSERT INTO meme (url) VALUES ('https://example.png');
