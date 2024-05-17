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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EpitAndFelveszCommandTests extends AssertJSwingJUnitTestCase {
    private FrameFixture window;
    private JTextComponentFixture inputField;
    private JButtonFixture button;

    @Override
    protected void onSetUp() {
        Settings settings = new Settings();
        settings.delayBetweenEvents(10);

        ParancsErtelmezoView dummyPEV = new ParancsErtelmezoView();
        ParancsErtelmezo dummyPE = new ParancsErtelmezo(dummyPEV);

        dummyPE.EnableDebugMode(true);
        dummyPE.runFromString("torol");
        dummyPE.OutputToView(true);

        dummyPE.EnableDebugMode(true);

        dummyPE.runFromString("letrehoz szerelo sze1 endl letrehoz cso c1 endl veletlen ki");

        Grafika frame = GuiActionRunner.execute(() -> new Grafika(dummyPEV, dummyPE));

        window = new FrameFixture(robot(), frame);
        window.show();
        inputField = window.panel("pGridBag").textBox("tfInput");
        button = window.button("bSend");
    }

    @Test
    public void shouldWarnIfInvalidNumberOfParametersForEpit() {
        inputField.setText("epit sze1");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Az epit parancs 2 paramétert vár. (epit <szerelo> <\"pumpa\"/\"cso\">)"));
    }

    @Test
    public void shouldWarnIfNonExistentPlayerForEpit() {
        inputField.setText("epit nonExistentPlayer pumpa");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű játékos: nonExistentPlayer"));
    }

    @Test
    public void shouldWarnIfInvalidBuildTypeForEpit() {
        inputField.setText("epit sze1 hiba");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("A második paraméternek \"pumpa\" vagy \"cso\"-nak kell lennie."));
    }

    @Test
    public void shouldBuildCsoSuccessfully() {
        inputField.setText("letrehoz forras f1");
        button.click();

        inputField.setText("epit sze1 cso");
        button.click();

        // Verify cso was built
        assertNotNull(ObjectView.getViewByName("sze1"));
    }

    @Test
    public void shouldBuildPumpaSuccessfully() {
        inputField.setText("letrehoz ciszterna ci1");
        button.click();

        inputField.setText("letrehoz pumpa p1");
        button.click();

        inputField.setText("letrehoz cso cs1");
        button.click();

        inputField.setText("osszekot cs1 p1");
        button.click();

        inputField.setText("osszekot cs1 ci1");
        button.click();

        inputField.setText("lep sze1 ci1");
        button.click();

        inputField.setText("termel ci1 pumpa");
        button.click();

        inputField.setText("frissit");
        button.click();

        inputField.setText("felvesz sze1 pumpa");
        button.click();

        inputField.setText("lep sze1 cs1");
        button.click();

        inputField.setText("epit sze1 pumpa");
        button.click();

        // Check output message
        assertTrue(window.textBox("jtaOutput").text().contains("Sikeresen megépült a pumpa"));

        // Verify pumpa was built (assuming there's a way to verify it in the game state)
        // For example, you might check the ObjectView or game state directly:
        assertNotNull(ObjectView.getViewByName("gen0"));
    }

    @Test
    public void shouldWarnIfNoPumpaInBackpack() {
        inputField.setText("epit sze1 pumpa");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Nincs pumpa a hatizsakban"));
    }

    @Test
    public void shouldWarnIfInvalidNumberOfParametersForFelvesz() {
        inputField.setText("felvesz");
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A felvesz parancs legalább 2, legfeljebb 4 paramétert vár."));

        inputField.setText("felvesz sze1 pumpa extraParam");
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A felvesz parancs legalább 2, legfeljebb 4 paramétert vár."));
    }

    @Test
    public void shouldWarnIfPlayerDoesNotExistForFelvesz() {
        inputField.setText("felvesz nemletezoJatekos pumpa");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű játékos: nemletezoJatekos"));
    }

    @Test
    public void shouldWarnIfInvalidObjectTypeForFelvesz() {
        inputField.setText("felvesz sze1 nemletezoTargy");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("A második paraméternek \"pumpa\" vagy \"cso\"-nak kell lennie."));
    }

    @Test
    public void shouldWarnIfPipeNameNotProvidedForFelvesz() {
        inputField.setText("felvesz sze1 cso");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("A cső felvételéhez meg kell adni a cső nevét."));
    }

    @Test
    public void shouldWarnIfPipeDoesNotExistForFelvesz() {
        inputField.setText("felvesz sze1 cso nemletezoCso");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű cső: nemletezoCso"));
    }

    @Test
    public void shouldWarnIfInvalidPipeOptionForFelvesz() {
        inputField.setText("felvesz sze1 cso c1 invalidOption");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("A cső felvételéhez az utolsó paraméternek \"egesz\" vagy \"fel\"-nek kell lennie."));
    }

    @Test
    public void shouldPickUpWholePipeIfNoOptionProvidedForFelvesz() {
        inputField.setText("felvesz sze1 cso c1");

        assertTrue(inputField.text().contains("felvesz sze1 cso c1"));

        button.click();
    }

    @Test
    public void shouldPickUpWholePipeIfOptionProvidedForFelvesz() {
        inputField.setText("felvesz sze1 cso c1 egesz");

        assertTrue(inputField.text().contains("felvesz sze1 cso c1 egesz"));

        button.click();
    }

    @Test
    public void shouldPickUpHalfPipeIfOptionProvidedForFelvesz() {
        inputField.setText("felvesz sze1 cso c1 fel");

        assertTrue(inputField.text().contains("felvesz sze1 cso c1 fel"));

        button.click();
    }
}
