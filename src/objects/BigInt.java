package objects;

public class BigInt
{
	public int magnitude;
	public double value;
	
	public BigInt()
	{
		magnitude = 0;
		value = 0;
	}
	
	public BigInt(double v)
	{
		magnitude = getMagnitude(v);
		value = getValue(v);
	}
	
	public BigInt (double v, int mag)
	{
		magnitude = getMagnitude(v)+mag;
		value = getValue(v);
	}
	
	public void add(double num)
	{
		if (num == 0)
			return;
		add(new BigInt(num));
	}
	public void subtract(double num)
	{

		if (num == 0)
			return;
		add(new BigInt(-num));
	}
	public void multiply(double num)
	{
		if (num == 0)
		{
			value = 0;
			magnitude = 0;
			return;
		}
		multiply(new BigInt(num));
	}
	public void divide(double num)
	{
		if (num == 0)
		{
			System.err.println("Tried to Divide by 0");
			return;
		}
		divide(new BigInt(num));
	}
	public boolean greaterThan(double num)
	{
		return compareTo(new BigInt(num))>0;
	}
	public boolean lessThan(double num)
	{
		return compareTo(new BigInt(num))<0;
	}
	public boolean equalTo(double num)
	{
		return compareTo(new BigInt(num))==0;
	}
	
	
	public void add(BigInt num)
	{
		int ordDiff = num.magnitude-magnitude;
		if (num.value == 0)
			return;
		if (ordDiff>15)
		{
			value = num.value;
			magnitude = num.magnitude;
			return;
		}
		if (-ordDiff>15)
			return;
		double val = num.value*Math.pow(10, ordDiff);
		value += val;
		simplify();
	}
	public void subtract(BigInt num)
	{
		add(new BigInt(-num.value,num.magnitude));
	}
	public void multiply(BigInt num)
	{
		magnitude += num.magnitude;
		value *= num.value;
		simplify();
	}
	
	public void divide(BigInt num)
	{
		magnitude -= num.magnitude;
		value /= num.value;
		simplify();
	}
	public void squared()
	{
		magnitude *= 2;
		value *= value;
		simplify();
	}
	public String toString()
	{
		if (magnitude < 5)
			return "" + Math.round(value*Math.pow(10, magnitude));
		String s = "";
		s += String.format("%.5f", value) + 'e'+magnitude;
		
		return s;
	}
	
	public int compareTo(BigInt num)
	{
		if (value < 0 && num.value > 0)
			return -1;
		if (value > 0 && num.value < 0)
			return 1;
		
		int negate = 1;
		if (value < 0)
			negate = -1;
		
		if (magnitude > num.magnitude)
			return 1 * negate;
		if (magnitude == num.magnitude)
		{
			if (value > num.value)
				return 1 * negate;
			if (value == num.value)
				return 0;
			return -1 * negate;
		}
		return -1 * negate;
	}
	public boolean greaterThan(BigInt num)
	{
		return compareTo(num)>0;
	}
	public boolean lessThan(BigInt num)
	{
		return compareTo(num)<0;
	}
	public boolean equalTo(BigInt num)
	{
		return compareTo(num)==0;
	}
	
	private void simplify()
	{
		if (getMagnitude(value) != 0)
		{
			magnitude += getMagnitude(value);
			value = getValue(value);
		}
			
	}
	
	
	
	public static BigInt add(BigInt num1, BigInt num2)
	{
		BigInt ret = new BigInt(num1.value,num1.magnitude);
		ret.add(num2);
		return ret;
	}
	public static BigInt subtract(BigInt num1, BigInt num2)
	{
		BigInt ret = new BigInt(num1.value,num1.magnitude);
		ret.subtract(num2);
		return ret;
	}
	public static BigInt multiply(BigInt num1, BigInt num2)
	{
		BigInt ret = new BigInt(num1.value,num1.magnitude);
		ret.multiply(num2);
		return ret;
	}
	public static BigInt divide(BigInt num1, BigInt num2)
	{
		BigInt ret = new BigInt(num1.value,num1.magnitude);
		ret.divide(num2);
		return ret;
	}
	
	public static BigInt add(BigInt num1, double num2)
	{
		BigInt ret = new BigInt(num1.value,num1.magnitude);
		ret.add(num2);
		return ret;
	}
	public static BigInt subtract(BigInt num1, double num2)
	{
		BigInt ret = new BigInt(num1.value,num1.magnitude);
		ret.subtract(num2);
		return ret;
	}
	public static BigInt multiply(BigInt num1, double num2)
	{
		BigInt ret = new BigInt(num1.value,num1.magnitude);
		ret.multiply(num2);
		return ret;
	}
	public static BigInt divide(BigInt num1, double num2)
	{
		BigInt ret = new BigInt(num1.value,num1.magnitude);
		ret.divide(num2);
		return ret;
	}
	
	public static BigInt add(double num1, BigInt num2)
	{
		return add(num2,num1);
	}
	public static BigInt subtract(double num1, BigInt num2)
	{
		BigInt ret = new BigInt(getValue(num1),getMagnitude(num1));
		ret.subtract(num2);
		return ret;
	}
	public static BigInt multiply(double num1, BigInt num2)
	{
		return multiply(num2,num1);
	}
	public static BigInt divide(double num1, BigInt num2)
	{
		BigInt ret = new BigInt(getValue(num1),getMagnitude(num1));
		ret.divide(num2);
		return ret;
	}
	public static BigInt square(BigInt num)
	{
		BigInt ret = new BigInt(num.value * num.value, num.magnitude * 2);
		ret.simplify();
		return ret;
	}

	public static int getMagnitude(double num)
	{
		if (num == 0)
			return 0;
		return (int) Math.floor(Math.log10(Math.abs(num)));
	}
	public static double getValue(double d)
	{
		if (d == 0)
			return 0;
		if (Math.abs(d) >= 1)
			d/=Math.pow(10, (int)(Math.log10(Math.abs(d))));
		else
			d/=Math.pow(10, Math.floor((Math.log10(Math.abs(d)))));
		return d;
	}
	
	public static void main(String[] args)
	{
		BigInt a = new BigInt(10);
		a.value = .5;
		a.magnitude = 1;
		a.simplify();
		getValue(a.value);
		System.out.println(getMagnitude(a.value));
		System.out.println(Math.log10(Math.abs(.5)));
		System.out.println(.5/Math.pow(10, Math.floor((Math.log10(Math.abs(.5))))));
		
	}
}
