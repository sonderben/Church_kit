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
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.churchkit.churchkit.R;

import java.io.ByteArrayOutputStream;
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
    private int color;
    //private int textSize;
    Typeface typeface ;//= Typeface.createFromAsset(getAssets(), "fonts/myfont.ttf"); // replace "myfont.ttf" with the name of your custom font file


    private int ratio=1;

    public DrawerCitacion(Context context,  String textToDraw)  {
        this.context = context;
        this.text = textToDraw;
        this.textSize = width/20;
        textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.WHITE);//font/robotolight.ttf
        typeface =  ResourcesCompat.getFont(context, R.font.robotolight);
        textPaint.setTypeface(typeface);

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
            if (color != Color.TRANSPARENT)
                canvas.drawColor(color);
        }



        StaticLayout yourLayout = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, canvas.getWidth())
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
        TextPaint appNamePaint = new TextPaint();
        appNamePaint.setColor(Color.RED);
        this.textSize = width/20;
        appNamePaint.setTextSize(textSize);

        StaticLayout appNameLayout = new StaticLayout(appName, appNamePaint, 250,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

        int appNameWidth = appNameLayout.getWidth();
        int appNameHeight = appNameLayout.getHeight();



        canvas.translate(canvas.getWidth()-appNameWidth, (canvas.getHeight() )-appNameHeight-20);
        appNameLayout.draw(canvas);


    }


    private Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream);
    }

    public void setProperties(int textSize){
        this.textSize = textSize;
    }


    public Bitmap getCitation(Bitmap bitmap){
        canvasHeight = width * ratio;
        Bitmap bit = Bitmap.createBitmap(width, canvasHeight, Bitmap.Config.ARGB_8888);
        image = bitmap;
        canvas = new Canvas(bit);
        draw(canvas);
        return bit;
    }

    public Bitmap getCitation(int color){
        canvasHeight = width * ratio;
        Bitmap bit = Bitmap.createBitmap(width, canvasHeight, Bitmap.Config.ARGB_8888);
        this.color = color;
        image = null;
        canvas = new Canvas(bit);
        draw(canvas);
        return bit;
    }

    public Bitmap getCitation(Context context,Uri uri) throws IOException {
        canvasHeight = width * ratio;
        Bitmap bit = Bitmap.createBitmap(width, canvasHeight, Bitmap.Config.ARGB_8888);
        image = getBitmapFromUri(context,uri);
        canvas = new Canvas(bit);
        draw(canvas);
        return bit;
    }
}