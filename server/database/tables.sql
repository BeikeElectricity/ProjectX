CREATE TABLE Player(
   playerId VARCHAR (50),
   nickname VARCHAR(50),
   PRIMARY KEY (playerId) 
);

CREATE TABLE Bus(
   vinNumber VARCHAR(40),
   motorType ENUM('Electric','Hybrid'),
   PRIMARY KEY (vinNumber)
);

CREATE TABLE Score(
   player VARCHAR (50),
   time BIGINT,
   score INT,
   bus VARCHAR(40),
   PRIMARY KEY (player, time),
   FOREIGN KEY (player) REFERENCES Player(playerId),
   FOREIGN KEY (bus) REFERENCES Bus(vinNumber)
);


