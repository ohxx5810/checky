package ca.wlu.cjim.cjim_boh_final_project;

import android.content.Context;

import java.util.UUID;

/**
 * Christopher Jim
 * Brian Oh
 * Final Project
 * CP400
 * Mark class - getters and setters for school work marks
 */

public class Mark{
    private UUID mId;
    private String mName;
    private int mGrade;
    private int mWeight;
    private static final int FULL_PERCENT = 100;
    private static Mark sMark;
    //private Date mDueDate;   //TODO: Implement proper date
    private String mDueDate;
    private boolean mCompleted = false;

    public Mark() {
        mId = UUID.randomUUID();
    }
    public UUID getId() {
        return mId;
    }

    public static Mark get(Context context) {
        if (sMark == null) {
            sMark = new Mark(context);
        }
        return sMark;
    }

    private Mark(Context context) {
    }

    public void setName(String name) {
        mName = name;
    }
    public String getName() {
        return mName;
    }

    public void setGrade(int grade) {
        mGrade = grade;
    }
    public int getGrade() { return mGrade; }

    public void setWeight(int weight) {
        mWeight = weight;
    }
    public int getWeight() {
        return mWeight;
    }

    public int getMarkWithWeight() {return mGrade *mWeight/FULL_PERCENT;}

    public void setDueDate(String date) {
        mDueDate = date;
    }
    public String getDueDate() {
        return mDueDate;
    }

    public void makeCompleted() {
        mCompleted = true;
    }
    public boolean isCompleted() {
        return mCompleted;
    }

}
