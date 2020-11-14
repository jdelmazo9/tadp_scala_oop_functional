package tadp.internal

import java.io.FileOutputStream
import java.time.LocalDateTime

import javafx.embed.swing.SwingFXUtils
import javax.imageio.ImageIO
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, Label, TextArea}
import scalafx.scene.image.WritableImage
import scalafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination}
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.{Group, Scene}

class TADPInteractiveDrawingScreen(dibujador: (String, TADPDrawingAdapter) => Any) extends JFXApp {
  var iterrrrrrrrrrrrr = 0
  val appWidth = 1200
  val appHeight = 800
  val paneSeparation = 20
  val contentWidth = appWidth - paneSeparation * 2
  val contentHeight = appHeight - paneSeparation * 2

  var canvas = new Canvas(contentWidth / 2, contentHeight - 60)
  var adapter = TADPDrawingAdapter(canvas)
  val canvasPane = new HBox()

  def setupCanvas() = {
    canvas = new Canvas(contentWidth / 2, contentHeight - 60)
    adapter = TADPDrawingAdapter(canvas)
    canvasPane.children = canvas
    canvasPane.margin = Insets(0, 0, 0, paneSeparation)
    canvasPane.maxHeight = canvas.height.value
    canvasPane.border = new Border(new BorderStroke(Color.Gray,
      BorderStrokeStyle.Solid,
      new CornerRadii(0),
      new BorderWidths(1)))
  }

  setupCanvas()

  val textField = new TextArea() {
    prefWidth = contentWidth / 2 - paneSeparation
    maxHeight = canvas.height.value
  }

  val drawButton = new Button("Dibujar") {
    layoutX = 0
    onAction = { _ => dibujar() }
  }
  val saveButton = new Button("Guardar") {
    layoutX = 70
    onAction = { _ => guardar() }
  }
  val bailando2020Button = new Button("El bailarin") {
    layoutX = 145
    onAction = { _ => bailame() }
  }
  val ctrlEnter = new KeyCodeCombination(KeyCode.Enter, KeyCombination.ControlDown)
  val parseResultLabel = new Label("") {
    layoutX = 230
    layoutY = 5
  }

  stage = new PrimaryStage {
    title = "TADP Draw"
    scene = new Scene(appWidth, appHeight) {

      val border = new BorderPane
      border.padding = Insets(paneSeparation, paneSeparation, paneSeparation, paneSeparation)
      border.left = textField
      border.right = canvasPane
      border.bottom = new Group {
        children = Seq(drawButton, saveButton, bailando2020Button, parseResultLabel)
        margin = Insets(paneSeparation, 0, 0, 0)
      }

      onKeyPressed = { keyEvent => if (ctrlEnter.`match`(keyEvent)) dibujar() }

      root = border
    }
  }

  def info(mensaje: String): Unit = {
    parseResultLabel.text = mensaje
    parseResultLabel.textFill = Color.Black
  }

  def error(mensaje: String): Unit = {
    parseResultLabel.text = mensaje
    parseResultLabel.textFill = Color.Red
  }

  def limpiarMensaje(): Unit = {
    parseResultLabel.text = ""
  }

  def dibujar(callback: () => Unit = () => ()): Unit = {
    setupCanvas()
    limpiarMensaje()
    val descripcionDeImagen = textField.text.getValue

    try {
      dibujador(descripcionDeImagen, adapter)
      callback()
    } catch {
      case e => error(e.getMessage)
    }

  }

  def bailame(callback: () => Unit = () => ()): Unit = {
    setupCanvas()
    limpiarMensaje()
    var descripcionDeImagen = ""
    if (iterrrrrrrrrrrrr % 2 == 0) {
      descripcionDeImagen = "grupo(\n\tescala[1.2, 1.2](\n\t\tgrupo(\n\t\t\tcolor[0, 0, 80](rectangulo[0 @ 0, 600 @ 700]),\n\t\t\tcolor[255, 255, 120](circulo[80 @ 80, 50]),\n\t\t\tcolor[0, 0, 80](circulo[95 @ 80, 40])\n\t\t)\n\t),\n\tcolor[50, 50, 50](triangulo[80 @ 270, 520 @ 270, 300 @ 690]),\n\tcolor[80, 80, 80](triangulo[80 @ 270, 170 @ 270, 300 @ 690]),\n\trotacion[-5](\ngrupo(\n\tcolor[100, 100, 100](triangulo[200 @ 200, 400 @ 200, 300 @ 150]),\n\tcolor[100, 100, 100](triangulo[200 @ 200, 400 @ 200, 300 @ 400]),\n\tcolor[150, 150, 150](triangulo[400 @ 200, 300 @ 400, 420 @ 320]),\n\tcolor[150, 150, 150](triangulo[300 @ 400, 200 @ 200, 180 @ 320]),\n\tcolor[100, 100, 100](triangulo[150 @ 280, 200 @ 200, 180 @ 320]),\n\tcolor[100, 100, 100](triangulo[150 @ 280, 200 @ 200, 150 @ 120]),\n\tcolor[100, 100, 100](triangulo[400 @ 200, 450 @ 300, 420 @ 320]),\n\tcolor[100, 100, 100](triangulo[400 @ 200, 450 @ 300, 450 @ 120]),\n        color[250, 100, 0](triangulo[300@ 410, 250 @ 450, 350 @ 500]),\n        color[250, 30, 0](triangulo[320@ 420, 280 @ 490, 240 @ 490]),\n\ttraslacion[65,-35](color[0, 200, 0](rotacion[15](rectangulo[180@ 220, 240@ 225]))),\n\ttraslacion[125,75](color[0, 200, 0](rotacion[-15](rectangulo[180@ 220, 240@ 225]))),\n\tgrupo(\n\t\tescala[0.4, 1](\n\t\t\tgrupo(\n\t\t\tcolor[220, 0,0](\n\t\t\t\tgrupo(\n\t\t\t\t\tcirculo[970 @ 270, 25],\n\t\t\t\t\tcirculo[530 @ 270, 25]\n\t\t\t\t)\n\t\t\t),\n\t\t\tcolor[0, 0,0](\n\t\t\t\tgrupo(\n\t\t\t\t\tcirculo[970 @ 270, 15],\n\t\t\t\t\tcirculo[530 @ 270, 15]\n\t\t\t\t)\n\t\t\t))\n\t\t)\n\t)))\n)"
    }
    else {
      descripcionDeImagen = "grupo(\n\tescala[1.2, 1.2](\n\t\tgrupo(\n\t\t\tcolor[0, 0, 80](rectangulo[0 @ 0, 600 @ 700]),\n\t\t\tcolor[255, 255, 120](circulo[80 @ 80, 50]),\n\t\t\tcolor[0, 0, 80](circulo[95 @ 80, 40])\n\t\t)\n\t),\n\tcolor[50, 50, 50](triangulo[80 @ 270, 520 @ 270, 300 @ 690]),\n\tcolor[80, 80, 80](triangulo[80 @ 270, 170 @ 270, 300 @ 690]),\n\trotacion[5](\ngrupo(\n\tcolor[100, 100, 100](triangulo[200 @ 200, 400 @ 200, 300 @ 150]),\n\tcolor[100, 100, 100](triangulo[200 @ 200, 400 @ 200, 300 @ 400]),\n\tcolor[150, 150, 150](triangulo[400 @ 200, 300 @ 400, 420 @ 320]),\n\tcolor[150, 150, 150](triangulo[300 @ 400, 200 @ 200, 180 @ 320]),\n\tcolor[100, 100, 100](triangulo[150 @ 280, 200 @ 200, 180 @ 320]),\n\tcolor[100, 100, 100](triangulo[150 @ 280, 200 @ 200, 150 @ 120]),\n\tcolor[100, 100, 100](triangulo[400 @ 200, 450 @ 300, 420 @ 320]),\n\tcolor[100, 100, 100](triangulo[400 @ 200, 450 @ 300, 450 @ 120]),\n        color[250, 100, 0](triangulo[300@ 410, 250 @ 450, 350 @ 500]),\n        color[250, 30, 0](triangulo[320@ 420, 280 @ 490, 240 @ 490]),\n\ttraslacion[65,-35](color[0, 200, 0](rotacion[15](rectangulo[180@ 220, 240@ 225]))),\n\ttraslacion[125,75](color[0, 200, 0](rotacion[-15](rectangulo[180@ 220, 240@ 225]))),\n\tgrupo(\n\t\tescala[0.4, 1](\n\t\t\tgrupo(\n\t\t\tcolor[220, 0,0](\n\t\t\t\tgrupo(\n\t\t\t\t\tcirculo[970 @ 270, 25],\n\t\t\t\t\tcirculo[530 @ 270, 25]\n\t\t\t\t)\n\t\t\t),\n\t\t\tcolor[0, 0,0](\n\t\t\t\tgrupo(\n\t\t\t\t\tcirculo[970 @ 270, 15],\n\t\t\t\t\tcirculo[530 @ 270, 15]\n\t\t\t\t)\n\t\t\t))\n\t\t)\n\t)))\n)"
    }
    iterrrrrrrrrrrrr += 1


    try {
      dibujador(descripcionDeImagen, adapter)
      callback()
    } catch {
      case e => error(e.getMessage)
    }

  }

  def guardar(): Unit = {
    dibujar { () =>
      val image = new WritableImage(canvas.width.intValue(), canvas.height.intValue())
      canvas.snapshot(null, image)

      val nombreDeImagen = s"dibujo-${LocalDateTime.now()}.png".replace(":","")
      val nombreDeCarpeta = "out/"

      ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new FileOutputStream(nombreDeCarpeta + nombreDeImagen))
      info(s"Imagen ${nombreDeImagen} guardada en ${nombreDeCarpeta}")
    }
  }
}
