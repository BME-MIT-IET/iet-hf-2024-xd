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

public class HelpDialogTest extends AssertJSwingJUnitTestCase {
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
    public void shouldShowDuneMessageDialog() {
        window.button("bHelp").click();

        JOptionPaneFixture optionPane = window.optionPane();

        String expectedMessage = "---Játékhoz szükséges parancsok--- \n" +
                "lep <jatekos> <mezo> \n" +
                "szerel <szerelo> \n" +
                "lyukaszt <jatekos> \n" +
                "allit <jatekos> <bemenet_cso> <kimenet_cso> \n" +
                "frissit \n" +
                "epit <szerelo> <”cso”/”pumpa”> \n" +
                "felvesz <szerelo> <”cso”/”pumpa”> [cso_nev] [egesz/fel] \n" +
                "allapot <objektum> <objektum_attributum> [filenév]  \n" +
                "csuszik <szabotor> \n" +
                "ragad <jatekos> \n" +
                "\n---Debug parancsok---\n" +
                "letrehoz <”cso”/”pumpa”/”ciszterna”/”forras”/”szerelo”/”szabotor”> <nev> \n" +
                "veletlen <”be”/”ki”> \n" +
                "tolt <fajlnev> \n" +
                "osszekot <mezo1> <mezo2> \n" +
                "elront <pumpa/cso> \n" +
                "termel <ciszterna> <”pumpa”/”cso”> \n" +
                "csofelulet <cso> <\"csuszos\"/\"ragados\"> \n" +
                "vizmennyiseg <pumpa/cso> <mennyiseg> \n" +
                "torol";
        assertTrue(
                optionPane.robot().finder().findAll(new GenericTypeMatcher<JLabel>(JLabel.class) {
                    @Override
                    protected boolean isMatching(JLabel label) {
                        return label.isShowing() && label.getText() != null;
                    }
                }).stream()
                .allMatch(label -> expectedMessage.contains(label.getText()))
        );
        window.dialog().button().click();
    }
}