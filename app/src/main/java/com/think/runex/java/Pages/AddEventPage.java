package com.think.runex.java.Pages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.exifinterface.media.ExifInterface;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Constants.xAction;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Utils.DeviceUtils;
import com.think.runex.java.Utils.KeyboardUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.AddEventActivityService;
import com.think.runex.java.Utils.Network.onNetworkCallback;
import com.think.runex.java.Utils.PermissionUtils;
import com.think.runex.java.Utils.UriUtils;
import com.think.runex.component.NumberTextWatcher;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.think.runex.java.Constants.Globals.RC_GALLERY_INTENT;
import static com.think.runex.config.ConstantsKt.DISPLAY_DATE_FORMAT;
import static com.think.runex.config.ConstantsKt.SERVER_DATE_TIME_FORMAT;

public class AddEventPage extends xFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    /**
     * Main variables
     */
    private final String ct = "AddEventPage->";

    // instance variables
    private PermissionUtils mPmUtils;


    // explicit variables
    private String mEventId = "";
    private String recordDate = "";
    private Uri activityImageUri = null;
    private Bitmap mBitmap = null;
    private NumberTextWatcher distantWatcher;
    private boolean ON_SUBMITTING = false;

    // views
    private SwipeRefreshLayout refreshLayout;
    private ImageView activityImage;
    private RelativeLayout btnAddImage;
    private ImageView iconAddImage;
    private TextView textAddImage;
    private TextInputEditText edtDistance;
    private TextView btnDate;
    private TextInputEditText edtNote;
    private AppCompatButton btnSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_add_event_activity, container, false);

        // view matching
        viewMatching(v);

        // view event listener
        viewEventListener();

        distantWatcher = new NumberTextWatcher(3, 2, edtDistance);
        edtDistance.addTextChangedListener(distantWatcher);

        //--> Permission utils
        mPmUtils = PermissionUtils.newInstance(activity);

        return v;
    }

    /**
     * Main variables
     */
    private File getImageFile() {
        // prepare usage variables
        final String destinationPath = activity.getExternalCacheDir() + "/" + System.currentTimeMillis() + ".jpg";

        // save file from bitmap
        return DeviceUtils.instance(activity).saveFileFromBitmap(mBitmap, destinationPath);
    }

    // view matching
    private void viewMatching(View v) {
        refreshLayout = v.findViewById(R.id.refresh_layout);
        refreshLayout.setEnabled(false);

        activityImage = v.findViewById(R.id.activity_image);
        btnAddImage = v.findViewById(R.id.btn_add_image);
        edtDistance = v.findViewById(R.id.edt_distance);
        btnDate = v.findViewById(R.id.btn_date);
        edtNote = v.findViewById(R.id.edt_note);
        btnSubmit = v.findViewById(R.id.btn_submit);
        iconAddImage = v.findViewById(R.id.ic_close);
        textAddImage = v.findViewById(R.id.tv_add_image);
    }

    // view event listener
    private void viewEventListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!ON_SUBMITTING) {
                    // gone progress dialog
                    refreshLayout.setRefreshing(false);

                    // exit from this process
                    return;
                }

            }
        });
        btnAddImage.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (ON_SUBMITTING) return;

        if (v.getId() == R.id.btn_add_image) {
            onClickAddImage();
        } else if (v.getId() == R.id.btn_date) {
            onClickDate();
        } else if (v.getId() == R.id.btn_submit) {
            // fire submitting api
            onClickSubmit();
        }
    }

    private void onClickAddImage() {
        KeyboardUtils.hideKeyboard(edtDistance);
        if (iconAddImage.getVisibility() == View.VISIBLE) {
            activityImageUri = null;
            loadActivityImage();

        } else {
            if (mPmUtils.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    mPmUtils.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                intentToGallery();
            } else {
                mPmUtils.requestPermissions(Globals.RC_PERMISSION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private void onClickDate() {
        KeyboardUtils.hideKeyboard(edtDistance);
        Calendar calendar = convertRecordDateToCalendar();
        DatePickerDialog dialog = new DatePickerDialog(activity, this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // prepare usage variables
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        // update props
        recordDate = "" + year + "-" + to2Digits((month + 1)) + "-" + to2Digits(dayOfMonth) + "T"
                + to2Digits(calendar.get(Calendar.HOUR_OF_DAY) + 7) + ":"
                + to2Digits(calendar.get(Calendar.MINUTE)) + ":"
                + to2Digits(calendar.get(Calendar.SECOND)) + "Z";

        // view binding
        btnDate.setText(toDisplayDate(recordDate, DISPLAY_DATE_FORMAT));
    }

    private void onClickSubmit() {
        if (isDataValid()) {

            // prevent from back pressed
            activity.preventFromBackPressed(true);

            // update flag
            ON_SUBMITTING = true;

            // display progress dialog
            refreshLayout.setRefreshing(true);

            String distance = (edtDistance.getText() != null) ? edtDistance.getText().toString() : "";
//            String imagePath = (activityImageUri != null) ? new UriUtils().getRealPath(activity, activityImageUri) : null;
            File imageFile = getImageFile();

            // fire
            new AddEventActivityService(activity, new onNetworkCallback() {
                @Override
                public void onSuccess(xResponse response) {
                    // prepare usage variables
                    final String mtn = ct + "onSuccess() ";

                    L.i(mtn + "json-string: " + response.jsonString);
                    Toast.makeText(activity, R.string.add_activity_success, Toast.LENGTH_SHORT).show();

                    // exit from this page
//                    getParentFragment().getChildFragmentManager()
//                            .beginTransaction()
//                            .remove(AddEventPage.this)
//                            .commit();

                    // clear flag
                    activity.preventFromBackPressed(false);
                    // back pressed
                    activity.onBackPressed();

                    // prepare usage variables
                    xTalk x = new xTalk();
                    x.resultCode = xAction.SUCCESS.ID;

                    // on result
                    onResult(x);
                }

                @Override
                public void onFailure(xResponse response) {
                    // prepare usage variables
                    final String mtn = ct + "onFailure() ";
                    L.i(mtn + "json-string: " + response.jsonString);

                    // gone progress dialog
                    refreshLayout.setRefreshing(false);

                    // clear flag
                    ON_SUBMITTING = false;

                    // clear flag
                    activity.preventFromBackPressed(false);
                }
            }).doIt(mEventId, distance, recordDate, (imageFile == null)
                    ? null
                    : imageFile.getPath(), "");
        }
    }

    private void intentToGallery() {
        startActivityForResult(
                Intent.createChooser(provideGalleryIntent(), getString(R.string.select_image)),
                RC_GALLERY_INTENT);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // prepare usage variables
        final String mtn = ct + "onActivityResult-> ";

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_GALLERY_INTENT && data != null && data.getData() != null) {
                activityImageUri = data.getData();

                try {
                    // prepare usage variables
                    final String path = new UriUtils().getRealPath(activity, activityImageUri);
                    final File imageFile = new File(path);
                    final Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
                    final ExifInterface ei = new ExifInterface(imageFile.getPath());
                    final int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                    Bitmap rotatedBitmap = null;

                    switch (orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(bitmap, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(bitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(bitmap, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = bitmap;
                    }

                    // update props
                    mBitmap = rotatedBitmap;

                    // view binding
                    activityImage.setImageBitmap(mBitmap);

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());
                }

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
        }
    }

    public AddEventPage setEventId(String eventId) {
        mEventId = eventId;
        return this;
    }

    private Calendar convertRecordDateToCalendar() {
        //String dateTime = btnDate.getText().toString();
        Calendar calendar = Calendar.getInstance();
        if (recordDate.length() > 0) {
            try {
                Date date = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault()).parse(recordDate);
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
            Date date = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault()).parse(dateStr);
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

    private boolean isDataValid() {
        if (mEventId.length() == 0) {
            Toast.makeText(activity, "No Event", Toast.LENGTH_SHORT).show();
            return false;
        }
        String distance = (edtDistance.getText() != null) ? edtDistance.getText().toString() : "";
        if (distance.length() == 0) {
            String distanceText = getString(R.string.distances);
            Toast.makeText(activity, getString(R.string.input_required, distanceText), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (recordDate == null || recordDate.length() == 0) {

            Toast.makeText(activity, getString(R.string.input_required, "Date"), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}

