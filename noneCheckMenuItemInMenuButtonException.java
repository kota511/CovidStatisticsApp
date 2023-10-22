/**
 * An exception used to ensure that only check menu items are included in certain menu Buttons' menu
 * @version 28/03/2023
 */
public class noneCheckMenuItemInMenuButtonException extends IllegalArgumentException{
    noneCheckMenuItemInMenuButtonException(){}
    public noneCheckMenuItemInMenuButtonException(String errorMessage){
        super();
    }   
}
