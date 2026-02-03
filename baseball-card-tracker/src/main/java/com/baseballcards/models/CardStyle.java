package com.baseballcards.models;

public class CardStyle {
    private String manufacturer;
    private int year;
    private String edition;

    public CardStyle() {
    }

    public CardStyle(String manufacturer, int year, String edition) {
        this.manufacturer = manufacturer;
        this.year = year;
        this.edition = edition;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }
}