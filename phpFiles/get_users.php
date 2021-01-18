<?
require("config.php");
$query = "SELECT id, name FROM users ORDER by ID ASC";

if ($result = mysqli_query($link, $query)) {
	
// ВЫВОД JSON
	$tmparray = array();
	while ($row = mysqli_fetch_assoc($result)) {
		$tmparray[] = $row;
	}
	echo json_encode($tmparray);
	// как объекты
	//echo json_encode($tmparray, JSON_FORCE_OBJECT);
	
/* ВЫВОД В ЧЕЛОВЕЧЕСКОМ ФОРМАТЕ
    // извлечение ассоциативного массива
    while ($row = mysqli_fetch_assoc($result)) {
        echo $row["id"].$row["name"]."<br>";
    }
*/

// удаление выборки
    mysqli_free_result($result);
}

mysqli_close($link);
?>