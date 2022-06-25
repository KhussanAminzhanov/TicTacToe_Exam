package com.example.tictactoe

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import com.example.tictactoe.MainViewModel.Companion.GRID_LENGTH
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var model: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gridLayoutField.columnCount = GRID_LENGTH
        binding.alert.btnRestartGame.setOnClickListener { restartGame() }

        model = ViewModelProvider(this)[MainViewModel::class.java]
        model.isCrossTurn.observe(this) {
            val isNotCrossTurn = model.isCrossTurn.value ?: false
            val whoTurn = if (isNotCrossTurn) "Noughts" else "Crosses"
            binding.textViewTurn.text = getString(R.string.turn, whoTurn)
        }

        model.isGameOver.observe(this) {
            Log.i("activity", "isGameOver observer called")
            if (it != MainViewModel.GameState.UNDEFINED) {
                val message = when (it) {
                    MainViewModel.GameState.CROSSES_WON -> getString(R.string.alert_message, "Crosses")
                    MainViewModel.GameState.NOUGHTS_WON -> getString(R.string.alert_message, "Noughts")
                    MainViewModel.GameState.DRAW -> "It is Draw!"
                    else -> ""
                }
                binding.alert.textViewAlertMessage.text = message
                binding.alert.linearLayoutCompatAlert.visibility = View.VISIBLE
                binding.gridLayoutField.forEach { button ->
                    (button as Button).isClickable = false
                }
            } else {
                binding.alert.linearLayoutCompatAlert.visibility = View.GONE
            }
        }

        addCells()
    }

    private fun restartGame() {
        model.resetField()
        binding.gridLayoutField.forEach {
            val button = it as Button
            button.isClickable = true
            button.background = AppCompatResources.getDrawable(this, R.drawable.btn_cell_normal)
            button.text = ""
        }
    }

    private fun addCells() {
        val numberCells = GRID_LENGTH * GRID_LENGTH
        repeat(numberCells) { binding.gridLayoutField.addView(getCell(it)) }
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
        return cell
    }

    private fun changeBackground(button: Button, drawable: Int, text: String) {
        button.background = AppCompatResources.getDrawable(this, drawable)
        button.text = text
    }
}