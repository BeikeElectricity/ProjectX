-- Select top 10 result in total

DELIMITER //
CREATE PROCEDURE GetGlobalTopTen ()
   BEGIN 
      SELECT nickname, score
      FROM (Score
           JOIN Player ON Score.player = Player.playerId)
      ORDER BY score DESC
      LIMIT 10;
   END
//
DELIMITER ;

-- Select top 10 result for player

DELIMITER //
CREATE PROCEDURE GetPlayerTopTen (IN player VARCHAR(50))
   BEGIN 
      SELECT nickname, score
      FROM
       (SELECT *
        FROM (Score
             JOIN Player ON Score.player = Player.playerId)) AS S
        WHERE S.player = player
        ORDER BY score DESC
        LIMIT 10;
   END
//
DELIMITER ;

