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

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bw.luzz.monkeyapplication.View.ArcProgressStackView;
import com.bw.luzz.monkeyapplication.View.Dialog;
import com.bw.luzz.monkeyapplication.View.DateTimePicker.date.DatePickerDialog;
import com.bw.luzz.monkeyapplication.View.DateTimePicker.time.RadialPickerLayout;
import com.bw.luzz.monkeyapplication.View.DateTimePicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReminderAddActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    private Toolbar mToolbar;
    private EditText mScriptText;
    private TextView mColorDesc, mTimeText, mRepeatText, mNameText, mHelpDesc;
    private FloatingActionButton mFAB;
    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private String mScript;
    private String mTime;
    private String mDate;
    private String mRepeat;
    private String mName;
    private String mHelpDes;
    private String mActive;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    // Values for orientation change
    private static final String KEY_SCRIPT = "script_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_SCRIPT_NAME = "repeat_no_key";
    private static final String KEY_HELP_DESC = "repeat_type_key";
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
        mScriptText = (EditText) findViewById(R.id.script);
        mColorDesc = (TextView) findViewById(R.id.color_desc);
        mTimeText = (TextView) findViewById(R.id.set_time);
        mRepeatText = (TextView) findViewById(R.id.set_repeat);
        mNameText = (TextView) findViewById(R.id.setname);
        mHelpDesc = (TextView) findViewById(R.id.helpdesc);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);

        // Setup Toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_add_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Initialize default values
        mActive = "true";
        mRepeat = "true";
        mName = "我的脚本";
        mHelpDes = "脚本相关api";

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


                /*String letter=mName.trim().substring(0,1);

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

        // Setup TextViews using reminder values
        //mColorDesc.setText(mDate);
        mTimeText.setText(mTime);
        mNameText.setText(mName);
        mHelpDesc.setText(mHelpDes);
        mRepeatText.setText("点击按钮添加循环");

        // To save state on device rotation
        if (savedInstanceState != null) {
            String script = savedInstanceState.getString(KEY_SCRIPT);
            mScriptText.setText(script);
            mScript = script;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            /*String savedDate = savedInstanceState.getString(KEY_DATE);
            mColorDesc.setText(savedDate);
            mDate = savedDate;*/

            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(saveRepeat);
            mRepeat = saveRepeat;

            String saveName = savedInstanceState.getString(KEY_SCRIPT_NAME);
            mNameText.setText(saveName);
            mName = saveName;

            String saveHelpDesc = savedInstanceState.getString(KEY_HELP_DESC);
            mHelpDesc.setText(saveHelpDesc);
            mHelpDes = saveHelpDesc;

            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }
    }

    // To save state on device rotation
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_SCRIPT, mScriptText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mColorDesc.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_SCRIPT_NAME, mNameText.getText());
        outState.putCharSequence(KEY_HELP_DESC, mHelpDesc.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);
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
        tpd.setTitle("选择延时");
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    // On clicking Date picker
    public void setColorPick(View v){
        /*Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");*/
        showWindow(deskLayout);
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


    public void onDelayAdd(View view){
        String timeStr=mTimeText.getText().toString();
        String script= mScriptText.getText().toString();

        String timeReg="(\\d\\d?):(\\d\\d?)";
        Matcher matcher=Pattern.compile(timeReg).matcher(timeStr);

        if(matcher.find()){
            timeStr=""+(Integer.valueOf(matcher.group(1))*3600+Integer.valueOf(matcher.group(2))*60);
        }else {
            throw new IllegalArgumentException("时间格式不正确");
        }
        mScriptText.setText(script+"\n"+"Delay "+timeStr);
    }



    public void onWhileAdd(View view) {
        //Toast.makeText(getApplicationContext(),"添加while循环",Toast.LENGTH_SHORT).show();
        String script= mScriptText.getText().toString();
        mScriptText.setText("While true \n"+script+"\n End \n");

    }

    // On clicking repeat type button
    public void onHelpClick(View v){
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

                mHelpDes = items[item];
                mHelpDesc.setText(mHelpDes);
                mRepeatText.setText("Every " + mName + " " + mHelpDes + "(s)");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // On clicking repeat interval button
    public void setScriptName(View v){
      /*  AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Number");

        // Create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mScript = Integer.toString(1);
                            mNameText.setText(mScript);
                            mRepeatText.setText("Every " + mScript + " " + mHelpDes + "(s)");
                        }
                        else {
                            mScript = input.getText().toString().trim();
                            mNameText.setText(mScript);
                            mRepeatText.setText("Every " + mScript + " " + mHelpDes + "(s)");
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

    // On clicking the save button
    public void saveReminder(){
        ReminderDatabase rb = new ReminderDatabase(this);
        Date date=new Date(System.currentTimeMillis());
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateandtime=dateFormat.format(date);
        // Creating Reminder
        int ID = rb.addReminder(new Reminder(mScript, mDate, dateandtime, mRepeat, mName, mHelpDes, mActive));

        // Set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);


        // Create a new notification
        if (mActive.equals("true")) {
            if (mRepeat.equals("true")) {
                //new AlarmReceiver().setRepeatAlarm(getApplicationContext(), mCalendar, ID, mRepeatTime);
            } else if (mRepeat.equals("false")) {
                //new AlarmReceiver().setAlarm(getApplicationContext(), mCalendar, ID);
            }
        }

        // Create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "保存",
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
                    mScriptText.setError("脚本内容不能为空!");

                else {
                    saveReminder();
                }
                return true;

            // On clicking discard reminder button
            // Discard any changes
            case R.id.discard_script:
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
        //finish();
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