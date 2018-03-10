<?php
include("includes/connection.php");

        $user_id=$_POST['user_id'];
	//$location_name-$_POST['location_name'];	
       // $latitude=$_POST['latitude'];
       // $longitude=$_POST['longitude'];
        
        
        $obj_s="SELECT * from user_location where user_id='".$user_id."'";
	$result=mysql_query($obj_s);
	while($data=mysql_fetch_assoc($result))
        {
        $array[]=$data;
        
        }    
    
        
        if(mysql_num_rows($result) == 0){
        $array["status"] = 0;

                           }else{
                            
                            $array["status"] = 1;
				
			}

echo json_encode($array);
?>
