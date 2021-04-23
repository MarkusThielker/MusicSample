package de.markus_thielker.uist_musicplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.markus_thielker.uist_musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}