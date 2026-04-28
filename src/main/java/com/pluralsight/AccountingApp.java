package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class AccountingApp {
    static final DateTimeFormatter dateFormatted = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter timeFormatted = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("-------------------Welcome at the \"Steel City\" Bank-------------------");
        logo(args);
        homeScreen(input);

    }

    public static void homeScreen(Scanner input) {
        ArrayList<Transactions> account = new ArrayList<>();
        boolean runTheProgram = true;
       try {
           while (runTheProgram) {

               System.out.println("Choose of the following options:");
               System.out.println("D) Deposit Funds");
               System.out.println("P) Withdraw / Pay");
               System.out.println("V) View Account Statement");
               System.out.println("C) Custom Search");
               System.out.println("L) Log Out");
               System.out.print("Enter the letter of the choice: ");
               String choice = input.nextLine().toUpperCase();

               switch (choice) {
                   case "D" :
                       deposit(input, account);
                       break;
                   case "P" :
                        payment(input, account);
                        break;
                   case "V" :
                       ledgerScreen(input, account);
                       break;
                   case "C" :
                       customSearch(input, account);
                       break;
                   case "L" :
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
    public static void bufferedwriter(Transactions t) throws Exception{
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
        bw.write(t.toString() + "\n");
        bw.close();

    }

    public static void deposit(Scanner input, ArrayList<Transactions> account) {
        try {
            LocalDateTime depositTime = LocalDateTime.now();
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
            bufferedwriter(t);
            account.add(t);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void payment(Scanner input, ArrayList<Transactions> account) {
        try {
            LocalDateTime depositTime = LocalDateTime.now();
            System.out.print("Enter the information about your payment: ");
            String payInfo = input.nextLine();

            System.out.print("Enter please the Vendor / Source : ");
            String source = input.nextLine();

            System.out.print("Enter the amount that you payed : $ ");
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

            bufferedwriter(t);
            account.add(t);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void ledgerScreen(Scanner input, ArrayList<Transactions> account) throws IOException {
        System.out.println("========== LEDGER ==========");
        System.out.println("A) All        - Display all entries");
        System.out.println("D) Deposits   - Display only deposits");
        System.out.println("P) Payments   - Display only payments");
        System.out.println("R) Reports    - View reports");
        System.out.println("B) Balance    -  See the balance  ");
        System.out.println("H) Home       - Go back to home screen");
        System.out.println("================================");
        System.out.print("Enter your choice: ");
        String choice = input.nextLine().toUpperCase();
        account.clear();

        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            if (line.startsWith("date")) continue;
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

            case "B" :
                balanceDisplay(account);
                break;

            case "H" :
                return ;
            default:
                System.out.println("Invalid choice");
        }

    }

    public static void reports (Scanner input, ArrayList<Transactions> account) {
        try {
            while (true) {
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
                        return;

                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }

        }catch (Exception e) {
            System.out.println("Invalid choice, please try again.");
        }
    }

    public static void customSearch (Scanner input, ArrayList<Transactions> account) {
        System.out.print("Enter Start Date: ");
        String startDate = input.nextLine();
        System.out.print("Enter End Date: ");
        String endDate = input.nextLine();
        System.out.print("Enter Description: ");
        String description = input.nextLine();
        System.out.print("Enter Vendor Name: ");
        String vendor = input.nextLine();
        System.out.print("Enter Amount: ");
        String amount = input.nextLine();

        LocalDate startDateParsed = null;
        LocalDate endDateParsed = null;
        double amountParsed = 0;
        boolean found = false;

        if (startDate.isEmpty()){
            startDate = null;
        }else{
           startDateParsed = LocalDate.parse(startDate);
        }
        if (endDate.isEmpty() ){
            endDate = null;
        } else{
            endDateParsed = LocalDate.parse(endDate);
        }
        if (description.isEmpty() ){
            description = null;
        }
        if (vendor.isEmpty() ){
            vendor = null;
        }
        if (amount.isEmpty() ) {
            amount = null;
        } else {
           amountParsed = Double.parseDouble(amount);
        }

       for (Transactions t : account) {
           boolean match = true;
           if (startDateParsed != null) {
              LocalDate transactionDate = LocalDate.parse(t.getDate());
              if (transactionDate.isBefore(startDateParsed)){
                  match = false;
              }
           }
           if (endDateParsed != null) {
               LocalDate transactionDate = LocalDate.parse(t.getDate());
               if (transactionDate.isAfter(endDateParsed)){
                   match = false;
               }
           }
           if (description != null) {
               if (!description.equalsIgnoreCase(t.getDescription())) {
                   match = false;
               }
           }
           if (vendor != null) {
               if (!vendor.equalsIgnoreCase(t.getVendor())) {
                   match = false;
               }
           }
           if (amount != null) {
               if (amountParsed != (t.getAmount())) {
                   match = false;
               }
           }
           if (match) {
               System.out.println(t.toString());
               found = true;
           }
       }
       if (!found) {
           System.out.println("No transactions found");
       }
    }

    public static void balanceDisplay ( ArrayList<Transactions> account) {
        double total = 0.0;
        double deposit = 0.0;
        double payments = 0;

        for (Transactions t : account) {
            if (t.getAmount() > 0) {
                deposit += t.getAmount();
            }
            if (t.getAmount() < 0) {
                payments += t.getAmount();
            }

        }
        total = deposit + payments;
        System.out.println("Total Deposits : " + deposit);
        System.out.println("Total Withdrawals : " + payments);
        System.out.println("Current Balance: " + total);

    }
    public static void logo(String[] args){
        String logo = """
                ============================================================
                   S T E E L  C I T Y  B A N K | Financial Systems v1.0
                ============================================================
           
                                |$ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $ $|
                                |           Your Reliable           |
                                |           B A N K $$$$            |
                                |___________________________________|
                                |   |                           |   |
                                |   |          _______          |   |
                                |   |        /    |    \\        |   |
                                |   |       |     |     |       |   |
                                |   |    ---|-----$-----|---    |   |
                                |   |       |     |     |       |   |
                                |   |        \\____|_____/       |   |
                                |   |                           |   |
                                |___|___________________________|___|
               ======================================================================
          
               ======================================================================
           """;
        System.out.println(logo);
    }


}
