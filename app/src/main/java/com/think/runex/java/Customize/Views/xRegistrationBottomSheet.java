package com.think.runex.java.Customize.Views;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.think.runex.R;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Utils.DeviceUtils;

public class xRegistrationBottomSheet implements View.OnClickListener{
    /**
     * Main variables
     */
    private final String ct = "xRegistrationBottomSheet->";

    // instance variables
    private Activity activity;
    private Fragment fragment;
    private BottomSheetBehavior bottomSheetBehavior;

    // explicit variables
    private final int ATLEAST_STRING_LENGTH = 5;
    private int layoutId;

    // views
    private BottomSheetDialog dialogBottomSheet;
    private View bottomSheetView;

    /** implements */
    @Override
    public void onClick(View view) {
        switch(view.getId()){

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

    /**
     * Feature methods
     */
    private boolean confirmPassword(String pass1, String pass2){
        return pass1.matches( pass2 );
    }
    private boolean isPasswordValid(String pass){
        return pass != null && !pass.isEmpty() && pass.length() > ATLEAST_STRING_LENGTH;
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

        bottomSheetView.findViewById(R.id.btn_register).setOnClickListener( this );
    }

    public void show() {
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
