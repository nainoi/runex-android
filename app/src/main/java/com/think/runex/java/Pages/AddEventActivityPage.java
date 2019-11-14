package com.think.runex.java.Pages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Activity.xActivity;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.PermissionUtils;
import com.think.runex.java.Utils.UriUtils;
import com.think.runex.ui.components.NumberTextWatcher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.think.runex.config.ConstantsKt.DISPLAY_DATE_FORMAT;
import static com.think.runex.java.Constants.Globals.RC_GALLERY_INTENT;

public class AddEventActivityPage extends xActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    /**
     * Main variables
     */
    private final String ct = "AddEventActivityPage->";
    private final String serverDateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    // instance variables
    private PermissionUtils mPmUtils;


    // explicit variables
    private String mEventId = "";
    private String recordDate = "";
    private Uri activityImageUri = null;
    private NumberTextWatcher distantWatcher;

    // views
    private ImageView activityImage;
    private RelativeLayout btnAddImage;
    private ImageView iconAddImage;
    private TextView textAddImage;
    private TextInputEditText edtDistance;
    private TextView btnDate;
    private TextInputEditText edtNote;
    private AppCompatButton btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_add_event_activity);

        ActivityUtils.newInstance(this).fullScreen();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            mEventId = b.getString("EVENT_ID");
        }

        // view matching
        viewMatching();

        // view event listener
        viewEventListener();

        distantWatcher = new NumberTextWatcher(3, 2, edtDistance);
        edtDistance.addTextChangedListener(distantWatcher);

        //--> Permission utils
        mPmUtils = PermissionUtils.newInstance(this);
    }

    // view matching
    private void viewMatching() {
        activityImage = findViewById(R.id.activity_image);
        btnAddImage = findViewById(R.id.btn_add_image);
        edtDistance = findViewById(R.id.edt_distance);
        btnDate = findViewById(R.id.btn_date);
        edtNote = findViewById(R.id.edt_note);
        btnSubmit = findViewById(R.id.btn_submit);
        iconAddImage = findViewById(R.id.ic_close);
        textAddImage = findViewById(R.id.tv_add_image);
    }

    // view event listener
    private void viewEventListener() {
        btnAddImage.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_image) {
            onClickAddImage();
        } else if (v.getId() == R.id.btn_date) {
            onClickDate();
        } else if (v.getId() == R.id.btn_submit) {
            onClickSubmit();
        }
    }

    private void onClickAddImage() {
        if (iconAddImage.getVisibility() == View.VISIBLE) {
            activityImageUri = null;
            loadActivityImage();
        } else {
            if (mPmUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    mPmUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                intentToGallery();
            } else {
                mPmUtils.requestPermissions(Globals.RC_PERMISSION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private void onClickDate() {
        Calendar calendar = convertDateTimeToCalendar();
        DatePickerDialog dialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        recordDate = "" + year + "-" + to2Digits((month + 1)) + "-" + to2Digits(dayOfMonth) + "T00:00:00Z";
        btnDate.setText(toDisplayDate(recordDate, DISPLAY_DATE_FORMAT));
    }

    private void onClickSubmit() {

    }

    private void intentToGallery() {
        startActivityForResult(
                Intent.createChooser(provideGalleryIntent(), getString(R.string.select_image)),
                RC_GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_GALLERY_INTENT && data != null && data.getData() != null) {
                activityImageUri = data.getData();
                loadActivityImage();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Globals.RC_PERMISSION) {
            if (isPermissionsGranted(permissions, grantResults)) {
                intentToGallery();
            }
        }
    }

    private boolean isPermissionsGranted(@NonNull String[] permissions, @NonNull int[] grantResults) {
        if ((permissions.length == 0 || grantResults.length == 0) && permissions.length != grantResults.length) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @SuppressLint("IntentReset")
    private Intent provideGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpg", "image/jpeg", "image/png", "image/gif"});
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    private void loadActivityImage() {
        if (activityImageUri == null) {
            iconAddImage.setVisibility(View.GONE);
            textAddImage.setText(R.string.add_image);
            activityImage.setImageDrawable(null);
        } else {
            iconAddImage.setVisibility(View.VISIBLE);
            textAddImage.setText(R.string.delete_image);
            Picasso.get().load(activityImageUri)
                    .into(activityImage);

            Log.e("Jozzee", "Path: " + activityImageUri.getPath());
            Log.e("Jozzee", "getAuthority: " + activityImageUri.getAuthority());
            Log.e("Jozzee", "Real Path: " + new UriUtils().getRealPath(this, activityImageUri));
        }
    }

    private Calendar convertDateTimeToCalendar() {
        //String dateTime = btnDate.getText().toString();
        Calendar calendar = Calendar.getInstance();
        if (recordDate.length() > 0) {
            try {
                Date date = new SimpleDateFormat(serverDateTimeFormat, Locale.getDefault()).parse(recordDate);
                calendar.setTime(date);
            } catch (Throwable error) {
                error.printStackTrace();
            }
        }
        return calendar;
    }

    private String to2Digits(int number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return String.valueOf(number);
        }
    }

    private String toDisplayDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.length() == 0) return dateStr;

        try {
            Date date = new SimpleDateFormat(serverDateTimeFormat, Locale.getDefault()).parse(dateStr);
            if (date != null) {
                return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
            } else {
                return dateStr;
            }
        } catch (Throwable error) {
            error.printStackTrace();
            return dateStr;
        }

    }


}

