package com.rajesh.gallary.utils;


import static com.rajesh.gallary.common.Constant.ENABLE_FINGER_PRINT_KEY;
import static com.rajesh.gallary.common.Constant.Pin_Key;

import android.content.Context;

import io.paperdb.Paper;

public class PasswordHelper {

    private boolean isFirstStep = true;
    private boolean isCheckableOLd = false;
    public PasswordHelper(Context context) {
        Paper.init(context);
    }

//    //Pattern methods
//    public void setPatternPass(String Pattern_Password) {
//        Paper.book().write(Pattern_key, Pattern_Password);
//    }
//
//    public String getPatternPass() {
//        return Paper.book().read(Pattern_key);
//    }
//
//    public boolean isPatternCorrect(String PatternPassword) {
//        return PatternPassword.equals(getPatternPass());
//    }
//
//    public void ClearPatternPassword() {
//        Paper.book().delete(Pattern_key);
//    }

    //Pin methods
    public void setPinPass(String Pin_Password) {
        Paper.book().write(Pin_Key, Pin_Password);
    }

    public String getPinPass() {
        return Paper.book().read(Pin_Key);
    }

    public boolean isPinCorrect(String PinPassword) {
        return PinPassword.equals(getPinPass());
    }

    public void ClearPinPassword() {
        Paper.book().delete(Pin_Key);
    }

    //Finger Print
    public void setFingerEnable(String FingerResult){
        Paper.book().write(ENABLE_FINGER_PRINT_KEY,FingerResult);
    }

    public String isFingerEnable(){
        return Paper.book().read(ENABLE_FINGER_PRINT_KEY);
    }

    //Check the steps
    public boolean isFirstStep() {
        return isFirstStep;
    }

    public void setFirstStep(boolean firstStep) {
        isFirstStep = firstStep;
    }

    public boolean isCheckableOLd() {
        return isCheckableOLd;
    }

    public void setCheckableOLd(boolean checkableOLd) {
        isCheckableOLd = checkableOLd;
    }
}
