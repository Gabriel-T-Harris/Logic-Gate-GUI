package GUI;

import Logic_Circuit.Circuit;
import Logic_Gates.AND_Gate;
import Logic_Gates.Input_Gate;
import Logic_Gates.Logic_Gate;
import Logic_Gates.NAND_Gate;
import Logic_Gates.NOR_Gate;
import Logic_Gates.NOT_Gate;
import Logic_Gates.OR_Gate;
import Logic_Gates.XNOR_Gate;
import Logic_Gates.XOR_Gate;
import Miscellaneous.Constants;
import Miscellaneous.Function_Storage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import static Logic_Circuit.Circuit.logic_gate_container;

/**
<b>
Purpose: Origin point of application execution<br>
Programmer: Gabriel Toban Harris<br>
Date: 2017-12-31/2018-1-1/2018-1-2/2018-1-3/2018-5-2/2018-5-3/2018-5-4/2018-5-5/2018-5-7/2018-5-8/2018-5-12/2018-5-15/2018-5-19/2018-5-21/2018-5-22/2018-5-24/2018-5-29/2018-5-31/
2018-6-6/2018-6-27/2018-6-28/2018-6-29/2018-6-30
</b>
*/

public class Starting_Point_GUI extends Application implements EventHandler<Event>
{
        private static int id = 0;
        public static String selected_togglebutton = "NULL";/*Use with change listener to determine which toggle button is selected as oppose to checking array index,
                                                              as this way doesn't rely on an array index which can change and can be misaligned.*/
        private static MenuItem[] menu_item_container = {new MenuItem("_Evaluate"), new MenuItem("_Help"), new MenuItem("_About"), new MenuItem("_Save"), new MenuItem("_Load")};
        private static ToggleGroup logic_gate_toggle_group = new ToggleGroup();
        private static Pane animation_pane_content = new Pane();//helper pane to animation_scroll_pane
        private static ScrollPane animation_scroll_pane;//Center
        private static Stage[] stage_container = {new Stage(), new Stage()};

        public void start(Stage primary_stage)
        {
         String[] string_container_help_about_stage = {"Help", "About", "Add: select a gate from the tabs and click in window to add selected gate.\n\n" +
                                                       "Remove: click a placed gate to remove it and its connections to other gates." +
                                                       "\n\nConnect: 1st gate clicked is the outputting gate, while the 2nd gate clicked is the inputting gate." +
                                                       "\n\nDisconnect: 1 gate connection between 1st clicked gate and 2nd clicked gate is removed, in the event that such a connection exists." +
                                                       "\n\n Zoom: was meant to work with scrolling mouse and button combinations, however was abandoned due to lack of intrest and usefullness." +
                                                       "\n\n While the Save menu item works fine, the Load menu item has issues with restoring gates. Load menu item was abandoned for similar reasons as Zoom." +
                                                       "\n\n Tips: Toggle input gates by clicking them with the secondary mouse button",
                                                       "Application programmed and coded by Gabriel Toban Harris with advisory aid from Mark."};//Made for for loop initialization of both help and about stage.
         FileChooser file_picker = new FileChooser();
         final Image IMAGE_ICON = new Image(Function_Storage.get_Resource_Dir(Constants.IMAGE_LOCATION + "Personal Symbol Without Background gth 2017.png"));
         Menu list_of_actions_menu = new Menu("_File");
         ToggleButton[] togglebutton_group_container = {new ToggleButton("_Add"), new ToggleButton("_Remove"), new ToggleButton("_Connect"), new ToggleButton("_Disconnect"), new ToggleButton("_Zoom")};
         RadioButton[] logic_gate_button_container = new RadioButton[8];
         ToggleGroup button_toggle_group = new ToggleGroup();
         BorderPane main_border_pane;//Pane that everything is built on
         HBox togglebutton_HBox = new HBox();
         VBox actions_VBox = new VBox();//Top
         VBox[] logic_gates_VBox_container = {new VBox(), new VBox()};//1st is regular_logic_gates_VBox, 2nd is negated_logic_gates_VBox
         TabPane logic_gate_pane = new TabPane();//Right
         Tab tab_container[] = new Tab [2];
         Stage main_stage = new Stage();//Dimensions are magic number representing what looks good cosmetically.

         //logic_gate_pane setup
           //for loop numbers are based on where Strings appear in Constants.LOGIC_GATE_FALSE_LOCATIONS[]
         for (int i = 0, j = 4; i < 4; i++, j++)
            {
             //regular logic gates
             logic_gate_button_container[i] = new RadioButton();
             logic_gate_button_container[i].setGraphic(new ImageView(Function_Storage.get_Resource_Dir(Constants.LOGIC_GATE_FALSE_LOCATIONS[i])));
             logic_gate_button_container[i].setToggleGroup(logic_gate_toggle_group);
             logic_gate_button_container[i].setUserData(i);
             logic_gates_VBox_container[0].getChildren().add(logic_gate_button_container[i]);

             //negated logic gates
             logic_gate_button_container[j] = new RadioButton();
             logic_gate_button_container[j].setGraphic(new ImageView(Function_Storage.get_Resource_Dir(Constants.LOGIC_GATE_FALSE_LOCATIONS[j])));
             logic_gate_button_container[j].setToggleGroup(logic_gate_toggle_group);
             logic_gate_button_container[j].setUserData(j);
             logic_gates_VBox_container[1].getChildren().add(logic_gate_button_container[j]);
            }

         logic_gate_button_container[0].setSelected(true);//Set it initially to avoid null pointers.

         tab_container[0] = new Tab("Regular Logic Gates");
         tab_container[1] = new Tab("Negated Logic Gates");

         for (int i = 0; i < logic_gates_VBox_container.length; i++)
            {
             logic_gates_VBox_container[i].setSpacing(25);
             logic_gates_VBox_container[i].setPadding(new Insets(0, 60, 0, 60));
             tab_container[i].setContent(new ScrollPane(logic_gates_VBox_container[i]));
            }

         for (int i = 0; i < tab_container.length; i++)
            {
             tab_container[i].setClosable(false);
             logic_gate_pane.getTabs().add(tab_container[i]);
            }

         //actions_VBox setup
         for (int i = 0; i < togglebutton_group_container.length; i++)
            {
             togglebutton_group_container[i].setToggleGroup(button_toggle_group);
             togglebutton_group_container[i].setId(togglebutton_group_container[i].getText() + "_button");
             togglebutton_group_container[i].setUserData(togglebutton_group_container[i].getId());
             togglebutton_HBox.getChildren().add(togglebutton_group_container[i]);
            }

         togglebutton_group_container[4].setDisable(true);//Zoom button

         EventHandler<ActionEvent> menu_event_handling = new EventHandler<ActionEvent>()
         {
          public void handle(ActionEvent e)
          {
           String source_id = ((MenuItem) e.getSource()).getId();//only being used with menu items

           //_Evaluate_menu_item
           if (source_id.equals("_Evaluate_menu_item"))
              Circuit.evaluate_circuit();
           //_Help_menu_item
           else if (source_id.equals("_Help_menu_item"))
               {
                stage_container[0].show();//help stage
                menu_item_container[1].setDisable(true);//_Help_menu_item
               }
           //_About_menu_item
           else if (source_id.equals("_About_menu_item"))
               {
                stage_container[1].show();//about stage
                menu_item_container[2].setDisable(true);//_About_menu_item
               }
           //_Save_menu_item
           else if (source_id.equals("_Save_menu_item"))
               {
                if (logic_gate_container.size() > 0)
                  {
                   file_picker.setTitle("Save As");
                   File file = file_picker.showSaveDialog(main_stage);
                   if (file != null)//test file before using
                      try (ObjectOutputStream output_file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file))))
                         {
                          Logic_Gate logic_gates_to_output[] = new Logic_Gate[logic_gate_container.size()];

                          Circuit.find_clean_all_gates();//Find all dirty gates for gate cleaning, then clean all gates for rank check. Done so everything is proper, before saving.
                          logic_gate_container.values().toArray(logic_gates_to_output);

                          output_file.writeObject(id);//Object that ensures uniqueness of Logic_Gate.id.
                          output_file.writeObject(logic_gates_to_output);//Object that everything can be made from.
                         }
                      catch (IOException ex)
                           {
                            ex.printStackTrace();
                           }
                      finally
                             {
                              file_picker.setInitialDirectory(file.getParentFile());
                             }
                  }
                else
                     Function_Storage.popup_message(AlertType.INFORMATION, "Nothing to save", null, "There are zero gates in play, thus nothing to save.");

               }
           //_Load_menu_item
           else if (source_id.equals("_Load_menu_item"))
               {
                file_picker.setTitle("Open File");
                File file = file_picker.showOpenDialog(main_stage);
                if (file != null)//test file before using
                   try (ObjectInputStream input_file = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file))))
                      {
                       //Order of input_file.readObject() is as is to match specific output_file.writeObject(Object obj).
                       id = (int) input_file.readObject();
                       Logic_Gate[] logic_gate_input_container = (Logic_Gate[]) input_file.readObject();

                       //Clear 3 of the 4 major object containers, to prepare for both Logic_Gate display and Logic_Gate storage.
                       get_animation_pane_content_getChildren().clear();
                       Circuit.id_universal_set_container.clear();
                       logic_gate_container.clear();

                       //remake circuit
                       for (int i = 0; i < logic_gate_input_container.length; i++)
                          {
                           //FIXME does not load properly, all objects are there, yet Logic_Gates are not visible. Note made in help stage.
                           //Logic_Gate display
                           logic_gate_input_container[i].call_after_deserialization();
                           logic_gate_container.putIfAbsent(logic_gate_input_container[i].get_ID(), logic_gate_input_container[i]);
                           get_animation_pane_content_getChildren().add(/*logic_gate_container.get(logic_gate_input_container[i].get_ID())*/logic_gate_input_container[i]);

                           //rows have to exist to be added to
                           while (logic_gate_input_container[i].rank > Circuit.id_universal_set_container.size() - 1)
                                 Circuit.id_universal_set_container.add(new LinkedList<Integer>());

                           Circuit.id_universal_set_container.get(logic_gate_input_container[i].rank).add(logic_gate_input_container[i].get_ID());
                          }

                       //connect circuit, Wire section
                       Wire.clear_wire_container();
                       for (int i = 0; i < logic_gate_input_container.length; i++)//can not combine with above loop as above loop is building new_hash_map.
                           for (int k = 0; k < logic_gate_input_container[i].out_gates.size(); k++)
                               Wire.connect_gate_wire(logic_gate_input_container[i], logic_gate_container.get(logic_gate_input_container[i].out_gates.get(k)));
                      }
                   catch (IOException | ClassNotFoundException ex)
                        {
                         ex.printStackTrace();
                        }
                   finally
                          {
                           file_picker.setInitialDirectory(file.getParentFile());
                          }
               }
          }
         };

         //set up menu
         for (int i = 0; i < menu_item_container.length; i++)
            {
             menu_item_container[i].setOnAction(menu_event_handling);
             menu_item_container[i].setId(menu_item_container[i].getText() + "_menu_item");
             if (i == 1 || i == 3)//Numbers based on what looks good for separator placement.
                list_of_actions_menu.getItems().add(new SeparatorMenuItem());
             list_of_actions_menu.getItems().add(menu_item_container[i]);
            }

         actions_VBox.getChildren().addAll(new MenuBar(list_of_actions_menu), togglebutton_HBox);
         actions_VBox.setStyle("-fx-font: 20 arial;");

         animation_scroll_pane = new ScrollPane(animation_pane_content);
         animation_scroll_pane.setPannable(true);
         animation_scroll_pane.setOnMouseClicked(this);

         button_toggle_group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
         {
          public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle)
          {
           if (new_toggle != null)//Skip when no togglebutton is selected.
              selected_togglebutton = (String) button_toggle_group.getSelectedToggle().getUserData();
           else
                selected_togglebutton = "NULL";
          }
         });

         //set up main pane
         main_border_pane = new BorderPane(animation_scroll_pane, actions_VBox, logic_gate_pane, null, null);

         //Set up windows for Help_button and About_button.
         for (byte i = 0; i < stage_container.length; i++)
            {
             VBox text_holder = new VBox(new Text(string_container_help_about_stage[2 + i]));//2 + to allign with string_container_help_about_stage.
             text_holder.setAlignment(Pos.CENTER);
             text_holder.setStyle("-fx-font: 24 arial;");
             stage_container[i].setScene(new Scene(new ScrollPane(text_holder), 400, 300));
             stage_container[i].setTitle(string_container_help_about_stage[i]);
             stage_container[i].getIcons().add(IMAGE_ICON);
             stage_container[i].addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, this);
            }

         file_picker.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Custom file type used by this application", "*.lgg"));//".lgg" is a custom file extension for this application. Stands for Logic Gate GUI.

         main_stage.setTitle("Logic Gate GUI");
         main_stage.setScene(new Scene(main_border_pane, 800, 510));
         main_stage.setMinWidth(555);
         main_stage.setMinHeight(450);
         main_stage.getIcons().add(IMAGE_ICON);
         main_stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, this);
         main_stage.show();
        }

        /**
         * X offset to have constant add location relative to mouse.
         */
        private static double x_offset()
        {
         return (animation_pane_content.getWidth() - animation_scroll_pane.getViewportBounds().getWidth()) * animation_scroll_pane.getHvalue();
        }

        /**
         * Y offset to have constant add location relative to mouse.
         */
        private static double y_offset()
        {
         return (animation_pane_content.getHeight() - animation_scroll_pane.getViewportBounds().getHeight()) * animation_scroll_pane.getVvalue();
        }

        /**
         * Getter for animation_pane_content.getChildren();.
         */
        public static ObservableList<Node> get_animation_pane_content_getChildren()
        {
         return animation_pane_content.getChildren();
        }

        /**
         * Allow outside access to set animation_scroll_pane.setPannable(boolean value).
         *
         * @param value when true animation_scroll_pane is pannable, ehen false animation_scroll_pane is not pannable
         */
        public static void animation_pane_is_pannable(boolean value)
        {
         animation_scroll_pane.setPannable(value);
        }

        /**
         * EventHandler of this class.
         */
        public void handle(Event e)
        {
         Object source = e.getSource();
         int selected_gate = (int) logic_gate_toggle_group.getSelectedToggle().getUserData();

         if (source == animation_scroll_pane)
           {
            //_Add_button
            if (selected_togglebutton.equals("_Add_button"))
              {
               //id check in case of id overflowed
               if (id == -1)//-1 is special value for gate creation.
                 {
                  System.err.println("Warning: id probably over flowed, no new gates may be created.");
                  Function_Storage.popup_message(Alert.AlertType.WARNING, "To many Gates", null, "Warning: no more gates can be created.");
                  return;
                 }
               switch (selected_gate)
                     {
                      case Constants.INPUT:
                          {
                           logic_gate_container.putIfAbsent(id, new Input_Gate(id, ((MouseEvent) e).getX() + x_offset(), ((MouseEvent) e).getY() + y_offset()));
                           break;
                          }
                      case Constants.AND:
                          {
                           logic_gate_container.putIfAbsent(id, new AND_Gate(id, ((MouseEvent) e).getX() + x_offset(), ((MouseEvent) e).getY() + y_offset()));
                           break;
                          }
                      case Constants.OR:
                          {
                           logic_gate_container.putIfAbsent(id, new OR_Gate(id, ((MouseEvent) e).getX() + x_offset(), ((MouseEvent) e).getY() + y_offset()));
                           break;
                          }
                      case Constants.XOR:
                          {
                           logic_gate_container.putIfAbsent(id, new XOR_Gate(id, ((MouseEvent) e).getX() + x_offset(), ((MouseEvent) e).getY() + y_offset()));
                           break;
                          }
                      case Constants.NOT:
                          {
                           logic_gate_container.putIfAbsent(id, new NOT_Gate(id, ((MouseEvent) e).getX() + x_offset(), ((MouseEvent) e).getY() + y_offset()));
                           break;
                          }
                      case Constants.NAND:
                          {
                           logic_gate_container.putIfAbsent(id, new NAND_Gate(id, ((MouseEvent) e).getX() + x_offset(), ((MouseEvent) e).getY() + y_offset()));
                           break;
                          }
                      case Constants.NOR:
                          {
                           logic_gate_container.putIfAbsent(id, new NOR_Gate(id, ((MouseEvent) e).getX() + x_offset(), ((MouseEvent) e).getY() + y_offset()));
                           break;
                          }
                      case Constants.XNOR:
                          {
                           logic_gate_container.putIfAbsent(id, new XNOR_Gate(id, ((MouseEvent) e).getX() + x_offset(), ((MouseEvent) e).getY() + y_offset()));
                           break;
                          }
                      default:
                             {
                              System.err.println("Error: no such gate exists, this message should never be seen.");
                              Function_Storage.popup_message(Alert.AlertType.ERROR, "Impossible Error", null, "Error: no such gate exists, this message should never be seen.");
                              return;
                             }
                     }
               Circuit.id_universal_set_container.get(0).add(id);
               get_animation_pane_content_getChildren().add(logic_gate_container.get(id++));
              }
            //Zoom_button
            else if (selected_togglebutton.equals("_Zoom_button"))
                {
                 //Abandoned functionality due to annoyance of implementation and lack of usefulness.
                }
           }
         //window close requests
         else if (e.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST)
             {
              //help stage
              if (source == stage_container[0])
                 menu_item_container[1].setDisable(false);//Help_button
              //about stage
              else if (source == stage_container[1])
                   menu_item_container[2].setDisable(false);//About_button
              //Must be from primary_stage
              else
                   Function_Storage.close_everything(stage_container);//Close both help stage and about stage in case either are open.
             }
        }

        //For Eclipse to know what file to run and for a runnable JAR.
        public static void main(String [] args){launch(args);}
}
