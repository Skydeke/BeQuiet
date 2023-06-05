package com.example.bequiet.model.dataclasses;

public class SelectableString {

    private final String data;
    private boolean selected;

    public SelectableString(String data, boolean selected) {
        this.data = data;
        this.selected = selected;
    }

    public String getData() {
        return data;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
