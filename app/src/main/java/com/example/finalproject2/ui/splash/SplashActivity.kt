package com.example.finalproject2.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject2.R
import com.example.finalproject2.ui.main.MainActivity
import com.example.finalproject2.ui.signin.SignInActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private val DELAY_MILI: Long = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intent =
            if (FirebaseAuth.getInstance().currentUser != null) Intent(MainActivity.getInstance(this)) else Intent(
                SignInActivity.getInstance(this)
            )

        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, DELAY_MILI)

    }
}
