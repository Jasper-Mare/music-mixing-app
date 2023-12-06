package src.prefs;

import java.awt.Dimension;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class PreferencesManager {

    private static final File appPrefsFile = new File("appPreferences.json");
    private AppPreferences appPrefs;

    public AppPreferences getAppPrefs() {
        return appPrefs;
    }

    public PreferencesManager() {
        try {
            JSONObject appPrefsJSON = (JSONObject) (new JSONParser().parse(new FileReader(appPrefsFile)));

            appPrefs = new AppPreferences(appPrefsJSON);

        } catch (IOException | ParseException e) {
            e.printStackTrace(System.err);

            // reading resulted in an error, so create default prefs
            appPrefs = AppPreferences.defaultPrefs;
        }

    }

    public void saveToFile() {
        saveAppPrefsToFile();
    }

    public void saveAppPrefsToFile() {
        // creating JSONObject
        JSONObject appPrefsJSON = new JSONObject();

        Dimension preferedDimension = appPrefs.getPreferedDimension();
        // for address data, first create LinkedHashMap
        Map<String, Integer> prefSize = new LinkedHashMap<String, Integer>(2);
        prefSize.put("w", preferedDimension.width);
        prefSize.put("h", preferedDimension.height);

        // putting address to JSONObject
        appPrefsJSON.put("preferedWindowSize", prefSize);

        try {
            PrintWriter appPrefsWriter;
            if (!appPrefsFile.exists()) {
                appPrefsFile.createNewFile();
            }

            appPrefsWriter = new PrintWriter(appPrefsFile);

            appPrefsWriter.write(appPrefsJSON.toJSONString());

            appPrefsWriter.flush();
            appPrefsWriter.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}

// CITATIONS:

// https://www.geeksforgeeks.org/parse-json-java/