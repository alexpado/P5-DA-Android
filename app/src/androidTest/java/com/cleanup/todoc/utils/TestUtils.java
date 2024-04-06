package com.cleanup.todoc.utils;

import static org.junit.Assert.assertEquals;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewAssertion;

/**
 * Created by dannyroa on 5/9/15.
 */
public class TestUtils {

    public static ViewAssertion expectedItemCount(int expected) {

        return (view, noViewFoundException) -> {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView            recyclerView = (RecyclerView) view;
            RecyclerView.Adapter<?> adapter      = recyclerView.getAdapter();
            assert adapter != null;
            assertEquals(expected, adapter.getItemCount());
        };
    }


    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {

        return new RecyclerViewMatcher(recyclerViewId);
    }

}