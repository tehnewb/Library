package container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Represents a type of holder for any {@code BasicItem} which allows for
 * modification of adding/removing of items and uses of a {@code Collection}.
 * 
 * @author Albert Beaupre
 *
 * @param <E>
 *            The type of {@code BasicItem} used within this collection
 */
@SuppressWarnings("unchecked")
public class Container<E extends BasicItem> implements Collection<E>,
		Iterable<E> {

	private final ContainerHandler<E> handler;
	private final short capacity; // Containers are not meant to have a large
									// capacity
	private BasicItem[] data; // The items within this container

	/**
	 * Constructs a new {@code Container} with the specified {@code capacity}
	 * and {@code handler}.
	 * 
	 * @param handler
	 *            the {@code ContainerHandler} to handle the container
	 * 
	 * @param capacity
	 *            the restricted amount of items to be within this
	 *            {@code Container}
	 */
	public Container(ContainerHandler<E> handler, int capacity) {
		this.data = new BasicItem[this.capacity = (short) capacity];
		this.handler = handler;
	}

	/**
	 * Used for the iteration of this {@code Container}.
	 * 
	 * @author Albert Beaupre
	 */
	private class ContainerIterator implements Iterator<E> {

		private int index;

		@Override
		public boolean hasNext() {
			return index < capacity;
		}

		public E next() {
			return (E) data[index++];
		}

	}

	/**
	 * Removes the specified {@code amount} from an item at the specified
	 * {@code index}, if possible, and returns true if the amount was removed.
	 * 
	 * @param index
	 *            the index of the item to remove the amount from
	 * @param amount
	 *            the amount to be removed from the item
	 * @return true if the amount was removed; return false otherwise
	 */
	public boolean remove(int index, int amount) {
		if (data[index] != null) {
			data[index].amount(data[index].getAmount() - amount);
			if (data[index].getAmount() < 1) // TODO < 1 should be < minStack
				data[index] = null;
			return true;
		}
		return false;
	}

	/**
	 * Removes the item at the specified {@code index} of this {@code Container}
	 * if possible, and returns the removed item.
	 * 
	 * @param index
	 *            the index of the item to remove
	 * @return the item removed; return null if non-existent
	 */
	public E remove(int index) {
		if (index == -1)
			throw new IllegalArgumentException("Value less than 0: " + index);
		E e = (E) data[index];
		if (e != null)
			data[index] = null;
		return e;
	}

	/**
	 * Returns the combined amounts of every item within this {@code Container}
	 * with the same id as the specified {@code itemId}.
	 * 
	 * @param itemId
	 *            the id of the items
	 * @return the amount of each item with the same id's combined
	 */
	public int amountOf(int itemId) {
		int amount = 0;
		for (int i = 0; i < capacity; i++)
			if (data[i] != null && data[i].getId() == itemId)
				amount += data[i].getAmount();
		return amount;
	}

	/**
	 * Returns the amount of indices within this {@code Container} that have not
	 * been occupied by an item.
	 * 
	 * @return the amount of free spaces within this {@code Container}
	 */
	public int getFreeSlots() {
		return capacity - size();
	}

	/**
	 * Returns true if this {@code Container} has no free slots open.
	 * 
	 * @return true if no slots open; return false otherwise
	 */
	public boolean isFull() {
		return getFreeSlots() == 0;
	}

	/**
	 * Sorts this {@code Container} to rearrange the items based on the
	 * specified {@code Comparator} argument.
	 * 
	 * @param comparator
	 *            the comparator to sort this {@code Container}.
	 */
	public void sort(Comparator<BasicItem> comparator) {
		this.shift();
		int size = this.size() - this.getFreeSlots();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (comparator.compare(this.get(i), this.get(j)) < 0) {
					BasicItem tmp = this.get(j);
					this.set(j, this.get(i));
					this.set(i, tmp);
				}
			}
		}
	}

	/**
	 * Shifts the elements within this {@code Container} to the left and leaves
	 * no empty spaces in between each item.
	 */
	public void shift() {
		ArrayList<BasicItem> shifted = new ArrayList<BasicItem>();
		Arrays.asList(data).stream().filter(n -> n != null)
				.forEach(n -> shifted.add(n));
		this.data = shifted.toArray(new BasicItem[capacity]);
	}

	/**
	 * Returns the number of maximum amount of items that is allowed within this
	 * {@code Container}.
	 * 
	 * @return the maximum amount of items allowed in this container
	 */
	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * Returns the amount of items within this {@code Container} not equal to
	 * null.
	 * 
	 * @return the amount of items
	 */
	public int size() {
		int size = 0;
		for (int i = 0; i < data.length; i++)
			if (data[i] != null)
				size++;
		return size;
	}

	/**
	 * Returns true if this {@code Container} has no items within it.
	 * 
	 * @return true if no items are within this {@code Container}; return false
	 *         otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns true if the specified {@code Object} is contained within this
	 * {@code Container} and is instance of {@code BasicItem}.
	 * 
	 * @param o
	 *            the object to check if container within this {@code Container}
	 * @return true if the object is within this {@code Container}; return false
	 *         otherwise
	 */
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	/**
	 * Returns true if the specified {@code objects} is contained inside of this
	 * {@code Container}.
	 * 
	 * @param objects
	 *            the objects to check
	 * @return true if all objects are contained; return false otherwise
	 */
	public boolean contains(Object... objects) {
		for (Object o : objects)
			if (!contains(o))
				return false;
		return true;
	}

	/**
	 * Swaps the item at each specified index with each other.
	 * 
	 * @param fromIndex
	 *            the index to swap from
	 * @param toIndex
	 *            the index to swap to
	 */
	public void swap(int fromIndex, int toIndex) {
		BasicItem old = get(toIndex);
		set(toIndex, get(fromIndex));
		set(fromIndex, old);
	}

	/**
	 * Returns the item element at the specified {@code index} of this
	 * {@code Container}.
	 * 
	 * @param index
	 *            the index to get the item at
	 * @return the item at the index; return null if non-existent
	 */
	public E get(int index) {
		return (E) data[index];
	}

	/**
	 * Returns the index of the specified {@code object}, which can be the id of
	 * an item or an instance of an item
	 * 
	 * @param object
	 *            the object to retrieve the index for
	 * @return the index retrieved; return -1 if not found
	 */
	public int indexOf(Object object) {
		if (object instanceof BasicItem) {
			BasicItem item = (BasicItem) object;
			for (int i = 0; i < capacity; i++)
				if (data[i] != null && data[i].getId() == item.getId()
						&& data[i].getAmount() >= item.getAmount())
					return i;
		} else if (object instanceof Integer) {
			for (int i = 0; i < capacity; i++)
				if (data[i] != null && data[i].getId() == (Integer) object)
					return i;
		}
		return -1;
	}

	/**
	 * Inserts the specified {@code element} at the {@code index} of this
	 * {@code Container}.
	 * 
	 * @param index
	 *            the index to insert the item at
	 * @param element
	 *            the item to be inserted
	 * @return true if the item was inserted; return false otherwise
	 */
	public boolean insert(int index, BasicItem element) {
		if (getFreeSlots() == 0)
			return false;
		BasicItem[] items = Arrays.copyOf(data, data.length);
		for (int i = index + 1; i < capacity; i++)
			set(i, items[i - 1]);
		data[index] = element;
		return true;
	}

	/**
	 * Sets the specified {@code element} at the {@code index} of this
	 * {@code Container} and returns the item which was previously there.
	 * 
	 * @param index
	 *            the index to set the item at
	 * @param element
	 *            the item to set at the index
	 * @return the previous item which was placed at the index; return null if
	 *         there wasn't one
	 */
	public E set(int index, BasicItem element) {
		return (E) (data[index] = element);
	}

	/**
	 * Returns the {@code Iterator} of this {@code Container}.
	 * 
	 * @return the iterator
	 */
	public ContainerIterator iterator() {
		return new ContainerIterator();
	}

	/**
	 * Converts the items within this {@code Container} to an array, which
	 * contains nulled objects if there is no item within that place in the
	 * array.
	 */
	public BasicItem[] toArray() {
		return Arrays.copyOf(data, data.length);
	}

	/*
	 * {@inheritDoc}
	 */
	public <T> T[] toArray(T[] a) {
		return (T[]) Arrays.copyOf(data, size(), a.getClass());
	}

	/*
	 * {@inheritDoc}
	 */
	public String toString() {
		return Arrays.toString(this.data);
	}

	/*
	 * {@inheritDoc}
	 */
	public boolean add(BasicItem e) {
		if (e == null)
			return false;
		return handler.add(this, e);
	}

	/*
	 * {@inheritDoc}
	 */
	public boolean remove(Object o) {
		if (o == null || !(o instanceof BasicItem))
			return false;
		return handler.remove(this, ((BasicItem) o));
	}

	/*
	 * {@inheritDoc}
	 */
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	/*
	 * {@inheritDoc}
	 */
	public boolean addAll(Collection<? extends E> c) {
		for (BasicItem item : c)
			if (item != null && !add(item))
				return false;
		return true;
	}

	/*
	 * {@inheritDoc}
	 */
	public boolean removeAll(Collection<?> c) {
		for (Object item : c)
			if (!remove(item))
				return false;
		return true;
	}

	/*
	 * {@inheritDoc}
	 */
	public boolean retainAll(Collection<?> c) {
		c.stream().filter(n -> !contains(n)).forEach(n -> remove(n));
		return this.containsAll(c);
	}

	/**
	 * Clears this {@code Container} of every item and replaces the value of the
	 * items with null.
	 */
	public void clear() {
		for (int i = 0; i < capacity; i++)
			data[i] = null;
	}

}
