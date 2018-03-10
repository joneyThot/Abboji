<?php
include("includes/connection.php");

        $user_id=$_POST['user_id'];
	$location_name=$_POST['location_name'];	
       // $latitude=$_POST['latitude'];
       // $longitude=$_POST['longitude'];
        
       // DELETE FROM `silitest_abooji`.`user_location` WHERE `user_location`.`id` = 1"
        
        $obj_s="DELETE from user_location where user_id='".$user_id."' and location_name='".$location_name."'";
	$result=mysql_query($obj_s);
	$array=mysql_fetch_assoc($result);
        
        if(mysql_num_rows($result) == 0){
        $array["status"] = 0;

                           }else{
                            
                            $array["status"] = 1;
				
			}

echo json_encode($array);
?>
