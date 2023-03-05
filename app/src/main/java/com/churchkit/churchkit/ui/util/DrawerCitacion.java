package com.churchkit.churchkit.ui.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DrawerCitacion {
    private Bitmap image;
    private String text;
    private int textSize;
    private TextPaint textPaint;
    private int  canvasHeight;
    private Context context;
    private Canvas canvas;
    //private int textSize;
    private int ratio=1;

    public DrawerCitacion(Context context,  String textToDraw)  {
        this.context = context;
        this.text = textToDraw;
        this.textSize = 20;
        textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.SANS_SERIF);

    }

    public void draw(Canvas canvas) {
        if ( image!=null ){
            Rect imageRect = new Rect(0, 0, 400, canvasHeight);
            canvas.drawBitmap(image, null, imageRect, null);
        }
        text = "genial cettte d";
        StaticLayout yourLayout = new StaticLayout(text, textPaint,
                canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f,
                true);
        canvas.translate((canvas.getWidth() / 2) - (yourLayout.getWidth() / 2), (canvas.getHeight() / 2) - ((yourLayout.getHeight() / 2)));
        yourLayout.draw(canvas);
    }


    private Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream);
    }

    public void setProperties(int textSize){
        this.textSize = textSize;
    }


    public Bitmap getCitation(Bitmap bitmap){
        canvasHeight = 400 * ratio;
        Bitmap bit = Bitmap.createBitmap(400, canvasHeight, Bitmap.Config.ARGB_8888);
        image = bitmap;
        canvas = new Canvas(bit);
        draw(canvas);
        return bit;
    }

    public Bitmap getCitation(Context context,Uri uri) throws IOException {
        canvasHeight = 400 * ratio;
        Bitmap bit = Bitmap.createBitmap(400, canvasHeight, Bitmap.Config.ARGB_8888);
        image = getBitmapFromUri(context,uri);
        canvas = new Canvas(bit);
        draw(canvas);
        return bit;
    }
}