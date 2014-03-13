CREATE USER 'courseplanner'@'localhost' IDENTIFIED BY 'tiger';
GRANT ALL PRIVILEGES ON courseplanner.* to courseplanner@localhost;
FLUSH PRIVILEGES;