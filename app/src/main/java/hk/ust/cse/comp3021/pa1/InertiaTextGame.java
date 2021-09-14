package hk.ust.cse.comp3021.pa1;

import hk.ust.cse.comp3021.pa1.controller.GameController;
import hk.ust.cse.comp3021.pa1.model.Direction;
import hk.ust.cse.comp3021.pa1.model.GameState;
import hk.ust.cse.comp3021.pa1.model.MoveResult;
import hk.ust.cse.comp3021.pa1.view.GameView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Objects;

/**
 * The text version of the Inertia game.
 */
public class InertiaTextGame {

    @NotNull
    private static final BufferedReader STDIN_READER = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Controller to mutate the state of the game.
     */
    @NotNull
    private final GameController controller;
    /**
     * View to inspect the state of the game.
     */
    @NotNull
    private final GameView view;

    /**
     * Whether to output the game board using Unicode characters.
     */
    private final boolean useUnicodeChars;

    /**
     * Creates a new instance using the provided options.
     *
     * @param gameState The initial game state to create a game from.
     * @param useUnicodeChars Whether to output the game board using Unicode characters.
     */
    public InertiaTextGame(@NotNull final GameState gameState, final boolean useUnicodeChars) {
        Objects.requireNonNull(gameState);

        this.controller = new GameController(gameState);
        this.view = new GameView(gameState);

        this.useUnicodeChars = useUnicodeChars;
    }

    /**
     * Runs the main loop of the game.
     */
    public void run() {
        while (!view.hasWon()) {
            view.output(useUnicodeChars);

            final var userInput = waitUserInput("Enter Your Move (UP/DOWN/LEFT/RIGHT/UNDO/QUIT): ", false);
            if ("quit".startsWith(userInput.toLowerCase(Locale.ENGLISH))) {
                break;
            }

            final var direction = parseDirection(userInput);
            if (direction == null) {
                if ("undo".startsWith(userInput.toLowerCase(Locale.ENGLISH))) {
                    final var result = controller.processUndo();
                    if (!result) {
                        System.out.println("No more steps to undo!");
                    }
                    continue;
                }

                System.out.println("Invalid choice!");
                waitUserInput("Press [ENTER] to continue...", true);
                continue;
            }

            final var moveResult = controller.processMove(direction);
            if (moveResult instanceof MoveResult.Invalid) {
                System.out.println("Invalid move!");
                waitUserInput("Press [ENTER] to continue...", true);
            } else if (moveResult instanceof MoveResult.Valid.Dead) {
                System.out.println("You died!");

                if (view.hasLost()) {
                    break;
                }

                waitUserInput("Press [ENTER] to continue...", true);
            }
        }

        if (view.hasWon()) {
            System.out.println("You win!");
        } else if (view.hasLost()) {
            System.out.println("You lost!");
        }
    }

    /**
     * Parses the input string as a direction.
     *
     * <p>
     * This implementation only requires the input string to match the prefix of a direction. For example, all following
     * strings will match with {@link Direction#UP}:
     *
     * <ul>
     *     <li>{@code U}</li>
     *     <li>{@code UP}</li>
     *     <li>{@code u}</li>
     *     <li>{@code up}</li>
     * </ul>
     * <p>
     * The matching is also case-insensitive.
     * </p>
     *
     * @param input The string input to parse as a direction.
     * @return The parsed {@link Direction}, or {@code null} if the direction cannot be determined from the input
     * string.
     */
    @Nullable
    private static Direction parseDirection(@NotNull final String input) {
        Objects.requireNonNull(input);

        if (input.isBlank()) {
            return null;
        }

        final var inputUpperCase = input.toUpperCase(Locale.ENGLISH);

        for (@NotNull final var dir : Direction.values()) {
            if (dir.toString().startsWith(inputUpperCase)) {
                return dir;
            }
        }

        return null;
    }

    /**
     * Outputs a prompt messages, and wait for the user's input.
     *
     * @param prompt  The prompt message to display to the user.
     * @param newLine Whether to insert a new-line character ({@code \n}) before reading from the user input.
     * @return The user's response.
     */
    @NotNull
    private static String waitUserInput(@Nullable final String prompt, boolean newLine) {
        if (newLine) {
            System.out.println(prompt);
        } else {
            System.out.print(prompt);
        }

        try {
            return STDIN_READER.readLine();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
