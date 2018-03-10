<?php
include("includes/connection.php");

	$check_sql = "SELECT *
	FROM merchant_card
	LEFT JOIN card_company on card_company.id = merchant_card.card_id
	GROUP BY merchant_card.card_id";
	$result = mysql_query($check_sql);
	$card=array();
	while($data = mysql_fetch_array($result))
	{
		//$card[]=$data;
		if($data['card_name'] ==""){
			$data['card_name'] = "";
		}
		$card[] = $data['card_name'];
	}
	$array['card'] = $card;
	//print_r($card);exit;
	
echo json_encode($array);
?>
