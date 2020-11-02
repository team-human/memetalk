DROP USER IF EXISTS memetalk_dev;
CREATE USER memetalk_dev PASSWORD '1234';

DROP DATABASE IF EXISTS memetalk;
CREATE DATABASE memetalk;
\c memetalk;

CREATE TABLE meme (
  id SERIAL PRIMARY KEY,
  url TEXT, -- DEPRECATED
  image BYTEA
);

CREATE TABLE meme_to_tag (
  meme_id INTEGER,
  tag VARCHAR(20)
);

INSERT INTO meme (url, image) VALUES ('https://example1.png', pg_read_binary_file('meme1.png'));
INSERT INTO meme (url, image) VALUES ('https://example2.png', pg_read_binary_file('meme2.png'));
INSERT INTO meme (url, image) VALUES ('https://example3.png', pg_read_binary_file('meme3.png'));

INSERT INTO meme_to_tag (meme_id, tag) VALUES (1, 'humor');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (1, 'software');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (2, 'humor');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (2, 'software');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (3, 'humor');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (3, 'life');
