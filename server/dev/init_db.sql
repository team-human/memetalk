DROP USER IF EXISTS memetalk_dev;
CREATE USER memetalk_dev PASSWORD '1234';

DROP DATABASE IF EXISTS memetalk;
CREATE DATABASE memetalk;
\c memetalk;

/* Table: meme_user */
CREATE TABLE meme_user (
  id SERIAL PRIMARY KEY,
  name VARCHAR(64),
);
INSERT INTO meme_user (name) VALUES ('Harry Potter');
INSERT INTO meme_user (name) VALUES ('Hermione Granger');

/* Table: meme */
CREATE TABLE meme (
  id SERIAL PRIMARY KEY,
  image BYTEA,
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  user_id INTEGER
);

INSERT INTO meme (image, user_id) VALUES (pg_read_binary_file('meme1.png'), 1);
INSERT INTO meme (image, user_id) VALUES (pg_read_binary_file('meme2.png'), 2);
INSERT INTO meme (image, user_id) VALUES (pg_read_binary_file('meme3.png'), 2);

/* Table: meme_to_tag */
CREATE TABLE meme_to_tag (
  meme_id INTEGER,
  tag VARCHAR(20)
);

INSERT INTO meme_to_tag (meme_id, tag) VALUES (1, 'humor');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (1, 'software');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (2, 'humor');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (2, 'software');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (3, 'humor');
INSERT INTO meme_to_tag (meme_id, tag) VALUES (3, 'life');

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO memetalk_dev;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO memetalk_dev;
