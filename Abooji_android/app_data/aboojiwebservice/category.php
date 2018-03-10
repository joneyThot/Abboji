<?php
include("includes/connection.php");

	$check_sql = "SELECT * FROM category WHERE status ='Active'";
	$result = mysql_query($check_sql);
	$category=array();
	while($data = mysql_fetch_array($result)) 
	 { 
	 	$category[]=$data;
	 } 
	//print_r($category);exit;
	 $array['category'] = $category;
	 /*if($advrts != ""){
	  $array["status"] = 1; // Successfully
	 }else{
	 $array["status"] = 0; // Error
	 }*/
echo json_encode($array); 
?>