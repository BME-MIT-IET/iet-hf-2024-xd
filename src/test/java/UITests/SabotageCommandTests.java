package UITests;

import org.assertj.swing.core.Settings;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.drukmakor.Grafika;
import org.drukmakor.ParancsErtelmezo;
import org.drukmakor.ParancsErtelmezoView;
import org.junit.Test;

import static org.junit.Assert.*;

public class SabotageCommandTests extends AssertJSwingJUnitTestCase {
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

        Grafika frame = GuiActionRunner.execute(() -> new Grafika(dummyPEV, dummyPE));

        window = new FrameFixture(robot(), frame);
        window.show();
        inputField = window.panel("pGridBag").textBox("tfInput");
        button = window.button("bSend");
    }

    @Test
    public void shouldWarnForInvalidNumberOfParametersInCsuszik() {
        inputField.setText("csuszik"); // Hiba: nincs paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A csuszik parancs egy paramétert vár"));

        inputField.setText("csuszik player1 extra"); // Hiba: túl sok paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A csuszik parancs egy paramétert vár"));
    }

    @Test
    public void shouldWarnForNonexistentPlayerInCsuszik() {
        inputField.setText("csuszik nonexistingplayer");
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű játékos: nonexistingplayer"));
    }

    @Test
    public void shouldSetPlayerSlipperyInCsuszik() {
        inputField.setText("letrehoz jatekos player1");
        button.click();
        inputField.setText("csuszik player1");
        button.click();
    }

    @Test
    public void shouldWarnForInvalidNumberOfParametersInRagad() {
        inputField.setText("ragad"); // Hiba: nincs paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A ragad parancs egy paramétert vár"));

        inputField.setText("ragad player1 extra"); // Hiba: túl sok paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A ragad parancs egy paramétert vár"));
    }

    @Test
    public void shouldWarnForNonexistentPlayerInRagad() {
        inputField.setText("ragad nonexistingplayer"); // Hiba: nem létező játékos
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű játékos: nonexistingplayer"));
    }

    @Test
    public void shouldSetPlayerStickyInRagad() {
        inputField.setText("letrehoz jatekos player1");
        button.click();
        inputField.setText("ragad player1"); // Játékos ragadóssá tétele
        button.click();
    }

    @Test
    public void shouldWarnForInvalidNumberOfParametersInElront() {
        inputField.setText("elront"); // Hiba: nincs paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Az elront parancs egy paramétert vár"));

        inputField.setText("elront field1 extra"); // Hiba: túl sok paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Az elront parancs egy paramétert vár"));
    }

    @Test
    public void shouldWarnForNonexistentFieldInElront() {
        inputField.setText("elront nonexistentfield"); // Hiba: nem létező mező
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű mező: nonexistentfield"));
    }

    @Test
    public void shouldSetFieldNotWorkingInElront() {
        inputField.setText("letrehoz mezo field1");
        button.click();
        inputField.setText("elront field1");
        button.click();
    }

}
