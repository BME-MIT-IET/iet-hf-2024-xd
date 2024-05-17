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

import static org.junit.Assert.assertTrue;

public class CsofeluletCommandTests extends AssertJSwingJUnitTestCase {
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
        dummyPE.runFromString("torol endl veletlen ki");

        Grafika frame = GuiActionRunner.execute(() -> new Grafika(dummyPEV, dummyPE));

        window = new FrameFixture(robot(), frame);
        window.show();
        inputField = window.panel("pGridBag").textBox("tfInput");
        button = window.button("bSend");
    }

    @Test
    public void shouldWarnForInvalidNumberOfParametersInCsofelulet() {
        inputField.setText("csofelulet"); // Hiba: nincs paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A csofelulet parancs két paramétert vár"));

        inputField.setText("csofelulet cso1"); // Hiba: hiányzó második paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A csofelulet parancs két paramétert vár"));

        inputField.setText("csofelulet cso1 extra1 extra2"); // Hiba: túl sok paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A csofelulet parancs két paramétert vár"));
    }

    @Test
    public void shouldWarnForNonexistentCsoInCsofelulet() {
        inputField.setText("csofelulet nonexistentcso csuszos"); // Hiba: nem létező cső
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű cső: nonexistentcso"));
    }

    @Test
    public void shouldWarnForInvalidSurfaceTypeInCsofelulet() {
        inputField.setText("letrehoz cso cso1");
        button.click();

        inputField.setText("csofelulet cso1 invalidSurface"); // Hiba: érvénytelen felülettípus
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Hibás paraméter. A paraméternek \"csuszos\" vagy \"ragados\"-nak kell lennie."));
    }

    @Test
    public void shouldSetCsoToCsuszos() {
        inputField.setText("letrehoz cso cso1");
        button.click();

        inputField.setText("csofelulet cso1 csuszos");
        button.click();

        inputField.setText("allapot cso1 csuszos");
        button.click();

        // Check output message
        assertTrue(window.textBox("jtaOutput").text().contains("cso1 csuszos: 5"));
    }

    @Test
    public void shouldSetCsoToRagados() {
        inputField.setText("letrehoz cso cso1");
        button.click();

        inputField.setText("csofelulet cso1 ragados");
        button.click();

        inputField.setText("allapot cso1 ragados");
        button.click();



        // Check output message
        assertTrue(window.textBox("jtaOutput").text().contains("cso1 ragados: 5"));
    }
}
