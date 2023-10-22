import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * This is the Covid App Launcher class.
 * The class extends the Application class to create a graphical user interface
 * (GUI).
 * The main method is the entry point of the application and is used to launch
 * the application.
 */
public class App extends Application {
    private final int MIN_HEIGHT = 600;
    private final int MIN_WIDTH = 1000;
    private final String APP_TITLE = "COVID-19 Statistic Explorer";
    private final String MAIN_PANEL = "MainPanel.fxml";

    @Override
    /**
     * App startup and what the display will be first thing opening the app.
     * The minimum height and width are set.
     * 
     * @param stage The main stage for the application.
     */
    public void start(Stage stage) throws IOException {
        BorderPane mainPanelRoot = FXMLLoader.load(getClass().getResource(MAIN_PANEL));

        Scene scene = new Scene(mainPanelRoot);

        stage.setTitle(APP_TITLE);
        stage.setScene(scene);
        stage.show();

        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
    }

    /**
     * App main launcher. Can be used to run the application from the editor.
     * 
     * @param args Args is used to collect data from the terminal.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
