package com.chiclaim.customview.compat;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.chiclaim.customview.R;

/**
 * Description：
 * <br/>
 * Created by kumu on 2017/3/30.
 */

public class EditItemView extends LinearLayout {

    public static final int CLICK_LEFT = 1;
    public static final int CLICK_RIGHT = 2;
    public static final int CLICK_MIDDLE = 3;

    private int count = 6;
    private int unitStringRes;

    private EditText etCount;

    OnEditViewClick onEditViewClick;

    public EditItemView(Context context) {
        super(context);
        init(context, null);
    }

    public EditItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setOnEditViewClick(OnEditViewClick onEditViewClick) {
        this.onEditViewClick = onEditViewClick;
    }

    public void plus() {
        etCount.setText(String.valueOf(++count));
        etCount.setSelection(etCount.getText().length());
    }

    public void minus() {
        etCount.setText(String.valueOf(--count));
        etCount.setSelection(etCount.getText().length());
    }


    //ImageView  TextView ImageView

    public EditText getEditView() {
        return etCount;
    }

    private void init(Context context, AttributeSet attrs) {

        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);

        int leftBorderColor = -1, rightBorderColor = -1, middleTextColor = -1,
                leftImageRes = -1, rightImageRes = -1, middleTextSize = -1;

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditItemView);

            leftBorderColor = typedArray.getColor(R.styleable.EditItemView_left_border_color, -1);
            leftImageRes = typedArray.getResourceId(R.styleable.EditItemView_left_image, -1);

            rightBorderColor = typedArray.getColor(R.styleable.EditItemView_left_border_color, -1);
            rightImageRes = typedArray.getResourceId(R.styleable.EditItemView_right_image, -1);

            //String middleText = typedArray.getString(R.styleable.EditItemView_middle_text);
            middleTextColor = typedArray.getColor(R.styleable.EditItemView_middle_text_color, -1);
            middleTextSize = typedArray.getDimensionPixelSize(R.styleable.EditItemView_middle_text_size, -1);

            unitStringRes = typedArray.getResourceId(R.styleable.EditItemView_unit, -1);

            typedArray.recycle();
        }

        ImageView leftImage = new ImageView(getContext());
        leftImage.setImageResource(leftImageRes);
        addView(leftImage);
        leftImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditViewClick != null) {
                    onEditViewClick.onClick(CLICK_LEFT);
                }
            }
        });

        //数量
        etCount = new AppCompatEditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        lp.gravity = Gravity.CENTER_VERTICAL;
        etCount.setLayoutParams(lp);
        etCount.setPadding(15,0,0,0);

        etCount.setCursorVisible(true);
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(etCount, 0);
        } catch (Exception ignored) {
        }

        etCount.setIncludeFontPadding(false);
        etCount.setBackgroundResource(0);
        if (middleTextSize != -1)
            etCount.setTextSize(middleTextSize);
        if (middleTextColor != -1)
            etCount.setTextColor(middleTextColor);
        etCount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        etCount.setMaxLines(1);
        etCount.setSingleLine(true);
        etCount.setText(String.valueOf(count));
        //etCount.setSelection(etCount.getText().length());
        etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && TextUtils.isDigitsOnly(s)) {
                    try {
                        count = Integer.parseInt(s.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        addView(etCount);

        //单位
        if (unitStringRes != -1) {
            TextView tvUnit = new TextView(getContext());
            tvUnit.setText(unitStringRes);
            if (middleTextSize != -1)
                tvUnit.setTextSize(middleTextSize);
            if (middleTextColor != -1)
                tvUnit.setTextColor(middleTextColor);
            addView(tvUnit);

            tvUnit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEditViewClick != null) {
                        onEditViewClick.onClick(CLICK_MIDDLE);
                    }
                }
            });
        }

        ImageView rightImage = new ImageView(getContext());
        rightImage.setImageResource(rightImageRes);
        addView(rightImage);

        rightImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditViewClick != null) {
                    onEditViewClick.onClick(CLICK_RIGHT);
                }
            }
        });
    }

    interface OnEditViewClick {
        void onClick(int which);
    }

}
