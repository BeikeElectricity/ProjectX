CREATE USER 'beike'@'localhost' IDENTIFIED BY beike;
GRANT ALL ON ProjectX.* TO 'beike'@'localhost';
DROP DATABASE IF EXISTS ProjectX;
CREATE DATABASE ProjectX;
USE ProjectX;
SOURCE tables.sql;
SOURCE procedures.sql;
SOURCE populate.sql;
