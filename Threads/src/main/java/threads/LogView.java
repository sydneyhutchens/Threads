package threads;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * This class provides a viewing area for logged messages from the
 * bank account simulation.
 * @author tcolburn
 */
public class LogView extends TextArea {
    
    /**
     * Creates a text area 500 x 400 pixels.
     */
    public LogView() {
        super.setPrefSize(500, 400);
    }
    
    /**
     * Adds a message to the text area by inserting at the end of the
     * existing text.
     * @param message the message to be added
     */
    public void log(String message) {
         Platform.runLater(() -> {  // to avoid synch issues with the JavaFX thread
            insertText(getText().length(), message);
        });
    }
    
    @Override
    public void clear() {
        super.clear();
    }
    
}