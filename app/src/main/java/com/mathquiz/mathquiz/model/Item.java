package com.mathquiz.mathquiz.model;

public class Item {

    private final String questions;
    private final String answers;

    public Item(String questions, String answers) {
        this.questions = questions;
        this.answers = answers;
    }

    public String getQuestions() {
        return questions;
    }

    public String getAnswers() {
        return answers;
    }
}
