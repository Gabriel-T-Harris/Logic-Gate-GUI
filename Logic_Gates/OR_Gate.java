package Logic_Gates;

import Logic_Circuit.Circuit;
import Miscellaneous.Constants;
import Miscellaneous.Function_Storage;
import javafx.scene.image.Image;

/**
 * A logic gate which returns true if at least one of its inputs (minimum 2) is true, otherwise it returns false.
 */
public class OR_Gate extends Logic_Gate
{
    /**
     * generated serial version ID
     */
    private static final long serialVersionUID = -804131962225474309L;

    //constructors
    /**
     * Main constructor of OR_Gate.
     * @param id that this Logic_Gate is set to
     * @param x_coordinate of Logic_Gate's top left corner
     * @param y_coordinate of Logic_Gate's top left corner
     */
    public OR_Gate(int id, double x_coordinate, double y_coordinate)
    {
     super(id, Constants.OR, x_coordinate, y_coordinate, new Image(Function_Storage.get_Resource_Dir(Constants.LOGIC_GATE_FALSE_LOCATIONS[Constants.OR])));
    }
    /**
     * Constructor for NOR_Gate.
     * @param id of the logic gate
     * @param gate_type reffers to which gate is being created. Number should be chosen from Miscellaneous.Constants.
                        Number references which Tooltip location should be chosen from Miscellaneous.Constants.LOGIC_GATE_TOOLTIP_LOCATIONS.
     * @param x_coordinate of Logic_Gate's top left corner
     * @param y_coordinate of Logic_Gate's top left corner
     * @param icon image to be used for gate
     */
    protected OR_Gate(int id, int gate_type, double x_coordinate, double y_coordinate, Image icon)
    {
     super(id, gate_type, x_coordinate, y_coordinate, icon);
    }

    @Override
    public void gateOutput()
    {
     if (this.in_gates.size() > 1)
        for (int i = 0; i < this.in_gates.size(); i++)
            // Short-circuit evaluation.
            if (Circuit.logic_gate_container.get(this.in_gates.get(i)).output.get())
              {
               this.output.set(true);
               return;
              }
     this.output.set(false);
    }
}
