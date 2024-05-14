package UITests;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.drukmakor.Grafika;
import org.drukmakor.ParancsErtelmezo;
import org.drukmakor.ParancsErtelmezoView;
import org.junit.Test;
import org.assertj.swing.core.Settings;
import java.awt.*;
import static org.junit.Assert.assertTrue;

public class DarkModeTest extends AssertJSwingJUnitTestCase {
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
    public void shouldTurnOnDarkMode() {
        // A sötét mód bekapcsolása
        window.panel("pGridBag").textBox("tfInput").enterText("dark");
        window.button("bSend").click();

        // Fő panel

        assertTrue(window.textBox("jtaOutput").background().target().equals(new Color(80, 80, 80)));

        assertTrue(window.panel("pGridBag").background().target().equals(new Color(150, 150, 150)));

        assertTrue(window.button("bSend").background().target().equals(new Color(100, 100, 100)));

        // Új játék gomb
        assertTrue(window.button("bNewGame").background().target().equals(new Color(100, 100, 100)));

        // Új egyedi pálya betöltése gomb
        assertTrue(window.button("bNewUniqueGame").background().target().equals(new Color(100, 100, 100)));

        // Súgó gomb
        assertTrue(window.button("bHelp").background().target().equals(new Color(100, 100, 100)));

        assertTrue(window.panel("pCantSee").background().target().equals(new Color(100, 100, 100)));

        // Segédpanel 1
        assertTrue(window.panel("pCantSee2").background().target().equals(new Color(100, 100, 100)));

        // Bemeneti mező
        assertTrue(window.textBox("tfInput").background().target().equals(new Color(100, 100, 100)));
    }

}
