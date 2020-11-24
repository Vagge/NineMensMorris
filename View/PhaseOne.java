package com.example.ninemensmorris.View;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ninemensmorris.Model.NineMenMorrisRules;
import com.example.ninemensmorris.R;

public class PhaseOne implements View.OnTouchListener, View.OnDragListener{

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
    private String PHASE = "1";
    private String from;
    private Activity activity;
    public PhaseOne(ViewModel vm, TextView information, Activity activity)
    {
        this.vm = vm;
        this.information = information;
        information.setText(INFORMATION_RED_TURN);
        removable = false;
        this.activity = activity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
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
                        actionRemove(dragEvent, v);
                        return true;
                    }
                    if(vm.win(NineMenMorrisRules.BLUE_MARKER))
                    {
                        information.setText(INFORMATION_RED_WIN);
                        return true;
                    }
                    if(vm.win(NineMenMorrisRules.RED_MARKER))
                    {
                        information.setText(INFORMATION_RED_WIN);
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
        }
        else
        {
            ((ImageView)v).setImageResource(R.drawable.blue);
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

    private void actionRemove(DragEvent dragEvent, View v)
    {
        information.setText(REMOVE_CHECKER);
        removable = true;
        //make where u take checkers from onlitsener null so u cant remove them
    }
}
