package threads;

/**
 *
 * @author Sydney Hutchens
 */
public class BankAccountRescuer extends BankAccountUser {
    public BankAccountRescuer(String name, BankAccount account, BankAccountUser[] users) {
        super(name, account, null);
        this.users = users;
    }
    
    @Override
    public synchronized void run() {
        while (allFinished() == false) {
            if(allWaiting() == true) {
                BankAccount tempAccount = getAccount();
                tempAccount.deposit(100, this);
                try {
                    wait();
                }
                catch (InterruptedException ex) { }
                try {
                    Thread.sleep((int)(Math.random() * 100));
                }
                catch (InterruptedException ex) { }
                notifyAll();
            }
        }
    }
    
    public boolean allFinished() {
       boolean done = false;
       for(BankAccountUser user : users) {
           if (user.getTransactionsRemaining() == 0) {
               done = true;
           }
       }
       return done;
    }
    
    public boolean allWaiting() {
        boolean wait = false;
        for (BankAccountUser user : users) {
            if(allFinished() == false && user.getWaiting() == true) {
                wait = true;
            } 
        }
        return wait;
    }
    
    private BankAccountUser[] users; 
}
