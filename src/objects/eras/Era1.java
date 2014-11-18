package objects.eras;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.UniverseCanvas;
import main.UniverseData;
import objects.Era;
import objects.Particle;

public class Era1 extends Era
{
	ArrayList<Particle> particles = new ArrayList<Particle>();
	
	int count = 0;
	
	public Era1(UniverseData d)
	{
		super(d);
	}

	@Override
	public void drawClickArea(Graphics2D g)
	{
		int light = count*3+10;
		g.setColor(new Color(light,light,light));
		g.fillRect(83, 130, 430, 430);
		for (int i = 0; i < particles.size(); i++)
		{
			particles.get(i).draw(g);
		}
	}

	@Override
	public void drawText(Graphics2D g)
	{
	}

	@Override
	public void drawAnimation(Graphics2D g)
	{
	}

	@Override
	public void doMouseEvents( MouseEvent e)
	{

		if (e.getButton() != MouseEvent.NOBUTTON)
		{
			for (int i = 0; i < 10; i++)
			{
				Particle p = new Particle(e.getX(), e.getY());
				// p.setVel(5, 0);
				p.setLife(40 + (int) (Math.random() * 60));
				p.setBrownian(true);
				p.setJerky(true);
				Color c = new Color(Color.HSBtoRGB((float) Math.random(), 1, 1));
				p.setColor(c);

				p.setAura(new Color(c.getRed(), c.getGreen(), c.getBlue(), 32));
				particles.add(p);
			}
		}

	}


	@Override
	public void getBuilding(int i)
	{
	}

	@Override
	public int getNumBuilding()
	{
		return 0;
	}

	@Override
	public String getBuildingData(int i)
	{
		return null;
	}

	@Override
	public void calculations()
	{
		if (Math.random()>.9)
		count ++;
		count %= 2;
		for (int i = 0; i < particles.size(); i++)
		{
			particles.get(i).calculate();
			if (particles.get(i).isDone() || !new Rectangle(83,130,430,430).contains(particles.get(i).getX(),particles.get(i).getY()))
			{
				particles.remove(particles.get(i));
			}
		}
	}

	public void unload()
	{
		selected = false;
		particles.clear();
	}

	@Override
	public void getUpgrades(int i)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean upgradeAvailable(int i)
	{
		return false;
	}


	@Override
	public int getNumUpgrade()
	{
		return 0;
	}

	@Override
	public String getUpgradeData(int i)
	{
		return null;
	}

	@Override
	public String getBuildingDescription(int num)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void drawExtras(Graphics2D g)
	{
		// TODO Auto-generated method stub
		
	}

}
