package bisection;
/*
 * Benjamin Culkin
 * 1/16/2018
 * CS 479
 * Bisection
 * 
 * Use the bisection method to find bracketed roots of arbitrary real-valued functions.
 */

import bjc.utils.math.Dual;
import bjc.utils.math.DualExpr;
import bjc.utils.math.DualExpr.ExprType;

/**
 * Bisect a curve to find bracketed roots of arbitrary real-valued functions.
 * @author bjculkin
 *
 */
public class Bisection {
	/**
	 * Maximum number of iterations to attempt.
	 */
	public static final int MAXITR = 500;

	/**
	 * Main method.
	 * @param args Unused CLI args.
	 */
	public static void main(String[] args) {
		/* The variable to manipulate in the expressions. */
		Dual varX = new Dual();
		DualExpr varXExpr = new DualExpr(varX);

		/* The functions to find the roots of. */
		DualExpr dualA = new DualExpr(ExprType.SUBTRACTION,
				new DualExpr(ExprType.COS, varXExpr), varXExpr);

		DualExpr dualB;

		{
			/* Construct the second function. */
			DualExpr tempA = new DualExpr(ExprType.POWER, varXExpr, new DualExpr(new Dual(5)));
			DualExpr tempB = new DualExpr(ExprType.MULTIPLICATION, new DualExpr(new Dual(3)),
					new DualExpr(ExprType.POWER, varXExpr, new DualExpr(new Dual(4))));
			DualExpr tempC = new DualExpr(ExprType.MULTIPLICATION, new DualExpr(new Dual(4)),
					new DualExpr(ExprType.POWER, varXExpr, new DualExpr(new Dual(3))));

			dualB = new DualExpr(ExprType.ADDITION,
					new DualExpr(ExprType.SUBTRACTION, tempA, tempB),
					new DualExpr(ExprType.SUBTRACTION, tempC, new DualExpr(new Dual(1))));
		}

		DualExpr dualC = new DualExpr(ExprType.SUBTRACTION,
				new DualExpr(ExprType.MULTIPLICATION, varXExpr, varXExpr),
				new DualExpr(new Dual(3)));

		/* The approximated roots, using Newton's method. */
		double newtonA = newton(dualA, varX, 0, 1, 0.0001);
		double newtonB = newton(dualB, varX, 0, 1, 0.0001);
		double newtonC = newton(dualC, varX, 1, 2, 0.0000001);

		/* Print out the answers + their function. */
		System.out.printf("Newton's Method:\n");
		System.out.printf("\tx   = cos(x)          => %.4f\n", newtonA);
		System.out.printf("\tx^5 - 3x^4 + 4x^2 - 1 => %.4f\n", newtonB);
		System.out.printf("\tx^2 - 3               => %.7f\n", newtonC);

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

	/**
	 * Use Newton's method to find the root of an equation.
	 *
	 * @param func
	 *                The function to find a root for.
	 *
	 * @param var
	 *                The variable in the function.
	 *
	 * @param lo
	 *                The lower bound for the root
	 *
	 * @param hi
	 *                The higher bound for the root
	 *
	 * @param tol
	 *                The tolerance for the answer
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
			newmid = prevmid - (res.real / res.dual);

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
	 * Bisect an arbitrary expression using the secant method.
	 * @param func The expression to bisect.
	 * @param var The variable to manipulate.
	 * @param lo The lower bounding value.
	 * @param hi The higher bounding value.
	 * @param tol The tolerance to find the answer to.
	 * @return The bisected root for the expression, within the specified tolerance.
	 */
	public static double secant(DualExpr func, Dual var, double lo, double hi, double tol) {
		/* Initial guesses for root. */
		double guess1 = (lo + hi) / 3; // 1/3 into the range
		double guess2 = ((lo + hi) / 3) * 2; // 2/3 into the range

		for (int i = 0; i < MAXITR; i++) {
			var.real = guess1;
			var.dual = 1;
			/* Evaluate the first guess. */
			Dual res1 = func.evaluate();

			var.real = guess2;
			var.dual = 1;
			/* Evaluate the first guess. */
			Dual res2 = func.evaluate();
			{
				double top1 = guess1 * res2.real;
				double top2 = guess2 * res1.real;

				double top = top1 - top2;

				double bot = res2.real - res1.real;

				/* Use the secant method to refine our guesses. */
				guess1 = guess2;
				guess2 = top / bot;
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
