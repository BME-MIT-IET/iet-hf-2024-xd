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

public class RepairAndLyukasztCommandTests extends AssertJSwingJUnitTestCase {
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
    public void shouldWarnIfInvalidNumberOfParametersForSzerel() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        inputField.setText("szerel player1 extraParam");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("A szerel parancs 1 paramétert vár. (szerel <jatekos>)"));
    }

    @Test
    public void shouldWarnIfNonExistentPlayerForSzerel() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        inputField.setText("szerel player1");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű játékos: player1"));
    }

    @Test
    public void shouldPerformRepairWithValidPlayer() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        inputField.setText("alwaysdebug");
        button.click();

        inputField.setText("letrehoz szerelo sz1");
        button.click();

        inputField.setText("szerel sz1");
        assertTrue(inputField.text().contains("szerel sz1"));
        button.click();
    }

    @Test
    public void shouldWarnIfInvalidNumberOfParametersForLyukaszt() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        inputField.setText("lyukaszt player1 extraParam");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("A lyukaszt parancs 1 paramétert vár. (lyukaszt <jatekos>)"));
    }

    @Test
    public void shouldWarnIfNonExistentPlayerForLyukaszt() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        inputField.setText("lyukaszt player1");
        button.click();

        assertTrue(window.textBox("jtaOutput").text().contains("Nincs ilyen nevű játékos: player1"));
    }

    @Test
    public void shouldPerformLyukasztWithValidPlayer() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        inputField.setText("alwaysdebug");
        button.click();

        inputField.setText("letrehoz szabotor sz1");
        button.click();

        inputField.setText("lyukaszt sz1");
        assertTrue(inputField.text().contains("lyukaszt sz1"));
        button.click();
    }

}
