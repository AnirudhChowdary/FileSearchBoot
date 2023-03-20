package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

@RestController
@RequestMapping("/api/searchfiles")
public class Main {

    @GetMapping()
    public List<String> getAllFiles(@RequestBody String input) throws FileNotFoundException {
        Map<String, Set<String>> Dictionary = new HashMap<>();
        readFiles(Dictionary);
        return printFiles(Dictionary,input);
    }


    public static List<String> printFiles(Map<String, Set<String>> Dictionary,String input) throws FileNotFoundException {

        String word;
        List<String> stopWords = readStopWords();
        List<String> files = new ArrayList<>();
        List<String> Repeatfiles = new ArrayList<>();

        String [] wordsInInput = input.split(" ");

        for(int i=0;i<wordsInInput.length;i++) {

            word=wordsInInput[i];
            String StopWordCheck = word.toLowerCase();
            if (stopWords.contains(StopWordCheck)) {
                files.add("It is a stop word");
                return files;
            } else if (!Dictionary.containsKey(word)) {
                files.add("The word does not exist in our file system");
                return files;
            } else {
                for (String filename : Dictionary.get(word)) {
                    files.add(filename + ".txt");
                }

            }
        }
      return files;
    }

    public static List<String> readStopWords() throws FileNotFoundException {
        String stopWordFilePath = "src/main/java/com/example/demo/StopWords.txt";
        Scanner stopWord = new Scanner(new FileReader(stopWordFilePath));
        List<String> stopWords = new ArrayList<>();
        while(stopWord.hasNext())
            stopWords.add(stopWord.next().toLowerCase());

        return stopWords;
    }


    public static void readFiles(Map<String, Set<String>> Dictionary) throws FileNotFoundException {
        Scanner Files;
        String word;
        String Filepath = "src/main/java/com/example/demo/files/";
        String fileExt =".txt";
        String fileName ="";
        List<String> stopWords = readStopWords();

        for(int x=0; x<100; x++)
        {
            fileName = Integer.toString(x+1);
            Files = new Scanner(new FileReader(Filepath+fileName+fileExt));
            while (Files.hasNext( ))
            {
                word = Files.next( );
                word = word.replaceAll("[^a-zA-Z0-9\\s]", "");
                String MatchtoStopWord = word;
                if(!stopWords.contains(MatchtoStopWord.toLowerCase())){
                    Set<String> filenames = Dictionary.get(word);
                    if (filenames == null) {
                        filenames = new HashSet<String>();
                    }
                    filenames.add(fileName);
                    Dictionary.put(word, filenames);
                }
            }
        }
    }
}
