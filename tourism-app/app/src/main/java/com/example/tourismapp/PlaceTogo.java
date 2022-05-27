package com.example.tourismapp;

public class PlaceTogo
{

    // Declaring variables
    private int id, image;
    private String name, location, description;

    public PlaceTogo(int id, int image, String name, String location, String description)
    {
        this.id = id;
        this.image = image;
        this.name = name;
        this.location = location;
        this.description = description;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
