package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
               System.out.print("Enter the number of the choice: ");
               int choice = input.nextInt();
               input.nextLine();

               switch (choice) {
                   case 1:
                       deposit(input, account);
                       break;
                   case 2:
                        payment(input, account);
                        break;
                   case 3:
                       ledgerScreen(input, account);
                       break;
                   case 4:
                       runTheProgram = false;
                       break;
                   default:
                       System.out.println("Invalid choice");
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
        System.out.print("Enter the information about the deposit transaction " +
                "\n(Example: Paycheck, Invoice 1003 paid, Bonus payment): ");
        String depositInfo = input.nextLine();
        System.out.print("Enter please the Vendor / Source : ");
        String source = input.nextLine();
        System.out.print("Enter the amount to be deposited: $ ");
        double amount = input.nextDouble();
        input.nextLine();
        if (amount < 0) {
            System.out.print("Please enter a positive value: $ ");
            amount = input.nextDouble();
            input.nextLine();
        }
        Transactions t = new Transactions(
                depositTime.format(dateFormatted),
                depositTime.format(timeFormatted),
                depositInfo,
                source,
                amount
        );

        bw.write(t.toString() + "\n");
        bw.close();
        account.add(t);
        return account;
    }

    public static ArrayList<Transactions> payment(Scanner input, ArrayList<Transactions> account) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
        LocalDateTime depositTime = LocalDateTime.now();
        DateTimeFormatter dateFormatted = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatted = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.print("Enter the information about your payment: ");
        String payInfo = input.nextLine();
        System.out.print("Enter please the Vendor / Source : ");
        String source = input.nextLine();
        System.out.print("Enter the amount that you payed (enter '-' before the amount): $ ");
        double amount = input.nextDouble();
        input.nextLine();
        if (amount > 0) {
            System.out.print("Please enter a negative value: $");
            amount = input.nextDouble();
            input.nextLine();
        }
        Transactions t = new Transactions(
                depositTime.format(dateFormatted),
                depositTime.format(timeFormatted),
                payInfo,
                source,
                amount
        );

        bw.write(t.toString() + "\n");
        bw.close();
        account.add(t);
        return account;

    }

    public static void ledgerScreen(Scanner input, ArrayList<Transactions> account) throws IOException {
        System.out.println("========== LEDGER ==========");
        System.out.println("1. All        - Display all entries");
        System.out.println("2. Deposits   - Display only deposits");
        System.out.println("3. Payments   - Display only payments");
        System.out.println("4. Reports    - View reports");
        System.out.println("5. Home       - Go back to home screen");
        System.out.println("================================");
        System.out.print("Enter your choice: ");
        int choice = input.nextInt();
        input.nextLine();
        account.clear();
        Collections.reverse(account);

        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split("\\|");
            Transactions t = new Transactions(data[0], data[1], data[2],data[3], Double.parseDouble(data[4]) );
            account.add(t);

        }
        br.close();

        switch (choice) {
            case 1:

                for (Transactions t : account) {
                    System.out.println(t.toString());
                }
                break;

            case 2:
                for (Transactions t : account) {
                    if (t.getAmount() > 0) {
                        System.out.println(t.toString());
                    }
                }
                break;
            case 3:
                for (Transactions t : account) {
                    if (t.getAmount() < 0) {
                        System.out.println(t.toString());
                    }
                }
                break;
            case 4:
                reports(input, account);
                break;

            case 5:
                homeScreen(input);
                break;
            default:
                System.out.println("Invalid choice");
        }

    }

    public static ArrayList<Transactions> reports (Scanner input, ArrayList<Transactions> account) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split("\\|");
            Transactions t = new Transactions(data[0], data[1], data[2],data[3], Double.parseDouble(data[4]) );
            account.add(t);

        }
        br.close();
        System.out.println("========== REPORTS ==========");
        System.out.println("1) Month To Date");
        System.out.println("2) Previous Month");
        System.out.println("3) Year To Date");
        System.out.println("4) Previous Year");
        System.out.println("5) Search by Vendor");
        System.out.println("0) Back");
        System.out.println("Enter your choice: ");
        int choice = input.nextInt();
        input.nextLine();
        Collections.reverse(account);

        switch (choice) {
            case 1:
                LocalDate today = LocalDate.now();
                for (Transactions t : account) {
                    LocalDate transactionDate = LocalDate.parse(t.getDate());
                    if (transactionDate.getMonth() == today.getMonth() && transactionDate.getYear() == today.getYear()) {
                        System.out.println(t.toString());
                    }

                }
                break;

            case 2:
                LocalDate previousMonth = LocalDate.now().minusMonths(1);
                for (Transactions t : account) {
                    LocalDate transactionDate = LocalDate.parse(t.getDate());
                    if (previousMonth .getMonth() == transactionDate.getMonth() && previousMonth.getYear() == transactionDate.getYear()) {
                        System.out.println(t.toString());
                    }
                }
                break;

            case 3:
              LocalDate thisYear = LocalDate.now();
              for (Transactions t : account) {
                LocalDate transactionDate = LocalDate.parse(t.getDate());
                if(thisYear.getYear() == transactionDate.getYear()) {
                    System.out.println(t.toString());
                }
            }
              break;

            case 4:
                LocalDate previousYear = LocalDate.now().minusYears(1);
                for (Transactions t : account) {
                    LocalDate transactionDate = LocalDate.parse(t.getDate());
                    if(previousYear.getYear() == transactionDate.getYear()) {
                        System.out.println(t.toString());
                    }
                }
                break;
            case 5:
                System.out.print("Enter the vendor: ");
                String vendor = input.nextLine();
                for(Transactions t : account) {
                    if (vendor.equals(t.getVendor())) {
                        System.out.println(t.toString());
                    }
                }
                break;

            case 0:
                ledgerScreen(input, account);
                break;

            default:
                System.out.println("Invalid choice, please try again.");
        }

        return account;
    }









}
