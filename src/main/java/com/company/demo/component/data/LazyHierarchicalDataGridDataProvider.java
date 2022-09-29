package com.company.demo.component.data;

import com.vaadin.data.provider.HierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.server.SerializablePredicate;
import io.jmix.ui.component.data.BindingState;
import io.jmix.ui.component.datagrid.DataGridItemsEventsDelegate;
import io.jmix.ui.component.datagrid.SortableDataGridDataProvider;
import io.jmix.ui.widget.data.EnhancedHierarchicalDataProvider;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class LazyHierarchicalDataGridDataProvider<T> extends SortableDataGridDataProvider<T>
        implements HierarchicalDataProvider<T, SerializablePredicate<T>>, EnhancedHierarchicalDataProvider<T> {

    public LazyHierarchicalDataGridDataProvider(LazyTreeDataGridItems<T> dataGridSource,
                                                DataGridItemsEventsDelegate<T> dataEventsDelegate) {
        super(dataGridSource, dataEventsDelegate);
    }

    public LazyTreeDataGridItems<T> getTreeDataGridSource() {
        return (LazyTreeDataGridItems<T>) dataGridItems;
    }

    @Override
    public int getChildCount(HierarchicalQuery<T, SerializablePredicate<T>> query) {
        if (getTreeDataGridSource().getState() == BindingState.INACTIVE) {
            return 0;
        }

        return getTreeDataGridSource().getChildCount(query);
    }

    @Override
    public Stream<T> fetchChildren(HierarchicalQuery<T, SerializablePredicate<T>> query) {
        return getTreeDataGridSource().fetchChildren(query);
    }

    @Override
    public boolean hasChildren(T item) {
        return getTreeDataGridSource().hasChildren(item);
    }

    @Override
    public int getLevel(T item) {
        LazyTreeDataGridItems<T> items = getTreeDataGridSource();
        if (!items.containsItem(item)) {
            throw new IllegalArgumentException("Data provider doesn't contain the item passed to the method");
        }

        int level = 0;
        T currentItem = item;
        while ((currentItem = items.getParent(currentItem)) != null) {
            ++level;
        }

        return level;
    }

    @Nullable
    @Override
    public T getParent(T item) {
        return getTreeDataGridSource().getParent(item);
    }
}
