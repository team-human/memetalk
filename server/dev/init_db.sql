DROP USER IF EXISTS memetalk_dev;
CREATE USER memetalk_dev PASSWORD '1234';

DROP DATABASE IF EXISTS memetalk;
CREATE DATABASE memetalk;
\c memetalk;

CREATE TABLE meme (
  id SERIAL PRIMARY KEY,
  image BYTEA
);

CREATE TABLE meme_to_tag (
  meme_id INTEGER,
  tag VARCHAR(20)
);

INSERT INTO meme (image) VALUES (pg_read_binary_file('meme1.png'));
INSERT INTO meme (image) VALUES (pg_read_binary_file('meme2.png'));
INSERT INTO meme (image) VALUES (pg_read_binary_file('meme3.png'));

INSERT INTO meme_to_tag (meme_id, tag) VALUES (1, 'humor');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (1, 'software');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (2, 'humor');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (2, 'software');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (3, 'humor');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (3, 'life');

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO memetalk_dev;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO memetalk_dev;
