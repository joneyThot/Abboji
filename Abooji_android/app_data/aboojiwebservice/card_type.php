<?php
include("includes/connection.php");

	$check_sql = "SELECT * from card_type";
	
	$result = mysql_query($check_sql);
	$card=array();
	while($data = mysql_fetch_array($result))
	{
		$card[]=$data;
		/*if($data['card_name'] ==""){
			$data['card_name'] = "";
		}
		$card[] = $data['card_name'];*/
	}
	$array['card_type'] = $card;
	//echo "<pre>";
	//print_r($card);exit;
	
echo json_encode($array);
?>
