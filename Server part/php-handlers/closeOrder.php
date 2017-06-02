<?php header('Content-Type: text/html; charset=UTF-8;');
    header('Access-Control-Allow-Origin: *'); 
    header("Cache-Control: no-cache, must-revalidate");  
    header("Pragma: no-cache"); 
?>

<?php
    // PHP-скрипт, вызываемый нажатием кнопки "Cancel". Изменяет статус заказа и добавляет комментарий менеджера
    $connection = mysqli_connect("fdb16.freehostingeu.com", "2364885_foof", "uLXznx4vwD", "2364885_foof");
    mysqli_set_charset($connection, "utf8");

    if(isset($_GET['id'])) {
        $id = $_GET['id'];
        $login = $_GET['mail'];
        $comment = $_GET['comment'];
        $status = "0";
        mysqli_query($connection, "UPDATE orders SET status='$status', comment='$comment' WHERE id = '$id'");
        $result = mysqli_query($connection, "SELECT orders FROM users WHERE login='$login'");
        $row = mysqli_fetch_row($result);
        $orders = json_decode($row[0]);
        //echo $orders[0];
        echo json_encode($orders[0]);
        for($i = 0; $i < count($orders); $i++) {
            if($orders[$i]->id==$id) {
                $orders[$i]->status=0;
                $orders[$i]->comment=$comment;
                break;
            }
        }
        $orders = json_encode($orders, JSON_UNESCAPED_UNICODE);
        echo $orders;
        mysqli_query($connection, "UPDATE users SET orders='".mysqli_real_escape_string($connection, $orders)."' WHERE login = '$login'");
    }
?>