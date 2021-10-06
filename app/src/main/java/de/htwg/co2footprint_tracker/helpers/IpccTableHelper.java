package de.htwg.co2footprint_tracker.helpers;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class IpccTableHelper {


    @RequiresApi(api = Build.VERSION_CODES.N)
    public IpccTableHelper(Context context) {


        String jsonString = "hier ist nichts!";
        try {
            InputStream is = context.getAssets().open("");
            jsonString = convertStreamToString(is);

        } catch (IOException e) {
            e.printStackTrace();
        }

//        String jsonString = new FileReader("de")

        HashMap<String, Object> map = new Gson().fromJson(jsonString, HashMap.class);

        Map<String, Object> flattenedMap = flatMap(map);


        int i = 0;

    }

    public Map getProductionValueMap() {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    static Map<String, Object> flatMap(Map<String, Object> map) {
        Map<String, Object> flatenedMap = new HashMap<>();
        map.forEach((key, value) -> {
            if (value instanceof Map) {
                flatenedMap.putAll(flatMap((Map) value));
            } else {
                flatenedMap.put(key, value);
            }
        });

        return flatenedMap;
    }

    public static String convertStreamToString(InputStream is)
            throws IOException {

        Writer writer = new StringWriter();
        char[] buffer = new char[2048];

        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        String text = writer.toString();
        return text;
    }

}
