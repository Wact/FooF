<?php header('Content-Type: text/html; charset=UTF-8;');
    header('Access-Control-Allow-Origin: *'); 
    header("Cache-Control: no-cache, must-revalidate");  
    header("Pragma: no-cache"); 
?>

<?php
    // PHP-скрипт, добавляющий данные нового пользователя в базу данных в виде отдельной записи
    $connection = mysqli_connect("fdb16.freehostingeu.com", "2364885_foof", "uLXznx4vwD", "2364885_foof");
    mysqli_set_charset($connection, "utf8");

    if(isset($_POST['login'])) {
        $login = $_POST['login'];
        $result = mysqli_query($connection,"SELECT * FROM users WHERE login='$login' LIMIT 1");
        $row = mysqli_fetch_row($result);

        if(!isset($row)) {
            $password = $_POST['password'];
            $dataAboutAccount = $_POST['dataAboutAccount'];
            $orders = json_encode([]);
            $bonus = "0";
            mysqli_query($connection, "INSERT INTO users (login, password, dataAboutAccount, orders, bonus) VALUES 
                                        ('$login','$password','$dataAboutAccount','$orders','$bonus')");
        }
        else {
            echo "Login is occupied";
        }
    }
?>