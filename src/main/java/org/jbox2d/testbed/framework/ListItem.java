package org.jbox2d.testbed.framework;

public class ListItem {
    public String category;
    public PlayLevel test;

    public ListItem(String argCategory) {
        category = argCategory;
    }

    public ListItem(PlayLevel argTest) {
        test = argTest;
    }

    public boolean isCategory() {
        return category != null;
    }

    @Override
    public String toString() {
        return isCategory() ? category : test.getLevelName();
    }
}