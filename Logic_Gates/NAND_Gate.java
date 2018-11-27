package Logic_Gates;

import Miscellaneous.Constants;
import Miscellaneous.Function_Storage;
import javafx.scene.image.Image;

/**
 * A logic gate which returns the opposite of what NAND_Gate returns.
 */
public class NAND_Gate extends AND_Gate
{
    /**
     * generated serial version ID
     */
    private static final long serialVersionUID = -2492218560965972559L;

    //constructors
    /**
     * Main constructor of NAND_Gate.
     * @param id that this Logic_Gate is set to.
     * @param x_coordinate of Logic_Gate's top left corner
     * @param y_coordinate of Logic_Gate's top left corner
     */
    public NAND_Gate(int id, double x_coordinate, double y_coordinate)
    {
     super(id, Constants.NAND, x_coordinate, y_coordinate, new Image(Function_Storage.get_Resource_Dir(Constants.LOGIC_GATE_FALSE_LOCATIONS[Constants.NAND])));
    }

    @Override
    public void gateOutput()
    {
     super.gateOutput();
     this.output.set(!this.output.get());
    }
}
