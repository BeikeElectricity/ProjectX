<?php
/* require id and nickname as parameters */
if(isset($_GET['id']) && strval($_GET['id']) &&
   isset($_GET['name']) && strval($_GET['name'])) {

	/* set respone header */
	header('Content-type: application/json');

	/* parse the input */
	$player_id = strval($_GET['id']);
	$nickname = strval($_GET['name']);

	/* connect to the db */
	$link = mysql_connect('localhost','beike','beike') or die('Cannot connect to the DB');
	mysql_select_db('ProjectX',$link) or die('Cannot select the DB');

	/* insert the user */
	$query = "INSERT INTO Player (playerId, nickname) VALUES ('$player_id','$nickname')";
	$result = mysql_query($query,$link);

    /* respond with status */
	if($result){
 		// successfully insert
        $response["success"] = 1;
        $response["message"] = "Successfully registered.";

        // echoing JSON response
        echo json_encode($response);
     } else {
     	// insert failed.
     	$response["success"] = 0;
     	$response["message"] = "Failed to register!";

        // echoing JSON response
        echo json_encode($response);
     }

	/* disconnect from the db */
	@mysql_close($link);
}
?>