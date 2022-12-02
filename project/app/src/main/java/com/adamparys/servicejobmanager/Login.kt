package com.adamparys.servicejobmanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.facebook.AccessToken
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.AccountKit
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.parse.*
import java.util.*
import kotlin.math.log

const val APP_REQUEST_CODE = 99;

class Login : AppCompatActivity(), View.OnClickListener, View.OnKeyListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val signupButton = findViewById(R.id.signUpButton) as Button
        signupButton.setOnClickListener{view ->
            sighnUp(view)
        }
        var token: com.facebook.accountkit.AccessToken? = AccountKit.getCurrentAccessToken()

        if (token != null) {
            //Handle Returning User
        } else {
            //Handle new or logged out user

            if (ParseUser.getCurrentUser() != null) {
                //ParseUser.logOutInBackground();
                Toast.makeText(this,ParseUser.getCurrentUser().username,Toast.LENGTH_LONG).show()
            }
        }

        loginTV = findViewById(R.id.logInTextView)
        loginTV?.setOnClickListener(this)
        val textView = findViewById(R.id.passwordEditText) as EditText
        textView.setOnKeyListener(this)

        val background = findViewById(R.id.backgrounfRelativeLayout) as ViewGroup
        val imageView = findViewById(R.id.loginLogo) as ImageView
        background.setOnClickListener(this)
        imageView.setOnClickListener(this)

        callbackManager = CallbackManager.Factory.create()
        //callbackManager.onActivityResult()

        val fbloginButton = findViewById(R.id.login_button) as LoginButton
        fbloginButton.setReadPermissions(Arrays.asList(EMAIL))
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        fbloginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.i("login result", "Success")
                val id = loginResult.accessToken.userId
                Toast.makeText(parent, "hello $id", Toast.LENGTH_LONG).show()
            }

            override fun onCancel() {
                // App code
                Log.i("login result", "Cancel")
            }

            override fun onError(exception: FacebookException) {
                // App code
                Log.i("login result", "Error")
            }
        })
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        ParseAnalytics.trackAppOpenedInBackground(intent)
    }

    private val EMAIL = "email"
    internal var signupModeActive: Boolean = false
    internal var loginTV: TextView? = null

    private var callbackManager: CallbackManager? = null


    fun phoneLogin(view: View) {
        val intent = Intent(this.parent, AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE, AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
            AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
            configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }
    override fun onClick(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        if (view.id == R.id.logInTextView) {
            val signupButton = findViewById(R.id.signUpButton) as Button
            if (signupModeActive) {
                signupModeActive = false
                signupButton.setText(R.string.log_in)
                loginTV?.text = "or, sign up"
            } else {
                signupModeActive = true
                signupButton.setText(R.string.sign_up)
                loginTV?.text = "or, log in"
            }
        }
    }

    fun sighnUp(view: View) {
        val username = (findViewById(R.id.userNameEditText) as EditText).text.toString()
        val password = (findViewById(R.id.passwordEditText) as EditText).text.toString()
        val user = ParseUser()
        if (username.isBlank() || password == "") {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show()
        } else {
            if (signupModeActive) {
                user.username = username
                user.setPassword(password)
                user.signUpInBackground { e ->
                    if (e == null) {
                        Toast.makeText(this, " up successful", Toast.LENGTH_SHORT)
                            .show()
                        showJobList()
                    } else {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {//login mode active
                ParseUser.logInInBackground(username, password) { user, e ->
                    if (e == null) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT)
                            .show()
                        showJobList()
                    } else {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
            sighnUp(v)
        }
        return false
    }

     private fun showJobList() {
        val intent = Intent(this.applicationContext, JobListActivity::class.java).apply {}
        startActivity(intent)

    }
}
