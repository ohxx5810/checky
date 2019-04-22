package ca.wlu.cjim.cjim_boh_final_project;


import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Christopher Jim
 * Brian Oh
 * Final Project
 * CP400
 */


public class Course {
    private UUID mId;
    private String  mCourseCode;
    private String  mCourseName;
    //private String  mNextDueDate = null;
    private Date mNextDueDate = null;
    private static Course sCourse;
    private ArrayList<Mark> mMarks;
    private int mGradeAverage = 0;

    public Course() {
        super();
        mId = UUID.randomUUID();
        mMarks = new ArrayList<>();
    }
    public UUID getId() {
        return mId;
    }

    public static Course get(Context context) {
        if (sCourse == null) {
            sCourse = new Course(context);

        }
        return sCourse;
    }

    private Course(Context context) {

        /*
        Mark mark1 = new Mark();
        mark1.setName("Assignment 1");
        mMarks.add(mark1);

        Mark mark2 = new Mark();
        mark2.setName("Midterm");
        mMarks.add(mark2);
        */

    }

    public void addMark(Mark m){
        mMarks.add(m);}

    public List<Mark> getMarks() {
        return this.mMarks;
    }

    public Mark getMark(UUID id) {
        for (Mark mark : this.mMarks) {
            if (mark.getId().equals(id)) {
                return mark;
            }
        }
        return null;
    }
    //TODO getGradeAverage
    public int getGradeAverage(){
        int gradeCount = 0, temp;
        int i = 0;
        do{
            if(i < mMarks.size())
            mGradeAverage += mMarks.get(i).getGrade();
            gradeCount = gradeCount + 1;
            i++;
        }while (i<=mMarks.size());

        mGradeAverage = mGradeAverage/gradeCount;
        return mGradeAverage;
    }


    // ------------ Mutators --------------
    public String getCourseCode() { return mCourseCode; }

    public void setCourseCode(String courseCode) {
        mCourseCode = courseCode.toUpperCase();
    }

    public String getCourseName() { return mCourseName; }

    public void setCourseName(String couseName) {
        mCourseName = couseName.toUpperCase();
    }

    public void setNextDueDate(Date date) {
        mNextDueDate = date;
    }

    public Date getNextDueDate() {
        return mNextDueDate;
    }

}

