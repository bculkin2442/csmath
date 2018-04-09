package bisection;
/*
 * Benjamin Culkin
 * 1/16/2018
 * CS 479
 * Bisection
 * 
 * Use the bisection method to find bracketed roots of arbitrary real-valued functions.
 */
import java.util.function.DoubleUnaryOperator;

public class PreBisection {
	public static void main(String[] args) {
		// The functions we're approximating a root for
		DoubleUnaryOperator functionA = x -> Math.cos(x) - x;

		DoubleUnaryOperator functionB = x -> {
			double valA = Math.pow(x, 5);
			double valB = 3 * Math.pow(x, 4);
			double valC = 4 * Math.pow(x, 3);

			return valA - valB + valC - 1;
		};

		DoubleUnaryOperator functionC = x -> (x * x) - 3;

		// The approximated roots
		double answerA = bisect(functionA, 0, 1, 0.0001);
		double answerB = bisect(functionB, 0, 1, 0.0001);
		double answerC = bisect(functionC, 1, 2, 0.0000001);

		// Print out the answers + their function
		System.out.printf("x   = cos(x)          => %.4f\n", answerA);
		System.out.printf("x^5 - 3x^4 + 4x^2 - 1 => %.4f\n", answerB);
		System.out.printf("x^2 - 3               => %.7f\n", answerC);
	}

	// Calculate the number of iterations to approximate to a specified tolerance
	private static double iterCount(double a, double b, double tol) {
		double inside = Math.log((b - a) / tol);

		return Math.ceil(inside / Math.log(2));
	}

	// Calculate a root for an equation by the bisection method
	private static double bisect(DoubleUnaryOperator func, double a, double b, double tol) {
		// Calculate the number of iterations
		double N = iterCount(a, b, tol);

		double val = 0.0;

		// Set our values
		double newa = a;
		double newb = b;
		double newc = (a + b) / 2;

		// Continue onwards until we get the approximation to within the right tolerance
		for (int i = 0; i < N; i++) {
			newc = (newa + newb) / 2;

			val = func.applyAsDouble(newc);

			// Pick the right direction to bisect in
			if (func.applyAsDouble(newa) * val < 0) {
				newb = newc;
			} else {
				newa = newc;
			}
		}

		// Return the right value
		return newc;
	}
}
