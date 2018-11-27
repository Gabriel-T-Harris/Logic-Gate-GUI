package Logic_Circuit;

import GUI.Starting_Point_GUI;
import GUI.Wire;
import Logic_Gates.Logic_Gate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;
import javafx.scene.control.Alert;
import static Miscellaneous.Function_Storage.popup_message;

/**
Purpose: Logic for how the circuit works.
Programmer: Gabriel Toban Harris
Date: 2017-12-31/2018-5-7/2018-5-8/2018-5-12/2018-5-13/2018-5-19/2018-5-20/2018-5-21/2018-5-22/2018-6-6/2018-6-27
*/

public class Circuit
{
    /**
     * Used for the connecting and disconnecting buttons. 1st string assigned a value, thus  is 1st selected gate.
     */
    private static int first_selected_gate = -1;
    /**
     * Used for the connecting and disconnecting buttons. 2nd string assigned a value, thus  is 2nd selected gate.
     */
    private static int second_selected_gate = -1;
    /**
     * Contains all logic gates ids that are used in application. Is sorted by row.
     */
    @SuppressWarnings("serial")//I don't want to serialize it.
    public static LinkedList<LinkedList<Integer>> id_universal_set_container = new LinkedList<LinkedList<Integer>>(){{add(new LinkedList<Integer>());}};
    /**
     * Is to keep track of gates that require rank updating.
     */
    private static TreeSet<Logic_Gate> dirty_gate_container = new TreeSet<Logic_Gate>(
            new Comparator<Logic_Gate>()
            {
             public int compare(Logic_Gate argument_0, Logic_Gate argument_1)
             {
              if (argument_0.rank != argument_1.rank)//To allow gates with the same rank in.
                 return argument_0.rank - argument_1.rank;//sort by rank
              else
                   return argument_0.get_ID() - argument_1.get_ID();//Tie breaker; using id as the order of gates with the same rank doesn't matter.
             }
            });
    /**
     * Maps gate ids to corresponding gates.
     */
    public static HashMap<Integer, Logic_Gate> logic_gate_container = new HashMap<Integer, Logic_Gate>();

    /**
     * Method to be used to run circuit. Runs each individual gate to end up running the whole Circuit.
     * Order determined by id_universal_set_container. That is by row, then each element in that row, then moving on to next row.
     */
    public static void evaluate_circuit()
    {
     if (!dirty_gate_container.isEmpty())
        find_clean_all_gates();//Find all dirty gates for gate cleaning, then clean all gates for rank check.

     for (int i = 0; i < id_universal_set_container.size(); i++)
         for (int k = 0; k < id_universal_set_container.get(i).size(); k++)
             logic_gate_container.get(id_universal_set_container.get(i).get(k)).gateOutput();
    }

    /**
     * Starting point of find_all_dirty_gates(LinkedList<Integer> input) that is made to be called generically. clean_all_gates() should be called after this function is called.
     */
    private static void find_all_dirty_gates()
    {
     LinkedList<Integer> place_holder_converter = new LinkedList<Integer>();
     //convert dirty_gate_container into a LinkedList<Integer> containing id.
     for (Logic_Gate logic_gate : dirty_gate_container)
        place_holder_converter.add(logic_gate.get_ID());

     find_all_dirty_gates(place_holder_converter);
    }

    /**
     * Use a depth-first search algorithm to find all dirty gates recursively. Found gates are added to dirty_gate_container.
     * @param input set of gates by gate id to recursivly look at. Normally is from a Logic_Gate.out_gates.
     */
    private static void find_all_dirty_gates(LinkedList<Integer> input)
    {
     if (input.isEmpty())
        return;//base case

     Logic_Gate place_holder;//declared after check for optimization
     for (int i = 0; i < input.size(); i++)
        {
         place_holder = logic_gate_container.get(input.get(i));
         if (!place_holder.has_been_visted)//avoid revisiting nodes
           {
            dirty_gate_container.add(place_holder);
            find_all_dirty_gates(place_holder.out_gates);
            place_holder.has_been_visted = true;
           }
        }
    }

    /**
     * <p>Clean all gates, meaning each gate's id in dirty_gate_container will have calculate_gate_rank(Logic_Gate input) called on them then have transfer_gate(Logic_Gate input) called on it.</p>
     * <br>Note: find_all_dirty_gates() should be called before this method to ensure that all dirty gates (even those not marked as such) have been found and are cleaned.
     */
    private static void clean_all_gates()
    {
     //iterate over dirty_gate_container
     while (!dirty_gate_container.isEmpty())
           transfer_gate(dirty_gate_container.first());

     //check that all gates have been removed.
     if (!dirty_gate_container.isEmpty())
        System.err.println("Error: all gates should be clean, not all gates clean. Circuit evaluation may be affected.");
    }

    /**
     * Method for calling find_all_dirty_gates() then clean_all_gates(), simply. Allows outside calls. Also both functions should be called in tandem.
     */
    public static void find_clean_all_gates()
    {
     if (!dirty_gate_container.isEmpty())
       {
        find_all_dirty_gates();//find all dirty gates for gate cleaning
        clean_all_gates();//clean all gates for rank check.
       }
    }

    /**
     * Updates a single gate's rank based on the actual gate. Is also a subroutine of ransfer_gate(Logic_Gate input).
     * @param input Logic_Gate (actual object) of gate to be updated.
     */
    private static void calculate_gate_rank(Logic_Gate input)
    {
     int max_rank/*Declared here for format reasons, the compiler will move it over with the initialization, thus no lose.*/, rank_place_holder;
     LinkedList<Integer> in_gates_place_holder = input.in_gates;

     //Just in case, though I do not think it will ever be used. Might be used when a gate had it's in_gates removed.
     if (in_gates_place_holder.isEmpty())
       {
        input.rank = 0;
        return;
       }

     //find max rank in list of inputting gates
     max_rank = logic_gate_container.get(in_gates_place_holder.get(0)).rank;
     for (int i = 1; i < in_gates_place_holder.size(); i++)
         if (max_rank < (rank_place_holder = logic_gate_container.get(in_gates_place_holder.get(i)).rank))
            max_rank = rank_place_holder;

     input.rank = max_rank + 1;
    }

    /**
     * <p>For transferring (remove then add) a gate based on rank into id_universal_set_container from dirty_gate_container. Is also a subroutine of clean_all_gates().</p><br>
     * Note: DO NOT call any function that recalculates input's rank before calling this function. Function internally depends on recalculating input's rank on it's own to work properly.
     * @param input Logic_Gate (actual object) of gate to be transfered.
     */
    private static void transfer_gate(Logic_Gate input)
    {
     int old_rank = input.rank/*Know old position before update.*/, input_id = input.get_ID();
     calculate_gate_rank(input);//Update rank, also known as as new position.
     input.has_been_visted = false;

     if (id_universal_set_container.get(old_rank).remove((Object) input_id))//Type cast to avoid confusion with which method to function.
       {
        int new_rank = input.rank;

        //rows have to exist to be added to
        while (new_rank > id_universal_set_container.size() - 1)
              id_universal_set_container.add(new LinkedList<Integer>());

        id_universal_set_container.get(new_rank).add(input_id);
        dirty_gate_container.remove(input);
       }
     else
          System.err.println("Error: gate " + input.get_ID() + " not properly removed.");
    }

    /**
     * <p>Function to connect gates, function to be called for Connect_button. Resets 'memory' of selected gates when 2 gates are connected.
     * Acts with the same logic as disconnect_button_functionality(int input), but adds an association between selected gates if such does not already exist.</p><br>Note 1st
     * selected gate not reset when switching selecting another button. Said value also used by the Disconnect_button Togglebutton.
     *
     * @param input id of selected gate
     */
    public static void connect_button_functionality(int input)
    {
     //If 1st String does not have a meaningful value, assign it one. Should be the gate that is outputting.
     if (first_selected_gate == -1)
        first_selected_gate = input;
     else
         {
          Logic_Gate outputting_gate, inputting_gate;
          //circular gate check
          if (input == first_selected_gate)
            {
             System.err.println("Error: cannot connect gate to itself. Selected gate reset.");
             first_selected_gate = -1;
             popup_message(Alert.AlertType.WARNING, "Circular Gate Attempt", null, "Warning: cannot connect gate to itself.");
             return;
            }

          second_selected_gate = input;//After this line 2 gates have been selected, thus proceed to connecting.

          //Only look for gates once after circular gate check.
          outputting_gate = logic_gate_container.get(first_selected_gate);
          inputting_gate = logic_gate_container.get(second_selected_gate);

          find_clean_all_gates();//Find all dirty gates for gate cleaning, then clean all gates for rank check.

          //rank check
          if (!inputting_gate.out_gates.isEmpty() && outputting_gate.rank > inputting_gate.rank)//The 1st condition is fail fast
            {
             System.err.println("Error: cannot have gate id " + first_selected_gate + " output to gate id " + second_selected_gate + ", as latter gate doesn't have a higher " +
                                "rank then former gate. Selected gate reset.");
             popup_message(Alert.AlertType.ERROR, "Possible Circular Gate Attempt", null, "Error: gate check failed.");
            }
          //Input_Gate check
          else if (inputting_gate.get_logic_gate_type() == 0)
              {
               System.err.println("Error: gate id " + second_selected_gate + ",  gate connection rejected due to attempt to input into an input gate. Selected gate reset.");
               popup_message(Alert.AlertType.WARNING, "Connection Rejected", null, "Input gates can not have inputs.");
              }
          //NOT_Gate check
          else if (inputting_gate.get_logic_gate_type() == 4 && inputting_gate.in_gates.size() == 1)
              {
               System.err.println("Error: gate id " + second_selected_gate + ",  gate connection rejected due to attempt to have more than one input for a NOT gate. Selected gate reset.");
               popup_message(Alert.AlertType.WARNING, "Connection Rejected", null, "Not gates can only have 1 input.");
              }
          else
              {
               //Connect gates both ways like a double linked list.
               if (!outputting_gate.out_gates.add(second_selected_gate))
                 {
                  System.err.println("Error: output gate connection failed.");
                  popup_message(Alert.AlertType.ERROR, "Gate Connection Failed", null, "Error: output gate connection failed for unknown reason.");
                 }
               if (!inputting_gate.in_gates.add(first_selected_gate))
                 {
                  System.err.println("Error: input gate connection failed.");
                  popup_message(Alert.AlertType.ERROR, "Gate Connection Failed", null, "Error: input gate connection failed for unknown reason.");
                 }

               dirty_gate_container.add(inputting_gate);//Maintenance for gate connection

               Wire.connect_gate_wire(outputting_gate, inputting_gate);//Connect gate visually.
              }

          //Reset first selected gate string to special value after connecting, second selected gate string will be reset before use.
          first_selected_gate = -1;
         }
    }

    /**
     * <p>Function to disconnect gates, function to be called for Disconnect_button. Resets 'memory' of selected gates when 2 gates are disconnected. Acts with the same logic
     * as connect_button_functionality(int input), but removes an association that may exist between selected gates.</p><br>Note 1st selected gate not reset when
     * switching selecting another button. Said value also used by the Connect_button Togglebutton.
     *
     * @param input id of selected gate
     */
    public static void disconnect_button_functionality(int input)
    {
     //If 1st String does not have a meaningful value, assign it one. Should be the gate that is outputting.
     if (first_selected_gate == -1)
        first_selected_gate = input;
     else
         {
          Logic_Gate outputting_gate, inputting_gate;//avoid looking twice
          //circular gate check
          if (input == first_selected_gate)
            {
             System.err.println("Error: cannot disconnect gate from itself. Selected gate reset.");
             first_selected_gate = -1;
             popup_message(Alert.AlertType.WARNING, "Circular Gate Attempt", null, "Warning: cannot disconnect gate from itself.");
             return;
            }

          second_selected_gate = input;//After this line 2 gates have been selected, thus proceed to connecting.

          //Only look for gates once after circular gate check.
          outputting_gate = logic_gate_container.get(first_selected_gate);
          inputting_gate = logic_gate_container.get(second_selected_gate);

          //Disconnect gates both ways like a double linked list. Typecast to Object to use LinkedList.remove(Object o) rather then LinkedList.remove(int index).
          if (!outputting_gate.out_gates.remove((Object) second_selected_gate))
            {
             System.err.println("Error: output gate disconnection failed.");
             popup_message(Alert.AlertType.ERROR, "Gate Disconnection Failed", null, "Error: output gate disconnection failed for unknown reason.");
            }
          if (!inputting_gate.in_gates.remove((Object) first_selected_gate))
            {
             System.err.println("Error: input gate disconnection failed.");
             popup_message(Alert.AlertType.ERROR, "Gate Disconnection Failed", null, "Error: input gate disconnection failed for unknown reason.");
            }

          dirty_gate_container.add(inputting_gate);//Add id second_selected_gate do to it being possible that it's rank is no longer valid.

          Wire.disconnect_gate_wire(outputting_gate, inputting_gate);//Disconnect gate visually.

          first_selected_gate = -1;//Reset first selected gate string to special value after connecting, second selected gate string will be reset before use.
         }
    }

    /**
     * Function to remove gates, function to be called for Remove_button. Removes all connections to other gates, then is removed from id_universal_set_container and
     * logic_gate_container.
     *
     * @param input id of gate to remove
     */
    public static void remove_button_functionality(int input)
    {
     boolean flag_error = false;
     Logic_Gate remove_gate_place_holder = logic_gate_container.get(input), dirty_gate_place_holder;

     //Remove remove_gate_place_holder from other Logic_Gates' out_gates.
     for (int i = 0; i < remove_gate_place_holder.in_gates.size(); i++)
         logic_gate_container.get(remove_gate_place_holder.in_gates.get(i)).out_gates.remove((Object) input);//Type cast to have LinkedList.remove(Object o) called.

     //Mark gates whose ids are in remove_gate_place_holder.out_gates as dirty and remove remove_gate_place_holder from said gates' input.
     for (int i = 0; i < remove_gate_place_holder.out_gates.size(); i++)
        {
         dirty_gate_place_holder = logic_gate_container.get(remove_gate_place_holder.out_gates.get(i));
         dirty_gate_place_holder.in_gates.remove((Object) input);//Type cast to have LinkedList.remove(Object o) called.
         dirty_gate_container.add(dirty_gate_place_holder);
        }

     //Remove gate from all possible locations.
     dirty_gate_container.remove(remove_gate_place_holder);
     if (!logic_gate_container.remove(input, remove_gate_place_holder))
         {
          System.err.println("Gate " + input + " not removed from logic_gate_container.");
          flag_error = true;
         }
     if (!id_universal_set_container.get(remove_gate_place_holder.rank).remove((Object) input))//Type cast to have LinkedList.remove(Object o) called.
       {
        System.err.println("Gate " + input + " not removed from id_universal_set_container.");
        flag_error = true;
       }
     if (!Starting_Point_GUI.get_animation_pane_content_getChildren().remove(remove_gate_place_holder))
       {
        System.err.println("Gate " + input + " not removed from Starting_Point_GUI.animation_pane_content.");
        flag_error = true;
       }
     if (flag_error)
        popup_message(Alert.AlertType.ERROR, "Gate Removal Failed", null, "Error: gate removal failed for unknown reason.");

     Wire.remove_all_gate_wires(remove_gate_place_holder);//remove all wires involving remove_gate_place_holder

     first_selected_gate = -1;//Reset first_selected_gate after removing gate to ensure first_selected_gate isn't pointing to something already removed.
    }
}
