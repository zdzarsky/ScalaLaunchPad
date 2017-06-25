import java.io.File
import javafx.scene.input.KeyCode

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.input.{KeyEvent, MouseEvent}
import scalafx.scene.layout.GridPane
import scalafx.scene.media._
import scalafx.scene.paint.Color._

object MainApplication extends JFXApp {

  val BUTTON_PADDING = 4
  val BUTTON_SIZE = 50
  val BUTTON_GRID = 4

  def player_restart(player:MediaPlayer): Unit ={
    player.stop()
    player.play()
  }

  val bip: String = "C:\\Users\\Wojciech\\Desktop\\ScalaProject\\ScalaLaunchPad\\src\\main\\resources\\track1.wav"
  val bip1: String = "C:\\Users\\Wojciech\\Desktop\\ScalaProject\\ScalaLaunchPad\\src\\main\\resources\\track2.wav"

  val hit: Media = new Media(new File(bip).toURI.toString)
  val mediaPlayer: MediaPlayer = new MediaPlayer(hit)
  val mediaPlayer1: MediaPlayer = new MediaPlayer(new Media(new File(bip1).toURI.toString))

  stage = new PrimaryStage {
    title = "ScalaFX Hello World"
    scene = new Scene {
      fill = White
      val grid : GridPane = new GridPane()
      grid.setPadding(Insets(5))
      grid.setHgap(BUTTON_PADDING.asInstanceOf[Double])
      grid.setVgap(BUTTON_PADDING.asInstanceOf[Double])

      for(i <- 0 to BUTTON_GRID){
        for(j <- 0 to BUTTON_GRID){
          val button = new Button("Mah")
          button.layoutX = BUTTON_SIZE
          button.layoutX = BUTTON_SIZE
          grid.add(button, j, i)
        }
      }

      onKeyPressed = (event: KeyEvent) => { event.getCode match {
          case KeyCode.A => player_restart(mediaPlayer)
          case KeyCode.B => player_restart(mediaPlayer1)
        }
      }

      // new Alert(AlertType.Information, "Hello Dialogs!!!").showAndWait()
      }
  }
}