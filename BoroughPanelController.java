import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller class for the Borough Panel, which displays the COVID-19 data for
 * a specific borough within a specific range. It defines the table columns and
 * their corresponding data fields, and retrieves the data to be displayed in
 * the table. The class also sets the title of the borough panel.
 * 
 */
public class BoroughPanelController {

    private String boroughName;

    private final DataHandlerSingleton dataHandler = DataHandlerSingleton.getInstance();

    @FXML
    private Label boroughTitle;

    @FXML
    private TableView<CovidData> tableID;

    @FXML
    private TableColumn<CovidData, String> dateColumn;

    @FXML
    private TableColumn<CovidData, Integer> newCasesColumn;

    @FXML
    private TableColumn<CovidData, Integer> newDeathsColumn;

    @FXML
    private TableColumn<CovidData, Integer> totalCasesColumn;

    @FXML
    private TableColumn<CovidData, Integer> groceryPharmacyColumn;

    @FXML
    private TableColumn<CovidData, Integer> residentialsColumns;

    @FXML
    private TableColumn<CovidData, Integer> transitColumn;

    @FXML
    private TableColumn<CovidData, Integer> workplaceColumn;

    @FXML
    private TableColumn<CovidData, Integer> parksColumn;

    @FXML
    private TableColumn<CovidData, Integer> retailRecreationColumn;

    /**
     * This method is executed once the FXML page is loaded. It defines the data
     * that each column in the table will take.The string argument passed to
     * columnID.setCellValueFactory(new PropertyValueFactory<>(some String))
     * specifies the name of the field in the data object from which the column
     * should retrieve data.
     */
    public void initialize() {
        // Set cell value factories for all table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        newCasesColumn.setCellValueFactory(new PropertyValueFactory<>("newCases"));
        newDeathsColumn.setCellValueFactory(new PropertyValueFactory<>("newDeaths"));
        totalCasesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCases"));
        groceryPharmacyColumn.setCellValueFactory(new PropertyValueFactory<>("groceryPharmacyGMR"));
        residentialsColumns.setCellValueFactory(new PropertyValueFactory<>("residentialGMR"));
        transitColumn.setCellValueFactory(new PropertyValueFactory<>("transitGMR"));
        workplaceColumn.setCellValueFactory(new PropertyValueFactory<>("workplacesGMR"));
        parksColumn.setCellValueFactory(new PropertyValueFactory<>("parksGMR"));
        retailRecreationColumn.setCellValueFactory(new PropertyValueFactory<>("retailRecreationGMR"));
        // Set table data
        tableID.setItems(getData());
    }

    /**
     * This method retrieves all the data to be displayed in the table.
     * Each data object in the returned collection corresponds to one row in the
     * table. The fields in the data objects should have names that match the
     * string arguments passed.
     * 
     * @return list of CovidData objects containing the data for all rows in
     *         the table.
     */
    public ObservableList<CovidData> getData() {
        List<CovidData> dataForBorough = dataHandler.getBoroughData(boroughName);
        ObservableList<CovidData> observableList = FXCollections.observableArrayList(dataForBorough);
        return observableList;
    }

    /**
     * Sets the title of the borough pane (presented when a borough is clicked on
     * the map panel) to be the borough name.
     */
    public void setBoroughName(String name) {
        boroughName = name;
        boroughTitle.setText(boroughName);
        initialize();
    }

}
