package com.think.runex.java.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;

import com.facebook.internal.CallbackManagerImpl;
import com.google.android.material.textview.MaterialTextView;
import com.think.runex.R;
import com.think.runex.feature.social.SocialLoginListener;
import com.think.runex.feature.social.SocialLoginManger;
import com.think.runex.feature.social.SocialPlatform;
import com.think.runex.feature.user.UserProvider;
import com.think.runex.java.App.App;
import com.think.runex.java.App.AppEntity;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Models.TokenObject;
import com.think.runex.java.Models.UserObject;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.rqLogin;
import com.think.runex.java.Utils.Network.Request.rqSocialPd;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.GetProfileService;
import com.think.runex.java.Utils.Network.Services.SocialsLoginPdService;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

    //Layout
    private RelativeLayout loginLayout;
    private RelativeLayout loginEmailLayout;

    // Button
    private AppCompatButton btnLogin;
    private FrameLayout btnLoginEmail;
    private FrameLayout btnLoginFacebook;
    private FrameLayout btnLoginGoogle;
    private MaterialTextView btnRegister;
    private MaterialTextView btnRegister2;
    private AppCompatImageButton btnClose;

    //--> Input fields
    private EditText inputEmail;
    private EditText inputPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initial social login manage.
        socialLoginManger = new SocialLoginManger();
        socialLoginManger.setSocialLoginListener(this);
        socialLoginManger.registerLoginCallback();

        // matching views
        matchingViews();

        // view event listener
        viewEventListener();

        // default account
        inputEmail.setText("fakespmh.21@gmail.com");
        inputPassword.setText("p@ss1234");
    }

    /**
     * Matching views
     */
    private void matchingViews() {
        loginLayout = findViewById(R.id.login_layout);
        loginEmailLayout = findViewById(R.id.login_email_layout);


        btnClose = findViewById(R.id.btn_close);
        btnLogin = findViewById(R.id.btn_login_as_email);
        btnLoginEmail = findViewById(R.id.btn_login_with_email);
        btnLoginFacebook = findViewById(R.id.btn_login_with_facebook);
        btnLoginGoogle = findViewById(R.id.btn_login_with_google);
        btnRegister = findViewById(R.id.btn_register);
        btnRegister2 = findViewById(R.id.btn_register_2);

        //--> Edit text
        inputEmail = findViewById(R.id.edt_email);
        inputPassword = findViewById(R.id.edt_password);
    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        btnClose.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLoginEmail.setOnClickListener(this);
        btnLoginFacebook.setOnClickListener(this);
        btnLoginGoogle.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnRegister2.setOnClickListener(this);
    }


    /**
     * View on click
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                if (loginEmailLayout.getVisibility() == View.VISIBLE) {
                    loginLayout.setVisibility(View.VISIBLE);
                    loginEmailLayout.setVisibility(View.GONE);
                } else {
                    // set activity result
                    setResult(Activity.RESULT_CANCELED);
                    // exit from this process
                    finish();
                }
                break;
            case R.id.btn_login_with_facebook:
                // prepare usage variables
                final List<String> permissions = new ArrayList<>();
                // update
                permissions.add("email");
                socialLoginManger.loginWithFacebook(this, permissions);
                break;
            case R.id.btn_login_with_google:
                socialLoginManger.loginWithGoogle(this);
                break;
            case R.id.btn_login_with_email:
                loginLayout.setVisibility(View.GONE);
                loginEmailLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_login_as_email:
                loginWithEmail();
                break;
            case R.id.btn_register:

                break;
            case R.id.btn_register_2:

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

    /**
     * Feature methods
     */

    private void updateAppEntity(AppEntity appEntity) {
        App.instance(this).save(appEntity);

    }


    private void loginWithEmail() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.length() == 0) {
            Toast.makeText(this, R.string.email_required, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() == 0) {
            Toast.makeText(this, R.string.password_required, Toast.LENGTH_SHORT).show();
            return;
        }

        apiLogin(new rqLogin(email, password, Globals.PLATFORM));
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

                    // update token
                    appEntity.setToken(token);
                    //--> confirm user has logged in
                    appEntity.setLoggedIn(true);

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
        Log.e("Jozzee", "apiLogin");
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

                try {
                    // prepare usage variables
                    final String jsonString = rsp.jsonString;
                    final TokenObject token = Globals.GSON.fromJson(jsonString, TokenObject.class);

                    if (token.isSuccessFul()) {
                        final AppEntity appEntity = App.instance(LoginActivity.this).getAppEntity();
                        // update token
                        appEntity.setToken(token);
                        //--> confirm user has logged in
                        appEntity.setLoggedIn(true);

                        // commit
                        updateAppEntity(appEntity);

                        // call user profile
                        apiGetProfile();
                    } else {
                        Toast.makeText(LoginActivity.this, token.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e);
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(xResponse res) {
                L.e(mtn + "response code: " + res.responseCode);
                L.e(mtn + "failed msg: " + res.jsonString);

                // set activity result
                setResult(Activity.RESULT_CANCELED);
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

        Toast.makeText(this, "fn: " + userProvider.getFirstName(), Toast.LENGTH_SHORT).show();

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
