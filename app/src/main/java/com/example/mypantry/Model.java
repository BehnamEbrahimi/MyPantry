package com.example.mypantry;

public class Model {
    private int id;
    private String name;
    private double price;
    private double quantityInPantry;
    private long isBought;
    private double quantityToBuy;
    private String location;
    private byte[] image;
    private boolean checked;

    public Model(int id, String name, double price, double quantityInPantry, long isBought, double quantityToBuy, String location, byte[] image, boolean checked) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantityInPantry = quantityInPantry;
        this.isBought = isBought;
        this.quantityToBuy = quantityToBuy;
        this.location = location;
        this.image = image;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public double getQuantityInPantry() {
        return quantityInPantry;
    }

    public void setQuantityInPantry(float quantityInPantry) {
        this.quantityInPantry = quantityInPantry;
    }

    public long getIsBought() {
        return isBought;
    }

    public void setIsBought(long isBought) {
        this.isBought = isBought;
    }

    public double getQuantityToBuy() {
        return quantityToBuy;
    }

    public void setQuantityToBuy(float quantityToBuy) {
        this.quantityToBuy = quantityToBuy;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

