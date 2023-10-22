/**
 * The way code in the GraphPanelController works is that is traverses down the component hierachy to find the required compoents e.g. the LineChart
 * & The menuButton. If the layout changes the controller will not be able to find these components. As a result these exceptions are used to alert future developers
 * of the fact that the controller can no longer find these required component. 
 */
public class incorrectTabLayoutException extends IllegalArgumentException{
    incorrectTabLayoutException(){}
    public incorrectTabLayoutException(String errorMessage){
        super();
    }   
}
