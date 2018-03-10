<?php
include("includes/connection.php");

        $user_id=$_POST['user_id'];
        $list_name=$_POST['list_name'];
        $list=$_POST['list'];
	$list_id=$_POST['list_id'];
	
	 // $user_id="3";
       //$list_name="test2";
       // $list=1,2,3,4;
	//$list_id="1";
        
	/*
	 1) save_shopping_list.php: (save list in table and also add user_id in share_with)
            send: user_id,list_name,list,list_id
            get: list_id
         
	    */
        
        $obj_s="SELECT * from temp_cart where id='".$list_id."'";
	$result=mysql_query($obj_s);
	$array=mysql_fetch_assoc($result);
        
			if(mysql_num_rows($result) == 0){
			
			if($_POST['list_name']){ 
			$sql = "INSERT INTO temp_cart(user_id,list_name,lists,share_with)
				VALUES('".$user_id."','".$list_name."','".$list."','".$user_id."')
				";
			}		
				if(mysql_query($sql)){

						  $array["status"] = 1; // Data updated  successfully
						  $array["inserted_to_list_id"] = mysql_insert_id();

					}
					else{
					$array["status"]=0;//data was not inserted.
					
					}
				

                           }
					else{
					     
			             $sql = "update temp_cart set
				     
				     list_name='".$list_name."',
				     lists='".$list."'
				     where id='".$list_id."'
			     
				     ";
				     
				     mysql_query($sql);
				     
				     $array["status"]="2";
				     
					     //echo "Updated ";exit;
				     }

echo json_encode($array);
?>
