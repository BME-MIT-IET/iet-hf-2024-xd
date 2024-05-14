package UITests;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.Settings;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.drukmakor.Grafika;
import org.drukmakor.ParancsErtelmezo;
import org.drukmakor.ParancsErtelmezoView;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.assertTrue;

public class NewUniqueGameCancelTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @Override
    protected void onSetUp() {
        Settings settings = new Settings();
        settings.delayBetweenEvents(10);
        // Dummy példányok létrehozása
        ParancsErtelmezoView dummyPEV = new ParancsErtelmezoView();
        ParancsErtelmezo dummyPE = new ParancsErtelmezo(dummyPEV);

        dummyPE.runFromFile("commandfiles/alap");
        dummyPE.EnableDebugMode(false);
        dummyPE.OutputToView(true);

        Grafika frame = GuiActionRunner.execute(() -> new Grafika(dummyPEV, dummyPE));

        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Test
    public void shouldShowNewUniqueGameDialog() {
        window.button("bNewUniqueGame").click();

        JOptionPaneFixture optionPane = window.optionPane();

        String expectedMessage = "Path: ";

        assertTrue(
                optionPane.robot().finder().findAll(new GenericTypeMatcher<JLabel>(JLabel.class) {
                            @Override
                            protected boolean isMatching(JLabel label) {
                                return label.isShowing() && label.getText() != null;
                            }
                        }).stream()
                        .allMatch(label -> expectedMessage.equals(label.getText()))
        );
        optionPane.buttonWithText("Cancel").click();

        String outputText = window.textBox("jtaOutput").text();
        assertTrue("The jtaOutput text area should be empty, but it contains: " + outputText, outputText.isEmpty());
    }
}
