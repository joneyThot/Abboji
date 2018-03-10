<?php
include("includes/connection.php");

        $user_id=$_POST['user_id'];
       // $latitude=$_POST['latitude'];
      //  $longitude=$_POST['longitude'];
        $user_email=$_POST['email'];
	//$user_email="surajmishra@gmail.com";
	
        $gender=$_POST['gender'];
		$ageGroup=$_POST['ageGroup'];
		$maritalStatus=$_POST['maritalStatus'];
		$children=$_POST['children'];
		$card_cat=$_POST['card_cat'];
		$card_type=$_POST['card_type'];
		$childAgeGroup=$_POST['childAgeGroup'];
	        $friendsEmail=$_POST['friendsEmail'];
		$cards=$_POST['cards'];
		//$cards="Its working";
		
		$categoryPreference=$_POST['categoryPreference'];
		$alert=$_POST['alert'];
		$alertTime=$_POST['alertTime'];

		 /* 
			save_user_profile.php
            send: user_id,gender,ageGroup,maritalStatus,children,childAgeGroup,friendsEmail,cards,categoryPreference,alert,alertTime,locations
            get: success or fail
	   	*/
		
        $obj_s="SELECT * from user where email='".$user_email."'";
	    $result=mysql_query($obj_s);
	    $data=mysql_fetch_assoc($result);
        
        if(mysql_num_rows($result) == 0){
		//echo "User does not exist nothing to update ";
		$array["status"]=0;
		
		
		
		//exit;
		
        }
			else{
					//children_age= '".$childAgeGroup."',
					//$array["status"] = 1; // some other problem to register user
					//$sql = "update INTO user (gender, age_range,marrital_status,children, children_age,freinds_email,cards, categoryPreference,alert,alertTime)
	//VALUES ('".$gender."','".$ageGroup."','".$maritalStatus."','".$children."','".$childAgeGroup."','".$friendsEmail."','".$cards."','".$categoryPreference."','".$alert."','".$alertTime."')";
					
		$sql = " UPDATE `user`
			SET email= '".$user_email."',
 				
			gender= '".$gender."',
			age_range= '".$ageGroup."',
			marrital_status= '".$maritalStatus."',
		        children= '".$children."',
			card_cat='".$card_cat."',
		        card_type='".$card_type."',
			
			childAgeGroup='".$childAgeGroup."',
			freinds_email= '".$friendsEmail."',
			cards='".$cards."',
			category_preference= '".$categoryPreference."',
			notification_alerts='".$alertTime."',	
			notification_status= '".$alert."'
			WHERE id=".$user_id." ";
				
				//echo $sql;exit;	
					if(mysql_query($sql)){

						  $array["status"] = 1; // Data updated  successfully
						//$array["user_id"] = mysql_insert_id();

					}
					else{
					$array["status"]=2;//data was not inserted.
					
					}
					
				}
				

echo json_encode($array);
?>
