/**
 * NOTE: If you updates a table definition, please also update
 * `prod/init_db.sql` if needed.
 */
DROP DATABASE IF EXISTS memetalk;
DROP USER IF EXISTS memetalk_dev;

CREATE USER memetalk_dev PASSWORD '1234';

CREATE DATABASE memetalk;
\c memetalk;

/* Table: meme_user */
CREATE TABLE meme_user (
  id SERIAL PRIMARY KEY,
  username VARCHAR(64),
  name VARCHAR(64),
  password VARCHAR(64),
  roles VARCHAR(128)
);

/*
 * Password is hashed, so '$2a...UuU3a' is `1234`, and '$2a...n6.QG' is `abcd`.
 */
INSERT INTO meme_user (username, name, password, roles) VALUES ('john', 'Harry Potter', '$2a$10$w4Op9AHpvs.MMc0c.oZAQeYRKxd0qfom8YxRP5bYmE.doyagUuU3a', 'USER;');
INSERT INTO meme_user (username, name, password, roles) VALUES ('marry', 'Hermione Granger', '$2a$10$rXMooigm9.Zwtib6bIMnu.xoMH7gLvyVOa29yyc6Z2kqIejKn6.QG', 'USER;');

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
