package com.example.tictactoe

import android.os.Bundle
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

    private var isCrossesMove = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        model = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(binding.root)
        addCells()
    }

    private fun addCells() {
        repeat(9) { binding.gridLayout.addView(getCell(it)) }
    }

    private fun getCell(id: Int): Button {
        val cell = layoutInflater.inflate(R.layout.btn_cell, null) as Button
        cell.setOnClickListener {
            model.mark(id, isCrossesMove)
            if (isCrossesMove) {
                changeBackground(cell, R.drawable.btn_cell_crosses, "X")
            } else {
                changeBackground(cell, R.drawable.btn_cell_noughts, "O")
            }
            cell.isClickable = false
            if (model.hasWon(isCrossesMove)) Toast.makeText(this, "Won!", Toast.LENGTH_LONG).show()
            isCrossesMove = !isCrossesMove
        }
        return cell
    }

    private fun changeBackground(button: Button, drawable: Int, text: String) {
        button.background = AppCompatResources.getDrawable(this, drawable)
        button.text = text
    }
}