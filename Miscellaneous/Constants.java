package Miscellaneous;

/**
 * Store constants used through out application
 * @author Gabriel Toban Harris
 * @date: 2017-10-6/2017-12-31/2018-1-2/2018-5-4/2018-5-7/2018-6-27
 */
public interface Constants
{
    /**
     * Unique int representing corresponding gate types, also corresponds to image locations arrays.
     */
    int INPUT = 0, AND = 1, OR = 2, XOR = 3, NOT = 4, NAND = 5, NOR = 6, XNOR = 7;

    /**
     * Name of folder containing resources.
     */
    String RESOURCE_NAME = "Resources";
    /**
     * Name of folder in in resource folder that contains image files.
     */
    String IMAGE_LOCATION = "Images/";
    /**
     * Name of folder in in resource folder that contains HTML and CSS files.
     */
    String HTML_CSS_LOCATION = "HTML and CSS/";
    /**
     * Locations within folder containing images that have the image for when a gate outputs false.
     */
    String LOGIC_GATE_FALSE_LOCATIONS [] = {Constants.IMAGE_LOCATION + "INPUT Gate False gth 2018.png", Constants.IMAGE_LOCATION + "AND Gate False gth 2018.png", Constants.IMAGE_LOCATION + "OR Gate False gth 2018.png",
                                            Constants.IMAGE_LOCATION + "XOR Gate False gth 2018.png", Constants.IMAGE_LOCATION + "NOT Gate False gth 2018.png", Constants.IMAGE_LOCATION + "NAND Gate False gth 2018.png",
                                            Constants.IMAGE_LOCATION + "NOR Gate False gth 2018.png", Constants.IMAGE_LOCATION + "XNOR Gate False gth 2018.png"};
    /**
     * Locations within folder containing images that have the image for when a gate outputs true.
     */
    String LOGIC_GATE_TRUE_LOCATIONS [] = {Constants.IMAGE_LOCATION + "INPUT Gate True gth 2018.png", Constants.IMAGE_LOCATION + "AND Gate True gth 2018.png", Constants.IMAGE_LOCATION + "OR Gate True gth 2018.png",
                                           Constants.IMAGE_LOCATION + "XOR Gate True gth 2018.png", Constants.IMAGE_LOCATION + "NOT Gate True gth 2018.png", Constants.IMAGE_LOCATION + "NAND Gate True gth 2018.png",
                                           Constants.IMAGE_LOCATION + "NOR Gate True gth 2018.png", Constants.IMAGE_LOCATION + "XNOR Gate True gth 2018.png"};
    /**
     * Locations within folder containing locations of Tooltip HTML files.
     */
    String LOGIC_GATE_TOOLTIP_LOCATIONS [] = {Constants.HTML_CSS_LOCATION + "INPUT_Gate_Tooltip.html", Constants.HTML_CSS_LOCATION + "AND_Gate_Tooltip.html", Constants.HTML_CSS_LOCATION + "OR_Gate_Tooltip.html",
                                              Constants.HTML_CSS_LOCATION + "XOR_Gate_Tooltip.html", Constants.HTML_CSS_LOCATION + "NOT_Gate_Tooltip.html", Constants.HTML_CSS_LOCATION + "NAND_Gate_Tooltip.html",
                                              Constants.HTML_CSS_LOCATION + "NOR_Gate_Tooltip.html", Constants.HTML_CSS_LOCATION + "XNOR_Gate_Tooltip.html"};
}
