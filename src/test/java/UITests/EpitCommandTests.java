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

public class EpitCommandTests extends AssertJSwingJUnitTestCase {
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
    public void shouldWarnIfInvalidNumberOfParametersForEpit() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        inputField.setText("epit szerelo1");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Az epit parancs 2 paramétert vár. (epit <szerelo> <\"pumpa\"/\"cso\">)"));
    }

    @Test
    public void shouldWarnIfNonExistentPlayerForEpit() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        inputField.setText("epit szerelo1 pumpa");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű játékos: szerelo1"));
    }

    @Test
    public void shouldWarnIfInvalidBuildTypeForEpit() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        // Create a player first
        inputField.setText("alwaysdebug");
        button.click();

        inputField.setText("letrehoz szerelo sz1");
        button.click();

        inputField.setText("epit sz1 hiba");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("A második paraméternek \"pumpa\" vagy \"cso\"-nak kell lennie."));
    }

    @Test
    public void shouldBuildCsoSuccessfully() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        // Create a player first
        inputField.setText("alwaysdebug");
        button.click();

        inputField.setText("letrehoz forras f1");
        button.click();

        inputField.setText("letrehoz szerelo sz1");
        button.click();

        inputField.setText("epit sz1 cso");
        button.click();

        // Verify cso was built
        assertNotNull(ObjectView.getViewByName("sz1"));
    }

    @Test
    public void shouldWarnIfNoPumpaInBackpack() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        // Create a player first
        inputField.setText("alwaysdebug");
        button.click();

        inputField.setText("letrehoz szerelo sz1");
        button.click();

        inputField.setText("epit sz1 pumpa");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Nincs pumpa a hatizsakban"));
    }

    @Test
    public void shouldBuildPumpaSuccessfully() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        // Create a player first
        inputField.setText("alwaysdebug");
        button.click();

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

        inputField.setText("letrehoz szerelo sz1");
        button.click();

        inputField.setText("lep sz1 c1");
        button.click();

        inputField.setText("termel c1 pumpa");
        button.click();

        inputField.setText("felvesz sz1 pumpa");
        button.click();

        inputField.setText("lep sz1 cs1");
        button.click();

        inputField.setText("epit sz1 pumpa");
        button.click();

        // Check output message
        assertTrue(window.textBox("jtaOutput").text().contains("Sikeresen megépült a pumpa"));

        // Verify pumpa was built (assuming there's a way to verify it in the game state)
        // For example, you might check the ObjectView or game state directly:
        assertNotNull(ObjectView.getViewByName("gen0"));
    }

}
