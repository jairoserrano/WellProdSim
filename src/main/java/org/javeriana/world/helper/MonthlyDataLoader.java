package org.javeriana.world.helper;

import org.javeriana.world.layer.data.MonthData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MonthlyDataLoader {

    public static List<MonthData> loadMonthlyDataFile(String dataFileLocation) throws IOException {
        ClassLoader classLoader = MonthlyDataLoader.class.getClassLoader();
        File jsonFile = new File(classLoader.getResource(dataFileLocation).getFile());
        String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFile.toURI())), StandardCharsets.UTF_8);
        return jsonToMonthlyData(jsonContent);
    }

    private static List<MonthData> jsonToMonthlyData(String jsonContent) {
        JSONArray radiationJson = new JSONArray(jsonContent);
        ArrayList<MonthData> monthlyData = new ArrayList<>();
        radiationJson.forEach(item -> {
            JSONObject currentObject = (JSONObject) item;
            MonthData monthData = new MonthData();
            monthData.setAverage(currentObject.getDouble("average"));
            monthData.setMaxValue(currentObject.getDouble("maxValue"));
            monthData.setMinValue(currentObject.getDouble("minValue"));
            monthData.setStandardDeviation(currentObject.getDouble("standardDeviation"));
            monthlyData.add(monthData);
        });
        return monthlyData;
    }
}
