package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import com.example.speedmatch.R
import com.example.speedmatch.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayout(): ActivityMainBinding {
        return ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction().add(R.id.flMain, ListFragment()).commit()
    }

}