package bisection;

/**
 * Represents a 'dual' number.
 *
 * Think imaginary numbers, where instead of i, we add a value d such that d^2 =
 * 0.
 */
public class Dual {
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