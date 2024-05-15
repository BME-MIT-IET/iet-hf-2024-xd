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

public class FileLoadingAndStateTests extends AssertJSwingJUnitTestCase {
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
    public void shouldWarnIfInvalidNumberOfParametersForTolt() {
        // Beállítjuk a teszt környezetet
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        // Tesztelt parancs hívása rossz paraméterekkel
        inputField.setText("tolt");
        button.click();

        // Ellenőrizzük az elvárt hibaüzenetet
        assertTrue(window.textBox("jtaOutput").text().contains("A tolt parancs egy paramétert vár. (tolt <fajlnev>)"));
    }

    @Test
    public void shouldWarnIfFileNotFoundForTolt() {
        // Beállítjuk a teszt környezetet
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        // Tesztelt parancs hívása egy nem létező fájlra
        inputField.setText("tolt non_existing_file");
        button.click();

        // Ellenőrizzük az elvárt hibaüzenetet
        assertTrue(window.textBox("jtaOutput").text().contains("Hiba a fájl beolvasásakor!"));
    }

    @Test
    public void shouldPrintWarningForInvalidNumberOfParameters() {
        inputField.setText("allapot"); // Hiba: nincs paraméter
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Az allapot parancs két vagy három paramétert vár"));

        inputField.setText("allapot player1"); // Hiba: hiányzó attribútum
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Az allapot parancs két vagy három paramétert vár"));

        inputField.setText("allapot player1 pumpaHatizsak"); // Hiba: hiányzó fájlnév
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Az allapot parancs két vagy három paramétert vár"));
    }

    @Test
    public void shouldPrintWarningForNonexistentObject() {
        inputField.setText("allapot cs1 *"); // Hiba: cs1 nem létező objektum
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű objektum: cs1"));

        inputField.setText("allapot player1 *"); // Hiba: player1 nem létező játékos
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű objektum: player1"));
    }

    @Test
    public void shouldPrintWarningForNonexistentAttribute() {
        inputField.setText("allapot * nonexistentAttribute");
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Figyelmeztetés: Ha az első paraméter csillag," +
                " akkor a második paraméternek is csillagnak kéne lennie. A parancs ettől függetlenül lefutott. (Kiírtuk az egész pályát)"));

        inputField.setText("letrehoz cso cs1"); // Hiba: xyz nem attribútum
        button.click();

        inputField.setText("allapot cs1 xyz"); // Hiba: xyz nem attribútum
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen attribútum: xyz"));
    }

    @Test
    public void shouldPrintObjectStateForExistingObject() {
        inputField.setText("letrehoz ciszterna c1");
        button.click();
        inputField.setText("letrehoz pumpa p1");
        button.click();
        inputField.setText("letrehoz cso cs1");
        button.click();
        inputField.setText("osszekot cs1 p1");
        button.click();
        inputField.setText("osszekot cs1 c1");
        button.click();
        inputField.setText("allapot c1 *"); // Csatorna c1 összes attribútuma
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("c1 mukodik: true"));
        assertTrue(window.textBox("jtaOutput").text().contains("c1 maxJatekosok: 2147483647"));
        assertTrue(window.textBox("jtaOutput").text().contains("c1 maxSzomszedok: 20"));
        assertTrue(window.textBox("jtaOutput").text().contains("c1 jatekosok:"));
        assertTrue(window.textBox("jtaOutput").text().contains("c1 vizmennyiseg: 0"));
        assertTrue(window.textBox("jtaOutput").text().contains("c1 termeltPumpak:"));
    }

    @Test
    public void shouldPrintObjectAttributeForExistingObject() {
        inputField.setText("letrehoz ciszterna c1");
        button.click();
        inputField.setText("allapot c1 vizmennyiseg"); // Csatorna c1 vízmennyisége
        button.click();
        assertTrue(window.textBox("jtaOutput").text().contains("c1 vizmennyiseg: 0"));
    }
}
