package fr.isen.eval_360_mobile

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private val inputEmail: EditText by lazy { findViewById(R.id.passwordLogIn) }
    private val inputusername: EditText by lazy { findViewById(R.id.emailLogIn) }
    private val inputpassword: EditText by lazy { findViewById(R.id.password) }
    private val inputpassword2: EditText by lazy { findViewById(R.id.password2) }
    private val btn_sign_up by lazy { findViewById<Button>(R.id.btn_sign_up) }
    private val emailPattern = Regex(pattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val progressDialog by lazy { ProgressDialog(this) }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val button = findViewById<Button>(R.id.btn_sign_in)
        button.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        btn_sign_up.setOnClickListener {
            performAuth()
        }

    }

    private fun performAuth() {
        val email = inputEmail.text.toString()
        val password = inputpassword.text.toString()
        val password2 = inputpassword2.text.toString()
        val username = inputusername.text.toString()

        if (!email.matches(emailPattern)) {
            inputEmail.error = "Enter correct email"
        } else if (password.isEmpty() || password.length < 6) {
            inputpassword.error = "Enter proper password"
        } else if (password != password2) {
            inputpassword2.error = "Passwords do not match"
        } else {
            progressDialog.setMessage("Please wait while registering")
            progressDialog.setTitle("Registration")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        val userProfileChangeRequest = UserProfileChangeRequest.Builder()
                            .setDisplayName(username)
                            .build()
                        user?.updateProfile(userProfileChangeRequest)
                            ?.addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    progressDialog.dismiss()
                                    // Store user info in realtime database
                                    val databaseReference = mDatabase.reference.child("users")
                                    val userId = user?.uid
                                    val userData = User(email, username)
                                    databaseReference.child(userId ?: "").setValue(userData)

                                    val button = findViewById<Button>(R.id.btn_sign_up)
                                    button.setOnClickListener {
                                        val intent = Intent(this, Login::class.java)
                                        startActivity(intent)
                                    }
                                    Toast.makeText(
                                        this,
                                        "Registration successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    progressDialog.dismiss()
                                    Toast.makeText(
                                        this,
                                        profileTask.exception?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this,
                            task.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }
    }
}

