import java.io.File

import scalafx.scene.control.Button
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.scene.paint.Color

class Pane(path : String, button : Button, color : String){

  var my_path = path
  var my_button = button
  var my_color = color
  var my_mediaPlayer = setPlayer(path)

  def lightButton() = {
    my_button.style = "-fx-background-color: " + my_color + ";"
  }


  def setButtonColor(col : String): Unit ={
    my_color = "#" + col
  }

  def unlightButton():Unit = {
      my_button.style = ".button"
  }

  def setPlayer(path : String): MediaPlayer ={
    new MediaPlayer(new Media(new File(path).toURI.toString))
  }

  def restartPlayer(): Unit = {
    my_mediaPlayer.stop()
    my_mediaPlayer.play()
  }
}