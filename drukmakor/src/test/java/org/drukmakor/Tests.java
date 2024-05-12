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

    @DisplayName("VizetNovel nem mukodo cso")
    @Test
    void testVizetNovel2() throws Exception {
        Cso cso = new Cso();
        cso.Kilyukaszt(); // Kilyukasztjuk a csot
        cso.VizetNovel(1); // 1 egység vizet pumpalunk a csobe
        assertEquals(0, cso.getVizmennyiseg()); // A cso nem mukodik, ezert nem lehet vizet pumpalni bele
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

    @DisplayName("Szomszed hozzaadas")
    @Test
    void testSzomszedHozzaad() {
        Cso cso = new Cso();
        Pumpa pumpa = new Pumpa();
        cso.SzomszedHozzaad(pumpa); // Hozzaadjuk a pumpa-t a cso szomszedaihoz
        assertEquals(1, cso.GetSzomszedok().size()); // A cso egy szomszeddal rendelkezik
        assertEquals(pumpa, cso.GetSzomszedok().get(0)); // A szomszed a cso2
        //Második pumpa szomszéd hozzáadása
        Pumpa pumpa2 = new Pumpa();
        cso.SzomszedHozzaad(pumpa2); // Hozzaadjuk a pumpa2-t a cso szomszedaihoz
        assertEquals(2, cso.GetSzomszedok().size()); // A cso ket szomszeddal rendelkezik
        assertEquals(pumpa2, cso.GetSzomszedok().get(1)); // A masodik szomszed a pumpa2
        //Ha a cso mar ket szomszeddal rendelkezik, akkor nem tudunk tobbet hozzaadni
        Ciszterna c = new Ciszterna();
        cso.SzomszedHozzaad(c); // Hozzaadjuk a ciszternat a cso szomszedaihoz
        assertEquals(2, cso.GetSzomszedok().size()); // A cso tovabbra is ket szomszeddal rendelkezik
        assertFalse(cso.GetSzomszedok().contains(c)); // A cso szomszedai kozott nincs a ciszterna

    }

    @DisplayName("PumpaEpit")
    @Test
    void testPumpaEpit() {
        Cso cso = new Cso();
        Ciszterna c = new Ciszterna();
        cso.SzomszedHozzaad(c); //Ez a ciszterna azért kell mert a pumpaepit függvényben a cso szomszédait vizsgáljuk (Kell hogy legyen legalább egy szomszéd)
        Szerelo szerelo = new Szerelo();
        szerelo.getPumpaHatizsak().add(new Pumpa()); // A szerelo hátizsákjában van egy pumpa
        szerelo.Lep(cso); // A szerelo ralep a csore
        cso.PumpaEpit(); // A csohoz hozzaepitjuk a pumpat
        assertEquals(1, cso.GetSzomszedok().size()); // A cso egy szomszeddal rendelkezik
        assertInstanceOf(Pumpa.class, cso.GetSzomszedok().get(0)); // A szomszed egy pumpa
        assertEquals(0, szerelo.getPumpaHatizsak().size()); // A szerelo hátizsákjában nincs tobb pumpa
        assertEquals(2, cso.GetSzomszedok().get(0).GetSzomszedok().size()); // A pumpa ket szomszeddal rendelkezik
    }

    @DisplayName("Csuszos csorol tovabb csuszik a jatekos")
    @Test
    void testCsuszosCsorolCsuszik() {
        // A veletlen esemenyeket kikapcsoljuk ehhez a teszthez, mert a csuszas veletlenszeru szomszedra csusztatja a jatekost
        Mezo.doRandomThings = false; //Ha ezt kikapcsoltuk akkor a jatekos mindig az elso szomszedra csuszik (egyebkent veletlenszeruen a két szomszéd közül)
        Cso cso = new Cso();
        Pumpa pumpa = new Pumpa();
        Pumpa pumpa2 = new Pumpa();
        cso.Csuszik(); // A csot csuszossa tesszuk, ez azt jelenti hogy ha rálép valaki akokr tovább fog csúszni az első szomszédra
        cso.SzomszedHozzaad(pumpa); // Ez az elso szomszed, erre fog csuszni a jatekos
        cso.SzomszedHozzaad(pumpa2);
        Szerelo szerelo = new Szerelo();
        szerelo.Lep(cso); // A szerelo ralep a csore, de mivel csuszossa tettuk a csot, ezert tovabb fog csuszni
        assertEquals(pumpa, szerelo.getHelyzet()); // A szerelo a pumpa-ra csuszott
    }

    @DisplayName("Csuszossag elmúltával a jatekos nem csuszik tovább")
    @Test
    void testCsuszosCsorolCsuszik2() throws Exception {
        // A veletlen esemenyeket kikapcsoljuk ehhez a teszthez, mert a csuszas veletlenszeru szomszedra csusztatja a jatekost
        Mezo.doRandomThings = false; //Ha ezt kikapcsoltuk akkor a jatekos mindig az elso szomszedra csuszik (egyebkent veletlenszeruen a két szomszéd közül)
        Cso cso = new Cso();
        Pumpa pumpa = new Pumpa();
        Pumpa pumpa2 = new Pumpa();
        cso.Csuszik(); // A csot csuszossa tesszuk, ez azt jelenti hogy ha rálép valaki akokr tovább fog csúszni az első szomszédra
        cso.SzomszedHozzaad(pumpa); // Ez az elso szomszed, erre fog csuszni a jatekos
        cso.SzomszedHozzaad(pumpa2);
        Szerelo szerelo = new Szerelo();
        szerelo.Lep(cso); // A szerelo ralep a csore, de mivel csuszossa tettuk a csot, ezert tovabb fog csuszni
        assertEquals(pumpa, szerelo.getHelyzet()); // A szerelo a pumpa-ra csuszott
        // 5 frissítés után a csuszosság el fog múlni
        for (int i = 0; i < 5; i++) {
            cso.Frissit();
        }
        assertEquals(0, cso.getCsuszos()); // A csuszossag elmult
        szerelo.Lep(cso); // A szerelo ralep a csore, de mar nem fog tovabb csuszni
        assertEquals(cso, szerelo.getHelyzet()); // A szerelo a cso-ra lepett
    }


}