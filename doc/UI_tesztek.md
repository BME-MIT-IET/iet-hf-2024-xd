# Swinget használó Java Alkalmazás UI Tesztelése

### <u> Eredmények </u> 
A tesztek sikeresen ellenőrzik a különböző játék funkciók és parancsok helyes működését, 
beleértve a **hibás use-case-k, paraméterek** kezelését és a parancsok által végzett műveletek **helyességét**.

### <u> A használt eszközről </u>
A Swinges UI tesztelése során az **AssertJ**-t alkalmaztam, amely lehetővé tette a grafikus felhasználói 
felület (GUI) automatizált tesztelését. 

Az AssertJ hasonló az ennél népszerűbb, webes felületek UI tesztelésére alkalmazott **Selenium**-hoz,
abban, hogy az eszköz képes a **felhasználói interakciók szimulálására**, mintha azok egy valódi felhasználó cselekvései lennének. 

A tesztek alapvetően úgy működnek, hogy az AssertJ **megnyitja az alkalmazás ablakát**, majd az egyes tesztek során végigkattingatja 
a megadott gombokat, kitöltöti a szövegmezőket, a **tesztben leírtak szerint** és megfigyeli a GUI különböző elemeinek elvárt 
**állapotváltazásait**.

Ez a megközelítés lehetővé tette, hogy ellenőrizzem, a felhasználói interakciók, use-casek helyesen működnek-e, és biztosította, hogy az
alkalmazás minden eleme megfelelően reagáljon a különböző parancsokra és eseményekre.

### <u> Tanulságok </u>
**Az automatizált UI Tesztelés Fontossága:** Az automatizált tesztelés lehetővé teszi a különböző use-case-k, funkciók gyors és hatékony 
tesztelését, **emberi beavatkozás igénye nélkül**. Ez biztosítja, hogy bármilyen változtatás esetén, amit az alkamlazáson végzünk, minden 
kívánt **UI funkció továbbra is helyesen fog működni**. 

Ez különösen fontos **nagy rendszerek** esetén, ahol a **manuális tesztelés időigényes** és hibára hajlamos lehet.

Az automatizált tesztek segítettek **beazonosítani** és kijavítani néhány **hibás működést** az alkalmazásban. 
Ez növelte a rendszer funkcionalitását és legfőképp a **felhasználói élményt**.

Összefoglalva, a UI tesztelés segíteni fog a rendszer helyes működésének biztosításában, frissítések, javítások, bővítések esetén, 
amik a jövőben bekövetkezhetnek. Nem utolsó sorban segített megnövelni az alkalmazás nyújtotta felhasználói élményt, valamint 
segített megértenem az automatizált tesztelés fontosságát UI-ok esetében is.