package Logic_Gates;

import GUI.Starting_Point_GUI;
import Logic_Circuit.Circuit;
import Miscellaneous.Constants;
import Miscellaneous.Function_Storage;
import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;

/**
Purpose: Parent class for logic gates
Programmer: Gabriel Toban Harris
Date: 2017-10-6/2018-5-3/2018-5-4(added tooltips to child gate classes and other things for this class)/2018-5-5(output stuff for children and reworked MouseEvents)/2018-5-6(changed output into a property)
/2018-5-7 (reworked Tooltips to work with HTML)/2018-5-8(reworked visibility of a few data members that involve the reworked gateOutput())/2018-5-12/2018-5-15(changed output to be private and made changes accordingly)/
2018-5-19 (changed gate_image_index to LOGIC_GATE_TYPE and added getter for it)/2018-5-20/2018-5-21/2018-6-6/2018-6-27/2018-6-28/2018-6-29/2018-6-30
*/

//Note any change to this class that has a domino effect on the children is mentioned in this class regarding dating (excluding stuff that is inherited, think stuff that causes reworks).
//Serialization could probably be handled better.
public abstract class Logic_Gate extends Rectangle implements EventHandler<MouseEvent>, Serializable
{
    /**
     * Boolean value for find_all_dirty_gates(LinkedList<Integer> input) is for optimized depth-first search to avoid retracing paths.
     */
    public transient boolean has_been_visted = false;
    /**
     * Boolean value representing SimpleBooleanProperty output value. For serialization as SimpleBooleanProperty is not serializable.
     */
    private boolean serializable_output_value = false;
    /**
     * The most recent output of the logic gate.
     */
    public transient SimpleBooleanProperty output;
    /**
     * Identification number of this particular logic gate.
     */
    private final int ID;
    /**
     * Link logic gate with index in Constants's arrays for image locations.
     */
    private final int LOGIC_GATE_TYPE;
    /**
     * For gate connecting and determines which 'row' in Circuit.id_universal_set_container.
     */
    public int rank = 0;
    /**
     * generated serial version ID
     */
    private static final long serialVersionUID = 4308357806735974927L;
    /**
     * Used internally only for dragging gates. Lines up mouse with position being clicked on gate.
     */
    private double delta_x, delta_y;
    /**
     * Used for restoring gate to correct position after deserialization; also as a byproduct of existing, for dragging.
     */
    private double x_coordinate, y_coordinate;
    /**
     * List of gates that are inputting into this gate. Note list is of said gates' ids.
     */
    public LinkedList<Integer> in_gates = new LinkedList<Integer>();
    /**
     * List of gates that are outputting from this gate. Note list is of said gates' ids.
     */
    public LinkedList<Integer> out_gates = new LinkedList<Integer>();

    //constructors
    /**
     * Logic_Gate constructor with Tooltip.
     * @param id of the logic gate
     * @param gate_type reffers to which gate is being created. Number should be chosen from Miscellaneous.Constants.
                        Number references which Tooltip location should be chosen from Miscellaneous.Constants.LOGIC_GATE_TOOLTIP_LOCATIONS.
     * @param x_coordinate of Logic_Gate's top left corner
     * @param y_coordinate of Logic_Gate's top left corner
     * @param icon image to be used for gate
     */
    public Logic_Gate(int id, int gate_type, double x_coordinate, double y_coordinate, Image icon)
    {
     super(icon.getWidth(), icon.getHeight(), new ImagePattern(icon));
     this.ID = id;
     this.LOGIC_GATE_TYPE = gate_type;
     this.setX(this.x_coordinate = x_coordinate);
     this.setY(this.y_coordinate = y_coordinate);

     {
      WebView step_1 = new WebView();
      Tooltip step_2 = new Tooltip();

      step_1.getEngine().load(new File(Function_Storage.get_Resource_Dir(Constants.LOGIC_GATE_TOOLTIP_LOCATIONS[gate_type])).toURI().toString());
      step_1.setPrefSize(500, 275);//Should probably find better way of dealing with Tooltip size. Oh well, good enough.
      step_2.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
      step_2.setGraphic(step_1);
      Tooltip.install(this, step_2);
     }

     //action listers
     this.setOnMousePressed(this);
     this.setOnMouseDragged(this);
     this.setOnMouseReleased(this);
     this.setOnMouseEntered(this);
     this.setOnMouseExited(this);

     set_up_output();//call last
    }

    //getters
    /**
     * Getter for private field id.
     */
    public int get_ID()
    {
     return this.ID;
    }
    /**
     * Getter for protected field get_logic_gate_type.
     */
    public int get_logic_gate_type()
    {
     return this.LOGIC_GATE_TYPE;
    }

    /**
     * toString of this class
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
     return "Logic_Gate [serializable_output_value = " + this.serializable_output_value + ", ID = " + this.ID + ", LOGIC_GATE_TYPE = " + this.LOGIC_GATE_TYPE + ", rank = " + this.rank +
            ", x_coordinate = " + this.x_coordinate + ", y_coordinate = " + this.y_coordinate + ", in_gates = " + this.in_gates + ", out_gates = " + this.out_gates + "]";
    }

    /**
     * Meant to be called after deserialization to ensure that the gate is properly deserialized.
     */
    public void call_after_deserialization()
    {
     set_up_output();
     this.setX(this.x_coordinate);
     this.setY(this.y_coordinate);
    }

    /**
     * Set up SimpleBooleanProperty output. Is meant to be called during constructors and after deserialization. Calling at other times should have no meaningful effect.
     */
    private void set_up_output()
    {
     output = new SimpleBooleanProperty(this.serializable_output_value);

     //Change image dynamically based on output.
     output.addListener(new ChangeListener<Boolean>()
     {
      public void changed(ObservableValue<? extends Boolean> observable, Boolean old_value, Boolean new_value)
      {
       setFill(new ImagePattern(new Image(Function_Storage.get_Resource_Dir((new_value) ? Constants.LOGIC_GATE_TRUE_LOCATIONS[LOGIC_GATE_TYPE] :
                                                                                          Constants.LOGIC_GATE_FALSE_LOCATIONS[LOGIC_GATE_TYPE]))));
       serializable_output_value = new_value;
      }
     });
    }

    /**
     * Runs the gate, should set the output value based on the overriding gate's functionality.
     */
    public abstract void gateOutput();

    /**
     * EventHandler of this class.
     */
    public void handle(MouseEvent e)
    {
     EventType<?> type = e.getEventType();

     //Update position values, won't work if user randomly changes window (like with meta key + tab key).
     if (type == MouseEvent.MOUSE_RELEASED)
       {
        this.getScene().setCursor(Cursor.HAND);
        Starting_Point_GUI.animation_pane_is_pannable(true);
       }
     else if (type == MouseEvent.MOUSE_ENTERED)
          this.getScene().setCursor(Cursor.HAND);
     else if (type == MouseEvent.MOUSE_EXITED)
          this.getScene().setCursor(Cursor.DEFAULT);
     else if (type == MouseEvent.MOUSE_PRESSED)
         {
          //for dragging
          this.delta_x = this.getX() - e.getX();
          this.delta_y = this.getY() - e.getY();
          this.getScene().setCursor(Cursor.MOVE);
          Starting_Point_GUI.animation_pane_is_pannable(false);

          //for logic gate buttons
          if (Starting_Point_GUI.selected_togglebutton.equals("_Connect_button"))
             Circuit.connect_button_functionality(this.ID);
          else if (Starting_Point_GUI.selected_togglebutton.equals("_Disconnect_button"))
               Circuit.disconnect_button_functionality(this.ID);
          else if (Starting_Point_GUI.selected_togglebutton.equals("_Remove_button"))
               Circuit.remove_button_functionality(this.ID);
          System.out.println(this);//useful for debugging
         }
     else if (type == MouseEvent.MOUSE_DRAGGED)
         {
          double x_place_holder, y_place_holder;
          if ((x_place_holder = e.getX() + delta_x) > 0)
             this.setX(this.x_coordinate = x_place_holder);
          if ((y_place_holder = e.getY() + delta_y) > 0)
             this.setY(this.y_coordinate = y_place_holder);
         }
    }
}
