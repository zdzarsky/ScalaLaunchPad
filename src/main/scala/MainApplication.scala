import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._
import scalafx.stage.FileChooser


object MainApplication extends JFXApp {

  case class DialogResult(path: String, color: String)

  val dialog = new Dialog[DialogResult]() {
    initOwner(stage)
    title = "Customize pane"
    headerText = "Select Color and Select SourceFile"
  }

  val path = Label("File not selected")

  val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  val cancelButtonType = new ButtonType("Cancel", ButtonData.CancelClose)

  dialog.dialogPane().buttonTypes = Seq(okButtonType, cancelButtonType)

  val selectFileButton = new Button("Select File")
  val colorPicker = new ColorPicker(Color.Green)
  selectFileButton.onAction = (e: ActionEvent) => {
    val fileChooser = new FileChooser
    path.text = fileChooser.showOpenDialog(stage).getAbsolutePath
  }

  val grid = new GridPane() {
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 10)

    add(new Label("Select file:"), 0, 0)
    add(path, 0, 2)
    add(selectFileButton, 1, 0)
    add(new Label("Pick color:"), 0, 1)
    add(colorPicker, 1, 1)
  }

  dialog.dialogPane().content = grid


  dialog.resultConverter = dialogButton =>
    if (dialogButton == okButtonType) {
  println(path.text.value)
  new DialogResult(path.text.value, colorPicker.value.value.toString)
  }
  else
      null


  val keyList: List[KeyCode] = fillKeyList()
  val paneList: List[Pane] = (0 until 16).map(_ => new Pane(
    ".\\src\\main\\resources\\silence.mp3",
    new Button("              \n\n\n\n\n"),
    "#fafafa"
  )).toList


  val keyCodeToPaneMap = (keyList zip paneList).toMap


  val BUTTON_PADDING = 4
  val BUTTON_SIZE = 50
  val BUTTON_GRID = 4


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
    paneList.foreach(p => grid.add(p.my_button, paneList.indexOf(p) % 4 + 1, paneList.indexOf(p) / 4 + 1))
    grid
  }

  def choosePane(event: KeyEvent): Unit = {
    keyList.contains(event.code) match {
      case true =>
        try {

          //playerContainer.play(event)
        } catch {
          case e: NoSuchElementException => println("No sound file for " + event.code.toString())
        }
      case false => if (event.code == KeyCode.Escape) {
        stage.close()
      }
    }

  }

  // val sources =  List(".\\src\\main\\resources\\silence.mp3", ".\\src\\main\\resources\\track2.wav")
  val sources = (0 until 16).map(_ => ".\\src\\main\\resources\\silence.mp3").toList
  val playerContainer = new PlayerContainer(sources, keyList)

  stage = new PrimaryStage {
    title = "Launchpad"
    scene = new Scene {
      val buttonGrid = generateGrid()
      fill = White
      content = new HBox {
        children = Seq(buttonGrid)
      }

      paneList.foreach(p => p.my_button.onAction = (event: ActionEvent) => {
        val result = dialog.showAndWait()
        result match {
          case Some(DialogResult(le_path, color)) =>
            p.setButtonColor(color.stripPrefix("0x").stripSuffix("ff").trim)
            p.my_mediaPlayer = p.setPlayer(le_path)

          case None => println("Le")
        }
      })

      onKeyPressed = (event: KeyEvent) => {
        keyCodeToPaneMap(event.code).restartPlayer()
        keyCodeToPaneMap(event.code).my_button.arm()
        keyCodeToPaneMap(event.code).lightButton()
      }

      onKeyReleased = (event: KeyEvent) => {
        keyCodeToPaneMap(event.code).my_button.disarm()
        keyCodeToPaneMap(event.code).unlightButton()
      }

    }
  }

}