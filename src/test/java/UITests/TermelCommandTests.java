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

public class TermelCommandTests extends AssertJSwingJUnitTestCase {
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
        dummyPE.runFromString("torol endl veletlen ki");

        Grafika frame = GuiActionRunner.execute(() -> new Grafika(dummyPEV, dummyPE));

        window = new FrameFixture(robot(), frame);
        window.show();
        inputField = window.panel("pGridBag").textBox("tfInput");
        button = window.button("bSend");
    }

    @Test
    public void shouldBuildPumpaSuccessfully() {
        inputField.setText("letrehoz ciszterna ci1");
        button.click();

        inputField.setText("termel ci1 pumpa");
        button.click();

        inputField.setText("frissit");
        button.click();
    }

    @Test
    public void shouldBuildCsoSuccessfully() {
        inputField.setText("letrehoz ciszterna ci1");
        button.click();

        inputField.setText("termel ci1 cso");
        button.click();

        inputField.setText("frissit");
        button.click();
    }

    @Test
    public void shouldWarnForInvalidNumberOfParametersInTermel() {
        inputField.setText("termel"); // Hiba: nincs paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A termel parancs két paramétert vár"));

        inputField.setText("termel ci1"); // Hiba: hiányzó paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A termel parancs két paramétert vár"));

        inputField.setText("termel ci1 extra param"); // Hiba: túl sok paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A termel parancs két paramétert vár"));
    }

    @Test
    public void shouldWarnForNonexistentFieldInTermel() {
        inputField.setText("termel nonexistentfield pumpa"); // Hiba: nem létező mező
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű mező: nonexistentfield"));
    }

    @Test
    public void shouldWarnForInvalidObjectTypeInTermel() {
        inputField.setText("letrehoz ciszterna ci1");
        button.click();

        inputField.setText("termel ci1 object"); // Hiba: érvénytelen objektumtípus
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Hibás paraméter. A paraméternek \"pumpa\" vagy \"cso\"-nak kell lennie."));
    }
}
