package com.example.pepperapp28aprile.utilities;

public class ErrorGameNotStarted extends Exception {
    public ErrorGameNotStarted(String giocoNonAvviato) {
        super( giocoNonAvviato);
    }
}