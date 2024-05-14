package UITests;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Settings;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.drukmakor.Grafika;
import org.drukmakor.ObjectView;
import org.drukmakor.ParancsErtelmezo;
import org.drukmakor.ParancsErtelmezoView;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class CommandInputTests extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @Override
    protected void onSetUp() {
        Settings settings = new Settings();
        settings.delayBetweenEvents(10);

        ParancsErtelmezoView dummyPEV = new ParancsErtelmezoView();
        ParancsErtelmezo dummyPE = new ParancsErtelmezo(dummyPEV);

        dummyPE.EnableDebugMode(false);
        dummyPE.OutputToView(true);

        dummyPE.EnableDebugMode(true);

        Grafika frame = GuiActionRunner.execute(() -> new Grafika(dummyPEV, dummyPE));

        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Test
    public void shouldEnterTextIntoInputField() {
        window.panel("pGridBag").textBox("tfInput").enterText("Teszt");

        window.panel("pGridBag").textBox("tfInput").requireText("Teszt");
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

    @Test
    public void shouldCreateOneOfEachItem() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        String commands = String.join(" endl ",
                "letrehoz cso cs1",
                "letrehoz pumpa p1",
                "letrehoz ciszterna c1",
                "letrehoz forras f1",
                "letrehoz szabotor sza1",
                "letrehoz szerelo sze1"
        );

        inputField.setText(commands);
        button.click();

        assertNotNull(ObjectView.getViewByName("cs1"));
        assertNotNull(ObjectView.getViewByName("p1"));
        assertNotNull(ObjectView.getViewByName("c1"));
        assertNotNull(ObjectView.getViewByName("f1"));
        assertNotNull(ObjectView.getViewByName("sza1"));
        assertNotNull(ObjectView.getViewByName("sze1"));

        // Ensure no unexpected views are present
        assertNull(ObjectView.getViewByName("cs2"));
        assertNull(ObjectView.getViewByName("p2"));
        assertNull(ObjectView.getViewByName("c2"));
        assertNull(ObjectView.getViewByName("f2"));
        assertNull(ObjectView.getViewByName("sza2"));
        assertNull(ObjectView.getViewByName("sze2"));
    }

    @Test
    public void shouldClearAllViews() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        String commands = String.join(" endl ",
                "letrehoz cso cs1",
                "letrehoz pumpa p1",
                "letrehoz ciszterna c1",
                "letrehoz forras f1",
                "letrehoz szabotor sza1",
                "letrehoz szerelo sze1",
                "torol"
        );

        inputField.setText(commands);
        button.click();

        // Verify all views are removed
        assertNull(ObjectView.getViewByName("cs1"));
        assertNull(ObjectView.getViewByName("p1"));
        assertNull(ObjectView.getViewByName("c1"));
        assertNull(ObjectView.getViewByName("f1"));
        assertNull(ObjectView.getViewByName("sza1"));
        assertNull(ObjectView.getViewByName("sze1"));
    }
}
