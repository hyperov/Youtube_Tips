package com.islam.hesn.youtubetips.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.islam.hesn.youtubetips.R
import com.islam.hesn.youtubetips.categories.view.CategoryListFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CategoryListFragment()).commit()
                    1 -> supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CategoryListFragment()).commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        setDefaultScreen()
    }

    private fun setDefaultScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CategoryListFragment()).commit()
    }
}