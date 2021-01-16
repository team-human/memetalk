/* 
 * Our current free plan for the psql on Heroku doesn't support creating roles
 * and creating database. So, we use the default role and default database.
 *
 * Run this script with:
 * $ heroku pg:psql -a metu-api < init_db.sql
 *
 * NOTE: This script drops existing tables.
 */

DROP TABLE IF EXISTS meme_user;
DROP TABLE IF EXISTS meme;
DROP TABLE IF EXISTS meme_to_tag;

CREATE TABLE meme_user (
  id SERIAL PRIMARY KEY,
  username VARCHAR(64),
  name VARCHAR(64),
  password VARCHAR(64),
  roles VARCHAR(128)
);

CREATE TABLE meme (
  id SERIAL PRIMARY KEY,
  image BYTEA,
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  user_id INTEGER
);

CREATE TABLE meme_to_tag (
  meme_id INTEGER,
  tag VARCHAR(20)
);
