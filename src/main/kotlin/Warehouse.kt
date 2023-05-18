// Neste ficheiro ficam alojados os vários objetos do armazém

import pt.isel.canvas.BLACK
import pt.isel.canvas.CYAN
import pt.isel.canvas.Canvas

// Constantes correspondentes ao sprite dos objetos
const val SPRITE_W = 241 / 6
const val SPRITE_H = 272 / 5 + 1

// Constantes correspondentes aos objectos Wall
const val WALL_W = SQUARE_W
const val WALL_H = SQUARE_H

// Constantes correspondentes aos objectos Box
const val BOX_W = SQUARE_W
const val BOX_H = SQUARE_H

// Constantes correspondentes aos objectos Target
const val TARGET_W = SQUARE_W
const val TARGET_H = SQUARE_H

// Valores utilizados no desenho da barra de estado do jogo
val TEXT_Y_POS = (WAREHOUSE_H - 5)
val LEVEL_X_POS = SQUARE_W
val MOVES_X_POS = WAREHOUSE_W - 4 * SQUARE_W

/*
* Função de extensão de Canvas que desenha as paredes no Canvas;
* Recebe como parâmetro uma lista de objetos do tipo Position correspondente às paredes;
* Cada objeto da lista é desenhado tendo como critérios o Sprite do ficheiro 'soko' presente na pasta resources
* e a posição x, y do objeto no Canvas.
 */
fun Canvas.drawWalls(walls:List<Position>) {
    walls.forEach {
        this.drawImage(
            "soko|${SPRITE_W},${4 * SPRITE_H},$WALL_W,$WALL_H",
            (it.col + walls.levelOffset().width) * SQUARE_W,
            (it.line + walls.levelOffset().height) * SQUARE_H
        )
    }
}

/*
* Função de extensão de Canvas que desenha as caixas no Canvas;
* Recebe como parâmetros duas lista de objetos do tipo Position,
* uma correspondente às caixas e outra correspondente aos alvos;
* Cada objeto da lista é desenhado tendo em conta se esse objeto está ou não num dos alvos;
* De acordo com isso, é dado um Sprite diferente do ficheiro 'soko' presente na pasta resources;
* é também passada a posição x, y do objeto no Canvas.
 */
fun Canvas.drawBoxes(boxes:List<Position>, targets: List<Position>, walls: List<Position>) {
    boxes.forEach {
        if (!it.onTarget(targets))
            this.drawImage(
                "soko|${2 * SPRITE_W},${4 * SPRITE_H},$BOX_W,$BOX_H",
                (it.col + walls.levelOffset().width) * SQUARE_W,
                (it.line + walls.levelOffset().height) * SQUARE_H
            )
        else
            this.drawImage(
                "soko|${3 * SPRITE_W},${4 * SPRITE_H},$BOX_W,$BOX_H",
                (it.col + walls.levelOffset().width) * SQUARE_W,
                (it.line + walls.levelOffset().height) * SQUARE_H
            )
    }
}

/*
* Função de extensão de Canvas que desenha os alvos no Canvas;
* Recebe como parâmetro uma lista de objetos do tipo Position correspondente aos alvos;
* Cada objeto da lista é desenhado tendo como critérios o Sprite do ficheiro 'soko' presente na pasta resources
* e a posição x, y do objeto no Canvas.
 */
fun Canvas.drawTargets(targets:List<Position>, walls: List<Position>) {
    targets.forEach {
        this.drawImage(
            "soko|0,${4 * SPRITE_H},$TARGET_W,$TARGET_H",
            (it.col + walls.levelOffset().width) * SQUARE_W,
            (it.line + walls.levelOffset().height) * SQUARE_H
        )
    }
}

/*
* Função de extensão de Canvas que desenha a barra de estado do jogo;
* Recebe como parâmetro um objeto do tipo Game;
* É desenhado um retângulo abaixo da área do jogo,
* no qual é desenhado o texto relativo ao nível e ao número de movimentos atuais.
 */
fun Canvas.drawScore(game: Game){
    this.drawRect(0, WAREHOUSE_H - SQUARE_H/2, WAREHOUSE_W, SQUARE_H / 2, CYAN)
    this.drawText(LEVEL_X_POS, TEXT_Y_POS , "LEVEL: ${game.level} ", BLACK, 20) //level
    this.drawText(MOVES_X_POS, TEXT_Y_POS, "MOVES: ${game.moves} ", BLACK, 20) //moves
}
