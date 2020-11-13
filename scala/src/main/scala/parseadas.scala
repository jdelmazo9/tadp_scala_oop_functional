import Dibujante.{dibujar, simplificar}
import parser.{DrawParsers, parserRectangulo}
import scalafx.scene.paint.Color
import tadp.Punto
import tadp.internal.TADPDrawingAdapter
import tree._

import scala.io.Source

case object parseadas extends App{
  //  println(satisfies(integer())(_ > 200).parse("250"))
  //  println(parserTraslacion.parse("a["))
  //  println(parserGrupo.parse("grupo(triangulo[200 @ 50, 101 @ 335, 299 @ 335],rectangulo[186 @ 248, 305 @ 380])"))
  //  println(parserColor.parse("color[30, 50, 130](rectangulo[186 @ 248, 305 @ 380])"))
  //  println(parserRectangulo.parse("rectangulo[186 @ 248, 305 @ 380]"))
  //  println(sepByCombinator(parserPunto, parserSep).parse("0 @ 100,1 @ 200]"))
  //  println(parserParametros(parserPunto, parserSep, 2).parse("[0 @ 100,1 @ 200]"))
  //  println(DrawParsers.parse("triangulo[0 @ 100,200 @ 300,150 @ 500]"))
  //  println(DrawParsers.parse("triangulo[0 @ 100,200 @ 300]"))
  //  println(DrawParsers.parse("triangulo[0 @ 100,200 @ 300,150 @ 500,150 @ 500]"))
  //  println(DrawParsers.parse("rectangulo[150 @ 100,200 @ 300]"))
  //  println(DrawParsers.parse("grupo(triangulo[200 @ 50, 101 @ 335, 299 @ 335],\n   rectangulo[150 @ 100,200 @ 300])"))
  //  DrawParsers.parseAndDraw("grupo(\n   triangulo[200 @ 50, 101 @ 335, 299 @ 335],\n   circulo[200 @ 350, 100]\n)")
  //  DrawParsers.parseAndDraw("escala[2,1](grupo(\n   triangulo[200 @ 50, 101 @ 335, 299 @ 335],\n   circulo[200 @ 350, 100]\n))")
  //  DrawParsers.parseAndDraw("color[30, 50, 130](\n       rectangulo[186 @ 248, 305 @ 380]\n   )")

  // Espadas
  //  DrawParsers.parseAndDraw("color[30, 50, 130](grupo(\n    grupo(\n   \t triangulo[250 @ 150, 150 @ 300, 350 @ 300],\n   \t triangulo[150 @ 300, 50 @ 450, 250 @ 450],\n   \t triangulo[350 @ 300, 250 @ 450, 450 @ 450]\n    ),\n    grupo(\n   \t rectangulo[460 @ 90, 470 @ 100],\n   \t rectangulo[430 @ 210, 500 @ 220],\n   \t rectangulo[430 @ 210, 440 @ 230],\n   \t rectangulo[490 @ 210, 500 @ 230],\n   \t rectangulo[450 @ 100, 480 @ 260]\n    )\n))")
  //  DrawParsers.parseAndDraw("grupo(grupo(\n    grupo(\n   \t triangulo[250 @ 150, 150 @ 300, 350 @ 300],\n   \t triangulo[150 @ 300, 50 @ 450, 250 @ 450],\n   \t triangulo[350 @ 300, 250 @ 450, 450 @ 450]\n    ),\n    grupo(\n   \t rectangulo[460 @ 90, 470 @ 100],\n   \t rectangulo[430 @ 210, 500 @ 220],\n   \t rectangulo[430 @ 210, 440 @ 230],\n   \t rectangulo[490 @ 210, 500 @ 230],\n   \t rectangulo[450 @ 100, 480 @ 260]\n    )\n))")
  //  DrawParsers.parseAndDraw("escala[1.45, 1.45](grupo(\n    grupo(\n   \t triangulo[250 @ 150, 150 @ 300, 350 @ 300],\n   \t triangulo[150 @ 300, 50 @ 450, 250 @ 450],\n   \t triangulo[350 @ 300, 250 @ 450, 450 @ 450]\n    ),\n    grupo(\n   \t rectangulo[460 @ 90, 470 @ 100],\n   \t rectangulo[430 @ 210, 500 @ 220],\n   \t rectangulo[430 @ 210, 440 @ 230],\n   \t rectangulo[490 @ 210, 500 @ 230],\n   \t rectangulo[450 @ 100, 480 @ 260]\n    )\n))")
  //  DrawParsers.parseAndDraw("escala[1.45, 1.45](\n grupo(\n   color[0, 0, 0](\n     rectangulo[0 @ 0, 400 @ 400]\n   ),\n   color[200, 70, 0](\n     rectangulo[0 @ 0, 180 @ 150]\n   ),\n   color[250, 250, 250](\n     grupo(\n       rectangulo[186 @ 0, 400 @ 150],\n       rectangulo[186 @ 159, 400 @ 240],\n       rectangulo[0 @ 159, 180 @ 240],\n       rectangulo[45 @ 248, 180 @ 400],\n       rectangulo[310 @ 248, 400 @ 400],\n       rectangulo[186 @ 385, 305 @ 400]\n    )\n   ),\n   color[30, 50, 130](\n       rectangulo[186 @ 248, 305 @ 380]\n   ),\n   color[250, 230, 0](\n       rectangulo[0 @ 248, 40 @ 400]\n   )\n )\n)")

  // Murcielago
  //  DrawParsers.parseAndDraw("escala[0.3,0.3](traslacion[550,0](rotacion[45](grupo(\n\tescala[1.2, 1.2](\n\t\tgrupo(\n\t\t\tcolor[0, 0, 80](rectangulo[0 @ 0, 600 @ 700]),\n\t\t\tcolor[255, 255, 120](circulo[80 @ 80, 50]),\n\t\t\tcolor[0, 0, 80](circulo[95 @ 80, 40])\n\t\t)\n\t),\n\tcolor[50, 50, 50](triangulo[80 @ 270, 520 @ 270, 300 @ 690]),\n\tcolor[80, 80, 80](triangulo[80 @ 270, 170 @ 270, 300 @ 690]),\n\tcolor[100, 100, 100](triangulo[200 @ 200, 400 @ 200, 300 @ 150]),\n\tcolor[100, 100, 100](triangulo[200 @ 200, 400 @ 200, 300 @ 400]),\n\tcolor[150, 150, 150](triangulo[400 @ 200, 300 @ 400, 420 @ 320]),\n\tcolor[150, 150, 150](triangulo[300 @ 400, 200 @ 200, 180 @ 320]),\n\tcolor[100, 100, 100](triangulo[150 @ 280, 200 @ 200, 180 @ 320]),\n\tcolor[100, 100, 100](triangulo[150 @ 280, 200 @ 200, 150 @ 120]),\n\tcolor[100, 100, 100](triangulo[400 @ 200, 450 @ 300, 420 @ 320]),\n\tcolor[100, 100, 100](triangulo[400 @ 200, 450 @ 300, 450 @ 120]),\n\tgrupo(\n\t\tescala[0.4, 1](\n\t\t\tcolor[0, 0,0](\n\t\t\t\tgrupo(\n\t\t\t\t\tcirculo[970 @ 270, 25],\n\t\t\t\t\tcirculo[530 @ 270, 25]\n\t\t\t\t)\n\t\t\t)\n\t\t)\n\t)\n))))")
  //  DrawParsers.parseAndDraw("grupo(grupo(triangulo[80 @ 270, 520 @ 270, 300 @ 690]),\n\tgrupo(triangulo[80 @ 270, 170 @ 270, 300 @ 690]),\n\tgrupo(triangulo[200 @ 200, 400 @ 200, 300 @ 150]),\n\tgrupo(triangulo[200 @ 200, 400 @ 200, 300 @ 400]),\n\tgrupo(triangulo[400 @ 200, 300 @ 400, 420 @ 320]),\n\tgrupo(triangulo[300 @ 400, 200 @ 200, 180 @ 320]),\n\tgrupo(triangulo[150 @ 280, 200 @ 200, 180 @ 320]),\n\tgrupo(triangulo[150 @ 280, 200 @ 200, 150 @ 120]),\n\tgrupo(triangulo[400 @ 200, 450 @ 300, 420 @ 320]),\n\tgrupo(triangulo[400 @ 200, 450 @ 300, 450 @ 120]))")

  // Red
  //  DrawParsers.parseAndDraw("color[100, 100, 100](\n  grupo(\n    color[0, 0, 0](\n      grupo(\n        color[201, 176, 55](\n          triangulo[0 @ 0, 650 @ 0, 0 @ 750]\n        ),\n        color[215, 215, 215](\n          triangulo[650 @ 750, 650 @ 0, 0 @ 750]\n        ),\n        color[255, 255, 255](\n          grupo(\n            rectangulo[230 @ 150, 350 @ 180],\n            rectangulo[110 @ 150, 470 @ 390]\n          )\n        ),\n        color[255, 0, 0](\n          grupo(\n            rectangulo[170 @ 60, 410 @ 150],\n            rectangulo[350 @ 60, 380 @ 180],\n            rectangulo[200 @ 60, 230 @ 180],\n            rectangulo[260 @ 300, 320 @ 330],\n            rectangulo[170 @ 390, 410 @ 480]\n          )\n        ),\n        rectangulo[200 @ 180, 380 @ 210],\n        rectangulo[230 @ 240, 260 @ 300],\n        rectangulo[320 @ 240, 350 @ 300],\n        rectangulo[200 @ 30, 380 @ 60],\n        rectangulo[170 @ 60, 200 @ 90],\n        rectangulo[380 @ 60, 410 @ 90],\n        rectangulo[140 @ 90, 170 @ 150],\n        rectangulo[410 @ 90, 440 @ 150],\n        rectangulo[110 @ 150, 200 @ 180],\n        rectangulo[110 @ 180, 170 @ 210],\n        rectangulo[140 @ 210, 170 @ 240],\n        rectangulo[80 @ 210, 110 @ 270],\n        rectangulo[110 @ 270, 170 @ 330],\n        rectangulo[110 @ 300, 200 @ 330],\n        rectangulo[80 @ 330, 110 @ 390],\n        rectangulo[110 @ 390, 200 @ 420],\n        rectangulo[140 @ 420, 170 @ 480],\n        rectangulo[200 @ 420, 260 @ 450],\n        rectangulo[320 @ 420, 380 @ 450],\n        rectangulo[260 @ 390, 320 @ 420],\n        rectangulo[170 @ 330, 410 @ 390],\n        rectangulo[170 @ 480, 260 @ 510],\n        rectangulo[260 @ 450, 320 @ 480],\n        rectangulo[320 @ 480, 410 @ 510],\n        rectangulo[410 @ 420, 440 @ 480],\n        rectangulo[380 @ 390, 470 @ 420],\n        rectangulo[470 @ 330, 500 @ 390],\n        rectangulo[380 @ 300, 470 @ 330],\n        rectangulo[410 @ 270, 470 @ 330],\n        rectangulo[470 @ 210, 500 @ 270],\n        rectangulo[410 @ 210, 440 @ 240],\n        rectangulo[410 @ 180, 470 @ 210],\n        rectangulo[380 @ 150, 470 @ 180],\n        rectangulo[380 @ 150, 470 @ 180]\n      )\n    )\n  )\n)")

  //  DrawParsers.interactiveScreen()
  val nodoHijo: Cuadrado = Cuadrado(Punto(10.0, 10.0),Punto(20.0,20.0))
  val nodoHijo2: Cuadrado = Cuadrado(Punto(30.0, 30.0),Punto(40.0,40.0))
  val color: Colorete = Colorete(nodoHijo, Color.rgb(200,0,0))
  val color2: Colorete = Colorete(nodoHijo2, Color.rgb(200,0,0))
  val escala: Escala = Escala(nodoHijo, 10.0, 10.0)
  val escala2: Escala = Escala(nodoHijo2, 10.0, 10.0)
  val escala3: Escala = Escala(nodoHijo2, 10.0, 10.0)


  val grupo: Grupo[Nodo] = Grupo(List(color, color2,escala))
  val grupo2: Grupo[Nodo] = Grupo(List(color, color2))
  val grupoEscala: Grupo[Nodo] = Grupo(List(escala, escala2, escala3))
  val grupoEscalaNoS: Grupo[Nodo] = Grupo(List(escala, escala2, escala3,color))


  println(simplificar(grupo))
  println(simplificar(grupo2))
  println(simplificar(grupoEscala))
  println(simplificar(grupoEscalaNoS))



  // Carpin
  //  println(Source.fromFile("C:\\Users\\Martin\\Documents\\TADP\\grupo12-2020-2c\\scala\\src\\main\\scala\\parser\\carpin.txt").mkString.trim)
    DrawParsers.parseAndDraw(Source.fromFile("C:\\Users\\jdelm\\OneDrive\\Escritorio\\elCARPINCHOBOSTERO.txt").mkString)

  //  println(DrawParsers.parse("escala[1.45, 1.45](\n grupo(\n   color[0, 0, 0](\n     rectangulo[0 @ 0, 400 @ 400]\n   ),\n   color[200, 70, 0](\n     rectangulo[0 @ 0, 180 @ 150]\n   ),\n   color[250, 250, 250](\n     grupo(\n       rectangulo[186 @ 0, 400 @ 150],\n       rectangulo[186 @ 159, 400 @ 240],\n       rectangulo[0 @ 159, 180 @ 240],\n       rectangulo[45 @ 248, 180 @ 400],\n       rectangulo[310 @ 248, 400 @ 400],\n       rectangulo[186 @ 385, 305 @ 400]\n    )\n   ),\n   color[30, 50, 130](\n       rectangulo[186 @ 248, 305 @ 380]\n   ),\n   color[250, 230, 0](\n       rectangulo[0 @ 248, 40 @ 400]\n   )\n )\n)"))

  //  DrawParsers.parseAndDraw("color[200, 0, 0](grupo(escala[1, 0.8](grupo(circulo[210 @ 250, 100],circulo[390 @ 250, 100])),rectangulo[200 @ 170, 400 @ 300],triangulo[113 @ 225, 487 @ 225, 300 @ 450]))")
  //  println(DrawParsers.parse("color[200, 0, 0](grupo(escala[1, 0.8](grupo(circulo[210 @ 250, 100],circulo[390 @ 250, 100])),rectangulo[200 @ 170, 400 @ 300],triangulo[113 @ 225, 487 @ 225, 300 @ 450]))"))

  //  val parserPartes = string("triangulo[") ~> (parserPunto <> (parserSep ~> parserPunto) <> (parserSep ~> parserPunto)) <~ char(']')
  //  println(parserPartes.parse("triangulo[0 @ 100,200 @ 300,150 @ 500]"))
  //  println(parserRectangulo.parse("rectangulo[150 @ 100,200 @ 300]"))
  //  println(parserRectangulo.parse("rectangulo[150 @ 100   ,   200 @ 300]"))
}