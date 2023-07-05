import java.util.*;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;

public class JavaWriteMariadbExample {

    public static void main(String[] args) {

        try {
 		System.out.println("--------------- Application settings ---------------");

                // check the environment variables needed for the application
                String cf_mariadb_server = System.getenv("CF_MARIADB_SERVER");
                if (cf_mariadb_server == null) {
                        System.err.println("CF_MARIADB_SERVER environment variable is not defined ! Exiting.");
                        System.exit(0);
                } else {
                        System.out.println("CF_MARIADB_SERVER:" + cf_mariadb_server);
                }
                // check the environment variables needed for the application
                String cf_mariadb_database = System.getenv("CF_MARIADB_DATABASE");
                if (cf_mariadb_database == null) {
                        System.err.println("CF_MARIADB_DATABASE environment variable is not defined ! Exiting.");
                        System.exit(0);
                } else {
                        System.out.println("CF_MARIADB_DATABASE:" + cf_mariadb_database);
                }
                // check the environment variables needed for the application
                String cf_mariadb_user = System.getenv("CF_MARIADB_USER");
                if (cf_mariadb_user == null) {
                        System.err.println("CF_MARIADB_USER environment variable is not defined ! Exiting.");
                        System.exit(0);
                } else {
                        System.out.println("CF_MARIADB_USER:" + cf_mariadb_user);
                }
                // check the environment variables needed for the application
                String cf_mariadb_pass = System.getenv("CF_MARIADB_PASS");
                if (cf_mariadb_pass == null) {
                        System.err.println("CF_MARIADB_PASS environment variable is not defined ! Exiting.");
                        System.exit(0);
                } else {
                        System.out.println("CF_MARIADB_PASS:" + cf_mariadb_pass);
                }
                // check the environment variables needed for the application
                String cf_insert_interval = System.getenv("CF_INSERT_INTERVAL");
                if (cf_insert_interval == null) {
                        cf_insert_interval = "30";
                        System.err.println("CF_INSERT_INTERVAL environment variable is not defined ! set cf_insert_interval for " + cf_insert_interval + " seconds");
                } else {
                        System.out.println("CF_INSERT_INTERVAL:" + cf_insert_interval);
                }

                // Database address and credentials
                String DB_URL = "jdbc:mariadb://" + cf_mariadb_server + ":3306/" + cf_mariadb_database;
                String USER = cf_mariadb_user;
                String PASS = cf_mariadb_pass;

                // Get network settings
                String SystemName = InetAddress.getLocalHost().getHostName();
                System.out.println("Hostname: " + SystemName);
                InetAddress iAddress = InetAddress.getLocalHost();
                String currentIp = iAddress.getHostAddress();
                System.out.println("Current IP address : " + currentIp);

		int counter = 1;
		int oper_stat = 0;
		int operation_status;

		do {
			// generate operation status 0/1
                        if (oper_stat % 2 == 0) {
             			operation_status = 1;
            		} else {
                		operation_status = 0;
            		}
			oper_stat = oper_stat + 1;

			// generate random number from 10000 to 99999
			int min = 10000; // Minimum value
        		int max = 99999; // Maximum value
			Random random = new Random();
        		int randomNumber = random.nextInt(max - min + 1) + min;

			// generate random description
			String[] descriptions = {"Java to wysokopoziomowy język programowania", "Java jest najczęściej używana w backendowych systemach aplikacji internetowych", "Java to język programowania i platforma do tworzenia oprogramowania komputerowego wprowadzona przez firme Sun Microsystems"};
			int min_array = 0;
			int max_array = descriptions.length;
			Random randomArray = new Random();
                        int randomIndex = randomArray.nextInt(descriptions.length);

		 	System.out.println("-------------------- " + counter + " -------------------------");

			// Establish the database connection
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formatDateTime = now.format(format);
                        System.out.println("Current datetime:" + formatDateTime);

            		// Create the SQL query
            		String sql = "INSERT INTO clipboard (app_date,hostname,description,rand_number,operation_status) VALUES (?, ?, ?, ?, ?)";

            		// Create a PreparedStatement object to execute the query
            		PreparedStatement statement = conn.prepareStatement(sql);

            		// Set the values for the parameters
            		statement.setString(1, formatDateTime);
            		statement.setString(2, SystemName);
            		statement.setString(3, descriptions[randomIndex]);
            		statement.setInt(4, randomNumber);
            		statement.setInt(5, operation_status);

            		// Execute the query
            		int rowsInserted = statement.executeUpdate();

            		if (rowsInserted > 0) {
				System.out.println("description:" + descriptions[randomIndex]);
				System.out.println("rand_number:" + randomNumber);
				System.out.println("operation_status:" + operation_status);
                		System.out.println("A new row has been inserted successfully.");
            		}

            		// Close the resources
            		statement.close();
            		conn.close();

			// set a delay in wrting to the database
                        int insert_interval = Integer.parseInt(cf_insert_interval);
			Thread.sleep(insert_interval*1000);

			counter = counter + 1;

                } while (true);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
