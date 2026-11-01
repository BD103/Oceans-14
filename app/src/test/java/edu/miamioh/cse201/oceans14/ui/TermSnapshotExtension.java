package edu.miamioh.cse201.oceans14.ui;

import org.jline.builtins.WebTerminal;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;

/**
 * A JUnit extension that implements snapshot testing for the terminal.
 * <p>
 * You can enable snapshot testing for a class by annotating it with
 * {@code @ExtendWith(SnapshotTerminalExtension.class)}. You can then create a new snapshot test by
 * making a new test method with the parameter {@code @TermSnapshot Terminal term}.
 *
 * <h1>Examples</h1>
 *
 * <pre>
 * {@code
 * @ExtendWith(SnapshotTerminalExtension.class)
 * public class MySnapshotTestClass {
 *     @Test
 *     void mySnapshotTest(@TermSnapshot Terminal term) {
 *         term.writer().print("Hello, world!");
 *     }
 * }
 * }
 * </pre>
 * <p>
 * To commit this snapshot, run {@code BLESS=1 ./gradlew test}. Afterward, the test will succeed as
 * long as "Hello, world!" remains printed to the terminal. Printing more text or changing
 * "Hello, world!" will cause the test to fail.
 * <p>
 * You can modify the terminal screen size with {@code @TermSnapshot(width = 100, height = 30)}.
 */
public class TermSnapshotExtension implements BeforeAllCallback, ParameterResolver, AfterEachCallback, TestInstancePreDestroyCallback {
    /**
     * The path to the snapshot folder.
     */
    private final static File snapshotFolder = new File("src/test/resources/snapshots");
    /**
     * A map that associates {@link ExtensionContext#getUniqueId()} with {@link Snapshot}s.
     */
    private final Map<String, Snapshot> snapshots = new HashMap<>();
    /**
     * True if we're blessing the current output, false if we're checking against the historical output.
     */
    private boolean bless;

    @Override
    public void beforeAll(ExtensionContext ec) {
        // Make sure folder containing snapshots exists.
        snapshotFolder.mkdirs();

        String blessEnv = System.getenv("BLESS");
        bless = !(blessEnv == null || blessEnv.isEmpty() || blessEnv.equals("0"));
    }

    @Override
    public boolean supportsParameter(ParameterContext pc, ExtensionContext ec) {
        // Support parameters that look like `@TermSnapshot Terminal term`.
        return pc.isAnnotated(TermSnapshot.class) && pc.getParameter().getType() == Terminal.class;
    }

    @Override
    public Object resolveParameter(ParameterContext pc, ExtensionContext ec) throws ParameterResolutionException {
        // Get the options passed to `@TermSnapshot`.
        TermSnapshot termSnapshot = pc.findAnnotation(TermSnapshot.class).get();

        // A snapshot's output is stored at `src/test/resources/snapshots/TestClassName.testMethodName.html`.
        File path = new File(snapshotFolder, ec.getRequiredTestClass().getSimpleName() + "." + ec.getRequiredTestMethod().getName() + ".html");

        try {
            // Create the terminal, which captures terminal operations and will eventually render
            // the HTML output. Even though we pass in a host and port, the web server will never
            // be started.
            WebTerminal term = new WebTerminal("localhost", 0, termSnapshot.width(), termSnapshot.height());

            // Insert our snapshot into our lookup map so it can be accessed by `afterEach()`.
            if (this.snapshots.putIfAbsent(ec.getUniqueId(), new Snapshot(term, path)) != null) {
                throw new ParameterResolutionException("Multiple `Terminal` parameters are not allowed in snapshot tests");
            }

            // Pass the created terminal to the test method.
            return term;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterEach(ExtensionContext ec) throws Exception {
        Snapshot snapshot = this.snapshots.get(ec.getUniqueId());

        // If the snapshot is null, then this is not a snapshot test. Exit.
        if (snapshot == null) {
            return;
        }

        // Make sure terminal buffer is flushed to the "screen".
        snapshot.term.flush();

        // Render the terminal's "screen" as HTML, normalizing line endings so Windows matches
        // Unix's behavior.
        String currentOutput = snapshot.term.dump(0, true)
                .replace("\r\n", "\n");

        if (this.bless) {
            Files.writeString(snapshot.path.toPath(), currentOutput);
        } else {
            try {
                String previousOutput = Files.readString(snapshot.path.toPath());
                Assertions.assertEquals(previousOutput, currentOutput, () -> "Current output does not match snapshot, run `BLESS=1 ./gradlew test` to bless the current output.");
            } catch (NoSuchFileException e) {
                Assertions.fail("No existing snapshot found at " + snapshot.path + ", run `BLESS=1 ./gradlew test` to bless the current output.");
            }
        }
    }

    @Override
    public void preDestroyTestInstance(ExtensionContext ec) throws Exception {
        for (Snapshot snapshot : this.snapshots.values()) {
            snapshot.term.close();
        }
    }

    private record Snapshot(WebTerminal term, File path) {
    }
}
