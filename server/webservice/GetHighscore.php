<?php
/* If a player was give get the id */
$player_id = isset($_GET['id']) and strval($_GET['id']) ? strval($_GET['id']) : "";

/* connect to the db */
$link = mysql_connect('localhost','beike','beike') or die('Cannot connect to the DB');
mysql_select_db('ProjectX',$link) or die('Cannot select the DB');

/* create a query */
if(isset($_GET['id'])){
	$query = "CALL GetPlayerTopTen('$player');";
} else {
	$query = "CALL GetGlobalTopTen();";
}

/* run the query */
$result = mysql_query($query,$link);

/* create one master array of the records */
$items = array();
if(mysql_num_rows($result)) {
	while($row = mysql_fetch_assoc($result)) {
		$items[] = $row;
	}
}

/* output in necessary format */
echo json_encode($items);

/* disconnect from the db */
@mysql_close($link);
?>