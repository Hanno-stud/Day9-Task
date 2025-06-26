package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;

public class OrderService {

    private final MongoCollection<Document> orders;
    private final Scanner scanner = new Scanner(System.in);

    public OrderService() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = client.getDatabase("ecommerce");
        orders = database.getCollection("orders");

        Runtime.getRuntime().addShutdownHook(new Thread(client::close));
    }

    public void placeOrder() {
        System.out.print("Enter User Email: ");
        String userEmail = scanner.nextLine().trim();

        System.out.print("Enter Products (comma-separated): ");
        List<String> products = Arrays.asList(scanner.nextLine().split(","));

        System.out.print("Enter Total Amount: ");
        double totalAmount = Double.parseDouble(scanner.nextLine());

        Document order = new Document()
                .append("orderId", java.util.UUID.randomUUID().toString())
                .append("userEmail", userEmail)
                .append("productList", products)
                .append("totalAmount", totalAmount)
                .append("status", "Placed")
                .append("orderDate", LocalDateTime.now().toString());

        orders.insertOne(order);
        System.out.println("Order placed successfully.");
    }

    public void updateOrderStatus() {
        System.out.print("Enter Order ID to update: ");
        String orderId = scanner.nextLine().trim();

        System.out.print("Enter New Status (Placed/Shipped/Delivered/Cancelled): ");
        String newStatus = scanner.nextLine().trim();

        orders.updateOne(eq("orderId", orderId), new Document("$set", new Document("status", newStatus)));
        System.out.println("Order status updated successfully.");
    }

    public void retrieveOrderHistory() {
        System.out.print("Enter User Email to fetch order history: ");
        String userEmail = scanner.nextLine().trim();

        FindIterable<Document> orderHistory = orders.find(eq("userEmail", userEmail));

        System.out.println("\nOrder History for " + userEmail + ":");
        DisplayFormatter.printOrderList(orderHistory);
    }

    public void aggregateSales() {
        System.out.println("Aggregate sales by: (1) Product (2) Time Period");
        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            System.out.print("Enter Product Name: ");
            String product = scanner.nextLine().trim();

            AggregateIterable<Document> sales = orders.aggregate(Arrays.asList(
                    Aggregates.match(new Document("productList", product)),
                    Aggregates.group("$productList", Accumulators.sum("totalSales", "$totalAmount"))
            ));

            DisplayFormatter.printSalesSummary("Product-wise Sales Summary", (FindIterable<Document>) sales);

        } else if (choice.equals("2")) {
            System.out.print("Enter Start Date (YYYY-MM-DD): ");
            String startDate = scanner.nextLine().trim();
            System.out.print("Enter End Date (YYYY-MM-DD): ");
            String endDate = scanner.nextLine().trim();

            AggregateIterable<Document> sales = orders.aggregate(Arrays.asList(
                    Aggregates.match(new Document("orderDate", new Document("$gte", startDate).append("$lte", endDate))),
                    Aggregates.group(null, Accumulators.sum("totalSales", "$totalAmount"))
            ));

            for (Document doc : sales) {
                System.out.println(doc.toJson());
            }
        } else {
            System.out.println("Invalid Option.");
        }
    }
}