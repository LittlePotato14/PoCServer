<?
require("config.php");

$postData = file_get_contents('php://input');
$data = json_decode($postData, true);

$query = "INSERT INTO users SET name = '{$data["name"]}'";

if(mysqli_query($link, $query)){
	echo "{'status' : 'OK'}";
}

mysqli_close($link);
?>
