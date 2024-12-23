package com.example.esemkarecipes

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val myVp : ViewPager2 = findViewById(R.id.viewPager2)
        val nav_bottom : BottomNavigationView = findViewById(R.id.nav_menu)
        myVp.adapter = tab_adapter(this)
        val color = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(-android.R.attr.state_selected)
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.red),
                ContextCompat.getColor(this, R.color.gray)
            )
        )
        nav_bottom.itemIconTintList = color
        nav_bottom.itemTextColor = color
        nav_bottom.setOnItemSelectedListener{ item ->
           myVp.currentItem = when(item.itemId){
               R.id.categories -> 0
               R.id.myProfile -> 1
               else -> 0
           }
            true
        }
        myVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                nav_bottom.menu.getItem(position).isChecked = true
            }
        })
        myVp.offscreenPageLimit = 2
    }
}