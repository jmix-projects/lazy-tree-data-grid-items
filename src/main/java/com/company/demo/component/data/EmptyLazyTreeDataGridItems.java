package com.company.demo.component.data;

import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.server.SerializablePredicate;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.ui.component.data.datagrid.EmptyDataGridItems;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class EmptyLazyTreeDataGridItems<E> extends EmptyDataGridItems<E> implements LazyTreeDataGridItems<E> {

    public EmptyLazyTreeDataGridItems(MetaClass metaClass) {
        super(metaClass);
    }

    @Override
    public int getChildCount(HierarchicalQuery<E, SerializablePredicate<E>> query) {
        return 0;
    }

    @Override
    public Stream<E> fetchChildren(HierarchicalQuery<E, SerializablePredicate<E>> query) {
        return null;
    }

    @Override
    public int getChildCount(E parent) {
        return 0;
    }

    @Override
    public Stream<E> getChildren(@Nullable E item) {
        return null;
    }

    @Override
    public boolean hasChildren(E item) {
        return false;
    }

    @Nullable
    @Override
    public E getParent(E item) {
        return null;
    }

    @Override
    public String getHierarchyPropertyName() {
        return null;
    }
}
