package threads;

/**
 * This class represents a bank account. It provides methods for setting the 
 * balance, depositing, and withdrawing.
 * @author tcolburn
 */
public class BankAccount {
    
    /**
     * Creates a new bank account.
     * @param logView the viewing area for logging deposit and withdrawal messages
     */
    public BankAccount(LogView logView) {
        this.logView = logView;
    }
    
    /**
     * Deposits an amount on behalf of a bank account user.
     * A message is logged showing the deposit amount and new balance.
     * Note that an assertion is used to check if this method is thread-safe.
     * @param amount the amount to deposit
     * @param user the user making the deposit
     */
    public synchronized void deposit(int amount, BankAccountUser user) {
        int newBalance = balance + amount;
        logView.log("\n" +user.getName()+ " Depositing $" +amount);
        balance = balance + amount;
        logView.log(". Balance = " + balance);
        checkFinished(user);
        notifyAll();
        assert(balance == newBalance); // should be true if this method is thread-safe
    }
    /**
     * Withdraws an amount on behalf of a bank account user.
     * A message is logged showing the withdrawal amount and new balance.
     * If the withdrawal would cause a negative balance, a runtime exception
     * is thrown.
     * Note that an assertion is used to check if this method is thread-safe.
     * @param amount the amount to withdraw
     * @param user the user making the withdrawal
     */
    public synchronized void withdraw(int amount, BankAccountUser user) throws InterruptedException {
        int newBalance = balance - amount;
        logView.log("\n" +user.getName() + " Withdrawing $" + amount);
        if ( amount > balance ) user.setWaiting(true);
        
        while( amount > balance) {
            wait();
        }
        user.setWaiting(false);
        balance = balance - amount;
        logView.log(". Balance = " + balance);
        checkFinished(user);
        notifyAll();
        assert(balance == newBalance); // should be true if this method is thread-safe
    }
    
    /**
     * Private helper method to log user finished message.
     * @param user the bank account user
     */
    private void checkFinished(BankAccountUser user) {
            if (user.getTransactionsRemaining() == 1) {
            logView.log("\n******************************\n        " + user.getName() + 
                        " finished.\n******************************");
        }
    }

    /**
     * Setter to set the initial balance of this bank account.
     * @param balance the initial balance
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Getter for the current balance of this bank account.
     * @return the current balance
     */
    public int getBalance() {
        return balance;
    }
    
    private final LogView logView;
    
    private int balance;
}