package com.placement.prepare.e2buddy.object;

public class CategoryData {
    private int categoryId;
    private String categoryName;

    public CategoryData(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
