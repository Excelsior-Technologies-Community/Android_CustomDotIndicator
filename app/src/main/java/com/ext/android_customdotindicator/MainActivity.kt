package com.ext.android_customdotindicator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.ext.primarydotindicator.PrimaryDotIndicator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val indicator = findViewById<PrimaryDotIndicator>(R.id.dotIndicator)

        // 3 sample images
        val images = listOf(
            android.R.drawable.ic_menu_camera,
            android.R.drawable.ic_menu_gallery,
            android.R.drawable.ic_menu_compass
        )

        // Set adapter
        viewPager.adapter = ImagePagerAdapter(images)

        // Attach indicator
        indicator.attachToViewPager(viewPager)
    }
}