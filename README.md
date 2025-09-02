# Bus Ticket Management System 🚌

This is a console-based Bus Ticket Management System developed using Java, MySQL, and JDBC. The application allows users to register, log in, view available buses, book tickets, and cancel bookings through a simple command-line interface.

-----

### 1\. Features ✨

  * User Management: Register and log in as a passenger.

  * Bus Information: View a list of available buses, including their route and seat availability.

  * Ticket Booking: Book a seat on a specific bus, with automatic seat count reduction.

  * Booking History: View all tickets booked under your account.

  * Booking Cancellation: Cancel a booking, which automatically restores the seat availability.

  * Database Integration: All data is stored and managed in a MySQL database.

-----

### 2\. Technology Stack 💻

  * Language: Java

  * Database: MySQL

  * Connectivity: JDBC (Java Database Connectivity)

  * Development Environment: IntelliJ IDEA

-----

### 3\. Installation and Setup 🚀

To get this project running on your local machine, follow these steps.

#### **Prerequisites**

  * Java Development Kit (JDK) 8 or higher

  * MySQL Server

  * IntelliJ IDEA

#### **Project Setup in IntelliJ**

1.  Clone this repository or download the project files.

2.  Open the project in IntelliJ IDEA.

3.  **Add the MySQL JDBC Driver**:

      * Download the [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) `.jar` file.

      * In IntelliJ, go to **File \> Project Structure \> Libraries**.

      * Click the `+` icon, select **Java**, and add the downloaded `.jar` file.

4.  **Update Database Credentials**:

      * Open `DatabaseManager.java`.

      * Modify the `USER` and `PASSWORD` variables to match your MySQL database credentials.

#### **Running the Application**

1.  Open the `Main.java` file in IntelliJ.

2.  Click the green "Run" button next to the `main` method.

3.  The application will start in the IntelliJ console, and you can interact with it by entering your choices.

-----

### 4\. Code Structure 📁

The project is organized into separate files for better maintainability and clarity.

  * `Main.java`: The main entry point of the application. It contains the menu loop and calls methods from other classes.

  * `DatabaseManager.java`: Manages the connection to the MySQL database.

  * `UserManager.java`: Handles user-related logic, including registration and login.

  * `BookingManager.java`: Contains all the functionality related to bus information and ticket management (viewing, booking, and canceling).

