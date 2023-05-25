package fr.isen.eval_360_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import fr.isen.eval_360_mobile.studentView.Eleve
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.os.AsyncTask
import java.io.OutputStream


class Login : AppCompatActivity() {

    private val inputEmail: EditText by lazy { findViewById(R.id.emailLogIn) }
    private val inputpassword: EditText by lazy { findViewById(R.id.passwordLogIn) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signUpText: TextView = findViewById(R.id.signInTextViewInLogInActivity)
        signUpText.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        val buttonSignIn = findViewById<Button>(R.id.signInbuttonLogIn)
        buttonSignIn.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputpassword.text.toString()

            // Exécution de la requête réseau dans AsyncTask
            AsyncTask.execute {
                authenticateUser(email, password)
            }
        }
    }

    private fun authenticateUser(email: String, password: String) {
        // Envoyer la requête d'authentification à l'application Flask
        val endpoint = "http://localhost:5000/teachers"
        val jsonData = """
            {
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()

        val url = URL(endpoint)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        val outputStream: OutputStream = connection.outputStream
        outputStream.write(jsonData.toByteArray())
        outputStream.flush()

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Authentification réussie
            runOnUiThread {
                val intent = Intent(this, Eleve::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            // Authentification échouée
            runOnUiThread {
                Toast.makeText(this, "Échec de l'authentification", Toast.LENGTH_SHORT).show()
            }
        }

        connection.disconnect()
    }

    private fun readResponse(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuilder()
        var line: String? = reader.readLine()
        while (line != null) {
            response.append(line)
            line = reader.readLine()
        }
        reader.close()
        return response.toString()
    }

    private fun checkAuthentication(email: String, password: String, teachersArray: JSONArray): Boolean {
        for (i in 0 until teachersArray.length()) {
            val teacherObject: JSONObject = teachersArray.getJSONObject(i)
            val teacherEmail = teacherObject.getString("email")
            val teacherPassword = teacherObject.getString("password")

            if (email == teacherEmail && password == teacherPassword) {
                return true
            }
        }

        return false
    }
}
