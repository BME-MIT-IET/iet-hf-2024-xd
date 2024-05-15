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
        pumpa.SzomszedHozzaad(cso);
        pumpa2.SzomszedHozzaad(cso);
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

    @DisplayName("Ragados nem teheto ragadossa ujra")
    @Test
    void testRagadosNemRagadhatUjra() throws Exception {
        Cso cso = new Cso();
        Szerelo szerelo = new Szerelo();
        szerelo.Lep(cso); // Legalabb egy jatekosnak kell lennie a csovon
        Szerelo masikSzerelo = new Szerelo();
        szerelo.RagadossaTesz(); // A szerelo ragadossa tette a csot
        cso.getJatekosok().remove(szerelo); // Eltavolitjuk a szerelot a csorol
        masikSzerelo.Lep(cso); // A masik szerelo ralep a csore
        cso.Frissit(); // 1 kor telik el
        masikSzerelo.RagadossaTesz(); // A masik szerelo is ragadossa probalja tenni a csovet, de ő nem tudja mert a csot mar ragadossa tette az előző szerelo
        assertEquals(szerelo, cso.getRagadossaTette()); // A szerelo ragadossa tette a csot
        assertEquals(4, cso.getRagados()); // A ragadossag 4 korig tart még (tehát a masik szerelo nem tudta ragadossa tenni)

    }


}

class SzereloTests {
    @DisplayName("Szerelo pumpat javit")
    @Test
    void testSzereloPumpaJavitas() {
        Szerelo szerelo = new Szerelo();
        Pumpa pumpa = new Pumpa();
        pumpa.setMukodik(false); // A pumpa nem mukodik
        szerelo.Lep(pumpa); // A szerelo ralep a pumpara (csak igy tudja megjavítani)
        szerelo.Javit(); // A szerelo megjavítja a pumpat
        assertTrue(pumpa.getMukodik()); // A pumpa mukodik
    }

    @DisplayName("Cso lecsatolasa sikeres")
    @Test
    void testCsoLecsatolas() {
        Szerelo szerelo = new Szerelo();
        Cso cso = new Cso();
        Ciszterna c = new Ciszterna();
        szerelo.Lep(c); // A szerelo ralep a ciszternara
        cso.SzomszedHozzaad(c); // A cso szomszedaihoz hozzaadjuk a ciszternat
        c.SzomszedHozzaad(cso); // A ciszterna szomszedaihoz hozzaadjuk a csot (kölcsönösen kell hozzaadni)
        szerelo.CsovetLecsatol(cso); // A szerelo lecsatolja a csot a ciszternarol
        assertEquals(0, cso.GetSzomszedok().size()); // A cso nem rendelkezik szomszedokkal, mert le lett csatolva
        assertEquals(0, c.GetSzomszedok().size()); // A ciszterna sem rendelkezik szomszedokkal, mert le lett csatolva
        assertEquals(1, szerelo.getCsoHatizsak().size()); // A szerelo hátizsákjában van a cső
        assertEquals(cso, szerelo.getCsoHatizsak().get(0)); // A hátizsákban a cső van
    }

    @DisplayName("Cso lecsatolasa sikertelen")
    @Test
    void testCsoLecsatolas2() {
        Szerelo szerelo = new Szerelo();
        Cso cso = new Cso();
        Ciszterna c = new Ciszterna();
        szerelo.Lep(c); // A szerelo ralep a ciszternara
        cso.SzomszedHozzaad(c); // A cso szomszedaihoz hozzaadjuk a ciszternat
        c.SzomszedHozzaad(cso); // A ciszterna szomszedaihoz hozzaadjuk a csot (kölcsönösen kell hozzaadni)
        //A szerelőnek mondjuk tele van a hátizsákja és ezért nem fogja tudni lecsatolni a csövet
        //Hat csövet adunk hozzá a hátizsákhoz (a hátizsák mérete 6)
        for (int i = 0; i < 6; i++) {
            szerelo.getCsoHatizsak().add(new Cso());
        }
        szerelo.CsovetLecsatol(cso); // A szerelo lecsatolja a csot a ciszternarol, de nem sikerul
        assertEquals(1, cso.GetSzomszedok().size()); // A cso tovabbra is rendelkezik szomszedokkal, mert nem lett lecsatolva
        assertEquals(1, c.GetSzomszedok().size()); // A ciszterna tovabbra is rendelkezik szomszedokkal, mert nem lett lecsatolva
        assertEquals(6, szerelo.getCsoHatizsak().size()); // A szerelo hátizsákjában 6 cső van, azok amiket korábban is tartalmazott
        assertFalse(szerelo.getCsoHatizsak().contains(cso)); // A hátizsákban nincs a cső amit most akartunk lecsatolni
    }

    @DisplayName("Egesz cso lecsatolasa sikeres")
    @Test
    void testEgeszCsoLecsatolas() {
        Szerelo szerelo = new Szerelo();
        Cso cso = new Cso();
        Ciszterna c = new Ciszterna();
        Ciszterna c2 = new Ciszterna();
        szerelo.Lep(c); // A szerelo ralep a ciszternara
        cso.SzomszedHozzaad(c); // A cso szomszedaihoz hozzaadjuk a ciszternat
        c.SzomszedHozzaad(cso); // A ciszterna szomszedaihoz hozzaadjuk a csot (kölcsönösen kell hozzaadni)
        c2.SzomszedHozzaad(cso); // A masik ciszterna szomszedaihoz is hozzaadjuk a csot
        cso.SzomszedHozzaad(c2); // A csot a masik ciszterna szomszedaihoz is hozzaadjuk
        szerelo.EgeszCsovetLecsatol(cso); // A szerelo lecsatolja az egesz csot a ciszternarol
        assertEquals(0, cso.GetSzomszedok().size()); // A cso nem rendelkezik szomszedokkal, mert le lett csatolva
        assertEquals(0, c.GetSzomszedok().size()); // A ciszterna sem rendelkezik szomszedokkal, mert le lett csatolva
        assertEquals(0, c2.GetSzomszedok().size()); // A masik ciszterna sem rendelkezik szomszedokkal, mert le lett csatolva
        assertEquals(2, szerelo.getCsoHatizsak().size()); // A szerelo hátizsákjában kétszer van a cső, mert az egesz csot lecsatolta, és ilyenkor mindkét végét a hátizsákba teszi
        assertEquals(cso, szerelo.getCsoHatizsak().get(0)); // A hátizsákban a cső van
    }

    @DisplayName("Egesz cso lecsatolasa sikertelen")
    @Test
    void testEgeszCsoLecsatolas2() {
        Szerelo szerelo = new Szerelo();
        Cso cso = new Cso();
        Ciszterna c = new Ciszterna();
        Ciszterna c2 = new Ciszterna();
        szerelo.Lep(c); // A szerelo ralep a ciszternara
        cso.SzomszedHozzaad(c); // A cso szomszedaihoz hozzaadjuk a ciszternat
        c.SzomszedHozzaad(cso); // A ciszterna szomszedaihoz hozzaadjuk a csot (kölcsönösen kell hozzaadni)
        c2.SzomszedHozzaad(cso); // A masik ciszterna szomszedaihoz is hozzaadjuk a csot
        cso.SzomszedHozzaad(c2); // A csot a masik ciszterna szomszedaihoz is hozzaadjuk
        //A szerelőnek mondjuk tele van a hátizsákja és ezért nem fogja tudni lecsatolni a csövet
        //Hat csövet adunk hozzá a hátizsákhoz (a hátizsák mérete 6)
        for (int i = 0; i < 6; i++) {
            szerelo.getCsoHatizsak().add(new Cso());
        }
        szerelo.EgeszCsovetLecsatol(cso); // A szerelo lecsatolja az egesz csot a ciszternarol, de nem sikerul
        assertEquals(2, cso.GetSzomszedok().size()); // A cso tovabbra is rendelkezik szomszedokkal, mert nem lett lecsatolva
        assertEquals(1, c.GetSzomszedok().size()); // A ciszterna tovabbra is rendelkezik szomszedokkal, mert nem lett lecsatolva
        assertEquals(1, c2.GetSzomszedok().size()); // A masik ciszterna
        assertEquals(6, szerelo.getCsoHatizsak().size()); // A szerelo hátizsákjában 6 cső van, azok amiket korábban is tartalmazott
    }

    @DisplayName("Egesz cso lecsatolasa sikertelen mert nincs cso hozzácsatolva a ciszternához")
    @Test
    void testEgeszCsoLecsatolas3() {
        Szerelo szerelo = new Szerelo();
        Ciszterna c = new Ciszterna();
        szerelo.Lep(c); // A szerelo ralep a ciszternara
        szerelo.EgeszCsovetLecsatol(c); // A szerelo lecsatolná az egesz csot a ciszternarol, de nem sikerul, mert nincs cső a ciszternához csatolva
        assertEquals(0, c.GetSzomszedok().size()); // A ciszternának nincs szomszédja, nem is volt
        assertEquals(0, szerelo.getCsoHatizsak().size()); // A szerelo hátizsákjában nincs cső
    }

    @DisplayName("Cso felcsatolasa sikeres")
    @Test
    void testCsoFelcsatolas() {
        Szerelo szerelo = new Szerelo();
        Cso cso = new Cso();
        Ciszterna c = new Ciszterna();
        //Szerelo hátizsákjában van a cső
        szerelo.getCsoHatizsak().add(cso);
        szerelo.Lep(c); // A szerelo ralep a ciszternara
        szerelo.CsovetFelcsatol(); // A szerelo felcsatolja a csot a ciszternara (a hátizsákból veszi ki a csovet, a legelsőt amit talál, de most csak egy van benne)
        assertEquals(1, cso.GetSzomszedok().size()); // A cso rendelkezik szomszedokkal, mert fel lett csatolva
        assertEquals(1, c.GetSzomszedok().size()); // A ciszterna rendelkezik szomszedokkal, mert fel lett csatolva
        assertEquals(0, szerelo.getCsoHatizsak().size()); // A szerelo hátizsákjában nincs a cső
    }

    @DisplayName("Cso felcsatolasa sikertelen mert nincs a hátizsákban cső")
    @Test
    void testCsoFelcsatolas2() {
        Szerelo szerelo = new Szerelo();
        Cso cso = new Cso();
        Ciszterna c = new Ciszterna();
        //Szerelo hátizsákjában nincs a cső
        szerelo.Lep(c); // A szerelo ralep a ciszternara
        szerelo.CsovetFelcsatol(); // A szerelo felcsatolja a csot a ciszternara, de nem sikerul
        assertEquals(0, cso.GetSzomszedok().size()); // A cso nem rendelkezik szomszedokkal, mert nem lett felcsatolva
        assertEquals(0, c.GetSzomszedok().size()); // A ciszterna sem rendelkezik szomszedokkal, mert nem lett felcsatolva
        assertEquals(0, szerelo.getCsoHatizsak().size()); // A szerelo hátizsákjában nincs cső, nem is volt
    }

    @DisplayName("Cso felcsatolasa sikertelen mert nem olyan mezon all a szerelo amihez csovet lehet csatolni")
    @Test
    void testCsoFelcsatolas3() {
        Szerelo szerelo = new Szerelo();
        Cso hatizsakCso = new Cso();
        Cso raleposCso = new Cso();
        //Szerelo hátizsákjában van a cső
        szerelo.getCsoHatizsak().add(hatizsakCso);
        //A szerelo ralep egy cso-re
        szerelo.Lep(raleposCso);
        szerelo.CsovetFelcsatol(); // A szerelo felcsatolja a csot a ciszternara, de nem sikerul, mert csőre nem lehet csövet csatolni
        assertEquals(0, hatizsakCso.GetSzomszedok().size()); // A hatizsakCso nem rendelkezik szomszedokkal, mert nem lett felcsatolva
        assertEquals(0, raleposCso.GetSzomszedok().size());  // A raleposCso sem rendelkezik szomszedokkal, mert nem lett felcsatolva
        assertEquals(1, szerelo.getCsoHatizsak().size()); // A szerelo hátizsákjában van a cső
        assertTrue(szerelo.getCsoHatizsak().contains(hatizsakCso)); // A hátizsákban még mindig a cső van
    }

    @DisplayName("Pumpa felvetele ciszternanal")
    @Test
    void testPumpaFelveteleCiszternanal() {
        Szerelo szerelo = new Szerelo();
        Ciszterna c = new Ciszterna();
        c.PumpaKeszit(); // A ciszternanal pumpat keszitunk
        //Szerelo hátizsákjában van a pumpa
        szerelo.Lep(c); // A szerelo ralep a ciszternara
        szerelo.PumpatFelvesz(); // A szerelo felveszi a pumpat a ciszternarol (arról a mezőről veszi fel, amin áll, nem kell paraméter)
        assertEquals(0, c.getTermeltPumpak().size()); // A ciszterna nem rendelkezik pumpakkal, mert fel lett véve
        assertEquals(1, szerelo.getPumpaHatizsak().size()); // A szerelo hátizsákjában van a pumpa
    }

    @DisplayName("Pumpa felvetele ciszternanal sikertelen")
    @Test
    void testPumpaFelveteleCiszternanal2() {
        Szerelo szerelo = new Szerelo();
        Ciszterna c = new Ciszterna();
        szerelo.Lep(c); // A szerelo ralep a ciszternara
        szerelo.PumpatFelvesz(); // A szerelo felvnné a pumpat a ciszternarol, de nem sikerul, mert nincs a ciszternan pumpa
        assertEquals(0, c.getTermeltPumpak().size()); // A ciszterna rendelkezik pumpakkal, mert nem lett felvéve
        assertEquals(0, szerelo.getPumpaHatizsak().size()); // A szerelo hátizsákjában nincs pumpa
    }

    @DisplayName("Pumpa felvetele ciszternanal sikertelen mert nincs a hátizsákban hely")
    @Test
    void testPumpaFelveteleCiszternanal3() {
        Szerelo szerelo = new Szerelo();
        Ciszterna c = new Ciszterna();
        c.PumpaKeszit(); // A ciszternanal pumpat keszitunk
        //Szerelo hátizsákjában nincs hely
        for (int i = 0; i < 6; i++) {
            szerelo.getPumpaHatizsak().add(new Pumpa());
        }
        szerelo.Lep(c); // A szerelo ralep a ciszternara
        szerelo.PumpatFelvesz(); // A szerelo felvnné a pumpat a ciszternarol, de nem sikerul, mert nincs hely a hátizsákban
        assertEquals(1, c.getTermeltPumpak().size()); // A ciszterna rendelkezik pumpakkal, mert nem lett felvéve
        assertEquals(6, szerelo.getPumpaHatizsak().size()); // A szerelo hátizsákjában 6 pumpa van
    }

    @DisplayName("Pumpa epites")
    @Test
    void testPumpaEpites() {
        Szerelo szerelo = new Szerelo();
        Cso cso = new Cso();
        Ciszterna c = new Ciszterna();
        c.SzomszedHozzaad(cso); // A ciszternanak van egy szomszedja, a cso (ez csak technikai okokbol kell)
        cso.SzomszedHozzaad(c); // A cso szomszedaihoz hozzaadjuk a ciszternat
        szerelo.getPumpaHatizsak().add(new Pumpa()); // A szerelo hátizsákjában van egy pumpa
        szerelo.Lep(cso); // A szerelo ralep a ciszternara
        szerelo.PumpatEpit(); // A szerelo epít egy pumpat a csohoz
        assertEquals(1, cso.GetSzomszedok().size()); // A csonek csak egy szomszedja lesz, a pumpa
        assertInstanceOf(Pumpa.class, cso.GetSzomszedok().get(0)); // A szomszed egy pumpa
        assertEquals(0, szerelo.getPumpaHatizsak().size()); // A szerelo hátizsákjában nincs pumpa
    }

    @DisplayName("Pumpa epites sikertelen mert nincs a hátizsákban pumpa")
    @Test
    void testPumpaEpites2() {
        Szerelo szerelo = new Szerelo();
        Cso cso = new Cso();
        Ciszterna c = new Ciszterna();
        c.SzomszedHozzaad(cso); // A ciszternanak van egy szomszedja, a cso (ez csak technikai okokbol kell)
        cso.SzomszedHozzaad(c); // A cso szomszedaihoz hozzaadjuk a ciszternat
        //Szerelo hátizsákjában nincs pumpa
        szerelo.Lep(cso); // A szerelo ralep a ciszternara
        szerelo.PumpatEpit(); // A szerelo epít egy pumpat a csohoz, de nem sikerul, mert nincs a hátizsákban pumpa
        assertEquals(1, cso.GetSzomszedok().size()); // A ciszterna nem rendelkezik szomszedokkal, mert nem lett felépítve mellé egy pumpa, csak a cso ami eddig is ott volt
        assertEquals(0, szerelo.getPumpaHatizsak().size()); // A szerelo hátizsákjában nincs pumpa
    }
}
