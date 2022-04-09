package threads;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * The top-level class for the bank account simulation.
 * It creates and displays a scene with a message logging area to the left and
 * simulation controls to the right.
 * @author tcolburn
 */
public class Simulation extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        LogView logView = new LogView();
        BankAccount account = new BankAccount(logView);
        SimulationControl simulationControl = new SimulationControl(account, logView);
        
        HBox root = new HBox();
        root.getChildren().addAll(logView, simulationControl);
        
        Scene scene = new Scene(root, 800, 500);
        
        primaryStage.setTitle("Bank Account Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}