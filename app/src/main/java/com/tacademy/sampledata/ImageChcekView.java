package com.tacademy.sampledata;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Jinju on 2016-08-13.
 */
public class ImageChcekView extends FrameLayout implements Checkable {
    public ImageChcekView(Context context) {
        this(context, null);
    }

    public ImageChcekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView checkView;

    private void init() {
        inflate(getContext(), R.layout.view_check, this);
        checkView = (ImageView)findViewById(R.id.image_check);
    }

    boolean isChecked = false;

    @Override
    public void setChecked(boolean checked) {
        if (isChecked != checked) {
            isChecked = checked;
            drawCheck();
        }
    }

    private void drawCheck() {
        if (isChecked)
            checkView.setImageResource(android.R.drawable.checkbox_on_background);
        else checkView.setImageResource(android.R.drawable.checkbox_off_background);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
