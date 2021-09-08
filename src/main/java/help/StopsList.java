package help;

import entity.Stop;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StopsList {
    public static List<Stop> list = new ArrayList<>();

    static {
//        list.add(new Stop("stop__10046034", "Менделеева"));
//        list.add(new Stop("stop__10046027", "Менделеева"));
//        list.add(new Stop("stop__10045042", "Станция метро Тракторный завод"));
//        list.add(new Stop("stop__10045041", "Станция метро Тракторный завод"));
//        list.add(new Stop("stop__10046679", "Севастопольская"));
//        list.add(new Stop("stop__10046671", "Севастопольская"));
//        list.add(new Stop("stop__10046188", "Авангардная"));
//        list.add(new Stop("stop__10046176", "Авангардная"));
        Path path = Paths.get("precious_data.txt");

        BufferedReader reader;
        try {
            reader = Files.newBufferedReader(path);
            String line;
            while (true) {
                line = reader.readLine();
                if (line != null) {
                    String[] arr = line.split(":");
                    System.out.println(arr[0]);
                    list.add(new Stop("stop__"+arr[0], arr[1]));
                } else {
                    reader.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(list);

        }
    }
}
