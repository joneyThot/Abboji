<?php
include("includes/connection.php");

        $user_id=$_POST['user_id'];
       $_card_position=$_POST['card_position'];	
       // $_card_position='67';
        //$user_id='3';
       // DELETE FROM `silitest_abooji`.`user_location` WHERE `user_location`.`id` = 1"
        
        $obj_s="select cards from user where id='".$user_id."'";
	$result=mysql_query($obj_s);
	$array=mysql_fetch_assoc($result);
        //echo "Testing result query".$result;
	//print_r($array);exit;
	//echo "Original Cards".$array['cards']."</br>";
	$array['cards']=str_replace($_card_position,",",$array['cards']);
	
	$array['cards']=str_replace(",,,",",",$array['cards']);
	$array['cards']=str_replace(",,","",$array['cards']);
	
	
	$obj_s="update user set cards='".$array['cards']."' where id='".$user_id."'";
	$result=mysql_query($obj_s);
	$array=mysql_fetch_assoc($result);
	
        if(mysql_num_rows($result) == 0){
        $array["status"] = 1;

                           }else{
                            
                            $array["status"] = 0;
				
			}

echo json_encode($array);
?>
