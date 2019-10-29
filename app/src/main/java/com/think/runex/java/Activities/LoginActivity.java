package com.think.runex.java.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.think.runex.java.Models.TokenObject;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.rqLogin;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.GetProfileService;
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
    private ActivityUtils activityUtils;

    // views
    private View btnLogin;
    private View btnLoginWithFacebook;
    private View btnLoginWithGoogle;
    //--> Input fields
    private EditText inputEmail;
    private EditText inputPassword;

    /**
     * View on click
     */
    @Override
    public void onClick(View view) {
        // prepare usage variables
        final List<String> permissions = new ArrayList<>();

        // update
        permissions.add("email");

        switch (view.getId()) {
            case R.id.btn_login:
                loginWithEmail();
                break;
            case R.id.btn_login_with_google:
                socialLoginManger.loginWithGoogle(this);
                break;
            case R.id.btn_login_with_facebook:
                socialLoginManger.loginWithFacebook(this, permissions);
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

        Toast.makeText(this, "AAA", Toast.LENGTH_SHORT).show();

        L.i(mtn + "Social login completed with: " + SocialPlatform.Companion.platformText(platform));
        L.i(mtn + "User: $userProvider" + Globals.GSON.toJson(userProvider));
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

        // Activity utils
        ActivityUtils actUtls = ActivityUtils.newInstance(this);
        actUtls.activity = this;
        actUtls.fullScreen();

        // activity manager
        activityUtils = ActivityUtils.newInstance(this);
        activityUtils.activity = this;
        activityUtils.fullScreen();

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
     * Feature methods
     */
    private void updateAppEntity(AppEntity appEntity) {
        App.instance(this).save(appEntity);

    }

    private boolean validate() {
        return true;
    }

    private void loginWithEmail() {
        if (validate()) {
            apiLogin(new rqLogin(inputEmail.getText().toString(),
                    inputPassword.getText().toString(),
                    Globals.PLATFORM));

        }
    }

    /**
     * API methods
     */
    private void apiGetProfile( ){
        // prepare usage variables
        final String mtn = ct +"apiGetProfile() ";

        //--> fire
        new GetProfileService(this, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn +"response-code: "+ response.responseCode);
                L.i(mtn +"response-body: "+ response.jsonString);

                // set activity result
                setResult(Activity.RESULT_OK);

                // exit from this process
                finish();
            }

            @Override
            public void onFailure(xResponse response) {
                L.e(mtn +"response-code: "+ response.responseCode);
                L.e(mtn +"msg: "+ response.message);

                // set activity result
                setResult(Activity.RESULT_CANCELED);

                // exit from this process
                finish();

            }
        }).doIt();
    }
    private void apiLogin(rqLogin request) {
        // prepare usage variables
        final String mtn = ct + "apiLogin() ";
        NetworkUtils nw = NetworkUtils.newInstance(this);
        NetworkProps props = new NetworkProps();

        // update props
        props.addHeader("Authorization", "Bearer " + Globals.TOKEN);
        props.setJsonAsObject(request);
        props.setUrl(APIs.LOGIN.VAL);

        nw.postJSON(props, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse rsp) {

                try {
                    // prepare usage variables
                    final String jsonString = rsp.jsonString;
                    final AppEntity appEntity = App.instance(LoginActivity.this).getAppEntity();
                    final TokenObject token = Globals.GSON.fromJson(jsonString, TokenObject.class);
                    L.i(mtn + "onSuccess: " + jsonString);

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
            public void onFailure(xResponse res) {
                L.e(mtn + "response code: " + res.responseCode);
                L.e(mtn + "failed msg: " + res.jsonString);

                // set activity result
                setResult(Activity.RESULT_CANCELED);
            }
        });

    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        btnLogin.setOnClickListener(this);
        btnLoginWithFacebook.setOnClickListener(this);
        btnLoginWithGoogle.setOnClickListener(this);

    }

    /**
     * Matching views
     */
    private void matchingViews() {
        btnLogin = findViewById(R.id.btn_login);
        btnLoginWithFacebook = findViewById(R.id.btn_login_with_facebook);
        btnLoginWithGoogle = findViewById(R.id.btn_login_with_google);

        //--> Edit text
        inputEmail = findViewById(R.id.edt_email);
        inputPassword = findViewById(R.id.edt_password);
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
