package UITests;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Settings;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.drukmakor.Grafika;
import org.drukmakor.ParancsErtelmezo;
import org.drukmakor.ParancsErtelmezoView;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.assertTrue;

public class NewUniqueGameFileNotFoundTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @Override
    protected void onSetUp() {
        Settings settings = new Settings();
        settings.delayBetweenEvents(10);
        // Dummy példányok létrehozása
        ParancsErtelmezoView dummyPEV = new ParancsErtelmezoView();
        ParancsErtelmezo dummyPE = new ParancsErtelmezo(dummyPEV);

        dummyPE.runFromFile("commandfiles/alap");
        dummyPE.EnableDebugMode(false);
        dummyPE.OutputToView(true);

        Grafika frame = GuiActionRunner.execute(() -> new Grafika(dummyPEV, dummyPE));

        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Test
    public void shouldOutputFileNotFound() {
        window.button("bNewUniqueGame").click();

        JOptionPaneFixture optionPane = window.optionPane();

        // Keressük meg az input mezőt a dialógusban és írjuk bele a szöveget
        optionPane.textBox().setText("filenamethatdoesnotexist.txt");

        // Kattintsunk az OK gombra a dialógusban
        optionPane.buttonWithText("OK").click();

        String targetOutput = "File nem létezik.";

        String outputText = window.textBox("jtaOutput").text();
        assertTrue("The jtaOutput should contain \"" + targetOutput + "\" \n" +
                "it contains only: " + outputText, outputText.contains(targetOutput));
    }

}
