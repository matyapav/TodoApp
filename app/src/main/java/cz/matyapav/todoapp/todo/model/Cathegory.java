package cz.matyapav.todoapp.todo.model;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class Cathegory {

    private String cathegoryName;
    private int imgResourceId;
    private String description;

    public Cathegory(){

    }

    public Cathegory(String cathegoryName, String description, int imageResourceId) {
        this.cathegoryName = cathegoryName;
        this.description = description;
        this.imgResourceId = imageResourceId;
    }

    public String getCathegoryName() {
        return cathegoryName;
    }

    public void setCathegoryName(String cathegoryName) {
        this.cathegoryName = cathegoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgResourceId() {
        return imgResourceId;
    }

    public void setImgResourceId(int imgResourceId) {
        this.imgResourceId = imgResourceId;
    }
}
