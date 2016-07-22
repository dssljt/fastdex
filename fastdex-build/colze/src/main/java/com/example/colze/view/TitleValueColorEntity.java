package com.example.colze.view;
 
public class TitleValueColorEntity extends TitleValueEntity {
 
    private int color;
 
    public TitleValueColorEntity(String title, float value, int color) {
        super(title, value);
        this.color = color;
    }
 
    public TitleValueColorEntity() {
        super();
    }
 
    /**
     * @return the color
     */
    public int getColor() {
        return color;
    }
 
    /**
     * @param color
     *            the color to set
     */
    public void setColor(int color) {
        this.color = color;
    }
}