# **Manuális tesztek java környezetben készült alkalmazáshoz**

### Manuális tesztek célja és jelentőssége, eredmények
A manuális tesztek a leginkább a felhasználó szemszögéből próbálják feltárni az alkalmazás esetleges hibáit. Ezt próbáltam szemléltetni az egyes teszteknél. Próbáltam a felhasználó fejével gondolkodni és azokat a funkciókat kivizsgálni, amik a kész program esetében előjönnek.  
 Minden tesztesetnél felvázoltam, hogy mi az elvárt, helyes működés és összevetettem a valóssal. Egy-egy tesztesetet egy közös minta alapján dokumentáltam a jobb átláthatóság érdekében. Továbbá némelyik tesztesetnél egyéb megemlítendő észrevétellel jeleztem ha valami szokatlan működést találtam. Minden cselekvésre, játékos interakcióra és az automatikus folyamatok hatásaira is kitértem, egy egészként kezelve őket. Minden esetben próbáltam az adott részfunkciók határait feszegetni, hiszen minden lehetséges interakcióra, bemenetre fel kell készülni.

### Tanulságok

A manuális tesztelés sokszor időigényesebb, mint az automatikus, kódból történő tesztelés. Emiatt jól kell megválasztani, hogy milyen funkciókat milyen módon tesztelünk. Vannak olyan esetek viszont, amikor az automatikus teszteléshez túl specifikus szituációt szeretnénk tesztelni. Ez különösen jelen van hasonló játékoknál mint a jelen projekt is, hiszen annyi féle játékszituáció kialakítható, amit automatikusan nagyon nehéz lenne nagy lefedettséggel tesztelni.  
Ugyanakkor azt vettem észre, hogy ellenben nagyon könnyű hibákat véteni manuális teszteléskor, vagy sok esetben átsiklani olyan részletek felett ami szükséges lehet a program működésének zavartalanságához.