package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import objects.Animations;
import objects.Era;
import objects.Particle;
import objects.eras.Era0;
import objects.eras.Era1;

public class UniverseCanvas extends Canvas implements MouseListener, MouseMotionListener, FocusListener, KeyListener{

	private BufferStrategy strategy;
	private final Timer timer;
	private TimerTask renderTask;

	boolean statsMenu = false;
	int statsMenuCounter = 40;
	ArrayList<Animations> clicks = new ArrayList<Animations>();
	ArrayList<Particle> particles = new ArrayList<Particle>();
	
	UniverseData data = new UniverseData();
	final Era[] eras = {new Era0(data), new Era1(data)};
	int currentEra = 0;
	
	public int[] DEBUGVAR = new int[100];
	
	
	ArrayList<MouseEvent> mouseBuffer = new ArrayList<MouseEvent>();
	ArrayList<KeyEvent> keyBuffer = new ArrayList<KeyEvent>();

	static HashMap<String, Image> imgMap = new HashMap<String, Image>();
	static HashMap<String, Font> fontMap = new HashMap<String, Font>();
		
	int mx=0,my=0;
	
	double dt;
	public UniverseCanvas() {
		this.setIgnoreRepaint(true);
		this.setPreferredSize(new Dimension(800,600));
		timer = new Timer();
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
        this.addFocusListener(this);
        this.addKeyListener(this);

	}

	public void render() {
		
		Graphics2D bkG = (Graphics2D) strategy.getDrawGraphics();

		if (DEBUGVAR[2] == 0)
		bkG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
//		bkG.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		drawBackground(bkG);
		bkG.clipRect(83, 130, 430, 430);
		drawClickArea(bkG);
		bkG.setClip(null);
//		drawClicks(bkG);	
//		drawParticles(bkG);
		if (DEBUGVAR[3]==0)
			drawGui(bkG);
		drawText(bkG);
		if (DEBUGVAR[5]==1)
		drawDebug(bkG);
		drawExtras(bkG);
		
		bkG.dispose();

		strategy.show();

		Toolkit.getDefaultToolkit().sync();
	}


	private void drawDebug(Graphics2D g) {
		g.setColor(Color.RED);
		g.setFont(new Font("Lucida Console", Font.PLAIN, 8));
		g.drawString(""+dt, 0, 10);
		
		if (1/dt<40)
			g.setColor(Color.WHITE);
		g.drawString("FPS "+(int)(1/dt), 0, 20);
		if (1/dt<40)
			g.setColor(Color.RED);
		
//		g.drawString("Limited FPS "+Math.min(40,(int)(1/dt)), 0, 30);
		int sum = 1;
		for (int i = 10; i <= 20; i++)
			sum += DEBUGVAR[i];
		g.drawString("AVG FPS "+ (11000/sum),0,30);
		g.drawString("P"+particles.size(), 0, 40);
		g.drawString(mx +", " +my, 0, 50);
		for (int i = 0; i < 10; i++)
			g.drawString(""+DEBUGVAR[i], 80, i*10+10);
		g.drawString(data.energy+"", 120, 20);
		
		if (DEBUGVAR[4]==1)
		{
			g.drawLine(0, my, 800, my);
			g.drawLine(mx, 0, mx, 600);
		}
		for (int i = 0; i < 18; i++)
		{
			int x = 2*i%7;
			int y = 2*i/7;
			g.drawOval(564+27*x, 209+47*y, 53, 53);
		}
		for (int i = 0; i < 3; i++)
		{
			g.drawRect(556, 10+i*60, 55,55);
		}
	}

	private void doMouseEvents()
	{
		while (mouseBuffer.size() > 0)
		{
			MouseEvent e = mouseBuffer.get(0);
			mouseBuffer.remove(0);
			if (e == null)
				continue;
			mx = e.getX();
			my = e.getY();
			if (new Rectangle(500, 100, 27, 16).contains(mx, my))
			{
				statsMenu = true;
				statsMenuCounter = 0;
			}
			if (new Rectangle(500, 575, 27, 16).contains(mx, my))
			{
				statsMenu = false;
				statsMenuCounter = 0;
			}
			if (new Rectangle(83, 130, 430, 430).contains(mx, my))
			{
				eras[currentEra].doMouseEvents(e);
			}
			if (mx < 54 && my > 136 && my<136+46*Math.min(9, eras.length))
			{
				int era = (my-136)/46;
				if (era != currentEra && data.eraAvailable[era])
				{
					if (e.getButton()!=MouseEvent.NOBUTTON)
					{
						eras[currentEra].unload();
						currentEra = era;
						eras[currentEra].load();
					}
					else
					{
						
					}
				}
			}
			int upgrade = 0;
			for (int i = 0; i < eras[currentEra].getNumUpgrade(); i++)
			{
				while (!eras[currentEra].upgradeAvailable(upgrade))
					upgrade++;
				int x = 2*i%7;
				int y = 2*i/7;
				if (getDistance(mx,my,590+27*x,235+47*y) <= 22)
				{
					if (e.getButton() != MouseEvent.NOBUTTON)
					{
						eras[currentEra].getUpgrades(upgrade);
					}
				}
				upgrade++;
			}
			for (int i = 0; i < eras[currentEra].getNumBuilding(); i++)
			{
				if (new Rectangle(556, 10+i*60, 55,55).contains(mx, my))
				{
					if (e.getButton() != MouseEvent.NOBUTTON)
					{
						eras[currentEra].getBuilding(i);
					}
				}
			}
		}
	}
	
	private double getDistance(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}

	private void doKeyEvents()
	{
		while (keyBuffer.size() > 0 )
		{
			KeyEvent e = keyBuffer.get(0);
			keyBuffer.remove(0);
			if (e.getKeyCode() == KeyEvent.VK_1) //pause calcs
				DEBUGVAR[1] = 1 - DEBUGVAR[1];
			if (e.getKeyCode() == KeyEvent.VK_2) // anti aliasing
				DEBUGVAR[2] = 1 - DEBUGVAR[2];
			if (e.getKeyCode() == KeyEvent.VK_3) //gui
				DEBUGVAR[3] = 1 - DEBUGVAR[3];
			if (e.getKeyCode() == KeyEvent.VK_4) //cursor position
				DEBUGVAR[4] = 1 - DEBUGVAR[4];
			if (e.getKeyCode() == KeyEvent.VK_5) //debug
				DEBUGVAR[5] = 1 - DEBUGVAR[5];
			if (e.getKeyCode() == KeyEvent.VK_6)
				DEBUGVAR[6] = 1 - DEBUGVAR[6];
			if (e.getKeyCode() == KeyEvent.VK_7)
				DEBUGVAR[7] = 1 - DEBUGVAR[7];
			if (e.getKeyCode() == KeyEvent.VK_8)
				DEBUGVAR[8] = 1 - DEBUGVAR[8];
//			if (e.getKeyCode() == KeyEvent.VK_E)
//				data.energy.add(100000000);
		}
//		System.out.println("Clicked: " + e.getX() + ", " + e.getY());
	}
	
	private void calculations() {
		eras[currentEra].calculations();
		data.calculations();
		
//		if (statsMenuCounter < 40)
//			statsMenuCounter++;
		for (int i = 0; i < clicks.size(); i++)
		{
			clicks.get(i).calculate();
			if (clicks.get(i).isDone())
			{
				clicks.remove(clicks.get(i));
			}
		}
		for (int i = 0; i < particles.size(); i++)
		{
			particles.get(i).calculate();
			if (particles.get(i).isDone())
			{
				particles.remove(particles.get(i));
			}
		}
		DEBUGVAR[0]++;
		if (DEBUGVAR[0]%25==0)
		{
			DEBUGVAR[9] = (int)data.energy.value - DEBUGVAR[8];
			DEBUGVAR[8] = (int)data.energy.value;
		}
    }

	private void drawClickArea(Graphics2D g) {
		eras[currentEra].drawClickArea(g);
	}

//	private void drawClicks(Graphics2D g) {
//		for (int i = 0; i < clicks.size(); i++)
//		{
//			clicks.get(i).draw(g);
//		}
//	}
//	
//	private void drawParticles(Graphics2D g) {
//		for (int i = 0; i < particles.size(); i++)
//		{
//			particles.get(i).draw(g);
//		}
//	}
	
	private void drawGui(Graphics2D g) {
		g.drawImage(getPicture("gui/VertiDividerThick.png"), 534, 0,11,600, null);
		g.drawImage(getPicture("gui/HorizDivider.png"), 544, 462,256,6, null);
		g.drawImage(getPicture("gui/RightTDivider.png"), 534, 462, null);

		g.drawImage(getPicture("gui/MainFrame.png"),  73, Math.max(100,120),522,569,0, Math.max(100-120,0), 450, 450, null);
		g.drawImage(getPicture("gui/HorizDividerThick.png"), 0, 100,534,10, null);
		g.drawImage(getPicture("gui/StatsButton.png"), 500, 100, null);
		g.drawImage(getPicture("gui/LeftTDivider.png"), 534, 100, null);
		g.drawImage(getPicture("gui/DownTDivider.png"), 55, 100, null);
		g.drawImage(getPicture("gui/VertiDivider.png"), 55, 100+10,6,490, null);
//
//		if(!statsMenu)
//		{
//			int slideAnim = Math.max(575-statsMenuCounter*statsMenuCounter*2, 100);
////			g.drawImage(getPicture("gui/MainFrame.png"), 73, 120, null);
//			if (slideAnim < 558)
//		}
//		else
//		{
//			int slideAnim = Math.min(575, 100+statsMenuCounter*statsMenuCounter*2);
//			if (slideAnim < 558)
//				g.drawImage(getPicture("gui/MainFrame.png"),  73, Math.max(slideAnim,120),522,569,0, Math.max(slideAnim-120,0), 450, 450, null);
//			g.drawImage(getPicture("gui/HorizDividerThick.png"), 0, slideAnim,534,10, null);
//			g.drawImage(getPicture("gui/CloseButton.png"), 500, slideAnim, null);
//			g.drawImage(getPicture("gui/LeftTDivider.png"), 534, slideAnim, null);
//			g.drawImage(getPicture("gui/DownTDivider.png"), 55, slideAnim, null);
//			g.drawImage(getPicture("gui/VertiDivider.png"), 55, slideAnim+10,6,490, null);
//		}

		g.drawImage(getPicture("gui/UpArrow.png"), 10, 115, null);
		g.drawImage(getPicture("gui/DownArrow.png"), 10, 575, null);
		for (int i = 0; i < Math.min(9, eras.length); i++)
		{
			if (eras[i].selected)
			{
				g.drawImage(getPicture("gui/SelectedTab.png"), 2, 136+i*48, null);
			}
			else if (data.eraAvailable[i]) 
			{
				g.drawImage(getPicture("gui/UnselectedTab.png"), 2, 136+i*48, null);
			}
			else
			{
				g.drawImage(getPicture("gui/UnlockableTab.png"), 2, 136+i*48, null);
			}
//			else
//			{
//				g.drawImage(getPicture("gui/LockedTab.png"), 2, 136+i*48, null);
//			}
		}
		
		for (int i = 0; i < eras[currentEra].getNumBuilding(); i++)
		{
			g.drawImage(getPicture("gui/Building.png"), 556, 10+i*60, null);
			

			g.setColor(Color.BLACK);
			g.setFont(getFont("whitrabt.ttf").deriveFont(40f));
			g.drawString(""+i, 572, 52+i*60);
			g.setColor(Color.WHITE);
			g.setFont(getFont("Goodwill.otf").deriveFont(12f));
			drawString(g, eras[currentEra].getBuildingDescription(i), 618, 29+i*60);
		}
		int upgrade = 0;
		for (int i = 0; i < eras[currentEra].getNumUpgrade(); i++)
		{
			while (!eras[currentEra].upgradeAvailable(upgrade))
				upgrade++;
			int x = 2 * i % 7;
			int y = 2 * i / 7;
			
			g.drawImage(getPicture("gui/Upgrade.png"), 566 + 27 * x,
					208 + 47 * y, null);

			g.setColor(Color.BLACK);
			g.setFont(getFont("whitrabt.ttf").deriveFont(40f));
			FontMetrics fm = getFontMetrics(getFont("whitrabt.ttf").deriveFont(
					40f));
			int width = fm.stringWidth("" + upgrade);
			g.drawString("" + upgrade, 590 + 27 * x - width / 2, 248 + 47 * y);
			
			if (getDistance(mx, my, 590 + 27 * x, 235 + 47 * y) <= 25)
			{
				g.setColor(Color.white);
				g.setFont(getFont("modenine.ttf").deriveFont(12f));
				String s = eras[currentEra].getUpgradeData(upgrade);
				drawStringToEdge(g, s, 550, 480, 250);
			}
			upgrade++;
		}
		for (int i = 0; i < eras[currentEra].getNumBuilding(); i++)
		{
			if (new Rectangle(556, 10 + i * 60, 55, 55).contains(mx, my))
			{
				g.setColor(Color.white);
				g.setFont(getFont("modenine.ttf").deriveFont(12f));
				String s = eras[currentEra].getBuildingData(i);
				drawStringToEdge(g, s, 550, 480,250);
				break;
			}
		}
	}

	private void drawText(Graphics2D g) {
		eras[currentEra].drawText(g);
	}
	
	private void drawBackground(Graphics2D g) {
		g.drawImage(getPicture("gui/Background.png"), 0, 0, null);
	}

	private void drawExtras(Graphics2D g) {
		eras[currentEra].drawExtras(g);
	}
	
	public static Image getPicture(String fileName) {
		Image img = null;
		if (imgMap.containsKey(fileName))
		{
			img = imgMap.get(fileName);
		}
		else
		{
			try
			{
				img = ImageIO.read(UniverseCanvas.class
						.getResourceAsStream("/images/" + fileName));
				
				imgMap.put(fileName, img);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static Font getFont(String fileName)
	{
		Font f = new Font("Arial", Font.PLAIN, 12);
		if (fontMap.containsKey(fileName))
		{
			f = fontMap.get(fileName);
		}
		else
		{
			try
			{
				f = Font.createFont(Font.TRUETYPE_FONT, UniverseCanvas.class
						.getResourceAsStream("/fonts/" + fileName));
				fontMap.put(fileName, f);
				
			} catch (Exception e)
			{
				System.err.println("Unable to find font: "+fileName);
				System.err.println("Using default: \"Arial\"");
				fontMap.put(fileName, f);
			}
		}
		
		return f;
	}
	
	
	public void setup() {
		this.createBufferStrategy(2);
		strategy = this.getBufferStrategy();
    	currentEra = 0;
		eras[currentEra].load();
    	data.eraAvailable[0] = true;
		start();
	}	

	public void start() {
		if (renderTask != null) {
			renderTask.cancel();
		}


		renderTask = new TimerTask() {

			@Override
			public void run() {
				long lasttime = System.currentTimeMillis();
				doMouseEvents();
				doKeyEvents();
				if (DEBUGVAR[1] == 0)
					calculations();
				render();
				long time = System.currentTimeMillis();
				dt = (time - lasttime)/1000.;
				for (int i = 20; i > 10; i--)
					DEBUGVAR[i] = DEBUGVAR[i-1];
				DEBUGVAR[10] = (int) (dt*1000);
			}
		};
//		while (true)
//		{
//			calculations();
//			render();
//		}		
		timer.scheduleAtFixedRate(renderTask, 0, 25);
	}

	protected void stop() {
		renderTask.cancel();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseBuffer.add(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseBuffer.add(e);
		
//		if (Math.random() > .9 && new Rectangle(83,130,430,430).contains(e.getX(), e.getY()))
//		{
////			clicks.add(new Animations("/images/era0/Click",5,5,e.getX()-5,e.getY()-5));
//			Particle p = new Particle(e.getX(),e.getY());
//			p.setColor(Color.white);
//			p.setAura(new Color(198,198,255,32));
//			particles.add(p);
//			data.energy++;
//		}
////		System.out.println("Moved: " + e.getX() + ", " + e.getY());
	}

	@Override
	public void focusGained(FocusEvent arg0)
	{	
	}
	@Override
	public void focusLost(FocusEvent arg0)
	{
//		System.exit(0);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		keyBuffer.add(e);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	void drawString(Graphics2D g, String text, int x, int y)
	{
//		y -= g.getFontMetrics().getHeight();
		for (String line : text.split("\n"))
		{
			g.drawString(line, x, y);
			y += g.getFontMetrics().getHeight();
		}
	}

	void drawStringToEdge(Graphics2D g, String text, int x, int y, int width)
	{
		int lastSplit = 0;
		String curLine = "";
		int h = g.getFontMetrics().getHeight();
		for (int i = 0; i <= text.length(); i++)
		{
			if (i == text.length() || Character.isWhitespace(text.charAt(i)))
			{
				String word = text.substring(lastSplit, i).trim();
				lastSplit = i + 1;
				if (g.getFontMetrics().stringWidth(word) > width)
				{
					System.err.println(word + " doesn't fit in width");
					g.drawString(word, x, y);
					return;
				}
				if (g.getFontMetrics().stringWidth(curLine + " " + word) >= width)
				{
					g.drawString(curLine, x, y);
					curLine = word;
					y += h;
				}
				else if (i == text.length() || text.charAt(i) == '\n')
				{
					if (!curLine.isEmpty())
					{
						curLine += " ";
					}
					curLine += word;
					g.drawString(curLine, x, y);
					curLine = "";
					y += h;
				}
				else
				{
					if (!curLine.isEmpty())
					{
						curLine += " ";
					}
					curLine += word;
				}
			}
		}
		if (!curLine.isEmpty())
			g.drawString(curLine, x, y);

	}
}
