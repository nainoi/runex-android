package com.think.runex.java.Pages;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.think.runex.R;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.RealmRecorderObject;
import com.think.runex.java.Utils.DateTime.DateTimeUtils;
import com.think.runex.java.Utils.DeviceUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.PermissionUtils;

import java.io.File;

public class SharePage extends xFragment implements View.OnClickListener {
    /**
     * Main variables
     */
    private final String ct = "SharePage->";

    // instance variables
    private RealmRecorderObject recorderObject;
    private CallbackManager callbackManager;
    private Bitmap previewMapImage = null;
    private Uri customImageUri = null;
    // explicit variables
    private final int SHARE_TO_FACEBOOK = 1001;
    private final int SAVE_TO_DEVICE = 1002;
    private final int ADD_PHOTO = 1003;
    private int lastAction = 0;

    // views
    private TextView lbDistance;
    private TextView lbDuration;
    private TextView lbDateTime;
    private ImageView previewImage;
    //--> buttons
    private View btnShareToFacebook;
    private View btnSaveToDevice;
    private ImageButton addPhotoButton;

    // implements
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_share_to_facebook:
                lastAction = SHARE_TO_FACEBOOK;
                shareToFacebook();
                break;
            case R.id.btn_save_to_device:
                lastAction = SAVE_TO_DEVICE;
                saveToDevice();
                break;
            case R.id.btn_add_image:
                if (customImageUri == null) {
                    intentToGallery();
                } else {
                    customImageUri = null;
                    addPhotoButton.setImageResource(R.drawable.ic_add_image);
                    updateImage();
                }
                break;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // exit from this page
        if (recorderObject == null) {
            // exit from this page
            getFragmentManager().beginTransaction()
                    .remove(this)
                    .commit();

            // exit from this process
            return;
        }

        // binding
        binding();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.page_share, container, false);

        // view matching
        viewMatching(v);

        // view event listener
        viewEventListener();

        return v;
    }

    /**
     * Feature methods
     */
    private boolean isGranted(String permissionName) {
        return PermissionUtils.newInstance(activity).checkPermission(permissionName);
    }

    private boolean requestPermissions() {
        if (!isGranted(Globals.READ_EXTERNAL_STORAGE) || !isGranted(Globals.WRITE_EXTERNAL_STORAGE)) {
            // request
            PermissionUtils.newInstance(this).requestPermissions(Globals.RC_PERMISSION
                    , new String[]{Globals.READ_EXTERNAL_STORAGE, Globals.WRITE_EXTERNAL_STORAGE});

            // exit from this process
            return false;
        }

        return true;
    }

    private void exit() {
        getFragmentManager().beginTransaction()
                .remove(this)
                .commit();
    }

    private void saveToDevice() {
        if (!requestPermissions()) return;

        // snap and save as file
        DeviceUtils.instance(activity).takeScreenshot2(activity, R.id.frame_full_view_display);

        // toast
        Toast.makeText(activity, "บันทึกสำเร็จ", Toast.LENGTH_SHORT).show();

        // exit from this page
        exit();

    }

    private void shareToFacebook() {
        if (!requestPermissions()) return;

        // prepare usage variables
        ShareDialog shareDialog = new ShareDialog(this);
        File file = DeviceUtils.instance(activity).takeScreenshot2(activity, R.id.frame_full_view_display);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        // update props
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                // toast
                Toast.makeText(activity, "สำเร็จ", Toast.LENGTH_SHORT).show();

                // exit from this page
                exit();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(activity, "เกิดข้อผิดพลาดขณะโพสท์", Toast.LENGTH_SHORT).show();

            }
        });
        // share to facebook
        shareDialog.show(content);

    }

    @SuppressLint("IntentReset")
    private void intentToGallery() {
        if (!requestPermissions()) return;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpg", "image/jpeg", "image/png"});
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "เลือกรูปภาพ"), ADD_PHOTO);
    }

    // binding
    private void binding() {
        final String mtn = ct + "binding() ";
        // labels
        lbDistance.setText(Globals.DCM_2.format(recorderObject.distanceKm) + " กม.");
        lbDuration.setText(DateTimeUtils.toTimeFormat(recorderObject.durationMillis));
        lbDateTime.setText(recorderObject.getWorkoutDate());

        L.i(mtn + "recorder-object: " + Globals.GSON.toJson(recorderObject));

        // image bitmap
        if (previewMapImage != null) {
            previewImage.setImageBitmap(previewMapImage);
        }
    }

    // view event listener
    private void viewEventListener() {
        btnSaveToDevice.setOnClickListener(this);
        btnShareToFacebook.setOnClickListener(this);
        addPhotoButton.setOnClickListener(this);
    }

    /**
     * View matching
     */
    private void viewMatching(View v) {
        lbDistance = v.findViewById(R.id.lb_distance);
        lbDuration = v.findViewById(R.id.lb_duration);
        lbDateTime = v.findViewById(R.id.lb_date_time);
        previewImage = v.findViewById(R.id.preview_image);
        //customPreviewImage = v.findViewById(R.id.custom_preview_image);
        //--> buttons
        btnSaveToDevice = v.findViewById(R.id.btn_save_to_device);
        btnShareToFacebook = v.findViewById(R.id.btn_share_to_facebook);
        addPhotoButton = v.findViewById(R.id.btn_add_image);
    }

    /**
     * Setter
     */
    public SharePage setRecorderObject(RealmRecorderObject recorderObject) {
        this.recorderObject = recorderObject;
        return this;
    }

    public SharePage setPreviewMapImage(Bitmap previewMapImage) {
        this.previewMapImage = previewMapImage;
        return this;
    }

    // Life cycle */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init facebook sdk
        FacebookSdk.sdkInitialize(activity);
        callbackManager = CallbackManager.Factory.create();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allGranted = true;

        for (int a = 0; a < grantResults.length; a++) {
            L.i("AAA: " + grantResults[a]);

            if (grantResults[a] != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;

                break;
            }
        }

        if (allGranted) if (lastAction == SHARE_TO_FACEBOOK) shareToFacebook();
        else saveToDevice();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_PHOTO) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                customImageUri = data.getData();
                updateImage();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateImage() {
        if (customImageUri != null) {
            addPhotoButton.setImageResource(R.drawable.ic_remove_image);
            previewImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(previewImage)
                    .load(customImageUri)
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .centerCrop()
                    .into(previewImage)
                    .clearOnDetach();
        } else {
            addPhotoButton.setImageResource(R.drawable.ic_add_image);
            // image bitmap
            if (previewMapImage != null) {
                previewImage.setScaleType(ImageView.ScaleType.FIT_XY);
                previewImage.setImageBitmap(previewMapImage);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (previewMapImage != null) {
            previewMapImage.recycle();
            previewMapImage = null;
        }
        super.onDestroy();
    }
}
