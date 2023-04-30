package com.e.cryptocracy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.e.cryptocracy.home.ui.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onStart() {
        super.onStart()
        // FirebaseAuth(this).invoke()

        CoroutineScope(Dispatchers.Default).launch {
            delay(2000)
            navigate()
        }


    }

    private fun navigate() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}