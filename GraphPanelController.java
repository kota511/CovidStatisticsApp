import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Tab;

/**
 * Graph Panel Controller
 * The idea of the Panel is to present the data between the start and ends dates in a line chart
 * Where the data being shown in the line chart is determined by the tab and the boroughs selected. 
 * Boroughs can easily be compared by adding multiple boroughs data to a single graph.
 * 
 * @version 27/03/2023
 */

public class GraphPanelController {

    @FXML
    private TabPane tabPane;
    
    private LineChart<String,Number> currentLineChart;

    private final DataHandlerSingleton dataHandler = DataHandlerSingleton.getInstance();

    private Tab currentTab;

    /**
     * Called once the program first starts running. This method aims to add event listeners to all tabs, so that when they are clicked/entered
     * The current lineChart is set to the one that is now viewable.
     * Also defines the current tab and current line chart to be the first one to ensure it is always defined
     */
    @FXML
    public void initialize(){
        currentTab = tabPane.getTabs().get(0);
        currentLineChart = getCurrentTabLineChart(currentTab);       
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            // This code now executes whenever a tab is changed
            currentTab  = tabPane.getSelectionModel().getSelectedItem();
            currentLineChart = getCurrentTabLineChart(newTab);
        });
    }

    /**
     * Called when a check menu item is check or unchecked, should recreate the graph to represent the data for all checked boroughs.
     * This is done by first updating a the listOfCheckedBoroughNames, then using that list we re-create and then redisplay the graph using this
     * data to the current tab.
     * 
     * @param event A check menu item is checked or unchecked 
     */
    @FXML
    void boroughSelected(ActionEvent event) throws noneCheckMenuItemInMenuButtonException{
        if (event.getSource() instanceof CheckMenuItem){ //Checks that item giving the signal is a CheckMenuItem to avoid a conversion error later
            CheckMenuItem boroughCheckMenuItem = ((CheckMenuItem) event.getSource());
            String boroughName = boroughCheckMenuItem.getText();
            if (boroughCheckMenuItem.isSelected()){
                addLineToGraph(boroughName);
            } else {
                removeLineFromGraph(boroughName);
            }
        } else {
            throw new noneCheckMenuItemInMenuButtonException("The item selected was not a CheckMenuItem, only CheckMenuItems are allowed in the Graph Pane borough Menu Boxes. Please remove all none CheckMenuItems");
        }

    }

    /**
     * Adds a line of the approiate data (determined by what tab the user is in) to the chart for the borough that got selected
     * @param boroughName The name of the borough selected
     */
    private void addLineToGraph(String boroughName) throws incorrectTabLayoutException{
        List<CovidData> boroughData = dataHandler.getBoroughData(boroughName);
        Collections.reverse(boroughData); //Orders the data so that oldest dates e.g. 2021 appears first (left) of newer ones e.g. 2023
        final XYChart.Series<String, Number> lineDataToAdd = new XYChart.Series<>();
        lineDataToAdd.setName(boroughName);
        Integer yAxisData = null;
        for (CovidData datum : boroughData){
            switch (currentTab.getText()){
                case "New Deaths": 
                    yAxisData = datum.getNewDeaths();
                    break;
                case "Total Deaths": 
                    yAxisData = datum.getTotalDeaths();
                    break;
                case "New Cases": 
                    yAxisData = datum.getNewCases();
                    break;
                case "Total Cases": 
                    yAxisData = datum.getTotalCases();
                    break;
                default:
                    throw new incorrectTabLayoutException("This Tab does not have a supported case to handle the getting of the data it wishes. This must be resolved");
            }
            lineDataToAdd.getData().add(new XYChart.Data<>(datum.getDate(), yAxisData));
        }
        currentLineChart.getData().add(lineDataToAdd);
}

    /**
     * Removes the line containing data from the unselected borough
     * @param boroughName
     */
    private void removeLineFromGraph(String boroughName){
        ObservableList<XYChart.Series<String, Number>> listOfLinesInGraph = currentLineChart.getData();
        // for (XYChart.Series<String, Number> line : listOfLinesInGraph){
        int currentIndex = 0;
        Boolean lineRemoved = false;
        while (currentIndex < listOfLinesInGraph.size() && !lineRemoved){
            if (listOfLinesInGraph.get(currentIndex).getName() == boroughName){
                currentLineChart.getData().remove(listOfLinesInGraph.get(currentIndex));
                lineRemoved = true;
            }
            currentIndex++;
        }
    }

    /**
     * Gets the line chart stored in a certain tab
     * @param tab the tab to look for the lineChart in
     * @return Returns the Line chart in the tab, or it creates an error to ensure future developers don't alter the layout and without noticing break the code, which relies on a specific layout to work
     */
    private LineChart<String,Number> getCurrentTabLineChart(Tab tab){
        String startOfIncorrectTabLayoutMessage = "The layout of this tab (" + tab.getText() + ") is wrong, ";
        if (tab.getContent() instanceof AnchorPane){
            AnchorPane tabAnchorPane = (AnchorPane) tab.getContent();
            if (tabAnchorPane.getChildren().get(0) instanceof BorderPane){
                BorderPane anchorsBorderPane = (BorderPane) tabAnchorPane.getChildren().get(0);
                if (anchorsBorderPane.getCenter() instanceof LineChart){
                    LineChart<String,Number> returnLineChart = (LineChart<String,Number>) anchorsBorderPane.getCenter();
                    return returnLineChart;
                } else {
                    throw new incorrectTabLayoutException(startOfIncorrectTabLayoutMessage + "The borderpane must contain a line chart in its center, please resolve this");
                }
            } else {
                throw new incorrectTabLayoutException(startOfIncorrectTabLayoutMessage + "The anchor pane must contain a BorderPane as it's first child component, please resolve this");
            }
        } else {
            throw new incorrectTabLayoutException(startOfIncorrectTabLayoutMessage + "The Tab must contain an anchor pane as it's sole Component (This anchor pane may contain more components), please resolve this");
        }
    }

    /**
     * Gets the menuButton in a specific tab
     * @param tab the tab to search for the menu button in
     * @return returns the menu button contaning all the boroughs in (for the specified tab)
     */
    private MenuButton getTabMenuButton(Tab tab){
        String startOfIncorrectTabLayoutMessage = "The layout of this tab (" + tab.getText() + ") is wrong, ";
        if (tab.getContent() instanceof AnchorPane){
            AnchorPane tabAnchorPane = (AnchorPane) tab.getContent();
            if (tabAnchorPane.getChildren().get(0) instanceof BorderPane){
                BorderPane anchorsBorderPane = (BorderPane) tabAnchorPane.getChildren().get(0);
                if (anchorsBorderPane.getBottom() instanceof HBox){
                    HBox borderBottomHbox = (HBox) anchorsBorderPane.getBottom();
                    if (borderBottomHbox.getChildren().get(0) instanceof MenuButton){
                        return (MenuButton) borderBottomHbox.getChildren().get(0);
                    } else {
                        throw new incorrectTabLayoutException(startOfIncorrectTabLayoutMessage + "The Hbox must contain a menu Button stored in its chid position one, please resolve this");
                    }
                } else {
                    throw new incorrectTabLayoutException(startOfIncorrectTabLayoutMessage + "The borderpane must contain a hBox in its bottom pane, please resolve this");
                }
            } else {
                throw new incorrectTabLayoutException(startOfIncorrectTabLayoutMessage + "The anchor pane must contain a BorderPane as it's first child component, please resolve this");
            }
        } else {
            throw new incorrectTabLayoutException(startOfIncorrectTabLayoutMessage + "The Tab must contain an anchor pane as it's sole Component (This anchor pane may contain more components), please resolve this");
        }
    }

    /**
     * This method clears all the graphs, so that they don't have any lines on them
     * This is supposed to be used when the dates are changed
     */
    @FXML
    public void clear(){
        LineChart<String, Number> startLineChart = currentLineChart;
        for (Tab tab : tabPane.getTabs()){
            MenuButton currentMenuButton = getTabMenuButton(tab);
            for (MenuItem menuItem : currentMenuButton.getItems()){
                if (menuItem instanceof CheckMenuItem){
                    CheckMenuItem checkMenuItem = (CheckMenuItem) menuItem;
                    if (checkMenuItem.isSelected()){
                        checkMenuItem.setSelected(false);
                    }
                } else {
                    throw new noneCheckMenuItemInMenuButtonException("The item selected was not a CheckMenuItem, only CheckMenuItems are allowed in the Graph Pane borough Menu Boxes. Please remove all none CheckMenuItems");
                }
            }
            currentLineChart = getCurrentTabLineChart(tab);
            currentLineChart.getData().clear();
        }
        currentLineChart = startLineChart;
    }
}