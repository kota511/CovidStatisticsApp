import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * The MainPanelController class handles the main functionality of the
 * application.
 * 
 * The class contains methods for initializing the application, loading and
 * resizing panels, handling DatePicker events, switching between panels amd
 * updating the UI based on user input.
 * 
 */
public class MainPanelController {

    // Data handler singleton for retrieving data
    private final DataHandlerSingleton dataHandler = DataHandlerSingleton.getInstance();

    // The names of the different FXML to be used
    private final String WELCOME_PANEL = "WelcomePanel.fxml";
    private final String MAP_PANEL = "MapPanel.fxml";
    private final String STATISTICS_PANEL = "StatisticsPanel.fxml";
    private final String GRAPH_PANEL = "GraphPanel.fxml";
    private final String WELCOME_PANEL_INFO = "WelcomePanelInfo.fxml";
    private final String MAP_PANEL_INFO = "MapPanelInfo.fxml";
    private final String STATISTICS_PANEL_INFO = "StatisticsPanelInfo.fxml";
    private final String GRAPH_PANEL_INFO = "GraphPanelInfo.fxml";
    private final String INFO_PANEL = "informationWindow.fxml";
    private final String INFO_PANEL_TITLE = "Information window";

    // Scaling attributes
    private final double WIDTH_FACTOR = 0.4;
    private final double HEIGHT_FACTOR = 0.6;
    private final double PIVOT_FACTOR = 2.0;

    // The controllers for some of the panels which are set to the centre of the
    // main panel
    private MapPanelController mapController;
    private StatisticsPanelController statisticController;
    private GraphPanelController graphController;

    // Arrays for the panels and controllers
    private final String[] PANEL_FXML_FILES = { WELCOME_PANEL, MAP_PANEL, STATISTICS_PANEL,
            GRAPH_PANEL };
    // Constants to remove magic numbers
    private final int NUMBER_OF_PANELS = PANEL_FXML_FILES.length;
    private final int MAX_PANEL_INDEX = NUMBER_OF_PANELS - 1;

    private final String[] INFO_FXML_FILES = { WELCOME_PANEL_INFO, MAP_PANEL_INFO,
            STATISTICS_PANEL_INFO, GRAPH_PANEL_INFO };
    private final Region[] PANELS = new Region[NUMBER_OF_PANELS];
    private final Parent[] INFO_PANELS = new Parent[NUMBER_OF_PANELS];

    private int currentPanelIndex = 0;

    private List<LocalDate> dates;

    @FXML
    private DatePicker datePickerLower;

    @FXML
    private DatePicker datePickerUpper;

    @FXML
    private Button previousPanel;

    @FXML
    private Button nextPanel;

    @FXML
    private BorderPane root;

    @FXML
    private TextArea informationBox;

    @FXML
    private Label dateRangeLabel;

    @FXML
    /**
     * This method is called once the FXML is loaded.
     * The panels to be placed in the centre of the main panel, the information
     * panels, and the panel controllers are loaded into their respective arrays,
     * and the initial panel is set.
     * The datepickers are populated to only allow the user to select valid periods.
     */
    public void initialize() {
        for (int i = 0; i < NUMBER_OF_PANELS; i++) {
            try {
                FXMLLoader panelLoader = new FXMLLoader(getClass().getResource(PANEL_FXML_FILES[i]));
                FXMLLoader infoLoader = new FXMLLoader(getClass().getResource(INFO_FXML_FILES[i]));
                PANELS[i] = panelLoader.load();
                INFO_PANELS[i] = infoLoader.load();

                Object controller = panelLoader.getController();
                if (controller instanceof MapPanelController) {
                    mapController = (MapPanelController) controller;
                } else if (controller instanceof StatisticsPanelController) {
                    statisticController = (StatisticsPanelController) controller;
                } else if (controller instanceof GraphPanelController) {
                    graphController = (GraphPanelController) controller;
                }

                resizePanel(PANELS[i]);
            } catch (java.io.IOException ioe) {
                ioe.printStackTrace();
            }
        }

        root.setCenter(PANELS[0]);

        dates = dataHandler.getDatesRange();

        // Disables buttons which are after the upper date selected for the lower date
        // picker.
        datePickerLower.setDayCellFactory(lowerDatePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (datePickerUpper.getValue() != null && item.isAfter(datePickerUpper.getValue())
                        || !dates.contains(item)) {
                    setDisable(true);
                }
            }
        });

        // Disables buttons which are before the lower date selected for the upper date
        // picker.
        datePickerUpper.setDayCellFactory(upperDatePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (datePickerLower.getValue() != null && item.isBefore(datePickerLower.getValue())
                        || !dates.contains(item)) {
                    setDisable(true);
                }
            }
        });
    }

    @FXML
    /**
     * Called when a date picker is clicked, it sets the date boundaries and updates
     * the UI appropriately
     * 
     * @param event Date picker is clicked.
     */
    void handleDatePicker(ActionEvent event) {
        setDateBoundaries();
        updateUI();
    }

    private void setDateBoundaries() {
        dataHandler.setLowerDate(datePickerLower.getValue());
        dataHandler.setUpperDate(datePickerUpper.getValue());
    }

    /**
     * Updates the interface when the user initially enters a date period or when
     * they change the selected date for all panels.
     */
    private void updateUI() {
        if (datePickerLower.getValue() != null && datePickerUpper.getValue() != null) {
            dateRangeLabel.setText(
                    "From: " + dataHandler.getLowerDate().toString() + " to " + dataHandler.getUpperDate().toString());
            disableButtons(false);
            dataHandler.filterListOfRecords();
            statisticController.setStatistic(statisticController.getCurrentStatisticsIndex());
            mapController.adjustColor();
            graphController.clear();
        }
    }

    /**
     * Disables the buttons, not allowing the user to change panel.
     * 
     * @param disable
     */
    private void disableButtons(boolean disable) {
        nextPanel.setDisable(disable);
        previousPanel.setDisable(disable);
    }

    @FXML
    /**
     * Switches to the previous panel in the array.
     * 
     * @param event The previous panel button is clicked
     */
    void switchToPreviousPanel(ActionEvent event) {
        currentPanelIndex--;
        if (currentPanelIndex < 0) {
            currentPanelIndex = MAX_PANEL_INDEX;
        }
        switchToPanel(PANELS[currentPanelIndex]);
    }

    @FXML
    /**
     * Switches to the next panel in the array.
     * 
     * @param event The next panel button is clicked
     */
    void switchToNextPanel(ActionEvent event) {
        currentPanelIndex++;
        if (currentPanelIndex > MAX_PANEL_INDEX) {
            currentPanelIndex = 0;
        }
        switchToPanel(PANELS[currentPanelIndex]);
    }

    /**
     * Used to change the panel at the centre of the main panel.
     * 
     * @param panel the panel to set the center to
     */
    private void switchToPanel(Region panel) {
        root.setCenter(panel);
    }

    /**
     * Rescales the panels according to the window size.
     * 
     * @param panel
     */
    private void resizePanel(Region panel) {

        panel.getTransforms().clear();
        Scale scale = new Scale();

        scale.xProperty().bind(root.widthProperty().divide(panel.widthProperty()).multiply(WIDTH_FACTOR));
        scale.yProperty().bind(root.heightProperty().divide(panel.heightProperty()).multiply(HEIGHT_FACTOR));

        scale.pivotXProperty().bind(panel.widthProperty().divide(PIVOT_FACTOR));
        scale.pivotYProperty().bind(panel.heightProperty().divide(PIVOT_FACTOR));

        panel.getTransforms().add(scale);
    }

    @FXML
    /**
     * Opens up the correct information panel for the current panel in the main
     * panel.
     * 
     * @throws IOException
     */
    void openInformationTab() throws IOException {

        BorderPane root = FXMLLoader.load(getClass().getResource(INFO_PANEL));
        Stage stage = new Stage();
        stage.setTitle(INFO_PANEL_TITLE);

        stage.setScene(new Scene(root));
        root.setCenter(INFO_PANELS[currentPanelIndex]);
        stage.show();

        stage.setResizable(false);

    }

}
