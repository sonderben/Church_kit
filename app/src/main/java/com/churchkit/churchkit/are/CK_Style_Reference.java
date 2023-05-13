package com.churchkit.churchkit.are;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.spans.AreUrlSpan;
import com.chinalwb.are.styles.ARE_ABS_FreeStyle;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;
import com.chinalwb.are.styles.toolitems.styles.ARE_Style_Link;
import com.churchkit.churchkit.R;

public class CK_Style_Reference extends ARE_ABS_FreeStyle {
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private ImageView mLinkImageView;
    private AREditText mEditText;

    public CK_Style_Reference(AREditText editText, ImageView imageView) {
        super(editText.getContext());
        this.mEditText = editText;
        this.mLinkImageView = imageView;
        this.setListenerForImageView(imageView);
    }



    @Override
    public void setListenerForImageView(ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CK_Style_Reference.this.openLinkDialog();
            }
        });
    }

    @Override
    public void applyStyle(Editable editable, int i, int i1) {

    }

    @Override
    public ImageView getImageView() {
        return null;
    }

    @Override
    public void setChecked(boolean b) {

    }

    private void openLinkDialog() {
        Activity activity = (Activity)this.mLinkImageView.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Reference");
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        final View areInsertLinkView = layoutInflater.inflate(R.layout.add_reference_to_note, (ViewGroup)null);
        builder.setView(areInsertLinkView).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText editText = (EditText)areInsertLinkView.findViewById(R.id.reference);
                String url = editText.getText().toString().trim();
                if (TextUtils.isEmpty(url)) {
                    dialog.dismiss();
                } else {
                    CK_Style_Reference.this.insertLink(url);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void insertLink(String reference) {
        if (!TextUtils.isEmpty(reference)) {
           /* if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }*/

            if (null != this.mEditText) {
                Editable editable = this.mEditText.getEditableText();
                int start = this.mEditText.getSelectionStart();
                int end = this.mEditText.getSelectionEnd();
                if (start == end) {
                    editable.insert(start, reference);
                    end = start + reference.length();
                }else {
                    editable.replace(start,end,reference);
                }

                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        // Define what happens when the text is clicked here
                        Toast.makeText(mLinkImageView.getContext(), "Ref clicked!", Toast.LENGTH_SHORT).show();
                    }
                };


                editable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //editable.setSpan(new AreUrlSpan(reference), start, end, 33);
            }

        }
    }
}
