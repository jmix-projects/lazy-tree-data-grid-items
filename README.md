# Lazy TreeDataGrid Items

The project contains an example of implementing lazy data loading for `TreeDataGrid`.

* `Task` - hierarchy entity
* `LazyTreeDataGrid` - replaces standard `TreeDataGrid` and creates specific vaadin `DataProvider` instance in case `LazyTreeDataGridItems` is passed. It registered in `LazyTreeComponentApplication` using `ComponentRegistration`.
* `LazyHierarchicalDataGridDataProvider` - vaadin `DataProvider` implementation that works with `LazyTreeDataGridItems`.
* `TaskBrowse.TaskLazyTreeDataGridItems` - sample implementation of `LazyTreeDataGridItems`. It uses `DataManager` to fetch data every time `TreeDataGrid` requested it. Query parameters are obtained from vaadin `HierarchicalQuery` object.