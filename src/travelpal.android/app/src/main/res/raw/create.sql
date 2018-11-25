CREATE TABLE Travel (
   _id       INTEGER PRIMARY KEY AUTOINCREMENT,
   fromLocation     CHAR(50) NOT NULL,
   toLocation       CHAR(50) NOT NULL,
   cost     INTEGER NOT NULL
);
CREATE TABLE Station (
   _id       INTEGER PRIMARY KEY AUTOINCREMENT,
   name     CHAR(50) NOT NULL
);
