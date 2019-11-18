package com.think.runex.java.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.think.runex.R;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.RecorderObject;
import com.think.runex.java.Utils.DateTime.DateTimeUtils;

public class SharePage extends xFragment implements View.OnClickListener{
    /** Main variables */
    private final String ct = "SharePage->";

    // instance variables
    private RecorderObject recorderObject;

    // views
    private TextView lbDistance;
    private TextView lbDuration;
    private ImageView previewImage;
    //--> buttons
    private View btnShareToFacebook;
    private View btnSaveToDevice;

    // implements
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_share_to_facebook: shareToFacebook(); break;
            case R.id.btn_save_to_device: break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.page_share, container, false);

        // view matching
        viewMatching( v );

        // view event listener
        viewEventListener();

        // binding
        binding();

        return v;
    }

    /** Feature methods */
    private void shareToFacebook(){

    }

    // binding
    private void binding(){
        // labels
        lbDistance.setText(Globals.DCM_2.format(recorderObject.distanceKm) +" กม." );
        lbDuration.setText(DateTimeUtils.toTimeFormat( recorderObject.recordingTime));

        // image bitmap
        previewImage.setImageBitmap( recorderObject.mapPreviewImage );

    }

    // view event listener
    private void viewEventListener(){
        btnSaveToDevice.setOnClickListener( this );
        btnShareToFacebook.setOnClickListener( this );

    }

    /** View matching */
    private void viewMatching( View v ){
        lbDistance = v.findViewById(R.id.lb_distance);
        lbDuration = v.findViewById(R.id.lb_duration);
        previewImage = v.findViewById(R.id.preview_image);
        //--> buttons
        btnSaveToDevice = v.findViewById(R.id.btn_save_to_device);
        btnShareToFacebook = v.findViewById(R.id.btn_share_to_facebook);
    }

    /** Setter */
    public SharePage setRecorderObject( RecorderObject recorderObject ){
        this.recorderObject = recorderObject;
        return this;
    }
}
