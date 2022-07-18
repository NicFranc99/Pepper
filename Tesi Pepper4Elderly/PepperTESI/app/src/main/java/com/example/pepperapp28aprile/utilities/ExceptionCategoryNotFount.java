package com.example.pepperapp28aprile.utilities;

class ExceptionCategoryNotFount extends Exception {
    public ExceptionCategoryNotFount(String categoria_non_presente) {
        super( categoria_non_presente);
    }
}
