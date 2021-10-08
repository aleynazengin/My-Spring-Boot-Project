package com.example.CrudApplicationUsingJpaMySql.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ScoreRequest {
    @NotNull(message = "Scor cannot be null")
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
