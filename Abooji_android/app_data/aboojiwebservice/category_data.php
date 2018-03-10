<?php
include("includes/connection.php");

	$category = $_POST['category'];
	     //$email = $_POST['email'];
	//echo "Category id is".$category;exit;
	
	//$category = "14";
	//$category = "all";
	/*if($category == "all")
	{
		$check_sql = "SELECT *
		FROM listing_offer
		LEFT JOIN category on FIND_IN_SET(category.id, listing_offer.category_id)";
	}
	else
	{
		$check_sql = "SELECT *
		FROM listing_offer
		LEFT JOIN category on FIND_IN_SET(category.id, listing_offer.category_id) WHERE category_id='".$category."'";
	
	
	}
	*/
	
        //modification on 22nd july//
        
        if($category == "all")
	{
		$check_sql = "SELECT *,listing_offer.id as listing_id
		FROM listing_offer
		LEFT JOIN category on FIND_IN_SET(category.id, listing_offer.category_id)";
	}
	else
	{
		$check_sql = "SELECT *,listing_offer.id as listing_id,category.id as cat_id
		FROM listing_offer
	LEFT JOIN category on FIND_IN_SET($category, listing_offer.category_id) WHERE category.id='".$category."'";
	
	
	}
	
        //modification ends //


	$result = mysql_query($check_sql);
	
	//echo $check_sql;
	$listing_offer=array();
	while($data = mysql_fetch_array($result)) 
	 { 
	 	$listing_offer[]=$data;
	 } 
	 
	$array['listing_offer'] = $listing_offer;
	 
	 // echo "<pre>";
	//  echo count($listing_offer);
	// print_r($listing_offer);
	// echo "<pre>";
	 
echo json_encode($array); 
?>
