package com.github.mydogtom.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.github.mydogtom.baseapp.loggedOutComponent
import com.github.mydogtom.userdetailsnavigator.UserDetailsNavigator
import javax.inject.Inject

class LogInActivity : AppCompatActivity(), LogInView {
    @Inject lateinit var presenter: LoginPresenter
    @Inject lateinit var userDetailsNavigator: UserDetailsNavigator
    private val logInButton by lazy {
        findViewById<Button>(R.id.buttonLogIn)
    }
    private val userNameEditText by lazy {
        findViewById<EditText>(R.id.editTextUserName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        DaggerLoginComponent.builder()
                .loginView(this)
                .loggedOutComponent(this.loggedOutComponent())
                .build()
                .inject(this)

        logInButton.setOnClickListener {
            presenter.performLogIn(userNameEditText.text.toString())
        }
    }

    override fun navigateToUserDetails() {
        userDetailsNavigator.openUserDetails(this)
    }
}
