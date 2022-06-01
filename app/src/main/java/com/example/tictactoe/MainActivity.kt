package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var model: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        model = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(binding.root)

        binding.alert.linearLayoutCompatAlert.visibility = View.GONE
        addCells()
    }

    private fun addCells() {
        repeat(9) { binding.gridLayout.addView(getCell(it)) }
    }

    private fun getCell(id: Int): Button {
        val cell = layoutInflater.inflate(R.layout.btn_cell, null) as Button
        cell.setOnClickListener {
            model.mark(id)
            if (model.isCrossTurn.value!!) {
                changeBackground(cell, R.drawable.btn_cell_crosses, "X")
            } else {
                changeBackground(cell, R.drawable.btn_cell_noughts, "O")
            }
            cell.isClickable = false
        }

        model.isGameOver.observe(this) {
            if (it) {
                val winner = if (model.isCrossTurn.value!!) "Crosses" else "Noughts"
                binding.alert.textViewAlertMessage.text = getString(R.string.alert_message, winner)
                binding.alert.linearLayoutCompatAlert.visibility = View.VISIBLE
                cell.isClickable = false
            }
        }

        return cell
    }

    private fun changeBackground(button: Button, drawable: Int, text: String) {
        button.background = AppCompatResources.getDrawable(this, drawable)
        button.text = text
    }
}