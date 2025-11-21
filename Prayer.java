package prayertime;

abstract class Prayer {
    String prayerName;
    String startTime;
    String endTime;

    public Prayer(String name, String start, String end) {
        this.prayerName = name;
        this.startTime = start;
        this.endTime = end;
    }

    abstract void displayPrayerDetails();

    public String getPrayerName() {
        return prayerName;
    }
}