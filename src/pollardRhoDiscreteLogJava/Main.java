package pollardRhoDiscreteLogJava;

import java.math.BigInteger;

public class Main {
	
	public static BigInteger calculateX(BigInteger aNum, BigInteger bNum, BigInteger sNum, BigInteger gNum, BigInteger pNum) {
		//BigInteger x = new BigInteger((sNum**aNum)*(gNum**bNum));
		BigInteger x1 = sNum.pow(aNum.abs().intValue());  // s ** a
		BigInteger x2 = gNum.pow(bNum.intValue()); // g ** b
		BigInteger x = x1.multiply(x2); // (s**a)*(g**b)
		x = x.mod(pNum); // mod p
		//System.out.println(x); // Debug
		return x;
	};
	
	public static BigInteger[] nextStep(BigInteger xNum, BigInteger aNum, BigInteger bNum, BigInteger pNum) {
		BigInteger modi = new BigInteger("3");
		BigInteger mod0 = new BigInteger("0");
		BigInteger mod1 = new BigInteger("1");
		BigInteger mod2 = new BigInteger("2");
		if(xNum.mod(modi).compareTo(mod0) == 0 ) {
			bNum = bNum.add(mod1);
			bNum = bNum.mod(pNum.subtract(mod1));
			return new BigInteger[] {aNum, bNum};
		}
		if(xNum.mod(modi).compareTo(mod1)== 0 ) {
			aNum = aNum.multiply(mod2);
			aNum = aNum.mod(pNum.subtract(mod1));
			bNum = bNum.multiply(mod2);
			bNum = bNum.mod(pNum.subtract(mod1));
			return new BigInteger[] {aNum, bNum};
		}
		if(xNum.mod(modi).compareTo(mod2) == 0 ) {
			aNum = aNum.add(mod1);
			aNum = aNum.mod(pNum.subtract(mod1));
			return new BigInteger[] {aNum, bNum};
		}
		else {
			System.out.println("Error in Function nextStep");
			return new BigInteger[] {aNum, bNum};
		}
	};
	
	
	
	public static void main(String[] args) {
		
		// Constant 1 to to make p-1
		BigInteger num1 = new BigInteger("1");
		
		// p = 23, g = 5, s = 7, Solution: 19
		// p = 10007, g = 5, s = 7 Solution: 1301
		// Change these Values for other variables
		BigInteger g = new BigInteger("5");
		BigInteger s = new BigInteger("7");
		BigInteger p = new BigInteger("10007");
		
		// a & b = Startvalues (can be whatever you want)
		BigInteger a = new BigInteger("34");
		a = a.mod(p);
		BigInteger b = new BigInteger("41");
		b = b.mod(p);
		
		// Counter
		int i = 0;
		
		// ----- Turtle -----
		BigInteger[] tNums = new BigInteger[3]; // tNums Array
		tNums[0] = a; // Startvalue A for Turtle
		tNums[1] = b; // Startvalue B for Turtle
		BigInteger tx = calculateX(a, b, s, g, p); // Calculate first X value of Turtle
		tNums = nextStep(tx, tNums[0], tNums[1], p); // Do first Turtle Step
		tx = calculateX(tNums[0], tNums[1], s, g, p); // Recalculate Turtle X
		/*
		System.out.println("------------*");
		System.out.println(tx); // Turtle X
		System.out.println(tNums[0]); // Turtle A
		System.out.println(tNums[1]); // Turtle B
		System.out.println("------------");
		*/
		// ----- Rabbit -----

		//System.out.println(rx);
		BigInteger[] rNums = new BigInteger[3]; // rNums Array
		rNums[0] = a; // Startvalue A for Rabbit
		rNums[1] = b; // Startvalue B for Rabbit
		BigInteger rx = calculateX(a, b, s, g, p); // Calculate first X value of Rabbit
		rNums = nextStep(rx, rNums[0], rNums[1], p); // Do first Rabbit Step ( 2x Turtle Step)
		rx = calculateX(rNums[0], rNums[1], s, g, p); // -------
		rNums = nextStep(rx, rNums[0], rNums[1], p); // -------
		rx = calculateX(rNums[0], rNums[1], s, g, p); // Recalculate Rabbit X
		/*
		System.out.println("------------*");
		System.out.println(rx); // Rabbit X
		System.out.println(rNums[0]); // Rabbit A
		System.out.println(rNums[1]); // Rabbit B 
		System.out.println("------------");
		*/
		// Start Loop
		while (rx.compareTo(tx) != 0) {
			tNums = nextStep(tx, tNums[0], tNums[1], p); // Turtle next Step
			tx = calculateX(tNums[0], tNums[1], s, g, p); // Turtle calculate new x
			rNums = nextStep(rx, rNums[0], rNums[1], p); // Rabbit 1. Step
			rx = calculateX(rNums[0], rNums[1], s, g, p); // Rabbit 1. new x
			rNums = nextStep(rx, rNums[0], rNums[1], p); // Rabbit 2. Step
			rx = calculateX(rNums[0], rNums[1], s, g, p); // Rabbit 2. new x
			i++; // increment counter
			//System.out.println(i); // Debug
		}
		
		
		BigInteger diff1 = rNums[0].subtract(tNums[0]);
		diff1 = diff1.gcd(p.subtract(num1));
		System.out.println(diff1);
		if(diff1.compareTo(num1)==0) {
			System.out.println("Simple Solution found: ");
			BigInteger outputSimple = tNums[1].subtract(rNums[1]);
			outputSimple = outputSimple.mod(p.subtract(num1));
			BigInteger outSim2 = rNums[0].subtract(tNums[0]);
			outSim2 = outSim2.mod(p.subtract(num1));
			outSim2 = outSim2.modInverse(p.subtract(num1));
			outputSimple = outputSimple.multiply(outSim2);
			System.out.println(outputSimple);
			System.out.print("Number of tries: ");
			System.out.println(i);
		}
		else {
			int number2 = 0; // new counter
			
			
			// calculate the Stuff like the inverse
			BigInteger run = diff1; // GCD value into run
			diff1 = tNums[1].subtract(rNums[1]);
			diff1 = diff1.mod(p.subtract(num1).divide(run));
			BigInteger diff2 = rNums[0].subtract(tNums[0]);
			diff2 = diff2.mod(p.subtract(num1).divide(run));
			diff2 = diff2.modInverse(p.subtract(num1).divide(run));
			BigInteger output = diff1.multiply(diff2);
			output = output.mod(p.subtract(num1).divide(run));
			
			while (number2 <= run.intValue()) {
				if (g.modPow(output, p).compareTo(s)==0){ // Check if you have the solution
					System.out.println("Solution Found");
					System.out.println(output);
					System.out.println("------------");
					System.out.print("Number of tries: ");
					System.out.println(i);
					break;
				}
				number2++; // increment 2nd counter
				output = output.add(p.subtract(num1).divide(run)); // Calculate new output
				output = output.mod(p.subtract(num1)); // 2nd step calculate new output
			}
			
		}
				
		
	};
	
};



