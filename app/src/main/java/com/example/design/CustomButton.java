package com.example.design;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import com.example.merakirana.R;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;

public class CustomButton extends AppCompatButton {

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
//        applyDefaultFont(context);
        FontManager.getInstance().applyFont(this, attrs);
//        initAttrs(context, attrs);
    }

    private void applyDefaultFont(Context context) {
        Typeface typeface = FontManager.getInstance().getTypeface(context, context.getString(R.string.font_regular));
        if (typeface != null) {
            this.setTypeface(typeface);
        }
    }

    void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(
                    attrs, R.styleable.customTextView);

            Drawable drawableLeft = null;
            Drawable drawableRight = null;
            Drawable drawableBottom = null;
            Drawable drawableTop = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawableLeft = attributeArray.getDrawable(R.styleable.customTextView_drawableLeftCompat);
                drawableRight = attributeArray.getDrawable(R.styleable.customTextView_drawableRightCompat);
                drawableBottom = attributeArray.getDrawable(R.styleable.customTextView_drawableBottomCompat);
                drawableTop = attributeArray.getDrawable(R.styleable.customTextView_drawableTopCompat);
            } else {
                final int drawableLeftId = attributeArray.getResourceId(R.styleable.customTextView_drawableLeftCompat, -1);
                final int drawableRightId = attributeArray.getResourceId(R.styleable.customTextView_drawableRightCompat, -1);
                final int drawableBottomId = attributeArray.getResourceId(R.styleable.customTextView_drawableBottomCompat, -1);
                final int drawableTopId = attributeArray.getResourceId(R.styleable.customTextView_drawableTopCompat, -1);

                if (drawableLeftId != -1)
                    drawableLeft = AppCompatResources.getDrawable(context, drawableLeftId);
                if (drawableRightId != -1)
                    drawableRight = AppCompatResources.getDrawable(context, drawableRightId);
                if (drawableBottomId != -1)
                    drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId);
                if (drawableTopId != -1)
                    drawableTop = AppCompatResources.getDrawable(context, drawableTopId);
            }
            setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
            attributeArray.recycle();
        }
    }

}
