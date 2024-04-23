package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class IOSearcher {

    public static boolean search(String word, String... fileNames) {
        for (String fileName : fileNames) {
            File file = new File(fileName);
            if (!file.exists() || !file.canRead()) {
                System.err.println("Cannot read file or file does not exist: " + fileName);
                continue;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
            {
                String line;
                while ((line = reader.readLine()) != null) 
                {
                    if (line.toLowerCase().contains(word.toLowerCase())) 
                    {
                        return true;
                    }
                }
            }
            catch (FileNotFoundException e) 
            {
                System.err.println("File not found: " + fileName);
                e.printStackTrace();
            }
            catch (IOException e) 
            {
                System.err.println("IOException occurred when reading the file: " + fileName);
                e.printStackTrace();
            }
        }
        return false;
    }
}
