package testing;

import objects.BigInt;

import org.junit.*;

import static org.junit.Assert.*;


public class BigIntTest {
	
    @Test
    public void testConstructor() {

    	BigInt a = new BigInt();
    	assert(0==a.value);
    	assert(0==a.magnitude);

    	a = new BigInt(0);
    	assert(0==a.value);
    	assert(0==a.magnitude);

    	a = new BigInt(1);
    	assert(1==a.value);
    	assert(0==a.magnitude);
    	
    	a = new BigInt(10);
    	assert(1==a.value);
    	assert(1==a.magnitude);

    	a = new BigInt(-10);
    	assert(-1==a.value);
    	assert(1==a.magnitude);

    	a = new BigInt(-0.1);
    	assert(-1==a.value);
    	assert(-1==a.magnitude);

    	a = new BigInt(0.1);
    	assert(1==a.value);
    	assert(-1==a.magnitude);

    	a = new BigInt(5,100);
    	assert(5==a.value);
    	assert(100==a.magnitude);

    	a = new BigInt(50,100);
    	assert(5==a.value);
    	assert(101==a.magnitude);

    	a = new BigInt(.5,100);
    	assert(5==a.value);
    	assert(99==a.magnitude);

    	a = new BigInt(-50,100);
    	assert(-5==a.value);
    	assert(101==a.magnitude);

    	a = new BigInt(-.5,100);
    	assert(-5==a.value);
    	assert(99==a.magnitude);
    }
	
    @Test
    public void testSubtraction() {

    	BigInt a = new BigInt(10);
    	BigInt b = new BigInt(5);
    	a.subtract(b);
    	assert(5==a.value);
    	assert(0==a.magnitude);

    	a = new BigInt(100);
    	b = new BigInt(5);
    	a.subtract(b);
    	System.out.println(a.value);
    	assert(9.5==a.value);
    	assert(1==a.magnitude);

    	a = new BigInt(100,100);
    	b = new BigInt(5);
    	a.subtract(b);
    	assert(1==a.value);
    	assert(102==a.magnitude);

    	a = new BigInt(100,100);
    	b = new BigInt(5);
    	a.subtract(b);
    	assert(100==a.value);
    	assert(100==a.magnitude);
    }
    

    @Test
    public void testAddition() {

    	BigInt a = new BigInt(5);
    	BigInt b = new BigInt(10);
    	a.add(b);
    	assert(1.5==a.value);
    	assert(1==a.magnitude);
    	

    	a = new BigInt(500);
    	b = new BigInt(100000000);
    	a.add(b);
    	assert(1.000005==a.value);
    	assert(8==a.magnitude);

    	a = new BigInt(-500);
    	b = new BigInt(100000000);
    	a.add(b);
    	assert(9.99995==a.value);
    	assert(7==a.magnitude);

    	a = new BigInt(500);
    	b = new BigInt(-100000000);
    	a.add(b);
    	assert(-9.99995==a.value);
    	assert(7==a.magnitude);
    }
}