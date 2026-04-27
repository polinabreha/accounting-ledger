package com.pluralsight;
import java.sql.Time;
import java.util.Date;

public class Transactions {
    String date;
    String time;
    String description;
    String vendor;
    double amount;

    public Transactions(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    public String toString() {
        return date +
                "|" + time +
                "|" + description  +
                "|" + vendor +
                "|" + amount;
    }



}
