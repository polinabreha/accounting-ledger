package com.pluralsight;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class AccountingApp {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("-----Welcome to the Accounting Application-----");

        homeScreen(input);

    }

    public static void homeScreen(Scanner input) {
        ArrayList<Transactions> account = new ArrayList<>();
        boolean runTheProgram = true;
       try {
           while (runTheProgram) {

               System.out.println("Choose of the following options:");
               System.out.println("1. Add Deposit");
               System.out.println("2. Make Payment (Debit)");
               System.out.println("3. Ledger");
               System.out.println("4. Exit");
               System.out.println("Enter the number of the choice: ");
               int choice = input.nextInt();
               input.nextLine();

               switch (choice) {
                   case 1:
                       account = deposit(input, account);
                       break;

               }

           }
       }catch (Exception e) {
           System.out.println(e.getMessage());
       }

    }

    public static ArrayList<Transactions> deposit(Scanner input, ArrayList<Transactions> account) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
        LocalDateTime depositTime = LocalDateTime.now();
        DateTimeFormatter dateFormatted = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatted = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println("Enter the information about the deposit transaction " +
                "\n(Example: Paycheck, Invoice 1003 paid, Bonus payment): ");
        String depositInfo = input.nextLine();
        System.out.println("Enter please the Vendor / Source : ");
        String source = input.nextLine();
        System.out.println("Enter the amount to be deposited: ");
        double amount = input.nextDouble();
        if (amount < 0) {
            System.out.println("Please enter a positive value: ");
            amount = input.nextDouble();
        }
        Transactions t = new Transactions(
                depositTime.format(dateFormatted),
                depositTime.format(timeFormatted),
                depositInfo,
                source,
                amount
        );
        bw.write(t.toString());
        bw.newLine();
        bw.close();
        account.add(t);
        return account;
    }









}
