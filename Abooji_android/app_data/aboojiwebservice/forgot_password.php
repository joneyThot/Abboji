<?php

 include("includes/connection.php");

 $email=$_POST['email'];
	//$email='suraj@siliconinfo.com';
 $info=array();

	if($email==""){
	$info["status"]=2; //Provide valid email
	}
	else{
	$suraj_obj="select * from user where email='".$email."'";
	$qry=mysql_query($suraj_obj);
	$result=mysql_fetch_assoc($qry);
		if(mysql_num_rows($qry)!=0){
			$info["status"]=1;//valid record
			
			$from = "info@abooji.com"; // sender
   			$subject = "Forgot Password";
   			$message = "Your Password is:".$result['password'];
   			 // message lines should not exceed 70 characters (PHP rule), so wrap it
    			//$message = wordwrap($message, 70);
   			// send mail
  		        mail($email,$subject,$message,"From: $from\n");
			
			}
			else{
				$info["status"]=0;//no record exists
			      }
	//echo "<pre>";print_r($result);echo "value of status is:".$info['status'];exit;
	}

 echo json_encode($info);
?>
