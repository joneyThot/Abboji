<?php
include("includes/connection.php");

       $user_id=$_POST['user_id'];
      //$user_id=13;
         // $latitude=$_POST['latitude'];
         //$longitude=$_POST['longitude'];
        
        
        $obj_s="SELECT * from user where id='".$user_id."'";
	$result=mysql_query($obj_s);
	$array=mysql_fetch_assoc($result);
        
        if(mysql_num_rows($result) == 0){
        
     
					$array["status"] = 0; // user register 	successfully
				
				}else{
					$array["status"] = 1; // some other problem to register user
				}
				
echo json_encode($array);
?>
