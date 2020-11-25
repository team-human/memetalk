/* 
 * Our current free plan for the psql on Heroku doesn't support creating roles
 * and creating database. So, we use the default role and default database.
 *
 * Run this script with:
 * $ heroku pg:psql -a metu-api < init_db.sql
 */
CREATE TABLE meme (
  id SERIAL PRIMARY KEY,
  image BYTEA
);

CREATE TABLE meme_to_tag (
  meme_id INTEGER,
  tag VARCHAR(20)
);
