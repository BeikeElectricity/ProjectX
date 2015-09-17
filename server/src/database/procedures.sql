-- Select top 10 result in total

DELIMITER //
CREATE PROCEDURE GetGlobalTopTen ()
   BEGIN 
      SELECT * 
      FROM Score
      ORDER BY score DESC
      LIMIT 10;
   END
//
DELIMITER ;

-- Select top 10 result for player

DELIMITER //
CREATE PROCEDURE GetPlayerTopTen (IN player INT)
   BEGIN 
      SELECT * 
      FROM Score s
      WHERE s.player = player
      ORDER BY score DESC
      LIMIT 10;
   END
//
DELIMITER ;

