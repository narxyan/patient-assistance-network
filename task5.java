import java.sql.*;
import java.util.Scanner;
import java.io.*;

public class IP {

    // Database credentials
    static final String DB_URL = "jdbc:sqlserver://narayan-sql-server.database.windows.net:1433;"
    		+ "database=cs-dsa-4513-sql-db;user=narayan@narayan-sql-server;password={xyz};"
    		+ "encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    static final String USER = "narayan";
    static final String PASS = "xyz";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("\n--- PAN Database Application ---");
                System.out.println("1. Enter a new team");
                System.out.println("2. Enter a new client and associate with teams");
                System.out.println("3. Enter a new volunteer and associate with teams");
                System.out.println("4. Enter volunteer hours for a team");
                System.out.println("5. Enter a new employee and associate with teams");
                System.out.println("6. Enter an expense for an employee");
                System.out.println("7. Enter a new donor and associate with donations");
                System.out.println("8. Retrieve client doctor details");
                System.out.println("9. Retrieve total employee expenses in a period");
                System.out.println("10. Retrieve volunteers in teams supporting a client");
                System.out.println("11. Retrieve teams founded after a date");
                System.out.println("12. Retrieve all people contact and emergency information");
                System.out.println("13. Retrieve employees who are also donors with donations");
                System.out.println("14. Increase salary of employees reporting to multiple teams");
                System.out.println("15. Delete clients without health insurance & low transport need");
                System.out.println("16. Import data from file");
                System.out.println("17. Export data to file");
                System.out.println("18. Quit");
                System.out.print("Select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1 -> enterNewTeam(conn, scanner);
                    case 2 -> enterNewClient(conn, scanner);
                    case 3 -> enterNewVolunteer(conn, scanner);
                    case 4 -> enterVolunteerHours(conn, scanner);
                    case 5 -> enterNewEmployee(conn, scanner);
                    case 6 -> enterEmployeeExpense(conn, scanner);
                    case 7 -> enterNewDonor(conn, scanner);
                    case 8 -> retrieveClientDoctor(conn, scanner);
                    case 9 -> retrieveEmployeeExpenses(conn, scanner);
                    case 10 -> retrieveVolunteersForClient(conn, scanner);
                    case 11 -> retrieveTeamsAfterDate(conn, scanner);
                    case 12 -> retrievePeopleContactInfo(conn);
                    case 13 -> retrieveEmployeesWhoAreDonors(conn);
                    case 14 -> increaseEmployeeSalary(conn);
                    case 15 -> deleteClientsWithoutInsurance(conn);
                    case 16 -> importDataFromFile(conn, scanner);
                    case 17 -> exportDataToFile(conn, scanner);
                    case 18 -> {
                        System.out.println("Quitting the application.");
                        running = false;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Option 1: Enter a new team
    private static void enterNewTeam(Connection conn, Scanner scanner) {
        System.out.print("Enter team name: ");
        String teamName = scanner.nextLine();
        System.out.print("Enter team type: ");
        String teamType = scanner.nextLine();
        System.out.print("Enter date formed (YYYY-MM-DD): ");
        String dateFormed = scanner.nextLine();

        //String query = "INSERT INTO Teams (team_name, team_type, date_formed) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Teams (team_name, team_type, date_formed) VALUES (?, ?, ?)")) {
            pstmt.setString(1, teamName);
            pstmt.setString(2, teamType);
            pstmt.setDate(3, Date.valueOf(dateFormed));
            pstmt.executeUpdate();
            System.out.println("Team added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    

    // Option 2: Enter a new client and associate with one or more teams
    private static void enterNewClient(Connection conn, Scanner scanner) {try {
        // Step 1: Check if SSN already exists in Person table
        System.out.print("Enter client SSN: ");
        String ssn = scanner.nextLine();

        String checkSSNQuery = "SELECT COUNT(*) FROM Person WHERE SSN = ?";
        boolean personExists;

        try (PreparedStatement checkSSNStmt = conn.prepareStatement(checkSSNQuery)) {
            checkSSNStmt.setString(1, ssn);
            try (ResultSet rs = checkSSNStmt.executeQuery()) {
                rs.next();
                personExists = rs.getInt(1) > 0;
            }
        }

        // Step 2: If SSN is not found, prompt for Person and Emergency Contact details
        if (!personExists) {
            // Gather information for Person table
            System.out.print("Enter client name: ");
            String name = scanner.nextLine();

            System.out.print("Enter client gender (M/F): ");
            String gender = scanner.nextLine();

            System.out.print("Enter client profession: ");
            String profession = scanner.nextLine();

            System.out.print("Enter client mailing address: ");
            String address = scanner.nextLine();

            System.out.print("Enter client email address: ");
            String email = scanner.nextLine();

            System.out.print("Enter client phone number: ");
            String phone = scanner.nextLine();

            System.out.print("Is the client on the mailing list? (1 for Yes, 0 for No): ");
            int onMailingList = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Insert into Person table
            //String personQuery = "INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email_address, phone_no, on_mailing_list) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email_address, phone_no, on_mailing_list) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                pstmt1.setString(1, ssn);
                pstmt1.setString(2, name);
                pstmt1.setString(3, gender);
                pstmt1.setString(4, profession);
                pstmt1.setString(5, address);
                pstmt1.setString(6, email);
                pstmt1.setString(7, phone);
                pstmt1.setInt(8, onMailingList);
                pstmt1.executeUpdate();
                System.out.println("Client added to Person table.");
            }

            // Gather information for Emergency Contact table
            System.out.print("How many emergency contacts does the client have? ");
            int numContacts = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            for (int i = 0; i < numContacts; i++) {
                System.out.print("Enter emergency contact name: ");
                String emergencyName = scanner.nextLine();

                System.out.print("Enter emergency contact phone number: ");
                String emergencyPhone = scanner.nextLine();

                System.out.print("Enter relationship with client: ");
                String relation = scanner.nextLine();

               // String emergencyContactQuery = "INSERT INTO Emergency_Contact (SSN, emergency_name, phone_number, relation) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmtEC = conn.prepareStatement("INSERT INTO Emergency_Contact (SSN, emergency_name, phone_number, relation) VALUES (?, ?, ?, ?)")) {
                    pstmtEC.setString(1, ssn);
                    pstmtEC.setString(2, emergencyName);
                    pstmtEC.setString(3, emergencyPhone);
                    pstmtEC.setString(4, relation);
                    pstmtEC.executeUpdate();
                    System.out.println("Emergency contact '" + emergencyName + "' added.");
                }
            }
        } else {
            System.out.println("SSN already exists in the Person table. Skipping personal details input.");
        }

        // Step 3: Insert into Client table (no matter if the SSN already existed or not)
        System.out.print("Enter doctor name: ");
        String doctorName = scanner.nextLine();

        System.out.print("Enter doctor phone number: ");
        String doctorPhone = scanner.nextLine();

        System.out.print("Enter date assigned (YYYY-MM-DD): ");
        String dateAssigned = scanner.nextLine();

        //String clientQuery = "INSERT INTO Client (SSN, doctor_name, doctor_phone_no, date_assigned) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt2 = conn.prepareStatement("INSERT INTO Client (SSN, doctor_name, doctor_phone_no, date_assigned) VALUES (?, ?, ?, ?)")) {
            pstmt2.setString(1, ssn);
            pstmt2.setString(2, doctorName);
            pstmt2.setString(3, doctorPhone);
            pstmt2.setDate(4, Date.valueOf(dateAssigned));
            pstmt2.executeUpdate();
            System.out.println("Client added to Client table.");
        }

        // Step 4: Associate client with insurance policies
        System.out.print("How many insurance policies does the client have? ");
        int numPolicies = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numPolicies; i++) {
            System.out.print("Enter insurance policy ID: ");
            String policyId = scanner.nextLine();

            System.out.print("Enter provider name: ");
            String providerName = scanner.nextLine();

            System.out.print("Enter provider address: ");
            String providerAddress = scanner.nextLine();

            System.out.print("Enter policy type (e.g., health, life, auto): ");
            String policyType = scanner.nextLine();

            // Insert into Insurance_Policy table if not already present
            //String policyQuery = "IF NOT EXISTS (SELECT * FROM Insurance_Policy WHERE policy_id = ?) " +
                                // "INSERT INTO Insurance_Policy (policy_id, provider_name, provider_address, policy_type) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt3 = conn.prepareStatement("IF NOT EXISTS (SELECT * FROM Insurance_Policy WHERE policy_id = ?) " +
                    "INSERT INTO Insurance_Policy (policy_id, provider_name, provider_address, policy_type) VALUES (?, ?, ?, ?)")) {
                pstmt3.setString(1, policyId);
                pstmt3.setString(2, policyId);
                pstmt3.setString(3, providerName);
                pstmt3.setString(4, providerAddress);
                pstmt3.setString(5, policyType);
                pstmt3.executeUpdate();
                System.out.println("Insurance policy '" + policyId + "' added.");
            }

            // Associate client with insurance policy in HasPolicy table
            //String hasPolicyQuery = "INSERT INTO HasPolicy (SSN, policy_id) VALUES (?, ?)";
            try (PreparedStatement pstmt4 = conn.prepareStatement("INSERT INTO HasPolicy (SSN, policy_id) VALUES (?, ?)")) {
                pstmt4.setString(1, ssn);
                pstmt4.setString(2, policyId);
                pstmt4.executeUpdate();
                System.out.println("Client associated with policy '" + policyId + "'.");
            }
        }

        // Step 5: Associate client with needs
        System.out.print("How many needs does the client have? ");
        int numNeeds = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numNeeds; i++) {
            System.out.print("Enter need type (e.g., transportation, food, shopping): ");
            String needType = scanner.nextLine();

            System.out.print("Enter importance level (1-10): ");
            int importance = scanner.nextInt();
            scanner.nextLine(); // Consume newline

           // String needsQuery = "INSERT INTO Needs (SSN, need_type, importance) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt5 = conn.prepareStatement("INSERT INTO Needs (SSN, need_type, importance) VALUES (?, ?, ?)")) {
                pstmt5.setString(1, ssn);
                pstmt5.setString(2, needType);
                pstmt5.setInt(3, importance);
                pstmt5.executeUpdate();
                System.out.println("Need '" + needType + "' with importance " + importance + " added for client.");
            }
        }

        // Step 6: Associate client with teams
        System.out.print("How many teams is the client associated with? ");
        int numTeams = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numTeams; i++) {
            System.out.print("Enter team name to associate with the client: ");
            String teamName = scanner.nextLine();

            System.out.print("Is the client active in this team? (Yes/No): ");
            String isActive = scanner.nextLine().equalsIgnoreCase("Yes") ? "Active" : "Inactive";

            //String teamAssociationQuery = "INSERT INTO assignedTo (SSN, team_name, isActive) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt6 = conn.prepareStatement("INSERT INTO assignedTo (SSN, team_name, isActive) VALUES (?, ?, ?)")) {
                pstmt6.setString(1, ssn);
                pstmt6.setString(2, teamName);
                pstmt6.setString(3, isActive);
                pstmt6.executeUpdate();
                System.out.println("Client associated with team '" + teamName + "'.");
            } catch (SQLException e) {
                System.out.println("Error associating client with team '" + teamName + "': " + e.getMessage());
            }
        }

        System.out.println("Client and all associated information added successfully.");

    } catch (SQLException e) {
        System.out.println("Error adding client: " + e.getMessage());
    }}

    // Additional methods for each option would follow the same format as above
    // Options 3 through 18 should be implemented similarly based on your SQL queries.
    // For brevity, only Options 1 and 2 have been implemented fully in this example.
    // The full implementation would be lengthy, but each option follows this structure.

    // Example placeholders for Options 3 - 18
    private static void enterNewVolunteer(Connection conn, Scanner scanner) { 
    	try {
            // Step 1: Check if SSN already exists in Person table
            System.out.print("Enter volunteer SSN: ");
            String ssn = scanner.nextLine();

            String checkSSNQuery = "SELECT COUNT(*) FROM Person WHERE SSN = ?";
            boolean personExists;

            try (PreparedStatement checkSSNStmt = conn.prepareStatement(checkSSNQuery)) {
                checkSSNStmt.setString(1, ssn);
                try (ResultSet rs = checkSSNStmt.executeQuery()) {
                    rs.next();
                    personExists = rs.getInt(1) > 0;
                }
            }

            // Step 2: If SSN is not found, gather and insert Person details
            if (!personExists) {
                System.out.print("Enter volunteer name: ");
                String name = scanner.nextLine();

                System.out.print("Enter volunteer gender (M/F): ");
                String gender = scanner.nextLine();

                System.out.print("Enter volunteer profession: ");
                String profession = scanner.nextLine();

                System.out.print("Enter volunteer mailing address: ");
                String address = scanner.nextLine();

                System.out.print("Enter volunteer email address: ");
                String email = scanner.nextLine();

                System.out.print("Enter volunteer phone number: ");
                String phone = scanner.nextLine();

                System.out.print("Is the volunteer on the mailing list? (1 for Yes, 0 for No): ");
                int onMailingList = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                // Insert into Person table
                //String personQuery = "INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email_address, phone_no, on_mailing_list) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email_address, phone_no, on_mailing_list) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                    pstmt1.setString(1, ssn);
                    pstmt1.setString(2, name);
                    pstmt1.setString(3, gender);
                    pstmt1.setString(4, profession);
                    pstmt1.setString(5, address);
                    pstmt1.setString(6, email);
                    pstmt1.setString(7, phone);
                    pstmt1.setInt(8, onMailingList);
                    pstmt1.executeUpdate();
                    System.out.println("Volunteer added to Person table.");
                }

                // Step 3: Add Emergency Contacts for the volunteer
                System.out.print("How many emergency contacts does the volunteer have? ");
                int numContacts = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                for (int i = 0; i < numContacts; i++) {
                    System.out.print("Enter emergency contact name: ");
                    String emergencyName = scanner.nextLine();

                    System.out.print("Enter emergency contact phone number: ");
                    String emergencyPhone = scanner.nextLine();

                    System.out.print("Enter relationship with volunteer: ");
                    String relation = scanner.nextLine();

                    //String emergencyContactQuery = "INSERT INTO Emergency_Contact (SSN, emergency_name, phone_number, relation) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmtEC = conn.prepareStatement("INSERT INTO Emergency_Contact (SSN, emergency_name, phone_number, relation) VALUES (?, ?, ?, ?)")) {
                        pstmtEC.setString(1, ssn);
                        pstmtEC.setString(2, emergencyName);
                        pstmtEC.setString(3, emergencyPhone);
                        pstmtEC.setString(4, relation);
                        pstmtEC.executeUpdate();
                        System.out.println("Emergency contact '" + emergencyName + "' added.");
                    }
                }
            } else {
                System.out.println("SSN already exists in the Person table. Skipping personal and emergency contact details input.");
            }

            // Step 4: Insert into Volunteer table
            System.out.print("Enter date joined (YYYY-MM-DD): ");
            String dateJoined = scanner.nextLine();

            System.out.print("Enter date of last training (YYYY-MM-DD): ");
            String dateOfTraining = scanner.nextLine();

            System.out.print("Enter location of training: ");
            String trainingLocation = scanner.nextLine();

            //String volunteerQuery = "INSERT INTO Volunteer (SSN, date_joined, date_of_training, location_of_training) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt2 = conn.prepareStatement("INSERT INTO Volunteer (SSN, date_joined, date_of_training, location_of_training) VALUES (?, ?, ?, ?)")) {
                pstmt2.setString(1, ssn);
                pstmt2.setDate(2, Date.valueOf(dateJoined));
                pstmt2.setDate(3, Date.valueOf(dateOfTraining));
                pstmt2.setString(4, trainingLocation);
                pstmt2.executeUpdate();
                System.out.println("Volunteer added to Volunteer table.");
            }

            // Step 5: Associate volunteer with teams
            System.out.print("How many teams is the volunteer associated with? ");
            int numTeams = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            for (int i = 0; i < numTeams; i++) {
                System.out.print("Enter team name to associate with the volunteer: ");
                String teamName = scanner.nextLine();

                System.out.print("Is the volunteer active in this team? (Yes/No): ");
                String isActive = scanner.nextLine().equalsIgnoreCase("Yes") ? "Active" : "Inactive";

                //String teamAssociationQuery = "INSERT INTO worksIn (SSN, team_name, isActive) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt3 = conn.prepareStatement("INSERT INTO worksIn (SSN, team_name, isActive) VALUES (?, ?, ?)")) {
                    pstmt3.setString(1, ssn);
                    pstmt3.setString(2, teamName);
                    pstmt3.setString(3, isActive);
                    pstmt3.executeUpdate();
                    System.out.println("Volunteer associated with team '" + teamName + "'.");
                } catch (SQLException e) {
                    System.out.println("Error associating volunteer with team '" + teamName + "': " + e.getMessage());
                }
            }

            System.out.println("Volunteer and all associated information added successfully.");

        } catch (SQLException e) {
            System.out.println("Error adding volunteer: " + e.getMessage());
        }
    	}
    private static void enterVolunteerHours(Connection conn, Scanner scanner) { 
    	try {
            // Prompt for volunteer SSN
            System.out.print("Enter volunteer SSN: ");
            String ssn = scanner.nextLine();

            // Prompt for team name
            System.out.print("Enter team name: ");
            String teamName = scanner.nextLine();

            // Prompt for month of work
            System.out.print("Enter the month for hours worked (e.g., 'November'): ");
            String workedMonth = scanner.nextLine();

            // Prompt for hours worked
            System.out.print("Enter the number of hours worked: ");
            int hoursWorked = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Update or insert hours worked in the worksIn table
            //String query = "UPDATE worksIn SET hours_worked = ? WHERE SSN = ? AND team_name = ? AND worked_month = ?";

            try (PreparedStatement pstmt = conn.prepareStatement("UPDATE worksIn SET hours_worked = ? WHERE SSN = ? AND team_name = ? AND worked_month = ?")) {
                pstmt.setInt(1, hoursWorked);
                pstmt.setString(2, ssn);
                pstmt.setString(3, teamName);
                pstmt.setString(4, workedMonth);

                int rowsAffected = pstmt.executeUpdate();

                // Check if update was successful
                if (rowsAffected > 0) {
                    System.out.println("Hours worked updated successfully.");
                } else {
                    // If no rows were updated, the record might not exist, so insert it
                    //String insertQuery = "INSERT INTO worksIn (SSN, team_name, hours_worked, worked_month, isActive) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO worksIn (SSN, team_name, hours_worked, worked_month, isActive) VALUES (?, ?, ?, ?, ?)")) {
                        insertStmt.setString(1, ssn);
                        insertStmt.setString(2, teamName);
                        insertStmt.setInt(3, hoursWorked);
                        insertStmt.setString(4, workedMonth);
                        insertStmt.setString(5, "Active"); // Assuming volunteer is active by default
                        insertStmt.executeUpdate();
                        System.out.println("Hours worked added successfully.");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error updating hours worked: " + e.getMessage());
        }
    }
    private static void enterNewEmployee(Connection conn, Scanner scanner) { try {
        // Step 1: Check if SSN already exists in Person table
        System.out.print("Enter employee SSN: ");
        String ssn = scanner.nextLine();

       // String checkSSNQuery = "SELECT COUNT(*) FROM Person WHERE SSN = ?";
        boolean personExists;

        try (PreparedStatement checkSSNStmt = conn.prepareStatement("SELECT COUNT(*) FROM Person WHERE SSN = ?")) {
            checkSSNStmt.setString(1, ssn);
            try (ResultSet rs = checkSSNStmt.executeQuery()) {
                rs.next();
                personExists = rs.getInt(1) > 0;
            }
        }

        // Step 2: If SSN is not found, gather and insert Person details
        if (!personExists) {
            System.out.print("Enter employee name: ");
            String name = scanner.nextLine();

            System.out.print("Enter employee gender (M/F): ");
            String gender = scanner.nextLine();

            System.out.print("Enter employee profession: ");
            String profession = scanner.nextLine();

            System.out.print("Enter employee mailing address: ");
            String address = scanner.nextLine();

            System.out.print("Enter employee email address: ");
            String email = scanner.nextLine();

            System.out.print("Enter employee phone number: ");
            String phone = scanner.nextLine();

            System.out.print("Is the employee on the mailing list? (1 for Yes, 0 for No): ");
            int onMailingList = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Insert into Person table
            //String personQuery = "INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email_address, phone_no, on_mailing_list) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email_address, phone_no, on_mailing_list) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                pstmt1.setString(1, ssn);
                pstmt1.setString(2, name);
                pstmt1.setString(3, gender);
                pstmt1.setString(4, profession);
                pstmt1.setString(5, address);
                pstmt1.setString(6, email);
                pstmt1.setString(7, phone);
                pstmt1.setInt(8, onMailingList);
                pstmt1.executeUpdate();
                System.out.println("Employee added to Person table.");
            }

            // Step 3: Add Emergency Contacts for the employee
            System.out.print("How many emergency contacts does the employee have? ");
            int numContacts = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            for (int i = 0; i < numContacts; i++) {
                System.out.print("Enter emergency contact name: ");
                String emergencyName = scanner.nextLine();

                System.out.print("Enter emergency contact phone number: ");
                String emergencyPhone = scanner.nextLine();

                System.out.print("Enter relationship with employee: ");
                String relation = scanner.nextLine();

                //String emergencyContactQuery = "INSERT INTO Emergency_Contact (SSN, emergency_name, phone_number, relation) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmtEC = conn.prepareStatement("INSERT INTO Emergency_Contact (SSN, emergency_name, phone_number, relation) VALUES (?, ?, ?, ?)")) {
                    pstmtEC.setString(1, ssn);
                    pstmtEC.setString(2, emergencyName);
                    pstmtEC.setString(3, emergencyPhone);
                    pstmtEC.setString(4, relation);
                    pstmtEC.executeUpdate();
                    System.out.println("Emergency contact '" + emergencyName + "' added.");
                }
            }
        } else {
            System.out.println("SSN already exists in the Person table. Skipping personal and emergency contact details input.");
        }

        // Step 4: Insert into Employee table
        System.out.print("Enter salary: ");
        double salary = scanner.nextDouble();

        System.out.print("Enter marital status: ");
        String maritalStatus = scanner.next();

        System.out.print("Enter hire date (YYYY-MM-DD): ");
        String hireDate = scanner.next();
        scanner.nextLine(); // Consume newline

        //String employeeQuery = "INSERT INTO Employee (SSN, salary, marital_status, hire_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt2 = conn.prepareStatement("INSERT INTO Employee (SSN, salary, marital_status, hire_date) VALUES (?, ?, ?, ?)")) {
            pstmt2.setString(1, ssn);
            pstmt2.setDouble(2, salary);
            pstmt2.setString(3, maritalStatus);
            pstmt2.setDate(4, Date.valueOf(hireDate));
            pstmt2.executeUpdate();
            System.out.println("Employee added to Employee table.");
        }

        // Step 5: Associate employee with teams
        System.out.print("How many teams does the employee manage? ");
        int numTeams = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numTeams; i++) {
            System.out.print("Enter team name managed by the employee: ");
            String teamName = scanner.nextLine();

            System.out.print("Enter report date for this team (YYYY-MM-DD): ");
            String reportDate = scanner.nextLine();

            System.out.print("Enter report description for this team: ");
            String reportDescription = scanner.nextLine();

            //String teamAssociationQuery = "INSERT INTO reportTo (team_name, SSN, report_date, report_description) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt3 = conn.prepareStatement("INSERT INTO reportTo (team_name, SSN, report_date, report_description) VALUES (?, ?, ?, ?)")) {
                pstmt3.setString(1, teamName);
                pstmt3.setString(2, ssn);
                pstmt3.setDate(3, Date.valueOf(reportDate));
                pstmt3.setString(4, reportDescription);
                pstmt3.executeUpdate();
                System.out.println("Employee associated with team '" + teamName + "'.");
            } catch (SQLException e) {
                System.out.println("Error associating employee with team '" + teamName + "': " + e.getMessage());
            }
        }

        System.out.println("Employee and all associated information added successfully.");

    } catch (SQLException e) {
        System.out.println("Error adding employee: " + e.getMessage());
    } }
    private static void enterEmployeeExpense(Connection conn, Scanner scanner) { try {
        // Step 1: Gather employee expense details
        System.out.print("Enter employee SSN: ");
        String ssn = scanner.nextLine();

        System.out.print("Enter expense amount: ");
        double expenseAmount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter expense date (YYYY-MM-DD): ");
        String expenseDate = scanner.nextLine();

        System.out.print("Enter expense description: ");
        String expenseDescription = scanner.nextLine();

        // Step 2: Insert expense into Emp_Expenses table
        //String expenseQuery = "INSERT INTO Emp_Expenses (SSN, expense_amount, expense_date, expense_description) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Emp_Expenses (SSN, expense_amount, expense_date, expense_description) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, ssn);
            pstmt.setDouble(2, expenseAmount);
            pstmt.setDate(3, Date.valueOf(expenseDate));
            pstmt.setString(4, expenseDescription);
            pstmt.executeUpdate();
            System.out.println("Employee expense recorded successfully.");
        }

    } catch (SQLException e) {
        System.out.println("Error recording employee expense: " + e.getMessage());
    } }
    private static void enterNewDonor(Connection conn, Scanner scanner) {  try {
        // Step 1: Check if SSN already exists in Person table
        System.out.print("Enter donor SSN: ");
        String ssn = scanner.nextLine();

        //String checkSSNQuery = "SELECT COUNT(*) FROM Person WHERE SSN = ?";
        boolean personExists;

        try (PreparedStatement checkSSNStmt = conn.prepareStatement("SELECT COUNT(*) FROM Person WHERE SSN = ?")) {
            checkSSNStmt.setString(1, ssn);
            try (ResultSet rs = checkSSNStmt.executeQuery()) {
                rs.next();
                personExists = rs.getInt(1) > 0;
            }
        }

        // Step 2: If SSN is not found, gather and insert Person details
        if (!personExists) {
            System.out.print("Enter donor name: ");
            String name = scanner.nextLine();

            System.out.print("Enter donor gender (M/F): ");
            String gender = scanner.nextLine();

            System.out.print("Enter donor profession: ");
            String profession = scanner.nextLine();

            System.out.print("Enter donor mailing address: ");
            String address = scanner.nextLine();

            System.out.print("Enter donor email address: ");
            String email = scanner.nextLine();

            System.out.print("Enter donor phone number: ");
            String phone = scanner.nextLine();

            System.out.print("Is the donor on the mailing list? (1 for Yes, 0 for No): ");
            int onMailingList = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Insert into Person table
            //String personQuery = "INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email_address, phone_no, on_mailing_list) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email_address, phone_no, on_mailing_list) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                pstmt1.setString(1, ssn);
                pstmt1.setString(2, name);
                pstmt1.setString(3, gender);
                pstmt1.setString(4, profession);
                pstmt1.setString(5, address);
                pstmt1.setString(6, email);
                pstmt1.setString(7, phone);
                pstmt1.setInt(8, onMailingList);
                pstmt1.executeUpdate();
                System.out.println("Donor added to Person table.");
            }

            // Step 3: Add Emergency Contacts for the donor
            System.out.print("How many emergency contacts does the donor have? ");
            int numContacts = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            for (int i = 0; i < numContacts; i++) {
                System.out.print("Enter emergency contact name: ");
                String emergencyName = scanner.nextLine();

                System.out.print("Enter emergency contact phone number: ");
                String emergencyPhone = scanner.nextLine();

                System.out.print("Enter relationship with donor: ");
                String relation = scanner.nextLine();

                //String emergencyContactQuery = "INSERT INTO Emergency_Contact (SSN, emergency_name, phone_number, relation) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmtEC = conn.prepareStatement("INSERT INTO Emergency_Contact (SSN, emergency_name, phone_number, relation) VALUES (?, ?, ?, ?)")) {
                    pstmtEC.setString(1, ssn);
                    pstmtEC.setString(2, emergencyName);
                    pstmtEC.setString(3, emergencyPhone);
                    pstmtEC.setString(4, relation);
                    pstmtEC.executeUpdate();
                    System.out.println("Emergency contact '" + emergencyName + "' added.");
                }
            }
        } else {
            System.out.println("SSN already exists in the Person table. Skipping personal and emergency contact details input.");
        }

        // Step 4: Insert into Donor table
        System.out.print("Does the donor wish to remain anonymous? (1 for Yes, 0 for No): ");
        int anonymous = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        //String donorQuery = "INSERT INTO Donor (SSN, anonymous) VALUES (?, ?)";
        try (PreparedStatement pstmt2 = conn.prepareStatement("INSERT INTO Donor (SSN, anonymous) VALUES (?, ?)")) {
            pstmt2.setString(1, ssn);
            pstmt2.setInt(2, anonymous);
            pstmt2.executeUpdate();
            System.out.println("Donor added to Donor table.");
        }

        // Step 5: Associate donor with donations
        System.out.print("How many donations does the donor have? ");
        int numDonations = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numDonations; i++) {
            System.out.print("Enter donation date (YYYY-MM-DD): ");
            String donationDate = scanner.nextLine();

            System.out.print("Enter donation type (Check or Credit): ");
            String donationType = scanner.nextLine();

            System.out.print("Enter donation amount: ");
            double donationAmount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter campaign name (leave empty if none): ");
            String campaignName = scanner.nextLine();

            if (donationType.equalsIgnoreCase("Check")) {
                System.out.print("Enter check number: ");
                String checkNumber = scanner.nextLine();

                //String checkQuery = "INSERT INTO [Check] (SSN, donation_date, donation_type, donation_amount, campaign_name, check_number) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt3 = conn.prepareStatement("INSERT INTO [Check] (SSN, donation_date, donation_type, donation_amount, campaign_name, check_number) VALUES (?, ?, ?, ?, ?, ?)")) {
                    pstmt3.setString(1, ssn);
                    pstmt3.setDate(2, Date.valueOf(donationDate));
                    pstmt3.setString(3, donationType);
                    pstmt3.setDouble(4, donationAmount);
                    pstmt3.setString(5, campaignName.isEmpty() ? null : campaignName);
                    pstmt3.setString(6, checkNumber);
                    pstmt3.executeUpdate();
                    System.out.println("Donation via check added.");
                }

            } else if (donationType.equalsIgnoreCase("Credit")) {
                System.out.print("Enter credit card number: ");
                String creditCardNo = scanner.nextLine();

                System.out.print("Enter credit card type (e.g., Visa, MasterCard): ");
                String creditCardType = scanner.nextLine();

                System.out.print("Enter credit card expiry date (YYYY-MM-DD): ");
                String expiryDate = scanner.nextLine();

                //String creditCardQuery = "INSERT INTO Credit_Card (SSN, donation_date, donation_type, donation_amount, campaign_name, credit_card_no, credit_card_type, credit_card_expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt4 = conn.prepareStatement("INSERT INTO Credit_Card (SSN, donation_date, donation_type, donation_amount, campaign_name, credit_card_no, credit_card_type, credit_card_expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                    pstmt4.setString(1, ssn);
                    pstmt4.setDate(2, Date.valueOf(donationDate));
                    pstmt4.setString(3, donationType);
                    pstmt4.setDouble(4, donationAmount);
                    pstmt4.setString(5, campaignName.isEmpty() ? null : campaignName);
                    pstmt4.setString(6, creditCardNo);
                    pstmt4.setString(7, creditCardType);
                    pstmt4.setDate(8, Date.valueOf(expiryDate));
                    pstmt4.executeUpdate();
                    System.out.println("Donation via credit card added.");
                }
            } else {
                System.out.println("Invalid donation type. Only 'Check' and 'Credit' are accepted.");
            }
        }

        System.out.println("Donor and all associated donation information added successfully.");

    } catch (SQLException e) {
        System.out.println("Error adding donor: " + e.getMessage());
    } }
    private static void retrieveClientDoctor(Connection conn, Scanner scanner) { try {
        // Prompt for client SSN
        System.out.print("Enter client SSN: ");
        String ssn = scanner.nextLine();

        // Query to retrieve doctor's name and phone number for the specified client
        String query = "SELECT doctor_name, doctor_phone_no FROM Client WHERE SSN = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, ssn);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve and print doctor's name and phone number
                    String doctorName = rs.getString("doctor_name");
                    String doctorPhone = rs.getString("doctor_phone_no");
                    System.out.println("Doctor's Name: " + doctorName);
                    System.out.println("Doctor's Phone Number: " + doctorPhone);
                } else {
                    System.out.println("No doctor information found for the specified client SSN.");
                }
            }
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving doctor information: " + e.getMessage());
    } }
    private static void retrieveEmployeeExpenses(Connection conn, Scanner scanner) { try {
        // Prompt for the start and end dates of the period
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();

        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();

        // Query to retrieve total expenses per employee within the specified date range
        String query = """
                SELECT E.SSN, P.person_name, SUM(EE.expense_amount) AS total_expense
                FROM Emp_Expenses EE
                JOIN Employee E ON EE.SSN = E.SSN
                JOIN Person P ON E.SSN = P.SSN
                WHERE EE.expense_date BETWEEN ? AND ?
                GROUP BY E.SSN, P.person_name
                ORDER BY total_expense DESC
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set the parameters for the date range
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.printf("%-15s %-20s %15s%n", "Employee SSN", "Employee Name", "Total Expense");
                System.out.println("-----------------------------------------------------------");

                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    String ssn = rs.getString("SSN");
                    String name = rs.getString("person_name");
                    double totalExpense = rs.getDouble("total_expense");
                    System.out.printf("%-15s %-20s %15.2f%n", ssn, name, totalExpense);
                }

                if (!hasResults) {
                    System.out.println("No expenses found for the specified date range.");
                }
            }
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving employee expenses: " + e.getMessage());
    } }
    private static void retrieveVolunteersForClient(Connection conn, Scanner scanner) { try {
        // Prompt for client SSN
        System.out.print("Enter client SSN: ");
        String clientSSN = scanner.nextLine();

        // Query to retrieve volunteers associated with teams supporting the specified client
       // String query = """
         //       SELECT DISTINCT P.person_name AS volunteer_name, V.SSN AS volunteer_ssn
           //     FROM worksIn W
             //   JOIN Volunteer V ON W.SSN = V.SSN
               // JOIN Person P ON V.SSN = P.SSN
                //JOIN assignedTo A ON W.team_name = A.team_name
                //WHERE A.SSN = ?
                //""";

        try (PreparedStatement pstmt = conn.prepareStatement("""
                SELECT DISTINCT P.person_name AS volunteer_name, V.SSN AS volunteer_ssn
                FROM worksIn W
                JOIN Volunteer V ON W.SSN = V.SSN
                JOIN Person P ON V.SSN = P.SSN
                JOIN assignedTo A ON W.team_name = A.team_name
                WHERE A.SSN = ?
                """)) {
            pstmt.setString(1, clientSSN);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.printf("%-15s %-20s%n", "Volunteer SSN", "Volunteer Name");
                System.out.println("----------------------------------------");

                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    String volunteerSSN = rs.getString("volunteer_ssn");
                    String volunteerName = rs.getString("volunteer_name");
                    System.out.printf("%-15s %-20s%n", volunteerSSN, volunteerName);
                }

                if (!hasResults) {
                    System.out.println("No volunteers found for teams supporting the specified client.");
                }
            }
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving volunteers: " + e.getMessage());
    } }
    private static void retrieveTeamsAfterDate(Connection conn, Scanner scanner) { try {
        // Prompt for the founding date
        System.out.print("Enter the date (YYYY-MM-DD) to find teams founded after: ");
        String foundedAfterDate = scanner.nextLine();

        // Query to retrieve team names founded after the specified date
        String query = """
                SELECT team_name, date_formed
                FROM Teams
                WHERE date_formed > ?
                ORDER BY date_formed ASC
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, Date.valueOf(foundedAfterDate)); // Set the date parameter

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.printf("%-20s %-15s%n", "Team Name", "Date Formed");
                System.out.println("----------------------------------------");

                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    String teamName = rs.getString("team_name");
                    Date dateFormed = rs.getDate("date_formed");
                    System.out.printf("%-20s %-15s%n", teamName, dateFormed.toString());
                }

                if (!hasResults) {
                    System.out.println("No teams were founded after the specified date.");
                }
            }
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving teams: " + e.getMessage());
    } }
    private static void retrievePeopleContactInfo(Connection conn) { try {
        // Query to retrieve person and their emergency contact information
        String query = """
                SELECT P.SSN, P.person_name, P.gender, P.profession, P.mailing_address, P.email_address, P.phone_no, EC.emergency_name, EC.phone_number AS emergency_phone, EC.relation
                FROM Person P
                LEFT JOIN Emergency_Contact EC ON P.SSN = EC.SSN
                ORDER BY P.person_name
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.printf("%-15s %-20s %-10s %-15s %-30s %-25s %-15s %-20s %-15s %-15s%n",
                        "SSN", "Name", "Gender", "Profession", "Address", "Email", "Phone", "Emergency Contact", "Emergency Phone", "Relation");
                System.out.println("--------------------------------------------------------------------------------------------------------------");

                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    String ssn = rs.getString("SSN");
                    String name = rs.getString("person_name");
                    String gender = rs.getString("gender");
                    String profession = rs.getString("profession");
                    String address = rs.getString("mailing_address");
                    String email = rs.getString("email_address");
                    String phone = rs.getString("phone_no");
                    String emergencyName = rs.getString("emergency_name");
                    String emergencyPhone = rs.getString("emergency_phone");
                    String relation = rs.getString("relation");

                    System.out.printf("%-15s %-20s %-10s %-15s %-30s %-25s %-15s %-20s %-15s %-15s%n",
                            ssn, name, gender, profession, address, email, phone,
                            (emergencyName != null ? emergencyName : "N/A"),
                            (emergencyPhone != null ? emergencyPhone : "N/A"),
                            (relation != null ? relation : "N/A"));
                }

                if (!hasResults) {
                    System.out.println("No people found in the database.");
                }
            }
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving people contact information: " + e.getMessage());
    } }
    private static void retrieveEmployeesWhoAreDonors(Connection conn) { try {
        // SQL query for retrieving employee donations from both Check and Credit_Card tables
        //String query = "SELECT p.person_name, SUM(donation_amount) AS total_donation, d.anonymous " +
          //             "FROM Donor d " +
            //           "JOIN Person p ON d.SSN = p.SSN " +
              //         "JOIN Employee e ON d.SSN = e.SSN " +
                //       "JOIN (" +
                  //     "    SELECT SSN, donation_amount FROM [Check] " +
                    //   "    UNION ALL " +
                      // "    SELECT SSN, donation_amount FROM Credit_Card" +
                      // ") AS donations ON d.SSN = donations.SSN " +
                     //  "GROUP BY p.person_name, d.anonymous " +
                      // "ORDER BY total_donation DESC";

        // Prepare the statement and set a timeout of 60 seconds
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT p.person_name, SUM(donation_amount) AS total_donation, d.anonymous " +
                "FROM Donor d " +
                "JOIN Person p ON d.SSN = p.SSN " +
                "JOIN Employee e ON d.SSN = e.SSN " +
                "JOIN (" +
                "    SELECT SSN, donation_amount FROM [Check] " +
                "    UNION ALL " +
                "    SELECT SSN, donation_amount FROM Credit_Card" +
                ") AS donations ON d.SSN = donations.SSN " +
                "GROUP BY p.person_name, d.anonymous " +
                "ORDER BY total_donation DESC")) {
            pstmt.setQueryTimeout(60); // Set timeout to 60 seconds
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Employee Donations:");
                System.out.println("Name\tTotal Donation\tAnonymous");

                while (rs.next()) {
                    String name = rs.getString("person_name");
                    double totalDonation = rs.getDouble("total_donation");
                    boolean anonymous = rs.getBoolean("anonymous");

                    System.out.printf("%s\t%.2f\t%s%n", name, totalDonation, anonymous ? "Yes" : "No");
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving employee donations: " + e.getMessage());
    } }
    private static void increaseEmployeeSalary(Connection conn) { try {
        // Query to increase salary by 10% for employees managing more than one team
        String query = """
                UPDATE Employee
                SET salary = salary * 1.10
                WHERE SSN IN (
                    SELECT E.SSN
                    FROM Employee E
                    JOIN reportTo R ON E.SSN = R.SSN
                    GROUP BY E.SSN
                    HAVING COUNT(R.team_name) > 1
                )
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Salary increased by 10% for " + rowsAffected + " employees managing more than one team.");
        }

    } catch (SQLException e) {
        System.out.println("Error updating employee salaries: " + e.getMessage());
    } }
    private static void deleteClientsWithoutInsurance(Connection conn) { try {
        conn.setAutoCommit(false); // Start a transaction

        // Step 1: Find clients to delete based on conditions
       // String findClientsQuery = "SELECT c.SSN FROM Client c " +
         //                         "LEFT JOIN Needs n ON c.SSN = n.SSN AND n.need_type = 'Transportation' " +
           //                       "LEFT JOIN HasPolicy hp ON c.SSN = hp.SSN " +
             //                     "WHERE hp.policy_id IS NULL AND (n.importance IS NULL OR n.importance < 5)";
        try (PreparedStatement findStmt = conn.prepareStatement("SELECT c.SSN FROM Client c " +
                "LEFT JOIN Needs n ON c.SSN = n.SSN AND n.need_type = 'Transportation' " +
                "LEFT JOIN HasPolicy hp ON c.SSN = hp.SSN " +
                "WHERE hp.policy_id IS NULL AND (n.importance IS NULL OR n.importance < 5)");
             ResultSet rs = findStmt.executeQuery()) {

            while (rs.next()) {
                String ssn = rs.getString("SSN");

                // Step 2: Delete related records in HasPolicy and Needs tables for each client SSN
                deleteClientAssociations(conn, ssn);

                // Step 3: Delete from Client table
            //    String deleteClientQuery = "DELETE FROM Client WHERE SSN = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM Client WHERE SSN = ?")) {
                    deleteStmt.setString(1, ssn);
                    deleteStmt.executeUpdate();
                    System.out.println("Deleted client with SSN: " + ssn);
                }
            }
        }

        conn.commit(); // Commit transaction
        System.out.println("All eligible clients without health insurance and low transportation needs have been deleted.");

    } catch (SQLException e) {
        try {
            conn.rollback(); // Roll back transaction on error
        } catch (SQLException rollbackEx) {
            System.out.println("Rollback failed: " + rollbackEx.getMessage());
        }
        System.out.println("Error deleting clients: " + e.getMessage());
    } finally {
        try {
            conn.setAutoCommit(true); // Restore auto-commit mode
        } catch (SQLException ex) {
            System.out.println("Failed to reset auto-commit mode: " + ex.getMessage());
        }
    }
}

// Helper method to delete related records in HasPolicy and Needs tables
private static void deleteClientAssociations(Connection conn, String ssn) throws SQLException {
    // Delete from HasPolicy
   // String deletePolicyQuery = "DELETE FROM HasPolicy WHERE SSN = ?";
    try (PreparedStatement deletePolicyStmt = conn.prepareStatement("DELETE FROM HasPolicy WHERE SSN = ?")) {
        deletePolicyStmt.setString(1, ssn);
        deletePolicyStmt.executeUpdate();
        System.out.println("Deleted associated policies for SSN: " + ssn);
    }

    // Delete from Needs
    //String deleteNeedsQuery = "DELETE FROM Needs WHERE SSN = ?";
    try (PreparedStatement deleteNeedsStmt = conn.prepareStatement("DELETE FROM Needs WHERE SSN = ?")) {
        deleteNeedsStmt.setString(1, ssn);
        deleteNeedsStmt.executeUpdate();
        System.out.println("Deleted associated needs for SSN: " + ssn);
    } 
	}
    
	private static void importDataFromFile(Connection conn, Scanner scanner) { System.out.print("Enter the input file name: ");
    String fileName = scanner.nextLine();

    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String line;
        
        // Assuming each line in the file is formatted as: team_name,team_type,date_formed
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            String teamName = data[0].trim();
            String teamType = data[1].trim();
            String dateFormed = data[2].trim();

            //String insertQuery = "INSERT INTO Teams (team_name, team_type, date_formed) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Teams (team_name, team_type, date_formed) VALUES (?, ?, ?)")) {
                pstmt.setString(1, teamName);
                pstmt.setString(2, teamType);
                pstmt.setDate(3, java.sql.Date.valueOf(dateFormed));
                pstmt.executeUpdate();
                System.out.println("Inserted team: " + teamName);
            } catch (SQLException e) {
                System.out.println("Error inserting team: " + teamName + " - " + e.getMessage());
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    } }
    private static void exportDataToFile(Connection conn, Scanner scanner) { System.out.print("Enter the output file name (including path): ");
    String fileName = scanner.nextLine();

    String query = "SELECT person_name, mailing_address FROM Person WHERE on_mailing_list = 1";

    try (PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet rs = pstmt.executeQuery();
         BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

        writer.write("Name, Mailing Address");
        writer.newLine();

        while (rs.next()) {
            String name = rs.getString("person_name");
            String address = rs.getString("mailing_address");
            writer.write(name + ", " + address);
            writer.newLine();
        }

        System.out.println("Mailing list exported successfully to " + fileName);

    } catch (SQLException e) {
        System.out.println("Error retrieving mailing list: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("Error writing to file: " + e.getMessage());
    } }
}

