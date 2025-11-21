package prayertime;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.Random; // <-- 1. Ei notun import-ta add koro

public class PrayerTimeReminder {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // --- 2. Hadith er list (Array) ---
        // Ekhane kichu Hadith add kora holo
        String[] hadithList = {
            "\"Purity is half of faith.\" (Sahih Muslim)",
            "\"Smiling in your brother's face is an act of charity.\" (Tirmidhi)",
            "\"He who is not merciful to others, will not be treated mercifully.\" (Bukhari)",
            "\"The best of you are those who have the best of manners.\" (Bukhari)",
            "\"Seek knowledge from the cradle to the grave.\"",
            "\"The upper hand (giving) is better than the lower hand (receiving).\" (Bukhari)"
        };
        // ------------------------------------

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        boolean isFriday = (dayOfWeek == DayOfWeek.FRIDAY);
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("eeee, MMMM d");
        String formattedDate = now.format(dateFormatter);
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        String formattedTime = now.format(timeFormatter);
        
        System.out.println("--- Dhaka Prayer Time Reminder ---");
        System.out.println("Current Time: " + formattedDate + " | " + formattedTime);
        System.out.println("------------------------------------");
        System.out.println("Select an option (e.g., '1', '2'):");
        System.out.println("1. Show Today's Schedule");
        System.out.println("2. Show Friday's Schedule (Calendar Pop-up)");
        System.out.println("3. Check current time status (Demo)");
        System.out.println("4. Demo Method Overloading");
        System.out.println("5. Demo Copy Constructor");
        System.out.println("6. Show a Hadith"); // <-- 3. Notun Menu Option
        System.out.print("Your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        Reminder reminder = new Reminder();
        PrayerSchedule today = new PrayerSchedule(formattedDate, isFriday);

        if (choice == 1) {
            today.displaySchedule();
        } 
        else if (choice == 2) {
            PrayerSchedule friday = new PrayerSchedule("Friday, Nov 14 (Demo)", true);
            friday.displaySchedule();
        } 
        else if (choice == 3) {
            System.out.println("\nChecking current time: " + formattedTime);
            reminder.checkTimeStatus(formattedTime);
            System.out.println("(Note: This demo only shows a status if the time *exactly* matches '4:50 AM', '6:10 AM', etc.)");
        } 
        else if (choice == 4) {
            System.out.println("\n--- Demo Overloading ---");
            reminder.setReminder("Asr");
            reminder.setReminder("Maghrib", "Meet at mosque.");
        } 
        else if (choice == 5) {
            System.out.println("\n--- Demo Copy Constructor ---");
            System.out.println("\nOriginal 'today' object:");
            today.displaySchedule();
            
            PrayerSchedule copy = new PrayerSchedule(today);
            copy.date = "COPY of Today"; 
            
            System.out.println("\nCopied and modified object:");
            copy.displaySchedule();
        }
        // --- 4. Notun logic block add kora holo ---
        else if (choice == 6) {
            System.out.println("\n--- A Hadith for You ---");
            
            // Ekta random object toiri koro
            Random random = new Random();
            
            // hadithList-er size-er moddhe ekta random number select koro (e.g., 0 theke 5)
            int randomIndex = random.nextInt(hadithList.length);
            
            // Oi random index-er Hadith-ta print koro
            System.out.println(hadithList[randomIndex]);
            System.out.println("--------------------------");
        }
        // ------------------------------------------
        else {
            System.out.println("Invalid choice. Please run again.");
        }
        
        scanner.close();
    }
}