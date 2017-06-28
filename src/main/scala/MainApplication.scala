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
import scalafx.stage.FileChooser.ExtensionFilter


object MainApplication extends JFXApp {

  case class DialogResult(path: String, color: String)

  val sources = (0 until 16).map(_ => ".\\src\\main\\resources\\silence.mp3").toList

  val dialog = new Dialog[DialogResult]() {
    initOwner(stage)
    title = "Customize pane"
    headerText = "Select Color and Select SourceFile"
  }

  val path = Label("File not selected")

  val selectFileButton = new Button("Select File")
  val colorPicker = new ColorPicker(Color.Green)

  val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  val cancelButtonType = new ButtonType("Cancel", ButtonData.CancelClose)

  val grid = new GridPane() {
    hgap = 10
    vgap = 10
    padding = Insets(20, 100, 10, 10)

    add(Label("Select file:"), 0, 0)
    add(path, 0, 2)
    add(selectFileButton, 1, 0)
    add(Label("Pick color:"), 0, 1)
    add(colorPicker, 1, 1)
  }

  val keyList: List[KeyCode] = fillKeyList()
  val paneList: List[Pane] = (0 until 16).map(_ => Pane(
    ".\\src\\main\\resources\\silence.mp3",
    new Button("\t\t\n\n\n\n\n"),
    "#e6e6e6"
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

  dialog.dialogPane().buttonTypes = Seq(okButtonType, cancelButtonType)

  selectFileButton.onAction = (e: ActionEvent) => {
    val fileChooser = new FileChooser
    fileChooser.getExtensionFilters.addAll(
      new ExtensionFilter("Audio Files", Seq("*.wav", "*.mp3", "*.aac"))
    )
    path.text.value = try {
      fileChooser.showOpenDialog(stage).getAbsolutePath
    }
    catch {
      case e: NullPointerException => path.text.value
    }
  }

  dialog.dialogPane().content = grid

  dialog.resultConverter = dialogButton =>
    if (dialogButton == okButtonType) {
      DialogResult(path.text.value, colorPicker.value.value.toString)
    }
    else
      null

  stage = new PrimaryStage {
    title = "Launchpad"
    scene = new Scene {
      val buttonGrid = generateGrid()
      fill = White
      content = new HBox {
        children = Seq(buttonGrid)
      }

      paneList.foreach(p => p.my_button.onAction = (event: ActionEvent) => {
        path.text = p.my_path
        val result = dialog.showAndWait()
        result match {
          case Some(DialogResult(le_path, color)) =>
            p.setButtonColor(color.stripPrefix("0x").stripSuffix("ff").trim)
            if (le_path != "File not selected") p.my_mediaPlayer = p.setPlayer(le_path)
            path.text = p.my_path
          case _ => ;
        }
      })

      onKeyPressed = (event: KeyEvent) => keyCodeToPaneMap.contains(event.code) match {
        case true =>
          keyCodeToPaneMap(event.code).restartPlayer()
          keyCodeToPaneMap(event.code).my_button.arm()
          keyCodeToPaneMap(event.code).lightButton()
        case false => if (event.code == KeyCode.Escape) stage.close()
      }
      onKeyReleased = (event: KeyEvent) => if (keyCodeToPaneMap.contains(event.code)) {
        keyCodeToPaneMap(event.code).my_button.disarm()
        keyCodeToPaneMap(event.code).dimButton()
      }
    }
  }

}