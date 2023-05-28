package com.churchkit.churchkit.ui.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.churchkit.churchkit.R;

import java.io.IOException;
import java.io.InputStream;

public class DrawerCitacion {
    private Bitmap image;
    private String text;
    //private int textSize;

    private TextPaint textPaint;
    private final int width = 1024;
    private int canvasHeight;
    private Context context;
    private Canvas canvas;
    private int bgColor;
    private TextPaint appNamePaint;


    private int fgColor;

    private int textSize;
    private Typeface typeface;
    Bitmap bitCanvas;


    private int ratio = 1;

    public DrawerCitacion(Context context, String textToDraw) {
        this.context = context;
        this.text = textToDraw;
        this.textSize = 40;

        appNamePaint = new TextPaint();
        appNamePaint.setShadowLayer(1, 2, 4, Color.BLACK);
        textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.WHITE);
        typeface = ResourcesCompat.getFont(context, R.font.robotolight);
        textPaint.setTypeface(typeface);

        appNamePaint.setTypeface(Typeface.SANS_SERIF);

        canvasHeight = 1820;

    }

    public Bitmap draw() {
        bitCanvas = Bitmap.createBitmap(width, canvasHeight, Bitmap.Config.ARGB_8888);

        canvas = new Canvas(bitCanvas);
        //canvas.se

        textPaint.setTextSize(textSize);
        int maxHeight = 100;
        int maxHeightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, maxHeight, context.getResources().getDisplayMetrics()); // convert dp to px



        if (image != null ) {
            Rect imageRect = new Rect(0, 0, width, canvasHeight);
            canvas.drawBitmap(image, null, imageRect, null);
        } else {
            if (bgColor != Color.TRANSPARENT)
                canvas.drawColor(bgColor);
        }

        textPaint.setColor(fgColor);



        StaticLayout yourLayout = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, canvas.getWidth() - 60)
                //.setMaxLines(maxLines)
                .setEllipsize(TextUtils.TruncateAt.END)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setIncludePad(true)
                .build();


        canvas.translate((canvas.getWidth() / 2) - (yourLayout.getWidth() / 2), (canvas.getHeight() / 2) - ((yourLayout.getHeight() / 2)));
        yourLayout.draw(canvas);

        canvas.translate(((canvas.getWidth() / 2) - (yourLayout.getWidth() / 2)) * -1, ((canvas.getHeight() / 2) - ((yourLayout.getHeight() / 2))) * -1);

        ///////
        // Draw the application name in the left bottom of the canvas


        String appName = "Church Kit";

        appNamePaint.setColor(Color.WHITE);

        appNamePaint.setTextSize(width / 20);

        StaticLayout appNameLayout = new StaticLayout(appName, appNamePaint, 250,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

        int appNameWidth = appNameLayout.getWidth();
        int appNameHeight = appNameLayout.getHeight();

        int dx1 = canvas.getWidth() - appNameWidth;
        int dy1 = (canvas.getHeight()) - appNameHeight - 20;
        canvas.translate(dx1, dy1);

        int dy = (canvas.getHeight()) - appNameHeight - 20;

        canvas.drawLine((canvas.getHeight()) - appNameHeight, dy, (canvas.getHeight()), dy, textPaint);

        appNameLayout.draw(canvas);
        canvas.translate(-30, -40);

        context.getDrawable(R.drawable.android_logo_24).draw(canvas);

        return bitCanvas;
    }


    private Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream);
    }


    public Bitmap getCitationWithBgColor(Bitmap bitmap) {
        image = bitmap;
        return draw();
    }

    public Bitmap getCitationWithNewFgColor(int color) {
        this.fgColor = color;

        return draw();
    }

    public Bitmap getCitationWithDiffSizeText(int textSize) {
        //canvasHeight = width * ratio;
        this.textSize = textSize;
        return draw();
    }

    public Bitmap getCitationWithNewFontStyle(int idFont) {
        this.typeface = ResourcesCompat.getFont(context, idFont);
        textPaint.setTypeface(typeface);
        return draw();
    }

    public Bitmap getCitationWithBgColor(int color) {
        this.bgColor = color;
        image = null;
        return draw();
    }

    public Bitmap getCitationWithBgColor(Context context, Uri uri) throws IOException {

        image = getBitmapFromUri(context, uri);
        return draw();
    }
}