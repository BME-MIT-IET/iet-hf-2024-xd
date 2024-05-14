package UITests;

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
import org.assertj.swing.core.Settings;

import static org.junit.Assert.*;

public class FieldAndPlayerLifecycleTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @Override
    protected void onSetUp() {
        Settings settings = new Settings();
        settings.delayBetweenEvents(10);

        ParancsErtelmezoView dummyPEV = new ParancsErtelmezoView();
        ParancsErtelmezo dummyPE = new ParancsErtelmezo(dummyPEV);

        dummyPE.EnableDebugMode(false);
        dummyPE.OutputToView(true);

        Grafika frame = GuiActionRunner.execute(() -> new Grafika(dummyPEV, dummyPE));

        window = new FrameFixture(robot(), frame);
        window.show();
    }


    @Test
    public void shouldClearAllViews() {
        JTextComponentFixture inputField = window.panel("pGridBag").textBox("tfInput");
        JButtonFixture button = window.button("bSend");

        inputField.setText("alwaysdebug");
        button.click();

        String commands = String.join(" endl ",
                "letrehoz cso cs1",
                "letrehoz pumpa p1",
                "letrehoz ciszterna c1",
                "letrehoz forras f1",
                "letrehoz szabotor sza1",
                "letrehoz szerelo sze1",
                "torol"
        );

        inputField.setText(commands);
        button.click();

        // Verify all views are removed
        assertNull(ObjectView.getViewByName("cs1"));
        assertNull(ObjectView.getViewByName("p1"));
        assertNull(ObjectView.getViewByName("c1"));
        assertNull(ObjectView.getViewByName("f1"));
        assertNull(ObjectView.getViewByName("sza1"));
        assertNull(ObjectView.getViewByName("sze1"));
    }
}
