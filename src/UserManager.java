import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserManager {
    private static int currentPassengerId = -1;
    private Scanner scanner;

    public UserManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public static int getCurrentPassengerId() {
        return currentPassengerId;
    }

    public void registerUser() {
        System.out.println("\n--- User Registration ---");
        // Name: Arunkumar V
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO passengers (name, email) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Registration successful! You can now log in.");
            } else {
                System.out.println("Registration failed. Please try again.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void loginUser() {
        System.out.println("\n--- User Login ---");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        String sql = "SELECT passenger_id, name FROM passengers WHERE email = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                currentPassengerId = rs.getInt("passenger_id");
                String name = rs.getString("name");
                System.out.println("Login successful! Welcome, " + name + ".");
            } else {
                System.out.println("Login failed. Invalid email.");
                currentPassengerId = -1;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            currentPassengerId = -1;
        }
    }
}