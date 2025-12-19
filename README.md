# vaadin-interface
set +H
sed -i "s|<PASSWORD>|$(printf '%s' "$PASSWORD" | sed 's/[&]/\\&/g')|g" test.yml


PASSWORD=':!&;(ihgst'
ESCAPED_PASSWORD=${PASSWORD//&/\\&}

sed -i "s/<PASSWORD>/${ESCAPED_PASSWORD}/g" test.yml
