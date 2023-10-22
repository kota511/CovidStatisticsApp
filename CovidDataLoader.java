import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import com.opencsv.CSVReader;
import java.net.URISyntaxException;

/**
 * Loads covid data from the csv file
 *
 */
public class CovidDataLoader {

    /**
     * Return an ArrayList containing the rows in the Covid London data set csv
     * file.
     */
    public ArrayList<CovidData> load() {
        System.out.println("Begin loading Covid London dataset...");
        ArrayList<CovidData> records = new ArrayList<CovidData>();
        try {
            URL url = getClass().getResource("covid_london.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String[] line;
            // skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {

                String date = line[0];
                String borough = line[1];
                Integer retailRecreationGMR = convertInt(line[2]);
                Integer groceryPharmacyGMR = convertInt(line[3]);
                Integer parksGMR = convertInt(line[4]);
                Integer transitGMR = convertInt(line[5]);
                Integer workplacesGMR = convertInt(line[6]);
                Integer residentialGMR = convertInt(line[7]);
                Integer newCases = convertInt(line[8]);
                Integer totalCases = convertInt(line[9]);
                Integer newDeaths = convertInt(line[10]);
                Integer totalDeaths = convertInt(line[11]);

                CovidData record = new CovidData(date, borough, retailRecreationGMR,
                        groceryPharmacyGMR, parksGMR, transitGMR, workplacesGMR,
                        residentialGMR, newCases, totalCases, newDeaths, totalDeaths);
                records.add(record);
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("Something Went Wrong?!");
            e.printStackTrace();
        }
        System.out.println("Number of Loaded Records: " + records.size());
        return records;
    }

    /**
     * @param intString the string to be converted to Integer type
     * @return the Integer value of the string, or null if the string is
     *         either empty or just whitespace.
     */
    private Integer convertInt(String intString) {
        if (intString != null && !intString.trim().equals("")) {
            return Integer.parseInt(intString);
        }
        return null;
    }

}
