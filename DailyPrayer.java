package prayertime;

class DailyPrayer extends Prayer {
    
    public DailyPrayer(String name, String start, String end) {
        super(name, start, end);
    }

    @Override
    void displayPrayerDetails() {
        System.out.println("  > " + this.prayerName + ": " + this.startTime + " - " + this.endTime);
    }
}