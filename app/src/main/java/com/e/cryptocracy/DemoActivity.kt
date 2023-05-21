package com.e.cryptocracy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.cryptocracy.coinDetail.graph.GraphUi
import com.e.cryptocracy.databinding.ActivityDemoBinding

class DemoActivity : AppCompatActivity() {
    lateinit var binding: ActivityDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // GraphUi().invoke(binding.hc, it.coinHistoryData)
    }
}