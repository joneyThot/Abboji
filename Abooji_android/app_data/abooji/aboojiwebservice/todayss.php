<?php
//$params = array ( "one"=>"red","two"=>"blue","three"=>"green" );
//header("Content-type: text/xml");
include("includes/connection.php");
//echo phpinfo();
//echo phpversion();
//exit;

        //$user_id=$_POST['user_id'];
       
       $user_id=3;
       
	/*
	 *           2)get_shopping_list.php: (search in share_with for given user_id)
            send: user_id
            get: list_id,list_name,list
            */
	
        
        $obj_s="SELECT id as list_id,user_id as id_of_user,list_name as list_name,lists as list,share_with as share_ids from temp_cart where FIND_IN_SET('".$user_id."',share_with)";
	$result=mysql_query($obj_s);
	while($data=mysql_fetch_assoc($result))
	{
		
		$array[]=$data;
		
	}
        
        if(mysql_num_rows($result) == 0){
        
       		$array["status"] = 0;
				
                           }else{
				//echo "Record exist";exit;
                                $array["status"] = 1;
                                
			}
                        

//$xml = new DOMDocument("1.0");


$response = xmlrpc_encode ( $array );

//$response->formatOutput = true;

echo ( $response );
//."</br>"."</pre>";
//print_r($response);

//$request = xmlrpc_encode_request($array);
//echo ( $request );
//echo json_encode($array);



//$xml = new SimpleXMLElement('<root/>');
//array_walk_recursive($array, array ($xml, 'addChild'));
//print $xml->asXML();


?>