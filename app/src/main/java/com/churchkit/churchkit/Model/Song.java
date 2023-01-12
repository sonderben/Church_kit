package com.churchkit.churchkit.Model;

public class Song {
    private int number;
    private String title;
    private boolean isBookmark;

    public Song(int number, String title, boolean isBookmark) {
        this.number = number;
        this.title = title;
        this.isBookmark = isBookmark;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public boolean isBookmark() {
        return isBookmark;
    }
}
