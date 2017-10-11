package com.github.mydogtom.userdetails

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.github.mydogtom.baseapp.loggedInComponent
import com.github.mydogtom.persistence.UserName
import javax.inject.Inject

class UserDetailsActivity : AppCompatActivity(), UserDetailsView {
    private val userNameTextView by lazy {
        findViewById<TextView>(R.id.tvUserName)
    }

    private val addressTextView by lazy {
        findViewById<EditText>(R.id.edAddress)
    }

    private val saveButton by lazy {
        findViewById<Button>(R.id.btnSave)
    }

    @Inject lateinit var presenter: UserDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        DaggerUserDetailsComponent.builder()
                .loggedInComponent(this.loggedInComponent())
                .userDetailsView(this)
                .build()
                .inject(this)

        saveButton.setOnClickListener {
            presenter.save(addressTextView.text.toString())
        }
    }

    override fun showAddress(address: String) {
        addressTextView.setText(address)
    }


    override fun showData(userName: UserName) {
        userNameTextView.text = userName.name
    }
}
