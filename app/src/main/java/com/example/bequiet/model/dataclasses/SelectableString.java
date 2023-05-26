package com.example.bequiet.model.dataclasses;

public class SelectableString {

    private String data;
    private boolean selected = false;

    public SelectableString(String data, boolean selected) {
        this.data = data;
        this.selected = selected;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
