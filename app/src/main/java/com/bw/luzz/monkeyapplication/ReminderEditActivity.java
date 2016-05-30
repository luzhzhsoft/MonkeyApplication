/*
 * Copyright 2015 Blanyal D'Souza.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.bw.luzz.monkeyapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.luzz.monkeyapplication.View.ArcProgressStackView;
import com.bw.luzz.monkeyapplication.View.Dialog;
import com.bw.luzz.monkeyapplication.View.material.ButtonFlat;
import com.bw.luzz.monkeyapplication.View.DateTimePicker.date.DatePickerDialog;
import com.bw.luzz.monkeyapplication.View.DateTimePicker.time.RadialPickerLayout;
import com.bw.luzz.monkeyapplication.View.DateTimePicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ReminderEditActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    private Toolbar mToolbar;
    private EditText mScriptText;
    private TextView mColorDesc, mTimeText, mRepeatText, mNameText, mHelpDesc;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
   // private Switch mRepeatSwitch;
    private ButtonFlat mRepeatButton;
    private String mScript;
    private String mTime;
    private String mDate;
    private String mName;
    private String mHelpString;
    private String mActive;
    private String mRepeat;
    private String[] mDateSplit;
    private String[] mTimeSplit;
    private int mReceivedID;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private Calendar mCalendar;
    private Reminder mReceivedReminder;
    private ReminderDatabase rb;
    //private AlarmReceiver mAlarmReceiver;

    // Constant Intent String
    public static final String EXTRA_REMINDER_ID = "Reminder_ID";

    // Values for orientation change
    private static final String KEY_SCRIPT = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_COLOR_DESC = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_NAME = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "active_key";

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;


    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayout;
    private long startTime;
    private int top;
    private DeskLayout deskLayout;

    /**
     L* @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        // Initialize Views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mScriptText = (EditText) findViewById(R.id.script);
        mColorDesc = (TextView) findViewById(R.id.color_desc);
        mTimeText = (TextView) findViewById(R.id.set_time);
        mRepeatText = (TextView) findViewById(R.id.set_repeat);
        mNameText = (TextView) findViewById(R.id.setname);
        mHelpDesc = (TextView) findViewById(R.id.helpdesc);
        mFAB1 = (FloatingActionButton) findViewById(R.id.fab);
       // mRepeatSwitch = (Switch) findViewById(R.id.repeat_switch);
        mRepeatButton=(ButtonFlat)findViewById(R.id.add);
        // Setup Toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_edit_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        createWindow();
        createDeskLayout();
        // Setup Reminder Title EditText
        mScriptText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mScript = s.toString().trim();
                mScriptText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Get reminder id from intent
        mReceivedID = Integer.parseInt(getIntent().getStringExtra(EXTRA_REMINDER_ID));

        // Get reminder using reminder id
        rb = new ReminderDatabase(this);
        mReceivedReminder = rb.getReminder(mReceivedID);

        // Get values from reminder
        mScript = mReceivedReminder.getScriptBody();
        mDate = mReceivedReminder.getDate();
        mTime = mReceivedReminder.getTime();
        mRepeat = mReceivedReminder.getRepeat();
        mName = mReceivedReminder.getScriptTitle();
        mHelpString = mReceivedReminder.getRepeatType();
        mActive = mReceivedReminder.getActive();

        // Setup TextViews using reminder values
        mScriptText.setText(mScript);
        mColorDesc.setText(mDate);
        mTimeText.setText(mTime);
        mNameText.setText(mName);
        mHelpDesc.setText(mHelpString);
        mRepeatText.setText("Every " + mName + " " + mHelpString + "(s)");

        // To save state on device rotation
        if (savedInstanceState != null) {
            String savedScript = savedInstanceState.getString(KEY_SCRIPT);
            mScriptText.setText(savedScript);
            mScript = savedScript;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            String savedColor = savedInstanceState.getString(KEY_COLOR_DESC);
            mColorDesc.setText(savedColor);
            mDate = savedColor;

            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(saveRepeat);
            mRepeat = saveRepeat;

            String savedName = savedInstanceState.getString(KEY_NAME);
            mNameText.setText(savedName);
            mName = savedName;

            String savedHelpDesc = savedInstanceState.getString(KEY_REPEAT_TYPE);
            mHelpDesc.setText(savedHelpDesc);
            mHelpString = savedHelpDesc;

            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }


       /* // Setup repeat switch
        if (mRepeat.equals("false")) {
            mRepeatSwitch.setChecked(false);
            mRepeatText.setText(R.string.repeat_off);

        } else if (mRepeat.equals("true")) {
            mRepeatSwitch.setChecked(true);
        }
*/
        // Obtain Date and Time details
        mCalendar = Calendar.getInstance();
        //mAlarmReceiver = new AlarmReceiver();

        /*mDateSplit = mDate.split("/");
        mTimeSplit = mTime.split(":");

        mDay = Integer.parseInt(mDateSplit[0]);
        mMonth = Integer.parseInt(mDateSplit[1]);
        mYear = Integer.parseInt(mDateSplit[2]);
        mHour = Integer.parseInt(mTimeSplit[0]);
        mMinute = Integer.parseInt(mTimeSplit[1]);*/
    }

    // To save state on device rotation
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_SCRIPT, mScriptText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_COLOR_DESC, mColorDesc.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_NAME, mNameText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, mHelpDesc.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    // On clicking Time picker
    public void setTime(View v){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    // On clicking Date picker
    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        mTimeText.setText(mTime);
    }

    // Obtain date from date picker
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        mColorDesc.setText(mDate);
    }



    // On clicking the repeat switch
    public void onWhileAdd(View view) {
        Toast.makeText(getApplicationContext(),"添加while",Toast.LENGTH_SHORT).show();

        String script= mScriptText.getText().toString();
        mScriptText.setText("While true \n"+script+"\n End \n");
    }

    // On clicking repeat type button
    public void onHelpClick(View v){
        /*final String[] items = new String[5];

        items[0] = "Minute";
        items[1] = "Hour";
        items[2] = "Day";
        items[3] = "Week";
        items[4] = "Month";

        // Create List Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mHelpString = items[item];
                mHelpDesc.setText(mHelpString);
                mRepeatText.setText("Every " + mName + " " + mHelpString + "(s)");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();*/

        Toast.makeText(getApplicationContext(),"help tiao zhuan",Toast.LENGTH_SHORT).show();
    }

    // On clicking repeat interval button
    public void setScriptName(View v){
        /*AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Number");

        // Create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mName = Integer.toString(1);
                            mNameText.setText(mName);
                            mRepeatText.setText("Every " + mName + " " + mHelpString + "(s)");
                        }
                        else {
                            mName = input.getText().toString().trim();
                            mNameText.setText(mName);
                            mRepeatText.setText("Every " + mName + " " + mHelpString + "(s)");
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing
            }
        });
        alert.show();*/
        final Dialog dialog=new Dialog(this,"输入脚本名称","默认脚本名为：我的脚本");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (input.getText().toString().length() == 0) {
                    mName = "我的脚本";
                    mNameText.setText(mName);
                    mRepeatText.setText("名称 " + mName );
                }
                else {
                    mName = input.getText().toString().trim();
                    mNameText.setText(mName);
                    mRepeatText.setText("名称 " + mName);
                }*/

                String input=dialog.getMessage();
                if (input.length() == 0) {
                    mName = "我的脚本";
                    mNameText.setText(mName);
                    mRepeatText.setText("名称 " + mName);
                }
                else {
                    mName = input.trim();
                    mNameText.setText(mName);
                    mRepeatText.setText("名称 " + mName);
                }
            }
        });
        dialog.show();
        if(mName !=null){
            dialog.setMessage(mName);
        }
    }

    // On clicking the update button
    public void updateReminder(){
        // Set new values in the reminder
        mReceivedReminder.setScriptBody(mScript);
        mReceivedReminder.setDate(mDate);
        //mReceivedReminder.setTime(mTime);
        mReceivedReminder.setRepeat(mRepeat);
        mReceivedReminder.setScriptTitle(mName);
        mReceivedReminder.setRepeatType(mHelpString);
        mReceivedReminder.setActive(mActive);


        Date date=new Date(System.currentTimeMillis());
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateandtime=dateFormat.format(date);

        mReceivedReminder.setTime(dateandtime);

        // Update reminder
        rb.updateReminder(mReceivedReminder);

        // Set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        // Cancel existing notification of the reminder by using its ID
        //mAlarmReceiver.cancelAlarm(getApplicationContext(), mReceivedID);

       /* // Check repeat type
        if (mHelpString.equals("Minute")) {
            mRepeatTime = Integer.parseInt(mName) * milMinute;
        } else if (mHelpString.equals("Hour")) {
            //mRepeatTime = Integer.parseInt(mName) * milHour;
        } else if (mHelpString.equals("Day")) {
            mRepeatTime = Integer.parseInt(mName) * milDay;
        } else if (mHelpString.equals("Week")) {
            mRepeatTime = Integer.parseInt(mName) * milWeek;
        } else if (mHelpString.equals("Month")) {
            mRepeatTime = Integer.parseInt(mName) * milMonth;
        }*/

        // Create a new notification
        if (mActive.equals("true")) {
            /*if (mRepeat.equals("true")) {
                mAlarmReceiver.setRepeatAlarm(getApplicationContext(), mCalendar, mReceivedID, mRepeatTime);
            } else if (mRepeat.equals("false")) {
                mAlarmReceiver.setAlarm(getApplicationContext(), mCalendar, mReceivedID);
            }*/
        }

        // Create toast to confirm update
        Toast.makeText(getApplicationContext(), "Edited",
                Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    // On pressing the back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Creating the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    // On clicking menu buttons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // On clicking the back arrow
            // Discard any changes
            case android.R.id.home:
                onBackPressed();
                return true;

            // On clicking save reminder button
            // Update reminder
            case R.id.save_script:
                mScriptText.setText(mScript);

                if (mScriptText.getText().toString().length() == 0)
                    mScriptText.setError("脚本不能为空");

                else {
                    updateReminder();
                }
                return true;

            // On clicking discard reminder button
            // Discard any changes
            case R.id.discard_script:
                Toast.makeText(getApplicationContext(), "放弃修改",
                        Toast.LENGTH_SHORT).show();

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @NonNull
    private void createDeskLayout() {
        deskLayout=new DeskLayout(this);

        deskLayout.setOnTouchListener(new View.OnTouchListener() {
            float mTouchX;
            float mTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float rawX = event.getRawX();
                float rawY = event.getRawY() - top;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchX = event.getX();
                        mTouchY = event.getY();
                        long endTime = System.currentTimeMillis() - startTime;
                        if (endTime <= 300) {
                            hideWidow(deskLayout);
                            Intent i=new Intent(getApplicationContext(),FloatWindowActivity.class);
                            startActivity(i);
                        }
                        startTime = System.currentTimeMillis();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        mLayout.x = (int) (rawX - mTouchX);
                        mLayout.y = (int) (rawY - mTouchY);
                        mWindowManager.updateViewLayout(deskLayout, mLayout);

                        break;
                    case MotionEvent.ACTION_UP:
                        mLayout.x = (int) (rawX - mTouchX);
                        mLayout.y = (int) (rawY - mTouchY);
                        mWindowManager.updateViewLayout(deskLayout, mLayout);
                        mTouchX = mTouchY = 0;
                        break;
                }
                return true;
            }
        });

    }

    private void hideWidow(DeskLayout deskLayout) {
        mWindowManager.removeView(deskLayout);
        finish();
    }
    private void showWindow(DeskLayout deskLayout) {
        mWindowManager.addView(deskLayout, mLayout);
        finish();
    }

    private void createWindow() {
        mWindowManager = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayout = new WindowManager.LayoutParams();

        mLayout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 设置窗体焦点及触摸：
        // FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)
        mLayout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置显示的模式
        mLayout.format = PixelFormat.RGBA_8888;
        // 设置对齐的方法
        mLayout.gravity = Gravity.TOP | Gravity.LEFT;
        // 设置窗体宽度和高度
        mLayout.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayout.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Rect rect=new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        top = rect.top;

    }

    public class DeskLayout extends LinearLayout {
        public final static int MODEL_COUNT = 4;


        // APSV
        private ArcProgressStackView mArcProgressStackView;
        // Parsed colors
        private int[] mStartColors = new int[MODEL_COUNT];
        private int[] mEndColors = new int[MODEL_COUNT];

        private Messenger bananaService;

        public DeskLayout(Context context) {
            super(context);
            setOrientation(LinearLayout.VERTICAL);
            this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            View v= LayoutInflater.from(context).inflate(R.layout.float_window_layout,null);

            this.addView(v);
            // Get APSV
            mArcProgressStackView = (ArcProgressStackView) v.findViewById(R.id.apsv);
            // Get colors
            final String[] startColors = getResources().getStringArray(R.array.polluted_waves);
            final String[] endColors = getResources().getStringArray(R.array.default_preview);
            final String[] bgColors = getResources().getStringArray(R.array.medical_express);

            // Parse colors
            for (int i = 0; i < MODEL_COUNT; i++) {
                mStartColors[i] = Color.parseColor(startColors[i]);
                mEndColors[i] = Color.parseColor(endColors[i]);
            }

            final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
            models.add(new ArcProgressStackView.Model("RED  210", 25, Color.parseColor("#ff890000"), mStartColors[0]));
            models.add(new ArcProgressStackView.Model("GREEN 120", 50, Color.parseColor(bgColors[1]), mStartColors[1]));
            models.add(new ArcProgressStackView.Model("BLUE 100", 75, Color.parseColor(bgColors[2]), mStartColors[2]));
            models.add(new ArcProgressStackView.Model("INT -1024512", 100, Color.parseColor(bgColors[3]), mStartColors[3]));

            mArcProgressStackView.setModels(models);
        }



    }
}