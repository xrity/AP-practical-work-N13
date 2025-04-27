import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static Scanner sc = new Scanner(System.in);
    public static final int MAX_ENTRIES = 50;

    public static String[] dates = new String[MAX_ENTRIES];
    public static String[] texts = new String[MAX_ENTRIES];
    public static int count = 0;

    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void addEntry() {
        if (count >= MAX_ENTRIES) {
            System.out.println("Diary is full. Cannot add more entries.");
            return;
        }

        System.out.println("Enter date (format: yyyy-MM-dd):");
        String dateInput = sc.nextLine();

        if (!isValidDate(dateInput)) {
            System.out.println("Invalid date format.");
            return;
        }

        System.out.println("Enter your diary entry (type 'END' on a new line to finish):");
        String text = "";
        while (true) {
            String line = sc.nextLine();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            text += line + "\n";
        }

        dates[count] = dateInput;
        texts[count] = text.trim();
        count++;

        System.out.println("Entry added.");
    }

    public static void deleteEntry() {
        System.out.println("Enter the date of the entry to delete (format: yyyy-MM-dd):");
        String dateInput = sc.nextLine();

        if (!isValidDate(dateInput)) {
            System.out.println("Invalid date format.");
            return;
        }

        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (dates[i].equals(dateInput)) {
                for (int j = i; j < count - 1; j++) {
                    dates[j] = dates[j + 1];
                    texts[j] = texts[j + 1];
                }
                dates[count - 1] = null;
                texts[count - 1] = null;
                count--;
                found = true;
                System.out.println("Entry deleted.");
                break;
            }
        }

        if (!found) {
            System.out.println("No entry found with that date.");
        }
    }

    public static void viewEntries() {
        if (count == 0) {
            System.out.println("No entries found.");
            return;
        }

        for (int i = 0; i < count; i++) {
            try {
                LocalDate date = LocalDate.parse(dates[i], dateFormatter);
                System.out.println((i + 1) + ". Date: " + date.format(dateFormatter) + " (" + date.getDayOfWeek() + ")");
            } catch (DateTimeParseException e) {
                System.out.println((i + 1) + ". Date: " + dates[i] + " (Invalid format)");
            }
            System.out.println(texts[i]);
            System.out.println("---------------------------");
        }
    }

    public static boolean isValidDate(String dateInput) {
        try {
            LocalDate.parse(dateInput, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("----- My Diary -----");
            System.out.println("1 - Add entry");
            System.out.println("2 - Delete entry");
            System.out.println("3 - View all entries");
            System.out.println("4 - Exit");
            System.out.print("Choose an option: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input, please enter a number.");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addEntry();
                    break;
                case 2:
                    deleteEntry();
                    break;
                case 3:
                    viewEntries();
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        }
    }
}
