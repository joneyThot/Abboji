<?php
include("includes/connection.php");

        $user_id=$_POST['user_id'];
	$location_name=$_POST['location_name'];	
        $latitude=$_POST['latitude'];
        $longitude=$_POST['longitude'];
        
        
        $obj_s="SELECT * from user_location where user_id ='".$user_id."' and location_name='".$location_name."'";
	$result=mysql_query($obj_s);
	$data=mysql_fetch_assoc($result);
        
        if(mysql_num_rows($result) == 0){
        
        $sql = "INSERT INTO user_location (user_id,location_name, latitude,longitude)
				VALUES ('".$user_id."','".$location_name."','".$latitude."','".$longitude."')";
					
				if(mysql_query($sql)){
					$array["status"] = 1; // user register 	successfully
					$array["user_id"] = mysql_insert_id();
				}else{
					$array["status"] = 0; // some other problem to register user
				}
				

                           }else{
				
				 $sql = "update user_location
					set latitude='".$latitude."',
					    longitude='".$longitude."'
					    where user_id ='".$user_id."' and location_name='".$location_name."'
					
					";
				mysql_query($sql);
				
				//echo "Update latitude and longitude for same location and user id ";exit;
			}

echo json_encode($array);
?>
