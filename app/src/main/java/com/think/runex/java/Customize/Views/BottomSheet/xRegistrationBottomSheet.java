package com.think.runex.java.Customize.Views.BottomSheet;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.think.runex.R;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Utils.DeviceUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Request.rqRegisterUser;
import com.think.runex.java.Utils.Network.Response.APIFormatResponse;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.RegisterUserService;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import java.net.HttpURLConnection;

public class xRegistrationBottomSheet implements View.OnClickListener {
    /**
     * Main variables
     */
    private final String ct = "xRegistrationBottomSheet->";

    // instance variables
    private Activity activity;
    private Fragment fragment;
    private BottomSheetBehavior bottomSheetBehavior;
    private BottomSheetCallback callback;

    // explicit variables
    private int requestCode = -1;
    private final int ATLEAST_STRING_LENGTH = 5;
    private int layoutId;
    private boolean ON_REGISTERING = false;

    // views
    private View btnRegister;
    private BottomSheetDialog dialogBottomSheet;
    private View bottomSheetView;
    //--> input fields
    private EditText ipEmail, ipPassword, ipConfirmPassword, ipFirstName, ipLastName;

    /**
     * implements
     */
    @Override
    public void onClick(View view) {
        // prepare usage variables
        final String mtn = ct +"onClick() ";

        // condition
        if( ON_REGISTERING ){
            // log
            L.i(mtn +"on register...");

            // exit from this process
            return;
        }

        switch (view.getId()) {
            case R.id.btn_close: dismiss(); break;
            case R.id.btn_register:

                if (!isEmailValid(string(ipEmail))) toast("อีเมลไม่ถูกต้อง");
                else if (!isPasswordValid(string(ipPassword))) toast("รหัสผ่านควรมีความยาวมากกว่า " + ATLEAST_STRING_LENGTH + " อักษร");
                else if (!confirmPassword(string(ipPassword), string(ipConfirmPassword))) toast("\"รหัสผ่าน\" ไม่ตรงกัน");
                else if (string(ipFirstName).isEmpty()) toast("โปรดระบุชื่อ");
                else if (string(ipLastName).isEmpty()) toast("โปรดระบุนามสกุล");
                else {
                    // update flag
                    ON_REGISTERING = true;

                    // prevent from close bottom sheet
                    dialogBottomSheet.setCancelable( false );

                    // call register api
                    apiRegister();

                }

                break;
        }

    }

    public xRegistrationBottomSheet(Fragment fragment, int layoutId) {
        super();
        this.layoutId = layoutId;
        this.fragment = fragment;

        initial();
    }

    public xRegistrationBottomSheet(Activity activity, int layoutId) {
        super();
        this.layoutId = layoutId;
        this.activity = activity;

        initial();
    }

    /**
     * Initial
     */
    private void initial() {
        if (activity != null) withActivity();
        else withFragment();

    }

    /** API methods */
    private void apiRegister(){
        // prepare usage variables
        final String mtn = ct +"apiRegister() ";
        rqRegisterUser rq = new rqRegisterUser();
        //--> update props
        rq.setEmail(string(ipEmail));
        rq.setPassword(string(ipPassword));
        rq.setFirstname(string(ipFirstName));
        rq.setLastname(string(ipLastName));
        rq.setPF(Globals.PLATFORM);

        L.i(mtn +"request: "+ Globals.GSON.toJson( rq ));

        // fire
        new RegisterUserService(activity, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                clearAll();

                if( response.responseCode == HttpURLConnection.HTTP_OK) {

                    // display log
                    L.i(mtn + "response: " + response.jsonString);

                    xTalk x = new xTalk();
                    x.requestCode = requestCode;
                    x.attachObject = rq;

                    // send callback
                    callback.xBottomSheetCallback(x);

                } else {
                    try{
                        // prepare usage variables
                        APIFormatResponse rsp = Globals.GSON.fromJson(response.jsonString, APIFormatResponse.class);

                        // display msg
                        toast( rsp.getMsg() );

                    }catch ( Exception e ){
                        L.e(mtn +"Err: "+ e.getMessage());

                    }
                }

            }

            @Override
            public void onFailure(xResponse response) {
                clearAll();

            }
        }).doIt( rq );
    }

    /**
     * Feature methods
     */
    private void clearAll(){
                // clear flag
        ON_REGISTERING = false;

        // update props
        dialogBottomSheet.setCancelable(true);

    }
    private void toast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    private String string(EditText edt) {
        return edt.getText().toString();
    }

    private boolean confirmPassword(String pass1, String pass2) {
        return pass1.matches(pass2);
    }

    private boolean isPasswordValid(String pass) {
        return pass != null && !pass.isEmpty() && pass.length() > ATLEAST_STRING_LENGTH;
    }

    private boolean stringLengthAtLeast(String str, int atLeastLength) {
        return str.length() > atLeastLength;
    }

    private boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);

    }

    private void bottomSheetProps() {
        // update props
        dialogBottomSheet.setContentView(bottomSheetView);
        //--> get view parent
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetBehavior.setPeekHeight(DeviceUtils.instance(activity).getDisplaySize().y - 200);
        //--> dialog listener
        dialogBottomSheet.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog d = (BottomSheetDialog) dialogInterface;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(R.id.design_bottom_sheet);
                if (bottomSheet == null)
                    return;
                bottomSheet.setBackground(null);
            }
        });

        bottomSheetView.findViewById(R.id.btn_register).setOnClickListener(this);
        bottomSheetView.findViewById(R.id.btn_close).setOnClickListener(this);

        ipEmail = bottomSheetView.findViewById(R.id.input_email);
        ipPassword = bottomSheetView.findViewById(R.id.input_password);
        ipConfirmPassword = bottomSheetView.findViewById(R.id.input_confirm_password);
        ipFirstName = bottomSheetView.findViewById(R.id.input_first_name);
        ipLastName = bottomSheetView.findViewById(R.id.input_last_name);

    }
    public void dismiss() {
        // clear all
        clearAll();

        // hide bottom sheet dialog
        dialogBottomSheet.dismiss();

    }
    public void show(int requestCode, BottomSheetCallback callback) {
        // update props
        this.requestCode = requestCode;
        this.callback = callback;

        // show dialog
        dialogBottomSheet.show();

    }

    private void withFragment() {
        // update props
        activity = fragment.getActivity();

        // prepare usage variables
        bottomSheetView = fragment.getLayoutInflater().inflate(layoutId, null);
        dialogBottomSheet = new BottomSheetDialog(fragment.getActivity());

        bottomSheetProps();

    }

    private void withActivity() {
        // prepare usage variables
        bottomSheetView = activity.getLayoutInflater().inflate(layoutId, null);
        dialogBottomSheet = new BottomSheetDialog(activity);

        bottomSheetProps();
    }

}
