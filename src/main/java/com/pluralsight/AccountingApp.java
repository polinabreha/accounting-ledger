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
               System.out.println("D) Add Deposit");
               System.out.println("P) Make Payment (Debit)");
               System.out.println("L) Ledger");
               System.out.println("X) Exit");
               System.out.print("Enter the letter of the choice: ");
               String choice = input.nextLine().toUpperCase();

               switch (choice) {
                   case "D" :
                       deposit(input, account);
                       break;
                   case "P" :
                        payment(input, account);
                        break;
                   case "L" :
                       ledgerScreen(input, account);
                       break;
                   case "X" :
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

    public static void deposit(Scanner input, ArrayList<Transactions> account) throws IOException {
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
    }

    public static void payment(Scanner input, ArrayList<Transactions> account) throws IOException {
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
            amount = amount * -1;
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

    }

    public static void ledgerScreen(Scanner input, ArrayList<Transactions> account) throws IOException {
        System.out.println("========== LEDGER ==========");
        System.out.println("A) All        - Display all entries");
        System.out.println("D) Deposits   - Display only deposits");
        System.out.println("P) Payments   - Display only payments");
        System.out.println("R) Reports    - View reports");
        System.out.println("H) Home       - Go back to home screen");
        System.out.println("================================");
        System.out.print("Enter your choice: ");
        String choice = input.nextLine().toUpperCase();
        account.clear();

        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
        String line;
        br.readLine();
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] data = line.split("\\|");
            Transactions t = new Transactions(data[0], data[1], data[2],data[3], Double.parseDouble(data[4]) );
            account.add(t);

        }
        br.close();
        Collections.reverse(account);

        switch (choice) {
            case "A" :

                for (Transactions t : account) {
                    System.out.println(t.toString());
                }
                break;

            case "D" :
                for (Transactions t : account) {
                    if (t.getAmount() > 0) {
                        System.out.println(t.toString());
                    }
                }
                break;
            case "P" :
                for (Transactions t : account) {
                    if (t.getAmount() < 0) {
                        System.out.println(t.toString());
                    }
                }
                break;
            case "R" :
                reports(input, account);
                break;

            case "H" :
                homeScreen(input);
                break;
            default:
                System.out.println("Invalid choice");
        }

    }

    public static void reports (Scanner input, ArrayList<Transactions> account) {
        try {
            boolean running = true;
            while (running) {
                System.out.println("========== REPORTS ==========");
                System.out.println("1) Month To Date");
                System.out.println("2) Previous Month");
                System.out.println("3) Year To Date");
                System.out.println("4) Previous Year");
                System.out.println("5) Search by Vendor");
                System.out.println("0) Back");
                System.out.print("Enter your choice: ");
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
                            if (previousMonth.getMonth() == transactionDate.getMonth() && previousMonth.getYear() == transactionDate.getYear()) {
                                System.out.println(t.toString());
                            }
                        }
                        break;

                    case 3:
                        LocalDate thisYear = LocalDate.now();
                        for (Transactions t : account) {
                            LocalDate transactionDate = LocalDate.parse(t.getDate());
                            if (thisYear.getYear() == transactionDate.getYear()) {
                                System.out.println(t.toString());
                            }
                        }
                        break;

                    case 4:
                        LocalDate previousYear = LocalDate.now().minusYears(1);
                        for (Transactions t : account) {
                            LocalDate transactionDate = LocalDate.parse(t.getDate());
                            if (previousYear.getYear() == transactionDate.getYear()) {
                                System.out.println(t.toString());
                            }
                        }
                        break;
                    case 5:
                        System.out.print("Enter the vendor: ");
                        String vendor = input.nextLine();
                        for (Transactions t : account) {
                            if (vendor.equalsIgnoreCase(t.getVendor())) {
                                System.out.println(t.toString());
                            }
                        }
                        break;

                    case 0:
                        running = false;
                        ledgerScreen(input, account);
                        break;

                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }

        }catch (IOException e) {
            System.out.println("Invalid choice, please try again.");
        }
    }









}
