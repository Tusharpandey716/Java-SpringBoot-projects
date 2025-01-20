import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Expense {
    private LocalDate date;
    private String category;
    private double amount;

    public Expense(LocalDate date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    // getters and setters
}

class User {
    private String username;
    private String password;
    private List<Expense> expenses;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.expenses = new ArrayList<>();
    }

    // getters and setters
}

class ExpenseTracker {
    private List<User> users;

    public ExpenseTracker() {
        this.users = new ArrayList<>();
    }

    public void registerUser(String username, String password) {
        // check if user already exists
        // create and add new user
    }

    public void addExpense(String username, String category, LocalDate date, double amount) {
        // find user
        // add expense to user's list of expenses
    }

    public void listExpenses(String username) {
        // find user
        // print user's list of expenses
    }

    public void categoryWiseSummation(String username) {
        // find user
        // print category-wise summation of expenses
    }

    public void saveExpenseData(String filename) {
        // save expense data to file
    }

    public void loadExpenseData(String filename) {
        // load expense data from file
    }
}

public class Main {
    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.printlnimport java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Expense {
    private LocalDate date;
    private String category;
    private double amount;

    public Expense(LocalDate date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    // getters and setters
}

class User {
    private String username;
    private String password;
    private List<Expense> expenses;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.expenses = new ArrayList<>();
    }

    // getters and setters
}

class ExpenseTracker {
    private List<User> users;

    public ExpenseTracker() {
        this.users = new ArrayList<>();
    }

    public void registerUser(String username, String password) {
        // check if user already exists
        // create and add new user
    }

    public void addExpense(String username, String category, LocalDate date, double amount) {
        // find user
        // add expense to user's list of expenses
    }

    public void listExpenses(String username) {
        // find user
        // print user's list of expenses
    }

    public void categoryWiseSummation(String username) {
        // find user
        // print category-wise summation of expenses
    }

    public void saveExpenseData(String filename) {
        // save expense data to file
    }

    public void loadExpenseData(String filename) {
        // load expense data from file
    }
}

public class Main {
    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println