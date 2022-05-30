package com.example.tictactoe

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val field = Array<Boolean?>(9) { null }

    fun mark(index: Int, isCross: Boolean) {
        field[index] = isCross
    }

    fun hasWon(isCross: Boolean): Boolean {
        val length = 3
        return checkRows(isCross, length, length - 1)
    }

    private fun checkDiagonal(isCross: Boolean, length: Int): Boolean {
        val indices = getDiagonal(0, length * length, length + 1)
        for (i in indices) if (field[i] != isCross) return false
        return true
    }

    private fun checkReverseDiagonal(isCross: Boolean, length: Int): Boolean {
        val indices = getDiagonal(length - 1, (length - 1) * length + 1, length - 1)
        for (i in indices) if (field[i] != isCross) return false
        return true
    }

    private fun checkRow(isCross: Boolean, length: Int, row: Int): Boolean {
        val indices = getRow(row, length)
        for (i in indices) if (field[i] != isCross) return false
        return true
    }

    private fun checkRows(isCross: Boolean, length: Int, row: Int): Boolean {
        if (row == 0) return checkRow(isCross, length, row)
        return checkRows(isCross, length, row - 1) || checkRow(isCross, length, row)
    }

    private fun getDiagonal(start: Int, end: Int, step: Int): List<Int> {
        val list = mutableListOf<Int>()
        for (i in start until end step step) list.add(i)
        return list
    }

    private fun getRow(row: Int, length: Int): List<Int> {
        val list = mutableListOf<Int>()
        repeat(length) { print("${(row * length) + it}\t") }
        return list
    }

    private fun getColumn(column: Int, length: Int): List<Int> {
        val list = mutableListOf<Int>()
        repeat(length) { print("${it * length + column}\t") }
        return list
    }

    private fun printField() {
        val text = ""
        repeat(3) {
            repeat(3) {

            }
        }
    }
}