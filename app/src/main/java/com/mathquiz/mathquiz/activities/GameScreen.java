package com.mathquiz.mathquiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mathquiz.mathquiz.model.Item;
import com.mathquiz.mathquiz.model.Player;
import com.mathquiz.mathquiz.model.QuestionList;
import com.mathquiz.mathquiz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class GameScreen extends AppCompatActivity {

    AlertDialog.Builder popUpDialog;

    TextView txtQuestion;
    TextView txtLevel;
    TextView txtTime;
    TextView txtScore;
    Button btnFalse;
    Button btnTrue;

    QuestionList mQuestion;
    public int currentQue = 0;
    int currentLvl = 1;
    int currentScore = 0;
    String desc = "0";
    boolean gameWinner = false;
    ArrayList<Item>QList;

    private static final String FORMAT = "%02d";
    private static final long START_TIME_IN_MILLIS = 20000;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMills = START_TIME_IN_MILLIS;
    private boolean mTimerRunning;

    String googlePersonalId;
    String googlePersonalName;

    Object dbUserScore;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        txtQuestion = findViewById(R.id.txt_question);
        txtLevel = findViewById(R.id.txt_level);
        txtScore = findViewById(R.id.txt_score);
        txtTime = findViewById(R.id.txt_time);
        btnTrue = findViewById(R.id.btnTrue);
        btnFalse = findViewById(R.id.btnFalse);

        popUpDialog = new AlertDialog.Builder(GameScreen.this);

        mQuestion = new QuestionList();

        QList = new ArrayList<>();

        for (int i = 0; i < mQuestion.Question.length; i++){
            QList.add(new Item(mQuestion.getQuestion(i),mQuestion.getAnswer(i)));
        }

        Collections.shuffle(QList);
        setData(currentQue);
        startTimer();

        btnTrue.setOnClickListener(view -> {
            pauseTimer();
            if (checkAnswer(currentQue)){
                currentQue++;
                currentLvl++;
                currentScore = currentScore + 5;
                if (currentQue < mQuestion.Question.length){
                    pauseTimer();
                    resetTimer();
                    startTimer();
                    setData(currentQue);
                }else{
                    gameWinner = true;
                    finalResult();
                }
            }else{
                finalResult();
            }

        });

        btnFalse.setOnClickListener(view -> {
            pauseTimer();
            if (!checkAnswer(currentQue)){
                currentQue++;
                currentLvl++;
                currentScore = currentScore + 5;
                if (currentQue < mQuestion.Question.length){
                    pauseTimer();
                    resetTimer();
                    startTimer();
                    setData(currentQue);
                }else{
                    gameWinner = true;
                    finalResult();
                }
            }else{
                finalResult();
            }

        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            googlePersonalId = personId;
            googlePersonalName = personName;

        }

        checkNewUser();

    }

    public void checkNewUser(){
        FirebaseDatabase.getInstance().getReference("Player").child(googlePersonalId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbUserScore = snapshot.child("score").getValue();
                if(dbUserScore == null){
                    addDbScore();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void checkBestScore(Integer currentHighScore, String desc){

        String s = dbUserScore.toString();
        int i =Integer.parseInt(s);

        if (i < currentHighScore){

            Player scr = new Player(googlePersonalName,currentHighScore,desc);

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            databaseReference = db.getReference("Player");
            databaseReference.child(googlePersonalId).setValue(scr);

        }
    }

    public void addDbScore(){

        Player scr = new Player(googlePersonalName,currentScore,desc);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference("Player");
        databaseReference.child(googlePersonalId).setValue(scr);
    }

    public void showGameOverPopUpDialog(Integer mLevel, Integer mScore){
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.common_popup_view, null);
            popUpDialog.setView(dialogView);
            AlertDialog alertDialog = popUpDialog.create();
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setGravity(Gravity.CENTER);
            alertDialog.show();

            Button btnExit = dialogView.findViewById(R.id.btnExit);
            Button btnResume = dialogView.findViewById(R.id.btnResume);
            TextView mainText = dialogView.findViewById(R.id.txtMain);
            TextView txtScore = dialogView.findViewById(R.id.txt_popup_score);
            TextView txtLevel = dialogView.findViewById(R.id.txt_popup_level);

            mainText.setText(R.string.txt_game_over);
            txtScore.setText("Score: " + mScore);
            txtLevel.setText("Level: " + mLevel);

            desc = mScore.toString();

            checkBestScore(mScore,desc);

            btnResume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentLvl = 1;
                    currentScore = 0;
                    if (currentQue < mQuestion.Question.length){
                        pauseTimer();
                        resetTimer();
                        startTimer();
                        Collections.shuffle(QList);
                        setData(0);
                    }
                    alertDialog.dismiss();
                }
            }

            );

            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            }

            );

    }

    public void showTimeOverPopUpDialog(Integer mLevel, Integer mScore){
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.common_popup_view, null);

            popUpDialog.setView(dialogView);
            AlertDialog alertDialog = popUpDialog.create();

            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.setCancelable(false);

            alertDialog.getWindow().setGravity(Gravity.CENTER);

            alertDialog.show();

            Button btnExit = dialogView.findViewById(R.id.btnExit);
            Button btnResume = dialogView.findViewById(R.id.btnResume);
            TextView mainText = dialogView.findViewById(R.id.txtMain);
            TextView txtScore = dialogView.findViewById(R.id.txt_popup_score);
            TextView txtLevel = dialogView.findViewById(R.id.txt_popup_level);

            mainText.setText(R.string.msg_time_out);
            txtScore.setText("Score: " + mScore);
            txtLevel.setText("Level: " + mLevel);

            desc = mScore.toString();

            checkBestScore(mScore,desc);

        btnResume.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             currentLvl = 1;
                                             currentScore = 0;
                                             if (currentQue < mQuestion.Question.length){
                                                 pauseTimer();
                                                 resetTimer();
                                                 startTimer();
                                                 Collections.shuffle(QList);
                                                 setData(0);
                                             }
                                             alertDialog.dismiss();
                                         }
                                     }

        );

        btnExit.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           finish();
                                       }
                                   }

        );
    }

    public void showGameWinnerPopUpDialog(){
        try {
            popUpDialog = new AlertDialog.Builder(getBaseContext());
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.game_winning_popup_view, null);
            popUpDialog.setView(dialogView);
            AlertDialog alertDialog = popUpDialog.create();
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setGravity(Gravity.BOTTOM);
            alertDialog.show();

            Button btnExit = dialogView.findViewById(R.id.btnExit);

            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /** Start Timer */
    void startTimer() {
       mCountDownTimer = new CountDownTimer(mTimeLeftInMills,1000) {

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMills = millisUntilFinished;
                updateCountDownText();
            }

            public void onFinish() {
                mTimerRunning = false;
                showTimeOverPopUpDialog(currentLvl,currentScore);
            }
        }.start();
       mTimerRunning = true;
    }

    void updateCountDownText(){
        int minutes = (int) (mTimeLeftInMills / 1000) / 60;
        int seconds = (int) (mTimeLeftInMills / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(),FORMAT , seconds);
        //txtTime.setText(""+String.format(FORMAT, TimeUnit.MILLISECONDS.toSeconds(mTimeLeftInMills) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mTimeLeftInMills))));
        txtTime.setText(timeLeftFormatted);
    }

    /** Pause Timer */
    void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
    }

    /** Reset Timer */
    void resetTimer(){
        mTimeLeftInMills = START_TIME_IN_MILLIS;
        updateCountDownText();
    }

    @SuppressLint("SetTextI18n")
    private void setData(int number){
        txtQuestion.setText(QList.get(number).getQuestions());
        txtLevel.setText("Level "+ currentLvl);
        txtScore.setText(""+ currentScore);
    }

    private boolean checkAnswer(int number){
        String ans = QList.get(number).getAnswers();
        return ans.equals("true");
    }

    private void finalResult(){
        if(gameWinner){
            showGameWinnerPopUpDialog();
        }else {
            showGameOverPopUpDialog(currentLvl,currentScore);
        }
    }

}