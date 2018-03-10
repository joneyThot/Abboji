<?php
include("includes/connection.php");

	$check_sql = "SELECT * FROM listing_offer WHERE user_type ='Card'";
	$result = mysql_query($check_sql);
	$card_listing_offer=array();
	while($data = mysql_fetch_array($result)) 
	 { 
	 	$card_listing_offer[]=$data;
	 } 
	//print_r($card_listing_offer);exit;
	$array['card_listing_offer'] = $card_listing_offer;
	 
echo json_encode($array); 
?>