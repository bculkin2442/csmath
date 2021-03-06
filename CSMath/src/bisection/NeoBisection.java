package bisection;

import java.util.function.DoubleUnaryOperator;

import bjc.utils.math.Dual;
import bjc.utils.math.DualExpr;
import bjc.utils.math.DualExpr.ExprType;

/*
 * Benjamin Culkin
 * 1/16/2018
 * CS 479
 * Bisection
 * 
 * Use the bisection method to find bracketed roots of arbitrary real-valued functions.
 */

/**
 * Use the bisection method to find bracketed roots of arbitrary real-valued
 * functions.
 * 
 * @author student
 *
 */
public class NeoBisection {
	/**
	 * Maximum number of iterations to attempt.
	 */
	public static final int MAXITR = 500;

	/**
	 * Main method
	 * 
	 * @param args Unused CLI args
	 */
	public static void main(String[] args) {
		/* Bisection Method. */

		/* The functions we're approximating a root for. */
		DoubleUnaryOperator functionA = x -> Math.cos(x) - x;

		DoubleUnaryOperator functionB = x -> {
			double valA = Math.pow(x, 5);
			double valB = 3 * Math.pow(x, 4);
			double valC = 4 * Math.pow(x, 3);

			return valA - valB + valC - 1;
		};

		DoubleUnaryOperator functionC = x -> (x * x) - 3;

		/* The approximated roots. */
		double answerA = bisect(functionA, 0, 1, 0.0001);
		double answerB = bisect(functionB, 0, 1, 0.0001);
		double answerC = bisect(functionC, 1, 2, 0.0000001);

		/* Print out the answers + their function. */
		System.out.printf("Bisection Method:\n");
		System.out.printf("\tx   = cos(x)          => %.4f\n", answerA);
		System.out.printf("\tx^5 - 3x^4 + 4x^2 - 1 => %.4f\n", answerB);
		System.out.printf("\tx^2 - 3               => %.7f\n\n", answerC);

		/* Newton's / Secant Method. */
		/* The variable to manipulate in the expressions. */
		Dual varX = new Dual();
		DualExpr varXExpr = new DualExpr(varX);

		/* The functions to find the roots of. */
		DualExpr dualA = new DualExpr(ExprType.SUBTRACTION, new DualExpr(ExprType.COS, varXExpr), varXExpr);

		DualExpr dualB;

		{
			/* Construct the second function. */
			DualExpr tempA = new DualExpr(ExprType.POWER, varXExpr, new DualExpr(new Dual(5)));
			DualExpr tempB = new DualExpr(ExprType.MULTIPLICATION, new DualExpr(new Dual(3)),
					new DualExpr(ExprType.POWER, varXExpr, new DualExpr(new Dual(4))));
			DualExpr tempC = new DualExpr(ExprType.MULTIPLICATION, new DualExpr(new Dual(4)),
					new DualExpr(ExprType.POWER, varXExpr, new DualExpr(new Dual(3))));

			dualB = new DualExpr(ExprType.ADDITION, new DualExpr(ExprType.SUBTRACTION, tempA, tempB),
					new DualExpr(ExprType.SUBTRACTION, tempC, new DualExpr(new Dual(1))));
		}

		DualExpr dualC = new DualExpr(ExprType.SUBTRACTION,
				new DualExpr(ExprType.POWER, varXExpr, new DualExpr(new Dual(2))), new DualExpr(new Dual(3)));

		/* Print out dualized expressions. */
		System.out.printf("Expressions:\n");
		System.out.printf("\t%s\n", dualA);
		System.out.printf("\t%s\n", dualB);
		System.out.printf("\t%s\n\n", dualC);

		/* The approximated roots, using Newton's method. */
		double newtonA = newton(dualA, varX, 0, 1, 0.0001);
		double newtonB = newton(dualB, varX, 0, 1, 0.0001);
		double newtonC = newton(dualC, varX, 1, 2, 0.0000001);

		/* Print out the answers + their function. */
		System.out.printf("Newton's Method:\n");
		System.out.printf("\tx   = cos(x)          => %.4f\n", newtonA);
		System.out.printf("\tx^5 - 3x^4 + 4x^2 - 1 => %.4f\n", newtonB);
		System.out.printf("\tx^2 - 3               => %.7f\n\n", newtonC);

		/* The approximated roots, using the secant method. */
		double secantA = secant(dualA, varX, 0, 1, 0.0001);
		double secantB = secant(dualB, varX, 0, 1, 0.0001);
		double secantC = secant(dualC, varX, 1, 2, 0.0000001);

		/* Print out the answers + their function. */
		System.out.printf("Secant Method:\n");
		System.out.printf("\tx   = cos(x)          => %.4f\n", secantA);
		System.out.printf("\tx^5 - 3x^4 + 4x^2 - 1 => %.4f\n", secantB);
		System.out.printf("\tx^2 - 3               => %.7f\n", secantC);
	}

	/*
	 * Calculate the number of iterations to approximate to a specified tolerance.
	 */
	private static double iterCount(double a, double b, double tol) {
		double inside = Math.log((b - a) / tol);

		return Math.ceil(inside / Math.log(2));
	}

	/* Calculate a root for an equation by the bisection method. */
	private static double bisect(DoubleUnaryOperator func, double a, double b, double tol) {
		/* Calculate the number of iterations. */
		double N = iterCount(a, b, tol);

		double val = 0.0;

		/* Set our values. */
		double newa = a;
		double newb = b;
		double newc = (a + b) / 2;

		/*
		 * Continue onwards until we get the approximation to within the right
		 * tolerance.
		 */
		for (int i = 0; i < N; i++) {
			newc = (newa + newb) / 2;

			val = func.applyAsDouble(newc);

			/* Pick the right direction to bisect in. */
			if (func.applyAsDouble(newa) * val < 0) {
				newb = newc;
			} else {
				newa = newc;
			}
		}

		/* Return the right value. */
		return newc;
	}

	/**
	 * Use Newton's method to find the root of an equation.
	 *
	 * @param func The function to find a root for.
	 *
	 * @param var  The variable in the function.
	 *
	 * @param lo   The lower bound for the root
	 *
	 * @param hi   The higher bound for the root
	 *
	 * @param tol  The tolerance for the answer
	 *
	 * @return The estimated value for the equation.
	 */
	public static double newton(DualExpr func, Dual var, double lo, double hi, double tol) {
		/* Initial guess for root. */
		double newmid = (lo + hi) / 2;

		for (int i = 0; i < MAXITR; i++) {
			/* Previous root guess. */
			double prevmid = newmid;

			/* Set the variable properly. */
			var.real = newmid;
			var.dual = 1;

			/* Evaluate the function and its derivative. */
			Dual res = func.evaluate();

			/* Use Newton's method to refine our solution. */
			newmid = newmid - (res.real / res.dual);
			/* Hand back the solution if it's good enough. */
			if (Math.abs(newmid - prevmid) < tol) {
				return newmid;
			}
		}

		System.out.println("Newton's method iteration limit reached.");

		/* Give back the solution. */
		return newmid;
	}

	/**
	 * Calculate a root using the secant method.
	 * 
	 * @param func The function to calculate a root for.
	 * @param var  The variable in the function.
	 * @param lo   The lower bound of the root.
	 * @param hi   The upper bound of the root.
	 * @param tol  The tolerance to find the root to.
	 * @return The root, to within the desired tolerance.
	 */
	public static double secant(DualExpr func, Dual var, double lo, double hi, double tol) {
		/* Initial guesses for root. */
		double guess1 = (lo + hi) / 3; // 1/3 into the range; x-2
		double guess2 = ((lo + hi) / 3) * 2; // 2/3 into the range; x-1

		for (int i = 0; i < MAXITR; i++) {
			var.real = guess1;
			var.dual = 0;
			/* Evaluate the first guess. fx-2 */
			Dual res1 = func.evaluate();

			var.real = guess2;
			var.dual = 0;
			/* Evaluate the first guess. fx-1 */
			Dual res2 = func.evaluate();

			double oldGuess1 = guess1;
			double oldGuess2 = guess2;

			/* Use the secant method to refine our guesses. */
			guess1 = guess2; // new fx-1

			{
				guess2 = ((oldGuess1 * res2.real) - (oldGuess2 * res1.real)) / (res2.real - res1.real);
			}

			/* Hand back the solution if it's good enough. */
			if (Math.abs(guess1 - guess2) < tol) {
				return guess2;
			}
		}

		System.out.println("Secant method iteration limit reached.");

		/* Give back the solution. */
		return guess2;
	}
}
