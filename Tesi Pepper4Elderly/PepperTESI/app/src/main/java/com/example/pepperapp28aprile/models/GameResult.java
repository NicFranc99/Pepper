package com.example.pepperapp28aprile.models;

import java.io.Serializable;

public class GameResult  implements Serializable {

    private int score;
    private String creationDateResult;
    private int idResult;
    private boolean isActive;

    public GameResult(int score, String creationDateResult, int idResult, boolean isActive){
        this.score = score;
        this.creationDateResult = creationDateResult;
        this.idResult = idResult;
        this.isActive = isActive;
    }

    public int getScore(){
        return this.score;
    }

    public boolean getIsActive(){
        return this.isActive;
    }

    public String getCreationDateResult(){
        return this.creationDateResult;
    }

    public Integer getIdResult(){
        return this.idResult;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void setCreationDateResult(String creationDateResult){
        this.creationDateResult = creationDateResult;
    }

    public void setIdResult(int idResult){
        this.idResult = idResult;
    }
}
