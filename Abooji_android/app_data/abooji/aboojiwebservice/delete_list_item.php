<?php
include("includes/connection.php");

        $list_id=$_POST['list_id'];
        $_card_position=$_POST['list'];	
       // $_card_position='67';
        //$user_id='3';
       // DELETE FROM `silitest_abooji`.`user_location` WHERE `user_location`.`id` = 1"
        
        $obj_s="select lists from temp_cart where id='".$list_id."'";
	$result=mysql_query($obj_s);
	$array=mysql_fetch_assoc($result);
        //echo "Testing result query".$result;
	//print_r($array);exit;
	//echo "Original Cards".$array['cards']."</br>";
	$array['lists']=str_replace($_card_position,",",$array['lists']);
	
	$array['lists']=str_replace(",,,",",",$array['lists']);
	$array['lists']=str_replace(",,","",$array['lists']);
	
	
	$obj_s="update temp_cart set lists='".$array['lists']."' where id='".$list_id."'";
	$result=mysql_query($obj_s);
	$array=mysql_fetch_assoc($result);
	
        if(mysql_num_rows($result) == 0){
        $array["status"] = 1;

                           }else{
                            
                            $array["status"] = 0;
				
			}

echo json_encode($array);
?>
