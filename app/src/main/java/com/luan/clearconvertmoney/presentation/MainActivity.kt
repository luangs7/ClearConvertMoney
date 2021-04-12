package com.luan.clearconvertmoney.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.luan.clearconvertmoney.R
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    val viewModel: ConvertViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupObservers()
    }

    private fun setupObservers(){
        viewModel.run {
            convertResult.observe(this@MainActivity, Observer {
                Log.d("","")
            })

            getCurrenciesResponse.observe(this@MainActivity, Observer {
                Log.d("","")
            })

            currencyFrom.observe(this@MainActivity, Observer {
                Log.d("","")
            })

            currencyTo.observe(this@MainActivity, Observer {
                Log.d("","")
            })
        }
    }
}