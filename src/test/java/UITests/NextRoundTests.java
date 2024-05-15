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

import static org.junit.Assert.assertTrue;

public class NextRoundTests extends AssertJSwingJUnitTestCase {
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
        dummyPE.runFromString("torol");
        dummyPE.OutputToView(true);

        dummyPE.EnableDebugMode(true);

        Grafika frame = GuiActionRunner.execute(() -> new Grafika(dummyPEV, dummyPE));

        window = new FrameFixture(robot(), frame);
        window.show();
        inputField = window.panel("pGridBag").textBox("tfInput");
        button = window.button("bSend");
    }
    @Test
    public void shouldZarnIfParametersProvidedForFrissit() {
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
}
