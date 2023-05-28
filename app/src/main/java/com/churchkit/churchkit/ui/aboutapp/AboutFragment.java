package com.churchkit.churchkit.ui.aboutapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.churchkit.churchkit.R;


public class AboutFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public AboutFragment() {
        // Required empty public constructor
    }


    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        TextView textView = view.findViewById(R.id.about);
        textView.setText(Html.fromHtml(txt));
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        return  view;
    }
    /*
     1. <b>How do I download the Church Kit app?</b></br>
  You can download the Church Kit app from your mobile device's app store.</br>
2. <b>Is Church Kit free?</b></br>Yes, Church Kit is free.</br>
3. <b>Can I access different versions of the Bible in Church Kit?</b></br>Yes, Church Kit allows access to different versions of the Bible.</br>
4. <b>Can I save my favorite verses in Church Kit?</b></br>Yes, Church Kit allows you to save favorite verses.</br>
5. <b>Can I perform advanced searches in Church Kit?</b></br>Yes, Church Kit allows for advanced searches in the Bible.</br>
6. <b>Can I read additional notes in Church Kit to better understand the biblical texts?</b> </br>Yes, Church Kit provides additional notes to help understand the biblical texts.</br>
7. <b>Can I access a wide variety of worship songs in Church Kit?</b></br>Yes, Church Kit provides access to a wide variety of worship songs.</br>
8. <b>Can I add my favorite songs to a personalized playlist in Church Kit?</b></br>Yes, Church Kit allows you to add favorite songs to a personalized playlist.</br>
9. <b>Can I find the chords and lyrics of songs in Church Kit?</b></br>Yes, Church Kit provides chords and lyrics of songs.</br>
10. <b>Can I use Church Kit to play worship songs in my church?</b></br>Yes, Church Kit provides chords and lyrics of songs to be played in the church.</br>
11. <b>What types of worship songs are available in Church Kit?</b></br>Church Kit provides a wide variety of worship songs, from classic hymns to modern songs.</br>
12. <b>Can I send feedback or suggestions to the Church Kit developers?</b></br>Yes, you can send feedback and suggestions through the Church Kit support page.</br>
13. <b>How can I report a problem or error in Church Kit?</b></br>You can report a problem or error through the Church Kit support page.</br>
14. <b>Can I use Church Kit offline?</b></br>Yes, you can use Church Kit offline if you have previously downloaded the necessary resources.</br>
15. <b>Can I change the Bible version in Church Kit?</b></br>Yes, you can change the Bible version in Church Kit.</br>
16. <b>Can I change the font size in Church Kit?</b></br>Yes, you can change the font size in Church Kit.</br>
17. <b>Can I change the app theme in Church Kit?</b></br>Yes, you can change the app theme in Church Kit.</br>
18. <b>Is Church Kit available in multiple languages?</b></br>Currently, Church Kit is only available in English.</br>
19. <b>Can I share a verse or song on social media from Church Kit?</b></br>Yes, you can share a verse or song on social media from Church Kit.</br>
20. <b>Can I add my own notes to verses in Church Kit?</b></br>Yes, you can add your own notes to verses in Church Kit</br>
     */

    String txt = "<P>Welcome to <b>Church Kit</b>, the perfect app to enhance your worship and connection with God. </br>Created by <a href=\"https://www.instagram.com/sonderben/\">Bennderson</a>  and <a href=\"https://johnyoute.com/\">John Ersen</a>, Church Kit is a practical and useful tool for those looking to deepen their Christian faith.</br> The app has three main sections: </br>\n" +
            "<p> <b>Song:</b> The songs section provides access to a wide variety of worship songs, from classic hymns to modern songs.</br></p>\n" +
            "<p><b>Bible: </b>The Bible section allows you to access different versions of the Bible, with all the features you need to study and analyze the Word of God. You can also save your favorite verses, perform advanced searches, and read additional notes to help you better understand the texts.</p>\n" +
            "\n" +
            "<p>Currently, Church Kit is available for free and without advertisements, but future versions will include advertising. This will allow us to continue developing the app and improving the user experience. <!--If you have any comments, suggestions, or issues with the app, please do not hesitate to contact us through our support page.--></p>\n" +
            "\n" +
            "In summary, Church Kit is the ideal app for anyone looking to get closer to God through the Bible and worship music. Download it today and start enjoying all its features and useful tools for your worship experience and spiritual growth.</p>";
}