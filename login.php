<?php
require 'init.php';

$identifier = $_POST["identifier"]; // Can be either username or email
$password = $_POST["password"];

$sql = "SELECT * FROM reg WHERE (name = ? OR email = ?) AND password = ?";
$stmt = $con->prepare($sql);
$stmt->bind_param("sss", $identifier, $identifier, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    echo "success";
} else {
    echo "invalid";
}

$stmt->close();
$con->close();
?>
