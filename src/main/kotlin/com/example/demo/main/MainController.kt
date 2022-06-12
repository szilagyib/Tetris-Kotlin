package com.example.demo.main

import javafx.scene.Node
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import tornadofx.*
import java.util.*

//the controller; implementing the game
class MainController : Controller() {

    private val boardScreen: MainView by inject()

    //the shape which the user controls at the moment
    private var currentShape: Tetromino = generateRandomShape()

    //the shapes on the board in a "fixed position"
    private var shapes: MutableList<Tetromino> = mutableListOf()

    private var timer: Timer = Timer()

    private var timerCancelled = false

    private var paused = false

    private var started = false

    private var score = 0

    private var speed: Long = 600

    //handles the user control
    fun handleKeyEvent(code: KeyCode) {
        if (paused) return
        when (code) {
            KeyCode.LEFT -> {
                if(!currentShape.isOnLeft() &&
                        !isShapeThere(currentShape.currentCells, "left")) {
                    currentShape.moveShape(code)
                    updateBoard()
                }
            }
            KeyCode.RIGHT -> {
                if(!currentShape.isOnRight(boardScreen.boardWidth-1) &&
                        !isShapeThere(currentShape.currentCells, "right")) {
                    currentShape.moveShape(code)
                    updateBoard()
                }
            }
            KeyCode.DOWN -> {
                moveDown()
            }
            KeyCode.UP -> {
                val oldCells = currentShape.currentCells
                val oldRotation = currentShape.rotation
                currentShape.rotate()
                currentShape.renderShape()
                if (!isShapeThere(currentShape.currentCells, "none") &&
                        !currentShape.isOutsideOfBoard(boardScreen.boardWidth, boardScreen.boardHeight)) {
                    currentShape.currentCells = oldCells
                    updateBoard()
                }
                else {
                    currentShape.currentCells = oldCells
                    currentShape.rotation = oldRotation
                }
            }
            else -> return
        }
    }

    //starts a new game
    fun startGame() {
        if (!started) {
            started = true
            if (timerCancelled) {
                timer = Timer()
                timerCancelled = false
                currentShape = generateRandomShape()
            }
            timer.scheduleAtFixedRate(MoveShapesTask { moveDown() }, 0, speed)
            boardScreen.player.play()
        }
    }

    //stops the current game; after restart a new game starts
    fun stopGame() {
        if (started) {
            started = false
            paused = false
            if(!timerCancelled) {
                timer.cancel()
                timer.purge()
                timerCancelled = true
            }
            currentShape = Empty()
            shapes = mutableListOf()
            clearBoard()
            boardScreen.player.stop()
            boardScreen.endGame()
            score = 0
            boardScreen.updateScore(score)
        }
    }

    fun pauseGame() {
        if (started && !paused) {
            paused = true
            timer.cancel()
            timer.purge()
            timerCancelled = true
            boardScreen.player.pause()
        }
    }

    fun resumeGame() {
        if (started && paused) {
            paused = false
            timer = Timer()
            timerCancelled = false
            timer.scheduleAtFixedRate(MoveShapesTask { moveDown() }, 0, speed)
            boardScreen.player.play()
        }
    }

    //updates the board when the current shape moves down by 1
    private fun updateBoard() {
        //the cells of the previous position should be cleared (black color)
        for (oldCell in currentShape.currentCells) {
            oldCell.color = Color.BLACK
            updateGridCell(oldCell)
        }
        //the cells of the new position should be colored according to the shapes type
        currentShape.renderShape()
        for (newCell in currentShape.currentCells)
            updateGridCell(newCell)
    }

    //recolours a specified cell of the board
    private fun updateGridCell(cell: Cell) {
        val n = getGridNode(cell)
        if (n != null)
            (n as Rectangle).fill = cell.color
    }

    //gives back the grid node which belongs to the specified cell
    private fun getGridNode(c: Cell) : Node? {
        val nodes = boardScreen.grid.getChildList()
        if (nodes != null) {
            for (n in nodes) {
                if (GridPane.getColumnIndex(n) == c.x && GridPane.getRowIndex(n) == c.y) {
                    return n
                }
            }
        }
        return null
    }

    //the automatic downward movement of the current shape
    private fun moveDown() {
        if (isShapeThere(currentShape.currentCells, "none"))
            stopGame()
        if (currentShape.isOnBottom(boardScreen.boardHeight - 1) ||
                isShapeThere(currentShape.currentCells, "down")) {
            shapes.add(currentShape)
            clearFullRows().let {
                moveAllShapeDown(it)
                score += it.size
                speed = if (1000 - score * 20 < 300) 300 else (1000 - score * 20).toLong()
                boardScreen.updateScore(score)
            }
            currentShape = generateRandomShape()
        }
        else {
            currentShape.moveShape(KeyCode.DOWN)
            updateBoard()
        }
    }

    //true if there is already a shape in the given direction
    private fun isShapeThere(cells: List<Cell>, dir: String) : Boolean {
        for (cell in cells) {
            for (s in shapes) {
                for (c in s.currentCells) {
                    when(dir) {
                        "none" -> if (c.x == cell.x && c.y == cell.y) return true
                        "down" -> if (c.x == cell.x && c.y == cell.y + 1) return true
                        "left" -> if (c.x == cell.x - 1 && c.y == cell.y) return true
                        "right" -> if (c.x == cell.x + 1 && c.y == cell.y) return true
                    }
                }
            }
        }
        return false
    }

    private fun generateRandomShape() : Tetromino {
        return when(Random().nextInt(7)) {
            0 -> I()
            1 -> O()
            2 -> T()
            3 -> J()
            4 -> L()
            5 -> S()
            6 -> Z()
            else -> throw Exception("Invalid shape type!")
        }
    }

    //clears all full rows and return the indexes of them
    private fun clearFullRows() : List<Int> {
        val clearedIdxList: MutableList<Int> = mutableListOf()
        for (i in 0 until boardScreen.boardHeight) {
            if (isRowFull(i)) {
                clearedIdxList.add(i)
                val shapesIter = shapes.iterator()
                while (shapesIter.hasNext()) {
                    val s = shapesIter.next()
                    val cellsIter = s.currentCells.iterator()
                    while(cellsIter.hasNext()){
                        val c = cellsIter.next()
                        if(c.y == i) {
                            updateGridCell(Cell(c.x, c.y, Color.BLACK))
                            cellsIter.remove()
                        }
                    }
                    if (s.currentCells.isEmpty())
                        shapesIter.remove()
                }
            }
        }
        return clearedIdxList
    }

    //after the full rows were cleared the shapes have to be moved down accordingly
    private fun moveAllShapeDown(clearedRows: List<Int>) {
        //the automatic downward movement of the current shape
        val rowIter = clearedRows.reversed().iterator()
        var cnt = 0
        while (rowIter.hasNext()) {
            val r = rowIter.next()
            val shapesIter = shapes.iterator()
            while (shapesIter.hasNext()) {
                val s = shapesIter.next()
                val cellsIter = s.currentCells.iterator()
                while(cellsIter.hasNext()){
                    val c = cellsIter.next()
                    if(c.y < r + cnt) {
                        updateGridCell(Cell(c.x, c.y, Color.BLACK))
                        val n = getGridNode(Cell(c.x, c.y+1))
                        if (n != null)
                            if((n as Rectangle).fill == Color.BLACK)
                                c.y++
                        updateGridCell(c)
                    }
                }
            }
            cnt++
        }
    }

    //true if the given row is full
    private fun isRowFull(y: Int) : Boolean {
        for(x in 0 until boardScreen.boardWidth) {
            val n = getGridNode(Cell(x, y))
            if (n != null)
                if((n as Rectangle).fill == Color.BLACK)
                    return false
        }
        return true
    }

    //fully clears the board; it is used when a game ends
    private fun clearBoard() {
        for (y in 0 until boardScreen.boardHeight)
            for (x in 0 until boardScreen.boardWidth)
                updateGridCell(Cell(x, y, Color.BLACK))
    }
}

//the timer task responsible for the automatic movement of the current shape
class MoveShapesTask(val f: () -> Unit) : TimerTask() {
    override fun run() {
        f()
    }
}