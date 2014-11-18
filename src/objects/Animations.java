package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animations {
	Image[] frames;
	
	int life;
	int lifeCounter;
	
	int xLoc, yLoc;
	
	public Animations(String fileLoc, int numFrames, int life, int x, int y)
	{
		frames = new Image[numFrames];
		for (int i = 0; i < numFrames; i++)
		{
			frames[i] = getPicture(fileLoc+i+".png");
		}
		this.life = life;
		xLoc = x;
		yLoc = y;
	}
	
	public void draw(Graphics2D g)
	{
		if (!isDone())
		{
			int frame = Math.max(0, ((int)(Math.ceil(((double)lifeCounter)/life*frames.length))-1));
			g.drawImage(frames[frame], xLoc, yLoc, null);
		}
		
	}
	
	public void calculate()
	{
		lifeCounter++;
	}
	
	public boolean isDone()
	{
		return life<lifeCounter;
	}
	
	private Image getPicture(String fileName) {
		Image img = null;
		try {
			img = ImageIO.read(getClass().getResource(fileName));
		} catch (Exception e) {
			System.err.println("Couldn't find \"" + fileName + "\"");
		}
		return img;
	}
}
