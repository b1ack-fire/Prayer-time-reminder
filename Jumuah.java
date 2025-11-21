package prayertime;

class Jumuah extends Prayer {
    
    public Jumuah(String name, String start, String end) {
        super(name, start, end);
    }

    @Override
    void displayPrayerDetails() {
        System.out.println("  > " + this.prayerName + ": " + this.startTime + " - " + this.endTime + " (Replaces Dhuhr)");
    }
}