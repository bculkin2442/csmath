package bisection;
import java.util.function.DoubleUnaryOperator;

/*
 * Benjamin Culkin
 * 1/16/2018
 * CS 479
 * Bisection
 * 
 * Use the bisection method to find bracketed roots of arbitrary real-valued functions.
 */

public class NeoBisection {
	/**
	 * Maximum number of iterations to attempt.
	 */
	public static final int MAXITR = 500;

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
		DualExpr dualA = new DualExpr(DualExpr.ExprType.SUBTRACTION,
				new DualExpr(DualExpr.ExprType.COS, varXExpr), varXExpr);

		DualExpr dualB;

		{
			/* Construct the second function. */
			DualExpr tempA = new DualExpr(varXExpr, 5);
			DualExpr tempB = new DualExpr(DualExpr.ExprType.MULTIPLICATION, new DualExpr(new Dual(3)),
					new DualExpr(varXExpr, 4));
			DualExpr tempC = new DualExpr(DualExpr.ExprType.MULTIPLICATION, new DualExpr(new Dual(4)),
					new DualExpr(varXExpr, 3));

			dualB = new DualExpr(DualExpr.ExprType.ADDITION,
					new DualExpr(DualExpr.ExprType.SUBTRACTION, tempA, tempB),
					new DualExpr(DualExpr.ExprType.SUBTRACTION, tempC, new DualExpr(new Dual(1))));
		}

		DualExpr dualC = new DualExpr(DualExpr.ExprType.SUBTRACTION, new DualExpr(varXExpr, 2),
				new DualExpr(new Dual(3)));

		/* Print out dualized expressions. */
		System.out.printf("Expressions:\n");
		System.out.printf("\t%s\n", dualA);
		System.out.printf("\t%s\n", dualB);
		System.out.printf("\t%s\n", dualC);

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
			newmid = newmid - (res.real / res.dual);
			System.out.printf("\tTRACE: prevmid: %f\t\tnewmid: %f\n", prevmid, newmid);
			/* Hand back the solution if it's good enough. */
			if (Math.abs(newmid - prevmid) < tol) {
				return newmid;
			}
		}

		System.out.println("Newton's method iteration limit reached.");

		/* Give back the solution. */
		return newmid;
	}

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

			double oldGuess1 = guess1;

			/* Use the secant method to refine our guesses. */
			guess1 = guess2;
			guess2 = ((oldGuess1 * res2.real) - (guess1 * res1.real)) / (res1.real - res2.real);

			System.out.printf("\tTRACE: guess1: %f\t\tguess2: %f\n", guess1, guess2);
			/* Hand back the solution if it's good enough. */
			if (Math.abs(guess1 - guess2) < tol) {
				return guess2;
			}
		}

		System.out.println("Secant method iteration limit reached.");

		/* Give back the solution. */
		return guess2;
	}

	/**
	 * Represents a 'dual' number.
	 *
	 * Think imaginary numbers, where instead of i, we add a value d such that d^2 =
	 * 0.
	 */
	public static class Dual {
		/**
		 * The real part of the dual number.
		 */
		public double real;
		/**
		 * The dual part of the dual number.
		 */
		public double dual;

		/**
		 * Create a new dual with both parts zero.
		 */
		public Dual() {
			real = 0;
			dual = 0;
		}

		/**
		 * Create a new dual number with a zero dual part.
		 *
		 * @param real
		 *                The real part of the number.
		 */
		public Dual(double real) {
			this.real = real;
			this.dual = 0;
		}

		/**
		 * Create a new dual number with a specified dual part.
		 *
		 * @param real
		 *                The real part of the number.
		 * @param dual
		 *                The dual part of the number.
		 */
		public Dual(double real, double dual) {
			this.real = real;
			this.dual = dual;
		}

		@Override
		public String toString() {
			return String.format("<%f, %f>", real, dual);
		}
	}

	/**
	 * Represents an expression using dual numbers.
	 *
	 * Useful for automatically differentiating expressions.
	 */
	public static class DualExpr {
		/**
		 * Represents the various types of dual expressions.
		 */
		public static enum ExprType {
			/**
			 * A fixed number.
			 */
			CONSTANT,
			/**
			 * An addition operation.
			 */
			ADDITION,
			/**
			 * A subtraction operation.
			 */
			SUBTRACTION,
			/**
			 * A multiplication operation.
			 */
			MULTIPLICATION,
			/**
			 * A division operation.
			 */
			DIVISION,
			/**
			 * A sine operation.
			 */
			SIN,
			/**
			 * A cosine operation.
			 */
			COS,
			/**
			 * An exponential function.
			 */
			EXPONENTIAL,
			/**
			 * A logarithm function.
			 */
			LOGARITHM,
			/**
			 * A power operation.
			 */
			POWER,
			/**
			 * An absolute value.
			 */
			ABSOLUTE
		}

		/**
		 * The type of the expression.
		 */
		public final ExprType type;

		/**
		 * The dual number value, for constants.
		 */
		public Dual number;

		/**
		 * The left (or first) part of the expression.
		 */
		public DualExpr left;
		/**
		 * The right (or second) part of the expression.
		 */
		public DualExpr right;

		/**
		 * The power to use, for power operations.
		 */
		public int power;

		/**
		 * Create a new constant dual number.
		 *
		 * @param num
		 *                The value of the dual number.
		 */
		public DualExpr(Dual num) {
			this.type = ExprType.CONSTANT;

			number = num;
		}

		/**
		 * Create a new unary dual number.
		 *
		 * @param type
		 *                The type of operation to perform.
		 * @param val
		 *                The parameter to the value.
		 */
		public DualExpr(ExprType type, DualExpr val) {
			this.type = type;

			left = val;
		}

		/**
		 * Create a new binary dual number.
		 *
		 * @param type
		 *                The type of operation to perform.
		 * @param val
		 *                The parameter to the value.
		 */
		public DualExpr(ExprType type, DualExpr left, DualExpr right) {
			this.type = type;

			this.left = left;
			this.right = right;
		}

		/**
		 * Create a new power expression.
		 *
		 * @param left
		 *                The expression to raise.
		 * @param power
		 *                The power to raise it by.
		 */
		public DualExpr(DualExpr left, int power) {
			this.type = ExprType.POWER;

			this.left = left;
			this.power = power;
		}

		/**
		 * Evaluate an expression to a number.
		 *
		 * Uses the rules provided in
		 * https://en.wikipedia.org/wiki/Automatic_differentiation
		 *
		 * @return The evaluated expression.
		 */
		public Dual evaluate() {
			/* The evaluated dual numbers. */
			Dual lval, rval;

			/* Perform the right operation for each type. */
			switch (type) {
			case CONSTANT:
				return number;
			case ADDITION:
				lval = left.evaluate();
				rval = right.evaluate();

				return new Dual(lval.real + rval.real, lval.dual + rval.dual);
			case SUBTRACTION:
				lval = left.evaluate();
				rval = right.evaluate();

				return new Dual(lval.real - rval.real, lval.real - rval.real);
			case MULTIPLICATION:
				lval = left.evaluate();
				rval = right.evaluate();

			{
				double lft = lval.dual * rval.real;
				double rght = lval.real * rval.dual;

				return new Dual(lval.real * rval.real, lft + rght);
			}
			case DIVISION:
				lval = left.evaluate();
				rval = right.evaluate();

			{
				if (rval.real == 0) {
					throw new IllegalArgumentException("ERROR: Attempted to divide by zero.");
				}

				double lft = lval.dual * rval.real;
				double rght = lval.real * rval.dual;

				double val = (lft - rght) / (rval.real * rval.real);

				return new Dual(lval.real / rval.real, val);
			}
			case SIN:
				lval = left.evaluate();

				return new Dual(Math.sin(lval.real), lval.dual * Math.cos(lval.real));
			case COS:
				lval = left.evaluate();

				return new Dual(Math.cos(lval.real), -lval.dual * Math.sin(lval.real));
			case EXPONENTIAL:
				lval = left.evaluate();

			{
				double val = Math.exp(lval.real);

				return new Dual(val, lval.dual * val);
			}
			case LOGARITHM:
				lval = left.evaluate();

				if (lval.real <= 0) {
					throw new IllegalArgumentException(
							"ERROR: Attempted to take non-positive log.");
				}

				return new Dual(Math.log(lval.real), lval.dual / lval.real);
			case POWER:
				lval = left.evaluate();

				if (lval.real == 0) {
					throw new IllegalArgumentException("ERROR: Raising zero to a power.");
				}

			{
				double rl = Math.pow(lval.real, power);

				double lft = Math.pow(lval.real, power - 1);

				return new Dual(rl, power * lft * lval.dual);
			}
			case ABSOLUTE:
				lval = left.evaluate();

				return new Dual(Math.abs(lval.real), lval.dual * Math.signum(lval.real));
			default:
				String msg = "ERROR: Unknown expression type %s";

				throw new IllegalArgumentException(String.format(msg, type));
			}
		}

		@Override
		public String toString() {
			switch (type) {
			case ABSOLUTE:
				return String.format("abs(%s)", left.toString());
			case ADDITION:
				return String.format("(%s + %s)", left.toString(), right.toString());
			case CONSTANT:
				return String.format("%s", number.toString());
			case COS:
				return String.format("cos(%s)", left.toString());
			case DIVISION:
				return String.format("(%s / %s)", left.toString(), right.toString());
			case EXPONENTIAL:
				return String.format("exp(%s)", left.toString());
			case LOGARITHM:
				return String.format("log(%s)", left.toString());
			case MULTIPLICATION:
				return String.format("(%s * %s)", left.toString(), right.toString());
			case POWER:
				return String.format("(%s ^ %d)", left.toString(), power);
			case SIN:
				return String.format("sin(%s)", left.toString());
			case SUBTRACTION:
				return String.format("(%s - %s)", left.toString(), right.toString());
			default:
				return String.format("UNKNOWN_EXPR");
			}
		}
	}
}
