package com.github.mydogtom.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.github.mydogtom.baseapp.loggedOutComponent
import javax.inject.Inject

class LogInActivity : AppCompatActivity() {
    @Inject lateinit var presenter: LoginPresenter
    private val logInButton by lazy {
        findViewById<Button>(R.id.buttonLogIn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        DaggerLoginComponent.builder()
                .loggedOutComponent(this.loggedOutComponent())
                .build()
                .inject(this)

        logInButton.setOnClickListener {
            presenter.performLogIn()
        }
    }
}
