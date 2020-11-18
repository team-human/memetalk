#!/bin/bash
# DEV_DIR points to the folder that contains this script.
DEV_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
curl localhost:8080/graphql \
  -F operations='{ "query": "mutation ($file: File!, $tags: [String!]) { createMeme(file: $file, tags: $tags) { tags } }", "variables": { "file": null, "tags": ["software", "humor"] } }' \
  -F map='{ "0": ["variables.file"] }' \
  -F file=@$DEV_DIR/meme1.png
