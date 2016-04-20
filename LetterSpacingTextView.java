package com.goldenfrog.vyprvpn.app.frontend.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.goldenfrog.vyprvpn.app.R;

/**
 * Created by clayt on 6/3/15.
 */
public class LetterSpacingTextView extends TextView {

    private int mLetterSpacingPx;

    public LetterSpacingTextView(Context context) {
        super(context);
        init(null);
    }

    public LetterSpacingTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(attrs);
    }

    public LetterSpacingTextView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(attrs);
    }

    public void setLetterSpacing(int letterSpacing)
    {
        this.mLetterSpacingPx = letterSpacing;
        applyLetterSpacing(getText());
        invalidate();
        requestLayout();
    }
    public float getLetterSpacing()
    {
        return mLetterSpacingPx;
    }

    private void init(AttributeSet attrs)
    {
        if (attrs == null)
            return;

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.LetterSpacingTextView, 0, 0);

        try {
            mLetterSpacingPx = a.getDimensionPixelSize(R.styleable.LetterSpacingTextView_letterSpacing, 0);
        } finally {
            a.recycle();
        }

        applyLetterSpacing(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        applyLetterSpacing(text);
    }

    private void applyLetterSpacing(CharSequence text) {
        if (text.length() > 1 && mLetterSpacingPx > 0)
        {
            SpannableString finalText = new SpannableString(text);
            finalText.setSpan(new LetterSpacingSpan(mLetterSpacingPx), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            super.setText(finalText, BufferType.SPANNABLE);
        }
        else
        {
            super.setText(text, BufferType.NORMAL);
        }
    }

    /**
     * Create a Span with letterSpacing number of pixels between letters
     */
    private static class LetterSpacingSpan extends ReplacementSpan {
        private int mSpacingPx;

        public LetterSpacingSpan(int letterSpacing) {
            mSpacingPx = letterSpacing;
        }

        @Override
        public int getSize(Paint paint, CharSequence text,
                           int start, int end, Paint.FontMetricsInt fm) {
            return (int) (paint.measureText(text, start, end) + (mSpacingPx * (end - start - 1)));
        }

        @Override
        public void draw(Canvas canvas, CharSequence text,
                         int start, int end, float x, int top, int y,
                         int bottom, Paint paint) {
            float dx = x;
            for (int i = start; i < end; i++) {
                canvas.drawText(text, i, i + 1, dx, y, paint);
                dx += paint.measureText(text, i, i + 1) + mSpacingPx;
            }
        }
    }
}