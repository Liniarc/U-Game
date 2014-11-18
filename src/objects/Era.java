package objects;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import main.UniverseCanvas;
import main.UniverseData;

public abstract class Era {

	public boolean selected;

	protected UniverseData data;
	
	protected Era(UniverseData d)
	{
		data = d;
	}
	
	public abstract void drawClickArea(Graphics2D g);
	
	public abstract void drawText(Graphics2D g);
	
	public abstract void drawAnimation(Graphics2D g);

	public abstract void drawExtras(Graphics2D g);
	
	public abstract void getUpgrades(int i);
	
	public abstract void getBuilding(int i);

	public abstract int getNumBuilding();
	
	public abstract int getNumUpgrade();

	public abstract boolean upgradeAvailable(int i);
	
	public abstract String getBuildingDescription(int num);
	
	public abstract String getBuildingData(int i);

	public abstract String getUpgradeData(int i);
	
	public abstract void doMouseEvents(MouseEvent e);

	public abstract void calculations();
	
	public void load()
	{	
		selected = true;
	}
	
	public void unload()
	{
		selected = false;
	}


	
	
}
