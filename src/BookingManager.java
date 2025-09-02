import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BookingManager {
    private Scanner scanner;

    public BookingManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public void viewBuses() {
        System.out.println("\n--- Available Buses ---");
        String sql = "SELECT * FROM buses";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("------------------------------------");
                System.out.println("Bus ID: " + rs.getInt("bus_id"));
                System.out.println("Bus Name: " + rs.getString("bus_name"));
                System.out.println("Route: " + rs.getString("route"));
                System.out.println("Capacity: " + rs.getInt("capacity"));
                System.out.println("Available Seats: " + rs.getInt("available_seats"));
                System.out.println("------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void bookTicket(int passengerId) {
        if (passengerId == -1) {
            System.out.println("\nPlease log in first to book a ticket.");
            return;
        }

        viewBuses();
        System.out.println("\n--- Book a Ticket ---");
        System.out.print("Enter Bus ID to book: ");
        try {
            int busId = Integer.parseInt(scanner.nextLine());

            String selectSql = "SELECT available_seats FROM buses WHERE bus_id = ?";
            String updateSql = "UPDATE buses SET available_seats = ? WHERE bus_id = ?";
            String insertSql = "INSERT INTO bookings (passenger_id, bus_id, seat_number) VALUES (?, ?, ?)";

            try (Connection conn = DatabaseManager.getConnection()) {
                conn.setAutoCommit(false);

                try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                    selectStmt.setInt(1, busId);
                    ResultSet rs = selectStmt.executeQuery();
                    if (rs.next()) {
                        int availableSeats = rs.getInt("available_seats");
                        if (availableSeats > 0) {
                            System.out.print("Enter seat number (1-" + availableSeats + "): ");
                            int seatNumber = Integer.parseInt(scanner.nextLine());
                            if (seatNumber > 0 && seatNumber <= availableSeats) {
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                                    updateStmt.setInt(1, availableSeats - 1);
                                    updateStmt.setInt(2, busId);
                                    updateStmt.executeUpdate();
                                }
                                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                                    insertStmt.setInt(1, passengerId);
                                    insertStmt.setInt(2, busId);
                                    insertStmt.setInt(3, seatNumber);
                                    insertStmt.executeUpdate();
                                }
                                conn.commit();
                                System.out.println("Ticket booked successfully!");
                            } else {
                                System.out.println("Invalid seat number. Booking failed.");
                                conn.rollback();
                            }
                        } else {
                            System.out.println("No seats available on this bus. Booking failed.");
                        }
                    } else {
                        System.out.println("Bus not found.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public void viewMyBookings(int passengerId) {
        if (passengerId == -1) {
            System.out.println("\nPlease log in first to view your bookings.");
            return;
        }

        System.out.println("\n--- My Bookings ---");
        String sql = "SELECT b.booking_id, bu.bus_name, bu.route, b.seat_number, b.booking_date " +
                "FROM bookings b JOIN buses bu ON b.bus_id = bu.bus_id " +
                "WHERE b.passenger_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, passengerId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("You have no current bookings.");
            } else {
                while (rs.next()) {
                    System.out.println("------------------------------------");
                    System.out.println("Booking ID: " + rs.getInt("booking_id"));
                    System.out.println("Bus Name: " + rs.getString("bus_name"));
                    System.out.println("Route: " + rs.getString("route"));
                    System.out.println("Seat Number: " + rs.getInt("seat_number"));
                    System.out.println("Booking Date: " + rs.getTimestamp("booking_date"));
                    System.out.println("------------------------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void cancelBooking(int passengerId) {
        if (passengerId == -1) {
            System.out.println("\nPlease log in first to cancel a booking.");
            return;
        }

        viewMyBookings(passengerId);
        System.out.println("\n--- Cancel Booking ---");
        System.out.print("Enter Booking ID to cancel: ");
        try {
            int bookingId = Integer.parseInt(scanner.nextLine());

            String selectSql = "SELECT bus_id FROM bookings WHERE booking_id = ? AND passenger_id = ?";
            String deleteSql = "DELETE FROM bookings WHERE booking_id = ?";
            String updateBusSql = "UPDATE buses SET available_seats = available_seats + 1 WHERE bus_id = ?";

            try (Connection conn = DatabaseManager.getConnection()) {
                conn.setAutoCommit(false);

                int busId = -1;
                try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                    selectStmt.setInt(1, bookingId);
                    selectStmt.setInt(2, passengerId);
                    ResultSet rs = selectStmt.executeQuery();
                    if (rs.next()) {
                        busId = rs.getInt("bus_id");
                    } else {
                        System.out.println("Booking not found or does not belong to your account.");
                        conn.rollback();
                        return;
                    }
                }

                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setInt(1, bookingId);
                    int affectedRows = deleteStmt.executeUpdate();
                    if (affectedRows > 0) {
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateBusSql)) {
                            updateStmt.setInt(1, busId);
                            updateStmt.executeUpdate();
                        }
                        conn.commit();
                        System.out.println("Booking successfully canceled and seat released!");
                    } else {
                        System.out.println("Failed to cancel booking. Please try again.");
                        conn.rollback();
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}