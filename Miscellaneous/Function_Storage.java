package Miscellaneous;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * General utilities class for static functions.
 * @author Gabriel Toban Harris (not only programmer, only one marking dates.)
 * @date: 2017-10-6/2018-1-1/2018-7-3
 */
public final class Function_Storage
{
    /**
     * Don't let anyone instantiate this class.
     */
    private Function_Storage() {}

    /**
     * Gets the horizontal bound coordinates of the window.
     */
    public static double horizontal_screen_bound()
    {
     return Screen.getPrimary().getVisualBounds().getWidth();
    }

    /**
     * Gets the vertical bound coordinates of the window.
     */
    public static double vertical_screen_bound()
    {
     return Screen.getPrimary().getVisualBounds().getHeight();
    }

    /**
     * Gets the directory of a file in the resources folder.
     */
    public static String get_Resource_Dir(String path)
    {
     return Constants.RESOURCE_NAME + "/" + path;
    }

    /**
     * close all stages
     * @param stages_close array of stages to close.
     */
    public static void close_everything(Stage[] stages_close)
    {
     for (byte index = 0; index < stages_close.length; index++)
         stages_close[index].close();
     System.exit(0);
    }

    /**
     * pop-up messages
     * @param type of Alert to be displayed
     * @param title of window
     * @param header of message
     * @param content main message displayed
     */
    public static void popup_message(AlertType type, String title, String header, String content)
    {
     Alert popup = new Alert(type);
     popup.setTitle(title);
     popup.setHeaderText(header);
     popup.setContentText(content);
     popup.setResizable(false);
     popup.showAndWait();
    }
}
