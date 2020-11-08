#!/bin/bash
PG_DATADIR='/usr/local/var/postgres'
# DEV_DIR points to the folder that contains this script.
DEV_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cp $DEV_DIR/meme1.png $DEV_DIR/meme2.png $DEV_DIR/meme3.png $PG_DATADIR
psql postgres < $DEV_DIR/init_db.sql
