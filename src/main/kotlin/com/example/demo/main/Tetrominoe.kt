package com.example.demo.main

import javafx.scene.input.KeyCode
import javafx.scene.paint.Color

//represents a shape in the game; there are 7 specific types
abstract class Tetromino(val c: Color, var upperLeftCorner: Cell = Cell(3, -3)) {

    //the current rotation of the shape; there are 4 possible value based on the cardinal directions
    var rotation: Int = 0

    //the cells forming the shape
    var currentCells: MutableList<Cell> = mutableListOf()

    //updates the currentCells list based on the rotation value
    abstract fun renderShape()

    //updates the rotation value
    fun rotate() {
        rotation = if (rotation == 3) 0 else rotation + 1
    }

    //moves the shape to the given direction by 1
    fun moveShape(direction: KeyCode) {
        when(direction) {
            KeyCode.DOWN -> upperLeftCorner.y++
            KeyCode.LEFT -> upperLeftCorner.x--
            KeyCode.RIGHT -> upperLeftCorner.x++
            else -> return
        }
    }

    //true if the shape has cells in the bottom row of the board
    fun isOnBottom(bottomRow: Int) : Boolean {
        for (c in currentCells) {
            if (c.y >= bottomRow)
                return true
        }
        return false
    }

    //true if the shape has cells in the rightmost column of the board
    fun isOnRight(rightBorder: Int) : Boolean {
        for (c in currentCells) {
            if (c.x >= rightBorder)
                return true
        }
        return false
    }

    //true if the shape has cells in the leftmost column of the board
    fun isOnLeft() : Boolean {
        for (c in currentCells) {
            if (c.x <= 0)
                return true
        }
        return false
    }

    //true if the shape sticks out of the board, it is used for deciding if rotation is possible
    fun isOutsideOfBoard(w: Int, h: Int) : Boolean {
        for (c in currentCells) {
            if (c.x < 0 || c.x >= w || c.y >= h)
                return true
        }
        return false
    }
}

//represents an empty shape, it is used when game stops
class Empty : Tetromino(c = Color.BLACK) {
    override fun renderShape() {
        currentCells = mutableListOf()
    }
}

//the 7 possible shapes; each has different cells

class I : Tetromino(c = Color.AQUAMARINE) {
    override fun renderShape() {
        currentCells = when(rotation) {
            0, 2 -> mutableListOf(
                    Cell(upperLeftCorner.x, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 3, upperLeftCorner.y + 1, c))
            1, 3 -> mutableListOf(
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 3, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y, c))
            else -> throw Exception("Invalid rotation value!")
        }
    }
}

class O : Tetromino(c = Color.YELLOW) {
    override fun renderShape() {
        currentCells = when(rotation) {
            0, 1, 2, 3 -> mutableListOf(
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y, c))
            else -> throw Exception("Invalid rotation value!")
        }
    }
}

class T : Tetromino(c = Color.MAGENTA) {
    override fun renderShape() {
        currentCells = when(rotation) {
            0 -> mutableListOf(
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 1, c))
            1 -> mutableListOf(
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y, c))
            2 -> mutableListOf(
                    Cell(upperLeftCorner.x, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y, c))
            3 -> mutableListOf(
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y, c))
            else -> throw Exception("Invalid rotation value!")
        }
    }
}

class J : Tetromino(c = Color.BLUE) {
    override fun renderShape() {
        currentCells = when(rotation) {
            0 -> mutableListOf(
                    Cell(upperLeftCorner.x, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x, upperLeftCorner.y, c))
            1 -> mutableListOf(
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y, c))
            2 -> mutableListOf(
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 1, c))
            3 -> mutableListOf(
                    Cell(upperLeftCorner.x + 0, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y, c))
            else -> throw Exception("Invalid rotation value!")
        }
    }
}

class L : Tetromino(c = Color.ORANGE) {
    override fun renderShape() {
        currentCells = when(rotation) {
            0 -> mutableListOf(
                    Cell(upperLeftCorner.x, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 1, c))
            1 -> mutableListOf(
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x, upperLeftCorner.y, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y, c))
            2 -> mutableListOf(
                    Cell(upperLeftCorner.x, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y, c))
            3 -> mutableListOf(
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y, c))
            else -> throw Exception("Invalid rotation value!")
        }
    }
}

class S : Tetromino(c = Color.GREEN) {
    override fun renderShape() {
        currentCells = when(rotation) {
            0, 2 -> mutableListOf(
                    Cell(upperLeftCorner.x, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 1, c))
            1, 3 -> mutableListOf(
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y, c))
            else -> throw Exception("Invalid rotation value!")
        }
    }
}

class Z : Tetromino(c = Color.RED) {
    override fun renderShape() {
        currentCells = when(rotation) {
            0, 2 -> mutableListOf(
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c))
            1, 3 -> mutableListOf(
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 2, c),
                    Cell(upperLeftCorner.x + 1, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y + 1, c),
                    Cell(upperLeftCorner.x + 2, upperLeftCorner.y, c))
            else -> throw Exception("Invalid rotation value!")
        }
    }
}