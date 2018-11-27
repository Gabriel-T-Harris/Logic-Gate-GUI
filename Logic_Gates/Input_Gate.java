package Logic_Gates;

import Miscellaneous.Constants;
import Miscellaneous.Function_Storage;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/*
Purpose: Input logic gate
Programmer: Gabriel Toban Harris
Date: 2017-12-30/2018-5-5/2018-6-27/2018-6-28/2018-6-30
*/

public class Input_Gate extends Logic_Gate
{
    /**
     * generated serial version ID
     */
    private static final long serialVersionUID = -1929009781975464937L;

    //constructors
    /**
     * Main constructor of Input_Gate
     * @param id that this Logic_Gate is set to
     * @param x_coordinate of Logic_Gate's top left corner
     * @param y_coordinate of Logic_Gate's top left corner
     */
    public Input_Gate(int id, double x_coordinate, double y_coordinate)
    {
     super(id, Constants.INPUT, x_coordinate, y_coordinate, new Image(Function_Storage.get_Resource_Dir(Constants.LOGIC_GATE_FALSE_LOCATIONS[Constants.INPUT])));
     this.setOnMouseClicked(this);
    }

    @Override
    public void gateOutput(){}//Do nothing due to the Input_Gate being special.

    public void handle(MouseEvent e)
    {
     if (e.getEventType() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseButton.SECONDARY)
       {
        //toggle input gate
        this.output.set(!this.output.get());
       }
     super.handle(e);
    }
}
