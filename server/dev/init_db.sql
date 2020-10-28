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

INSERT INTO meme (url, image) VALUES ('https://example1.png', pg_read_binary_file('meme1.png'));
INSERT INTO meme (url, image) VALUES ('https://example2.png', pg_read_binary_file('meme2.png'));
INSERT INTO meme (url, image) VALUES ('https://example3.png', pg_read_binary_file('meme3.png'));
