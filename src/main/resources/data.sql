DROP TABLE IF EXISTS BAD_IP;
DROP TABLE IF EXISTS LIST_SOURCE;

CREATE TABLE BAD_IP
(
        id INT PRIMARY KEY,
        ip VARCHAR(250) NOT NULL,
        sources VARCHAR(250) NOT NULL

);

CREATE TABLE LIST_SOURCE
(
    id INT PRIMARY KEY,
    listName VARCHAR(250) NOT NULL,
    listUrl VARCHAR(250) NOT NULL,
    version VARCHAR(250) NOT NULL
);
