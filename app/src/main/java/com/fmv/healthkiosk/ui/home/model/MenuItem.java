package com.fmv.healthkiosk.ui.home.model;

public class MenuItem {
    private final String id;
    private final String name;
    private final int logo;

    public MenuItem(String id, String name, int logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLogo() {
        return logo;
    }
}
