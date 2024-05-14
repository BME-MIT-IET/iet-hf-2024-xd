package UITests;

import org.assertj.swing.core.Settings;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.drukmakor.Grafika;
import org.drukmakor.ParancsErtelmezo;
import org.drukmakor.ParancsErtelmezoView;
import org.junit.Test;

public class InputTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @Override
    protected void onSetUp() {
        // Dummy példányok létrehozása
        ParancsErtelmezoView dummyPEV = new ParancsErtelmezoView();
        ParancsErtelmezo dummyPE = new ParancsErtelmezo(dummyPEV);

        Settings settings = new Settings();
        settings.delayBetweenEvents(10);

        dummyPE.runFromFile("commandfiles/alap");
        dummyPE.EnableDebugMode(false);
        dummyPE.OutputToView(true);

        Grafika frame = GuiActionRunner.execute(() -> new Grafika(dummyPEV, dummyPE));

        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Test
    public void shouldEnterTextIntoInputField() {
        window.panel("pGridBag").textBox("tfInput").enterText("Teszt");

        window.panel("pGridBag").textBox("tfInput").requireText("Teszt");
    }
}
