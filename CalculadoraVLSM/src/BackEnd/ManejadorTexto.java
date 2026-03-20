package BackEnd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ManejadorTexto{

    public static ArrayList<ArrayList<String>> readFile(String filePath) throws IOException{
        File file = new File(filePath);
        ArrayList<ArrayList<String>> config = new ArrayList<>();

        if(!file.exists()){
            return config;
        }

        try(BufferedReader readFile = new BufferedReader(new FileReader(file))){
            String line = readFile.readLine();
            while(line != null){
                ArrayList<String> data = parseConfigLine(line);
                if(data!=null){
                    config.add(data);
                }
                line = readFile.readLine();
            }
        }
        return config;
    }

    private static ArrayList<String> parseConfigLine(String line){
        String[] values = line.split(";", 2);
        if(values.length<2){
            return null;
        }

        ArrayList<String> data = new ArrayList<>();
        data.add(values[0]);
        data.add(values[1]);
        return data;
    }
}
