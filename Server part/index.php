<?php header('Content-Type: text/html; charset=UTF-8;');
    header('Access-Control-Allow-Origin: *'); 
    header("Cache-Control: no-cache, must-revalidate");  
    header("Pragma: no-cache");
    ob_start();
?>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html ng-app="Console">
	<!-- Главный PHP-скрипт системы -->
    <head>
        <meta charset="utf-8">
	<title>FooF</title>
	<link href="main.css" rel="stylesheet">
        <script src="angular.min.js"></script>
	<script src="angular-cookies.js"></script>
        <script src="console.js"></script>
    </head>
    <body class="container" ng-controller="ConsoleController">
        <?php
			if(isset($_POST['submit'])) {
				$connection = mysqli_connect("fdb16.freehostingeu.com", "2364885_foof", "uLXznx4vwD", "2364885_foof");
				mysqli_set_charset($connection, "utf8");

				$result = mysqli_query($connection, " SELECT * FROM personnel WHERE login ='".mysqli_real_escape_string($connection,$_POST['login'])."' LIMIT 1");

				$row = mysqli_fetch_array($result);
				if($row['password'] === hash('sha256',$_POST['password'])) {
					setcookie("status", $row['status'], time()+3600*24*30, "/"); ?>
					<form name="btnExit" action="index.php" method="post"><input name="submit" type="submit" value="Выйти" ng-click="setZeroCookie();"/>
						<input value="" name="login" size="40" type="hidden" />
						<input value="" name="password" size="40" type="hidden" />
					<div>
						<table class="ordersTable" ng-if="master">
							<caption>Активные заказы</caption>
							<tr class="order">
								<td class="id"><b>#</b></td>
								<td class="firstName"><b>Имя</b></td>
								<td class="secondName"><b>Фамилия</b></td>
								<td class="phone"><b>Телефон</b></td>
								<td class="mail"><b>Почта</b></td>
								<td class="address"><b>Адрес</b></td>
								<td class="products"><b>Товары</b></td>
								<td class="price"><b>Итого</b></td>
								<td class="btnCancel"><b>Отменить заказ</b></td>
							</tr>
							<tr class="order" ng-repeat="order in orders | limitTo:ordersLimit">
								<td class="id">{{order.id}}</td>
								<td class="firstName">{{order.firstName}}</td>
								<td class="secondName">{{order.secondName}}</td>
								<td class="phone">{{order.phone}}</td>
								<td class="mail">{{order.mail}}</td>
								<td class="address">{{order.address}}</td>
								<td class="products"><pre>{{order.products}}</pre></td>
								<td class="price">{{order.price}}</td>
								<td class="btnCancel"><button type="button" ng-click="cancelOrder(order.id, order.mail);">Отмена</button></td>
							</tr>
						</table>
						<!--<li ng-repeat="order in orders | filter:q as results">
							[{{$index + 1}}] {{order.firstName}}
						</li>-->
						<hr>
						<p>© Artyom Malygin, 2017</p>
					</div>
				<?php } 
				else { ?>
				<section id="authorization">
					</br>
					<p>Авторизация</p>
					<!--<p class="error">Были введены неправильно логин и/или пароль</p>-->
					<form name="authorization" action="index.php" method="post">
						<input value="" name="login" size="40" type="text" placeholder="Логин" required>
						<br>
						<br>
						<input value="" name="password" size="40" type="password" placeholder="Пароль" required>
						<br>
						<br>
						<input name="submit" type="submit" value="Войти">
					</form>
				</section>
				<?php } ?>
			<?php } else { ?>
			<section id="authorization">
				</br>
				<p>Авторизация</p>
				<form name="authorization" action="index.php" method="post">
					<input value="" name="login" size="40" type="text" placeholder="Логин" required>
					<br>
					<br>
					<input value="" name="password" size="40" type="password" placeholder="Пароль" required>
					<br>
					<br>
					<input name="submit" type="submit" value="Войти" />
				</form>
			</section>
		    <?php } ?>
        
    </body>
</html>

<?php
        ob_end_flush();
?>