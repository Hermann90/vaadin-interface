# vaadin-interface
set +H
sed -i "s|<PASSWORD>|$(printf '%s' "$PASSWORD" | sed 's/[&]/\\&/g')|g" test.yml
