package cz.matyapav.todoapp.todo.util.enums;

import java.io.Serializable;

import cz.matyapav.todoapp.R;

/**
 * Enumeration for priorities
 */
public enum TodoPriority implements Serializable{

    LOW("Low", R.color.colorGreen),
    MEDIUM("Medium", R.color.colorOrange),
    HIGH("High", R.color.colorRed);

    private String name;
    private int colorId;

    private TodoPriority(String priorityName, int colorId) {
        this.name = priorityName;
        this.colorId = colorId;
    }

    public int getColorId() {
        return colorId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
