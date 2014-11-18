package objects.eras;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.UniverseCanvas;
import main.UniverseData;
import objects.BigInt;
import objects.Era;

public class Era0 extends Era
{
	ArrayList<Point2D> clicks = new ArrayList<Point2D>();
	int count = 0;
	double progress = 0;
	double progressBar = 0;   
	int boost = 0;
	
	public Era0(UniverseData d)
	{
		super(d);
	}

	@Override
	public void drawClickArea(Graphics2D g)
	{
		BufferedImage img = new BufferedImage(430,430, BufferedImage.TYPE_INT_RGB);
		ArrayList<Point2D> relevantClicks = new ArrayList<Point2D>();
		ArrayList<Integer> radius = new ArrayList<Integer>();
		for (int i = 1; i<clicks.size();i++)
		{
			if (clicks.get(i).getX()!=-100)
			{
				relevantClicks.add(clicks.get(i));
				radius.add(i);
			}
		}
		if (clicks.size() != 0)
			for (int i = 0; i < 430; i += 4)
				for (int j = 0; j < 430; j += 3)
				{
					double val = progress;
					for (int k = 0; k < relevantClicks.size(); k++)
							val += (radius.get(k) / (relevantClicks.get(k).distance(i + 83,
									j + 130)));
					val = 1 - .5 * val;
					val = Math.max(Math.min(1, val), 0);
					int c = Color.HSBtoRGB((float) (val / 1.2), (float) (val),
							(float) (1 - val));
					for (int ii = 0; ii < 4; ii++)
						for (int jj = 0; jj < 2; jj++)
							img.setRGB(Math.min(429, i + ii),
									Math.min(429, j + jj), c);
				}
		g.drawImage(img, 83, 130, null);
	}

	@Override
	public void drawText(Graphics2D g)
	{
		g.setColor(new Color(74,78,181));

		double round = (Math.sin(count/30.)+1); 
		double bar = Math.min(350, progressBar+round);
		g.fillRect(151, 66, (int) bar, 21);
  
		g.setColor(new Color(74,78,181,(int) (255*(bar%1))));
		g.drawLine((int)( 151+bar), 66,  (int) (151+bar), 86);
		
		g.drawImage(UniverseCanvas.getPicture("gui/StatusBar.png"), 25, 8, null);
		Font f= UniverseCanvas.getFont("modenine.ttf");
		

		g.setColor(new Color(74,78,181));
		g.setFont(f.deriveFont(18f));
		String display = "";
		display += data.energy;
		g.drawString("ENERGY: " + display, 40, 40);

	}

	@Override
	public void drawAnimation(Graphics2D g)
	{
	}

	@Override
	public void drawExtras(Graphics2D g)
	{
		if (data.era0upgrades[3])
		{
			g.setColor(Color.WHITE);
			int b = 0;
			if (!data.era0upgrades[5])
				b = (int)(boost/100.*20);
			else
				b = (int)(boost/200.*20);
			g.fillRect(765, 102-b, 10, b);

			g.setFont(UniverseCanvas.getFont("Goodwill.otf").deriveFont(12f));
			g.drawString("Boost", 755, 115);
			
//			g.setColor(new Color(41,48,161));
			g.drawRect(765, 82, 10, 20);
		}
		
	}


	@Override
	public void doMouseEvents( MouseEvent e)
	{

		if (e.getButton() != MouseEvent.NOBUTTON||(data.era0upgrades[1] && Math.random() > .9))
		{
				double val = -100;
				for (int k = 0; k < clicks.size(); k++)
					if (clicks.get(k).getX() != -100)
						val += Math.sqrt((clicks.get(k).distance(e.getPoint())
								* k * .1));
					else
						val += 10;
				if (data.era0upgrades[0])
					data.energy.add(BigInt.multiply(Math.max(8, val / 5),
							 data.era0buildings[0]));
				else
					data.energy.add(BigInt.multiply( 10 , data.era0buildings[0]));
				clicks.add(e.getPoint());
				while (clicks.size() > 20)
					clicks.remove(0);
		}
		
		if (e.getButton() != MouseEvent.NOBUTTON)
		{
			if (data.era0upgrades[3])
				boost+=20;
			if (data.era0upgrades[8])
			{
				data.era0buildings[0].add(data.era0buildings[2]);
				if (data.era0upgrades[10])
					data.era0buildings[1].add(data.era0buildings[2]);
				if (data.era0upgrades[11])
					data.era0buildings[2].add(data.era0buildings[2]);
			}
		}
		

	}

	@Override
	public void getBuilding(int num)
	{
		BigInt cost = new BigInt();
		switch (num)
		{
			case 0:
				cost.add(BigInt.multiply(100, BigInt.square(data.era0buildings[0])));
				break;
			case 1:
				cost.add(BigInt.multiply(200, BigInt.square(data.era0buildings[1])));
				break;
			case 2:
				cost.add(BigInt.multiply(1000, BigInt.square(data.era0buildings[2])));
				break;
		}
		if (data.era0upgrades[4])
			cost.divide(2);
		if (!data.energy.lessThan(cost))
		{
			data.energy.subtract(cost);
			data.era0buildings[num].add(1);;
		}
	}

	@Override
	public int getNumBuilding()
	{
		int num = 1;
		if (data.era0upgrades[2])
			num++;
		if (data.era0upgrades[8])
			num++;
		return num;
	}

	@Override
	public String getBuildingDescription(int num)
	{
		String s = "";
		BigInt cost = new BigInt();

		switch (num)
		{
			case 0:
				cost.add(BigInt.multiply(100, BigInt.square(data.era0buildings[0])));
				if (data.era0upgrades[4])
					cost.divide(2);
				s = "Energy Multiplier\nOwned: " + data.era0buildings[0] + "\n"
						+ "Cost: " + cost;
				break;
			case 1:
				cost.add(BigInt.multiply(200, BigInt.square(data.era0buildings[1])));
				if (data.era0upgrades[4])
					cost.divide(2);
				s = "Energy Generator Multiplier\nOwned: " + data.era0buildings[1] + "\n" 
						+ "Cost: " + cost;
				break;
			case 2:
				cost.add(BigInt.multiply(1000, BigInt.square(data.era0buildings[2])));
				if (data.era0upgrades[4])
					cost.divide(2);
				s = "Energy Multiplier Builder\nOwned: " + data.era0buildings[2] + "\n"
						+ "Cost: " + cost;
				break;
		}				
		return s;
	}

	
	@Override
	public String getBuildingData(int num)
	{
		String s = "";

		switch (num)
		{
			case 0:
				s = "Energy Multiplier\n"
						+ "Increases your click strength";
				break;
			case 1:
				s = "Energy Generator Multiplier\n"
						+ "Increases auto energy strength";
				break;
			case 2:
				s = "Energy Multiplier Builder\n"
						+ "Builds Energy Multipliers";
				break;
		}				
		return s;
	}

	@Override
	public void getUpgrades(int num)
	{
		BigInt cost = new BigInt();
		switch (num)
		{
			case 0:
				cost = new BigInt(2000);
				break;
			case 1:
				cost = new BigInt(6500);
				break;
			case 2:
				cost = new BigInt(20000);
				break;
			case 3:
				cost = new BigInt(50000);
				break;
			case 4:
				cost = new BigInt(100000);
				break;
			case 5:
				cost = new BigInt(150000);
				break;
			case 6:
				cost = new BigInt(199999);
				break;
			case 7:
				cost = new BigInt(345678);
				break;
			case 8:
				cost = new BigInt(1234567);
				break;
			case 9:
				cost = new BigInt(111111111);
				break;
			case 10:
				cost = new BigInt(1234512345);
				break;
			case 11:
				cost = new BigInt(2.5,9);
				break;
		}
		if (!data.energy.lessThan(cost))
		{
			data.era0upgrades[num] = true;
			data.energy.subtract(cost);
		}
	}

	
	@Override
	public String getUpgradeData(int num)
	{
		String s = "";

		switch(num)
		{

			case 0:
				s = "Energy from random clicks\nCost: 2000\n"
						+ "Clicks are stronger when further apart";
				break;
			case 1:
				s = "Energy trail\nCost: 6500\n"
						+ "Moving the mouse sometimes creates energy";
				break;
			case 2:
				s = "Automatic Energy\nCost: 20000\n"
						+ "Get Energy automatically over time";
				break;
			case 3:
				s = "Chain Reaction\nCost: 50000\n"
						+ "clicking increases the rate you obtain energy from energy generators for a short period of time";
				break;
			case 4:
				s = "Discount\nCost: 100000\n"
						+ "Buildings become cheaper";
				break;
			case 5:
				s = "Longer Chains\nCost: 150000\n"
						+ "Chain reactions lasts longer";
				break;
			case 6:
				s = "Stronger chains\nCost: 199999\n"
						+ "Chain reactions are stronger";
				break;
			case 7:
				s = "More auto energy\nCost: 345678\n"
						+ "Auto energy is faster";
				break;
			case 8:
				s = "Builder\nCost: 1234567\n"
						+ "Increase click multiplier with every click";
				break;
			case 9:
				s = "Auto builder\nCost: 111111111\n"
						+ "Builder runs automatically";
				break;
			case 10:
				s = "Build auto builder\nCost: 1234512345\n"
						+ "Clicks increase automatic builder rates";
				break;
			case 11:
				s = "Exponential building\nCost: 2500000000\n"
						+ "The Builder Builds more of itself";
				break;
		}
		return s;
	}

	@Override
	public int getNumUpgrade()
	{
		int num = 0;
		for (int i = 0; i < data.era0upgrades.length; i++)
		{
			if (upgradeAvailable(i))
				num++;
		}
		return num;
	}

	@Override
	public boolean upgradeAvailable(int i)
	{
		switch(i)
		{
			case 0:
			case 1:
			case 2:
			case 4:
				return !data.era0upgrades[i];
			case 3:
			case 7:
			case 8:
				return data.era0upgrades[2] && !data.era0upgrades[i];
			case 5:
			case 6:
				return data.era0upgrades[3] && !data.era0upgrades[i];
			case 9:
			case 10:
			case 11:
				return data.era0upgrades[8] && !data.era0upgrades[i];
		}
		return false;
	}
	
	@Override
	public void calculations()
	{
		count ++;
		progress = Math.log1p(data.energy.value) + data.energy.magnitude*Math.log(10);
		progress = Math.sqrt(progress);
		progress /= Math.sqrt(200);
//		data.energy +=1;
		if (progressBar < progress*350)
			progressBar+=Math.max(.01, ( progress*350- progressBar)/100);

		if (data.era0upgrades[2])
		{
			if (count % 4 == 0 || data.era0upgrades[7])
			{
				double multiplier = 1;
				if (data.era0upgrades[3]&&boost>0)
				{
					if (!data.era0upgrades[6])
						multiplier = 3;
					else
						multiplier = 10;

				}
				clicks.add(new Point((int) (Math.random() * 430 + 83),
						(int) (Math.random() * 430 + 130)));
				data.energy.add( BigInt.multiply(data.era0buildings[1],multiplier * 10));
			}
			else
				clicks.add(new Point(-100, -100));
		}
		else if (count % 2 == 0)
			clicks.add(new Point(-100, -100));
		
		while (clicks.size() > 20)
			clicks.remove(0);

		if (boost > 100 && !data.era0upgrades[5])
			boost = 100;
		else if (boost > 200)
			boost = 200;
		if (boost > 0)
			boost--;
		
		if (data.era0upgrades[9] && count % 10 == 0)
		{
			data.era0buildings[0].add(data.era0buildings[2]);
			if (data.era0upgrades[10])
				data.era0buildings[1].add(data.era0buildings[2]);
			if (data.era0upgrades[11])
				data.era0buildings[2].add(data.era0buildings[2]);
		}

		if (!data.eraAvailable[1] && progress >= 1)
			data.eraAvailable[1] = true;
	}

	public void load()
	{
		for (int i = 0; i < 20; i++)
			clicks.add(new Point(-100,-100));
		selected = true;
		
	}
	
	public void unload()
	{
		clicks.clear();
		selected = false;
	}


}
