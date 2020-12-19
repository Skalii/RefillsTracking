package skalii.testjob.trackensure.ui.activity


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils

import androidx.appcompat.app.AppCompatActivity

import by.kirich1409.viewbindingdelegate.viewBinding

import com.google.firebase.auth.FirebaseAuth

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

import skalii.testjob.trackensure.R
import skalii.testjob.trackensure.databinding.ActivityLoginBinding
import skalii.testjob.trackensure.helper.setVisibility
import skalii.testjob.trackensure.helper.toast


class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private val viewBinding by viewBinding(ActivityLoginBinding::bind, R.id.activity_login)
    private val mainLaunch = MainScope() + CoroutineName(this.javaClass.simpleName)

    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val editEmail = viewBinding.editEmailActivityLogin
        val editPassword = viewBinding.editPasswordActivityLogin
        val progressBar = viewBinding.progressBarActivityLogin

        auth = FirebaseAuth.getInstance()
        if (auth?.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        auth = FirebaseAuth.getInstance()
        viewBinding.buttonSignupActivityLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }
        viewBinding.buttonResetPasswordActivityLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, PasswordResetActivity::class.java))
        }
        viewBinding.buttonLoginActivityLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            if (TextUtils.isEmpty(email)) {
                toast("Enter email address!")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                toast("Enter password!")
                return@setOnClickListener
            }
            progressBar.setVisibility(true)

            auth
                ?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this@LoginActivity) { task ->
                    progressBar.setVisibility(false)
                    if (!task.isSuccessful) {
                        if (password.length < 6) {
                            editPassword.error = "Password must be longer than 6 characters"
                        } else {
                            mainLaunch.launch { toast("Auth failed") }
                        }
                    } else {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                }
        }
    }

}