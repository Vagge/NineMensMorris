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

import com.example.ninemensmorris.R;

public class TouchView implements View.OnTouchListener, View.OnDragListener{

    private static final String DATA_TAG_RED = "2";
    private static final String DATA_TAG_BLUE = "1";
    private static final int EMPTY_SPACE = 0;
    private String TURN = "2";
    private ViewModel vm;
    public TouchView(ViewModel vm)
    {
        this.vm = vm;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        View.DragShadowBuilder builder = new MyDragShadowBuilder(v);
        ClipData clipData = insertData(v);
        v.startDrag(clipData, builder,null,0);
        return true;
    }

    @Override
    public boolean onDrag(View v, DragEvent dragEvent) {
        switch(dragEvent.getAction())
        {
            case DragEvent.ACTION_DRAG_STARTED:
                Log.d("TAG", "onDrag: drag started.");

                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d("TAG", "onDrag: drag entered.");
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                Log.d("TAG", "onDrag: current point: ( " + dragEvent.getX() + " , " + dragEvent.getY() + " )"  );
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                Log.d("TAG", "onDrag: exited.");
                return true;

            case DragEvent.ACTION_DROP:
                Log.d("TAG", dragEvent.getClipData().getItemAt(0).getText().toString());
                //if correct turn and valid placement
                if(dragEvent.getClipData().getItemAt(0).getText().toString().equals(TURN)&&
                        vm.isValid(Integer.parseInt(((ImageView)v).getContentDescription().toString()), EMPTY_SPACE, Integer.parseInt(dragEvent.getClipData().getItemAt(0).getText().toString())))
                {

                    actionDrop(dragEvent, v);
                    if(vm.remove(Integer.parseInt(((ImageView)v).getContentDescription().toString())))
                    {
                        actionRemove(dragEvent, v);
                    }
                    if(TURN.equals("2"))
                    {
                        TURN = "1";
                    }
                    else
                    {
                        TURN = "2";
                    }
                }
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d("TAG", "onDrag: ended.");
                return true;
            default:
                Log.e("TAG","Unknown action type received by OnStartDragListener.");
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
        v.setOnDragListener(null);
        v.setOnTouchListener(null);
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

    }
}
