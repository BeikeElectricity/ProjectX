<?php
/* require score, player id, time and bus as input */
if( isset($_GET['score']) && intval($_GET['score']) &&
    isset($_GET['id']) && strval($_GET['id']) &&
    isset($_GET['time']) && intval($_GET['time']) &&
    isset($_GET['bus']) && strval($_GET['bus'])) {

    /* set response header */
	header('Content-type: application/json');

	/* parse the input */
	$score = intval($_GET['score']);
	$player_id = strval($_GET['id']);
	$time = intval($_GET['time']);
	$bus = strval($_GET['bus']);

	/* connect to the db */
	$link = mysql_connect('localhost','beike','beike') or die('Cannot connect to the DB');
	mysql_select_db('ProjectX',$link) or die('Cannot select the DB');

	/* insert the score*/
	$query = "INSERT INTO Score (player, time, score, bus) VALUES ('$player_id','$time','$score','$bus')";
	$result = mysql_query($query,$link);

    /* respond with status */
	if($result){
 		// successfully insert
        $response["success"] = 1;
        $response["message"] = "Score successfully recorded.";

        // echoing JSON response
        echo json_encode($response);
     } else {
     	// insert failed.
     	$response["success"] = 0;
     	$response["message"] = "Score failed to record!";

        // echoing JSON response
        echo json_encode($response);
     }

	/* disconnect from the db */
	@mysql_close($link);
}
?>