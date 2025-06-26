package org.example;

import com.mongodb.client.MongoIterable;
import org.bson.Document;

import java.util.List;

public class DisplayFormatter {

    public static void printOrderList(MongoIterable<Document> results) {
        int counter = 1;
        System.out.println("\n" + "=".repeat(120));

        for (Document order : results) {
            List<String> products = order.getList("productList", String.class);

            System.out.printf("\nOrder #%d\n", counter++);
            System.out.println("-".repeat(120));
            System.out.printf("%-18s : %s\n", "Order ID", order.getString("orderId"));
            System.out.printf("%-18s : %s\n", "User Email", order.getString("userEmail"));
            System.out.printf("%-18s : $%,.2f\n", "Total Amount", order.getDouble("totalAmount"));
            System.out.printf("%-18s : %s\n", "Status", order.getString("status"));
            System.out.printf("%-18s : %s\n", "Order Date", order.getString("orderDate"));
            System.out.printf("%-18s : %s\n", "Products", String.join(", ", products));
            System.out.println("-".repeat(120));
        }

        System.out.println("=".repeat(120));
    }

    public static void printSalesSummary(String header, MongoIterable<Document> results) {
        System.out.println("\n" + header);
        System.out.println("=".repeat(60));
        System.out.printf("%-30s | %-20s\n", "Key", "Total Sales ($)");
        System.out.println("-".repeat(60));

        for (Document doc : results) {
            System.out.printf("%-30s | %,.2f\n", doc.getString("_id"), doc.getDouble("totalSales"));
        }

        System.out.println("=".repeat(60));
    }
}
