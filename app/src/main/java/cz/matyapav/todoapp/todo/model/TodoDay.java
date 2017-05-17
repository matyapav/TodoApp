package cz.matyapav.todoapp.todo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cz.matyapav.todoapp.todo.util.enums.TodoPriority;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class TodoDay {

    private Date date;
    private List<Todo> todos;

    public TodoDay() {
        this.todos = new ArrayList<>();
    }

    public TodoDay(Date date, List<Todo> todos) {
        this.date = date;
        this.todos = todos;
    }

    public Date getDate() {
        return date;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    public void addTodo(Todo todo){
        if(todos == null){
            todos = new ArrayList<>();
        }
        todos.add(todo);
    }

    public boolean removeTodo(Todo todo){
        if(todos == null){
            return false;
        }
        return todos.remove(todo);
    }

    public int getTodosCount(TodoPriority priority){
        int sum = 0;
        if(todos == null){
            return 0;
        }
        for (Todo todo : todos) {
            if(todo.getPriority().equals(priority)){
                sum++;
            }
        }
        return sum;
    }


    public int getNumberOfCompletedTodos(){
        int result = 0;
        if(todos == null){
            return 0;
        }
        for (Todo todo : todos) {
            if(todo.isCompleted()){
                result++;
            }
        }
        return result;
    }
}
