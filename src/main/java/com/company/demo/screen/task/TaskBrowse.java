package com.company.demo.screen.task;

import com.company.demo.component.data.LazyTreeDataGridItems;
import com.company.demo.entity.Task;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.server.SerializablePredicate;
import io.jmix.core.*;
import io.jmix.core.LoadContext.Query;
import io.jmix.core.common.event.EventHub;
import io.jmix.core.common.event.Subscription;
import io.jmix.core.entity.EntityValues;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.metamodel.model.MetaPropertyPath;
import io.jmix.ui.component.TreeDataGrid;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@UiController("demo_Task.browse")
@UiDescriptor("task-browse.xml")
@LookupComponent("tasksTable")
@Route("tasks")
public class TaskBrowse extends StandardLookup<Task> {

    private static final Logger log = LoggerFactory.getLogger(TaskBrowse.class);

    @Autowired
    private TreeDataGrid<Task> tasksTable;

    @Autowired
    private DataManager dataManager;
    @Autowired
    private Metadata metadata;

    @Subscribe
    public void onInit(InitEvent event) {
        tasksTable.setItems(new TaskLazyTreeDataGridItems("parent"));
    }

    public class TaskLazyTreeDataGridItems implements LazyTreeDataGridItems<Task> {

        private final EventHub events = new EventHub();
        private final String hierarchyProperty;

        private Task selectedItem;
        private Sort sort = Sort.UNSORTED;

        public TaskLazyTreeDataGridItems(String hierarchyProperty) {
            this.hierarchyProperty = hierarchyProperty;
        }

        @Nullable
        @Override
        public MetaClass getEntityMetaClass() {
            return metadata.getClass(Task.class);
        }

        @Override
        public int getChildCount(HierarchicalQuery<Task, SerializablePredicate<Task>> query) {
            LoadContext<Object> loadContext = new LoadContext<>(getEntityMetaClass())
                    .setQuery(new Query("select e from demo_Task e where e.parent = :parent")
                            .setParameter("parent", query.getParent())
                            .setSort(sort)
                            .setFirstResult(query.getOffset())
                            .setMaxResults(query.getLimit())
                    );

            return ((int) dataManager.getCount(loadContext));
        }

        @Override
        public Stream<Task> fetchChildren(HierarchicalQuery<Task, SerializablePredicate<Task>> query) {
            List<Task> list = dataManager.load(Task.class)
                    .query("select e from demo_Task e where e.parent = :parent")
                    .parameter("parent", query.getParent())
                    .sort(sort)
                    .firstResult(query.getOffset())
                    .maxResults(query.getLimit())
                    .list();

            log.info("Loaded: {} items", list.size());

            return list.stream();
        }

        @Override
        public void sort(Object[] propertyId, boolean[] ascending) {
            sort = createSort(propertyId, ascending);
        }

        @Override
        public void resetSortOrder() {
            sort = Sort.UNSORTED;
        }

        private Sort createSort(Object[] propertyId, boolean[] ascending) {
            List<Sort.Order> orders = new ArrayList<>();
            for (int i = 0; i < propertyId.length; i++) {
                String property;
                if (propertyId[i] instanceof MetaPropertyPath) {
                    property = ((MetaPropertyPath) propertyId[i]).toPathString();
                } else {
                    property = (String) propertyId[i];
                }
                Sort.Order order = ascending[i] ? Sort.Order.asc(property) : Sort.Order.desc(property);
                orders.add(order);
            }
            return Sort.by(orders);
        }

        @Override
        public boolean hasChildren(Task item) {
            LoadContext<Object> loadContext = new LoadContext<>(getEntityMetaClass())
                    .setQuery(new Query("select e from demo_Task e where e.parent = :parent")
                            .setParameter("parent", item));

            return dataManager.getCount(loadContext) > 0;
        }

        @Nullable
        @Override
        public Task getParent(Task item) {
            return EntityValues.getValue(item, getHierarchyPropertyName());
        }

        @Override
        public String getHierarchyPropertyName() {
            return hierarchyProperty;
        }

        @Nullable
        @Override
        public Object getItemId(Task item) {
            return EntityValues.getId(item);
        }

        @Nullable
        @Override
        public Task getItem(@Nullable Object itemId) {
            return itemId == null
                    ? null
                    : dataManager.load(Id.of(itemId, Task.class)).one();
        }

        @Nullable
        @Override
        public Object getItemValue(Object itemId, MetaPropertyPath propertyId) {
            return EntityValues.getValueEx(getItem(itemId), propertyId);
        }

        @Override
        public int indexOfItem(Task item) {
            // TODO: implement
            return 0;
        }

        @Nullable
        @Override
        public Task getItemByIndex(int index) {
            // TODO: implement
            return null;
        }

        @Override
        public Stream<Task> getItems() {
            return dataManager.load(Task.class)
                    .all()
                    .sort(sort)
                    .list()
                    .stream();
        }

        @Override
        public List<Task> getItems(int startIndex, int numberOfItems) {
            return dataManager.load(Task.class)
                    .query("select e from demo_Task e")
                    .sort(sort)
                    .firstResult(startIndex)
                    .maxResults(numberOfItems)
                    .list();
        }

        @Override
        public boolean containsItem(Task item) {
            // TODO: implement
            return true;
        }

        @Override
        public int size() {
            LoadContext<Task> loadContext = new LoadContext<>(getEntityMetaClass());
            return (int) dataManager.getCount(loadContext);
        }

        @Nullable
        @Override
        public Task getSelectedItem() {
            return selectedItem;
        }

        @Override
        public void setSelectedItem(@Nullable Task item) {
            this.selectedItem = item;

            fireSelectedITemChanged(item);
        }

        private void fireSelectedITemChanged(Task item) {
            events.publish(SelectedItemChangeEvent.class, new SelectedItemChangeEvent<>(this, item));
        }

        @Override
        public Subscription addValueChangeListener(Consumer<ValueChangeEvent<Task>> listener) {
            return events.subscribe(ValueChangeEvent.class, (Consumer) listener);
        }

        @Override
        public Subscription addItemSetChangeListener(Consumer<ItemSetChangeEvent<Task>> listener) {
            return events.subscribe(ItemSetChangeEvent.class, (Consumer) listener);
        }

        @Override
        public Subscription addSelectedItemChangeListener(Consumer<SelectedItemChangeEvent<Task>> listener) {
            return events.subscribe(SelectedItemChangeEvent.class, (Consumer) listener);
        }

        @Override
        public Subscription addStateChangeListener(Consumer<StateChangeEvent> listener) {
            return events.subscribe(StateChangeEvent.class, listener);
        }
    }
}