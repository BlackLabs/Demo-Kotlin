package mx.pagandocheck.pagandodemokotlin.models;

import android.graphics.Bitmap;
import android.graphics.Picture;

public class ElementToPrint {
    private float size;
    private String type;
    private Picture picture; // Assuming Picture is a class or interface you have defined

    // Constructor with all parameters
    public ElementToPrint( float size, String type ,Picture picture) {
        this.size = size;
        this.type = type;
        this.picture = picture;
    }

    public ElementToPrint( float size, String type ) {
        this.size = size;
        this.type = type;
        this.picture = null;
    }

    // Overloaded constructor with default values
    public ElementToPrint() {
        this( 16f, "text", null);
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}

 class MultiplePrint {
    private String text;
    private String date;
    private Bitmap image; // Nullable in Kotlin, can be null in Java as well
    private boolean isTitle;

    // Constructor with all parameters
    public MultiplePrint(String text, String date, Bitmap image, boolean isTitle) {
        this.text = text;
        this.date = date;
        this.image = image;
        this.isTitle = isTitle;
    }

    // Overloaded constructor with default values
    public MultiplePrint() {
        this("", "", null, false);
    }

    // Getters and Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }
}


