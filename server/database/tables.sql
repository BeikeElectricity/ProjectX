CREATE TABLE Player(
   playerId VARCHAR (50),
   nickname VARCHAR(50),
   PRIMARY KEY (playerId) 
);

CREATE TABLE Bus(
   numberPlate VARCHAR(6),
   motorType ENUM('Electric','Hybrid'),
   PRIMARY KEY (numberPlate)
);

CREATE TABLE Score(
   player VARCHAR (50),
   time DATETIME,
   score INT,
   bus VARCHAR(6),
   PRIMARY KEY (player, time),
   FOREIGN KEY (player) REFERENCES Player(playerId),
   FOREIGN KEY (bus) REFERENCES Bus(numberPlate)
);


