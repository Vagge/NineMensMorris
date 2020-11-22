package com.example.ninemensmorris.View;

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
    private static final String REMOVE_CHECKER = "Remove opponents checker!";
    private boolean removable;
    private String TURN = "2";
    private ViewModel vm;
    private TextView information;
    public PhaseOne(ViewModel vm, TextView information)
    {
        this.vm = vm;
        this.information = information;
        information.setText(INFORMATION_RED_TURN);
        removable = false;
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
    public boolean onDrag(View v, DragEvent dragEvent) {
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
                if(dragEvent.getClipData().getItemAt(0).getText().toString().equals(TURN)&&
                        vm.isValid(Integer.parseInt(((ImageView)v).getContentDescription().toString()), EMPTY_SPACE, Integer.parseInt(dragEvent.getClipData().getItemAt(0).getText().toString())))
                {

                    actionDrop(dragEvent, v);
                    if(vm.remove(Integer.parseInt(((ImageView)v).getContentDescription().toString())))
                    {
                        actionRemove(dragEvent, v);
                        return true;
                    }
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
        ClipData clipData;
        if(v.getId() == R.id.red_checker)
        {
            ClipData.Item item = new ClipData.Item(DATA_TAG_RED);
            clipData = new ClipData(DATA_TAG_RED, new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
        }
        else
        {
            ClipData.Item item = new ClipData.Item(DATA_TAG_BLUE);
            clipData = new ClipData(DATA_TAG_BLUE, new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
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
