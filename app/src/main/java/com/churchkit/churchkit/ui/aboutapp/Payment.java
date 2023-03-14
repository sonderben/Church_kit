package com.churchkit.churchkit.ui.aboutapp;

import android.app.Activity;
import android.util.Log;

import com.churchkit.churchkit.R;
import com.razorpay.Checkout;

import org.json.JSONObject;

public class Payment {
    public static void startPayment(Activity activity){

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setImage(R.mipmap.ic_launcher);
        checkout.setKeyID("rzp_test_sn2ShyAZPpq2aQ");

        /*Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_eNwgA5oEnElHde");*/
        /**
         * Set your logo here
         */


        /**
         * Reference to current activity
         */
        ///final Activity activity = getActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Church kit");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#283593");//#283593
            options.put("currency", "USD");
            options.put("amount", "500");//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact","+8293048654");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }
}
