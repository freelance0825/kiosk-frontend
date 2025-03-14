package com.fmv.healthkiosk.ui.auth.pin.widget;
import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.fmv.healthkiosk.R;

public class CustomPinView extends LinearLayout {
    private static final int PIN_LENGTH = 6;
    private EditText[] pinBoxes = new EditText[PIN_LENGTH];
    private int containerSpacing = dpToPx(16);

    public CustomPinView(Context context) {
        super(context);
        init(context);
    }

    public CustomPinView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomPinView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        LayoutInflater inflater = LayoutInflater.from(context);

        for (int i = 0; i < PIN_LENGTH; i++) {
            final int index = i;
            EditText editText = new EditText(context);

            // Atur layout params dengan tinggi match_parent
            LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
            if (i > 0) {
                params.setMargins(containerSpacing, 0, 0, 0); // Spasi antar kotak
            }
            editText.setLayoutParams(params);
            editText.setBackgroundResource(R.drawable.bg_rounded_child_card);
            editText.setTextColor(Color.WHITE);
            editText.setTextSize(24);
            editText.setGravity(android.view.Gravity.CENTER);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.setCursorVisible(false);
            editText.setTextAlignment(TEXT_ALIGNMENT_CENTER);

            // Saat input, langsung menjadi dotted (•)
            editText.setTransformationMethod(new android.text.method.PasswordTransformationMethod());

            editText.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (!TextUtils.isEmpty(editText.getText())) {
                            editText.setText(""); // Hapus input di kotak aktif
                        } else if (index > 0) {
                            pinBoxes[index - 1].setText(""); // Hapus input sebelumnya
                            pinBoxes[index - 1].requestFocus(); // Kembali ke kotak sebelumnya
                        }
                        return true;
                    }
                }
                return false;
            });

            editText.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 1 && index < PIN_LENGTH - 1) {
                        pinBoxes[index + 1].requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {
                    if (s.length() == 1) {
                        editText.removeTextChangedListener(this);
                        editText.setTransformationMethod(new PasswordTransformationMethod());
                        editText.addTextChangedListener(this);

                        if (isAllPinFilled()) {
                            hideKeyboard(editText);
                        }
                    }
                }
            });

            addView(editText);
            pinBoxes[i] = editText;
        }
    }

    private boolean isAllPinFilled() {
        for (EditText pinBox : pinBoxes) {
            if (TextUtils.isEmpty(pinBox.getText())) {
                return false;
            }
        }
        return true;
    }

    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public void setContainerSpacing(int dp) {
        this.containerSpacing = dpToPx(dp);
        requestLayout();
    }

    public String getPin() {
        StringBuilder pin = new StringBuilder();
        for (EditText pinBox : pinBoxes) {
            pin.append(pinBox.getText().toString());
        }
        return pin.toString();
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
