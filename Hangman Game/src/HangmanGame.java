import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class HangmanGame {
    private static List<String> words;
    private static String word;
    private static StringBuilder guessedWord;
    private static int attemptsLeft;
    private static Set<Character> guessedLetters;

    public static void main(String[] args) {
        loadWordsFromFile();

        Scanner scanner = new Scanner(System.in);
        do {
            play(scanner);
            System.out.print("\nWould you like to play again? (y/n): ");
        } while (scanner.nextLine().trim().equalsIgnoreCase("y"));
        System.out.println("Thanks for playing Hangman!");
    }

    private static void loadWordsFromFile() {
        try {
            words = Files.readAllLines(Paths.get("words.txt"));
            if (words.isEmpty()) {
                throw new IOException("Word list is empty.");
            }
        } catch (IOException e) {
            System.err.println("Error reading words file: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void play(Scanner scanner) {
        initializeGame();

        while (attemptsLeft > 0 && guessedWord.indexOf("_") != -1) {
            System.out.println("\n" + "-".repeat(40));
            System.out.println("Attempts left: " + attemptsLeft);
            drawHangman();
            System.out.println("Word: " + guessedWord);
            System.out.println("Guessed letters: " + guessedLetters);
            System.out.print("Enter your guess: ");
            String input = scanner.nextLine().toLowerCase().trim();

            if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                char guess = input.charAt(0);
                if (guessedLetters.contains(guess)) {
                    System.out.println("You've already guessed that letter!");
                } else {
                    guessedLetters.add(guess);
                    checkGuess(guess);
                }
            } else {
                System.out.println("Invalid input. Please enter a single letter.");
            }
        }

        if (guessedWord.indexOf("_") == -1) {
            System.out.println("\n🎉 Congratulations! You guessed the word: " + word);
        } else {
            drawHangman();
            System.out.println("\n💀 Game over! You ran out of attempts. The word was: " + word);
        }
    }

    private static void initializeGame() {
        word = getRandomWord();
        guessedWord = new StringBuilder(word.replaceAll(".", "_"));
        attemptsLeft = 6;
        guessedLetters = new LinkedHashSet<>();
    }

    private static String getRandomWord() {
        return words.get(new Random().nextInt(words.size())).toLowerCase();
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
            System.out.println("✅ Correct!");
        } else {
            System.out.println("❌ Incorrect!");
            attemptsLeft--;
        }
    }

    private static void drawHangman() {
        String[] stages = {
                """
             _______
             |     |
             |     O
             |    /|\\
             |    / \\
             |
            ---------
            """,
                """
             _______
             |     |
             |     O
             |    /|\\
             |    /
             |
            ---------
            """,
                """
             _______
             |     |
             |     O
             |    /|\\
             |
             |
            ---------
            """,
                """
             _______
             |     |
             |     O
             |    /|
             |
             |
            ---------
            """,
                """
             _______
             |     |
             |     O
             |     |
             |
             |
            ---------
            """,
                """
             _______
             |     |
             |     O
             |
             |
             |
            ---------
            """,
                """
             _______
             |     |
             |
             |
             |
             |
            ---------
            """
        };
        System.out.println(stages[6 - attemptsLeft]);
    }
}