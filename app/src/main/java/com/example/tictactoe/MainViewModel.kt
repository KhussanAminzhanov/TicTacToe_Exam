package com.example.tictactoe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val length = 3
    private val field = Array<Boolean?>(length * length) { null }

    private val _isCrossTurn = MutableLiveData(false)
    val isCrossTurn: LiveData<Boolean> = _isCrossTurn

    private val _isGameOver = MutableLiveData(false)
    val isGameOver: LiveData<Boolean> = _isGameOver

    fun mark(index: Int) {
        _isCrossTurn.value = _isCrossTurn.value?.not()
        field[index] = _isCrossTurn.value
        hasWon()
    }

    private fun hasWon() {
        if (checkDiagonal()
            || checkReverseDiagonal()
            || checkRows(length - 1)
            || checkColumns(length - 1)
        ) {
            _isGameOver.value = true
        }
    }

    private fun getDiagonal(start: Int, end: Int, step: Int): List<Int> {
        val list = mutableListOf<Int>()
        for (i in start until end step step) list.add(i)
        return list
    }

    private fun checkDiagonal(): Boolean {
        val indices = getDiagonal(0, length * length, length + 1)
        for (i in indices) {
            if (field[i] != _isCrossTurn.value) return false
        }
        return true
    }

    private fun checkReverseDiagonal(): Boolean {
        val indices = getDiagonal(length - 1, (length - 1) * length + 1, length - 1)
        for (i in indices) {
            if (field[i] != _isCrossTurn.value) return false
        }
        return true
    }

    private fun getRow(row: Int): List<Int> {
        val list = mutableListOf<Int>()
        repeat(length) { list.add((row * length) + it) }
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
        repeat(length) { list.add(it * length + column) }
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
}