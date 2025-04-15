package com.example.spaceinvader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var myView: MyView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myView = MyView(this)
        setContentView(myView)
        myView.setBackgroundResource(R.drawable.image2)
    }
}

