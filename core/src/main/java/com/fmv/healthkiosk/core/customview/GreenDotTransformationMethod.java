package com.fmv.healthkiosk.core.customview;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class GreenDotTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(final CharSequence source, final View view) {
        return new PasswordCharSequence(source);
    }

    private static class PasswordCharSequence implements CharSequence {
        private final CharSequence mSource;

        PasswordCharSequence(CharSequence source) {
            mSource = source;
        }

        public char charAt(int index) {
            return '•'; // Use dot symbol
        }

        public int length() {
            return mSource.length();
        }

        public CharSequence subSequence(int start, int end) {
            return new PasswordCharSequence(mSource.subSequence(start, end));
        }
    }
}
