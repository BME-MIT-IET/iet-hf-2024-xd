package org.drukmakor;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Main osztály
 */
public class Main {
    public static Pontszamlalo pontsz = new Pontszamlalo();

    public static void main(String[] args) {



        //VIEW-OS PARANCSÉRTELMEZO PÉLDA:

        //Először view létrehozása
        ParancsErtelmezoView pev = new ParancsErtelmezoView();
        //Aztán jön a ParancsErtelmezo.
        //A parancsértelmezőnek átadjuk a view-t konstruktorban, hogy tudja, hogy hova küldje a kimenetet. Ezzel a viewban is beállítódik a parancsértelmező automatikusan.
        //Tehát mást nem kell állítgatni, csak a view-t létrehozni, és a parancsértelmezőt létrehozni, és konstruktorban átadni neki a view-t.
        //Alternatív megoldás, hogy a parancsértelmezőt létrehozod, és a viewt létrehozod, majd a ParancsErtelmezo SetView(...) függvényével beállítod neki a viewt.
        //Ez is kölcsönös, tehát a view itt is beállítja a parancsértelmezőt magának automatikusan.
        ParancsErtelmezo pe2 = new ParancsErtelmezo(pev);

        //A parancsértlmezőben most már van olyan hogy debug mód. Ilyenkor minden parancs elérthető. A debug mód alapból be van kapcsolva
        //Amikor pályát inicializálsz, akkor muszáj hogy be legyen kapcsolva, mert a hozzá szükséges parancsok debug módosak.
        pe2.runFromFile("commandfiles/alap"); //Alap pálya inicializálása mondjuk.

        //Miután inicializáltad a pályát, kapcsold ki a debug módot, hogy ne lehessen elérni a debug parancsokat játék közben.
        pe2.EnableDebugMode(false);

        //Alapból a parancsértelmező a standard kimenetre írja a kimenetet. Ha azt akarjuk hogy a view kapja meg az output-ot, akkor azt be kell állítani így:
        pe2.OutputToView(true);
        Grafika gr = new Grafika(pev, pe2);
        //Ezután már a view fogja megkapni a kimenetet, és a viewnak kell majd továbbítania a kimenetet a megfelelő TextBox-nak. (Lásd: ParancsErtelmezoView.ReceiveFromPE)




        //A view is tud természetesen parancsokat küldeni a parancsértelmezőnek.
        //Ezt kell majd hivogatni amikor az Input TextBox-ba beírnak egy parancsot és megnyoomják a gombot hogy ok
        //pev.SendToPE("allapot pcs1 vizmennyiseg"); //Példa parancs küldésre a viewból a parancsértelmezőnek. Nyilván valójában a TextBox-ból jön majd a parancs.
    }
}
