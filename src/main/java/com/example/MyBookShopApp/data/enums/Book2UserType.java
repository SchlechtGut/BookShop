package com.example.MyBookShopApp.data.enums;

public enum Book2UserType {
    KEPT, CART, PAID, ARCHIVED;

    private String name;

    {
        switch (name()) {
            case "KEPT": name = "Отложена";
                break;
            case "CART": name = "В корзине";
                break;
            case "PAID": name = "Куплена";
                break;
            case "ARCHIVED": name = "В архиве";
        }
    }

    public String getName() {
        return name;
    }
}
