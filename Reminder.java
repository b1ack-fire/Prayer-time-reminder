package prayertime;

class Reminder {

    public void checkTimeStatus(String time) {
        System.out.println("\nChecking status for time: " + time);

        if (time.equals("6:10 AM") || time.equals("11:55 AM") || time.equals("5:31 PM")) {
            System.out.println("Status: [RED] Forbidden time to pray.");
        } 
        else if (time.equals("4:50 AM") || time.equals("12:30 PM") || time.equals("4:20 PM")) {
            System.out.println("Status: [GREEN] Perfect time for prayer.");
        } 
        else if (time.equals("5:55 AM") || time.equals("4:10 PM") || time.equals("5:25 PM")) {
            System.out.println("Status: [YELLOW] Late time, but still valid.");
        } 
        else {
            System.out.println("Status: Not a specific prayer time.");
        }
    }

    public void setReminder(String prayerName) {
        System.out.println("\nReminder set for " + prayerName + ".");
    }

    public void setReminder(String prayerName, String customMessage) {
        System.out.println("\nReminder set for " + prayerName + ": " + customMessage);
    }
}