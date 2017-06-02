<?php header('Content-Type: text/html; charset=UTF-8;');
    header('Access-Control-Allow-Origin: *'); 
    header("Cache-Control: no-cache, must-revalidate");  
    header("Pragma: no-cache"); 
?>

<?php
    // PHP-скрипт, получающий список 'активных' заказов из базы данных
    $connection = mysqli_connect("fdb16.freehostingeu.com", "2364885_foof", "uLXznx4vwD", "2364885_foof");
    mysqli_set_charset($connection, "utf8");

    $result = mysqli_query($connection,"SELECT * FROM orders WHERE status='1'");
    $total_rows = mysqli_num_rows($result);

    class Order {
        var $id;
        var $firstName;
        var $secondName;
        var $phone;
        var $mail;
        var $address;
        var $products;
        var $price;

        function __construct($id, $firstName, $secondName, $phone, $mail, $address, $products, $price) {
            $this->id = $id;
            $this->firstName = $firstName;
            $this->secondName = $secondName;
            $this->phone = $phone;
            $this->mail = $mail;
            $this->address = $address;
            $this->products = $products;
            $this->price = $price;
        }
    }

    $orders = [];
    while($row = mysqli_fetch_row($result)) {
        
        array_push($orders, new Order($row[0], $row[1], $row[2], $row[3], $row[4], $row[5], $row[6], $row[7]));
    }
    //echo $orders[0]->secondName;
    echo json_encode($orders);
?>