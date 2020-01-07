package com.example.cinemaapp;

import java.nio.file.FileAlreadyExistsException;

public class FavMovie {

    public  String title;
    public String descript;

    public FavMovie()
    {

    }

    public FavMovie(String title, String descript) {
        this.title = title;
        this.descript = descript;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getTitle() {
        return title;
    }

    public String getDescript() {
        return descript;
    }
}
