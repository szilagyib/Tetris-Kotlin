package com.example.demo.main

import com.example.demo.Styles
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.GridPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.converter.NumberStringConverter
import tornadofx.*
import java.io.File

class MainView : View("Tetris") {

    private val controller: MainController by inject()

    var grid: GridPane = GridPane()

    private var startLabel: Label = Label("Press ENTER to start!")

    private var model = object : ViewModel() {
        var score = SimpleIntegerProperty()
    }

    val player: MediaPlayer = MediaPlayer(Media(
            File("src/main/resources/korobeiniki.m4a").toURI().toString()))

    private val cellSize = 30.0

    val boardWidth = 10

    val boardHeight = 20

    override val root = hbox {
        prefHeight = 600.0
        prefWidth = 460.0
        spacing = 10.0
        background = Background(BackgroundFill(Color.BLACK, null, null))

        player.volume = 1.0
        player.cycleCount = MediaPlayer.INDEFINITE

        //gridpane
        padding = insets(10.0)
        for (y in 0 until boardHeight) {
            for (x in 0 until boardWidth) {
                val cell = Rectangle(cellSize, cellSize, Color.BLACK)
                cell.stroke = Color.LIGHTGRAY
                grid.add(cell, x, y)
            }
        }

        startLabel.padding = insets(5.0, 2.0)
        startLabel.addClass(Styles.start)

        stackpane {
            add(grid)
            add(startLabel)
        }

        vbox {
            paddingTop = 100
            hbox {
                spacing = 10.0
                label("Score:") {
                    addClass(Styles.heading)
                }
                text {
                    bind(model.score, converter = NumberStringConverter())
                    fill = Color.LIGHTGREEN
                    addClass(Styles.heading)
                }
            }
            hbox {
                spacing = 15.0
                paddingTop = 20
                button {
                    background = Background.EMPTY
                    graphic = imageview("pause.png") {
                        fitHeight = 30.0
                        isPreserveRatio = true
                    }
                    tooltip("pause")
                    action {
                        controller.pauseGame()
                    }
                }
                button {
                    background = Background.EMPTY
                    graphic = imageview("play.png") {
                        fitHeight = 30.0
                        isPreserveRatio = true
                    }
                    tooltip("resume")
                    action {
                        controller.resumeGame()
                    }
                }
                button {
                    background = Background.EMPTY
                    graphic = imageview("stop.png") {
                        fitHeight = 30.0
                        isPreserveRatio = true
                    }
                    tooltip("stop")
                    action {
                        controller.stopGame()
                    }
                }
            }
        }

        addEventHandler(KeyEvent.KEY_PRESSED) {
            if (it.code == KeyCode.ENTER) {
                controller.startGame()
                startLabel.isVisible = false
            }
            else controller.handleKeyEvent(it.code)
        }
    }

    fun updateScore(s: Int) {
        model.score.set(s)
    }

    fun endGame() {
        startLabel.isVisible = true
        replaceWith(LeaderboardView(model.score.value))
    }
}