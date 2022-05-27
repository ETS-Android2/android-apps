package com.example.tourismapp;

public class TopDestination
{

    // Declaring variables
    private int id, image;

    public TopDestination(int id, int image)
    {
        this.id = id;
        this.image = image;
    }

    // Setter & Getter


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
