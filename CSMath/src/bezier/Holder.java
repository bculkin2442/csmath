package bezier;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Dummy class for holding a value, and being notified when it changes.
 * 
 * Essentially a pointer with change notifications.
 * 
 * @author bjculkin
 *
 * @param <E>
 *            The type of held value.
 */
public class Holder<E> {
	/*
	 * The held value.
	 */
	private E val;

	/*
	 * The listeners to notify.
	 */
	private final List<Consumer<E>> listeners;

	/**
	 * Create a new holder holding nothing.
	 */
	public Holder() {
		listeners = new LinkedList<>();
	}

	/**
	 * Create a new holder holding a specific value.
	 * 
	 * @param val
	 *            The value being held.
	 */
	public Holder(E val) {
		this();

		this.val = val;
	}

	/**
	 * Get the contained value.
	 * 
	 * @return The contained value.
	 */
	public E getVal() {
		return val;
	}

	/**
	 * Set the contained value, and notify listeners.
	 * 
	 * @param val
	 *            The new value.
	 */
	public void setVal(E val) {
		this.val = val;

		/*
		 * Notify listeners.
		 */
		for (Consumer<E> listen : listeners) {
			listen.accept(val);
		}
	}

	/**
	 * Add a listener.
	 * 
	 * @param listen
	 *            The listener to add.
	 */
	public void addHolderListener(Consumer<E> listen) {
		listeners.add(listen);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((val == null) ? 0 : val.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Holder<?> other = (Holder<?>) obj;
		if (val == null) {
			if (other.val != null)
				return false;
		} else if (!val.equals(other.val))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Holder [val=" + val + "]";
	}
}