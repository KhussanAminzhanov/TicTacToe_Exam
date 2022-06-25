package com.example.tictactoe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    enum class GameState {
        CROSSES_WON, NOUGHTS_WON, DRAW, UNDEFINED
    }

    private val field = Array<Boolean?>(GRID_LENGTH * GRID_LENGTH) { null }

    private val _isCrossTurn = MutableLiveData(false)
    val isCrossTurn: LiveData<Boolean> = _isCrossTurn

    private val _isGameOver = MutableLiveData(GameState.UNDEFINED)
    val isGameOver: LiveData<GameState> = _isGameOver

    fun mark(index: Int) {
        _isCrossTurn.value = _isCrossTurn.value?.not()
        field[index] = _isCrossTurn.value
        hasWon()
        isDraw()
    }

    private fun hasWon() {
        if (isThereWinner()
        ) {
            _isGameOver.value = when (_isCrossTurn.value) {
                true -> GameState.CROSSES_WON
                false -> GameState.NOUGHTS_WON
                null -> return
            }
        }
    }

    private fun isThereWinner() : Boolean {
        return checkDiagonal()
                || checkReverseDiagonal()
                || checkRows(GRID_LENGTH - 1)
                || checkColumns(GRID_LENGTH - 1)
    }

    private fun isDraw() {
        if (isFieldFull() && !isThereWinner()) _isGameOver.value = GameState.DRAW
    }

    private fun isFieldFull() : Boolean {
        field.forEach { if (it == null) return false }
        return true
    }

    private fun getDiagonal(start: Int, end: Int, step: Int): List<Int> {
        val list = mutableListOf<Int>()
        for (i in start until end step step) list.add(i)
        return list
    }

    private fun checkDiagonal(): Boolean {
        val indices = getDiagonal(0, GRID_LENGTH * GRID_LENGTH, GRID_LENGTH + 1)
        for (i in indices) {
            if (field[i] != _isCrossTurn.value) return false
        }
        return true
    }

    private fun checkReverseDiagonal(): Boolean {
        val indices = getDiagonal(GRID_LENGTH - 1, (GRID_LENGTH - 1) * GRID_LENGTH + 1, GRID_LENGTH - 1)
        for (i in indices) {
            if (field[i] != _isCrossTurn.value) return false
        }
        return true
    }

    private fun getRow(row: Int): List<Int> {
        val list = mutableListOf<Int>()
        repeat(GRID_LENGTH) { list.add((row * GRID_LENGTH) + it) }
        return list
    }

    private fun checkRow(row: Int): Boolean {
        val indices = getRow(row)
        for (i in indices) {
            if (field[i] != _isCrossTurn.value) return false
        }
        return true
    }

    private fun checkRows(row: Int): Boolean {
        if (row == 0) return checkRow(row)
        return checkRows(row - 1) || checkRow(row)
    }

    private fun getColumn(column: Int): List<Int> {
        val list = mutableListOf<Int>()
        repeat(GRID_LENGTH) { list.add(it * GRID_LENGTH + column) }
        return list
    }

    private fun checkColumn(column: Int): Boolean {
        val indices = getColumn(column)
        for (i in indices) {
            if (field[i] != _isCrossTurn.value) return false
        }
        return true
    }

    private fun checkColumns(column: Int): Boolean {
        if (column == 0) return checkColumn(column)
        return checkColumns(column - 1) || checkColumn(column)
    }

    fun resetField() {
        field.forEachIndexed { index, _ -> field[index] = null }
        _isCrossTurn.value = false
        _isGameOver.value = GameState.UNDEFINED
    }

    companion object {
        const val GRID_LENGTH: Int = 3
    }
}