package prayertime;

class PrayerSchedule {
    String date;
    Prayer fajr;
    Prayer dhuhrOrJumuah;
    Prayer asr;
    Prayer maghrib;
    Prayer isha;

    public PrayerSchedule(String date, boolean isFriday) {
        this.date = date;
        
        if (isFriday) {
            this.fajr = new DailyPrayer("Fajr", "4:45 AM", "6:01 AM");
            this.dhuhrOrJumuah = new Jumuah("Jumu'ah", "12:30 PM", "1:30 PM");
            this.asr = new DailyPrayer("Asr", "4:15 PM", "5:30 PM");
            this.maghrib = new DailyPrayer("Maghrib", "5:33 PM", "6:03 PM");
            this.isha = new DailyPrayer("Isha", "6:45 PM", "11:30 PM");
        } else {
            this.fajr = new DailyPrayer("Fajr", "4:45 AM", "6:01 AM");
            this.dhuhrOrJumuah = new DailyPrayer("Dhuhr", "11:58 AM", "4:14 PM");
            this.asr = new DailyPrayer("Asr", "4:15 PM", "5:30 PM");
            this.maghrib = new DailyPrayer("Maghrib", "5:33 PM", "6:03 PM");
            this.isha = new DailyPrayer("Isha", "6:45 PM", "11:30 PM");
        }
    }

    public PrayerSchedule(PrayerSchedule other) {
        this.date = other.date;
        this.fajr = other.fajr;
        this.dhuhrOrJumuah = other.dhuhrOrJumuah;
        this.asr = other.asr;
        this.maghrib = other.maghrib;
        this.isha = other.isha;
    }

    public void displaySchedule() {
        System.out.println("--- Schedule for " + date + " ---");
        fajr.displayPrayerDetails();
        dhuhrOrJumuah.displayPrayerDetails();
        asr.displayPrayerDetails();
        maghrib.displayPrayerDetails();
        isha.displayPrayerDetails();
        
        System.out.println("\n--- Forbidden Times ---");
        System.out.println("  > Sunrise: 6:02 AM - 6:17 AM");
        System.out.println("  > Midday (Zawal): 11:50 AM - 11:58 AM");
        System.out.println("  > Sunset: 5:30 PM - 5:33 PM");
    }
}