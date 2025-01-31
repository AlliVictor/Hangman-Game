import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class HangmanGame {
    private static List<String> words;
    private static String word;
    private static StringBuilder guessedWord;
    private static int attemptsLeft;

    public static void main(String[] args) {
        loadWordsFromFile("src/words.txt");
        play();
    }

    private static void loadWordsFromFile(String fileName) {
        try {
            words = Files.readAllLines(Paths.get(fileName));
            if (words.isEmpty()) {
                throw new IOException("Word list is empty.");
            }
        } catch (IOException e) {
            System.err.println("Error reading words file: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void play() {
        initializeGame();

        try (Scanner scanner = new Scanner(System.in)) {
            while (attemptsLeft > 0 && guessedWord.indexOf("_") != -1) {
                System.out.println("\nAttempts left: " + attemptsLeft);
                System.out.println("Word: " + guessedWord);
                System.out.print("Enter your guess: ");
                String input = scanner.nextLine().toLowerCase();

                if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                    checkGuess(input.charAt(0));
                } else {
                    System.out.println("Invalid guess. Please enter a single letter.");
                }
            }
        }

        if (guessedWord.indexOf("_") == -1) {
            System.out.println("\nCongratulations! You guessed the word: " + word);
        } else {
            System.out.println("\nGame over! You ran out of attempts. The word was: " + word);
        }
    }

    private static void initializeGame() {
        word = getRandomWord();
        guessedWord = new StringBuilder(word.replaceAll(".", "_"));
        attemptsLeft = 6;
    }

    private static String getRandomWord() {
        return words.get(new Random().nextInt(words.size()));
    }

    private static void checkGuess(char guess) {
        boolean guessedCorrectly = false;

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
                guessedWord.setCharAt(i, guess);
                guessedCorrectly = true;
            }
        }

        if (guessedCorrectly) {
            System.out.println("Correct guess!");
        } else {
            System.out.println("Incorrect guess!");
            attemptsLeft--;
        }
    }
}