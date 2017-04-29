package cz.matyapav.todoapp.todo.util.enums;

import cz.matyapav.todoapp.R;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public enum TodoPriority {

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
