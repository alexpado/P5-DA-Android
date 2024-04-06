package com.cleanup.todoc.utils;

import static org.junit.Assert.assertArrayEquals;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.cleanup.todoc.model.TaskWithProject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Class combining all utility methods used for testing purpose.
 */
public final class TestUtils {

    /**
     * Private constructor to avoid any instantiation.
     */
    private TestUtils() {}

    /**
     * Utility method allowing to retrieve the provided {@link LiveData} content.
     *
     * @param liveData
     *         The {@link LiveData} from which the content will be retrieved
     * @param <T>
     *         Type of the {@link LiveData} content
     *
     * @return The {@link LiveData} content.
     *
     * @throws InterruptedException
     *         Threw if something happens while awaiting the {@link CountDownLatch}.
     * @throws IllegalStateException
     *         Threw if the {@link LiveData} did not received any content for 2 seconds.
     */
    public static <T> T liveData(final LiveData<T> liveData) throws InterruptedException {

        // Create a container that will accept our value
        Container<T>   container = new Container<>();
        CountDownLatch latch     = new CountDownLatch(1);

        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {

                container.set(o);
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw new IllegalStateException("LiveData value was never set.");
        }
        return container.get();
    }

    /**
     * Ensure that the provided {@link Comparator} sort in the expected order a {@link List} of
     * {@link I}.
     *
     * @param expected
     *         Array of value in the expected order.
     * @param accumulator
     *         Array which will accumulate sorted values.
     * @param items
     *         Array of items to sort
     * @param comparator
     *         {@link Comparator} to sort the provided {@link List}
     * @param transformer
     *         {@link Function} to transform the sorted items to their expected type.
     * @param <T>
     *         The type of items
     * @param <I>
     *         The type of the comparable type
     */
    public static <T, I> void assertComparator(I[] expected, I[] accumulator, List<T> items, Comparator<T> comparator, Function<T, I> transformer) {

        Collections.sort(items, comparator);

        // Convert data to raw form factor
        for (int i = 0; i < items.size(); i++) {
            accumulator[i] = transformer.apply(items.get(i));
        }

        assertArrayEquals(expected, accumulator);
    }

    /**
     * Ensure that the provided {@link Comparator} sort in the expected order a {@link List} of
     * {@link TaskWithProject}.
     *
     * @param items
     *         The {@link List} of {@link TaskWithProject} to sort
     * @param comparator
     *         The {@link Comparator} to use when sorting
     * @param expectedIdOrder
     *         An array of ids representing the expected order of items
     */
    public static void assertTaskComparator(List<TaskWithProject> items, Comparator<TaskWithProject> comparator, Long[] expectedIdOrder) {

        Long[] accumulator = new Long[expectedIdOrder.length];

        TestUtils.assertComparator(
                expectedIdOrder,
                accumulator,
                items,
                comparator,
                twp -> twp.getTask().getId()
        );
    }

}
