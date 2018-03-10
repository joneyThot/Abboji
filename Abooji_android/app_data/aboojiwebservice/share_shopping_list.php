<?php
include("includes/connection.php");

        $list_id=$_POST['list_id'];
	// $list_id="73";
       $email_ids=$_POST['email_ids'];
	//$email_ids='surajmishra@gmail.com,suraj@siliconinfo.com,user@mail.com';
       
        
	/*
	 *     3)share_shopping_list.php: (get user_id from email and store user_id in
	 *     share_with where list_id match)
            send: list_id,email_ids
            get: success or fail
            
            //select id as user_id from user where find_in_set(email,'surajmishra@gmail.com,suraj@siliconinfo.com,user@mail.com');
            
            
while($data=mysql_fetch_assoc($result))
	{
		$array[]=$data;
		
	}
	
            */
	
        
        $obj_s="select id as user_id from user where find_in_set(email,'".$email_ids."');";
	$result=mysql_query($obj_s);
	
	
	while($data=mysql_fetch_assoc($result))
	{	if(empty($array['user_id']))
		{
			$array['user_id']=$data['user_id'];
		}
		else{
		$array['user_id']=$array['user_id'].",".$data['user_id'];
		}
	}
	
	
        
	//echo "<pre>";print_r($array['user_id']);
	//echo "after merging, array is";print_r(trim(",",$array['user_id']));
	//exit;
	
        if(mysql_num_rows($result) == 0){
		
				$array["status"] = 0;
				echo "No records to fetch";//exit;
        

                           }else{
				
				
				$sql = " update temp_cart set
		share_with='".$array['user_id']."'
		where id='".$list_id."'
		";
					
				if(mysql_query($sql)){
					$array["status"] = 1; // updated share with succesfully
					//$array["user_id"] = mysql_insert_id();
				}else{
					$array["status"] = 2; // some other problem to update the table
				}
				
				
			}

echo json_encode($array);
?>
