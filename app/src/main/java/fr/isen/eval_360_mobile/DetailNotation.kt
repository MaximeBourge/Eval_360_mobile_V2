package fr.isen.eval_360_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.play.integrity.internal.f

class DetailNotation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_notation)

        val username = intent.getStringExtra("USERNAME")
        val textView = findViewById<TextView>(R.id.textView2)
        textView.text = username

        val note1EditText = findViewById<EditText>(R.id.note1)
        val note2EditText = findViewById<EditText>(R.id.note2)
        val note3EditText = findViewById<EditText>(R.id.note3)
        val note4EditText = findViewById<EditText>(R.id.note4)
        val noteFinal = findViewById<TextView>(R.id.textView8)
        val notes = listOf(note1EditText, note2EditText, note3EditText, note4EditText)

        val validationButton = findViewById<Button>(R.id.button7)
        validationButton.setOnClickListener {
            if (validateFields(notes)) {
                val note1 = note1EditText.text.toString().toDouble()
                val note2 = note2EditText.text.toString().toDouble()
                val note3 = note3EditText.text.toString().toDouble()
                val note4 = note4EditText.text.toString().toDouble()
                val average = note1 * 0.6 + note2 * 0.1 + note3 * 0.15 + note4 * 0.15
                val formatAverage = String.format("%.1f", average)
                Log.d("DetailNotation", "Moyenne calculée : $average")

                noteFinal.setText(formatAverage.toString())
                // Do something with the notes
            }

        }
    }

    private fun validateFields(notes: List<EditText>): Boolean {
        for (note in notes) {
            val noteString = note.text.toString()
            if (noteString.isBlank()) {
                note.error = "Ce champ est obligatoire"
                return false
            } else {
                val noteValue = noteString.toDoubleOrNull()
                if (noteValue == null || noteValue < 0.0 || noteValue > 20.0) {
                    note.error = "La note doit être comprise entre 0 et 20"
                    return false
                }
            }
        }
        return true
    }
}



