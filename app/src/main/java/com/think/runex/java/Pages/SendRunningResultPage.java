package com.think.runex.java.Pages;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.think.runex.R;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.xFragment;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.Request.SubmitRunningResultService;
import com.think.runex.java.Utils.Network.Request.rqSubmitRunningResult;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendRunningResultPage extends xFragment
        implements onNetworkCallback
        , View.OnClickListener {
    /**
     * Main variables
     */
    private final String ct = "SendRunningResultPage->";

    // explicit variables
    private Calendar mCalendar = Calendar.getInstance();
    private long mSubmitTimestamp = System.currentTimeMillis();

    // views
    private View btnSubmit, btnCancel, btnSelectDate;
    private TextView inputDate;
    private EditText inputDist;

    /**
     * Implement methods
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                apiSubmitRunningResult();
                break;
            case R.id.btn_cancel:
                break;
            case R.id.frame_submit_date:
                displayDatePicker();
                break;
        }
    }

    @Override
    public void onSuccess(xResponse response) {
        // prepare usage variables
        final String mtn = ct + "onSuccess() ";
        L.i(mtn + "response: " + response.jsonString);
    }

    @Override
    public void onFailure(xResponse response) {
        // prepare usage variables
        final String mtn = ct + "onFailure() ";
        L.e(mtn + "response: " + response.jsonString);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init view
        binding();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_send_running_result, container, false);

        // view matching
        viewMatching(v);

        // view event listener
        viewEventListener();

        return v;
    }

    /**
     * Feature methods
     */
    private void displayDatePicker() {
        // prepare usage variables
        Calendar cal = mCalendar;

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //--> calendar props
                cal.set(Calendar.YEAR, i);
                cal.set(Calendar.MONTH, i1);
                cal.set(Calendar.DATE, i2);
                //--> timestamp
                mSubmitTimestamp = cal.getTimeInMillis();

                // binding views
                binding();
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

        //--> customize
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

        // display
        datePickerDialog.show();
    }

    private void binding() {
        inputDate.setText(Globals.SDF_ONLY_DATE.format(mSubmitTimestamp));
    }

    /**
     * API methods
     */
    private void apiSubmitRunningResult() {
        // prepare usage variables
        rqSubmitRunningResult request = new rqSubmitRunningResult();

        //--> update props
        request.distance = 0.1;
        request.event_id = Globals.EVENT_ID;
        request.activity_date = System.currentTimeMillis();

        // submit running result
        new SubmitRunningResultService(activity, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                // prepare usage variables
                final String mtn = ct + "onSuccess() ";

                L.i(mtn + "json-string: " + response.jsonString);
            }

            @Override
            public void onFailure(xResponse response) {
                // prepare usage variables
                final String mtn = ct + "onFailure() ";
                L.i(mtn + "json-string: " + response.jsonString);

            }
        }).doIt(request);
    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        // prepare usage variables
        final String mtn = ct + "viewEventListener() ";

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSelectDate.setOnClickListener(this);
        inputDist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            final DecimalFormat dcf = new DecimalFormat("#.##");
            final Pattern pattern = Pattern.compile("\\.");
            String lastDist = "0.00";

            @Override
            public void afterTextChanged(Editable editable) {
                inputDist.removeTextChangedListener(this);

                L.i(mtn + "comming msg: " + editable);

                try {
                    // prepare usage variables
                    String msg = editable.toString();

                    // condition
                    if(msg.isEmpty()) {

                    }  else {
                        // prepare usage variables
                        Matcher matcher = pattern.matcher(msg);

                        int count = 0;
                        while (matcher.find()) {
                            ++count;

                            if (count > 1) break;
                        }

                        if (count > 1) {
                            editable.clear();
                            editable.append(lastDist);

                        } else {

                            editable.clear();
                            editable.append( ( count > 0 && (msg +"").split("\\.").length > 1)
                                    ? dcf.format(Double.parseDouble(msg))
                                    : msg);

                            lastDist = msg;

                            // keep last double format
//                    lastDouble = Double.parseDouble(dcf.format(msg));

                        }

                    }

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());

                }

                inputDist.addTextChangedListener(this);

            }
        });
    }

    /**
     * View matching
     */
    private void viewMatching(View v) {
        btnSelectDate = v.findViewById(R.id.frame_submit_date);
        inputDate = v.findViewById(R.id.input_date);
        inputDist = v.findViewById(R.id.input_distance);
        btnSubmit = v.findViewById(R.id.btn_submit);
        btnCancel = v.findViewById(R.id.btn_cancel);

    }
}
