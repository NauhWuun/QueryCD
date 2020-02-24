package com.Query.Columnar;

import java.io.*;
import java.util.*;

public class Configure
{
    private static final Properties pros = new Properties();

    public static void toLocal(Map<Object, Object> subColTimeObject) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream("QueryCD.properties", true);
            pros.putAll(subColTimeObject);
            pros.store(fos, "Properties TimeIndex");
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Map<Object, Object> loadInLocal() {
        Map<Object, Object> maps = new HashMap<>();

        try {
            FileInputStream fis = new FileInputStream("QueryCD.properties");
            pros.load(fis);
            pros.forEach(maps::put);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maps;
    }
}