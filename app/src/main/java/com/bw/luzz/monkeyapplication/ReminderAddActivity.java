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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bw.luzz.monkeyapplication.View.ArcProgressStackView;
import com.bw.luzz.monkeyapplication.View.Dialog;
import com.cleveroad.audiowidget.AudioWidget;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;


public class ReminderAddActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    private Toolbar mToolbar;
    private EditText mTitleText;
    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText;
    private FloatingActionButton mFAB;
    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private String mTitle;
    private String mTime;
    private String mDate;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    // Values for orientation change
    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        // Initialize Views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleText = (EditText) findViewById(R.id.reminder_title);
        mDateText = (TextView) findViewById(R.id.set_date);
        mTimeText = (TextView) findViewById(R.id.set_time);
        mRepeatText = (TextView) findViewById(R.id.set_repeat);
        mRepeatNoText = (TextView) findViewById(R.id.set_repeat_no);
        mRepeatTypeText = (TextView) findViewById(R.id.set_repeat_type);
        mFAB = (FloatingActionButton) findViewById(R.id.starred1);

        // Setup Toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_add_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Initialize default values
        mActive = "true";
        mRepeat = "true";
        mRepeatNo = "我的脚本";
        mRepeatType = "Hour";

        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;
        createWindow();
        createDeskLayout();

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(deskLayout);


                /*String letter=mRepeatNo.trim().substring(0,1);

                int color=mColorGenerator.getRandomColor();
                TextDrawable textDrawable= TextDrawable.builder()
                        .buildRound(letter, color);
                Drawable d=getResources().getDrawable(R.mipmap.ic_launcher);
                AudioWidget audioWidget=new AudioWidget.Builder(getApplicationContext())
                                                        .defaultAlbumDrawable(textDrawable)
                                                        .playlistDrawable(d)
                                                        .build();
                audioWidget.show(100,100);*/
            }
        });
        // Setup Reminder Title EditText
        mTitleText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Setup TextViews using reminder values
        //mDateText.setText(mDate);
        mTimeText.setText(mTime);
        mRepeatNoText.setText(mRepeatNo);
        mRepeatTypeText.setText(mRepeatType);
        mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");

        // To save state on device rotation
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            /*String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
            mDate = savedDate;*/

            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(saveRepeat);
            mRepeat = saveRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            mRepeatNoText.setText(savedRepeatNo);
            mRepeatNo = savedRepeatNo;

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            mRepeatTypeText.setText(savedRepeatType);
            mRepeatType = savedRepeatType;

            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }
    }

    // To save state on device rotation
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mDateText.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatNoText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTypeText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);
    }

    // On clicking Time picker
    public void setTime(View v){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
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

    // Obtain time from time picker
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
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
        mDateText.setText(mDate);
    }





    public void onWhileAdd(View view) {
        Toast.makeText(getApplicationContext(),"添加while循环",Toast.LENGTH_SHORT).show();
    }

    // On clicking repeat type button
    public void selectRepeatType(View v){
        final String[] items = new String[5];

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

                mRepeatType = items[item];
                mRepeatTypeText.setText(mRepeatType);
                mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // On clicking repeat interval button
    public void setRepeatNo(View v){
      /*  AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setScriptBody("Enter Number");

        // Create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mScriptTitle = Integer.toString(1);
                            mRepeatNoText.setText(mScriptTitle);
                            mRepeatText.setText("Every " + mScriptTitle + " " + mRepeatType + "(s)");
                        }
                        else {
                            mScriptTitle = input.getText().toString().trim();
                            mRepeatNoText.setText(mScriptTitle);
                            mRepeatText.setText("Every " + mScriptTitle + " " + mRepeatType + "(s)");
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();*/

        final Dialog dialog=new Dialog(this,"输入脚本名称","默认脚本名为：我的脚本");
        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (input.getText().toString().length() == 0) {
                    mRepeatNo = "我的脚本";
                    mRepeatNoText.setText(mRepeatNo);
                    mRepeatText.setText("名称 " + mRepeatNo );
                }
                else {
                    mRepeatNo = input.getText().toString().trim();
                    mRepeatNoText.setText(mRepeatNo);
                    mRepeatText.setText("名称 " + mRepeatNo);
                }*/

                String input=dialog.getMessage();
                if (input.length() == 0) {
                    mRepeatNo = "我的脚本";
                    mRepeatNoText.setText(mRepeatNo);
                    mRepeatText.setText("名称 " + mRepeatNo );
                }
                else {
                    mRepeatNo = input.trim();
                    mRepeatNoText.setText(mRepeatNo);
                    mRepeatText.setText("名称 " + mRepeatNo);
                }
            }
        });
        dialog.show();
        if(mRepeatNo!=null){
            dialog.setMessage(mRepeatNo);
        }
    }

    // On clicking the save button
    public void saveReminder(){
        ReminderDatabase rb = new ReminderDatabase(this);

        // Creating Reminder
        int ID = rb.addReminder(new Reminder(mTitle, mDate, mTime, mRepeat, mRepeatNo, mRepeatType, mActive));

        // Set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        // Check repeat type
        if (mRepeatType.equals("Minute")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
        } else if (mRepeatType.equals("Hour")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
        } else if (mRepeatType.equals("Day")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
        } else if (mRepeatType.equals("Week")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
        } else if (mRepeatType.equals("Month")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
        }

        // Create a new notification
        if (mActive.equals("true")) {
            if (mRepeat.equals("true")) {
                //new AlarmReceiver().setRepeatAlarm(getApplicationContext(), mCalendar, ID, mRepeatTime);
            } else if (mRepeat.equals("false")) {
                //new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);
            }
        }

        // Create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "Saved",
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
            case R.id.save_reminder:
                mTitleText.setText(mTitle);

                if (mTitleText.getText().toString().length() == 0)
                    mTitleText.setError("脚本内容不能为空!");

                else {
                    saveReminder();
                }
                return true;

            // On clicking discard reminder button
            // Discard any changes
            case R.id.discard_reminder:
                Toast.makeText(getApplicationContext(), "还原修改",
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
                            Intent i=new Intent(getApplicationContext(),ReminderAddActivity.class);
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
        Intent home = new Intent(Intent.ACTION_MAIN);

        home.addCategory(Intent.CATEGORY_HOME);

        startActivity(home);
    }

    private void createWindow() {
        mWindowManager = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayout = new WindowManager.LayoutParams();

        mLayout.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 设置窗体焦点及触摸：
        // FLAG_NOT_FOCUSABLE(不能获得按键输入焦点)
        mLayout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
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