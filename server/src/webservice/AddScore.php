/* require the user as the parameter */
if(isset($_GET['player']) && intval($_GET['player'])) {

	/* soak in the passed variable or set our own */
	$score = isset($_GET['score']) ? intval($_GET['score']) : 10; //10 is the default
	$player_id = intval($_GET['user']); //no default
	$time = intval($_GET['time']);
	$bus = strval($_GET['bus']);

	/* connect to the db */
	$link = mysql_connect('localhost','root','root') or die('Cannot connect to the DB');
	mysql_select_db('db_name',$link) or die('Cannot select the DB');

	/* grab the posts from the db */
	$query = "INSERT Score (player, time, score, bus) VALUES ($player_id,$time,$score,$bus)";
	$result = mysql_query($query,$link) or die('Errant query:  '.$query);

	if($result){
 		// successfully insert
        $response["success"] = 1;
        $response["message"] = "Score successfully recorded.";

        // echoing JSON response
        echo json_encode($response);
     } else {
     	// insert failed.
     	$response["success"] = 0;
     	$response["message"] = "Score failed to record!"
     }

	/* disconnect from the db */
	@mysql_close($link);
}