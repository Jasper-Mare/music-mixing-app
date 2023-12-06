package src.prefs;

import java.awt.Dimension;
import java.util.Map;

import org.json.simple.JSONObject;

public class AppPreferences {

    // ======================= Attributes ======================= //

    private Dimension preferedDimension;

    // ======================= Defaults ======================= //

    public static final AppPreferences defaultPrefs = new AppPreferences(
            new Dimension(640, 480));

    // ======================= Constructors ======================= //

    public AppPreferences(Dimension preferedDimension) {
        this.preferedDimension = preferedDimension;
    }

    public AppPreferences(JSONObject jsonObject) {

        Map<String, Long> prefSize = (Map<String, Long>) jsonObject.get("preferedWindowSize");
        preferedDimension = new Dimension(((Long) prefSize.get("w")).intValue(), ((Long) prefSize.get("h")).intValue());

    }

    // ======================= Getters ======================= //

    public Dimension getPreferedDimension() {
        return preferedDimension;
    }

}
