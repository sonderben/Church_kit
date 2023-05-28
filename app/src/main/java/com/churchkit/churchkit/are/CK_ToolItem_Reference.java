//package com.churchkit.churchkit.are;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.chinalwb.are.AREditText;
//import com.chinalwb.are.Util;
//import com.chinalwb.are.styles.IARE_Style;
//import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Abstract;
//import com.chinalwb.are.styles.toolitems.IARE_ToolItem_Updater;
//import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Link;
//import com.churchkit.churchkit.R;
//
//public class CK_ToolItem_Reference extends ARE_ToolItem_Abstract {
//    public CK_ToolItem_Reference(){}
//    @Override
//    public IARE_Style getStyle() {
//        if (this.mStyle == null) {
//            AREditText editText = this.getEditText();
//            //this.mStyle = new ARE_Style_Link(editText, (ImageView)this.mToolItemView);
//            this.mStyle = new CK_Style_Reference(editText,(ImageView)this.mToolItemView);
//        }
//
//        return this.mStyle;
//    }
//
//    @Override
//    public View getView(Context context) {
//        if (null == context) {
//            return this.mToolItemView;
//        } else {
//            if (this.mToolItemView == null) {
//                ImageView imageView = new ImageView(context);
//                int size = Util.getPixelByDp(context, 40);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
//                imageView.setLayoutParams(params);
//                imageView.setImageResource(R.drawable.reference_24);
//                imageView.bringToFront();
//                this.mToolItemView = imageView;
//            }
//
//            return this.mToolItemView;
//        }
//    }
//
//    @Override
//    public void onSelectionChanged(int i, int i1) {
//
//    }
//
//    @Override
//    public IARE_ToolItem_Updater getToolItemUpdater() {
//        return null;
//    }
//}
