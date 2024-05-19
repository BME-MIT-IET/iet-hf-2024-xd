package org.drukmakor;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * Egy absztrakt ősosztály, amiből az összes konkrét objektumnak a játékban lesz
 * egy "view"-ja, ami felelős a grafikus megjelenítéséért.
 */
public abstract class ObjectView {
	/**
	 * Kirajzolási tér mérete
	 */
	protected static final int CANVAS_WIDTH = 980;
	protected static final int CANVAS_HEIGHT = 740;

	/**
	 * Az eddig létrehozott összes view-t tartalmazó statikus lista.
	 */
	private static ArrayList<ObjectView> allViews = new ArrayList<>();
	protected Boolean perzisztens = false; // Ha nem törölhető egy nézet

	/**
	 * A view-k kirajzolásához használt bufferek (layerek z-index szerint) és a
	 * hozzájuk tartozó grafikák.
	 */
	private static ArrayList<BufferedImage> layers = new ArrayList<>(); // Maga a buffer
	private static ArrayList<Graphics> layerGraphics = new ArrayList<>(); // Rajzoláshoz Graphics
	private static ArrayList<Graphics2D> layerClear = new ArrayList<>(); // Törléshez Graphics
	private static int scale = 2; // Bufferek nagyítása

	/**
	 * Statikus bufferek(layerek) létrehozása
	 */
	static {
		for (int i = 0; i < 4; i++) {
			BufferedImage layer = new BufferedImage(CANVAS_WIDTH * scale, CANVAS_HEIGHT * scale,
					BufferedImage.TYPE_INT_ARGB);
			layers.add(layer);

			Graphics2D lg = layer.createGraphics();
			lg.scale(scale, scale);
			lg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			lg.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			lg.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
			layerGraphics.add(lg);

			Graphics2D clearGraphics = layer.createGraphics();
			clearGraphics.setComposite(AlphaComposite.Clear);
			layerClear.add(clearGraphics);
		}
	}

	/**
	 * Az objektum megjelenéséért felelős változók default értékekkel.
	 */
	protected int x = 0, y = 0; // Megjelenítés koordinátái
	protected int oldalmeret = 50; // View oldalmérete
	protected String nev = "?"; // Objektum neve
	protected Color nevSzin = Color.white; // Objektum nevének színe
	protected Boolean lathato = true; // Objektum láthatósága
	private static final Font font = new Font("Arial", Font.PLAIN, 14);

	public ObjectView() {
		allViews.add(this);
	}

	/**
	 * Amikor a hozzá tartozó objektum változott, akkor átadja magát a hozzá tartozó
	 * view-nak, ami ez alapján tudja frissíteni a grafikáját.
	 * @param m - Az objektum ami szól a viewnak hogy frissítsen.
	 */
	public void NotifyField(Mezo m) {};
	public void NotifyPlayer(Jatekos m) {};

	/**
	 * A viewhez tartozó grafika rárajzolása az adott bufferre.
	 * @param layers - A bufferek amikre rajzolni kell.
	 */
	public abstract void Draw(ArrayList<Graphics> layers);

	/**
	 * A view frissítése egy animációs lépéssel, ha a view támogat animációt.
	 */
	public void Animate() {
	};

	/**
	 * Beállítja az objektum megjelenítésekor használt nevét.
	 * @param n - Objektum neve
	 */
	public void SetNev(String n) {
		nev = n;
	}

	/**
	 * Beállítja hogy milyen koordinátán rajzolódjon ki a nézet
	 */
	public void setHely(int posX, int posY) {
		x = posX;
		y = posY;
	}

	/**
	 * Beállítja hogy milyen koordinátán rajzolódjon ki a nézet
	 */
	public void setLathato(boolean l) {
		lathato = l;
	}

	/**
	 * Visszaadja az objektum középpontját, ami nem feltétlenül a x vagy y. A
	 * leszármazott döntiel hogy hova teszi a középpontját (pl.: Cső)
	 * @return Középpont X koordinátája
	 */
	public int getKozepX() {
		return x;
	}

	/**
	 * Visszaadja az objektum középpontját, ami nem feltétlenül a x vagy y. A
	 * leszármazott döntiel hogy hova teszi a középpontját (pl.: Cső)
	 * @return Középpont Y koordinátája
	 */
	public int getKozepY() {
		return y;
	}

	/**
	 * Kiszámolja hogy a view milyen távol van az adott ponttól. Ha a view nem
	 * definiálja felül, a default implementáció egy 'oldalmeret' átlójú kört tekint
	 * hitboxnak.
	 * @param px - A pont x koordinátája
	 * @param py - A pont y koordinátája
	 * @return View távolsága a ponttól
	 */
	public double getDistanceFromPoint(int px, int py) {
		int dx = Math.abs(px - x);
		int dy = Math.abs(py - y);
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Visszadaja az összes eddig léterhozott nézetet.
	 * @return Az összes eddig létrehozott nézet listája.
	 */
	static public ArrayList<ObjectView> GetAllViews() {
		return allViews;
	}

	/**
	 * Törli az összes eddig léterhozott nézetet.
	 */
	static public void RemoveAllViews() {
		allViews.removeIf(s -> !s.perzisztens);
	}

	/**
	 * A leszármazottaknak segédfüggvény, ami segítségével kirajzolhatják a nevüket
	 * stringként az adott graphics bufferre.
	 * 
	 * @param g    - A graphics amire rajzol
	 * @param posX - A szöveg közepének a koordinátája
	 * @param posY - A szöveg közepének a koordinátája
	 */
	protected void DrawName(Graphics g, int posX, int posY) {
		// Font metrikái alapján a szöveg helyének kiszámítása
		FontMetrics metrics = g.getFontMetrics(font);
		int textX = posX - (metrics.stringWidth(nev) / 2);
		int textY = posY + (metrics.getAscent() - metrics.getDescent()) / 2;

		g.setFont(font);
		g.setColor(Color.black);

		// Körvonal kirajzolása
		int outlineSize = 1;
		for (int dx = -outlineSize; dx <= outlineSize; dx++) {
			for (int dy = -outlineSize; dy <= outlineSize; dy++) {
				g.drawString(nev, textX + dx, textY + dy);
			}
		}

		// Szöveg kirajzolása
		g.setColor(nevSzin);
		g.drawString(nev, textX, textY);
	}

	/**
	 * Kirajzol egy adott Graphics-ra minden view-t.
	 * @param g        - A Graphics amire a kirajzolás történik
	 * @param darkMode - A téma színéhez illő háttért állítja be rajzolásnál.
	 */
	public static synchronized void DrawAllViews(Graphics g, Boolean darkMode) {
		Graphics2D out = (Graphics2D) g;
		out.scale(1.0d / scale, 1.0d / scale);
		int scaledWidth = CANVAS_WIDTH * scale;
		int scaledHeight = CANVAS_HEIGHT * scale;

		// Előző kép törlése
		if (darkMode) {
			out.setColor(new Color(130, 130, 130));
			out.fillRect(0, 0, scaledWidth, scaledHeight);
		} else {
			out.clearRect(0, 0, scaledWidth, scaledHeight);
		}

		// Nézetek bufferekbe rajzolása
		for (ObjectView view : allViews) {
			view.Animate();
			view.Draw(layerGraphics);
		}

		// Bufferek kirajzolása és törlése
		for (int i = 0; i < layers.size(); i++) {
			out.drawImage(layers.get(i), 0, 0, null);
			layerClear.get(i).fillRect(0, 0, scaledWidth, scaledHeight);
		}

	}

	/**
	 * Az animációk léptetéséért és kirajzolásért felelős szál.
	 */
	private static Thread animationThread;

	/**
	 * Elindítja a view-k animációját ha még nem volt elindítva.
	 * @param drawpanel - a napel amit újra kell rajzolni.
	 */
	public static void StartAnimation(JPanel drawpanel) {
		// Csak egy szál jöhet létre animációkhoz
		if (animationThread != null)
			return;

		// 60 fps-el való kirajzolás
		animationThread = new Thread(() -> {
			while (true) {
				drawpanel.repaint();
				try {
					Thread.sleep(1000 / 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		animationThread.setDaemon(true);
		animationThread.start();
	}

}
