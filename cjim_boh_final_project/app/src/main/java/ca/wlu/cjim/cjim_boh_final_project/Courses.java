package ca.wlu.cjim.cjim_boh_final_project;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Christopher Jim
 * Brian Oh
 * Final Project
 * CP400
 * manipulators for Courses List
 */

public class Courses {
    private static Courses sCourses;
    private ArrayList<Course> mCourses;

    public static Courses get(Context context) {
        if (sCourses == null) {
            sCourses = new Courses(context);
        }
        return sCourses;
    }

    private Courses(Context context) {

        mCourses = new ArrayList<>();

        Course course1 = new Course();
        course1.setCourseCode("CP331");
        course1.setCourseName("Parallel Networks");
        mCourses.add(course1);

        Course course2 = new Course();
        course2.setCourseCode("CP386");
        course2.setCourseName("Operating Systems");
        mCourses.add(course2);

        Course course3 = new Course();
        course3.setCourseCode("MA238");
        course3.setCourseName("Discrete Mathematics");
        mCourses.add(course3);



    }

    public void addCourse(Course c) {
        mCourses.add(c);
    }


    public List<Course> getCourses() {
        return mCourses;
    }

    public Course getCourse(UUID id) {
        for (Course course : mCourses) {
            if (course.getId().equals(id)) {
                return course;
            }
        }
        return null;
    }
}
