package app.utils;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class FilesystemUtilities {
    public static Scanner sc = new Scanner(System.in);
    public static final int MAX_ENTRIES = 50;

    public static String[] dates = new String[MAX_ENTRIES];
    public static String[] texts = new String[MAX_ENTRIES];
    public static int count = 0;

    public static DateTimeFormatter storageFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter displayFormat;

    public static void addEntry() {
        if (count >= MAX_ENTRIES) {
            System.out.println("Diary is full.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String dateTimeStr = now.format(storageFormat);

        System.out.println("Enter your diary entry (type 'END' on a new line to finish):");
        String text = "";
        while (true) {
            String line = sc.nextLine();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            text += line + "\n";
        }

        dates[count] = dateTimeStr;
        texts[count] = text.trim();
        count++;

        System.out.println("Entry added.");
    }

    public static void deleteEntry() {
        System.out.println("Enter the date of the entry to delete (in display format):");
        String input = sc.nextLine();

        boolean found = false;
        for (int i = 0; i < count; i++) {
            try {
                LocalDateTime date = LocalDateTime.parse(dates[i], storageFormat);
                if (date.format(displayFormat).equals(input)) {
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
            } catch (DateTimeParseException e) {

            }
        }

        if (!found) {
            System.out.println("No matching entry found.");
        }
    }

    public static void viewEntries() {
        if (count == 0) {
            System.out.println("No entries.");
            return;
        }

        for (int i = 0; i < count; i++) {
            try {
                LocalDateTime dt = LocalDateTime.parse(dates[i], storageFormat);
                System.out.println((i + 1) + ". Date: " + dt.format(displayFormat));
            } catch (DateTimeParseException e) {
                System.out.println((i + 1) + ". Date: " + dates[i] + " (Invalid format)");
            }
            System.out.println(texts[i]);
            System.out.println();
        }
    }

    public static void savePrompt(String defaultPath) {
        System.out.print("Save diary before exiting? (yes/no): ");
        String ans = sc.nextLine();
        if (ans.equalsIgnoreCase("yes")) {
            System.out.print("Enter file path to save (or press Enter to overwrite " + defaultPath + "): ");
            String input = sc.nextLine();
            if (input.trim().isEmpty()) {
                saveToFile(defaultPath);
            } else {
                saveToFile(input.trim());
            }
        }
    }

    public static void saveToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (int i = 0; i < count; i++) {
                writer.println(dates[i]);
                writer.println(texts[i]);
                writer.println();
            }
            System.out.println("Diary saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    public static void loadFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String currentDate = null;
            String currentText = "";
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (currentDate != null && count < MAX_ENTRIES) {
                        dates[count] = currentDate;
                        texts[count] = currentText.trim();
                        count++;
                    }
                    currentDate = null;
                    currentText = "";
                } else if (currentDate == null) {
                    currentDate = line;
                } else {
                    currentText += line + "\n";
                }
            }

            if (currentDate != null && count < MAX_ENTRIES) {
                dates[count] = currentDate;
                texts[count] = currentText.trim();
                count++;
            }

            System.out.println("Diary loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading file.");
        }
    }
}