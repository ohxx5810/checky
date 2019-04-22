package ca.wlu.cjim.cjim_boh_final_project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


/**
 * Christopher Jim
 * Brian Oh
 * Final Project
 * CP400
 * Course List - Semester
 */

public class CourseListFragment extends Fragment{
    private RecyclerView mCourseRecyclerView;
    private CourseAdapter mAdapter;
    private Button mDoneCoursesButton;
    private Button mNewSemesterButton;
    private ImageView mImageView;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final String EXTRA_COURSE_ID = "ca.wlu.cjim.cjim_a4.course_id";
    private static final int GRADE_F[] = {0,49};
    private static final int GRADE_D[] = {50,59};
    private static final int GRADE_C[] = {60,69};
    private static final int GRADE_B[] = {70,79};
    private static final int GRADE_A[] = {80,100};
    private static boolean no_more_courses = false;
    private static boolean new_semester = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.course_list_frag_title);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String photo_name = getString(R.string.photo_name);

        View view = inflater.inflate(R.layout.fragment_course_list, container, false);

        //TODO: image_view_home_page removed!
        /*
        mImageView = (ImageView) view.findViewById(R.id.image_view_home_page);
        String packageName = this.getClass().getPackage().getName();

        int resID = getResources().getIdentifier(photo_name,"mipmap",packageName);
        mImageView.setImageResource(resID);
        */

        mCourseRecyclerView = (RecyclerView) view.findViewById(R.id.course_recycler_view);
        mCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mDoneCoursesButton = (Button) view.findViewById(R.id.done_courses_button);

        if (no_more_courses == true) {
            setHasOptionsMenu(false);
            mDoneCoursesButton.setVisibility(View.INVISIBLE);
        }

        mDoneCoursesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                setHasOptionsMenu(false);
                mDoneCoursesButton.setVisibility(View.INVISIBLE);
                mNewSemesterButton.setVisibility(View.VISIBLE);
                no_more_courses = true;
                new_semester = true;

            }
        });

        mNewSemesterButton = (Button) view.findViewById(R.id.new_semester_button);
        if (new_semester == false) {
            mNewSemesterButton.setVisibility(View.INVISIBLE);
        }
        mNewSemesterButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                setHasOptionsMenu(true);
                mNewSemesterButton.setVisibility(View.INVISIBLE);
                mDoneCoursesButton.setVisibility(View.VISIBLE);
                Courses coursesActivity = Courses.get(getActivity());
                List<Course> courses = coursesActivity.getCourses();
                for(int i = 0; i < courses.size()+1; i++) {
                    courses.clear();
                    updateUI();
                }
                no_more_courses = false;
                new_semester = true;
            }
        });

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe
                Courses coursesActivity = Courses.get(getActivity());
                List<Course> courses = coursesActivity.getCourses();
                courses.remove(position);
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe
                Courses coursesActivity = Courses.get(getActivity());
                List<Course> courses = coursesActivity.getCourses();
                courses.remove(position);
                updateUI();

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mCourseRecyclerView);

        updateUI();
        return view;
    }

    private void updateUI() {
        Courses coursesActivity = Courses.get(getActivity());
        List<Course> courses = coursesActivity.getCourses();

        mAdapter = new CourseAdapter(courses);
        mCourseRecyclerView.setAdapter(mAdapter);
        getActivity().setTitle(R.string.course_list_frag_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_course_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_course:
                Intent intent = new Intent (getActivity(), AddCourseActivity.class);
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        Courses courses = Courses.get(getActivity());
        int courseCount = courses.getCourses().size();
        String subtitle = getString(R.string.subtitle_format, courseCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mCourseNameTextView;
        private TextView mColorIndicator;
        //private TextView mMarkAverage;
        private ImageView mImageView;
        private Course mCourse;
        private String mNewMark = "0%";
        private TextView mDueDate;



        public CourseHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //mImageView = (ImageView) itemView.findViewById(R.id.list_item_thumbnail);
            mCourseNameTextView = (TextView) itemView.findViewById(R.id.list_item_course_title_text_view);
            mDueDate = (TextView) itemView.findViewById(R.id.list_item_course_due_date_text_view);
            mColorIndicator = (TextView) itemView.findViewById(R.id.list_item_grade_average_color_indicator);
            //mMarkAverage = (TextView)   itemView.findViewById(R.id.list_item_mark_average);
        }

        public void bindCourse(Course course) {
            mCourse = course;
            //String mPhotoName = mCourse.getPhotoName();
            //String packageName = this.getClass().getPackage().getName();
            //int resID = getResources().getIdentifier(mPhotoName,"drawable",packageName);
            //mImageView.setImageResource(resID);
            mCourseNameTextView.setText(mCourse.getCourseCode() + " - " + mCourse.getCourseName());

            if (mCourse.getNextDueDate() != null)
                mDueDate.setText(mCourse.getNextDueDate().toString());

            //TODO Colour Indicator set accroding to grade average


            if (mCourse.getMarks().size() == 0) {
                mColorIndicator.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.yellow));
            }
            else{
                if (mCourse.getGradeAverage() >= GRADE_D[0] && mCourse.getGradeAverage() <= GRADE_D[1]) {
                    mColorIndicator.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.red));

                } else if (mCourse.getGradeAverage() >= GRADE_C[0] && mCourse.getGradeAverage() <= GRADE_C[1]) {
                    mColorIndicator.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.orange));

                } else if (mCourse.getGradeAverage() >= GRADE_B[0] && mCourse.getGradeAverage() <= GRADE_B[1]) {
                    mColorIndicator.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.yellow));

                } else if (mCourse.getGradeAverage() >= GRADE_A[0] && mCourse.getGradeAverage() <= GRADE_A[1]) {
                    mColorIndicator.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.green));

                }
                else{
                    mColorIndicator.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
                }

            }



        }

        @Override
        public void onClick (View v) {
            CourseFragment course = CourseFragment.newInstance(mCourse.getId());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, course);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();

        }
    }


    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {

        private List<Course> mCourses;

        public CourseAdapter(List<Course> courses) {
            mCourses = courses;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_course, parent, false);
            return new CourseHolder(view);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position) {
            Course course = mCourses.get(position);
            holder.bindCourse(course);

        }

        @Override
        public int getItemCount() {
            return mCourses.size();
        }


    }




}



