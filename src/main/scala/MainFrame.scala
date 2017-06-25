import java.io.File

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.input.MouseEvent
import scalafx.scene.media._
import scalafx.scene.paint.Color._

object MainFrame extends JFXApp {
  val bip: String = "..\\resources\\Ed Sheeran - Shape Of You.mp3"
  val hit: Media = new Media(new File(bip).toURI.toString)
  val mediaPlayer: MediaPlayer = new MediaPlayer(hit)
  stage = new PrimaryStage {
    title = "ScalaFX Hello World"
    scene = new Scene {
      fill = Black
      onMouseClicked = (event: MouseEvent) => mediaPlayer.play()
      // new Alert(AlertType.Information, "Hello Dialogs!!!").showAndWait()
    }
  }
}