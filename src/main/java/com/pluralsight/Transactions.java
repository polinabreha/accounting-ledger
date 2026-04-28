package com.pluralsight;

public class Transactions {
    String date;
    String time;
    String description;
    String vendor;
    double amount;
    String category;

    public Transactions(String date, String time, String description, String vendor, double amount, String category) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
        this.category = category;
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

    public String getCategory() { return category; }

    public String toString() {
        return date +
                "|" + time +
                "|" + description  +
                "|" + vendor +
                "|" + amount +
                "|" + category;
    }



}
