<?php
include("includes/connection.php");

			$today=date("Y-m-d");
		        $valtoday='\''.$today.'\'';
			$search_keyword = $_POST['keyword'];/*FOR KEYWORD*/
			//$search_keyword ="offer2";
			//$search_keyword =74;
            $search_category = $_POST['category'];/*FOR CATEGORY*/
            //$search_category =4;
            $search_card_name =$_POST['card'];/*FOR CARD*/
            //$search_card_name = 1;
            //$search_range =$_POST['range'];/*FOR RANGE*/
            $search_location =$_POST['location'];/*FOR LOCATION*/
            //$search_location = 10;
            
            
            /*=============START FOR CARD SEARCH=============*/
            $sql_where_clause = "";
			if($search_card_name!=0){
            	if($sql_where_clause!=""){
                       $sql_where_clause .= " AND card_id='".$search_card_name."'";
                }else{
                    $sql_where_clause  = "card_id='".$search_card_name."'";
                }
            }
            $check_sql = "SELECT *
            FROM merchant_card
            LEFT JOIN card_company on card_company.id = merchant_card.card_id
            WHERE $sql_where_clause";
            $result = mysql_query($check_sql);
            $search_card=array();
            while($data = mysql_fetch_array($result))
            {
            	$search_card[]=$data;
            }
            $array['search_card'] = $search_card;
            //print_r($search_card);exit;
            /*=============END FOR CARD SEARCH=============*/
            
            
            /*=============START FOR TITLE AND CATEGORY  SEARCH=============*/
            $sql_where_clause = "";
             if($search_keyword !=0 || $search_keyword !=""){
             	if($sql_where_clause!=""){
             		//$sql_where_clause .= " AND listing_offer.id='".$search_keyword."'";/*=====FOR ID SEARCH======/*
             		$sql_where_clause .= " AND listing_offer.title Like '%".$search_keyword."%'";
            	}else{
            		//$sql_where_clause  = "listing_offer.id='".$search_keyword."'";/*=====FOR ID SEARCH======/*
            		$sql_where_clause  = "listing_offer.title Like'%".$search_keyword."%'";
            	}
            }else if($search_category!=0){
            	if($sql_where_clause!=""){
                       $sql_where_clause .= " AND category_id='".$search_category."'";
                }else{
                    $sql_where_clause  = "category_id='".$search_category."'";
                }
            }
            if($sql_where_clause == ""){
            	/*$check_sql = "SELECT *,listing_offer.id as listing_id
            	FROM listing_offer
            	LEFT JOIN category on category.id = listing_offer.category_id
		where
                $valtoday BETWEEN start_date and end_date

		";*/
		
	//backup line  LEFT JOIN merchant_employer AS merchant_employer ON ( merchant_employer.id = listing_offer.user_id )
	
		$check_sql="
			    SELECT listing_offer . * , merchant.merchant_name AS merchant_merchant_name, merchant_employer.outlet_name AS merchant_employer_outlet_name, card_company.company_name AS card_company_company_name, cc_card.card_name AS cc_card_card_name, mall.mall_name AS mall_mall_name, mall_name.name AS mall_name_name,merchant_employer.outlet_name AS outlet_name,merchant_card.promo_name AS promo_name
	    FROM listing_offer
	    LEFT JOIN merchant AS merchant ON ( merchant.id = listing_offer.user_id )
	   
		LEFT JOIN merchant_employer AS merchant_employer ON ( merchant_employer.id = listing_offer.outlet_id )
		LEFT JOIN merchant_card AS merchant_card ON ( merchant_card.id = listing_offer.cc_ids )


	    LEFT JOIN card_company AS card_company ON ( card_company.id = listing_offer.user_id )
	    LEFT JOIN cc_card AS cc_card ON ( cc_card.id = listing_offer.user_id )
	    LEFT JOIN mall AS mall ON ( mall.id = listing_offer.user_id )
	    LEFT JOIN mall_name AS mall_name ON ( mall_name.id = mall.admall_id )
	    LEFT JOIN category on category.id = listing_offer.category_id
	    where $valtoday BETWEEN start_date and end_date";
			    
		
            }else{
            /*	$check_sql = "SELECT *,listing_offer.id as listing_id
            	FROM listing_offer
            	LEFT JOIN category on category.id = listing_offer.category_id
            	WHERE $valtoday BETWEEN start_date and end_date and $sql_where_clause";*/
	    
	    $check_sql="
		     SELECT listing_offer . * , merchant.merchant_name AS merchant_merchant_name, merchant_employer.outlet_name AS merchant_employer_outlet_name, card_company.company_name AS card_company_company_name, cc_card.card_name AS cc_card_card_name, mall.mall_name AS mall_mall_name, mall_name.name AS mall_name_name,merchant_employer.outlet_name AS outlet_name,merchant_card.promo_name AS promo_name
	 FROM listing_offer
	 LEFT JOIN merchant AS merchant ON ( merchant.id = listing_offer.user_id )

		LEFT JOIN merchant_employer AS merchant_employer ON ( merchant_employer.id = listing_offer.outlet_id )
		LEFT JOIN merchant_card AS merchant_card ON ( merchant_card.id = listing_offer.cc_ids )
	

	 LEFT JOIN card_company AS card_company ON ( card_company.id = listing_offer.user_id )
	 LEFT JOIN cc_card AS cc_card ON ( cc_card.id = listing_offer.user_id )
	 LEFT JOIN mall AS mall ON ( mall.id = listing_offer.user_id )
	 LEFT JOIN mall_name AS mall_name ON ( mall_name.id = mall.admall_id )
	 LEFT JOIN category on category.id = listing_offer.category_id
	 WHERE $valtoday BETWEEN start_date and end_date and $sql_where_clause";
	    
            }
            
            $result = mysql_query($check_sql);
	    
	    //echo $check_sql;exit;
	    
            $search_keyword_cat=array();
            while($data = mysql_fetch_array($result))
            {
            	$search_keyword_cat[]=$data;
            }
            $array['search_keyword_cat'] = $search_keyword_cat;
            //print_r($search_keyword_cat);exit;
             /*=============END FOR TITLE AND CATEGORY  SEARCH=============*/
			
            
            
            /*=============START LOGIN USER DETAILS FOR DISTANE AND LOCATION===============*/
            $cmp_type = $_SESSION['COMPANY_TYPE'];
            //$_SESSION['USER'] = 1;
            $cmp_type = "Merchant";
            if(!empty($cmp_type)){
            	if($cmp_type == "Merchant"){
            		$check_sql = "SELECT *
            		FROM merchant WHERE id='".$_SESSION['USER']."'";
            		$result = mysql_query($check_sql);
            		$data = mysql_fetch_assoc($result);
            	}else if($cmp_type == "Mall"){
            		$check_sql = "SELECT *
            		FROM mall WHERE id='".$_SESSION['USER']."'";
            		$result = mysql_query($check_sql);
            		$data = mysql_fetch_assoc($result);
            	}else if($cmp_type == "Card"){
            		$check_sql = "SELECT *
            		FROM card_company WHERE id='".$_SESSION['USER']."'";
            		$result = mysql_query($check_sql);
            		$data = mysql_fetch_assoc($result);
            	}
            }
            	
            	
            	
            $latitude = "";
            $longitude = "";
            $login_latitide = $data['latitude'];
            $login_longitude = $data['longitude'];
            for($i=0;$i<count($search_keyword_cat);$i++)
            {
            if($search_keyword_cat[$i]['latitude']!="" && $search_keyword_cat[$i]['longitude']!="" && $login_latitide!="" && $login_longitude!=""){
            	$distance_km=distance($login_latitide, $login_longitude, $search_keyword_cat[$i]['latitude'], $search_keyword_cat[$i]['longitude'], "K");
            	$search_keyword_cat[$i]['distance']=number_format($distance_km,"2",".",",");
            }else{
            $search_keyword_cat[$i]['distance']="----";
            }
            	
            if($search_location==""){
            	$new_details_array[]=$search_keyword_cat[$i];
            }else if($search_location!="" && $distance_km!=""){
	            if($distance_km<=($search_location)){
	            $new_details_array[]=$search_keyword_cat[$i];
	            }
            }
            }
            $array['loaction'] = $new_details_array;
            //print_r($new_details_array);exit;
            
            /*=============START FOR RANGE================*/
			function distance($lat1, $lon1, $lat2, $lon2, $unit) {
				
				$theta = $lon1 - $lon2;
				$dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
				$dist = acos($dist);
				$dist = rad2deg($dist);
				$miles = $dist * 60 * 1.1515;
				$unit = strtoupper($unit);
			
				if($unit == "K")
				{
					return ($miles * 1.609344);
				}
			}
			/*=============START FOR RANGE================*/
			/*=============END LOGIN USER DETAILS FOR DISTANE AND LOCATION===============*/
echo json_encode($array); 
?>
