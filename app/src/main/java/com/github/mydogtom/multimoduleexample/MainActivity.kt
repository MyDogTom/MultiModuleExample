package com.github.mydogtom.multimoduleexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.mydogtom.login.navigator.LoginNavigator
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject lateinit var loginNavigator: LoginNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        DaggerMainActivityComponent.builder().build().inject(this)
    }


    @OnClick(R.id.buttonStart)
    fun onStartClick() {
        Toast.makeText(this, "start activity", Toast.LENGTH_SHORT).show()
        loginNavigator.openLogin(this)
    }
}
