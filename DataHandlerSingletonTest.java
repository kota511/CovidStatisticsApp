import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.List;

/**
 * The test class for the DataHandlerSingleton class.
 *
 */
public class DataHandlerSingletonTest

{//
    private DataHandlerSingleton dataHandler;

    // Initializing two LocalDates to be used as test cases
    LocalDate lower = LocalDate.parse("2021-02-02");
    LocalDate upper = LocalDate.parse("2022-02-06");

    /*
     * Sets up the test fixture before each test method.
     */
    @BeforeEach
    public void setUp() {
        dataHandler = new DataHandlerSingleton();
    }
    
    @AfterEach
    public void tearDown() {
       dataHandler = new DataHandlerSingleton();
    }

    /*
     * Test the setLowerDate() method.
     */
    @Test
    public void testSetLowerDate() {
        dataHandler.setLowerDate(lower);
        assertEquals(lower, dataHandler.getLowerDate());
    }

    /*
     * Test the setUpperDate() method.
     */
    @Test
    public void testSetUpperDate() {
        dataHandler.setUpperDate(upper);
        assertEquals(upper, dataHandler.getUpperDate());
    }

    /*
     * Test the getDatesRange() method.
     */
    @Test
    public void testGetDatesRange() {
        List<LocalDate> dates = dataHandler.getDatesRange();
        assertNotNull(dates);
        assertFalse(dates.isEmpty());
    }

    /*
     * Test the filterListOfRecords() method.
     */
    @Test
    public void testFilterListOfRecords() {
        dataHandler.setLowerDate(lower);
        dataHandler.setUpperDate(upper);
        dataHandler.filterListOfRecords();
        List<LocalDate> filteredDates = dataHandler.getFilteredDatesRange();
        assertNotNull(filteredDates);
        assertFalse(filteredDates.isEmpty());
    }

    /*
     * Test the getFilteredDatesRange() method.
     */
    @Test
    public void testGetFilteredDatesRange() {
        dataHandler.setLowerDate(lower);
        dataHandler.setUpperDate(upper);
        dataHandler.filterListOfRecords();
        List<LocalDate> filteredDates = dataHandler.getFilteredDatesRange();
        assertNotNull(filteredDates);
        assertFalse(filteredDates.isEmpty());
        for (LocalDate date : filteredDates) {
            assertFalse(date.isBefore(dataHandler.getLowerDate()));
            assertFalse(date.isAfter(dataHandler.getUpperDate()));
        }
    }

    /*
     * Test the getBoroughData() method.
     */
    @Test
    public void testGetBoroughData() {
        dataHandler.setLowerDate(lower);
        dataHandler.setUpperDate(upper);
        dataHandler.filterListOfRecords();
        List<CovidData> boroughData = dataHandler.getBoroughData("Hillingdon");
        assertNotNull(boroughData);
        assertFalse(boroughData.isEmpty());
        for (CovidData data : boroughData) {
            assertEquals("Hillingdon", data.getBorough());
        }
    }

    /*
     * Test the getTotalDeaths() method.
     */
    @Test
    public void testGetTotalDeaths() {
        dataHandler.setLowerDate(lower);
        dataHandler.setUpperDate(upper);
        dataHandler.filterListOfRecords();
        int totalDeaths = dataHandler.getTotalDeaths(upper);
        assertTrue(totalDeaths >= 0);
    }

    /*
     * Test the getAverageCases() method.
     */
    @Test
    public void testGetAverageCases() {
        dataHandler.setLowerDate(lower);
        dataHandler.setUpperDate(upper);
        dataHandler.filterListOfRecords();
        double averageCases = dataHandler.getAverageCases(upper);
        assertTrue(averageCases >= 0);
    }

    /*
     * Test the getAverageWorkplacesGMR() method.
     */
    @Test
    public void testGetAverageWorkplacesGMR() {
        assertThrows(NullPointerException.class, () -> {
        double averageWorkplacesGMR = dataHandler.getAverageWorkplacesGMR(lower, upper);
    });
    }

    /*
     * Test the getAverageGroceryPharmacyGMR() method.
     */
    @Test
    public void testGetAverageGroceryPharmacyGMR() {
        assertThrows(NullPointerException.class, () -> {
        double averageGroceryPharmacyGMR = dataHandler.getAverageGroceryPharmacyGMR(lower, upper);
        assertNotNull(averageGroceryPharmacyGMR);
    });
    }

    /*
     * Test the getBoroughDeaths() method.
     */
    @Test
    public void testGetBoroughDeaths() {
        dataHandler.setLowerDate(lower);
        dataHandler.setUpperDate(upper);
        dataHandler.filterListOfRecords();
        int deaths = dataHandler.getBoroughDeaths("Hillingdon");
        assertTrue(deaths >= 0);
    }
    
    
}
