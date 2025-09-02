import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager(scanner);
        BookingManager bookingManager = new BookingManager(scanner);

        while (true) {
            System.out.println("\n--- Bus Ticket Management System ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Buses");
            System.out.println("4. Book Ticket");
            System.out.println("5. View My Bookings");
            System.out.println("6. Cancel Booking");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    userManager.registerUser();
                    break;
                case "2":
                    userManager.loginUser();
                    break;
                case "3":
                    bookingManager.viewBuses();
                    break;
                case "4":
                    bookingManager.bookTicket(UserManager.getCurrentPassengerId());
                    break;
                case "5":
                    bookingManager.viewMyBookings(UserManager.getCurrentPassengerId());
                    break;
                case "6":
                    bookingManager.cancelBooking(UserManager.getCurrentPassengerId());
                    break;
                case "7":
                    System.out.println("Thank you for using the system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}