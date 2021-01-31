package com.think.runex.java.Pages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.think.runex.BuildConfig;
import com.think.runex.R;
import com.think.runex.feature.user.UserInfo;
import com.think.runex.java.App.App;
import com.think.runex.java.App.AppEntity;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.UpdateProfileImageResponse;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.UpdateProfileImageService;
import com.think.runex.java.Utils.Network.Services.UpdateProfileService;
import com.think.runex.java.Utils.Network.onNetworkCallback;
import com.think.runex.java.Utils.PermissionUtils;
import com.think.runex.java.Utils.RxBus;
import com.think.runex.java.event.UpdateProfileEvent;
import com.think.runex.ui.component.ProgressDialog;
import com.think.runex.ui.component.SelectImageSourceDialog;
import com.think.runex.ui.component.GenderDialog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


import static com.think.runex.config.ConstantsKt.DISPLAY_DATE_FORMAT_SHOT_MONTH;
import static com.think.runex.config.ConstantsKt.SERVER_DATE_TIME_FORMAT;


public class EditProfilePage extends xFragment
        implements DatePickerDialog.OnDateSetListener,
        GenderDialog.OnGenderSelectedListener, SelectImageSourceDialog.OnSelectImageSourceListener {

    private static final int RC_CAMERA_PERMISSION = 9001;
    private static final int RC_READ_EXTERNAL_PERMISSION = 9002;
    private static final int RC_TAKE_PICTURE = 9003;
    private static final int RC_PICK_IMAGE = 9004;

    private View rootView;
    private AppCompatImageButton blackButton;
    private TextView saveButton;

    private AppCompatImageView profileImage;
    private AppCompatImageButton changeProfileImageButton;
    private TextView emailLabel;
    private TextInputEditText firstNameInput;
    private TextInputEditText lastNameInput;
    private AppCompatTextView birthDateButton;
    private AppCompatTextView genderButton;

    private UserInfo userInfo;

    //Temp variable
    private String birthDate = "";
    //private Uri profileImageUri;

    private File tempProfileImageFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = App.instance(activity).getAppEntity().user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.page_edit_profile, container, false);
        setupComponents();
        updateUserInfo();
        isSaveProfileEnable();
        subscribeUi();
        return rootView;
    }

    private void setupComponents() {
        blackButton = rootView.findViewById(R.id.btn_cancel);
        saveButton = rootView.findViewById(R.id.lb_confirm);

        profileImage = rootView.findViewById(R.id.profile_image);
        changeProfileImageButton = rootView.findViewById(R.id.change_profile_image_button);
        emailLabel = rootView.findViewById(R.id.email_label);
        firstNameInput = rootView.findViewById(R.id.name_input);
        lastNameInput = rootView.findViewById(R.id.last_name_input);
        birthDateButton = rootView.findViewById(R.id.birth_date_input);
        genderButton = rootView.findViewById(R.id.gender_input);
    }

    private void updateUserInfo(){
        //Update data to views
        Glide.with(profileImage)
                .load(userInfo.getAvatar())
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(profileImage)
                .clearOnDetach();

        emailLabel.setText(userInfo.getEmail());
        firstNameInput.setText(userInfo.getFirstName());
        lastNameInput.setText(userInfo.getLastName());
        genderButton.setText(userInfo.getGender());

        birthDate = userInfo.getBirthDate();
        Calendar birthDateCalendar = convertServerDateTimeToCalendar(userInfo.getBirthDate());
        if (birthDateCalendar.get(Calendar.YEAR) < 1000) {
            birthDateButton.setText("");
        } else {
            birthDateButton.setText(userInfo.getBirthDateDisplay());
        }
    }

    private void subscribeUi() {
        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performUpdateProfile();
            }
        });

        changeProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectImageSourceDialog().show(getChildFragmentManager(), "SelectImageSourceDialog");
            }
        });

        birthDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        genderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GenderDialog().show(getChildFragmentManager(), "GenderDialog");
            }
        });

        firstNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isSaveProfileEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isSaveProfileEnable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = convertServerDateTimeToCalendar(birthDate);
        if (calendar.get(Calendar.YEAR) < 1000) {
            calendar = Calendar.getInstance();
        }

        DatePickerDialog dialog = new DatePickerDialog(requireContext(), this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        StringBuilder builder = new StringBuilder();

        builder.append(year);
        builder.append("-");

        if ((month + 1) < 10) {
            builder.append("0");
        }
        builder.append(month + 1);
        builder.append("-");

        if (dayOfMonth < 10) {
            builder.append("0");
        }
        builder.append(dayOfMonth);
        builder.append("T00:00:00Z");

        birthDate = builder.toString();
        birthDateButton.setText(convertServerDateTimeToDisplayDate(birthDate));
        isSaveProfileEnable();
    }

    @Override
    public void onGenderSelected(@NotNull String gender) {
        genderButton.setText(gender);
        isSaveProfileEnable();
    }

    private Calendar convertServerDateTimeToCalendar(String dateTime) {
        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(dateTime)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault());
                calendar.setTime(Objects.requireNonNull(sdf.parse(dateTime)));
            } catch (Throwable error) {
                error.printStackTrace();
            }
        }
        return calendar;
    }

    private String convertServerDateTimeToDisplayDate(String dateTime) {
        String displayDateTime = dateTime;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault());
            Date date = null;
            date = sdf.parse(dateTime);
            if (date != null) {
                displayDateTime = new SimpleDateFormat(DISPLAY_DATE_FORMAT_SHOT_MONTH, Locale.getDefault()).format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return displayDateTime;
    }

    private void isSaveProfileEnable() {
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();

        if (!Objects.equals(userInfo.getFirstName(), firstName) ||
                !Objects.equals(userInfo.getLastName(), lastName) ||
                !userInfo.getBirthDate().equals(birthDate) ||
                !userInfo.getGender().equals(genderButton.getText().toString())) {
            saveButton.setEnabled(true);
            saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent));
        } else {
            saveButton.setEnabled(false);
            saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColorHint));
        }
    }


    private void performUpdateProfile() {
        ProgressDialog progressDialog = ProgressDialog.Companion.newInstance("Update Profile...", -1);
        getChildFragmentManager().beginTransaction()
                .add(progressDialog, "ProgressDialog")
                .commitNowAllowingStateLoss();

        //Update profile data
        userInfo.setFirstName(firstNameInput.getText().toString());
        userInfo.setLastName(lastNameInput.getText().toString());
        userInfo.setFullName(firstNameInput.getText().toString() + " " + lastNameInput.getText().toString());
        userInfo.setBirthDate(birthDate);
        userInfo.setGender(genderButton.getText().toString());

        new UpdateProfileService(requireActivity(), new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                progressDialog.dismissAllowingStateLoss();
                Toast.makeText(requireContext(), "Update profile success", Toast.LENGTH_SHORT).show();

                //Save profile
                AppEntity appEntity = App.instance(requireActivity()).getAppEntity();
                appEntity.user = userInfo;
                App.instance(requireActivity()).save(appEntity);
                updateUserInfo();

                isSaveProfileEnable();
                RxBus.publish(RxBus.SUBJECT, new UpdateProfileEvent(userInfo));
            }

            @Override
            public void onFailure(xResponse response) {
                progressDialog.dismissAllowingStateLoss();
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show();
            }
        }).doIt(userInfo);
    }

    private void performUpdateProfileImage() {
        if (tempProfileImageFile == null) {
            return;
        }

        ProgressDialog progressDialog = ProgressDialog.Companion.newInstance("Update Profile Image...", -1);
        getChildFragmentManager().beginTransaction()
                .add(progressDialog, "ProgressDialog")
                .commitNowAllowingStateLoss();

        new UpdateProfileImageService(requireActivity(), new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                progressDialog.dismissAllowingStateLoss();
                Toast.makeText(requireContext(), "Update profile success", Toast.LENGTH_SHORT).show();
                UpdateProfileImageResponse profileImageResponse = Globals.GSON.fromJson(response.jsonString, UpdateProfileImageResponse.class);

                userInfo.setAvatar(profileImageResponse.getData().getUrl());
                performUpdateProfile();
            }

            @Override
            public void onFailure(xResponse response) {
                progressDialog.dismissAllowingStateLoss();
                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show();
            }
        }).doIt(tempProfileImageFile);
    }

    @Override
    public void onSelectImageSource(int source) {
        if (source == SelectImageSourceDialog.SOURCE_CAMERA) {
            if (checkCameraPermission()) {
                openCamera();
            }
        } else if (source == SelectImageSourceDialog.SOURCE_GALLERY) {
            if (checkGalleryPermission()) {
                openGallery();
            }
        }
    }

    private boolean isGranted(String permissionName) {
        return PermissionUtils.newInstance(activity).checkPermission(permissionName);
    }

    private boolean checkCameraPermission() {
        if (!isGranted(Manifest.permission.CAMERA) ||
                !isGranted(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                !isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_CAMERA_PERMISSION);
            return false;
        }
        return true;
    }

    private boolean checkGalleryPermission() {
        if (!isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RC_READ_EXTERNAL_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_CAMERA_PERMISSION || requestCode == RC_READ_EXTERNAL_PERMISSION) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                if (requestCode == RC_CAMERA_PERMISSION) {
                    openCamera();
                } else if (requestCode == RC_READ_EXTERNAL_PERMISSION) {
                    openGallery();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void openCamera() {
        Intent intent = provideCameraIntent();
        if (intent != null) {
            startActivityForResult(intent, RC_TAKE_PICTURE);
        }
    }

    private void openGallery() {
        startActivityForResult(Intent.createChooser(provideGalleryIntent(), getString(R.string.select_image)), RC_PICK_IMAGE);
    }

    private Intent provideCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireContext().getPackageManager()) == null) {
            Toast.makeText(requireContext(), "This Application do not have Camera Application", Toast.LENGTH_SHORT).show();
            return null;
        }
        tempProfileImageFile = createTempPhotoFile();
        if (tempProfileImageFile == null) {
            Toast.makeText(requireContext(), "Cannot create image file", Toast.LENGTH_SHORT).show();
            return null;
        }

        Uri uri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", tempProfileImageFile);
        List<ResolveInfo> resolvedIntentActivities = requireContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        int grantPermission = Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        grantPermission |= Intent.FLAG_GRANT_READ_URI_PERMISSION;

        for (ResolveInfo resolveInfo : resolvedIntentActivities) {
            requireContext().grantUriPermission(resolveInfo.activityInfo.packageName, uri, grantPermission);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    private File createTempPhotoFile() {
        File tempFile = null;
        try {
            final String fileName = System.currentTimeMillis() + "";
            tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/RUNEX");
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            tempFile = new File(tempFile + "/" + fileName + ".jpg");
        } catch (Throwable error) {
            error.printStackTrace();
        }
        return tempFile;
    }

    @SuppressLint("IntentReset")
    private Intent provideGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpg", "image/jpeg", "image/png"});
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_TAKE_PICTURE) {
                performUpdateProfileImage();
            } else if (requestCode == RC_PICK_IMAGE) {
                String path = getPath(data.getData());
                tempProfileImageFile = new File(path);
                performUpdateProfileImage();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        String s = null;
        if (cursor.moveToFirst()) {
            s = cursor.getString(column_index);
        }
        cursor.close();
        return s;
    }
}