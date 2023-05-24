package fr.isen.eval_360_mobile
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.isen.eval_360_mobile.studentView.Eleve

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val inputEmail: EditText by lazy { findViewById(R.id.emailLogIn) }
    private val inputpassword: EditText by lazy { findViewById(R.id.passwordLogIn) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val signUpText: TextView = findViewById(R.id.signInTextViewInLogInActivity)
        signUpText.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        val buttonSignIn = findViewById<Button>(R.id.signInbuttonLogIn)
        buttonSignIn.setOnClickListener {
            val email = inputEmail.text.toString().trim()
            val password = inputpassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this, "Veuillez remplir tous les champs.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // L'authentification a réussi, redirigez l'utilisateur vers l'activité souhaitée
                            val intent = Intent(this, Eleve::class.java)
                            startActivity(intent)
                            Toast.makeText(
                                this,
                                "Connexion réussie",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // L'authentification a échoué, affichez un message d'erreur à l'utilisateur
                            Toast.makeText(
                                this, "Échec de l'authentification.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}
