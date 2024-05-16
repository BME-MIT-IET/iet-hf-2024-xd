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

public class NextRoundAndWaterflowTests extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    JTextComponentFixture inputField;
    JButtonFixture button;

    @Override
    protected void onSetUp() {
        Settings settings = new Settings();
        settings.delayBetweenEvents(10);

        ParancsErtelmezoView dummyPEV = new ParancsErtelmezoView();
        ParancsErtelmezo dummyPE = new ParancsErtelmezo(dummyPEV);

        dummyPE.EnableDebugMode(true);
        dummyPE.runFromString("torol endl veletlen ki");
        dummyPE.OutputToView(true);

        dummyPE.EnableDebugMode(true);

        Grafika frame = GuiActionRunner.execute(() -> new Grafika(dummyPEV, dummyPE));

        window = new FrameFixture(robot(), frame);
        window.show();
        inputField = window.panel("pGridBag").textBox("tfInput");
        button = window.button("bSend");
    }
    @Test
    public void shouldWarnIfParametersProvidedForFrissit() {
        inputField.setText("frissit param1");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Figyelmeztetés: A frissit parancs nem vár paramétert. A parancs ettől függetlenül lefutott."));
    }

    @Test
    public void shouldDeclareWinnerAtRound20() {
        for (int i = 0; i < 20; i++) {
            inputField.setText("frissit");
            button.click();
        }

        assertTrue(window.textBox("jtaOutput").text().contains("A játéknak vége!"));
        assertTrue(window.textBox("jtaOutput").text().contains("Nyertes:"));
    }
    @Test
    public void shouldWarnForInvalidNumberOfParametersInVizmennyiseg() {
        inputField.setText("vizmennyiseg"); // Hiba: nincs paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A vizmennyiseg parancs két paramétert vár"));

        inputField.setText("vizmennyiseg cso1"); // Hiba: hiányzó második paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A vizmennyiseg parancs két paramétert vár"));

        inputField.setText("vizmennyiseg cso1 10 extra"); // Hiba: túl sok paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("A vizmennyiseg parancs két paramétert vár"));
    }

    @Test
    public void shouldWarnForNonexistentCsoOrPumpaInVizmennyiseg() {
        inputField.setText("vizmennyiseg nonexistentcso 10"); // Hiba: nem létező cső vagy pumpa
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű cső vagy pumpa: nonexistentcso"));
    }

    @Test
    public void shouldWarnForInvalidAmountInVizmennyiseg() {
        inputField.setText("letrehoz cso cso1");
        button.click();

        inputField.setText("vizmennyiseg cso1 invalidAmount"); // Hiba: érvénytelen mennyiség
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Hibás a második paraméter. A paraméternek számnak kell lennie."));
    }

    @Test
    public void shouldSetVizmennyisegForCso() {
        inputField.setText("letrehoz cso cso1");
        button.click();

        inputField.setText("vizmennyiseg cso1 5");
        button.click();


        inputField.setText("allapot cso1 vizmennyiseg");
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("cso1 vizmennyiseg: 5"));
    }

    @Test
    public void shouldSetVizmennyisegForPumpa() {
        inputField.setText("letrehoz pumpa pumpa1");
        button.click();

        inputField.setText("vizmennyiseg pumpa1 5");
        button.click();

        inputField.setText("allapot pumpa1 vizmennyiseg");
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("pumpa1 vizmennyiseg: 5"));
    }
}
