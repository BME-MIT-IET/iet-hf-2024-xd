package org.drukmakor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Tests {
    ParancsErtelmezo pre;

    @BeforeEach
    void setUp() {
        pre = new ParancsErtelmezo();
        pre.runFromString("letrehoz forras pf1\n" +
                "letrehoz ciszterna pc1\n" +
                "letrehoz pumpa pp1\n" +
                "letrehoz pumpa pp2\n" +
                "letrehoz pumpa pp3\n" +
                "letrehoz cso pcs1\n" +
                "letrehoz cso pcs2\n" +
                "letrehoz cso pcs3\n" +
                "letrehoz cso pcs4\n" +
                "letrehoz cso pcs5\n" +
                "letrehoz cso pcs6\n" +
                "letrehoz cso pcs7\n" +
                "letrehoz szerelo psze1\n" +
                "letrehoz szerelo psze2\n" +
                "letrehoz szabotor psza1\n" +
                "osszekot pcs1 pf1\n" +
                "osszekot pcs1 pp1\n" +
                "osszekot pp1 pcs2\n" +
                "osszekot pcs2 pp2\n" +
                "osszekot pp2 pcs6\n" +
                "osszekot pcs6 pc1\n" +
                "osszekot pc1 pcs7\n" +
                "osszekot pp1 pcs3\n" +
                "osszekot pcs3 pp3\n" +
                "osszekot pp3 pcs4\n" +
                "osszekot pcs4 pp2\n" +
                "osszekot pp3 pcs5\n" +
                "osszekot pcs5 pc1\n" +
                "lep psza1 pcs3\n" +
                "lep psze2 pp3\n" +
                "lep psze1 pcs5\n"+
                "veletlen ki"
        );
    }

    @DisplayName("Ez egy mindig lefuto teszt (check)")
    @Test
    void testSingleSuccessTest() {
        assertTrue(true);
    }

    @DisplayName("Cso letrehozasa")
    @Test
    void cso_letrehozasa() {
        pre.runFromString("letrehoz cso cs1");
        pre.runFromString("allapot cs1 *");
        assertEquals("cs1 mukodik: true\n" +
                        "cs1 szomszedok: \n" +
                        "cs1 maxJatekosok: 1\n" +
                        "cs1 maxSzomszedok: 2\n" +
                        "cs1 jatekosok: \n" +
                        "cs1 vizmennyiseg: 0\n"+
                        "cs1 lyukCooldown: 0\n" +
                        "cs1 csuszos: 0\n" +
                        "cs1 ragados: 0",
                pre.getAllapotString());
    }

    @Test
    @DisplayName("n Pumpa felcsatolasa a csore")
    void felcsatol() {
        pre.runFromString("letrehoz cso cs1\n" +
                "letrehoz pumpa p1\n" +
                "letrehoz pumpa p2\n" +
                "letrehoz pumpa p3\n" +
                "letrehoz pumpa p4\n" +
                "letrehoz pumpa p5\n" +
                "osszekot cs1 p1\n" +
                "osszekot cs1 p2\n" +
                "osszekot cs1 p3\n" +
                "osszekot cs1 p4\n" +
                "osszekot cs1 p5\n" +
                "allapot cs1 szomszedok");
        String output = pre.getAllapotString();
        assertEquals("cs1 szomszedok: \n" +
                "p1\n" +
                "p2", output);
    }

    @Test
    @DisplayName("Ket cso osszekotese")
    void osszekot_ket_csot() {
        pre.runFromString("letrehoz cs1\n" +
                "letrehoz cs2\n" +
                "osszekot cs1 cs2\n" +
                "allapot cs1 szomszedok\n" +
                "allapot cs2 szomszedok");
        assertEquals("", pre.getAllapotString());
    }

    @Test
    @DisplayName("Szomszedos, foglalt csore lepes")
    void csore_lep() {
        pre.runFromString("lep psze2 pcs5\n" +
                "allapot pp3 jatekosok\n" +
                "allapot pcs5 jatekosok");
        String out = pre.getAllapotString();
        assertEquals("pp3 jatekosok: \n" +
                "psze2\n" +
                "pcs5 jatekosok: \n" +
                "psze1", out);
    }

    @Test
    @DisplayName("ures cso tulpumpalasa")
    void tulpumpal() {
        pre.runFromString("allit psze2 pcs3 pcs5\n" +
                "vizmennyiseg pp3 3\n" +
                "frissit\n" +
                "frissit\n" +
                "allapot pcs3 vizmennyiseg\n" +
                "allapot pp3 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs3 vizmennyiseg: 1\n" +
                "pp3 vizmennyiseg: 2", out);
    }


    @Test
    @DisplayName("Cso kilyukasztasa")
    void kilyukaszt() {
        pre.runFromString("allit psze2 pcs3 pcs5\n" +
                "vizmennyiseg pcs5 1\n" +
                "vizmennyiseg pp3 3\n" +
                "allapot pcs5 mukodik\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "lyukaszt psze1\n" +
                "allapot pcs5 mukodik\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "frissit\n" +
                "allapot pcs5 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs5 mukodik: true\n" +
                "pcs5 vizmennyiseg: 1\n" +
                "pcs5 mukodik: false\n" +
                "pcs5 vizmennyiseg: 0\n" +
                "pcs5 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("Kilyukasztott cso kilyukasztasa")
    void lyukaszt() {
        pre.runFromString("allit psze2 pcs3 pcs5\n" +
                "vizmennyiseg pcs5 1\n" +
                "vizmennyiseg pp3 3\n" +
                "allapot pcs5 mukodik\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "lyukaszt psze1\n" +
                "allapot pcs5 mukodik\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "frissit\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "lyukaszt psze2\n" +
                "frissit\n" +
                "allapot pcs5 mukodik\n" +
                "allapot pcs5 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs5 mukodik: true\n" +
                "pcs5 vizmennyiseg: 1\n" +
                "pcs5 mukodik: false\n" +
                "pcs5 vizmennyiseg: 0\n" +
                "pcs5 vizmennyiseg: 0\n" +
                "pcs5 mukodik: false\n" +
                "pcs5 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("Nemy lyukaszthato cso lyukasztasa")
    void lyukaszt2() {
        pre.runFromString("allit psze2 pcs5 pcs3\n" +
                "vizmennyiseg pcs5 1\n" +
                "vizmennyiseg pp3 3\n" +
                "lyukaszt psze1\n" +
                "szerel psze1\n" +
                "lyukaszt psze1\n" +
                "allapot pcs5 mukodik\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "allapot pcs5 lyukCooldown\n" +
                "frissit\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "allapot pcs5 lyukCooldown");
        String out = pre.getAllapotString();
        assertEquals("pcs5 mukodik: true\n" +
                "pcs5 vizmennyiseg: 0\n" +
                "pcs5 lyukCooldown: 5\n" +
                "pcs5 vizmennyiseg: 0\n" +
                "pcs5 lyukCooldown: 4", out);
    }

    @Test
    @DisplayName("Lyukas cso javitasa")
    void javit() {
        pre.runFromString("allit psze2 pcs5 pcs3\n" +
                "vizmennyiseg pcs5 1\n" +
                "vizmennyiseg pp3 3\n" +
                "lyukaszt psze1\n" +
                "szerel psze1\n" +
                "allapot pcs5 mukodik\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "allapot pcs5 lyukCooldown\n" +
                "frissit\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "allapot pcs5 lyukCooldown");
        String out = pre.getAllapotString();
        assertEquals("pcs5 mukodik: true\n" +
                "pcs5 vizmennyiseg: 0\n" +
                "pcs5 lyukCooldown: 5\n" +
                "pcs5 vizmennyiseg: 0\n" +
                "pcs5 lyukCooldown: 4", out);
    }

    @Test
    @DisplayName("Nem lyukas, nem lyukaszthato csovet javit")
    void javit2() {
        pre.runFromString("allit psze2 pcs3 pcs5\n" +
                "vizmennyiseg pcs5 1\n" +
                "vizmennyiseg pp3 3\n" +
                "lyukaszt psze1\n" +
                "szerel psze1\n" +
                "szerel psze1\n" +
                "allapot pcs5 mukodik\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "allapot pcs5 lyukCooldown\n" +
                "frissit\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "allapot pcs5 lyukCooldown");
        String out = pre.getAllapotString();
        assertEquals("pcs5 mukodik: true\n" +
                "pcs5 vizmennyiseg: 0\n" +
                "pcs5 lyukCooldown: 5\n" +
                "pcs5 vizmennyiseg: 0\n" +
                "pcs5 lyukCooldown: 4", out);
    }

    @Test
    @DisplayName("Nem lyukas cso javitasa")
    void javit3() {
        pre.runFromString("allit psze2 pcs3 pcs5\n" +
                "vizmennyiseg pcs5 1\n" +
                "vizmennyiseg pp3 3\n" +
                "szerel psze1\n" +
                "allapot pcs5 mukodik\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "allapot pcs5 lyukCooldown\n" +
                "frissit\n" +
                "allapot pcs5 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs5 mukodik: true\n" +
                "pcs5 vizmennyiseg: 1\n" +
                "pcs5 lyukCooldown: 0\n" +
                "pcs5 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("Nem csuszos cso csuszossa tetele")
    void csuszos() {
        pre.runFromString("csuszik psza1\n" +
                "allapot pcs3 csuszos\n" +
                "allapot pcs3 jatekosok\n" +
                "lep psza1 pp1\n" +
                "lep psze2 pcs3\n" +
                "allapot pp1 jatekosok\n" +
                "allapot pcs3 jatekosok\n" +
                "allapot pp3 jatekosok");
        String out = pre.getAllapotString();
        assertEquals("pcs3 csuszos: 5\n" +
                "pcs3 jatekosok: \n" +
                "psza1\n" +
                "pp1 jatekosok: \n" +
                "psza1\n" +
                "psze2\n" +
                "pcs3 jatekosok: \n" +
                "pp3 jatekosok:", out);
    }

    @Test
    @DisplayName("Csuszos cso csuszossa tetele")
    void csuszos2() {
        pre.runFromString("csuszik psza1\n" +
                "csuszik psza1\n" +
                "allapot pcs3 csuszos\n" +
                "allapot pcs3 jatekosok\n" +
                "lep psza1 pp1\n" +
                "lep psze2 pcs3\n" +
                "allapot pp1 jatekosok\n" +
                "allapot pcs3 jatekosok\n" +
                "allapot pp3 jatekosok");
        String out = pre.getAllapotString();
        assertEquals("pcs3 csuszos: 5\n" +
                "pcs3 jatekosok: \n" +
                "psza1\n" +
                "pp1 jatekosok: \n" +
                "psza1\n" +
                "psze2\n" +
                "pcs3 jatekosok: \n" +
                "pp3 jatekosok:", out);
    }

    @Test
    @DisplayName("Cso ragadossa tetele")
    void ragados() {
        pre.runFromString("ragad psza1\n" +
                "allapot pcs3 ragados\n" +
                "allapot pcs3 ragadossatette\n" +
                "lep psza1 pp1\n" +
                "allapot pcs3 jatekosok\n" +
                "allapot pp1 jatekosok\n" +
                "allapot pcs3 ragadossatette");
        String out = pre.getAllapotString();
        assertEquals("pcs3 ragados: 5\n" +
                "pcs3 ragadossaTette: psza1\n" +
                "pcs3 jatekosok: \n" +
                "pp1 jatekosok: \n" +
                "psza1", out);
    }

    @Test
    @DisplayName("Ragados cso ragadossa tetele")
    void ragados2() {
        pre.runFromString("ragad psza1\n" +
                "ragad psza1\n" +
                "allapot pcs3 ragados\n" +
                "allapot pcs3 ragadossatette\n" +
                "lep psza1 pp1\n" +
                "allapot pcs3 jatekosok\n" +
                "allapot pp1 jatekosok\n" +
                "allapot pcs3 ragadossatette\n" +
                "lep psze2 pcs3\n" +
                "ragad psza1\n" +
                "allapot pcs3 ragadossatette\n" +
                "lep psze2 pp3\n" +
                "allapot pcs3 jatekosok\n" +
                "allapot pp3 jatekosok");
        String out = pre.getAllapotString();
        assertEquals("pcs3 ragados: 5\n" +
                "pcs3 ragadossaTette: psza1\n" +
                "pcs3 jatekosok: \n" +
                "pp1 jatekosok: \n" +
                "psza1\n" +
                "pcs3 jatekosok: \n" +
                "psze2\n" +
                "pp3 jatekosok:", out);
    }

    @Test
    @DisplayName("Ragados csorol lelepes")
    void ragad_lep() {
        pre.runFromString("ragad psza1\n" +
                "allapot pcs3 ragados\n" +
                "allapot pcs3 ragadossatette\n" +
                "lep psza1 pp1\n" +
                "allapot pcs3 jatekosok\n" +
                "allapot pp1 jatekosok\n" +
                "allapot pcs3 ragadossatette\n" +
                "lep psze2 pcs3\n" +
                "lep psze2 pp3\n" +
                "allapot pcs3 jatekosok\n" +
                "allapot pp3 jatekosok");
        String out = pre.getAllapotString();
        assertEquals("pcs3 ragados: 5\n" +
                "pcs3 ragadossaTette: psza1\n" +
                "pcs3 jatekosok: \n" +
                "pp1 jatekosok: \n" +
                "psza1\n" +
                "pcs3 jatekosok: \n" +
                "psze2\n" +
                "pp3 jatekosok:", out);
    }

    @Test
    @DisplayName("Csuszos cso ragadossa tetele")
    void csuszik_ragad() {
        pre.runFromString("csuszik psza1\n" +
                "ragad psza1\n" +
                "lep psza1 pp1\n" +
                "lep psze2 pcs3\n" +
                "lep psze2 pp3\n" +
                "allapot pcs3 ragados\n" +
                "allapot pcs3 csuszos\n" +
                "allapot pcs3 jatekosok\n" +
                "allapot pp3 jatekosok");
        String out = pre.getAllapotString();
        assertEquals("pcs3 ragados: 5\n" +
                "pcs3 csuszos: 5\n" +
                "pcs3 jatekosok: \n" +
                "psze2\n" +
                "pp3 jatekosok:", out);
    }

    @Test
    @DisplayName("Pumpa letrehozasa")
    void pletrehoz() {
        pre.runFromString("letrehoz pumpa p \n" +
                "allapot p *");
        String out = pre.getAllapotString();
        assertEquals("p mukodik: true\n" +
                "p szomszedok: \n" +
                "p maxJatekosok: 2147483647\n" +
                "p maxSzomszedok: 5\n" +
                "p jatekosok: \n" +
                "p vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("n db Pumpa felcsatolasa")
    void pfelcsatol() {
        pre.runFromString("letrehoz pumpa p\n" +
                "letrehoz cso cs1\n" +
                "letrehoz cso cs2\n" +
                "letrehoz cso cs3\n" +
                "letrehoz cso cs4\n" +
                "letrehoz cso cs5\n" +
                "letrehoz cso cs6\n" +
                "osszekot p cs1\n" +
                "osszekot p cs2\n" +
                "osszekot p cs3\n" +
                "osszekot p cs4\n" +
                "osszekot p cs5\n" +
                "osszekot p cs6\n" +
                "allapot p maxSzomszedok\n" +
                "allapot p szomszedok");
        String out = pre.getAllapotString();
        assertEquals("p maxSzomszedok: 5\n" +
                "p szomszedok: \n" +
                "cs1\n" +
                "cs2\n" +
                "cs3\n" +
                "cs4\n" +
                "cs5", out);
    }

    @Test
    @Disabled
    @DisplayName("Pumpahoz aktiv elem kotese")
    void aktiv() {
        pre.runFromString("letrehoz pumpa p1\n" +
                "letrehoz ciszterna c\n" +
                "letrehoz forras f\n" +
                "letrehoz pumpa p2\n" +
                "osszekot p1 c\n" +
                "osszekot p1 f\n" +
                "osszekot p1 p2\n" +
                "allapot p1 szomszedok");
        String out = pre.getAllapotString();
        assertEquals("szomszedok: ", out);
    }

    @Test
    @DisplayName("Pumpara lepes")
    void plepes() {
        pre.runFromString("lep psza1 pp3\n" +
                "allapot pcs3 jatekosok\n" +
                "allapot pp3 jatekosok");
        String out = pre.getAllapotString();
        assertEquals("pcs3 jatekosok: \n" +
                "pp3 jatekosok: \n" +
                "psza1\n" +
                "psze2", out);
    }

    @Test
    @DisplayName("Pumpa allitasa")
    void allit() {
        pre.runFromString("allit psze2 pcs3 pcs4\n" +
                "allapot pp3 bemenet\n" +
                "allapot pp3 kimenet");
        String out = pre.getAllapotString();
        assertEquals("pp3 bemenet: pcs4\n" +
                "pp3 kimenet: pcs3", out);
    }

    @Test
    @DisplayName("ervenytelen pumpa allitasa")
    void allit2() {
        pre.runFromString("allit psze2 pcs3 pcs4\n" +
                "allit psze2 pcs1 pcs2\n" +
                "allapot pp3 bemenet\n" +
                "allapot pp3 kimenet");
        String out = pre.getAllapotString();
        assertEquals("pp3 bemenet: pcs4\n" +
                "pp3 kimenet: pcs3", out);
    }

    @Test
    @DisplayName("Pumpalas ures csobe")
    void pumpal() {
        pre.runFromString("allit psze2 pcs3 pcs5\n" +
                "vizmennyiseg pcs3 1\n" +
                "vizmennyiseg pp3 3\n" +
                "frissit\n" +
                "allapot pcs3 vizmennyiseg\n" +
                "allapot pp3 vizmennyiseg\n" +
                "allapot pcs5 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs3 vizmennyiseg: 1\n" +
                "pp3 vizmennyiseg: 3\n" +
                "pcs5 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("Pumpalas teli csobe")
    void pumpal2() {
        pre.runFromString("allit psze2 pcs3 pcs4\n" +
                "vizmennyiseg pcs3 1\n" +
                "vizmennyiseg pp3 3\n" +
                "vizmennyiseg pcs4 1\n" +
                "frissit\n" +
                "allapot pcs3 vizmennyiseg\n" +
                "allapot pp3 vizmennyiseg\n" +
                "allapot pcs4 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs3 vizmennyiseg: 1\n" +
                "pp3 vizmennyiseg: 4\n" +
                "pcs4 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("Pumpalas teli csobe, teli pumpaval")
    void pumpal3() {
        pre.runFromString("allit psze2 pcs3 pcs4\n" +
                "vizmennyiseg pcs3 1\n" +
                "vizmennyiseg pp3 5\n" +
                "vizmennyiseg pcs4 1\n" +
                "frissit\n" +
                "allapot pcs3 vizmennyiseg\n" +
                "allapot pp3 vizmennyiseg\n" +
                "allapot pcs4 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs3 vizmennyiseg: 1\n" +
                "pp3 vizmennyiseg: 5\n" +
                "pcs4 vizmennyiseg: 1", out);
    }

    @Test
    @DisplayName("ures csobol ures tartallyal pumpalas")
    void pumpal4() {
        pre.runFromString("allit psze2 pcs3 pcs4\n" +
                "frissit\n" +
                "allapot pcs3 vizmennyiseg\n" +
                "allapot pp3 vizmennyiseg\n" +
                "allapot pcs4 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs3 vizmennyiseg: 0\n" +
                "pp3 vizmennyiseg: 0\n" +
                "pcs4 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("ures csobol, de nem ures tartallyal pumpalas")
    void pumpal5() {
        pre.runFromString("allit psze2 pcs3 pcs4\n" +
                "vizmennyiseg pp3 3\n" +
                "frissit\n" +
                "allapot pcs3 vizmennyiseg\n" +
                "allapot pp3 vizmennyiseg\n" +
                "allapot pcs4 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs3 vizmennyiseg: 1\n" +
                "pp3 vizmennyiseg: 2\n" +
                "pcs4 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("Elromlott pumpaval pumpalas")
    void pumpal6() {
        pre.runFromString("allit psze2 pcs3 pcs4\n" +
                "elront pp3\n" +
                "vizmennyiseg pcs3 1\n" +
                "vizmennyiseg pp3 1\n" +
                "frissit\n" +
                "allapot pcs3 vizmennyiseg\n" +
                "allapot pp3 vizmennyiseg\n" +
                "allapot pcs4 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs3 vizmennyiseg: 1\n" +
                "pp3 vizmennyiseg: 1\n" +
                "pcs4 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("Pumpa elrontasa")
    void elrontp() {
        pre.runFromString("allapot pp3 mukodik\n" +
                "elront pp3\n" +
                "allapot pp3 mukodik");
        String out = pre.getAllapotString();
        assertEquals("pp3 mukodik: true\n" +
                "pp3 mukodik: false", out);
    }

    @Test
    @DisplayName("Pumpa megjavitasa")
    void pmegjavit() {
        pre.runFromString("allit psze2 pcs3 pcs4\n" +
                "elront pp3\n" +
                "allapot pp3 mukodik\n" +
                "vizmennyiseg pcs3 1\n" +
                "vizmennyiseg pp3 1\n" +
                "szerel psze2\n" +
                "allapot pp3 mukodik\n" +
                "frissit\n" +
                "allapot pcs3 vizmennyiseg\n" +
                "allapot pp3 vizmennyiseg\n" +
                "allapot pcs4 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pp3 mukodik: false\n" +
                "pp3 mukodik: true\n" +
                "pcs3 vizmennyiseg: 1\n" +
                "pp3 vizmennyiseg: 1\n" +
                "pcs4 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("Forras letrehozasa")
    void fletrehoz() {
        pre.runFromString("letrehoz forras tesztforras\n" +
                "allapot tesztforras maxSzomszedok");
        String out = pre.getAllapotString();
        assertEquals("tesztforras maxSzomszedok: 5", out);
    }

    @Test
    @DisplayName("Forrashoz csatolas tesztelese")
    void forrashoz_csatol() {
        pre.runFromString("letrehoz forras tesztforras \n" +
                "letrehoz cso cso1\n" +
                "letrehoz cso cso2\n" +
                "letrehoz cso cso3\n" +
                "letrehoz cso cso4\n" +
                "letrehoz cso cso5\n" +
                "allapot tesztforras szomszedok\n" +
                "osszekot tesztforras cso1\n" +
                "allapot tesztforras szomszedok\n" +
                "osszekot tesztforras cso2\n" +
                "allapot tesztforras szomszedok\n" +
                "osszekot tesztforras cso3\n" +
                "allapot tesztforras szomszedok\n" +
                "osszekot tesztforras cso4\n" +
                "allapot tesztforras szomszedok\n" +
                "osszekot tesztforras cso5\n"
        );
        String out = pre.getAllapotString();
        assertEquals("tesztforras szomszedok: \n" +
                "tesztforras szomszedok: \n" +
                "cso1\n" +
                "tesztforras szomszedok: \n" +
                "cso1\n" +
                "cso2\n" +
                "tesztforras szomszedok: \n" +
                "cso1\n" +
                "cso2\n" +
                "cso3\n" +
                "tesztforras szomszedok: \n" +
                "cso1\n" +
                "cso2\n" +
                "cso3\n" +
                "cso4", out);
    }

    @Test
    @Disabled
    @DisplayName("Forrashoz aktiv elem csatlakoztatasa: ")
    void hozzacsatol() {
        pre.runFromString("letrehoz forras tesztforras \n" +
                "letrehoz pumpa p1 \n" +
                "letrehoz ciszterna c1 \n" +
                "letrehoz forras f1\n" +
                "osszekot tesztforras p1\n" +
                "allapot tesztforras szomszedok\n" +
                "osszekot tesztforras c1\n" +
                "allapot tesztforras szomszedok\n" +
                "osszekot tesztforras f1\n" +
                "allapot tesztforras szomszedok\n"
        );
        String out = pre.getAllapotString();
        assertEquals("tesztforras szomszedok: \n" +
                "tesztforras szomszedok: \n" +
                "tesztforras szomszedok: \n", out);
    }

    @Test
    @DisplayName("A forras vizet termel")
    void ftermel() {
        pre.runFromString("frissit \n" +
                "allapot pcs1 vizmennyiseg \n");
        String out = pre.getAllapotString();
        assertEquals("pcs1 vizmennyiseg: 1", out);
    }

    @Test
    @DisplayName("Forras vizet termel, cso teli")
    void ftermel2() {
        pre.runFromString("vizmennyiseg pcs1 1 \n" +
                "frissit \n" +
                "allapot pcs1 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs1 vizmennyiseg: 1", out);
    }

    @Test
    @DisplayName("Csorol a forrasra leptetek egy jatekost")
    void forrasralep() {
        pre.runFromString("lep psza1 pp1 \n" +
                "lep psza1 pcs1 \n" +
                "lep psza1 pf1 \n" +
                "allapot pf1 jatekosok \n" +
                "allapot pcs1 jatekosok"

        );
        String out = pre.getAllapotString();
        assertEquals("pf1 jatekosok: \n" +
                "psza1\n" +
                "pcs1 jatekosok:", out);
    }

    @Test
    @DisplayName("Csorol forrasra lep, ahol van ember")
    void ralep2() {
        pre.runFromString(
                "lep psza1 pp1 \n" +
                "lep psza1 pcs1\n" +
                "lep psza1 pf1\n" +
                "lep psze2 pcs3 \n" +
                "lep psze2 pp1 \n" +
                "lep psze2 pcs1 \n" +
                "lep psze2 pf1 \n" +
                "allapot pf1 jatekosok \n" +
                "allapot pcs1 jatekosok"
        );
        String out = pre.getAllapotString();
        assertEquals("pf1 jatekosok: \n" +
                "psza1\n" +
                "psze2\n" +
                "pcs1 jatekosok:"
, out);
    }

    @Test
    @DisplayName("Ciszternat letrehoz")
    void cizsternat_letrehoz() {
        pre.runFromString("letrehoz ciszterna tesztciszterna \n" +
                "allapot tesztciszterna termeltpumpak \n" +
                "allapot tesztciszterna szomszedok");
        String out = pre.getAllapotString();
        assertEquals("tesztciszterna termeltPumpak: \n" +
                "tesztciszterna szomszedok:", out);

    }

    @Test
    @DisplayName("Ciszternahoz sok csovet csatlakoztatok")
    void ciszterna_kapcsol() {
        pre.runFromString("letrehoz ciszterna tesztciszterna\n" +
                "letrehoz cso cso1\n" +
                "osszekot tesztciszterna cso1\n" +
                "letrehoz cso cso2\n" +
                "osszekot tesztciszterna cso2\n" +
                "letrehoz cso cso3\n" +
                "osszekot tesztciszterna cso3\n" +
                "letrehoz cso cso4\n" +
                "osszekot tesztciszterna cso4\n" +
                "letrehoz cso cso5\n" +
                "osszekot tesztciszterna cso5\n" +
                "letrehoz cso cso6\n" +
                "osszekot tesztciszterna cso6\n" +
                "letrehoz cso cso7\n" +
                "osszekot tesztciszterna cso7\n" +
                "letrehoz cso cso8\n" +
                "osszekot tesztciszterna cso8\n" +
                "letrehoz cso cso9\n" +
                "osszekot tesztciszterna cso9\n" +
                "letrehoz cso cso10\n" +
                "osszekot tesztciszterna cso10\n" +
                "letrehoz cso cso11\n" +
                "osszekot tesztciszterna cso11\n" +
                "letrehoz cso cso12\n" +
                "osszekot tesztciszterna cso12\n" +
                "letrehoz cso cso13\n" +
                "osszekot tesztciszterna cso13\n" +
                "letrehoz cso cso14\n" +
                "osszekot tesztciszterna cso14\n" +
                "letrehoz cso cso15\n" +
                "osszekot tesztciszterna cso15\n" +
                "letrehoz cso cso16\n" +
                "osszekot tesztciszterna cso16\n" +
                "letrehoz cso cso17\n" +
                "osszekot tesztciszterna cso17\n" +
                "letrehoz cso cso18\n" +
                "osszekot tesztciszterna cso18\n" +
                "letrehoz cso cso19\n" +
                "osszekot tesztciszterna cso19\n" +
                "letrehoz cso cso20\n" +
                "osszekot tesztciszterna cso20\n" +
                "letrehoz cso cso21\n" +
                "osszekot tesztciszterna cso21\n" +
                "allapot tesztciszterna szomszedok"
        );
        String out = pre.getAllapotString();
        assertEquals("tesztciszterna szomszedok: \n" +
                "cso1\n" +
                "cso10\n" +
                "cso11\n" +
                "cso12\n" +
                "cso13\n" +
                "cso14\n" +
                "cso15\n" +
                "cso16\n" +
                "cso17\n" +
                "cso18\n" +
                "cso19\n" +
                "cso2\n" +
                "cso20\n" +
                "cso3\n" +
                "cso4\n" +
                "cso5\n" +
                "cso6\n" +
                "cso7\n" +
                "cso8\n" +
                "cso9", out);
    }

    @Test
    @Disabled
    @DisplayName("Ciszternahoz aktiv elemet csatlakoztatok: ")
    void  aktiv_csati() {
        pre.runFromString("letrehoz ciszterna tesztciszterna\n" +
                "letrehoz pumpa p1\n" +
                "letrehoz ciszterna c1\n" +
                "letrehoz forras f1\n" +
                "allapot tesztciszterna szomszedok\n" +
                "osszekot tesztciszterna p1\n" +
                "allapot tesztciszterna szomszedok\n" +
                "osszekot tesztciszterna c1\n" +
                "allapot tesztciszterna szomszedok\n" +
                "osszekot tesztciszterna f1\n" +
                "allapot tesztciszterna szomszedok\n" +
                "osszekot tesztciszterna p1");
        String out = pre.getAllapotString();
        assertEquals("tesztciszterna szomszedok: \n" +
                "tesztciszterna szomszedok: \n" +
                "tesztciszterna szomszedok: \n" +
                "tesztciszterna szomszedok: \n", out);
    }

    @Test
    @DisplayName("Ciszterna vizet sziv")
    void ciszterna_sziv() {
        pre.runFromString("vizmennyiseg pcs6 1\n" +
                "vizmennyiseg pcs5 1\n" +
                "vizmennyiseg pcs7 1\n" +
                "frissit\n" +
                "allapot pcs6 vizmennyiseg\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "allapot pcs7 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs6 vizmennyiseg: 0\n" +
                "pcs5 vizmennyiseg: 0\n" +
                "pcs7 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("Ciszterna vizet sziv, de csovekben nincs viz")
    void ciszterna_sziv2() {
        pre.runFromString("vizmennyiseg pcs6 1\n" +
                "vizmennyiseg pcs7 1\n" +
                "frissit\n" +
                "allapot pcs6 vizmennyiseg\n" +
                "allapot pcs5 vizmennyiseg\n" +
                "allapot pcs7 vizmennyiseg"
        );
        String out = pre.getAllapotString();
        assertEquals("pcs6 vizmennyiseg: 0\n" +
                "pcs5 vizmennyiseg: 0\n" +
                "pcs7 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("Ciszterna uj pumpat hoz letre")
    void ujpumpa_by_ciszterna() {
        pre.runFromString("termel pc1 pumpa\n" +
                "allapot pc1 termeltpumpak");
        String out = pre.getAllapotString();
        assertEquals("pc1 termeltPumpak: \n" +
                "gen0", out);
    }

    @Test
    @DisplayName("Ciszternaba uj csovet hoznek letre, de nincs hely")
    void ciszterna_ujsco() {
        pre.runFromString(
                "letrehoz cso cs1\n" +
                        "osszekot cs1 pc1\n" +
                        "letrehoz cso cs2\n" +
                        "osszekot cs2 pc1\n" +
                        "letrehoz cso cs3\n" +
                        "osszekot cs3 pc1\n" +
                        "allapot pc1 szomszedok"
        );
        String out = pre.getAllapotString();
        assertEquals("pc1 szomszedok: \n" +
                        "cs1\n" +
                        "cs2\n" +
                        "cs3\n" +
                        "pcs5\n" +
                        "pcs6\n" +
                        "pcs7"
                , out);
    }

    @Test
    @DisplayName("Csorol pumpara lepek, majd vissza")
    void leptest() {
        pre.runFromString("lep psza1 pp1\n" +
                "allapot pp1 jatekosok\n" +
                "lep psza1 pcs3\n" +
                "allapot pcs3 jatekosok");
        String out = pre.getAllapotString();
        assertEquals("pp1 jatekosok: \n" +
                "psza1\n" +
                "pcs3 jatekosok: \n" +
                "psza1", out);
    }

    @Test
    @DisplayName("Csorol forrasra lepek, majd vissza")
    void lep2() {
        pre.runFromString("lep psza1 pp1\n" +
                "lep psza1 pcs1\n" +
                "lep psza1 pf1\n" +
                "allapot pf1 jatekosok\n" +
                "lep psza1 pcs1 \n" +
                "allapot pcs1 jatekosok"
        );
        String out = pre.getAllapotString();
        assertEquals("pf1 jatekosok: \n" +
                "psza1\n" +
                "pcs1 jatekosok: \npsza1", out);
    }

    @Test
    @DisplayName("Csorol pumpara lepek, majd vissza")
    void lep3() {
        pre.runFromString("lep psze1 pc1\n" +
                "allapot pc1 jatekosok\n" +
                "lep psze1 pcs5\n" +
                "allapot pcs5 jatekosok");
        String out = pre.getAllapotString();
        assertEquals("pc1 jatekosok: \n" +
                "psze1\n" +
                "pcs5 jatekosok: \n" +
                "psze1", out);
    }

    @Test
    @DisplayName("Kilyukasztok egy csovet")
    void lyuk() {
        pre.runFromString("lyukaszt psza1\n" +
                "allapot pcs3 mukodik\n" +
                "allapot pcs3 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs3 mukodik: false\n" +
                "pcs3 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("Ragadossa teszek")
    void ragacs() {
        pre.runFromString("ragad psza1\n" +
                "allapot pcs3 ragados");
        String out = pre.getAllapotString();
        assertEquals("pcs3 ragados: 5", out);
    }

    @Test
    @DisplayName("Letrehozok egy szerelot")
    void szerelo_megszuletik() {
        pre.runFromString("letrehoz szerelo sz1\n" +
                "allapot sz1 maxHatizsakKapacitas");
        String out = pre.getAllapotString();
        assertEquals("sz1 maxHatizsakKapacitas: 5", out);
    }

    @Test
    @DisplayName("Megfoltozok egy csovet")
    void csofoltozas() {
        pre.runFromString("lyukaszt psza1\n" +
                "lep psza1 pp1\n" +
                "lep psze2 pcs3\n" +
                "szerel psze2\n" +
                "allapot pcs3 mukodik");
        String out = pre.getAllapotString();
        assertEquals("pcs3 mukodik: true", out);
    }

    @Test
    @DisplayName("Szerelo felvesz egy pumpat a ciszternatol")
    void pumpatfelvesz() {
        pre.runFromString("lep psze1 pc1 \n" +
                "termel pc1 pumpa\n" +
                "felvesz psze1 pumpa \n" +
                "allapot psze1 pumpaHatizsak \n" +
                "allapot pc1 termeltPumpak"
        );
        String out = pre.getAllapotString();
        assertEquals("psze1 pumpaHatizsak: \n" +
                "gen0\n" +
                "pc1 termeltPumpak:", out);
    }

    @Test
    @DisplayName("Szerelo felvesz egy pumpat a hatizsakba, de nincs hely a szerelonel")
    void szerelu_pumpat_felvesz() {
        pre.runFromString("lep psze1 pc1\n" +
                "termel pc1 pumpa\n" +
                "termel pc1 pumpa\n" +
                "termel pc1 pumpa\n" +
                "termel pc1 pumpa\n" +
                "termel pc1 pumpa\n" +
                "termel pc1 pumpa\n" +
                "felvesz psze1 pumpa \n" +
                "felvesz psze1 pumpa \n" +
                "felvesz psze1 pumpa \n" +
                "felvesz psze1 pumpa \n" +
                "felvesz psze1 pumpa \n" +
                "felvesz psze1 pumpa \n" +
                "allapot pc1 termeltpumpak\n" +
                "allapot psze1 pumpaHatizsak"
        );
        String out = pre.getAllapotString();
        assertEquals("pc1 termeltPumpak: \n" +
                        "gen0\n" +
                        "psze1 pumpaHatizsak: \n" +
                        "gen1\n" +
                        "gen2\n" +
                        "gen3\n" +
                        "gen4\n" +
                        "gen5"
                , out);
    }

    @Test
    @DisplayName("Ciszternahoz kapcsolodo pumpat a hatizsakba teszek")
    void pumpa_hatizsak() {
        pre.runFromString("lep psze1 pc1\n" +
                "termel pc1 pumpa\n" +
                "felvesz psze1 pumpa \n" +
                "allapot psze1 pumpaHatizsak\n" +
                "allapot pc1 szomszedok");
        String out = pre.getAllapotString();
        assertEquals("psze1 pumpaHatizsak: \n" +
                "gen0\n" +
                "pc1 szomszedok: \n" +
                "pcs5\n" +
                "pcs6\n" +
                "pcs7", out);
    }

    @Test
    @DisplayName("Egy cso mindket veget felveszem")
    void felveszfel() {
        pre.runFromString("vizmennyiseg pcs7 1\n" +
                "lep psze1 pc1\n" +
                "felvesz psze1 cso pcs7 egesz\n" +
                "allapot pc1 szomszedok\n" +
                "allapot psze1 csoHatizsak\n" +
                "allapot pcs7 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pc1 szomszedok: \n" +
                "pcs5\n" +
                "pcs6\n" +
                "psze1 csoHatizsak: \n" + "pcs7\n" + "pcs7\n" +
                "pcs7 vizmennyiseg: 1", out);
    }

    @Test
    @DisplayName("Pumpa kimenetet lecsatlakoztatom")
    void pumpa_lecsatlakoztat() {
        pre.runFromString("vizmennyiseg pcs3 1\n" +
                "allit  psze2 pcs3 pcs4\n" +
                "felvesz cso pcs4 fel\n" +
                "frissit\n" +
                "allapot pcs4 vizmennyiseg");
        String out = pre.getAllapotString();
        assertEquals("pcs4 vizmennyiseg: 0", out);
    }

    @Test
    @DisplayName("Csovet hatizsakbol felcsatol")
    void csovet_felcsatol() {
        pre.runFromString("vizmennyiseg pcs3 1\n" +
                "allit psze2 pcs3 pcs4\n" +
				"felvesz psze2 cso pcs4 fel\n"
				+
                "allapot pp3 szomszedok\n" +
                "epit psze2 cso\n" +
                "allapot pp3 szomszedok\n" +
                "allapot psze2 csoHatizsak");
        String out = pre.getAllapotString();
        assertEquals("pp3 szomszedok: \n" +
                "pcs3\n" +
                "pcs5\n" +
                "pp3 szomszedok: \n" +
                "pcs3\n" +
                "pcs4\n" +
                "pcs5\n" +
                "psze2 csoHatizsak:", out);
    }

    @Test
    @DisplayName("Csovet teli pumpahoz csatol")
    void pumpacsatol() {
        pre.runFromString(
                "letrehoz cso cs0\n" +
                        "osszekot pp3 cs0\n" +
                        "letrehoz cso cs1\n" +
                        "osszekot pp3 cs1\n" +
                        "letrehoz cso cs2\n" +
                        "osszekot pp3 cs2\n" +
                        "letrehoz cso cs3\n" +
                        "osszekot pp3 cs3\n" +
                        "letrehoz cso cs4\n" +
                        "osszekot pp3 cs4\n" +
                        "letrehoz cso cs5\n" +
                        "osszekot pp3 cs5\n" +
                        "letrehoz cso cs6\n" +
                        "osszekot pp3 cs6\n" +
                        "allapot pp3 szomszedok");
        String out = pre.getAllapotString();
        assertEquals("pp3 szomszedok: \n" +
                "cs0\n" +
                "cs1\n" +
                "pcs3\n" +
                "pcs4\n" +
                "pcs5", out);
    }

    @Test
    @DisplayName("Pumpat epitek csovon")
    void pumpa_csovon() {
        pre.runFromString("lep psze1 pc1\n" +
                "termel pc1 pumpa\n" +
                "termel pc1 cso\n" +    //TODO: itt hogy van a cső létrehozás?
                "felvesz psze1 pumpa\n" +
                "felvesz psze1 cso gen0\n" +
                "lep psze1 pcs5\n" +
                "epit psze1 pumpa\n" +
                "allapot pcs5 szomszedok\n" +
                "allapot p1 szomszedok\n" +
                "allapot cs1 szomszedok"
        );
        String out = pre.getAllapotString();
        assertEquals("pcs5 szomszedok: \n" +
                "gen0\n" +
                "pc1", out);
    }

    @Test
    @DisplayName("Pumpat epitek csovon, aminek 1 vege van")
    void pumpa_epit() {
        pre.runFromString("lep psze1 pc1 \n" +
                "termel pc1 pumpa \n" +
                "termel pc1 cso \n" +
                "felvesz psze1 pumpa \n" +
                "felvesz psze1 cso pcs7 \n" +
                "lep psze1 pcs7\n" +
                "epit psze1 pumpa \n" +
                "allapot pcs7 szomszedok \n" +
                "allapot p1 szomszedok \n" +
                "allapot cs1 szomszedok"
        );
        String out = pre.getAllapotString();
        assertEquals("pcs7 szomszedok:", out);
    }

    @Test
    @DisplayName("Szabotor letrehozasa")
    void szabotor_birth() {
        pre.runFromString("letrehoz szabotor sz\n" +
                "allapot sz *");
        String out = pre.getAllapotString();
        assertEquals("sz maxHatizsakKapacitas: 5\n" +
                "sz pumpaHatizsak: \n" +
                "sz csoHatizsak:", out);
    }

    @Test
    @DisplayName("Cso csuszossa tetele")
    void csuszoss() {
        pre.runFromString("allapot pcs3 csuszos\n" +
                "csuszik psza1\n" +
                "allapot pcs3 csuszos");
        String out = pre.getAllapotString();
        assertEquals("pcs3 csuszos: 0\n" +
                "pcs3 csuszos: 5", out);
    }

}