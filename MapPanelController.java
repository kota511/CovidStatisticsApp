import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * MapPanelController class manages the Map panel in the application. It
 * displays a map of London with boroughs as buttons.
 * The class provides functionality for changing the button color based on the
 * number of COVID deaths in the borough and for displaying borough data when a
 * borough button is clicked. It also contains a dictionary which maps initials
 * to full name.
 * 
 */
public class MapPanelController {
    // FXML annotations for the borough buttons
    @FXML
    private Button enfi, barn, hgry, walt, hrrw, bren, camd, isli, hack, redb, have, hill, eali, kens, wstm, towh, newh,
            bark, houn, hamm, wand, city, gwch, bexl, rich, mert, lamb, sthw, lews, king, sutt, croy, brom;

    // Data handler singleton for retrieving data
    private final DataHandlerSingleton dataHandler = DataHandlerSingleton.getInstance();
    private static final HashMap<String, String> BOROUGH_DICTIONARY = createDictionary();

    // ArrayList of borough buttons
    private static final ArrayList<Button> BUTTONS = new ArrayList<>();
    private static int maximumDeathsInBorough = 0;
    private final String BOROUGH_PANE = "BoroughPanel.fxml";

    @FXML
    /**
     * Initialising the buttons.
     */
    public void initialize() {
        addButtonsToList();
    }

    /**
     * Add all FXML buttons to list.
     */
    private void addButtonsToList() {
        BUTTONS.addAll(Arrays.asList(enfi, barn, hgry, walt, hrrw, bren, camd, isli, hack, redb, have, hill, eali, kens,
                wstm, towh, newh, bark, houn, hamm, wand, city, gwch, bexl, rich, mert, lamb, sthw, lews, king, sutt,
                croy, brom));
    }

    /**
     * Method to find the borough with most COVID deaths.
     * 
     * @param deathsInBorough
     */

    private void updateMaximumDeaths(int deathsInBorough) {
        if (deathsInBorough > maximumDeathsInBorough) {
            maximumDeathsInBorough = deathsInBorough;
        }
    }

    /**
     * Calculates the death percentage for a given borough associated with the
     * specified button.
     * 
     * @param button button associated with borough for which to calculate death
     *               percentage
     * @return death percentage for the specified borough
     */
    private double calculateDeathPercentage(Button button) {
        String borough = BOROUGH_DICTIONARY.get(button.getText());
        double boroughDeaths = dataHandler.getBoroughDeaths(borough);
        double deathPercentage = (boroughDeaths / maximumDeathsInBorough) * 100.0;
        return deathPercentage;
    }

    /**
     * Updates the shade of the buttons depending on the deaths for each borough at
     * the dates selected
     */
    public void adjustColor() {
        maximumDeathsInBorough = 0;
        BUTTONS.forEach(button -> {
            String borough = BOROUGH_DICTIONARY.get(button.getText());
            updateMaximumDeaths(dataHandler.getBoroughDeaths(borough));
        });

        BUTTONS.forEach(button -> {
            button.setStyle("-fx-background-color:" +
                    "radial-gradient(center 50% 50%, radius " +
                    calculateDeathPercentage(button)
                    + "%, #e76f51, #ffe0d4);");
        });
    }

    /**
     * This method changes the background colour of a button when the cursor enters.
     * 
     * @param event The MouseEvent triggered by hovering over the button.
     */
    @FXML
    void buttonEntered(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color:" +
                "radial-gradient(center 50% 50%, radius " +
                calculateDeathPercentage(button)
                + "%, #728be8, #ffe0d4);");
    }

    /**
     * This method changes the background colour of a button when the cursor leaves.
     * 
     * @param event The MouseEvent triggered by hovering off the button.
     */
    @FXML
    void buttonExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color:" +
                "radial-gradient(center 50% 50%, radius " +
                calculateDeathPercentage(button)
                + "%, #e76f51, #ffe0d4);");
    }

    @FXML
    /**
     * When the user clicks on a borough button, it displays the data
     * collection of the borough for the specified period in a new window.
     * 
     * @param event
     * @throws IOException
     */
    void onClicked(ActionEvent event) throws IOException {

        String borough = BOROUGH_DICTIONARY.get(((Button) event.getSource()).getText());

        FXMLLoader loader = new FXMLLoader(getClass().getResource(BOROUGH_PANE));
        Parent root = loader.load();

        BoroughPanelController controller = loader.getController();
        controller.setBoroughName(borough);

        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.setTitle(borough);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Dictionary mapping the boroughs initials to its full name
     * 
     * @return dictionary
     */
    public static HashMap<String, String> createDictionary() {
        HashMap<String, String> dict = new HashMap<>();
        dict.put("ENFI", "Enfield");
        dict.put("BARN", "Barnet");
        dict.put("HRGY", "Haringey");
        dict.put("WALT", "Waltham Forest");
        dict.put("HRRW", "Harrow");
        dict.put("BREN", "Brent");
        dict.put("CAMD", "Camden");
        dict.put("ISLI", "Islington");
        dict.put("HACK", "Hackney");
        dict.put("REDB", "Redbridge");
        dict.put("HAVE", "Havering");
        dict.put("HILL", "Hillingdon");
        dict.put("EALI", "Ealing");
        dict.put("KENS", "Kensington And Chelsea");
        dict.put("WSTM", "Westminster");
        dict.put("TOWH", "Tower Hamlets");
        dict.put("NEWH", "Newham");
        dict.put("BARK", "Barking And Dagenham");
        dict.put("HOUN", "Hounslow");
        dict.put("HAMM", "Hammersmith And Fulham");
        dict.put("WAND", "Wandsworth");
        dict.put("CITY", "City Of London");
        dict.put("GWCH", "Greenwich");
        dict.put("BEXL", "Bexley");
        dict.put("RICH", "Richmond Upon Thames");
        dict.put("MERT", "Merton");
        dict.put("LAMB", "Lambeth");
        dict.put("STHW", "Southwark");
        dict.put("LEWS", "Lewisham");
        dict.put("KING", "Kingston Upon Thames");
        dict.put("SUTT", "Sutton");
        dict.put("CROY", "Croydon");
        dict.put("BROM", "Bromley");

        return dict;
    }

}
