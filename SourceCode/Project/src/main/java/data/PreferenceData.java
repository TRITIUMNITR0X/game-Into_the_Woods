package data;

/**
 * Preference Data stores the fundamental information of user-defined preferences.
 */
public class PreferenceData {

    public static int DEFAULT_WINDOW_WIDTH = 0;
    public static int DEFAULT_WINDOW_HEIGHT = 0;

    public static int window_width_selected = 0;
    public static int window_height_selected = 0;

    public static int window_width_actual = 0;
    public static int window_height_actual = 0;

    private static WindowType window_type = WindowType.WINDOWED;

    public static short FRAMERATE_DEFAULT = 60;
    public static short frameRate = 60;

    public static double scaledW = 1;
    public static double scaledH = 1;

    public void calcResScale() {
        /*
        scaledW = (double) window_width_selected / DEFAULT_WINDOW_WIDTH;
        scaledH = (double) window_height_selected / DEFAULT_WINDOW_HEIGHT;
        */
        /*
        scaledW = (double) DEFAULT_WINDOW_WIDTH / (double)window_width_actual;
        scaledH = (double) DEFAULT_WINDOW_HEIGHT / (double)window_height_actual;
        */
        scaledW = (double)window_width_actual / (double) DEFAULT_WINDOW_WIDTH;
        scaledH = (double)window_height_actual / (double) DEFAULT_WINDOW_HEIGHT;
    }

    public void setWindowWidthSelected(int width) {
        this.window_width_selected = width;
    }

    public int getWindowWidthSelected() {
        return window_width_selected;
    }

    public void setWindowHeightSelected(int height) {
        this.window_height_selected = height;
    }

    public int getWindowHeightSelected() {
        return window_height_selected;
    }

    public void setWindowWidthActual(int width) {
        window_width_actual = width;
    }

    public void setWindowHeightActual(int height) {
        window_height_actual = height;
    }

    public void setWindowWidthDefault(int width) {
        this.DEFAULT_WINDOW_WIDTH = width;
    }

    public void setWindowHeightDefault(int height) {
        this.DEFAULT_WINDOW_HEIGHT = height;
    }

    public void setWindowType(int type) {
        this.window_type = WindowType.values()[type];
    }

    public WindowType getWindowType() {
        return window_type;
    }

    public void setFrameRate(short frameRate) {
        this.frameRate = frameRate;
    }

    public short getFrameRate() {
        return frameRate;
    }

    public void setFrameRateDefault(short framerateDefault) {
        FRAMERATE_DEFAULT = framerateDefault;
    }



    public enum WindowType {
        WINDOWED(0, "Windowed"), WINDOWED_FULLSCREEN(1, "Windowed Fullscreen"), FULLSCREEN(2, "Fullscreen");

        private int type;
        private String name;

        WindowType(int type, String name){
            this.type = type;
            this.name = name;
        }
    }

    public void post() {
        calcResScale();
    }

    public String toString() {
        return window_width_selected + " " + window_height_selected + " " + window_type;
    }

}
