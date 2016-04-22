package com.bw.luzz.monkeyapplication;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FloatWindowActivity extends AppCompatActivity {
    Button mStartButton;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayout;
    private long startTime;
    private int top;
    private DeskLayout deskLayout;
    private LocalBroadcastManager mLocalBroadcaseManager;
    private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"超时结束",Toast.LENGTH_SHORT).show();
        }
    };

    /*private String jsonData="While true \n" +
            "Delay 1000 \n"+
            "TracePrint:\"luzzz\" \n" +
            "End \n";*/
    private String jsonData="While true\n" +
            "Delay 1000 \n"+
            "If CmpColor(544,1044,-549846)\n" +
            "Then tap 933 688\n" +
            "ElseIf CmpColor(544,1044,-2165794)\n" +
            "Then tap 549 1044\n" +
            "ElseIf CmpColor(544,1044,-8103631)\n" +
            "Then tap 80 242\n" +
            "ElseIf CmpColor(544,1044,-114887)\n" +
            "Then tap 797 489\n" +
            "ElseIf CmpColor(544,1044,-1)\n" +
            "Then tap 741 1027\n" +
            "Break \n"+
            "End\n" +
            "End \n";
   // private String jsonData=TestJson.json4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_window);
        createWindwo();
        createDeskLayout();
        mStartButton=(Button)findViewById(R.id.start_btn);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow(deskLayout);

            }
        });
        mLocalBroadcaseManager = LocalBroadcastManager.getInstance(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalBroadcaseManager.registerReceiver(mBroadcastReceiver, new IntentFilter(MonkeyService.COMMAND_RESULT));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcaseManager.unregisterReceiver(mBroadcastReceiver);
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
                        }
                        Toast.makeText(getApplicationContext(), "" + endTime, Toast.LENGTH_SHORT).show();
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

    private void createWindwo() {
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

    public class DeskLayout extends LinearLayout{

        private Messenger bananaService;

        public DeskLayout(Context context) {
            super(context);
            setOrientation(LinearLayout.VERTICAL);
            this.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            View v= LayoutInflater.from(context).inflate(R.layout.float_window_layout,null);
            Button mStartButton=(Button)v.findViewById(R.id.exe_btn);
            Button mA=(Button)v.findViewById(R.id.jiao_a);
            Button mB=(Button)v.findViewById(R.id.jiao_b);
            mStartButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i=new Intent(getApplicationContext(),BananaService.class);
                    getApplicationContext().bindService(i, mServiceConnection, Context.BIND_AUTO_CREATE);
                }
            });
            mA.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent i=new Intent(MonkeyService.class.getName());
                    i.putExtra("json",jsonData);
                    mLocalBroadcaseManager.sendBroadcast(i);*/
                    Message message=Message.obtain(null,BananaService.RUN_SCRIPT);
                    Bundle data=new Bundle();
                    data.putString(BananaService.SCRIPT,jsonData);
                    message.setData(data);
                    try {
                        bananaService.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
            mB.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            this.addView(v);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            return super.onTouchEvent(event);
        }
         ServiceConnection mServiceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name,final IBinder service) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bananaService = new Messenger(service);
                        Message message=Message.obtain(null,BananaService.RUN_SCRIPT);
                        Bundle data=new Bundle();
                        data.putString(BananaService.SCRIPT,jsonData);
                        message.setData(data);
                        try {
                            bananaService.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                //Message message=new Message()

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Intent i=new Intent(getContext(),BananaService.class);
                bindService(i,mServiceConnection,Context.BIND_AUTO_CREATE);
            }
        };
    }

}
