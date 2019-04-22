package ca.wlu.cjim.cjim_boh_final_project;

/**
 * Christopher Jim
 * Brian Oh
 * Final Project
 * CP400
 * Course Fragment - Holds the marks and due dates
 */


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.widget.Toast;


public class CourseFragment extends Fragment {
    private RecyclerView mMarkRecyclerView;
    private MarkAdapter mAdapter;
    private Course mCourse;
    private TextView mNameField;
    private Button mDoneButton;
    private Button mAddButton;
    private Button mDueDateButton; //TODO: ??
    private CheckBox mCheckedBox;
    private EditText mWorkTitle;
    private EditText mMarkField;
    private EditText mWeightField;
    private int amountOfGrades, totalGradeSum, totalAverage, gradeAccordingToWeight;
    private final int full_percent = 100;
    //add color constants (black (0-49), red (50-59), orange (60-69), yellow (70-79), green (80-100):
        //get average compare to the 5 ranges.
        //if average is in that range set status color to reflect this
    private static boolean DONE_ADDING_MARKS = false;

    private static final String ARG_COURSE_ID = "course_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    public static CourseFragment newInstance(UUID courseId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COURSE_ID, courseId);

        CourseFragment fragment = new CourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID courseId = (UUID) getArguments().getSerializable(ARG_COURSE_ID);
        mCourse = Courses.get(getActivity()).getCourse(courseId);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(mCourse.getCourseCode());
        View v = inflater.inflate(R.layout.fragment_course, container, false);
        View v2 = inflater.inflate(R.layout.list_item_mark, container, false);

        mMarkRecyclerView = (RecyclerView) v.findViewById(R.id.mark_recycler_view);
        mMarkRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNameField = (TextView) v.findViewById(R.id.course_name);  //"OPERATING SYSTEMS"
        mNameField.setText(mCourse.getCourseName());


        mDoneButton = (Button) v.findViewById(R.id.done_adding);

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DONE_ADDING_MARKS = true;
                mAddButton.setVisibility(View.INVISIBLE);
                mDoneButton.setVisibility(View.INVISIBLE);
                updateUI();

                //Intent intent = new Intent(getActivity(), CourseListActivity.class);
                //startActivity(intent);

            }
        });

        UUID courseId = (UUID) getArguments().getSerializable(ARG_COURSE_ID);
        mCourse = Courses.get(getActivity()).getCourse(courseId);
        mAddButton = (Button) v.findViewById(R.id.list_item_mark_add_button);
        mDueDateButton = (Button) v.findViewById(R.id.list_item_mark_due_date_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mark mark = new Mark();
                UUID id = mCourse.getId();
                Course course = Courses.get(getActivity()).getCourse(id);
                course.addMark(mark);
                updateUI();
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe
                Course course = Course.get(getActivity());
                List<Mark> marks = course.getMarks();
                marks.remove(position);
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe
                Course course = Course.get(getActivity());
                List<Mark> marks = course.getMarks();
                marks.remove(position);
                updateUI();

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mMarkRecyclerView);

        getActivity().setTitle(mCourse.getCourseCode());
        updateUI();

        return v;
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return; }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCourse.setDueDate(date);
            //mDueDateButton.setText(mCourse.getDueDate().toString());
        }
    }
    */


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
                Intent intent = new Intent(getActivity(), CourseListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateUI() {
        List<Mark> marks = mCourse.getMarks();
        mAdapter = new CourseFragment.MarkAdapter(marks);

        if (DONE_ADDING_MARKS) {

            //mNameField.setEnabled(false);
            //mMarkField.setEnabled(false);
            //mWeightField.setEnabled(false);

        }

        mMarkRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(mCourse.getCourseCode());
        updateUI();
    }

    Calendar myCalendar = Calendar.getInstance();



    private class MarkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mStatusBox;
        private EditText mWorkTitle;
        private EditText mMarkField;
        private EditText mWeightField;
        private ArrayList<Mark> mMarks;
        private Button mDueDateButton;
        private Calendar mCalendar;
        private Mark mMark;
        private int year, month, day;


        private MarkHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //mStatusBox = (TextView) itemView.findViewById(R.id.list_item_mark_status);
            //mStatusBox.setBackgroundColor(Color.rgb(100,100,100));
            mWorkTitle = (EditText) itemView.findViewById(R.id.list_item_work_title);
            mMarkField = (EditText) itemView.findViewById(R.id.list_item_mark_text_view);
            mWeightField = (EditText) itemView.findViewById(R.id.list_item_mark_weight_text_view);
            mDueDateButton = (Button) itemView.findViewById(R.id.list_item_mark_due_date_button);


        }


        //TODO: FIX HERE !!!
        private void bindMark(final Mark mMark) {
            this.mMark = mMark;
            if(mMark.getName()!=null) {
                mWorkTitle.setText(this.mMark.getName());
            }
            if(mMark.getGrade()!=0 ) {
                mMarkField.setText(Integer.toString(this.mMark.getGrade()));
            }
            if (mMark.getWeight()!=0) {
                mWeightField.setText(Integer.toString(this.mMark.getWeight()));
            }
            if (this.mMark.getDueDate() != null){
                mDueDateButton.setText(this.mMark.getDueDate().toString());}
            //TODO set CourseListFragment Duedate to most recent one

            //TODO ACTION LISTENERS FOR THE EditText IN THE MARK RECYCLER VIEW\
            // -------------------------------MAY NEED TO CHANGE-----
            mWorkTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {
                    //System.out.println("\n\nERROR DETECTED HERE\n\n");
                    /*Toast.makeText(getActivity(),
                                "Click!", Toast.LENGTH_SHORT).show();*/
                    mMark.setName(mWorkTitle.getText().toString());

                }
            });


            mMarkField.addTextChangedListener(new TextWatcher() {
                int mGrade;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // This space intentionally left blank
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        UUID id = mCourse.getId();
                        Course course = Courses.get(getActivity()).getCourse(id);
                        int mGradeAverage = course.getGradeAverage();

                        mGrade = Integer.parseInt(mMarkField.getText().toString());
                        mMark.setGrade(mGrade);

                        /*
                        Toast.makeText(getActivity(),
                                mGradeAverage, Toast.LENGTH_LONG).show();
                                */






                    } catch (NumberFormatException e) {
                        Toast.makeText(getActivity(),
                                "Please enter an integer!", Toast.LENGTH_SHORT).show();
                    }


                }
            });
            mWeightField.addTextChangedListener(new TextWatcher() {
                int mWeight;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
                @Override
                public void afterTextChanged(Editable s) {

                    try {
                        mWeight = Integer.parseInt(mWeightField.getText().toString());
                        mMark.setWeight(mWeight);

                    } catch (NumberFormatException e) {
                        Toast.makeText(getActivity(),
                                "Please enter an integer!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            mDueDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = getFragmentManager();  //TODO: Issue from Listing 12.6?
                    //FragmentManager manager = getActivity().getSupportFragmentManager();
                    //DatePickerFragment dialog = new DatePickerFragment();
                    DatePickerFragment dialog = DatePickerFragment.newInstance(
                            mCourse.getNextDueDate());
                    dialog.setTargetFragment(CourseFragment.this, REQUEST_DATE); //TODO: needed? 12:30
                    dialog.show(manager, DIALOG_DATE);

                    /*
                    //TODO Add Date Picker and set CourseListFragment EditText to most recent duedate
                    // calender class's instance and get current date , month and year from calender
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR); // current year
                    int mMonth = c.get(Calendar.MONTH); // current month
                    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                    // date picker dialog
                    //datePickerDialog.show();
                    */

                }
            });


            //##############################################################################################################
        }



        @Override
        public void onClick(View v) {
            CourseFragment course = CourseFragment.newInstance(mCourse.getId());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, course);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();

        }

    }


    private class MarkAdapter extends RecyclerView.Adapter<MarkHolder> {

        private List<Mark> mMarks;

        private MarkAdapter(List<Mark> marks) {
            mMarks = marks;
        }

        @Override
        public MarkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_mark, parent, false);
            return new MarkHolder(view);
        }

        @Override
        public void onBindViewHolder(MarkHolder holder, int position) {
            //Course course = mCourse.get(position);
            Mark mark = mMarks.get(position);
            holder.bindMark(mark);
        }

        @Override
        public int getItemCount() {
            return mMarks.size();
        }


    }


}