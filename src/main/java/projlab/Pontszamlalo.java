package projlab;

/**
 * A pontok számlálásáért felelős osztály
 */
public class Pontszamlalo {
    private int szabotorPoint, szereloPoint = 0;
    private int currRound = 1;
    private int maxRound = 20;

    protected PontszamlaloView view = new PontszamlaloView();

    /**
     * Szabotőrök pontszának növelése
     * @param p a pont, amennyivel nő a pontszám
     */
    public void szabotorPontotNovel(int p){
        szabotorPoint += p;
		view.Notify(this);
    }

    /**
     * Szerelők ponszámának növelése
     * @param p a pont, amennyivel nő a pontszám
     */
    public void szereloPontotNovel(int p){
        szereloPoint += p;
		view.Notify(this);
    }

    /**
     * Kör növelése 1-el
     */
    public void korNovel(){
        currRound += 1;
		view.Notify(this);
    }

    /**
	 * Visszaállítja a pontszámokat
	 */
	public void reset() {
		szabotorPoint = szereloPoint = 0;
		currRound = 1;
		view.Notify(this);
	}

	/**
	 * Jelenlegi kör értékének lekérdezése
	 * @return jelenlegi kör száma
	 */
    public int getCurrRound() {
        return currRound;
    }

    /**
     * Szabotőrök pontjanak lekérdezése
     * @return szabotőrök pontja
     */
    public int getSzabotorPoint() {
        return szabotorPoint;
    }

    /**
     * Szerelők pontjanak lekérdezése
     * @return szerelők pontja
     */
    public int getSzereloPoint() {
        return szereloPoint;
    }
}
