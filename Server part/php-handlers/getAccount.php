<?php header('Content-Type: text/html; charset=UTF-8;');
    header('Access-Control-Allow-Origin: *'); 
    header("Cache-Control: no-cache, must-revalidate");  
    header("Pragma: no-cache"); 
?>

<?php
    // PHP-скрипт, получающий данные авторизованного пользователя из базы данных
    class Account {
        var $mail;
        var $password;
        var $dataAboutAccount;
        var $orders;
        var $bonus;

        function __construct($login, $password, $dataAboutAccount, $orders, $bonus) {
            $this->mail = $login;
            $this->password = $password;
            $this->dataAboutAccount = $dataAboutAccount;
            $this->orders = $orders;
            $this->bonus = $bonus;
        }
    }

    $connection = mysqli_connect("fdb16.freehostingeu.com", "2364885_foof", "uLXznx4vwD", "2364885_foof");
    mysqli_set_charset($connection, "utf8");
    
    if(isset($_POST['login'])) {
        $login = $_POST['login'];
        $result = mysqli_query($connection,"SELECT * FROM users WHERE login='$login' LIMIT 1");
        $row = mysqli_fetch_row($result);

        if($row[2] === $_POST['password']) {
            echo json_encode(new Account($row[1], $row[2], $row[3], $row[4], $row[5]));
        } else {
            echo "Wrong password";
        }
    }
?>