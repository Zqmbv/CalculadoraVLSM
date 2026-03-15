package calculadoravlsm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class ManejadorTexto{

    public static ArrayList<ArrayList<String>> readFile(String filePath) throws IOException{
        File file = new File(filePath);
        BufferedReader readFile = new BufferedReader(new FileReader(file));
        ArrayList<ArrayList<String>> config = new ArrayList<>();
        if(file.exists()){
            String line = readFile.readLine();
            while(line != null){
                ArrayList<String> data = new ArrayList<>();
                data.add(line.split(";")[0]);
                data.add(line.split(";")[1]);
                config.add(data);
                line = readFile.readLine();
            }
        }
        readFile.close();
        return config;
    }
}
