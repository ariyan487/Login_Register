<?php
require 'init.php';

$name = $_POST["name"];
$email = $_POST["email"];
$phone = $_POST["phone"];
$password = $_POST["password"];

// Check if the username already exists
$stmt = $con->prepare("SELECT * FROM reg WHERE name = ?");
$stmt->bind_param("s", $name);
$stmt->execute();
$result_username = $stmt->get_result();

// Check if the email already exists
$stmt = $con->prepare("SELECT * FROM reg WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result_email = $stmt->get_result();

// Check if the phone number already exists
$stmt = $con->prepare("SELECT * FROM reg WHERE phone = ?");
$stmt->bind_param("s", $phone);
$stmt->execute();
$result_phone = $stmt->get_result();

// Check if the password already exists
$stmt = $con->prepare("SELECT * FROM reg WHERE password = ?");
$stmt->bind_param("s", $password);
$stmt->execute();
$result_password = $stmt->get_result();

if ($result_username->num_rows > 0) {
    echo "exists_username"; // Username already exists
} elseif ($result_email->num_rows > 0) {
    echo "exists_email"; // Email already registered
} elseif ($result_phone->num_rows > 0) {
    echo "exists_phone"; // Phone number already registered
} elseif ($result_password->num_rows > 0) {
    echo "exists_password"; // Password already exists
} else {
    // Insert new record
    $stmt = $con->prepare("INSERT INTO reg (name, email, phone, password) VALUES (?, ?, ?, ?)");
    $stmt->bind_param("ssss", $name, $email, $phone, $password);

    if ($stmt->execute()) {
        echo "success"; // Registration successful
    } else {
        echo "Error: " . $con->error; // Registration failed
    }
}

$stmt->close();
$con->close();
?>
