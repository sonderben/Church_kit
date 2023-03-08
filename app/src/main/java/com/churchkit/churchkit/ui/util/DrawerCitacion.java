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

import androidx.core.content.res.ResourcesCompat;

import com.churchkit.churchkit.R;

import java.io.IOException;
import java.io.InputStream;

public class DrawerCitacion {
    private Bitmap image;
    private String text;
    private int textSize;
    private TextPaint textPaint;
    private final int width = 1024;
    private int  canvasHeight;
    private Context context;
    private Canvas canvas;
    private int bgColor;
    private TextPaint appNamePaint;
    private Bitmap tempImage;
    private int tempBgColor;
    private int fgColor ;
    private int tempFgColor;
    //private int textSize;
    private Typeface typeface ;
    private Typeface tempTypeface ;


    private int ratio=1;

    public DrawerCitacion(Context context,  String textToDraw)  {
        this.context = context;
        this.text = textToDraw;
        this.textSize = width/20;
        appNamePaint = new TextPaint();
        appNamePaint.setShadowLayer(1,2,4,Color.BLACK);
        textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.WHITE);//font/robotolight.ttf
        typeface = Typeface.SANS_SERIF;  //ResourcesCompat.getFont(context, R.font.robotolight);
        textPaint.setTypeface(typeface);
        appNamePaint.setTypeface(typeface);

    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        //int width = 400; // width of the canvas
        int maxHeight = 100; // maximum height of the StaticLayout in dp
        int maxHeightPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, maxHeight, context.getResources().getDisplayMetrics()); // convert dp to px

        int maxLines = (int) Math.ceil(maxHeightPx / paint.getFontSpacing()); // calculate the maximum number of lines that can fit within the desired height



        ////////////////

        if ( image!=null ){
            Rect imageRect = new Rect(0, 0, width, canvasHeight);
            canvas.drawBitmap(image, null, imageRect, null);
        }else {
            if (bgColor != Color.TRANSPARENT)
                canvas.drawColor(bgColor);
        }

        textPaint.setColor(fgColor);
        //textPaint.setColor(Color.WHITE);


        StaticLayout yourLayout = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, canvas.getWidth()-60)
                //.setMaxLines(maxLines)
                .setEllipsize(TextUtils.TruncateAt.END)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setIncludePad(true)
                .build();



        canvas.translate((canvas.getWidth() / 2) - (yourLayout.getWidth() / 2), (canvas.getHeight() / 2) - ((yourLayout.getHeight() / 2)));
        yourLayout.draw(canvas);

        canvas.translate(((canvas.getWidth() / 2) - (yourLayout.getWidth() / 2))*-1, ((canvas.getHeight() / 2) - ((yourLayout.getHeight() / 2)))*-1  );

        ///////
        // Draw the application name in the left bottom of the canvas


        String appName = "Church Kit";

        appNamePaint.setColor(Color.WHITE);
        this.textSize = width/20;
        appNamePaint.setTextSize(textSize);

        StaticLayout appNameLayout = new StaticLayout(appName, appNamePaint, 250,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

        int appNameWidth = appNameLayout.getWidth();
        int appNameHeight = appNameLayout.getHeight();



        canvas.translate(canvas.getWidth()-appNameWidth, (canvas.getHeight() )-appNameHeight-20);

        int dy = (canvas.getHeight() )-appNameHeight-20;
        int dx = (canvas.getHeight() )-appNameHeight-20;
        canvas.drawLine((canvas.getHeight() )-appNameHeight,dy,(canvas.getHeight() ),dy,textPaint);

        appNameLayout.draw(canvas);


    }


    private Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream);
    }

    public void setProperties(int textSize){
        this.textSize = textSize;
    }


    public Bitmap getCitationWithBgColor(Bitmap bitmap){
        canvasHeight = width * ratio;
        Bitmap bit = Bitmap.createBitmap(width, canvasHeight, Bitmap.Config.ARGB_8888);

        this.bgColor = this.tempBgColor;
        this.typeface = tempTypeface;//
        image = bitmap;
        tempImage = bitmap;
        tempBgColor = 0;
        canvas = new Canvas(bit);
        draw(canvas);
        return bit;
    }
    public Bitmap getCitationWithNewFgColor(int color){
        canvasHeight = width * ratio;
        Bitmap bit =  Bitmap.createBitmap(width,canvasHeight,Bitmap.Config.ARGB_8888);
         this.bgColor = this.tempBgColor;
        this.image = this.tempImage;
        this.typeface = tempTypeface;
         this.fgColor = color;
         tempFgColor = color;
        canvas = new Canvas(bit);
        draw(canvas);
        return bit;
    }
    public Bitmap getCitationWithNewFontStyle(int idFont){
        canvasHeight = width * ratio;
        Bitmap bit =  Bitmap.createBitmap(width,canvasHeight,Bitmap.Config.ARGB_8888);
        this.bgColor = this.tempBgColor;
        this.image = this.tempImage;
        this.typeface =   ResourcesCompat.getFont(context, idFont);
        canvas = new Canvas(bit);
        draw(canvas);
        return bit;
    }

    public Bitmap getCitationWithBgColor(int color){
        canvasHeight = width * ratio;
        Bitmap bit = Bitmap.createBitmap(width, canvasHeight, Bitmap.Config.ARGB_8888);
        this.bgColor = color;
        image = null;
        tempImage = null;
        tempBgColor = color;
        this.typeface = tempTypeface;
        this.fgColor =/*Color.RED; */tempFgColor;
        canvas = new Canvas(bit);
        draw(canvas);
        return bit;
    }

    public Bitmap getCitationWithBgColor(Context context, Uri uri) throws IOException {
        canvasHeight = width * ratio;
        Bitmap bit = Bitmap.createBitmap(width, canvasHeight, Bitmap.Config.ARGB_8888);
        image = getBitmapFromUri(context,uri);
        tempImage = image;
        tempBgColor = 0;
        canvas = new Canvas(bit);
        draw(canvas);
        return bit;
    }
}