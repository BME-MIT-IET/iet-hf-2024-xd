package org.drukmakor;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class Tests {
    @DisplayName("Ez egy mindig lefuto teszt (check)")
    @Test
    void testSingleSuccessTest() {
        assertTrue(true);
    }
}

class CsoTests{
    @DisplayName("Cso elrontasa, majd javitasa")
    @Test
    void testCsoJavitas() {
        Cso cso = new Cso();
        cso.setMukodik(false);
        assertFalse(cso.getMukodik());
        cso.Megjavit();
        assertTrue(cso.getMukodik());
    }

    @DisplayName("Cso csuszossa tetele")
    @Test
    void testCsoCsuszossa() {
        Cso cso = new Cso();
        cso.Csuszik();
        assertEquals(5, cso.getCsuszos());
    }

    @DisplayName("Cso ragadossa tetele")
    @Test
    void testCsoRagadossa() {
        Szerelo szerelo = new Szerelo();
        Cso cso = new Cso();
        szerelo.Lep(cso); // Legalabb egy jatekosnak kell lennie a csovon
        cso.Ragad();
        assertEquals(5, cso.getRagados());
    }

    @DisplayName("Ragadossa tette")
    @Test
    void testRagadossaTette() {
        Szerelo szerelo = new Szerelo();
        Cso cso = new Cso();
        szerelo.Lep(cso); // Legalabb egy jatekosnak kell lennie a csovon
        cso.Ragad();
        assertEquals(szerelo, cso.getRagadossaTette());
    }

    @DisplayName("Jatekos elengedese")
    @Description("Azt nezzuk, hogy helyesen muokdik-e a cso ragadossaga. " +
            "Az a jatekos, aki ragadossa tette a csovet lelephet rola," +
            "mig mas jatekosok nem.")
    @Test
    void testJatekosElenged() throws Exception {
        Szerelo szerelo = new Szerelo();
        Szabotor szabotor = new Szabotor();
        Cso cso = new Cso();
        szerelo.Lep(cso); // A szerelo ralep a csore
        cso.Ragad(); // ...es ragadossa teszi
        assertTrue(cso.JatekosElenged(szerelo)); // A szerelo tette ragadossa, ezert ot elengedi
        cso.JatekosEltavolit(szerelo); // Eltavolitjuk a szerelot, hogy a szabotor oda tudjon lepni (mivel csak egy jatekos lehet a csovon)
        szabotor.Lep(cso); // Ezutan a szabotor lep a csore, neki ra kell ragadnia
        assertFalse(cso.JatekosElenged(szabotor)); // Tehat mivel nem a szabotor tette ragadossa, ezert nem engedjuk el
        // megvarjuk amig a ragadossag elmulik, ez 5 kor, majd ujra megprobaljuk
        for (int i = 0; i < 5; i++) {
            cso.Frissit();
        }
        assertTrue(cso.JatekosElenged(szabotor)); // Most mar elengedheti
    }

    @DisplayName("Jatekos elfogadasa, ures cso")
    @Test
    void testJatekosElfogad() {
        Szerelo szerelo = new Szerelo();
        Cso cso = new Cso();
        assertTrue(cso.JatekosElfogad(szerelo));
    }

    @DisplayName("Jatekos elfogadasa, tele cso")
    @Test
    void testJatekosElfogad2() {
        Szerelo szerelo = new Szerelo();
        Szabotor szabotor = new Szabotor();
        Cso cso = new Cso();
        szerelo.Lep(cso); //Egy valaki mar all a csovon
        assertFalse(cso.JatekosElfogad(szabotor)); // Nem tudunk tobb jatekost elfogadni (max 1 jatekos lehet a csovon)
    }

    @DisplayName("Kilyukasztas")
    @Test
    void testKilyukasztas() {
        Cso cso = new Cso();
        cso.SetVizmennyiseg(1); // A cso tartalmaz vizet
        cso.Kilyukaszt(); // Kilyukasztjuk
        assertFalse(cso.getMukodik()); // A cso nem mukodik mert kilyukasztottuk
        assertEquals(0, cso.getVizmennyiseg()); // A viz kifolyt a csobol a kilyukasztas miatt
    }

    @DisplayName("LyukCooldown")
    @Test
    void testLyukCooldown() throws Exception {
        Cso cso = new Cso();
        cso.SetVizmennyiseg(1); // A cso tartalmaz vizet
        cso.Kilyukaszt(); // Kilyukasztjuk
        cso.setMukodik(true); // A csovet rogton ujra megjavitjuk
        assertEquals(5, cso.getLyukCooldown()); // A lyukasztas utan 5 korig nem lehet ujra kilyukasztani
        cso.Kilyukaszt(); // Megprobaljuk ujra kilyukasztani, de nem sikerul
        assertTrue(cso.getMukodik()); // A cso mukodik
        for (int i = 0; i < 5; i++) {
            cso.Frissit(); // 5 kor telik el
        }
        assertEquals(0, cso.getLyukCooldown()); // Most mar ujra kilyukaszthatjuk
        cso.Kilyukaszt(); // Kilyukasztjuk
        assertFalse(cso.getMukodik()); // A cso nem mukodik mert kilyukasztottuk
    }

    @DisplayName("VizetNovel")
    @Test
    void testVizetNovel() throws Exception {
        Cso cso = new Cso();
        cso.VizetNovel(1); // 1 egység vizet pumpalunk a csobe
        assertEquals(1, cso.getVizmennyiseg()); // A cso tartalmaz 1 egység vizet
        cso.VizetNovel(1); // még 1 egység vizet pumpalunk a csobe
        assertEquals(1, cso.getVizmennyiseg()); // A csoben maximum 1 egység viz lehet, tehát továbbra is 1 egység van benne
    }

    @DisplayName("VizetCsokkent")
    @Test
    void testVizetCsokkent() throws Exception {
        Cso cso = new Cso();
        cso.SetVizmennyiseg(1); // A cso tartalmaz 1 egység vizet
        cso.VizetCsokkent(1); // 1 egység vizet kiveszunk a csobol
        assertEquals(0, cso.getVizmennyiseg()); // A cso tartalma 0
        cso.VizetCsokkent(1); // még 1 egység vizet megprobálunk kivenni a csobol
        assertEquals(0, cso.getVizmennyiseg()); // A cso tartalma tovabbra is 0, mert nem lehet negativ vizmennyiseg
    }
}