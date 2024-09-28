package PersonalFinanceTrackerPackage;

import java.util.*;
import java.text.DecimalFormat;

public class PersonalFinanceTracker {
    private static List<Transaction> transactions = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addTransaction();
                    break;
                case 2:
                    viewTransactions();
                    break;
                case 3:
                    viewBalance();
                    break;
                case 4:
                    viewExpenseBreakdown();
                    break;
                case 5:
                    System.out.println("Exiting. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Personal Finance Tracker ---");
        System.out.println("1. Add Transaction");
        System.out.println("2. View Recent Transactions");
        System.out.println("3. View Balance");
        System.out.println("4. View Expense Breakdown");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addTransaction() {
        System.out.print("Enter transaction type (income/expense): ");
        String type = scanner.nextLine().toLowerCase();
        
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        Transaction transaction = new Transaction(
            type.equals("income") ? TransactionType.INCOME : TransactionType.EXPENSE,
            amount,
            category,
            description
        );
        transactions.add(transaction);
        System.out.println("Transaction added successfully!");
    }

    private static void viewTransactions() {
        System.out.println("\n--- Recent Transactions ---");
        int start = Math.max(0, transactions.size() - 5);
        for (int i = transactions.size() - 1; i >= start; i--) {
            System.out.println(transactions.get(i));
        }
    }

    private static void viewBalance() {
        double income = calculateTotal(TransactionType.INCOME);
        double expenses = calculateTotal(TransactionType.EXPENSE);
        double balance = income - expenses;

        System.out.println("\n--- Financial Summary ---");
        System.out.println("Total Income: $" + df.format(income));
        System.out.println("Total Expenses: $" + df.format(expenses));
        System.out.println("Current Balance: $" + df.format(balance));
    }

    private static void viewExpenseBreakdown() {
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.EXPENSE) {
                categoryTotals.put(t.getCategory(), 
                    categoryTotals.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
            }
        }

        System.out.println("\n--- Expense Breakdown ---");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            System.out.println(entry.getKey() + ": $" + df.format(entry.getValue()));
        }
    }

    private static double calculateTotal(TransactionType type) {
        return transactions.stream()
            .filter(t -> t.getType() == type)
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
}

enum TransactionType {
    INCOME, EXPENSE
}

class Transaction {
    private TransactionType type;
    private double amount;
    private String category;
    private String description;
    private Date date;

    public Transaction(TransactionType type, double amount, String category, String description) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = new Date();
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        return String.format("%s - %s: $%s (%s) - %s", 
            date, type, df.format(amount), category, description);
    }
}