package org.example;

import org.example.OrderService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- E-COMMERCE ORDER SYSTEM ---");
            System.out.println("1. Place New Order");
            System.out.println("2. Update Order Status");
            System.out.println("3. Retrieve Order History by User");
            System.out.println("4. Aggregate Total Sales");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": orderService.placeOrder(); break;
                case "2": orderService.updateOrderStatus(); break;
                case "3": orderService.retrieveOrderHistory(); break;
                case "4": orderService.aggregateSales(); break;
                case "5": return;
                default: System.out.println("Invalid Option.");
            }
        }
    }
}