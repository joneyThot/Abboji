<?php
include("includes/connection.php");

$today=date("Y-m-d");
$valtoday='\''.$today.'\'';
//echo $today."space ".$valtoday;exit; for adding '';


	$check_sql = "SELECT * FROM listing_offer WHERE offer_type ='Ad' and $valtoday BETWEEN start_date and end_date order by id DESC";
	$result = mysql_query($check_sql);
	//print_r($check_sql);exit;
	$advrts=array();
	while($data = mysql_fetch_array($result)) 
	 { 
	 	$advrts[]=$data;
	 	//$advrts[]= $data['default_image'];
	 } 
	//print_r($advrts);exit;
	/*if($advrts != ""){
		$array["status"] = 1; // Successfully
	}else{
		$array["status"] = 0; // Error
	}*/
	$array['advrts'] = $advrts;
	//echo '<pre>';
	
echo json_encode($array); 
?>