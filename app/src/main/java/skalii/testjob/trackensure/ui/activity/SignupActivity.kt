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
import skalii.testjob.trackensure.databinding.ActivitySignupBinding
import skalii.testjob.trackensure.helper.setVisibility
import skalii.testjob.trackensure.helper.toast


class SignupActivity : AppCompatActivity(R.layout.activity_signup) {

    private val viewBinding by viewBinding(ActivitySignupBinding::bind, R.id.activity_signup)
    private val mainLaunch = MainScope() + CoroutineName(this.javaClass.simpleName)

    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val editEmail = viewBinding.editEmailActivitySignup
        val editPassword = viewBinding.editPasswordActivitySignup
        val progressBar = viewBinding.progressBarActivitySignup

        auth = FirebaseAuth.getInstance()

        viewBinding.buttonResetPasswordActivitySignup.setOnClickListener {
            startActivity(Intent(this@SignupActivity, PasswordResetActivity::class.java))
        }
        viewBinding.buttonLoginActivitySignup.setOnClickListener { finish() }
        viewBinding.buttonSignupActivitySignup.setOnClickListener {
            val email = editEmail.text.toString().trim { it <= ' ' }
            val password = editPassword.text.toString().trim { it <= ' ' }

            if (TextUtils.isEmpty(email)) {
                toast("Enter email address!")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                toast("Enter password!")
                return@setOnClickListener
            }
            if (password.length < 6) {
                toast("Password too short, enter minimum 6 characters!")
                return@setOnClickListener
            }

            progressBar.setVisibility(true)

            auth
                ?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this@SignupActivity) { task ->
                    mainLaunch.launch { toast("Create user with email completed") }
                    progressBar.setVisibility(false)
                    if (!task.isSuccessful) {
                        mainLaunch.launch { toast("Authentication failed." + task.exception) }
                    } else {
                        startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                        finish()
                    }
                }
        }
    }

    override fun onResume() {
        super.onResume()
        viewBinding.progressBarActivitySignup.setVisibility(false)
    }

}