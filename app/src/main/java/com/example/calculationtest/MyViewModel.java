package com.example.calculationtest;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MyViewModel extends AndroidViewModel {
    private SavedStateHandle handle;
    private static String KEY_HIGH_SCORE = "key_high_score";
    private static String KEY_LEFT_NUMBER = "key_left_number";
    private static String KEY_RIGHT_NUMBER = "key_right_number";
    private static String KEY_OPERATOR = "key_operator";
    private static String KEY_ANSWER = "key_answer";
    private static String SAVE_SHP_DATA_NAME = "save_shp_data_name";
    private static String KEY_CURRENT_SCORE = "key_current_score";
    private static String KEY_REST_TIME = "key_rest_time";
    boolean win_flag = false;
    public MyViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        if (!handle.contains(KEY_HIGH_SCORE)) {
            SharedPreferences shp = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);
            handle.set(KEY_HIGH_SCORE,shp.getInt(KEY_HIGH_SCORE,0));
            handle.set(KEY_LEFT_NUMBER,0);
            handle.set(KEY_RIGHT_NUMBER,0);
            handle.set(KEY_OPERATOR,"+");
            handle.set(KEY_ANSWER,0);
            handle.set(KEY_CURRENT_SCORE,0);
            handle.set(KEY_REST_TIME,100);

        }
        this.handle = handle;
    }
    public MutableLiveData<Integer>getLeftNumber(){
        return handle.getLiveData(KEY_LEFT_NUMBER);
    }
    public MutableLiveData<Integer>getRightNumber(){
        return handle.getLiveData(KEY_RIGHT_NUMBER);
    }
    public MutableLiveData<String>getOperator() {
        return handle.getLiveData(KEY_OPERATOR);
    }
    public MutableLiveData<Integer>getHighScore(){
        return handle.getLiveData(KEY_HIGH_SCORE);
    }
    public MutableLiveData<Integer>getCurrentScore(){
        return handle.getLiveData(KEY_CURRENT_SCORE);
    }
    public MutableLiveData<Integer>getAnswer(){
        return handle.getLiveData(KEY_ANSWER);
    }
    public MutableLiveData<Integer>getTime() {return handle.getLiveData(KEY_REST_TIME);}
//    public MutableLiveData<String>getBG() {return handle.getLiveData(KEY_BG);}
    void generator() {
        int LEVEL = 1;
        getTime().setValue(100);
        if (getCurrentScore().getValue() > 0){
            LEVEL = getCurrentScore().getValue() + 1;
        }
//
//        if (LEVEL % 1 == 1){
//            getBG().setValue("#DAF3E5A0");
//        }else if (LEVEL % 1 == 2){
//            getBG().setValue("#DAF6E17B");
//        }else if (LEVEL % 1 == 3){
//            getBG().setValue("#DAF6DC5A");
//        }else if (LEVEL % 1 == 4){
//            getBG().setValue("#DAF8D83B");
//        }else if (LEVEL % 1 == 5){
//            getBG().setValue("#DAD8F45B");
//        }else if (LEVEL % 1 == 6){
//            getBG().setValue("#DAA2F45B");
//        }else if (LEVEL % 1 == 7){
//            getBG().setValue("#DA8FE6AF");
//        }else if (LEVEL % 1 == 8){
//            getBG().setValue("#DA7384DA");
//        }else if (LEVEL % 1 == 9){
//            getBG().setValue("#DAD89BE3");
//        }else if (LEVEL % 1 == 10){
//            getBG().setValue("#DAF38484");
//        }else if (LEVEL % 1 == 11){
//            getBG().setValue("#DADCBB6A");
//        }

        Random random = new Random();
        int x,y;
        x = random.nextInt(LEVEL) + 1;
        y = random.nextInt(LEVEL) + 1;
        if (x%2==0) {
            getOperator().setValue("+");
            if (x>y) {
                getAnswer().setValue(x);
                getLeftNumber().setValue(y);
                getRightNumber().setValue(x - y);
            } else {
                getAnswer().setValue(y);
                getLeftNumber().setValue(x);
                getRightNumber().setValue(y - x);
            }

        } else {
            getOperator().setValue("-");
            if (x>y) {
                getAnswer().setValue(x - y);
                getLeftNumber().setValue(x);
                getRightNumber().setValue(y);
            } else {
                getAnswer().setValue(y - x);
                getLeftNumber().setValue(y);
                getRightNumber().setValue(x);
            }
        }
    }


    CountDownTimer timer = new CountDownTimer(10*1000,1000) {
        @Override
        public void onTick(long l) {
            getTime().setValue(getTime().getValue() - 2);
        }

        @Override
        public void onFinish() {

        }
    };


        @SuppressWarnings("ConstantConditions")
    void save() {
        SharedPreferences shp = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putInt(KEY_HIGH_SCORE, getHighScore().getValue());
        editor.apply();
    }

    @SuppressWarnings("ConstantConditions")
    void answerCorrect() {
        getCurrentScore().setValue(getCurrentScore().getValue() + 1);
        if (getCurrentScore().getValue() > getHighScore().getValue()) {
            getHighScore().setValue(getCurrentScore().getValue());
            win_flag = true;
        }
        generator();
        timer.start();
    }
}

