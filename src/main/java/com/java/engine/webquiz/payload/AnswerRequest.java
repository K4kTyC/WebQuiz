package com.java.engine.webquiz.payload;

import java.util.ArrayList;
import java.util.List;

public class AnswerRequest {

    private List<Integer> answer = new ArrayList<>();


    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer == null ? new ArrayList<>() : answer;
    }
}
