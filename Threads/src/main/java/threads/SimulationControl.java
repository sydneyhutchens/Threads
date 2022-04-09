package threads;

import java.util.Random;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A class providing controls for a bank account simulation.
 * Controls allow for selecting a starting balance for the account, 
 * the number of users (siblings) of the account, the transaction limit,
 * the number of transactions each user will make, and a button to
 * run the simulation.
 * @author tcolburn
 */
public class SimulationControl extends VBox {
    
    /**
     * Creates the simulation controls arranged in a vertical box
     * @param account the bank account
     * @param logView the message logging area
     */
    public SimulationControl(BankAccount account, LogView logView) {
        this.account = account;
        this.logView = logView;
        
        balance = new ComboBox(FXCollections.observableArrayList(100, 500, 1000, 10000));
        balance.setValue(100);
        
        siblings = new ComboBox(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        siblings.setValue(1);
        
        limit = new ComboBox(FXCollections.observableArrayList(50, 100, 200, 500));
        limit.setValue(50);
        
        numTransactions = new ComboBox(FXCollections.observableArrayList(10, 25, 50, 100));
        numTransactions.setValue(10);
        
        setPadding(new Insets(10));
        
        super.getChildren().addAll(
                new LabeledControl("Starting Balance:", balance),
                new LabeledControl("Number of Siblings:", siblings),
                new LabeledControl("Transaction Limit:", limit),
                new LabeledControl("Transactions per Sibling:", numTransactions),
                new Runner());
    }
    
    /**
     * A private class for creating labeled combo boxes.
     */
    private class LabeledControl extends HBox {
        public LabeledControl(String label, Control control) {
            setPadding(new Insets(5));
            setSpacing(10);
            setAlignment(Pos.CENTER_LEFT);
            Label lab = new Label(label);
            lab.setPrefWidth(labelWidth);
            super.getChildren().addAll(lab, control);
        }
    }
    
    /**
     * A private class for the RUN button.
     * When clicked, the users are generated, the account balance is set.
     * and each user's transactions are executed.
     */
    private class Runner extends Button {
        public Runner() {
            super("RUN");
            setOnAction(e -> {
                generateUsers();
                account.setBalance(balance.getValue());
                logView.clear();
                try {
                    for (BankAccountUser user : users) {
                        user.start();
                    }
                    parent.start();
                }
                catch(Exception ex) {
                    logView.log(ex.getMessage());
                }
            });
        }
    }
    
    /**
     * A private helper method that generates an array of bank account users
     * as an instance field.
     */
    private void generateUsers() {
        users = new BankAccountUser[siblings.getValue()];
        for (int i = 0; i < users.length; i++) {
            users[i] = new BankAccountUser("Sibling " + (i+1), account, generateTransactions());
        }
        
        parent = new BankAccountRescuer("Parent", account, users);
    }
    
    /**
     * A private helper method that generates an integer array of random transactions.
     * A positive integer is a deposit; a negative integer is a withdrawal.
     * The transaction amounts are randomly generated within the limit, and whether
     * the amount is a deposit or withdrawal is also random.
     * @return the array of transactions
     */
    private int[] generateTransactions() {
        int[] transactions = new int[numTransactions.getValue()];
        for (int i = 0; i < transactions.length; i++) {
            int amount = GENERATOR.nextInt(limit.getValue()) + 1; // 1 <= amount <= amountLimit
            transactions[i] = GENERATOR.nextBoolean() ? amount : -amount;
        }
        return transactions;
    }
    
    private static final Random GENERATOR = new Random();
    
    private final ComboBox<Integer> balance;
    private final ComboBox<Integer> siblings;
    private final ComboBox<Integer> limit;
    private final ComboBox<Integer> numTransactions;
    
    private final BankAccount account;
    
    private final LogView logView;
    
    private BankAccountUser[] users;
    
    private BankAccountRescuer parent;
    
    private final double labelWidth = new Text("Transactions per Sibling:").getLayoutBounds().getWidth();
}