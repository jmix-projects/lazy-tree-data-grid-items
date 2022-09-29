package com.company.demo.component;

import com.company.demo.component.data.LazyHierarchicalDataGridDataProvider;
import com.company.demo.component.data.LazyTreeDataGridItems;
import io.jmix.ui.component.data.DataGridItems;
import io.jmix.ui.component.datagrid.DataGridDataProvider;
import io.jmix.ui.component.impl.TreeDataGridImpl;

public class LazyTreeDataGrid<E> extends TreeDataGridImpl<E> {

    @Override
    protected DataGridDataProvider<E> createDataGridDataProvider(DataGridItems<E> dataGridItems) {
        if (dataGridItems instanceof LazyTreeDataGridItems) {
            return new LazyHierarchicalDataGridDataProvider<>((LazyTreeDataGridItems<E>) dataGridItems, this);
        } else {
            return super.createDataGridDataProvider(dataGridItems);
        }
    }
}
