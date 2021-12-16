package com.mathquiz.mathquiz.model;

public class QuestionList {

    public String[] Question = {

            "9 x 13 = 117",
            "65 x 2 + 10 = 130",
            "90 + 1290 = 1480",
            "1987 - 2651 = -664",
            "90 % 6 = 15",
            "800 x 2 + 400 = 2000",
            "10 x 9 - 100 = 10",
            "90 % 2 + 35 = 80",
            "800 % 4 x 5 = 100",
            "70 + 90 - 160 = 10",
            "39 - 128 = -89",
            "26 + 16 + 8 = 40",
            "83 x 2 + 10 = 166",
            "1958 - 58 + 500 = 2400",
            "67 x 2 + 6 = 140",
            "550 x 3 = 1450",
            "1200 - 1300 + 50 = 50",
            "20 x 5 +100 = 200",
            "1800 % 3 = 900",
            "71 - 97 + 6 = -20"

    };

    public String[] Answer = {

            "true",
            "false",
            "false",
            "true",
            "true",
            "true",
            "false",
            "true",
            "false",
            "false",
            "true",
            "false",
            "false",
            "true",
            "true",
            "false",
            "false",
            "true",
            "false",
            "true"

    };

    public String getQuestion(int number) {
      return Question[number];
    }

    public String getAnswer(int number) {
        return Answer[number];
    }

}
