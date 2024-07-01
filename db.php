       <?php

        $servername = "localhost";
        $username = "id22367528_mydb487"; // Enter your user name
        $dbname = "id22367528_my_db";  // Enter your database name
        $password = "53097147@Aa"; // Enter your password

        $con = mysqli_connect($servername, $username, $password, $dbname );

        if($con){
          // echo "Connection successfully";
        }
        else{
            echo "Connection faild";
        }
        ?>