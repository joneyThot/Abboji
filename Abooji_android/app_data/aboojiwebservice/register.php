<?php
include("includes/connection.php");

$email = $_POST['email'];
$password = $_POST['password'];
$company_name = $_POST['company_name'];
$comp_reg_number = $_POST['comp_reg_number'];
$company_type = $_POST['company_type'];
$country_comp_reg_in = $_POST['country_comp_reg_in'];

/*$email = 'krunal1@siliconinfo.com';
$password = '1QXGZG5P1R';
$company_name = 'silicon';
$comp_reg_number = '123456';
$company_type = 'Mall';
$country_comp_reg_in = '52';*/

$date = date('Y-m-d');

	if($email=="" &&  $password==""){
		$array["status"] = 3; // Please provide complete information
	}else{
		
		/*if($company_type == 'Mall'){
			$check_sql = "SELECT id,password FROM mall WHERE email ='".$email."'";
			$result = mysql_query($check_sql);
			$data = mysql_fetch_assoc($result);
			if(mysql_num_rows($result) == 0){
				$sql = "INSERT INTO mall (email, company_name, password, UEN_NUMBER, country_id)
				VALUES ( '".$email."','".$company_name."','".$password."','".$comp_reg_number."','".$country_comp_reg_in."')";
					
				if(mysql_query($sql)){
					$array["status"] = 1; // user register 	successfully
					$array["user_id"] = mysql_insert_id();
				}else{
					$array["status"] = 0; // some other problem to register user
				}
			}else{
				echo "Already exist";exit;
			}
		}
		*/
		
		$obj_s="SELECT  id,password from user where email='".$email."'";
		$result=mysql_query($obj_s);
		$data=mysql_fetch_assoc($result);
		
		if(mysql_num_rows($result) == 0){
				$sql = "INSERT INTO user (email, password)
				VALUES ( '".$email."','".$password."')";
				
					
				if(mysql_query($sql)){
					$array["status"] = 1; // user register 	successfully
					$array["user_id"] = mysql_insert_id();
				}else{
					$array["status"] = 0; // some other problem to register user
				}
			}else{
				$array["status"] = 2;
				//echo "Already exist";exit;
			}
		
		
		
	}

echo json_encode($array); 
?>
