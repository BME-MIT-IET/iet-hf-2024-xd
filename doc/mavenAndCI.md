Maven build keretrendszer beüzemelése + GitHub Actions CI beüzemelése

A feladat elvégzéséhez létrehoztam a git projekt lokális verzióját. Ezt követően létrehoztam egy fejlesztési ágat a keretrendszer beüzemelésére.
Az eredeti projekt más fordítási keretrendszert használt, az ehhez tratozó fájlokat eltávolítottam. Majd a forráskódot meg az annak működéséhez szükséges erőforrásokat átszerveztem egy új mappastruktúrába. A Maven beüzemeléséhez szükséges volt a pom.xml fájl megfelelő konfigurálása, többek közt definiáltam benne a java verziót, a szükséges függőségeket, az útvonalat az erőforrásokhoz stb. Ezután az "mvn complie" parancs többszörös lefuttatásával debuggoltam a projektet, a forráskódon minimális változtatás kellett(például a .java fájlok package láthatósága). Sikeres fordulás után az "mvn package" parancs kiadásával létrehoztam az új .jar binárist az alkalmazás futtatásához, ezt a fájlt a target mappából elindítva lefutott helyesen a csapat áltat kiválasztott szoftver projekt. 

A folytonos integráció beüzemeléséhez a projekt webes felületén létrehoztam a YAML fájlt és lefuttattam a CI-t. Ez párszor hibás futással végződött, mivel eltérések voltak a pom.xml által definiált projektben. Ezeket a változtatásokat lokálisan elvégeztem és újabb hiba(például java verzió) esetén megismételtem. A fejlesztési ágat csak a sikeres futás esetén olvasztottam be a fő ágra.

