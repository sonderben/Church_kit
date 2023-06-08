package com.churchkit.churchkit.ui.aboutapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.churchkit.churchkit.R;


public class FaqFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FaqFragment() {
        // Required empty public constructor
    }


    public static FaqFragment newInstance(String param1, String param2) {
        FaqFragment fragment = new FaqFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_faq, container, false);
        TextView textView = root.findViewById(R.id.faq_txtview);
        textView.setText(Html.fromHtml(faq));
        return root;
    }

    String faq = "<p>1. <b>How do I download the Church Kit app?</b><br />You can download the Church Kit app from your mobile device's app store.<br />" +
            "2. <b>Is Church Kit free?</b><br />Yes, Church Kit is free.<br />" +
            "3. <b>Can I access different versions of the Bible in Church Kit?</b><br />Yes, Church Kit allows access to different versions of the Bible.<br />" +
            "4. <b>Can I save my favorite verses in Church Kit?</b><br />No, but Church Kit allows you to save your favorite chapter and highlight a verse or portion of a verse.<br />" +
            "5. <b>Can I perform advanced searches in Church Kit?</b><br />Yes, Church Kit allows for advanced searches.<br />" +
            "6. <b>Can I read additional notes in Church Kit to better understand the biblical texts?</b><br />No, Church Kit does not provide additional notes to help understand the biblical texts.<br />" +
            "7. <b>Can I add my favorite songs to a personalized playlist in Church Kit?</b><br />Currently, Church Kit does not support this feature.<br />" +
            "8. <b>What types of worship songs are available in Church Kit?</b><br />Church Kit provides a wide variety of worship songs, from classic hymns to modern songs.<br />" +

            "9. <b>Can I change the app theme in Church Kit?</b><br />Currently, you can't.<br />" +
            "10. <b>Is Church Kit available in multiple languages?</b><br />Yes, Church Kit is available in 4 languages.<br />" +
            "11. <b>Can I share a verse or song on social media from Church Kit?</b><br />Yes, you can share a verse or song on social media from Church Kit.</p>";

}