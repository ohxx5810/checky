package ca.wlu.cjim.cjim_boh_final_project;

/**
 * Christopher Jim
 * Brian Oh
 * Final Project
 * CP400
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import java.util.List;
import java.util.UUID;

public class CoursePagerActivity extends AppCompatActivity {

    private static final String EXTRA_COURSE_ID =
            "ca.wlu.cjim.cjim_boh_final_project.course_id";

    private ViewPager mViewPager;
    private List<Course> mCourses;

    public static Intent newIntent(Context packageContext, UUID courseId) {
        Intent intent = new Intent(packageContext, CoursePagerActivity.class);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_pager);

        UUID courseId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_COURSE_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_course_pager_view_pager);

        mCourses = Courses.get(this).getCourses();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Course course = mCourses.get(position);
                return CourseFragment.newInstance(course.getId());
            }

            @Override
            public int getCount() {
                return mCourses.size();
            }
        });

        for (int i = 0; i < mCourses.size(); i++) {
            if (mCourses.get(i).getId().equals(courseId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}

