package com.example.demo.main

import com.example.demo.Styles
import com.google.gson.Gson
import javafx.event.ActionEvent
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.paint.Color
import tornadofx.View
import tornadofx.vbox

import tornadofx.*
import java.io.File

class LeaderboardView(private val currentScore: Int) : View("Tetris") {

    private val file: File = File("src/main/resources/leaderboard.txt")

    private var players: MutableList<Player> = readPlayers(file)

    private val playerField: TextField = TextField()

    private val highScoresView: TableView<Player> = TableView(players.asObservable())

    private val submitBtn: Button = Button()

    override val root = vbox(alignment = Pos.CENTER) {
        background = Background(BackgroundFill(Color.BLACK, null, null))
        padding = insets(10.0, 10.0)
        spacing = 20.0

        label("Game Over") {
            addClass(Styles.end)
        }

        hbox(alignment = Pos.CENTER) {
            spacing = 10.0

            label("Player:") {
                addClass(Styles.player)
            }

            playerField.prefHeight = 30.0
            add(playerField)

            submitBtn.background = Background.EMPTY
            submitBtn.graphic = imageview("submit.png") {
                fitHeight = 30.0
                isPreserveRatio = true
            }

            submitBtn.action {
                players.add(Player(playerField.text, currentScore))
                players.sortBy { it.score.inv() }
                setPlayersPlace()
                keepFirstTen()
                writePlayers(file)
                highScoresView.refresh()
                highScoresView.isVisible = true
                submitBtn.isDisable = true
            }

            add(submitBtn)
        }

        highScoresView.readonlyColumn("Place", Player::place)
        highScoresView.readonlyColumn("Name", Player::name).remainingWidth()
        highScoresView.readonlyColumn("Score", Player::score)
        highScoresView.columnResizePolicy = SmartResize.POLICY
        highScoresView.fixedCellSize = 20.0
        highScoresView.prefHeight = 245.0
        highScoresView.isVisible = false

        add(highScoresView)

        button("New Game") {
            paddingTop = 20.0
            prefHeight = 50.0
            prefWidth = 200.0
            addClass(Styles.restart)
            action {
                replaceWith(MainView::class)
            }
        }

        addEventHandler(KeyEvent.KEY_PRESSED) {
            if (it.code == KeyCode.ENTER) {
                submitBtn.fireEvent(ActionEvent())
            }
        }
    }

    private fun setPlayersPlace() {
        for (p in players)
            p.place = players.indexOf(p) + 1
    }

    private fun keepFirstTen() {
        val iter = players.iterator()
        while(iter.hasNext()) {
            val p = iter.next()
            if (p.place > 10)
                iter.remove()
        }
    }

    private fun readPlayers(f: File) : MutableList<Player> {
        val p: MutableList<Player> = mutableListOf()
        f.forEachLine {
            p.add(Gson().fromJson(it, Player::class.java))
        }
        return p
    }

    private fun writePlayers(f: File) {
        var s = String()
        for (p in players)
            s += Gson().toJson(p, Player::class.java) + "\n"
        f.writeText(s)
    }
}