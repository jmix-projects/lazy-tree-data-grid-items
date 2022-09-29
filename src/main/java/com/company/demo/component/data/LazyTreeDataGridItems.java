package com.company.demo.component.data;

import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.server.SerializablePredicate;
import io.jmix.ui.component.data.BindingState;
import io.jmix.ui.component.data.TreeDataGridItems;
import io.jmix.ui.component.data.meta.EntityDataGridItems;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public interface LazyTreeDataGridItems<T> extends TreeDataGridItems<T>, EntityDataGridItems<T> {

    /**
     * Get the number of immediate child data items for the parent item returned
     * by a given query.
     *
     * @param query given query to request the count for
     * @return the count of child data items for the data item
     * {@link HierarchicalQuery#getParent()}
     */
    int getChildCount(HierarchicalQuery<T, SerializablePredicate<T>> query);

    /**
     * Fetches data from this HierarchicalDataProvider using given
     * {@code query}. Only the immediate children of
     * {@link HierarchicalQuery#getParent()} will be returned.
     *
     * @param query given query to request data with
     * @return a stream of data objects resulting from the query
     */
    Stream<T> fetchChildren(HierarchicalQuery<T, SerializablePredicate<T>> query);

    @Override
    default int getChildCount(T parent) {
        return getChildCount(new HierarchicalQuery<>(null, parent));
    }

    @Override
    default Stream<T> getChildren(@Nullable T item) {
        return fetchChildren(new HierarchicalQuery<>(null, item));
    }

    @Override
    default BindingState getState() {
        return BindingState.ACTIVE;
    }
}
