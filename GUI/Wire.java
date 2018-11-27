package GUI;

import java.util.HashMap;
import java.util.LinkedList;
import Logic_Gates.Logic_Gate;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
<b>
Purpose: Wires of Logic Circuit<br />
Programmer: Gabriel Toban Harris <br />
Date: 2018-5-21/2018-5-22/2018-5-29/2018-5-31/2018-6-29/2018-6-30
</b>
*/

//Concepts for Wire are from elsewhere, though code has been heavily modified to fit this project and general improvements.
public class Wire extends Line
{
    /**
     * Container for wires and maps a given id to LinkedList of wires represented by that id.
     */
    private static HashMap<String, LinkedList<Wire>> wire_container = new HashMap<String, LinkedList<Wire>>();

    /**
     * Wire object representing a gate connection visually
     *
     * @param from 1st gate selected, gate that wire is starting from
     * @param to 2nd gate selected, gate that wire is ending at
     */
    private Wire(Logic_Gate from, Logic_Gate to)
    {
        /**
         * helper class for
         */
        final class Location
        {
            /**
             * Coordinate values
             */
            public ReadOnlyDoubleWrapper x_coordinate = new ReadOnlyDoubleWrapper(), y_coordinate = new ReadOnlyDoubleWrapper();

            /**
             * Constructor of class
             *
             * @param source true for from (starting), false for to (ending)
             * @param node that location is being acted on
             */
            public Location(boolean source, Node node)
            {
             if (source)
               {
                from_center(node.getBoundsInParent());
                node.boundsInParentProperty().addListener(new ChangeListener<Bounds>()
                {
                 public void changed(ObservableValue<? extends Bounds> observableValue, Bounds oldBounds,Bounds bounds)
                 {
                  from_center(bounds);
                 }
                });
               }
            else
                {
                 to_center(node.getBoundsInParent());
                 node.boundsInParentProperty().addListener(new ChangeListener<Bounds>()
                 {
                  public void changed(ObservableValue<? extends Bounds> observableValue, Bounds oldBounds,Bounds bounds)
                  {
                   to_center(bounds);
                  }
                 });
                }
            }

            /**
             * Function for from to calculate center.
             * @param bounds of parent node being acted on
             */
            private void from_center(Bounds bounds)
            {
             x_coordinate.set(bounds.getMinX() + bounds.getWidth());
             y_coordinate.set(bounds.getMinY() + bounds.getHeight() / 2 - 2);//Magic number in an attempt to somewhat align things visually.
            }

            /**
             * Function for to to calculate center.
             * @param bounds of parent node being acted on
             */
            private void to_center(Bounds bounds)
            {
             x_coordinate.set(bounds.getMinX());
             y_coordinate.set(bounds.getMinY() + bounds.getHeight() / 2);
            }
        }

     Location from_location = new Location(true, from), to_location = new Location(false, to);

     startXProperty().bind(from_location.x_coordinate);
     startYProperty().bind(from_location.y_coordinate);
     endXProperty().bind(to_location.x_coordinate);
     endYProperty().bind(to_location.y_coordinate);
     setMouseTransparent(true);

     colour_wire(from.output.get());

     //Change colour dynamically based on from's output.
     from.output.addListener(new ChangeListener<Boolean>()
     {
      public void changed(ObservableValue<? extends Boolean> observable, Boolean old_value, Boolean new_value)
      {
       colour_wire(new_value);
      }
     });
    }

    /**
     * Way in which keys are made for wire_container.
     *
     * @param from 1st gate selected, gate that wire is starting from
     * @param to 2nd gate selected, gate that wire is ending at
     * @return String represent the id formed
     */
    private static String generate_id(int from_id, int to_id)
    {
     return "{" + from_id + ", " + to_id + "}";
    }

    /**
     * Way in which keys are made for wire_container.
     *
     * @param from 1st gate selected, gate that wire is starting from
     * @param to 2nd gate selected, gate that wire is ending at
     * @return String represent the id formed
     */
    private static String generate_id(Logic_Gate from, Logic_Gate to)
    {
     return "{" + from.get_ID() + ", " + to.get_ID()  + "}";
    }

    /**
     * For Starting_Point_GUI to clear stuff during loading of a file. This function only clears the local container of Wires.
     */
    public static void clear_wire_container()
    {
     wire_container.clear();
    }

    /**
     * Function to alter colour of wire based on from gate's output.
     *
     * @param from_output boolena representing value of from gate's output
     */
    private void colour_wire(boolean from_output)
    {
     this.setStroke((from_output) ? Color.GREEN : Color.RED);
    }

    /**
     * Function to remove wires, function to be called for Remove_button. Removes all wires to other gates.
     *
     * @param gate logic gate being removed
     */
    public static void remove_all_gate_wires(Logic_Gate gate)
    {
     String generated_id_place_holder;
     HashMap<String, Boolean> already_checked_ids = new HashMap<String, Boolean>();//avoid repeating paths

     //remove from gate
     for (int i = 0; i < gate.out_gates.size(); i++)
        {
         generated_id_place_holder = generate_id(gate.get_ID(), gate.out_gates.get(i));

         if (already_checked_ids.containsKey(generated_id_place_holder))
            continue;//already dealt with
         already_checked_ids.put(generated_id_place_holder, null);
         Starting_Point_GUI.get_animation_pane_content_getChildren().removeAll(wire_container.get(generated_id_place_holder));
         wire_container.remove(generated_id_place_holder);
        }
     already_checked_ids.clear();
     //remove to gate
     for (int i = 0; i < gate.in_gates.size(); i++)
        {
         generated_id_place_holder = generate_id(gate.in_gates.get(i), gate.get_ID());

         if (already_checked_ids.containsKey(generated_id_place_holder))
            continue;//already dealt with
         already_checked_ids.put(generated_id_place_holder, null);
         Starting_Point_GUI.get_animation_pane_content_getChildren().removeAll(wire_container.get(generated_id_place_holder));
         wire_container.remove(generated_id_place_holder);
        }
    }

    /**
     * Function to connect Logic_Gates with a wire.
     *
     * @param from 1st gate selected, gate that wire is starting from
     * @param to 2nd gate selected, gate that wire is ending at
     */
    @SuppressWarnings("serial")
    public static void connect_gate_wire(Logic_Gate from, Logic_Gate to)
    {
     String key = generate_id(from, to);
     Wire place_holder = new Wire(from, to);

     //already exists
     if (wire_container.containsKey(key))
        wire_container.get(key).add(place_holder);
     //does not already exist
     else
          if (null != wire_container.putIfAbsent(key, new LinkedList<Wire>() {{add(place_holder);}}))
             System.err.println("Error: this message should never appear. Mapping already existed in wire_container.");

     Starting_Point_GUI.get_animation_pane_content_getChildren().add(place_holder);
    }

    /**
     * Functino to disconnect (remove one wire) Logic_Gates from each other.
     *
     * @param from 1st gate selected, gate that wire is starting from
     * @param to 2nd gate selected, gate that wire is ending at
     */
    public static void disconnect_gate_wire(Logic_Gate from, Logic_Gate to)
    {
     Starting_Point_GUI.get_animation_pane_content_getChildren().remove(wire_container.get(generate_id(from, to)).remove());
    }
}
