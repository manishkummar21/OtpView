package com.otpview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class OtpView extends AppCompatEditText {

    public static final String XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";


    private float mLineStrokeSelected = 2; //2dp by default

    float mSpace = 0; //24 dp by default
    float mCharSize = 0;
    float mNumChars = 6;
    float mLineSpacing = 8; //8dp by default
    private int mMaxLength = 6;
    private OnClickListener mClickListener;


    Paint fillPaint = new Paint();
    Paint strokePaint = new Paint();


    int[][] mStates = new int[][]{
            new int[]{android.R.attr.state_selected}, // selected
            new int[]{android.R.attr.state_focused}, // focused
            new int[]{-android.R.attr.state_focused}, // unfocused
    };

    int[] mColors = new int[]{
            Color.GREEN,
            Color.BLACK,
            Color.GRAY
    };

    ColorStateList mColorStates = new ColorStateList(mStates, mColors);


    public OtpView(Context context) {
        super(context);
    }

    public OtpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public OtpView(Context context, AttributeSet attrs,
                   int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OtpView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        //basic constraint
        setTextIsSelectable(false);
        setCursorVisible(false);

        float multi = context.getResources().getDisplayMetrics().density;

        mLineStrokeSelected = multi * mLineStrokeSelected;

        // fill
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.parseColor("#ffffff"));

        // stroke
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStrokeWidth(mLineStrokeSelected);


        setBackgroundResource(0);

        mLineSpacing = multi * mLineSpacing; //convert to pixels
        mMaxLength = attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", 6);
        mNumChars = mMaxLength;

        //Disable copy paste
        super.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        if (!isInEditMode()) {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorControlActivated, outValue, true);
            final int colorActivated = outValue.data;
            mColors[0] = colorActivated;

            context.getTheme().resolveAttribute(R.attr.colorPrimaryDark,
                    outValue, true);
            final int colorDark = outValue.data;
            mColors[1] = colorDark;

            context.getTheme().resolveAttribute(R.attr.colorControlHighlight,
                    outValue, true);
            final int colorHighlight = outValue.data;
            mColors[2] = colorHighlight;
        }


        // When tapped, move cursor to end of text.
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(getText().length());
                if (mClickListener != null) {
                    mClickListener.onClick(v);
                }
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int availableWidth = (int) (getWidth() * 0.80);

        mCharSize = availableWidth / mMaxLength;
        mCharSize = (float) (mCharSize * 0.60);
        mSpace = (float) (mCharSize * 0.30);

        int startX = (int) ((int) (getWidth() * 0.10) + (mSpace));
        int bottom = getHeight() - getPaddingBottom();

        //Text Width
        Editable text = getText();
        int textLength = text.length();
        float[] textWidths = new float[textLength];
        getPaint().getTextWidths(getText(), 0, textLength, textWidths);

        for (int i = 0; i < mNumChars; i++) {

            updateColorForLines(i == textLength);

            RectF rect = new RectF(startX, 0 + getPaddingTop(), startX + mCharSize + mSpace, bottom);
            canvas.drawRoundRect(rect, 5, 5, fillPaint);    // fill
            canvas.drawRoundRect(rect, 5, 5, strokePaint);  // stroke

            if (getText().length() > i) {
                float middle = startX + (mSpace + mCharSize) / 2;
                canvas.drawText(text, i, i + 1, middle - textWidths[0] / 2, (bottom + getTextSize() + 2 * mLineStrokeSelected) / 2, getPaint());
            }


            startX += mCharSize + (2 * mSpace);
        }


    }

    private void updateColorForLines(boolean next) {
        if (isFocused()) {
            strokePaint.setStrokeWidth(mLineStrokeSelected);
            strokePaint.setColor(getColorForState(android.R.attr.state_focused));
            if (next) {
                strokePaint.setColor(getColorForState(android.R.attr.state_selected));
            }
        } else {
            strokePaint.setStrokeWidth(mLineStrokeSelected);
            strokePaint.setColor(getColorForState(-android.R.attr.state_focused));
        }
    }

    private int getColorForState(int... states) {
        return mColorStates.getColorForState(states, Color.BLACK);
    }
}
