package ca.wlu.cjim.cjim_boh_final_project;


/**
 * Christopher Jim
 * Brian Oh
 * Final Project
 * CP400
 */


import android.support.v4.app.Fragment;

public class CourseListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CourseListFragment();
    }
}

