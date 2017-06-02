<?php header('Content-Type: text/html; charset=UTF-8;');
    header('Access-Control-Allow-Origin: *'); 
    header("Cache-Control: no-cache, must-revalidate");  
    header("Pragma: no-cache"); 
?>

<?php
    // PHP-скрипт, получающий список товаров из базы данных
    $connection = mysqli_connect("fdb16.freehostingeu.com", "2364885_foof", "uLXznx4vwD", "2364885_foof");
    mysqli_set_charset($connection, "utf8");

    $result = mysqli_query($connection,"SELECT * FROM products ORDER BY product ASC");
    $total_rows = mysqli_num_rows($result);

    class Product {
        var $name;
        var $price;
        var $fileName;

        function __construct($name, $price, $fileName) {
            $this->name = $name;
            $this->price = $price;
            $this->fileName = $fileName;
        }
    }

    //$response = "";
    $products = [];
    while($row = mysqli_fetch_row($result)) {
        //$response .= $row[1].":".$row[2].";";
        
        array_push($products, new Product($row[1], $row[2], $row[3]));
    }
    echo json_encode($products);
    //echo $response;
?>