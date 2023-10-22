import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javafx.event.ActionEvent;

/**
 * Controller class for the statistics panel.
 * Displays different statistics about the COVID-19 pandemic.
 * Allows the user to switch between statistics using "next" and "previous"
 * buttons.
 * 
 */
public class StatisticsPanelController {

    // Data handler singleton for retrieving statistics data
    private final DataHandlerSingleton dataHandler = DataHandlerSingleton.getInstance();

    // Current selected statistic index
    private int currentStatisticIndex = 0;

    // Constants for different statistic indices
    private static final int INDEX_MAX = 4;
    private static final int TOTAL_DEATHS_INDEX = 0;
    private static final int AVERAGE_CASES_INDEX = 1;
    private static final int DATE_WITH_HIGHEST_DEATHS_INDEX = 2;
    private static final int AVERAGE_WORKPLACE_TRAFFIC_INDEX = 3;
    private static final int AVERAGE_GROCERY_PHARMACY_TRAFFIC_INDEX = 4;

    // Constants for different statistic descriptions
    private static final String TOTAL_DEATHS_DESCRIPTION = "\nTotal Deaths\n";
    private static final String AVERAGE_CASES_DESCRIPTION = "Average Cases";
    private static final String DATE_WITH_HIGHEST_DEATHS_DESCRIPTION = "\nDate with Highest Deaths\n";
    private static final String AVERAGE_WORKPLACE_TRAFFIC_DESCRIPTION = "Average Change in Workplace Traffic";
    private static final String AVERAGE_GROCERY_PHARMACY_TRAFFIC_DESCRIPTION = "Average Change in Grocery and Pharmacy Traffic";

    @FXML
    private TextArea statisticsShower;

    @FXML
    /**
     * Switches to the next statistic
     * 
     * @param event next statistic button clicked
     */
    void switchToNextStatistic(ActionEvent event) {
        currentStatisticIndex++;
        if (currentStatisticIndex > INDEX_MAX) {
            currentStatisticIndex = 0;
        }
        setStatistic(currentStatisticIndex);
    }

    @FXML
    /**
     * Switches to the previous statistic
     * 
     * @param event previous statistic button clicked
     */
    void switchToPreviousStatistic(ActionEvent event) {
        currentStatisticIndex--;
        if (currentStatisticIndex < 0) {
            currentStatisticIndex = INDEX_MAX;
        }
        setStatistic(currentStatisticIndex);
    }

    /**
     * Formats the output text the statistics which require an average.
     * 
     * @param description the description of the statistic
     * @param value       the value of the statistic
     */
    private String statisticText(String description, double value) {
        return "\n" + description + "\n" + String.format("%.2f", value);

    }

    /**
     * Sets the text of the text-area to the statistic with the current index.
     * 
     * @param index current statistic index
     */
    public void setStatistic(int index) {
        switch (index) {
            case TOTAL_DEATHS_INDEX:
                int totalDeaths = dataHandler.getTotalDeaths(dataHandler.getUpperDate());
                statisticsShower.setText(TOTAL_DEATHS_DESCRIPTION + totalDeaths);
                break;
            case AVERAGE_CASES_INDEX:
                double averageCases = dataHandler.getAverageCases(dataHandler.getUpperDate());
                statisticsShower.setText(statisticText(AVERAGE_CASES_DESCRIPTION, averageCases));
                break;
            case DATE_WITH_HIGHEST_DEATHS_INDEX:
                statisticsShower.setText(DATE_WITH_HIGHEST_DEATHS_DESCRIPTION + dataHandler.getUpperDate());
                break;

            case AVERAGE_WORKPLACE_TRAFFIC_INDEX:
                double averageWorkplace = dataHandler.getAverageWorkplacesGMR(dataHandler.getLowerDate(),
                        dataHandler.getUpperDate());
                statisticsShower.setText(statisticText(AVERAGE_WORKPLACE_TRAFFIC_DESCRIPTION, averageWorkplace));
                break;

            case AVERAGE_GROCERY_PHARMACY_TRAFFIC_INDEX:
                double averageGroceryPharmacy = dataHandler
                        .getAverageGroceryPharmacyGMR(dataHandler.getLowerDate(), dataHandler.getUpperDate());
                statisticsShower
                        .setText(statisticText(AVERAGE_GROCERY_PHARMACY_TRAFFIC_DESCRIPTION, averageGroceryPharmacy));
                break;
        }

    }

    /**
     * Returns the current statistic indexs
     */
    public int getCurrentStatisticsIndex() {
        return currentStatisticIndex;
    }

}
