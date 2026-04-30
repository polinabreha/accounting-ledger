package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AccountingApp {
    static final DateTimeFormatter dateFormatted = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter timeFormatted = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static final String ANSI_BRIGHT_BLUE = "\u001B[96m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_Cyan = "\u001B[36m";
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println(ANSI_BRIGHT_BLUE + "-------------------Welcome at the \"Steel City\" Bank-------------------" + ANSI_RESET);
        logo();
        homeScreen(input);

    }

    public static void homeScreen(Scanner input) {
        ArrayList<Transactions> account = new ArrayList<>();
        boolean runTheProgram = true;
       try {
           LocalDateTime dateTime = LocalDateTime.now();
           System.out.println(ANSI_Cyan + "Start the program: " +dateTime.format(dateFormatted) + " " + dateTime.format(timeFormatted) + ANSI_RESET);
           while (runTheProgram) {

               System.out.println(ANSI_BRIGHT_BLUE + "Choose of the following options:" + ANSI_RESET);
               System.out.println("D) Deposit Funds");
               System.out.println("P) Withdraw / Pay");
               System.out.println("V) View Account Statement");
               System.out.println("L) Log Out");
               System.out.print("Enter the letter of the choice: ");
               String choice = input.nextLine().toUpperCase().trim();

               switch (choice) {
                   case "D" :
                       deposit(input, account);
                       break;
                   case "P" :
                        payment(input, account);
                        break;
                   case "V" :
                       accountStatementScreen(input, account);
                       break;
                   case "L" :
                       LocalDateTime now = LocalDateTime.now();
                       System.out.println(ANSI_Cyan + "End the program: " + now.format(dateFormatted) + " " +  now.format(timeFormatted) + ANSI_RESET);
                       logo2();
                       runTheProgram = false;
                       break;
                   default:
                       System.out.println("Invalid choice");
               }

           }
       }catch (InputMismatchException e) {
           System.out.println("Invalid input" + e.getMessage());
       }

    }
    public static void bufferedwriter(Transactions t) throws IOException{
        BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/transactions.csv", true));
        bw.write(t.toString() + "\n");
        bw.close();

    }

    public static void deposit(Scanner input, ArrayList<Transactions> account) {
        try {
            LocalDateTime depositTime = LocalDateTime.now();
            System.out.print(ANSI_BRIGHT_BLUE + "Enter the information about the deposit transaction " +
                    "\n(Example: Paycheck, Invoice 1003 paid, Bonus payment): " + ANSI_RESET);
            String depositInfo = input.nextLine();

            System.out.print("Enter please the Vendor / Source : ");
            String source = input.nextLine();

            System.out.print("Enter the amount to be deposited: $ ");
            double amount = input.nextDouble();
            input.nextLine();


            while (amount < 0) {
                System.out.print("Please enter a positive value: $ ");
                amount = input.nextDouble();
                input.nextLine();
            }
            System.out.println("Enter please the Category : ");
            System.out.println("1. Salary");
            System.out.println("2. Freelance");
            System.out.println("3. Gift");
            System.out.println("4. Refund");
            System.out.println("5. Other Income");
            System.out.print("Enter your choice: ");
            String category = "";
            int choice = input.nextInt();
            input.nextLine();
            switch (choice) {
                case 1:
                    category = "Salary";
                    break;
                case 2:
                    category = "Freelance";
                    break;
                case 3:
                    category = "Gift";
                    break;
                case 4:
                    category = "Refund";
                    break;
                case 5:
                    category = "Other Income";
                    break;
                default:
                    System.out.println("Invalid choice");

            }


            Transactions t = new Transactions(
                    depositTime.format(dateFormatted),
                    depositTime.format(timeFormatted),
                    depositInfo,
                    source,
                    amount,
                    category
            );
            bufferedwriter(t);
            account.add(t);
        }catch (IOException | InputMismatchException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void payment(Scanner input, ArrayList<Transactions> account) {
        try {
            LocalDateTime depositTime = LocalDateTime.now();
            System.out.print(ANSI_BRIGHT_BLUE + "Enter the information about your payment: " + ANSI_RESET);
            String payInfo = input.nextLine();

            System.out.print("Enter please the Vendor / Source : ");
            String source = input.nextLine();

            System.out.print("Enter the amount that you payed : $ ");
            double amount = input.nextDouble();
            input.nextLine();

            System.out.println("Enter please the Category : ");
            System.out.println("1. Essentials (Rent / Mortgage, Utilities  (electricity, water, gas), Transportation )");
            System.out.println("2. Education (Courses,Books,Subscriptions (learning platforms) )");
            System.out.println("3. Food (grocery shopping, eating out)");
            System.out.println("4. Health (medical bills,pharmacy, gym)");
            System.out.println("5. Entertainment (Movies, Games, Streaming)");
            System.out.println("6. Other");
            System.out.print("Enter the choice : ");
            String category = "";
            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    category = "Essentials";
                    break;
                case 2:
                    category = "Education";
                    break;
                case 3:
                    category = "Food";
                    break;
                case 4:
                    category = "Health";
                    break;
                case 5:
                    category = "Entertainment";
                    break;
                case 6:
                    category = "Other";
                    break;
                default:
                    System.out.println("Invalid choice");
            }

            if (amount > 0) {
                amount = amount * -1;
            }
            Transactions t = new Transactions(
                    depositTime.format(dateFormatted),
                    depositTime.format(timeFormatted),
                    payInfo,
                    source,
                    amount,
                    category
            );

            bufferedwriter(t);
            account.add(t);

        } catch (IOException e) {
            System.out.println("Invalid input"+ e.getMessage());
        }
    }

    public static void accountStatementScreen(Scanner input, ArrayList<Transactions> account) {
     try {
         while (true) {
             System.out.println(ANSI_Cyan + "========== View Account Statement ==========" + ANSI_RESET);
             System.out.println("A) All        - Display all entries");
             System.out.println("D) Deposits   - Display only deposits");
             System.out.println("P) Payments   - Display only payments");
             System.out.println("R) Reports    - View reports");
             System.out.println("C) Custom Search - search transactions");
             System.out.println("B) Balance    -  See the balance  ");
             System.out.println("S) Spending   - By category");
             System.out.println("Q) Spending chart - display spending bar");
             System.out.println("H) Home       - Go back to home screen");
             System.out.println(ANSI_Cyan + "================================" + ANSI_RESET);
             System.out.print("Enter your choice: ");
             String choice = input.nextLine().toUpperCase().trim();
             account.clear();

             BufferedReader br = new BufferedReader(new FileReader("src/main/resources/transactions.csv"));
             String line;
             while ((line = br.readLine()) != null) {
                 if (line.trim().isEmpty()) continue;
                 if (line.startsWith("date")) continue;
                 String[] data = line.split("\\|");
                 Transactions t = new Transactions(data[0], data[1], data[2], data[3], Double.parseDouble(data[4]), data[5]);
                 account.add(t);

             }
             br.close();
             Collections.reverse(account);

             switch (choice) {
                 case "A":
                     for (Transactions t : account) {
                         System.out.println(t.toString());
                     }
                     break;

                 case "D":
                     for (Transactions t : account) {
                         if (t.getAmount() > 0) {
                             System.out.println(t.toString());
                         }
                     }
                     break;
                 case "P":
                     for (Transactions t : account) {
                         if (t.getAmount() < 0) {
                             System.out.println(t.toString());
                         }
                     }
                     break;
                 case "R":
                     reports(input, account);
                     break;
                 case "C":
                     customSearch(input, account);
                     break;
                 case "B":
                     balanceDisplay(account);
                     break;
                 case "S":
                     categoryDisplay(account);
                     break;
                 case "Q":
                     spendingChart(account);
                     break;
                 case "H":
                     return;
                 default:
                     System.out.println("Invalid choice");
             }
         }
     }catch (IOException e) {
         System.out.println("Invalid choice");
     }

    }

    public static void reports (Scanner input, ArrayList<Transactions> account) {
        try {
            while (true) {
                boolean found = false;
                System.out.println(ANSI_Cyan + "========== REPORTS ==========" + ANSI_RESET);
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
                                found = true;
                            }

                        }
                        break;

                    case 2:
                        LocalDate previousMonth = LocalDate.now().minusMonths(1);
                        for (Transactions t : account) {
                            LocalDate transactionDate = LocalDate.parse(t.getDate());
                            if (previousMonth.getMonth() == transactionDate.getMonth() && previousMonth.getYear() == transactionDate.getYear()) {
                                System.out.println(t.toString());
                                found = true;
                            }
                        }
                        break;

                    case 3:
                        LocalDate thisYear = LocalDate.now();
                        for (Transactions t : account) {
                            LocalDate transactionDate = LocalDate.parse(t.getDate());
                            if (thisYear.getYear() == transactionDate.getYear()) {
                                System.out.println(t.toString());
                                found = true;
                            }
                        }
                        break;

                    case 4:
                        LocalDate previousYear = LocalDate.now().minusYears(1);
                        for (Transactions t : account) {
                            LocalDate transactionDate = LocalDate.parse(t.getDate());
                            if (previousYear.getYear() == transactionDate.getYear()) {
                                System.out.println(t.toString());
                                found = true;
                            }
                        }
                        break;
                    case 5:
                        System.out.print("Enter the vendor: ");
                        String vendor = input.nextLine();
                        for (Transactions t : account) {
                            if (vendor.equalsIgnoreCase(t.getVendor())) {
                                System.out.println(t.toString());
                                found = true;
                            }
                        }
                        break;

                    case 0:
                        return;

                    default:
                        System.out.println("Invalid choice, please try again.");
                }
                if (!found) {
                    System.out.println("No transactions found");
                }
            }

        }catch (InputMismatchException e) {
            System.out.println("Invalid choice, please try again.");
        }
    }

    public static void customSearch (Scanner input, ArrayList<Transactions> account) {
        try {
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

            if (startDate.isEmpty()) {
                startDate = null;
            } else {
                startDateParsed = LocalDate.parse(startDate);
            }
            if (endDate.isEmpty()) {
                endDate = null;
            } else {
                endDateParsed = LocalDate.parse(endDate);
            }
            if (description.isEmpty()) {
                description = null;
            }
            if (vendor.isEmpty()) {
                vendor = null;
            }
            if (amount.isEmpty()) {
                amount = null;
            } else {
                amountParsed = Double.parseDouble(amount);
            }

            for (Transactions t : account) {
                boolean match = true;
                if (startDateParsed != null) {
                    LocalDate transactionDate = LocalDate.parse(t.getDate());
                    if (transactionDate.isBefore(startDateParsed)) {
                        match = false;
                    }
                }
                if (endDateParsed != null) {
                    LocalDate transactionDate = LocalDate.parse(t.getDate());
                    if (transactionDate.isAfter(endDateParsed)) {
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
        }catch (InputMismatchException e) {
            System.out.println("Invalid choice, please try again.");
        }
    }

    public static void balanceDisplay ( ArrayList<Transactions> account) {
        double deposit = 0.0;
        double payments = 0.0;

        for (Transactions t : account) {
            if (t.getAmount() > 0) {
                deposit += t.getAmount();
            }
            if (t.getAmount() < 0) {
                payments += t.getAmount();
            }

        }
        double total = deposit + payments;
        System.out.println( ANSI_Cyan + "===== BALANCE =====" + ANSI_RESET);
        System.out.printf("Total Deposits : $%,.2f%n" , deposit);
        System.out.printf("Total Withdrawals : $%,.2f%n", payments);
        System.out.printf("Current Balance: $%,.2f%n" , total);
        System.out.println(ANSI_Cyan +"==================="+ ANSI_RESET);

    }

    public static void categoryDisplay ( ArrayList<Transactions> account) {
        HashMap<String, Double> categoryTotals = new HashMap<>();

        for (Transactions t : account) {
            String category = t.getCategory();
            if(t.getAmount() <0 ){
                categoryTotals.put(category ,categoryTotals.getOrDefault(category, 0.0)+ t.getAmount());

            }

        }
        System.out.println(ANSI_Cyan + "===== SPENDING BY CATEGORY ====="+ ANSI_RESET);
        for (String category : categoryTotals.keySet()) {
            System.out.printf(
                    "%s: $%.2f%n",
                    category,
                    categoryTotals.get(category)
            );
        }
        System.out.println(ANSI_Cyan +"=================================" +ANSI_RESET);


    }

    public static void spendingChart ( ArrayList<Transactions> account) {
        HashMap<String, Integer> spendingBars = new HashMap<>();

        for (Transactions t : account) {
            String category = t.getCategory();
            if(t.getAmount() <0 ){
                spendingBars.put(category, spendingBars.getOrDefault(category, 0) + 1);
            }
        }
        System.out.println(ANSI_Cyan + "===== SPENDING CHART =====" + ANSI_RESET);
       for(String category : spendingBars.keySet()) {
           int count = spendingBars.get(category);
           String bar = "";
           for( int i = 0; i < count; i++ ) {
               bar += "#";
           }
           System.out.println(category + ": " + bar);
       }
        System.out.println(ANSI_Cyan + "==========================" +ANSI_RESET);


    }


    public static void logo(){
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

    public static void logo2() {
        System.out.println(ANSI_Cyan + "-----------------------S T E E L  C I T Y-----------------------" +ANSI_RESET);
        System.out.println("----------------------------------------------------------------");
        System.out.println(ANSI_BRIGHT_BLUE +"--------------THANK YOU FOR VISITING US TODAY!!!----------------" +ANSI_RESET);
        System.out.println("----------------------------------------------------------------");

        String logo2 = """
            .                                            .
                    .                      .              .           .
                            _    .                  .               _
                        _  | |          _                      _   | |
                       | | | |      _  | |            _       | |  | |  _
                     _ | | | |     | | | |      _    | |    _ | |  | | | |
                    | || |_| |_____| |_| |____ | |___| |___| |_| |__| |_| |
                    | ||      STEEL CITY BANK  |                       | |
                    | ||_______________________|_______________________| |
                   /  \\_________________________________________________/  \\
                  /_________________________________________________________\\
           """;
        System.out.println(logo2);


    }
}
