package com.think.runex.feature.social

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.*
import com.facebook.internal.CallbackManagerImpl
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.jozzee.android.core.simpleName
import com.jozzee.android.core.utility.Logger
import com.think.runex.R
import com.think.runex.feature.user.UserProvider
import com.think.runex.java.Constants.Constants
import kotlinx.android.synthetic.main._list_item_event.*
import java.lang.Exception

class SocialLoginManger {
    private val ct: String ="SocialLoginManager->"
    companion object {
        const val RC_GOOGLE_LOGIN = 1001
        //const val RC_FACEBOOK_LOGIN = 1002
    }

    private var loginListener: SocialLoginListener? = null

    fun setSocialLoginListener(loginListener: SocialLoginListener) {
        this.loginListener = loginListener
    }

    //Google
    private var googleSignInClient: GoogleSignInClient? = null

    //Facebook
    private var facebookCallbackManager = CallbackManager.Factory.create()
    private var facebookCallback = object : FacebookCallback<LoginResult> {

        //When log in with facebook success.
        override fun onSuccess(result: LoginResult?) {
            val accessToken = AccessToken.getCurrentAccessToken()
            if (accessToken != null && accessToken.isExpired.not()) {
                Logger.debug(simpleName(), "Login with Facebook as AccessToken: ${accessToken.token}")
                Logger.info(Constants.TAG.VAL, "Login with Facebook as AccessToken: ${accessToken.token}")
                onFacebookLoginResult(accessToken)
            }
        }

        //When user cancel login (or back pressed)
        override fun onCancel() {
            loginListener?.onLoginWithSocialCancel()
        }

        //When login error.
        override fun onError(error: FacebookException?) {
            Log.e(simpleName(), "Login with Facebook Error: ${error?.message}")
            loginListener?.onLoginWithSocialError(Exception(error?.message, error?.cause))
        }
    }


    /**
     * [permissions] Permission set for facebook login
     * Reference: [https://developers.facebook.com/docs/facebook-login/permissions?locale=en_US#reference-default]
     * Default is request email.
     */
    fun loginWithFacebook(fragment: Fragment, permissions: Collection<String> = listOf("email")) {
        // prepare usage variables
        val mtn: String = "loginWithFacebook() ";

        Logger.info(Constants.TAG.VAL, mtn +"loginWithFacebook");
        LoginManager.getInstance().logIn(fragment, permissions)
    }

    fun loginWithFacebook(activity: Activity, permissions: Collection<String> = listOf("email")) {
        LoginManager.getInstance().logIn(activity, permissions)
    }

    private fun onFacebookLoginResult(accessToken: AccessToken) {
        val graphRequest = GraphRequest.newMeRequest(accessToken) { jsonObject, _ ->
            if (jsonObject != null) {
                try {
                    val user = UserProvider().apply {
                        id = jsonObject.getString("id") ?: ""
                        fullName = jsonObject.getString("name") ?: ""
                        firstName = jsonObject.getString("first_name") ?: ""
                        lastName = jsonObject.getString("last_name") ?: ""
                        email = jsonObject.getString("email") ?: ""
                        avatar = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url")
                    }
                    loginListener?.onLoginWithSocialCompleted(SocialPlatform.FACEBOOK, user)

                    //TODO("Should to logout when have a profile")
                    LoginManager.getInstance().logOut()

                } catch (e: Exception) {
                    e.printStackTrace()
                    loginListener?.onLoginWithSocialError(e)
                }
            }
        }
        graphRequest.parameters = Bundle().apply {
            putString("fields", "id,name,first_name,last_name,email,picture.type(large)")
        }
        graphRequest.executeAsync()
    }

    fun loginWithGoogle(fragment: Fragment) {
        fragment.startActivityForResult(getGoogleSignInClient(fragment.context!!)?.signInIntent, RC_GOOGLE_LOGIN)
    }

    fun loginWithGoogle(activity: Activity) {
        activity.startActivityForResult(getGoogleSignInClient(activity)?.signInIntent, RC_GOOGLE_LOGIN)
    }

    private fun getGoogleSignInClient(context: Context): GoogleSignInClient? {
        if (googleSignInClient == null) {
            // prepare usage variables
//            var serverClientId: String = context.getString(R.string.server_client_id)

            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestServerAuthCode(serverClientId)
                    .requestEmail()
                    .build()
            // Build a GoogleSignInClient with the options specified by gso.
            googleSignInClient = GoogleSignIn.getClient(context, gso)
        }
        return googleSignInClient
    }

    private fun onGoogleLoginResult(completedTask: Task<GoogleSignInAccount>) {
        // prepare usage variables
        val mtn: String = "onGoogleLoginResult() ";

        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                Logger.debug(simpleName(), "Login with Google as idToken: ${account.id}")

                val user = UserProvider().apply {
                    id = account.id ?: ""
                    fullName = account.displayName ?: ""
                    firstName = account.givenName ?: ""
                    lastName = account.familyName ?: ""
                    email = account.email ?: ""
                    avatar = account.photoUrl?.toString() ?: ""
                }
                loginListener?.onLoginWithSocialCompleted(SocialPlatform.GOOGLE, user)

                //TODO("Should to logout when have a profile")
                googleSignInClient?.signOut()?.addOnCompleteListener {
                    googleSignInClient?.revokeAccess()?.addOnCanceledListener {
                        clearGoogleLoginResult()
                    }
                }
            }

        } catch (e: Exception) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Logger.error(Constants.TAG.VAL, mtn +"Login with Google Error: " + e.message)

            e.printStackTrace()
            loginListener?.onLoginWithSocialError(e)
        }
    }

    private fun clearGoogleLoginResult() {
        if (googleSignInClient != null) {
            googleSignInClient = null
        }
    }

    fun registerLoginCallback() {
        LoginManager.getInstance().registerCallback(facebookCallbackManager, facebookCallback)
    }

    fun unRegisterLoginCallback() {
        LoginManager.getInstance().unregisterCallback(facebookCallbackManager)
    }

    fun handleLogInResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // prepare usage variables
        val mtn: String = ct +"handleLoginResult() "
        Logger.error(Constants.TAG.VAL, mtn +"handleLogInResult")

        when (requestCode) {
            CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode() -> {
                facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
            }
            RC_GOOGLE_LOGIN -> {
                onGoogleLoginResult(GoogleSignIn.getSignedInAccountFromIntent(data))
            }
        }
    }
}