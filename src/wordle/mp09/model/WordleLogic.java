package wordle.mp09.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleLogic {
    private ArrayList<String> wordsList = new ArrayList<String>();
    private String WRONG = "\u001B[31m";
    private String CLOSE = "\u001B[33m";
    private String CORRECT = "\u001B[32m";
    private String RESET = "\u001B[00m";

    public WordleLogic() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\beryp\\Desktop\\repos\\WordleMP09\\src\\wordle\\mp09\\words\\words.txt"));
            String line = reader.readLine();
            while (line != null) {
                wordsList.add(line.trim());
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRandomWord() {
        Random random = new Random();
        int index = random.nextInt(wordsList.size());
        return wordsList.get(index);
    }

    public String checkGuess(String guess, String word) {
        guess =  guess.toUpperCase();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < guess.length(); i++) {
            char c = guess.charAt(i);
            if (word.indexOf(c) == i) {
                // If the letter exists and is in the correct position, it shows it in green
                result.append(CORRECT).append(c).append(RESET);
            } else if (word.indexOf(c) != -1) {
                // If the letter exists and is NOT in the correct position, it shows it in yellow
                result.append(CLOSE).append(c).append(RESET);
            } else {
                // If the letter doesn't exist at all, it shows it in red
                result.append(WRONG).append(c).append(RESET);
            }
        }
        return result.toString();
    }
}