package fr.isen.eval_360_mobile.studentView

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import fr.isen.eval_360_mobile.R

class DetailNotation : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_notation)

        val username = intent.getStringExtra("USERNAME")
        val textView = findViewById<TextView>(R.id.textView2)
        textView.text = username
    }
}



