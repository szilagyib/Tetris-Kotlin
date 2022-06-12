package com.example.demo

import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val start by cssclass()
        val end by cssclass()
        val player by cssclass()
        val input by cssclass()
        val restart by cssclass()
    }

    init {
        heading {
            fontWeight = FontWeight.BOLD
            fontSize = 20.px
            textFill = Color.LIGHTGRAY
        }

        start {
            fontWeight = FontWeight.BOLD
            fontSize = 30.px
            textFill = Color.BLACK
            backgroundColor += Color.color(0.8275, 0.8275, 0.8275, 0.8)
            text {
                stroke = Color.BLUE
                strokeWidth = 1.px
            }
        }

        end {
            fontWeight = FontWeight.BOLD
            fontSize = 50.px
            textFill = Color.BLUE
            alignment = Pos.CENTER
        }

        player {
            fontWeight = FontWeight.BOLD
            fontSize = 20.px
            textFill = Color.LIGHTGRAY
        }

        input {
            padding = box(10.px)
            fontSize = 20.px
            textFill = Color.LIGHTGRAY
        }

        restart {
            backgroundColor += Color.TRANSPARENT
            borderColor += box(Color.LIGHTGREEN)
            fontWeight = FontWeight.BOLD
            fontSize = 30.px
            textFill = Color.LIGHTGREEN
        }

        button {
            padding = box(0.px)
        }

        tableView {
            borderColor += box(Color.LIGHTGRAY)
            borderWidth += box(1.px, 1.px, 1.px, 1.px)
            columnHeader {
                label {
                    textFill = Color.LIGHTGRAY
                }
                borderWidth += box(0.px, 1.px, 2.px, 1.px)
                borderColor += box(Color.LIGHTGRAY)
            }
            tableColumn {
                backgroundColor += Color.BLACK
                textFill = Color.LIGHTGRAY
                alignment = Pos.CENTER
            }
        }
    }
}