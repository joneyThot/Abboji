<?php
include("includes/connection.php");

	$check_sql = "SELECT DISTINCT cc_card.*,card_type.card_type_title,
	card_category.card_category_title,card_company.company_name as organisation_name
	FROM cc_card
	LEFT JOIN card_company ON card_company.id = cc_card.card_id
	LEFT JOIN card_type ON card_type.id = cc_card.card_type_id
	LEFT JOIN card_category ON card_category.id = cc_card.card_category_id";
	
	$result = mysql_query($check_sql);
	
	//echo $check_sql;exit;
	
	
	$card=array();
	while($data = mysql_fetch_array($result))
	{
		$card[]=$data;
		/*if($data['card_name'] ==""){
			$data['card_name'] = "";
		}
		$card[] = $data['card_name'];*/
	}
	$array['card_details'] = $card;
	//print_r($card);exit;
	
echo json_encode($array);
?>
