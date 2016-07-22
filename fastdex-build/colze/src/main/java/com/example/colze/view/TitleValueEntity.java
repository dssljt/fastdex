package com.example.colze.view;
 
public class TitleValueEntity {
    private String title;
    private float value;
     
    public TitleValueEntity(String title, float value) {
        super();
        this.title = title;
        this.value = value;
    }
 
    public TitleValueEntity() {
        super();
    }
 
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
 
    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
 
    /**
     * @return the value
     */
    public float getValue() {
        return value;
    }
 
    /**
     * @param value
     *            the value to set
     */
    public void setValue(float value) {
        this.value = value;
    }
}