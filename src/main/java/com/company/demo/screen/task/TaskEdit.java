package com.company.demo.screen.task;

import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import com.company.demo.entity.Task;

@UiController("demo_Task.edit")
@UiDescriptor("task-edit.xml")
@EditedEntityContainer("taskDc")
@Route(value = "tasks/edit", parentPrefix = "tasks")
public class TaskEdit extends StandardEditor<Task> {
}