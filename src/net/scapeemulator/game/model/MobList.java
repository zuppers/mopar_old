package net.scapeemulator.game.model;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.scapeemulator.game.model.mob.Mob;

/**
 * A {@link MobList} can store a certain amount of a certain {@link Mob} type.
 * It provides a way to retrieve, add and remove as well as an
 * {@link MobListIterator}.
 *
 * @author Graham?
 * @param <T> The type of {@link Mob} this {@link MobList} should store.
 */
public final class MobList<T extends Mob> implements Iterable<T> {

    private final Mob[] mobs;
    private int size = 0;

    /**
     * Create a {@link MobList} with a certain maximum capacity.
     * @param capacity The capacity of this {@link MobList}.
     */
    public MobList(int capacity) {
        mobs = new Mob[capacity];
    }

    /**
     * The size of the list.
     * @return The amount of {@link Mob}-s stored.
     */
    public int getSize() {
        return size;
    }

    /**
     * Add mob to this {@link MobList}. Seeks the first empty spot in the list
     * and fills it up. The Id of mob will be updated as well.
     * ({@link Mob#setId(int)})
     *
     * @param mob The element to add.
     * @return True when successfully added, false otherwise.
     */
    public boolean add(T mob) {
        for (int id = 0; id < mobs.length; id++) {
            if (mobs[id] == null) {
                mobs[id] = mob;
                size++;

                mob.setId(id + 1);
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the element on the index specified.
     *
     * @param index The index where to find retrieve the element from.
     * @return The element on that index. null is returned when index exceeds
     * the boundaries.
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index <= 0 || index >= mobs.length + 1) {
            return null;
        }

        return (T) mobs[index - 1];
    }

    /**
     * Remove the element from this {@link MobList}.
     * 
     * @param mob The element to remove.
     */
    public void remove(T mob) {
        int id = mob.getId();
        assert id != 0;

        id--;
        assert mobs[id] == mob;

        mobs[id] = null;
        size--;

        mob.resetId();
    }

    /**
     * Gets a new {@link MobListIterator} to iterate over this {@link MobList}'s
     * elements.
     * @return A new {@link MobListIterator}.
     */
    @Override
    public Iterator<T> iterator() {
        return new MobListIterator();
    }

    /**
     * An {@link Iterator} to iterate over elements the {@link MobList}
     * contains.
     */
    private class MobListIterator implements Iterator<T> {

        private int index = 0;

        /**
         * Gets whether there is another element left for {@link next()}.
         * @return Whether there exists a next non-null element.
         */
        @Override
        public boolean hasNext() {
            for (int i = index; i < mobs.length; i++) {
                if (mobs[i] != null) {
                    return true;
                }
            }

            return false;
        }

        /**
         * Gets the next element.
         *
         * @return The next element.
         * @throws NoSuchElementException When there is no next element.
         * @see hasNext()
         */
        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            for (; index < mobs.length; index++) {
                if (mobs[index] != null) {
                    return (T) mobs[index++];
                }
            }

            throw new NoSuchElementException();
        }

        /**
         * Remove the current element.
         * @throws IllegalStateException When there is no current element.
         */
        @SuppressWarnings("unchecked")
        @Override
        public void remove() {
            if (index == 0 || mobs[index - 1] == null) {
                throw new IllegalStateException();
            }

            MobList.this.remove((T) mobs[index - 1]);
        }

    }

}
