package com.think.runex.java.Pages;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.think.runex.R;
import com.think.runex.feature.user.UserInfo;
import com.think.runex.java.App.App;
import com.think.runex.java.App.AppEntity;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.UpdateProfileService;
import com.think.runex.java.Utils.Network.onNetworkCallback;
import com.think.runex.java.Utils.RxBus;
import com.think.runex.java.event.RefreshEvent;
import com.think.runex.java.event.UpdateProfileEvent;
import com.think.runex.ui.component.ProgressDialog;
import com.think.runex.ui.profile.GenderDialog;
import com.think.runex.util.GlideApp;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.think.runex.util.ConstantsKt.DISPLAY_DATE_FORMAT_SHOT_MONTH;
import static com.think.runex.util.ConstantsKt.SERVER_DATE_TIME_FORMAT;


public class EditProfilePage extends xFragment
        implements DatePickerDialog.OnDateSetListener, GenderDialog.OnGenderSelectedListener {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = App.instance(activity).getAppEntity().user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.page_edit_profile, container, false);
        setupComponents();
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

        //Update data to views
        GlideApp.with(profileImage)
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
            birthDateButton.setText(userInfo.birthDate());
        }

        isSaveProfileEnable();
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
}