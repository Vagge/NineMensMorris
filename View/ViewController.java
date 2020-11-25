package com.example.ninemensmorris.View;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GestureDetectorCompat;

import com.example.ninemensmorris.Model.NineMenMorrisRules;
import com.example.ninemensmorris.R;
import com.example.ninemensmorris.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewController implements View.OnTouchListener, View.OnDragListener, GestureDetector.OnGestureListener, SensorEventListener {

    private static final String DATA_TAG_RED = "2";
    private static final String DATA_TAG_BLUE = "1";
    private static final int EMPTY_SPACE = 0;
    private static final String INFORMATION_RED_TURN = "Red turn";
    private static final String INFORMATION_BLUE_TURN = "Blue turn";
    private static final String INFORMATION_BLUE_WIN = "Blue win";
    private static final String INFORMATION_RED_WIN = "Red turn";
    private static final String REMOVE_CHECKER = "Remove opponents checker!";
    private boolean removable;
    private String TURN = "2";
    private final ViewModel vm;
    private TextView information;
    private TextView redMarkers;
    private TextView blueMarkers;
    private Activity activity;
    private ImageView mRedChecker;
    private ImageView mBlueChecker;
    private List<ImageView> board;
    private Button newGame;
    private GestureDetectorCompat mDetector;
    private ImageView boardImage;
    private SensorManager manager;
    private float aVal;
    private float shake;
    private float aLast;
    public ViewController(ViewModel vm, TextView information, Activity activity)
    {
        this.vm = vm;
        this.information = information;
        information.setText(INFORMATION_RED_TURN);
        redMarkers = activity.findViewById(R.id.red_markers_left);
        blueMarkers = activity.findViewById(R.id.blue_markers_left);
        newGame = activity.findViewById(R.id.start_new_game);
        boardImage = (ImageView)activity.findViewById(R.id.board);
        boardImage.setBackgroundColor(Color.parseColor(vm.getBackgroundColor()));
        removable = false;
        this.activity = activity;
        newGame.setOnClickListener(v -> newGame());
        mDetector = new GestureDetectorCompat(activity, this);
        manager = (SensorManager)activity.getSystemService(activity.SENSOR_SERVICE);
        manager.registerListener(shakeListener, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        shake = 0.00f;
        initGame();
    }

    private void initGame()
    {
        mRedChecker = activity.findViewById(R.id.red_checker);
        mBlueChecker = activity.findViewById(R.id.blue_checker);
        board = new ArrayList<>();
        for(int i = 1; i < 25; i++)
        {
            board.add(activity.findViewById(activity.getResources().getIdentifier("pos_"+i,"id", activity.getPackageName())));
            board.get(i-1).setOnDragListener(this);
            board.get(i-1).setOnTouchListener(this);
            board.get(i-1).setContentDescription(Integer.toString(i));
            if(vm.board(i-1) == NineMenMorrisRules.EMPTY_SPACE)
            {
                board.get(i-1).setImageResource(R.drawable.ic_action_name);
            }

        }
        mRedChecker.setOnTouchListener(this);
        mBlueChecker.setOnTouchListener(this);
        mRedChecker.setOnDragListener(this);
        mBlueChecker.setOnDragListener(this);
        mBlueChecker.setContentDescription("0");
        mRedChecker.setContentDescription("0");
        int blue = vm.getBluemarker()+1;
        int red = vm.getRedmarker()+1;
        blueMarkers.setText(Integer.toString(blue));
        redMarkers.setText(Integer.toString(red));
        vm.newGame();
    }

    private void newGame()
    {
        TURN = "2";
        information.setText(INFORMATION_RED_TURN);
        for(int i = 1; i < 25; i++)
        {
            board.get(i-1).setImageResource(R.drawable.ic_action_name);
            blueMarkers.setText("9");
            redMarkers.setText("9");
            vm.newGame();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }

        View.DragShadowBuilder builder = new MyDragShadowBuilder(v);
        ClipData clipData = insertData(v);
        if(removable)
        {
            int toBeRemoved;
            if(TURN.equals(DATA_TAG_BLUE))
            {
                toBeRemoved = NineMenMorrisRules.RED_MARKER;
            }
            else
            {
                toBeRemoved = NineMenMorrisRules.BLUE_MARKER;
            }
            if(vm.remove(Integer.parseInt(v.getContentDescription().toString()), toBeRemoved))
            {
                ((ImageView)v).setImageResource(R.drawable.ic_action_name);
                removable = false;
                if(TURN.equals("2"))
                {
                    TURN = "1";
                    information.setText(INFORMATION_BLUE_TURN);
                }
                else
                {
                    TURN = "2";
                    information.setText(INFORMATION_RED_TURN);
                }
                v.requestLayout();
                return true;
            }
            else
            {
                return true;
            }
        }
        v.startDrag(clipData, builder,null,0);
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent dragEvent)
    {
        switch(dragEvent.getAction())
        {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                return true;

            case DragEvent.ACTION_DROP:
                //if correct turn and valid placement

                int from = Integer.parseInt(dragEvent.getClipData().getItemAt(1).getText().toString());
                if(dragEvent.getClipData().getItemAt(0).getText().toString().equals(TURN)&&  vm.isValid(Integer.parseInt(v.getContentDescription().toString()), from, Integer.parseInt(dragEvent.getClipData().getItemAt(0).getText().toString())))
                {
                    actionDrop(dragEvent, v);
                    if(vm.remove(Integer.parseInt(v.getContentDescription().toString())))
                    {
                        if(actionRemove(dragEvent, v))//if win
                        {
                            newGame();
                        }
                        return true;
                    }
                    if(TURN.equals(DATA_TAG_RED))
                    {
                        TURN = DATA_TAG_BLUE;
                        information.setText(INFORMATION_BLUE_TURN);
                    }
                    else
                    {
                        TURN = DATA_TAG_RED;
                        information.setText(INFORMATION_RED_TURN);
                    }
                }
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                return true;
            default:
                break;
        }
        return false;
    }

    private void actionDrop(DragEvent dragEvent, View v)
    {

        if(dragEvent.getClipData().getItemAt(0).getText().toString().equals(DATA_TAG_RED))
        {
            ((ImageView)v).setImageResource(R.drawable.red);
            int result = 0;
            result = Integer.parseInt(redMarkers.getText().toString());
            if(result>0)
                result = result - 1;
            redMarkers.setText(Integer.toString(result));
        }
        else
        {
            ((ImageView)v).setImageResource(R.drawable.blue);
            int result = 0;
            result = Integer.parseInt(blueMarkers.getText().toString());
            if(result>0)
                result = result - 1;
            blueMarkers.setText(Integer.toString(result));
        }
        if(!dragEvent.getClipData().getItemAt(1).getText().toString().equals("0"))
        {
            ImageView view = (ImageView)activity.findViewById(activity.getResources().getIdentifier("pos_"+dragEvent.getClipData().getItemAt(1).getText().toString(),"id", activity.getPackageName()));
            view.setImageResource(R.drawable.ic_action_name);
        }
        v.getLayoutParams().height = 100;
        v.getLayoutParams().width = 100;
        v.requestLayout();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private static class MyDragShadowBuilder extends View.DragShadowBuilder
    {
        private static Drawable shadow;
        public MyDragShadowBuilder(View v)
        {
            super(v);
            shadow = new ColorDrawable(Color.LTGRAY);
        }
        @Override
        public void onProvideShadowMetrics (Point size, Point touch) {
            int width, height;
            height = getView().getHeight()/3;
            width = getView().getWidth()/3;
            shadow.setBounds(0, 0, width, height);
            size.set(width, height);
            touch.set(width / 2, height / 2);
        }
        @Override
        public void onDrawShadow(Canvas canvas)
        {
            shadow.draw(canvas);
        }
    }

    private ClipData insertData(View v)
    {
        ClipData clipData = null;
        if(v.getId() == R.id.red_checker)
        {
            ClipData.Item item = new ClipData.Item(DATA_TAG_RED);
            ClipData.Item from = new ClipData.Item(Integer.toString(EMPTY_SPACE));
            clipData = new ClipData(DATA_TAG_RED, new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
            clipData.addItem(from);
        }
        else if(v.getId() == R.id.blue_checker)
        {
            ClipData.Item item = new ClipData.Item(DATA_TAG_BLUE);
            ClipData.Item from = new ClipData.Item(Integer.toString(EMPTY_SPACE));
            clipData = new ClipData(DATA_TAG_BLUE, new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
            clipData.addItem(from);
        }
        else if(vm.board(Integer.parseInt(v.getContentDescription().toString()))==NineMenMorrisRules.RED_MARKER)
        {
            ClipData.Item item = new ClipData.Item(DATA_TAG_RED);
            ClipData.Item from = new ClipData.Item(v.getContentDescription());
            clipData = new ClipData(DATA_TAG_RED, new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
            clipData.addItem(from);
        }
        else if(vm.board(Integer.parseInt(v.getContentDescription().toString()))==NineMenMorrisRules.BLUE_MARKER)
        {
            ClipData.Item item = new ClipData.Item(DATA_TAG_BLUE);
            ClipData.Item from = new ClipData.Item(v.getContentDescription());
            clipData = new ClipData(DATA_TAG_BLUE, new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
            clipData.addItem(from);
        }
        return clipData;
    }

    private boolean actionRemove(DragEvent dragEvent, View v)
    {
        information.setText(REMOVE_CHECKER);
        removable = true;
        if(vm.win(NineMenMorrisRules.BLUE_MARKER))
        {
            information.setText(INFORMATION_RED_WIN);
            Toast toast = Toast.makeText(activity, INFORMATION_RED_WIN, Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
        if(vm.win(NineMenMorrisRules.RED_MARKER))
        {
            information.setText(INFORMATION_BLUE_WIN);
            Toast toast = Toast.makeText(activity, INFORMATION_BLUE_WIN, Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
        return false;
        //make where u take checkers from onlitsener null so u cant remove them
    }

    private SensorEventListener shakeListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event)
        {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            aLast = aVal;
            aVal = (float)Math.sqrt((double)(x*x+y*y+z*z));
            float delta = aVal - aLast;
            shake = shake*0.9f + delta;
            if(shake>12)
            {
                newGame();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //algorithm for sensorchanged inspired from stackoverflow
        }
    };
}