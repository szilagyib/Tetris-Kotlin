package com.example.demo

import com.example.demo.main.MainView
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.stage.Stage
import tornadofx.App
import kotlin.system.exitProcess

class MyApp : App(MainView::class, Styles::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.onCloseRequest = EventHandler {
            Platform.exit()
            exitProcess(0)
        }
    }
}

fun main(args: Array<String>) {
    //disable thread checks because the view change should be called from the controller's side
    System.setProperty("glass.disableThreadChecks", "true")
    Application.launch(MyApp::class.java, *args)
}