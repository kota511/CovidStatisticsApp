import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.function.ToIntFunction;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The DataHandlerSingleton class provides an easy way for various panels to
 * call data without having to filter the data themselves.
 * It follows the singleton design pattern and only creates a new instance if
 * one doesn't already exist.
 *
 */
public class DataHandlerSingleton {
    private ArrayList<CovidData> listOfRecords;
    private List<CovidData> filteredListOfRecords;

    private CovidDataLoader covidDataLoader = new CovidDataLoader();
    private LocalDate lowerDate = null;
    private LocalDate upperDate = null;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int NUMBER_OF_BOROUGHS = 33;

    private static DataHandlerSingleton instance;

    /**
     * Gets the data handler object, only creates a new one if one doesn't already
     * exist as per the singelton construct
     * 
     * @return returns the dataHandlerSingleton object
     */
    public static DataHandlerSingleton getInstance() {
        if (instance == null) {
            instance = new DataHandlerSingleton();
        }
        return instance;
    }

    /**
     * Constructor method which reads the data from the CSV file and filters a
     * record if it contains any null items in order to produce more accurate
     * statistics.
     */
    public DataHandlerSingleton() {
        listOfRecords = covidDataLoader.load().stream()
                .filter(data -> data.getBorough() != null)
                .filter(data -> data.getDate() != null)
                .filter(data -> data.getNewCases() != null)
                .filter(data -> data.getTotalCases() != null)
                .filter(data -> data.getNewDeaths() != null)
                .filter(data -> data.getParksGMR() != null)
                .filter(data -> data.getTotalDeaths() != null)
                .filter(data -> data.getWorkplacesGMR() != null)
                .filter(data -> data.getGroceryPharmacyGMR() != null)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Set the lower date boundary.
     * 
     * @param date
     */
    public void setLowerDate(LocalDate date) {
        lowerDate = date;
    }

    /**
     * Set the upper date boundary.
     * 
     * @param date
     */
    public void setUpperDate(LocalDate date) {
        upperDate = date;
    }

    /**
     * @return upper date boundary
     */
    public LocalDate getUpperDate() {
        return upperDate;
    }

    /**
     * @return lower date boundary
     */
    public LocalDate getLowerDate() {
        return lowerDate;
    }

    /**
     * @return list of all dates in the CSV file
     */
    public List<LocalDate> getDatesRange() {
        return listOfRecords.stream()
                .map(data -> LocalDate.parse(data.getDate(), DATE_FORMAT))
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Creates a list which contains only the records inclusive between the two
     * selected data boundaries.
     */
    public void filterListOfRecords() {
        filteredListOfRecords = listOfRecords.stream()
                .filter(data -> !LocalDate.parse(data.getDate(), DATE_FORMAT).isBefore(lowerDate))
                .filter(data -> !LocalDate.parse(data.getDate(), DATE_FORMAT).isAfter(upperDate))
                .collect(Collectors.toList());
    }

    /**
     * @return list of all dates in the range between the user defined upper and
     *         lower date bounds (inclusive)
     */
    public List<LocalDate> getFilteredDatesRange() {
        return filteredListOfRecords.stream()
                .map(data -> LocalDate.parse(data.getDate(), DATE_FORMAT))
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * @param borough the selected borough
     * @return list of records for the selected borough which are within the
     *         specificed date range.
     */
    public List<CovidData> getBoroughData(String borough) {
        return filteredListOfRecords.stream()
                .filter(data -> data.getBorough().equals(borough))
                .collect(Collectors.toList());
    }

    /**
     * Calculates the total metric of a specific CovidData attribute up until a
     * specific end date.
     * 
     * @param end             the end date up to which the total is calculated
     * @param metricExtractor a function which extracts the metric of interest from
     *                        a CovidData object
     * @return the total metric of the attribute up until the specified date
     *
     */
    private int getTotalMetric(LocalDate end, ToIntFunction<CovidData> metricExtractor) {
        return listOfRecords.stream()
                .filter(record -> !LocalDate.parse(record.getDate(), DATE_FORMAT).isAfter(end))
                .mapToInt(metricExtractor)
                .sum();

    }

    /**
     * Calculates the total metric of a specific CovidData attribute between two
     * dates.
     * 
     * @param start           the start date of the period
     * @param end             the end date of the period
     * @param metricExtractor a function which extracts the metric of interest from
     *                        a CovidData object
     * @return the total metric of the attribute between the specified dates
     */
    private int getTotalMetricGMR(LocalDate start, LocalDate end, ToIntFunction<CovidData> metricExtractor) {
        return listOfRecords.stream()
                .filter(record -> !LocalDate.parse(record.getDate(), DATE_FORMAT).isAfter(end))
                .filter(record -> !LocalDate.parse(record.getDate(), DATE_FORMAT).isBefore(start))
                .mapToInt(metricExtractor)
                .sum();
    }

    /**
     * Calculates the total number of deaths up until a specific date.
     * 
     * @param end the end date up to which the total number of deaths is calculated
     * @return the total number of deaths up until the specified date
     */
    public int getTotalDeaths(LocalDate end) {
        return getTotalMetric(end, CovidData::getNewDeaths);
    }

    /**
     * Calculate the average number of cases per borough up until the end date.
     *
     * @param end the end date up to which the total number of cases is calculated
     * @return the average number of cases per borough at that date
     */
    public double getAverageCases(LocalDate end) {
        int totalCases = getTotalMetric(end, CovidData::getNewCases);
        return (double) totalCases / NUMBER_OF_BOROUGHS;
    }

    /**
     * Calculate the average change in traffic for workplaces between the start and
     * end date for all boroughs.
     * 
     * @param start start date
     * @param end   end date
     * @return average change in traffic for workplaces between the start and end
     *         date for all boroughs
     */
    public double getAverageWorkplacesGMR(LocalDate start, LocalDate end) {
        int totalWorkplacesGMR = getTotalMetricGMR(start, end, CovidData::getWorkplacesGMR);
        return (double) totalWorkplacesGMR / (NUMBER_OF_BOROUGHS * getFilteredDatesRange().size());
    }

    /**
     * Calculate the average change in grocery and pharmacy traffic between the
     * start and
     * end date for all boroughs.
     * 
     * @param start start date
     * @param end   end date
     * @return average change in grocery and pharmacy traffic between the start and
     *         end date for all boroughs.
     */
    public double getAverageGroceryPharmacyGMR(LocalDate start, LocalDate end) {
        int totalGroceryPharmacyGMR = getTotalMetricGMR(start, end, CovidData::getGroceryPharmacyGMR);
        return (double) totalGroceryPharmacyGMR / (NUMBER_OF_BOROUGHS * getFilteredDatesRange().size());
    }

    /**
     * Calcualte the total deaths at the end of our selected date range.
     * 
     * @param boroughName the borough name to get deaths for
     * @return The total deaths on the last day of our range for the specified
     *         borough
     */
    public int getBoroughDeaths(String boroughName) {
        if (getBoroughData(boroughName).size() != 0) {
            return getBoroughData(boroughName).get(0).getTotalDeaths();
        }
        return 0;
    }

}
