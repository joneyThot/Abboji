<?php
include("includes/connection.php");

        $user_id=$_POST['user_id'];
        $list_id=$_POST['list_id'];
        
       //$user_id=3;

        
        $obj_s="Delete from temp_cart where id='".$list_id."'";
	$result=mysql_query($obj_s);
	
        if(mysql_num_rows($result) == 0){
        
       		$array["status"] = 0;
				
                           }else{
				//echo "Record exist";exit;
                                $array["status"] = 1;
                                
			}

echo json_encode($array);
?>
