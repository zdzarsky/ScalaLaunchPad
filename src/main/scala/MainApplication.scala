import java.io.File

import com.sun.glass.ui.Application.EventHandler

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer
import scalafx.Includes._
import scalafx.animation.PauseTransition
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.value.ObservableValue
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent}
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.scene.media._
import scalafx.scene.paint.Color._



object MainApplication extends JFXApp {


  val keyList: List[KeyCode] = fillKeyList()
  val buttonList: List[Button] = (0 until 16).map(_ => new Button{
    text = "               \n\n\n\n\n"
    layoutX = 500
    layoutY = 500
  }).toList

  val keycodeToButtonMap = (keyList zip buttonList).toMap


  val BUTTON_PADDING = 4
  val BUTTON_SIZE = 50
  val BUTTON_GRID = 4

  def serveButtonAction(button: Button): Unit = {
    button.onKeyPressed = (event : KeyEvent) => button.arm()
    button.onKeyReleased = (event : KeyEvent) => button.disarm()
  }

  def fillKeyList(): List[KeyCode] = {
    List(KeyCode.Digit1, KeyCode.Digit2, KeyCode.Digit3, KeyCode.Digit4,
      KeyCode.Q, KeyCode.W, KeyCode.E, KeyCode.R,
      KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.F,
      KeyCode.Z, KeyCode.X, KeyCode.C, KeyCode.V)
  }

  def generateGrid(): GridPane = {
    val grid = new GridPane {
      margin = Insets(150)
      hgap = BUTTON_PADDING.asInstanceOf[Double]
      vgap = BUTTON_PADDING.asInstanceOf[Double]
    }
    buttonList.foreach(b => grid.add(b, buttonList.indexOf(b) % 4 + 1, buttonList.indexOf(b)/4 + 1))
    grid
  }

  def choosePane(event: KeyEvent): Unit = {
    keyList.contains(event.code) match {
      case true =>
        try {

          val button = keycodeToButtonMap(event.code)
          serveButtonAction(button)
          //playerContainer.play(event)
        } catch {
          case e: NoSuchElementException => println("No sound file for " + event.code.toString())
        }
      case false => if(event.code == KeyCode.Escape){
        stage.close()
      }
    }

  }

 // val sources =  List(".\\src\\main\\resources\\silence.mp3", ".\\src\\main\\resources\\track2.wav")
  val sources = (0 until 16).map(_ => ".\\src\\main\\resources\\silence.mp3").toList
  val playerContainer = new PlayerContainer(sources, keyList)

  stage = new PrimaryStage {
    title = "Launchpad"
    println(buttonList.size)
    scene = new Scene {
      val buttonGrid = generateGrid()
      fill = White
      content = new HBox {
        children = Seq(buttonGrid)
      }

      buttonList.foreach(b => b.onAction = (event : ActionEvent) => {

      })

      onKeyPressed = (event: KeyEvent) => {
        playerContainer.play(event)
        keycodeToButtonMap(event.code).arm()
        keycodeToButtonMap(event.code).style = "-fx-background-color:#90EE90;"

      }
      onKeyReleased = (event : KeyEvent) => {
        keycodeToButtonMap(event.code).disarm()
        keycodeToButtonMap(event.code).style =".button"
      }

    }
  }

}