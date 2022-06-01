package com.example.tictactoe

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.forEach
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

        binding.alert.btnRestartGame.setOnClickListener { restartGame() }

        model.isCrossTurn.observe(this) {
            val isNotCrossTurn = model.isCrossTurn.value ?: false
            val whoTurn = if (isNotCrossTurn) "Noughts" else "Crosses"
            binding.textViewTurn.text = getString(R.string.turn, whoTurn)
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
        repeat(9) { binding.gridLayoutField.addView(getCell(it)) }
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
            Log.i("activity", "isGameOver observer called")
            if (it) {
                val winner = if (model.isCrossTurn.value!!) "Crosses" else "Noughts"
                binding.alert.textViewAlertMessage.text = getString(R.string.alert_message, winner)
                binding.alert.linearLayoutCompatAlert.visibility = View.VISIBLE
                cell.isClickable = false
            } else {
                binding.alert.linearLayoutCompatAlert.visibility = View.GONE
            }
        }

        return cell
    }

    private fun changeBackground(button: Button, drawable: Int, text: String) {
        button.background = AppCompatResources.getDrawable(this, drawable)
        button.text = text
    }
}