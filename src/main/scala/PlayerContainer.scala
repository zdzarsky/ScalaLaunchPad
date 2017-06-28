import java.io.File

import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.media._

/**
  * Created by Michał Zakrzewski on 2017-06-26.
  */
class PlayerContainer(sources: List[String], keys: List[KeyCode]) {
  val players = collection.mutable.Map[KeyCode, MediaPlayer]()
  for (pair <- keys zip sources) {
    players += pair._1 -> new MediaPlayer(new Media(new File(pair._2).toURI.toString))
  }

  def play(event: KeyEvent): Unit = {
    try {
      playerRestart(players(event.code))
    } catch {
      case e: NoSuchElementException => throw new NoSuchElementException
    }
  }

  def playerFromOption(player: Option[MediaPlayer]): MediaPlayer = player match {
    case Some(p) => p
    case _ => throw new NoSuchElementException
  }

  def playerRestart(player: MediaPlayer): Unit = {
    player.stop()
    player.play()
  }

  def changeButtonSoundFile(button: KeyCode, source: String): Unit = {
    val file = new File(source)
    if (source.endsWith(".mp3") || source.endsWith(".wav")) file.exists() match {
      case true => players.update(button, new MediaPlayer(new Media(new File(source).toURI.toString)))
      case _ => throw new NoSuchElementException
    }
    else throw new MatchError
    //TODO in MainApplication show toast when somebody wants to change to nonexistent file or wrong type
  }

}
