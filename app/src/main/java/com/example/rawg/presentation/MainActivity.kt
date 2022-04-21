package com.example.rawg.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.featureGames.presentation.GamesFragment
import com.example.rawg.R
import com.example.rawg.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportFragmentManager.beginTransaction().add(binding.navHostFragment.id, GamesFragment())
            .commit()
    }
}

