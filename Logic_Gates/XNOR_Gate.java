package Logic_Gates;

import Miscellaneous.Constants;
import Miscellaneous.Function_Storage;
import javafx.scene.image.Image;

/**
 * A logic gate which returns the opposite of what XNOR_Gate returns.
 */
public class XNOR_Gate extends XOR_Gate
{
    /**
     * generated serial version ID
     */
    private static final long serialVersionUID = -9007704752400771450L;

    //constructors
    /**
     * Main constructor of XNOR_Gate.
     * @param id that this Logic_Gate is set to
     * @param x_coordinate of Logic_Gate's top left corner
     * @param y_coordinate of Logic_Gate's top left corner
     */
    public XNOR_Gate(int id, double x_coordinate, double y_coordinate)
    {
     super(id, Constants.XNOR, x_coordinate, y_coordinate, new Image(Function_Storage.get_Resource_Dir(Constants.LOGIC_GATE_FALSE_LOCATIONS[Constants.XNOR])));
    }

    @Override
    public void gateOutput()
    {
     super.gateOutput();
     this.output.set(!output.get());
    }
}
