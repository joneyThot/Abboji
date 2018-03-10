<?php
include("includes/connection.php");

$email = $_POST['email'];
$password = $_POST['password'];
/*$company_type = $_POST['company_type'];
$email = 'surajmishra@gmail.com';
$password = 'admin';
$company_type = 'Mall';*/

$array = array ();
if($email=="" || $password==""){
	$array["status"] = 2; // Please provide complete information
}else{


	/*if($company_type == 'Mall'){
		$check_sql = "SELECT * FROM mall WHERE email ='".$email."' AND password ='".$password."'";
		$result = mysql_query($check_sql);
		$data = mysql_fetch_assoc($result);
		
	}else if($company_type == 'Merchant'){
		$check_sql = "SELECT * FROM merchant WHERE email ='".$email."' AND password ='".$password."'";
		$result = mysql_query($check_sql);
		$data = mysql_fetch_assoc($result);
		
	}else if($company_type == 'Card'){
		$check_sql = "SELECT * FROM card_company WHERE email ='".$email."' AND password ='".$password."'";
		$result = mysql_query($check_sql);
		$data = mysql_fetch_assoc($result);
	}
	if(mysql_num_rows($result) != 0){
		$array["status"] = 1; // user login successfully
		$array["user_id"] = $data['id'];
	}else{
		$array["status"] = 0; // Error: Invalid username or password
	}
	
	*/
	
	//modification on 22 july//
	
	$suraj_obj="SELECT * from user where email='".$email."' AND password='".$password."'";
	$result=mysql_query($suraj_obj);
	$data=mysql_fetch_assoc($result);
	
	if(mysql_num_rows($result) != 0){
		$array["status"] = 1; // user login successfully
		$array["user_id"] = $data['id'];
	}else{
		$array["status"] = 0; // Error: Invalid username or password
	}
	
	
	//print_r($data);exit;
	//modification ends//
	
	
}

echo json_encode($array); 
?>
