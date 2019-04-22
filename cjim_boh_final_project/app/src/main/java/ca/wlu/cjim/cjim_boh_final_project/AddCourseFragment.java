package ca.wlu.cjim.cjim_boh_final_project;

/**
 * Christopher Jim
 * Brian Oh
 * Final Project
 * CP400
 * AddCourseFragment - add new course to semester
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.UUID;

public class AddCourseFragment extends Fragment {
    private Course mCourse;
    private EditText mCourseCode;
    private EditText mCourseName;
    private Button mAddCourseButton;
    private Button mCancelButton;
    private static final String ARG_COURSE_ID = "course_id";

    public static AddCourseFragment newInstance(UUID courseId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COURSE_ID, courseId);

        AddCourseFragment fragment = new AddCourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_course, container, false);

        mCourseCode = (EditText) v.findViewById(R.id.course_code);
        mCourseName = (EditText) v.findViewById(R.id.course_name);
        //mPhoneEdit = (EditText) v.findViewById(R.id.phone_number);
        mAddCourseButton = (Button) v.findViewById(R.id.add_button);
        mAddCourseButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String FullName, PhoneNumber;

                Course course = new Course();
                course.setCourseCode(mCourseCode.getText().toString());
                course.setCourseName(mCourseName.getText().toString());

                Courses.get(getActivity()).addCourse(course);

                getActivity().finish();
            }
        });
        mCancelButton = (Button) v.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getActivity().finish();
            }
        });

        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.course, menu);

        MenuItem mBackButton = menu.findItem(R.id.back_button_course);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_button_course:
                Intent intent = new Intent(getActivity(),CourseListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

