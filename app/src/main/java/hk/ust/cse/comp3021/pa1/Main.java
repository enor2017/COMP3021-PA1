package hk.ust.cse.comp3021.pa1;

import hk.ust.cse.comp3021.pa1.model.GameState;
import hk.ust.cse.comp3021.pa1.util.GameStateSerializer;

import java.io.FileNotFoundException;
import java.nio.file.Path;

/**
 * The main class handling command-line options parsing.
 */
public class Main {

    /**
     * Main entry-point.
     *
     * @param args Arguments from the command-line.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: Main [--unicode] [GAME_FILE]");
            System.out.println();

            System.exit(1);
        }

        final var useUnicodeChars = args[0].equals("--unicode");
        var pathArgIdx = useUnicodeChars ? 1 : 0;

        final GameState gameState;
        try {
            final var pathToLoad = Path.of(args[pathArgIdx]).toAbsolutePath();
            gameState = GameStateSerializer.loadFrom(pathToLoad);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file " + args[0] + " to load", e);
        }

        new InertiaTextGame(gameState, useUnicodeChars).run();
    }
}
