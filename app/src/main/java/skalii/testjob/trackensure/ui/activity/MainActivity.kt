package skalii.testjob.trackensure.ui.activity


import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat

import by.kirich1409.viewbindingdelegate.viewBinding

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

import skalii.testjob.trackensure.R
import skalii.testjob.trackensure.databinding.ActivityMainBinding
import skalii.testjob.trackensure.ui.adapter.ViewPagerAdapter
import skalii.testjob.trackensure.ui.fragment.PageRefillFragment
import skalii.testjob.trackensure.ui.fragment.PageStatisticFragment


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewBinding by viewBinding(ActivityMainBinding::bind, R.id.activity_main)

    private var authListener: AuthStateListener? = null
    private var auth: FirebaseAuth? = null

    private val fragmentList = listOf(PageRefillFragment(), PageStatisticFragment())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(ResourcesCompat.getColor(resources, R.color.color_accent, null))
        )

        auth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser

        authListener = AuthStateListener {
            if (it.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        setViewPager()

        viewBinding.fabActivityMain.setOnClickListener {
            startActivity(Intent(this, SaveRefillActivity::class.java))
        }

    }


    fun signOut() {
        auth!!.signOut()
    }

    override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener(authListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth!!.removeAuthStateListener(authListener!!)
        }
    }

    private fun setViewPager() {

        val tabLayout = viewBinding.tabActivityMain
        val viewPager = viewBinding.viewPagerActivityMain

        viewPager.offscreenPageLimit = 2
        viewPager.adapter = ViewPagerAdapter(fragmentList, supportFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.customView = createTabTextView(R.string.tab_refill)
                1 -> tab.customView = createTabTextView(R.string.tab_statistic)
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

        })
    }

    private fun createTabTextView(resId: Int) =
        TextView(this).apply {
            maxLines = 1
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            setAutoSizeTextTypeUniformWithConfiguration(
                10,
                16,
                1,
                TypedValue.COMPLEX_UNIT_SP
            )
            setText(resId)
            setTextAppearance(R.style.Text_Tab)
            setTypeface(typeface, Typeface.BOLD)
        }

}