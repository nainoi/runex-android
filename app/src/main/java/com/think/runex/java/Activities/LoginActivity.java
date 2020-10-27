package com.think.runex.java.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.facebook.internal.CallbackManagerImpl;
import com.think.runex.R;
import com.think.runex.feature.social.SocialLoginListener;
import com.think.runex.feature.social.SocialLoginManger;
import com.think.runex.feature.social.SocialPlatform;
import com.think.runex.feature.user.UserProvider;
import com.think.runex.java.App.App;
import com.think.runex.java.App.AppEntity;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Views.BottomSheet.BottomSheetCallback;
import com.think.runex.java.Customize.Views.BottomSheet.xRegistrationBottomSheet;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Models.TokenObject;
import com.think.runex.java.Models.UserObject;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.DeviceUtils;
import com.think.runex.java.Utils.KeyboardUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.rqLogin;
import com.think.runex.java.Utils.Network.Request.rqRegisterUser;
import com.think.runex.java.Utils.Network.Request.rqSocialPd;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.GetProfileService;
import com.think.runex.java.Utils.Network.Services.SocialsLoginPdService;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static com.think.runex.feature.social.SocialLoginManger.RC_GOOGLE_LOGIN;

public class LoginActivity extends FragmentActivity implements
        SocialLoginListener,
        View.OnClickListener {
    /**
     * Main variables
     */
    private final String ct = "LoginActivity->";

    // instance variables
    private SocialLoginManger socialLoginManger;
    private ActivityUtils activityUtils;

    // explicit variables
    private boolean ON_LOGGING_IN = false;

    // views
    private View btnToLoginWithEmail;
    private View btnLoginWithEmail;
    private View btnLoginWithFacebook;
    private View btnLoginWithGoogle;
    private View btnExit;
    private View btnRegister;
    private View bottomSheetView;
    //--> Input fields
    private EditText inputEmail;
    private EditText inputPassword;
    //--> Frame socials login
    private View frameSocialsLogin;
    private View frameLoginWithEmail;
    //--> xRegister bottom sheet
    private xRegistrationBottomSheet bottomSheetRegistration;

    @Override
    public void onBackPressed() {
        if (frameLoginWithEmail.getVisibility() == View.VISIBLE) {
            frameLoginWithEmail.setVisibility(View.GONE);
            frameSocialsLogin.setVisibility(View.VISIBLE);

        } else super.onBackPressed();
    }

    /**
     * View on click
     */
    @Override
    public void onClick(View view) {
        // on logging in
        if (ON_LOGGING_IN) return;

        // update flag
        ON_LOGGING_IN = true;

        // gone keyboard
        KeyboardUtils.hideKeyboard( getWindow().getDecorView() );

        switch (view.getId()) {
            //--> Login action
            case R.id.btn_login_with_email:
                // login with email
                loginWithEmail();
                break;
            case R.id.btn_login_with_google:
                socialLoginManger.loginWithGoogle(this);
                break;
            case R.id.btn_login_with_facebook:
                socialLoginManger.loginWithFacebook(this, new ArrayList<String>() {{
                    add("email");
                }});

                break;

            //--> Feature action
            //--> Register
            case R.id.btn_register:
                // clear flag
                ON_LOGGING_IN = false;

                // display register dialog
                bottomSheetRegistration.show(Globals.RC_REGISTER_USER, new BottomSheetCallback() {
                    @Override
                    public void xBottomSheetCallback(xTalk xTalk) {
                        // prepare usage variables
                        View fakeView = new View(LoginActivity.this);
                        rqRegisterUser register = (rqRegisterUser) xTalk.attachObject;

                        // update props
                        fakeView.setId( R.id.btn_to_login_with_email );

                        // perform on-click
                        onClick( fakeView );

                        // update flag
                        ON_LOGGING_IN = true;

                        // dismiss dialog
                        bottomSheetRegistration.dismiss();

                        // update login with email props
                        inputEmail.setText( register.getEmail() );
                        inputPassword.setText( register.getPassword() );

                        // auto login with email
                        loginWithEmail();
                    }
                });

                break;
            case R.id.btn_to_login_with_email:
                // update flag
                ON_LOGGING_IN = false;

                // display login with email frame
                frameLoginWithEmail.setVisibility(View.VISIBLE);
                frameSocialsLogin.setVisibility(View.GONE);

                break;
            case R.id.btn_cross:
                // update flag
                ON_LOGGING_IN = false;

                if (frameLoginWithEmail.getVisibility() == View.VISIBLE) {
                    frameLoginWithEmail.setVisibility(View.GONE);
                    frameSocialsLogin.setVisibility(View.VISIBLE);

                } else onBackPressed();

                break;
        }


    }

    /**
     * Implement methods
     */
    @Override
    public void onLoginWithSocialCompleted(int platform, @NotNull UserProvider userProvider) {
        // prepare usage variables
        final String mtn = ct + "onLoginWithSocialCompleted() ";
        final rqSocialPd request = getRqSocialPd(platform, userProvider);

        L.i(mtn + " * * * Social Login Completed * * * ");
        L.i(mtn + "platform: " + SocialPlatform.Companion.platformText(platform));
        L.i(mtn + "user's provider: " + Globals.GSON.toJson(userProvider));
        L.i(mtn + "request PD: " + Globals.GSON.toJson(request));

        // request token
        apiSocialsLogin(request);

    }

    @Override
    public void onLoginWithSocialCancel() {

    }

    @Override
    public void onLoginWithSocialError(@NotNull Exception exception) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // activity manager
        activityUtils = ActivityUtils.newInstance(this);
        activityUtils.fullScreen();

        //Initial social login manage.
        socialLoginManger = new SocialLoginManger();
        socialLoginManger.setSocialLoginListener(this);
        socialLoginManger.registerLoginCallback();

        // register bottom sheet
        bottomSheetRegistration = new xRegistrationBottomSheet(this, R.layout.bottomsheet_registration);

        // matching views
        viewsMatching();

        // view event listener
        viewEventListener();

        // default account
//        inputEmail.setText("fakespmh.21@gmail.com");
//        inputPassword.setText("p@ss1234");
    }

    /**
     * Feature methods
     */

    private void updateAppEntity(AppEntity appEntity) {
        App.instance(this).save(appEntity);

    }

    private boolean validate() {
        return !inputPassword.getText().toString().isEmpty()
                && isEmailValid(inputEmail.getText().toString());
    }

    private void loginWithEmail() {
        if (validate()) {
            apiLogin(new rqLogin(inputEmail.getText().toString(),
                    inputPassword.getText().toString(),
                    Globals.PLATFORM));

        } else {

            // clear flag
            ON_LOGGING_IN = false;

            // toast
            Toast.makeText(this, "โปรดระบุอีเมล และ พาสเวิร์ดให้ถูกต้อง", Toast.LENGTH_SHORT).show();

        }
    }

    private boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);

    }

    /**
     * API methods
     */
    private void apiGetProfile() {
        // prepare usage variables
        final String mtn = ct + "apiGetProfile() ";

        //--> fire
        new GetProfileService(this, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn + "response-code: " + response.responseCode);
                L.i(mtn + "response-body: " + response.jsonString);

                try {

                    // prepare usage variables
                    UserObject user = Globals.GSON.fromJson(response.jsonString, UserObject.class);
                    App app = App.instance(LoginActivity.this);

                    // save
                    AppEntity appEntity = app.getAppEntity().setUser(user);

                    // commit
                    app.save(appEntity);

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e);

                }
                // set activity result
                setResult(Activity.RESULT_OK);

                // exit from this process
                finish();

            }

            @Override
            public void onFailure(xResponse response) {
                L.e(mtn + "response-code: " + response.responseCode);
                L.e(mtn + "msg: " + response.message);

                // set activity result
                setResult(Activity.RESULT_CANCELED);

                // exit from this process
                finish();


            }
        }).doIt();
    }

    private void apiSocialsLogin(rqSocialPd request) {
        // prepare usage variables
        final String mtn = ct + "apiSocialLogin() ";

        // fire
        new SocialsLoginPdService(this, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn + "onSuccess: " + response.jsonString);

                try {
                    // prepare usage variables
                    final String jsonString = response.jsonString;
                    final AppEntity appEntity = App.instance(LoginActivity.this).getAppEntity();
                    final TokenObject token = Globals.GSON.fromJson(jsonString, TokenObject.class);

                    // commit
                    updateAppEntity(appEntity);

                    // call user profile
                    apiGetProfile();

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e);

                    // set activity result
                    setResult(Activity.RESULT_CANCELED);

                    // exit from this process
                    finish();

                }

            }

            @Override
            public void onFailure(xResponse response) {
                L.e(mtn + "code: " + response.responseCode);
                L.e(mtn + "body: " + response.jsonString);

                // set activity result
                setResult(Activity.RESULT_CANCELED);

            }
        }).doIt(request);
    }

    private void apiLogin(rqLogin request) {
        // prepare usage variables
        final String mtn = ct + "apiLogin() ";
        NetworkUtils nw = NetworkUtils.newInstance(this);
        NetworkProps props = new NetworkProps();

        // update props
        props.setJsonAsObject(request);
        props.setUrl(APIs.LOGIN.VAL);

        nw.postJSON(props, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse rsp) {

                if( rsp.responseCode != HttpURLConnection.HTTP_OK ){
                    // clear flag
                    ON_LOGGING_IN = false;

                    // toast
                    Toast.makeText(LoginActivity.this, "เกิดข้อผิดพลาดขณะเข้าสู่ระบบ", Toast.LENGTH_SHORT).show();

                    // exit from this process
                    return;
                }

                try {
                    // prepare usage variables
                    final String jsonString = rsp.jsonString;
                    final AppEntity appEntity = App.instance(LoginActivity.this).getAppEntity();
                    final TokenObject token = Globals.GSON.fromJson(jsonString, TokenObject.class);
                    L.i(mtn + "onSuccess: " + jsonString);

                    // commit
                    updateAppEntity(appEntity);

                    // call user profile
                    apiGetProfile();

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e);

                    // set activity result
                    setResult(Activity.RESULT_CANCELED);

                    // exit from this process
                    finish();

                }

            }

            @Override
            public void onFailure(xResponse res) {
                L.e(mtn + "response code: " + res.responseCode);
                L.e(mtn + "failed msg: " + res.jsonString);

                // set activity result
                setResult(Activity.RESULT_CANCELED);

                // exit from this page
                finish();
            }
        });

    }

    /**
     * Getter
     */
    private rqSocialPd getRqSocialPd(int platform, UserProvider userProvider) {
        // prepare usage variables
        final String mtn = ct + "getRqSocialPd() ";
        final rqSocialPd rq = new rqSocialPd();

        rq.setAvartar(userProvider.getAvatar());
        rq.setEmail(userProvider.getEmail());
        rq.setFirst_name(userProvider.getFirstName());
        rq.setLast_name(userProvider.getLastName());
        rq.setFullname(userProvider.getFirstName() + " " + userProvider.getLastName());
        rq.setPf(Globals.PLATFORM);
        rq.setProvider_id(userProvider.getId());
        rq.setVersions("versions");

        if (SocialPlatform.Companion.platformText(platform).equalsIgnoreCase("Google")) {
            rq.setProvider("GoogleID");

        } else if (SocialPlatform.Companion.platformText(platform).equalsIgnoreCase("Facebook")) {
            rq.setProvider(userProvider.getId());

        } else L.e(mtn + "Platform does not match.");

        return rq;
    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        btnToLoginWithEmail.setOnClickListener(this);
        btnLoginWithEmail.setOnClickListener(this);
        btnLoginWithFacebook.setOnClickListener(this);
        btnLoginWithGoogle.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    /**
     * Matching views
     */
    private void viewsMatching() {
        btnRegister = findViewById(R.id.btn_register);
        btnExit = findViewById(R.id.btn_cross);
        btnToLoginWithEmail = findViewById(R.id.btn_to_login_with_email);
        btnLoginWithEmail = findViewById(R.id.btn_login_with_email);
        btnLoginWithFacebook = findViewById(R.id.btn_login_with_facebook);
        btnLoginWithGoogle = findViewById(R.id.btn_login_with_google);

        //--> Edit text
        inputEmail = findViewById(R.id.edt_email);
        inputPassword = findViewById(R.id.edt_password);

        //--> Frames
        frameLoginWithEmail = findViewById(R.id.frame_login_with_email);
        frameSocialsLogin = findViewById(R.id.frame_socials_login);

        //--> Bottom sheet


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_GOOGLE_LOGIN ||
                requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            socialLoginManger.handleLogInResult(requestCode, resultCode, data);

        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    protected void onDestroy() {
        socialLoginManger.unRegisterLoginCallback();
        super.onDestroy();
    }
}
