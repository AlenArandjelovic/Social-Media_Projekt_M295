FROM mariadb:11

# DB Variablen
ENV MYSQL_ROOT_PASSWORD=rootpassword
ENV MYSQL_DATABASE=socialmedia
ENV MYSQL_USER=alen
ENV MYSQL_PASSWORD=mypassword

# Optional: SQL Scripts für Tabellen initialisieren
COPY ./initdb.sql /docker-entrypoint-initdb.d/

EXPOSE 3306
