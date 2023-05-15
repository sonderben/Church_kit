package com.churchkit.churchkit.database.entity.base;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public abstract class Book {
    @PrimaryKey
    @NonNull
    protected String id;
    protected String abbreviation;


    protected String title;
    protected int position;
    protected int childAmount;

    protected String color;
    public String image;

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

    public Book(@NonNull String id, String abbreviation, String title, int position, int childAmount,String color,String image) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.title = title;
        this.position = position;
        this.childAmount = childAmount;
        this.color = color;
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return position == book.position && childAmount == book.childAmount && id.equals(book.id) && Objects.equals(abbreviation, book.abbreviation) && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, abbreviation, title, position, childAmount);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", title='" + title + '\'' +
                ", position=" + position +
                ", childAmount=" + childAmount +
                ", color='" + color + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getChildAmount() {
        return childAmount;
    }

    public void setChildAmount(int childAmount) {
        this.childAmount = childAmount;
    }
}
