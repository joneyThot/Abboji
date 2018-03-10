<?php
include("includes/connection.php");

$today=date("Y-m-d");
$valtoday='\''.$today.'\'';

	//$check_sql = "SELECT * FROM listing_offer WHERE offer_type ='TodaysSpecial'
	//and $valtoday BETWEEN start_date and end_date order by id DESC";
	
	$check_sql="SELECT listing_offer . * , merchant.merchant_name AS merchant_merchant_name, merchant_employer.outlet_name AS merchant_employer_outlet_name, card_company.company_name AS card_company_company_name, cc_card.card_name AS cc_card_card_name, mall.mall_name AS mall_mall_name, mall_name.name AS mall_name_name,merchant_employer.outlet_name AS outlet_name,merchant_card.promo_name AS promo_name
FROM listing_offer
LEFT JOIN merchant AS merchant ON ( merchant.id = listing_offer.user_id )
	LEFT JOIN merchant_employer AS merchant_employer ON ( merchant_employer.id = listing_offer.outlet_id )
	LEFT JOIN merchant_card AS merchant_card ON ( merchant_card.id = listing_offer.cc_ids )

LEFT JOIN card_company AS card_company ON ( card_company.id = listing_offer.user_id )
LEFT JOIN cc_card AS cc_card ON ( cc_card.id = listing_offer.user_id )
LEFT JOIN mall AS mall ON ( mall.id = listing_offer.user_id )
LEFT JOIN mall_name AS mall_name ON ( mall_name.id = mall.admall_id )
WHERE listing_offer.id !=0
AND (
$valtoday BETWEEN start_date and end_date
and offer_type='TodaysSpecial'
)
ORDER BY id DESC";
	
	
	$result = mysql_query($check_sql);
	
	//echo $check_sql;exit;
	
	$todaysspecial=array();
	while($data = mysql_fetch_array($result)) 
	 { 
	 	$todaysspecial[]=$data;
	 } 
	//print_r($todaysspecial);exit;
	$array['todaysspecial'] = $todaysspecial;
	 
echo json_encode($array); 
?>
