package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Particle
{

	Color color, aura;

	int life;

	double xLoc, yLoc;
	double xVel, yVel;
	double xAcc, yAcc;
	double xJerk, yJerk;

	int xDes, yDes;
	boolean brownian;
	boolean jerky;
	boolean hasTarget;
	boolean hasAura;

	public Particle(int x, int y)
	{
		xLoc = x;
		yLoc = y;
		color = Color.white;
		life = 100;
		brownian = true;
	}

	public Particle(int x, int y, int targetX, int targetY)
	{
		xLoc = x;
		yLoc = y;
		xDes = targetX;
		yDes = targetY;
		color = Color.white;
		life = 100;
		jerky = true;
	}

	public void setRandSpeed()
	{
		xVel = Math.random() * 10 - 5;
		yVel = Math.random() * 10 - 5;
	}

	public void setRandAcc()
	{
		xAcc = Math.random() * 4 - 2 - xVel / 4;
		yAcc = Math.random() * 4 - 2 - yVel / 4;
	}

	public void setRandJerk()
	{
		xJerk = (Math.random() - .5) / 400 * (xLoc - xDes);
		yJerk = (Math.random() - .5) / 400 * (yLoc - yDes);
	}

	public void draw(Graphics2D g)
	{
		
		if (hasAura)
		{
			g.setColor(aura);
			g.fillOval((int) xLoc - 2, (int) yLoc - 2, 5, 5);
			g.fillOval((int) xLoc - 1, (int) yLoc - 1, 3, 3);
		}
		g.setColor(color);
		g.drawRect((int) xLoc, (int) yLoc, 0, 0);

	}

	public void calculate()
	{
		life--;
		xLoc += xVel;
		yLoc += yVel;

		xVel += xAcc;
		yVel += yAcc;

		if (brownian)
		{
			setRandAcc();
		}
		else
		{
			if (jerky)
			{
				setRandJerk();
			}

			// xVel=Math.min(Math.max(xVel,-5), 5);
			// yVel=Math.min(Math.max(yVel,-5),5);
			if (hasTarget)
			{
				xAcc = (xDes - xLoc) / 1000 + xJerk;
				yAcc = (yDes - yLoc) / 1000 + yJerk;
				if (Math.sqrt((xLoc - xDes) * (xLoc - xDes) + (yLoc - yDes)
						* (yLoc - yDes)) < 100)
				{
					xVel /= 1.1;
					xAcc *= 150 / Math.abs(xLoc - xDes);
					yVel /= 1.1;
					yAcc *= 150 / Math.abs(yLoc - yDes);
				}
			}
			else
			{
				xAcc = (xVel) / 1000 + xJerk;
				yAcc = (yVel) / 1000 + yJerk;
			}
			// if (Math.abs(xVel)>6)
			// xAcc/=5;
			// if (Math.abs(yVel)>6)
			// yAcc/=5;
		}

	}

	public boolean isDone()
	{
		return life <= 0 || distance() < 3;
	}

	public double distance()
	{
		return Math.sqrt((xLoc - xDes) * (xLoc - xDes) + (yLoc - yDes)
				* (yLoc - yDes));
	}

	public int getX()
	{
		return (int) xLoc;
	}

	public int getY()
	{
		return (int) yLoc;
	}

	public void setLife(int l)
	{
		life = l;
	}
	
	public void setAcc(int ddx, int ddy)
	{
		xAcc = ddx;
		yAcc = ddy;
	}

	public void setVel(int dx, int dy)
	{
		xVel = dx;
		yAcc = dy;
	}

	public void setBrownian(boolean isBrownian)
	{
		brownian = isBrownian;
	}

	public void setJerky(boolean isJerky)
	{
		jerky = isJerky;
	}

	public void setColor(Color c)
	{
		color = c;
	}

	public void setAura(Color c)
	{
		hasAura = true;
		aura = c;
	}
}
