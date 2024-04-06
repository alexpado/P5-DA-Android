package com.cleanup.todoc.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Container, as the name suggest, encapsulates a single value of type {@link T}. This is to
 * avoid compiler error related to the modification of a field/variable outside the scope of a
 * lambda.
 * <p>
 * <b>WARNING:</b> Unlike the counterpart {@link AtomicInteger} and the likes, this is not
 * thread-safe.
 *
 * @param <T>
 *         The type of the field.
 */
public class Container<T> {

    private T v;

    /**
     * Define the value of this {@link Container} instance.
     *
     * @param v
     *         The new {@link Container} value.
     */
    public void set(T v) {

        this.v = v;
    }

    /**
     * Retrieve the value of this {@link Container} instance.
     *
     * @return The {@link Container} value.
     */
    public T get() {

        return v;
    }

}
