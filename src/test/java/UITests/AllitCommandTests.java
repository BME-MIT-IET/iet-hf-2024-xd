package UITests;

import org.assertj.swing.core.Settings;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.drukmakor.Grafika;
import org.drukmakor.ObjectView;
import org.drukmakor.ParancsErtelmezo;
import org.drukmakor.ParancsErtelmezoView;
import org.junit.Test;

import static org.junit.Assert.*;

public class AllitCommandTests extends AssertJSwingJUnitTestCase {
    private FrameFixture window;
    JButtonFixture button;
    JTextComponentFixture inputField;

    @Override
    protected void onSetUp() {
        Settings settings = new Settings();
        settings.delayBetweenEvents(10);

        ParancsErtelmezoView dummyPEV = new ParancsErtelmezoView();
        ParancsErtelmezo dummyPE = new ParancsErtelmezo(dummyPEV);

        dummyPE.EnableDebugMode(true);
        dummyPE.OutputToView(true);

        dummyPE.EnableDebugMode(true);
        dummyPE.runFromString("torol");

        Grafika frame = GuiActionRunner.execute(() -> new Grafika(dummyPEV, dummyPE));

        window = new FrameFixture(robot(), frame);
        window.show();
        inputField = window.panel("pGridBag").textBox("tfInput");
        button = window.button("bSend");
    }

    @Test
    public void shouldWarnIfInvalidNumberOfParametersForAllit() {
        inputField.setText("letrehoz pumpa p1");
        button.click();

        inputField.setText("allit player1 p1");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Az allit parancs 3 paramétert vár. (allit <jatekos> <bemenet> <kimenet>)"));
    }

    @Test
    public void shouldWarnIfNonExistentPlayerForAllit() {inputField.setText("allit player1 bemenet kimenet");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű játékos: player1"));
    }

    @Test
    public void shouldWarnIfNonExistentBemenetForAllit() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        inputField.setText("alwaysdebug");
        button.click();

        inputField.setText("letrehoz szerelo sz1");
        button.click();

        inputField.setText("allit sz1 bemenet kimenet");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű mező: bemenet"));
    }

    @Test
    public void shouldWarnIfNonExistentKimenetForAllit() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        inputField.setText("alwaysdebug");
        button.click();

        inputField.setText("letrehoz szerelo sz1");
        button.click();

        inputField.setText("letrehoz pumpa p1");
        button.click();

        inputField.setText("allit sz1 p1 kimenet");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű mező: kimenet"));
    }

    @Test
    public void shouldPerformAllitWithValidParameters() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        inputField.setText("alwaysdebug");
        button.click();

        inputField.setText("letrehoz szerelo sz1");
        button.click();

        inputField.setText("letrehoz pumpa p1");
        button.click();

        inputField.setText("letrehoz cso c1");
        button.click();

        inputField.setText("letrehoz cso c2");
        button.click();

        inputField.setText("allit sz1 c1 c2");

        assertTrue(inputField.text().equals("allit sz1 c1 c2"));
        button.click();
    }
}
