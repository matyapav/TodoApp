package cz.matyapav.todoapp.todo.model;


import java.util.Date;

import cz.matyapav.todoapp.todo.enums.TodoPriority;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class Todo {

    private String title;
    private Date dateAndTimeStart;
    private Date dateAndTimeEnd;
    private Cathegory cathegory;
    private boolean notification;
    private String description;
    private boolean completed;
    private TodoPriority priority;

    public Todo() {
        //ma byt prazdny
    }

    public Todo(
            Cathegory cathegory,
            boolean completed,
            Date dateAndTimeEnd,
            Date dateAndTimeStart,
            String description,
            boolean notification,
            TodoPriority priority,
            String title
    ){
        this.cathegory = cathegory;
        this.completed = completed;
        this.dateAndTimeEnd = dateAndTimeEnd;
        this.dateAndTimeStart = dateAndTimeStart;
        this.description = description;
        this.notification = notification;
        this.priority = priority;
        this.title = title;
    }

    public Cathegory getCathegory() {
        return cathegory;
    }

    public void setCathegory(Cathegory cathegory) {
        this.cathegory = cathegory;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getDateAndTimeEnd() {
        return dateAndTimeEnd;
    }

    public void setDateAndTimeEnd(Date dateAndTimeEnd) {
        this.dateAndTimeEnd = dateAndTimeEnd;
    }

    public Date getDateAndTimeStart() {
        return dateAndTimeStart;
    }

    public void setDateAndTimeStart(Date dateAndTimeStart) {
        this.dateAndTimeStart = dateAndTimeStart;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public TodoPriority getPriority() {
        return priority;
    }

    public void setPriority(TodoPriority priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
