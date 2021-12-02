package com.mazej.todo_list.database;

import com.mazej.todo_list.objects.Task;

import java.util.List;

public class PostTodoList {

    private String access_token;
    private String id;
    private String name;
    private List<Task> tasks;

    public PostTodoList(String access_token, String id, String name, List<Task> tasks) {
        this.access_token = access_token;
        this.id = id;
        this.name = name;
        this.tasks = tasks;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
