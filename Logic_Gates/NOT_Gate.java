package Logic_Gates;

import Logic_Circuit.Circuit;
import Miscellaneous.Constants;
import Miscellaneous.Function_Storage;
import javafx.scene.image.Image;

/**
 * A logic gate which returns the opposite value of its only input (maximum 1).
 */
public class NOT_Gate extends Logic_Gate
{
    /**
     * generated serial version ID
     */
    private static final long serialVersionUID = -3974928202088232018L;

    //constructors
    /**
     * Main constructor of NOT_Gate.
     * @param id that this Logic_Gate is set to
     * @param x_coordinate of Logic_Gate's top left corner
     * @param y_coordinate of Logic_Gate's top left corner
     */
    public NOT_Gate(int id, double x_coordinate, double y_coordinate)
    {
     super(id, Constants.NOT, x_coordinate, y_coordinate, new Image(Function_Storage.get_Resource_Dir(Constants.LOGIC_GATE_FALSE_LOCATIONS[Constants.NOT])));
    }

    @Override
    public void gateOutput()
    {
     if (this.in_gates.size() > 0)
        this.output.set(!Circuit.logic_gate_container.get(this.in_gates.get(0)).output.get());
     else
          this.output.set(true);
    }
}
