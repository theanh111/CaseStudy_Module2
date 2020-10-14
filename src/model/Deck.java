package model;

import java.io.Serializable;

public class Deck implements Serializable {
    private int id;
    private float size;
    private String name;
    private String brand;
    private long price;
    private String color;
    private String image;

    public Deck() {
    }

    public Deck(int id, float size, String name, String brand, long price, String color, String image) {
        this.id = id;
        this.size = size;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.color = color;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "id=" + id +
                ", size=" + size +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", color='" + color + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
