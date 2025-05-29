package app;


import app.utils.FilesystemUtilities;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DiaryApplication {
    public static Scanner sc = new Scanner(System.in);
    public static DateTimeFormatter displayFormat;

    public static void run() {
        System.out.println("----- My Diary -----");
        System.out.println("1 - Create new diary");
        System.out.println("2 - Load existing diary");
        String filePath = "";

        while (true) {
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter filename to create: ");
                filePath = sc.nextLine();
                break;
            } else if (choice.equals("2")) {
                System.out.print("Enter filename to load: ");
                filePath = sc.nextLine();
                FilesystemUtilities.loadFromFile(filePath);
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }

        System.out.println("Choose date display format (e.g., yyyy-MM-dd HH:mm:ss or dd.MM.yyyy HH:mm): ");
        String formatInput = sc.nextLine();
        try {
            displayFormat = DateTimeFormatter.ofPattern(formatInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid format, defaulting to yyyy-MM-dd HH:mm:ss.");
            displayFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }

        boolean running = true;
        while (running) {
            System.out.println("1 - Add entry");
            System.out.println("2 - Delete entry");
            System.out.println("3 - View all entries");
            System.out.println("4 - Exit");
            System.out.print("Choose an option: ");

            String opt = sc.nextLine();
            switch (opt) {
                case "1":
                    FilesystemUtilities.addEntry();
                    break;
                case "2":
                    FilesystemUtilities.deleteEntry();
                    break;
                case "3":
                    FilesystemUtilities.viewEntries();
                    break;
                case "4":
                    running = false;
                    FilesystemUtilities.savePrompt(filePath);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
