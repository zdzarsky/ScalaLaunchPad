import java.io.File

import scalafx.scene.input.KeyCode
import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer
import scala.swing.event.ActionEvent
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent}
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.scene.media._
import scalafx.scene.paint.Color._


object MainApplication extends JFXApp {

  val buttonToMediaPlayerMap = new HashMap[Button, MediaPlayer]
  val keycodeToButtonMap = new HashMap[KeyCode, Button]
  val keyList : List[KeyCode] = fillKeyList()
  val buttonList : ListBuffer[Button] = new ListBuffer[Button]

  val BUTTON_PADDING = 4
  val BUTTON_SIZE = 50
  val BUTTON_GRID = 4

  def serveButtonAction(button : Button): Unit ={

  }

  def fillKeyList() : List[KeyCode] ={
      List(KeyCode.Digit1, KeyCode.Digit2, KeyCode.Digit3, KeyCode.Digit4,
        KeyCode.Q, KeyCode.W, KeyCode.E, KeyCode.R,
        KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.F,
        KeyCode.Z, KeyCode.X, KeyCode.C, KeyCode.V)
  }

  def generateGrid(): GridPane ={
     val grid = new GridPane {
      margin = Insets(150)
      hgap = BUTTON_PADDING.asInstanceOf[Double]
      vgap = BUTTON_PADDING.asInstanceOf[Double]
     }
    for (i <- 1 to BUTTON_GRID) {
      for (j <- 1 to BUTTON_GRID) {
        val button = new Button{

        }
        buttonList += button
        grid.add(button, i, j)
      }
    }
    grid
  }
  def choosePane(event : KeyEvent) : Unit = {
    event.code match {
      case KeyCode.A => player_restart(mediaPlayer)
      case KeyCode.B => player_restart(mediaPlayer1)
      case _ => ;
    }
  }

  def player_restart(player: MediaPlayer): Unit = {
    player.stop()
    player.play()
  }


  val bip: String = "C:\\Users\\Wojciech\\Desktop\\ScalaProject\\ScalaLaunchPad\\src\\main\\resources\\track1.wav"
  val bip1: String = "C:\\Users\\Wojciech\\Desktop\\ScalaProject\\ScalaLaunchPad\\src\\main\\resources\\track2.wav"


  val hit: Media = new Media(new File(bip).toURI.toString)
  val mediaPlayer: MediaPlayer = new MediaPlayer(hit)
  val mediaPlayer1: MediaPlayer = new MediaPlayer(new Media(new File(bip1).toURI.toString))


  stage = new PrimaryStage {
    title = "Launchpad"
    println(keyList.size)
    scene = new Scene {
      val buttonGrid = generateGrid()
      fill = White
      content = new HBox {
        children = Seq(buttonGrid)
      }

      onKeyPressed = (event: KeyEvent) => choosePane(event)


    }
  }

}