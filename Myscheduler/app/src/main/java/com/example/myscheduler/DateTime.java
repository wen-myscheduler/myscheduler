package com.example.myscheduler;

public class DateTime {
    int year, month, day, hour, minute, second;
    public DateTime(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    public String builder() {
        return String.format("%4d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    }
}
