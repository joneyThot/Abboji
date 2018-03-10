<?php
ob_start();
session_start();
error_reporting(NULL);
ini_set("display_error", "on");

/*$db_server = "localhost";
$db_username = "silimobi_admin";
$db_password = "admin123$";
$db = "silimobi_hampton_jitney";*/
/*

$db_server = "hamptonjitney.com"; //"rpaxis.net"; //"66.7.200.109";    //212.119.25.54
$db_username = "rpaxis16_hampton";
$db_password = "W&9[5=~PEGH$";
$db = "rpaxis16_hampton";
*/

$db_server = "localhost";
$db_username = "envtest_abooji";
$db_password = "admin123";
//"TW]7_32OTx*e";
$db = "envtest_abooji";
//=== MySQL ====================//
$mysql_conn_str = mysql_connect($db_server, $db_username, $db_password);
if (!$mysql_conn_str){
    echo mysql_error();exit;
}else{
	if(mysql_select_db($db, $mysql_conn_str)){
		//echo "connected";exit;
	}else{
       // echo "error in connection";exit;
	}
}
//=== PDO ====================//
?>