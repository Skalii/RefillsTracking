package skalii.testjob.trackensure.ui.activity


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
import skalii.testjob.trackensure.databinding.ActivityPasswordResetBinding
import skalii.testjob.trackensure.helper.setVisibility
import skalii.testjob.trackensure.helper.toast


class PasswordResetActivity : AppCompatActivity(R.layout.activity_password_reset) {

    private val viewBinding
            by viewBinding(ActivityPasswordResetBinding::bind, R.id.activity_password_reset)
    private val mainLaunch = MainScope() + CoroutineName(this.javaClass.simpleName)

    private var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val editEmail = viewBinding.editEmailActivityPasswordReset
        val progressBar = viewBinding.progressBarActivityPasswordReset

        auth = FirebaseAuth.getInstance()
        viewBinding.buttonBackActivityPasswordReset.setOnClickListener { finish() }
        viewBinding.buttonResetActivityPasswordReset.setOnClickListener {
            val email = editEmail.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                toast("Enter your registered email")
                return@setOnClickListener
            }
            progressBar.setVisibility(true)
            auth
                ?.sendPasswordResetEmail(email)
                ?.addOnCompleteListener { task ->
                    mainLaunch.launch {
                        if (task.isSuccessful) {
                            toast("We have sent you instructions to reset your password!")
                        } else {
                            toast("Failed to send reset email!")
                        }
                    }
                    progressBar.setVisibility(false)
                }
        }
    }

}