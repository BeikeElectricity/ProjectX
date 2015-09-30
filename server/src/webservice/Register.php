/* require player and nickname as parameters */
if(isset($_GET['player']) && intval($_GET['player']) &&
   isset($_GET['nickname']) &&& strval($_GET['nickname']) {

	/* soak in the passed variables */
	$player_id = intval($_GET['player']);
	$nickname = strval($_GET['nickname']);

	/* connect to the db */
	$link = mysql_connect('localhost','root','root') or die('Cannot connect to the DB');
	mysql_select_db('db_name',$link) or die('Cannot select the DB');

	/* grab the posts from the db */
	$query = "INSERT Player (playerId, nickname) VALUES ($player_id,$nickname)";
	$result = mysql_query($query,$link) or die('Errant query:  '.$query);

	if($result){
 		// successfully insert
        $response["success"] = 1;
        $response["message"] = "Successfully registered.";

        // echoing JSON response
        echo json_encode($response);
     } else {
     	// insert failed.
     	$response["success"] = 0;
     	$response["message"] = "Failed to register!"

        // echoing JSON response
        echo json_encode($response);
     }

	/* disconnect from the db */
	@mysql_close($link);
}