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
import java.awt.*;

import static org.junit.Assert.assertTrue;

public class DuneMessageTest extends AssertJSwingJUnitTestCase {
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
    public void shouldShowDuneMessageDialog() {
        // Beírjuk az input mezőbe a "Dune" parancsot
        window.panel("pGridBag").textBox("tfInput").enterText("Dune");
        window.button("bSend").click();
        window.button("bHelp").click();

        // Ellenőrizzük, hogy a megfelelő dialógus szöveg megjelenik
        JOptionPaneFixture optionPane = window.optionPane();

        String expectedMessage = "“I must not fear.\n" +
                "Fear is the mind-killer.\n" +
                "Fear is the little-death that brings total obliteration.\n" +
                "I will face my fear.\n" +
                "I will permit it to pass over me and through me.\n" +
                "And when it has gone past I will turn the inner eye to see its path.\n" +
                "Where the fear has gone there will be nothing. Only I will remain.”\n -- Bene Gesserit Litany";

        // Keresd meg az összes JLabel komponenst és ellenőrizd, hogy tartalmazza-e a kívánt szöveget
        assertTrue(
                optionPane.robot().finder().findAll(new GenericTypeMatcher<JLabel>(JLabel.class) {
                            @Override
                            protected boolean isMatching(JLabel label) {
                                return label.isShowing() && label.getText() != null;
                            }
                        }).stream()
                        .allMatch(label -> expectedMessage.contains(label.getText()))
        );
        window.dialog().button().click();
    }
}