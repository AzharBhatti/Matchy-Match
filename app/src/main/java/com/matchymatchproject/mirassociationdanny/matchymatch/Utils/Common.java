package com.matchymatchproject.mirassociationdanny.matchymatch.Utils;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

public class Common {
    public static View.OnTouchListener touchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    };
}
