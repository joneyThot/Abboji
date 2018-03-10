<?php

 include("includes/connection.php");

    /* i call webservice with this param:
   user_id,email,new_password
   
   and return me status:
   0- error
   1- succses */
 $email=$_POST['email'];
	//$email='suraj@siliconinfo.com';
 $new_pass=$_POST['new_password'];
//$new_pass='new_pass2';     
 $info=array();

	if($email==""){
	$info["status"]=2; //Provide valid email
	}
	else{
	//$suraj_obj="select * from user where email='".$email."'";
        
        $suraj_obj="update user set password='".$new_pass."' where email='".$email."' ";
	$qry=mysql_query($suraj_obj);
       // echo $suraj_obj;
      //  printf("Records affected: %d\n", mysql_affected_rows());
        //exit;
            
		if(mysql_affected_rows()){
                        $arr=array($email,"suraj@siliconinfo.com");
			$info["status"]=1;//valid record
			
			$from = "info@abooji.com"; // sender
   			$subject = "Password Changed";
   			$message = "Your Password is:".$new_pass;
   			 // message lines should not exceed 70 characters (PHP rule), so wrap it
    			//$message = wordwrap($message, 70);
   			// send mail
  		        mail($arr,$subject,$message,"From: $from\n");
			
			}
			else{
				$info["status"]=0;//no record exists
			      }
	//echo "<pre>";print_r($result);echo "value of status is:".$info['status'];exit;
	}

 echo json_encode($info);
?>
