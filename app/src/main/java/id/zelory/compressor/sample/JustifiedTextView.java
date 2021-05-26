package id.zelory.compressor.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint({"AppCompatCustomView"})
/* renamed from: id.zelory.compressor.sample.JustifiedTextView */
public class JustifiedTextView extends TextView {
    private int mLineY;
    private int mViewWidth;

    public JustifiedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        this.mViewWidth = getMeasuredWidth();
        String text = (String) getText();
        this.mLineY = 0;
        this.mLineY = (int) (((double) this.mLineY) + (((double) getTextSize()) * 1.5d));
        Layout layout = getLayout();
        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            String line = text.substring(lineStart, lineEnd);
            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, getPaint());
            if (!needScale(line) || i >= layout.getLineCount() - 1) {
                canvas.drawText(line, 0.0f, (float) this.mLineY, paint);
            } else {
                drawScaledText(canvas, lineStart, line, width);
            }
            this.mLineY += getLineHeight();
        }
    }

    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth) {
        float x = 0.0f;
        if (isFirstLineOfParagraph(lineStart, line)) {
            String blanks = "  ";
            canvas.drawText(blanks, 0.0f, (float) this.mLineY, getPaint());
            x = 0.0f + StaticLayout.getDesiredWidth(blanks, getPaint());
            line = line.substring(3);
        }
        float d = ((((float) this.mViewWidth) - lineWidth) / ((float) line.length())) - 1.0f;
        for (int i = 0; i < line.length(); i++) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, getPaint());
            canvas.drawText(c, x, (float) this.mLineY, getPaint());
            x += cw + d;
        }
    }

    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

    private boolean needScale(String line) {
        boolean z = false;
        if (line.length() == 0) {
            return false;
        }
        if (line.charAt(line.length() - 1) != 10) {
            z = true;
        }
        return z;
    }
}
