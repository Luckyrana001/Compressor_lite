package imagepicker;

/**
 * Created by Mickael on 18/10/2016.
 */
public class GlobalHolder {

    private static GlobalHolder ourInstance = new GlobalHolder();
    private PickerManager pickerManager;

    private GlobalHolder() {
    }

    public static GlobalHolder getInstance() {
        return ourInstance;
    }

    public PickerManager getPickerManager() {
        return pickerManager;
    }

    public void setPickerManager(PickerManager pickerManager) {
        this.pickerManager = pickerManager;
    }
}
