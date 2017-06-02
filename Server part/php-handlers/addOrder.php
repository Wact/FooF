<?php header('Content-Type: text/html; charset=UTF-8;');
    header('Access-Control-Allow-Origin: *'); 
    header("Cache-Control: no-cache, must-revalidate");  
    header("Pragma: no-cache"); 
?>

<?php
    // PHP-скрипт, добавляющий заказ в базу данных
    class Order {
        var $id;
        var $firstName;
        var $secondName;
        var $address;
        var $products;
        var $price;
        var $status;
        var $comment;

        function __construct($id, $firstName, $secondName, $address, $products, $price, $status) {
            $this->id = $id;
            $this->firstName = $firstName;
            $this->secondName = $secondName;
            $this->address = $address;
            $this->products = $products;
            $this->price = $price;
            $this->status = $status;
            $this->comment = null;
        }
    }

    $connection = mysqli_connect("fdb16.freehostingeu.com", "2364885_foof", "uLXznx4vwD", "2364885_foof");
    mysqli_set_charset($connection, "utf8");

    if(isset($_POST['dataAboutUser'])) {
        $dataAboutUser = json_decode($_POST['dataAboutUser']);
        $firstName = $dataAboutUser->firstName;
        $secondName = $dataAboutUser->secondName;
        $phone = $dataAboutUser->phone;
        $mail = $dataAboutUser->mail;
        $address = $dataAboutUser->address;
        $products = json_decode($_POST['products']);
        $postProducts = [];
        $price = 0;
        $androidId = $dataAboutUser->androidId;
        $status = "1";
        for($i = 0; $i < sizeof($products); $i++) {
            $price += $products[$i]->price;
            $postProducts[$i] = $products[$i]->product;
        }
        $cost = json_encode($price);
        $products = json_encode($postProducts, JSON_UNESCAPED_UNICODE);
        mysqli_query($connection, "INSERT INTO orders (firstName, secondName, phone, mail, address, products, price, androidId, status) VALUES 
                                    ('$firstName','$secondName','$phone','$mail','$address','$products','$cost','$androidId','$status')");
        $id = json_encode(mysqli_insert_id($connection));
        $result = mysqli_query($connection, "SELECT * FROM users WHERE login='$mail'");
        if($result != null) {
            $row = mysqli_fetch_row($result);
            $orders = json_decode($row[4], true);
            $bonus = json_decode($row[5]);
            $bonus += $price*0.1;
            $bonus = json_encode($bonus);
            array_push($orders, new Order($id, $firstName, $secondName, $address, $products, $cost, $status));
            if($orders==null)
                $orders = [];
            $orders = json_encode($orders);
            mysqli_query($connection, "UPDATE users SET orders='".mysqli_real_escape_string($connection, $orders)."', bonus='$bonus' WHERE login='$mail'");
        }
    }
?>