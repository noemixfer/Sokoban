import pt.isel.canvas.*

/*
* UNIDADE CURRICULAR: PROGRAMAÇÃO
* DOCENTE: PAULA GRAÇA
* 3º TRABALHO DE GRUPO
*
* GRUPO 1
* Micaela Macatrão, nº 39734
* João Marques, nº 49212
* Noemi Ferreira, nº 49226
*
* JANEIRO DE 2023
*/

/*
* Constantes que guardam os códigos ASCII das teclas utilizadas para os KeyEvents.
 */
const val SPACE_CODE = 32
const val MINUS_CODE = 45
const val MINUS_NUMPAD_CODE = 109
const val LOWER_R_CODE = 'r'.code
const val UPPER_R_CODE = 'R'.code
const val BACKSPACE = 8

/*
* Definição do tipo enumerável de objetos Direction, que pode tomar um de quatro valores:
* UP, DOWN, LEFT, RIGHT (CIMA, BAIXO, ESQUERDA, DIREITA)
 */
enum class Direction { UP,DOWN,LEFT,RIGHT }

/*
* Constantes e valores que definem as dimensões do Canvas, as dimensões e a quantidade das quadrículas do jogo.
 */
val LINES = (levels.maxBy { it.height }).height
val COLUMNS = (levels.maxBy { it.width }).width
const val SQUARE_W = 40
const val SQUARE_H = 52
val WAREHOUSE_H = SQUARE_H * LINES + SQUARE_H/2
val WAREHOUSE_W = SQUARE_W * COLUMNS


fun main() {

    print("Begin ")
    onStart {
        // Declaração das variáveis que compõem o Canvas, o labirinto e o jogo (objeto do tipo Game)
        val warehouse = Canvas(WAREHOUSE_W, WAREHOUSE_H)
        val man = Man((levels[0].positionOfType(Type.MAN)), Direction.RIGHT)
        val walls = levels[0].positionsOfType(Type.WALL)
        val boxes = levels[0].positionsOfType(Type.BOX)
        val targets = levels[0].positionsOfType(Type.TARGET)

        /* Declaração da variável do tipo Game, composta pelas variáveis
        * man, walls, boxes e targets acima declarados,
        * pelo nível do jogo, o valor inicial de movimentos, a lista vazia do undo
        * e por um objeto do tipo Dimension que diz respeito ao tamanho do jogo,
        * com base no número de linhas e colunas.
         */
        var game = Game(Dimension(COLUMNS, LINES), man, walls, boxes, targets, 1, 0, emptyList())

        // Desenha o jogo no Canvas
        warehouse.drawGame(game)

        /*
        * Definição do comportamento do jogo quando uma tecla do teclado é pressionada,
        * caracterizado pelo movimento do homem e das caixas;
        */
        warehouse.onKeyPressed { keyEvent: KeyEvent ->
            when (keyEvent.code) {
            /*
            * Para as setas, é desenhado um novo objeto Game com base na execução da função tryMove;
             */
                RIGHT_CODE -> {
                    game = game.tryMove(Direction.RIGHT)
                    warehouse.drawGame(game)
                }
                LEFT_CODE -> {
                    game = game.tryMove(Direction.LEFT)
                    warehouse.drawGame(game)
                }
                UP_CODE -> {
                    game = game.tryMove(Direction.UP)
                    warehouse.drawGame(game)
                }
                DOWN_CODE -> {
                    game = game.tryMove(Direction.DOWN)
                    warehouse.drawGame(game)
                }
            /*
            * Para a barra de espaços é desenhado um novo objeto Game com o nível seguinte
            * caso as caixas se encontrem todas nos alvos, com recurso à função de extensão de Game changeLevel;
             */
                SPACE_CODE -> {
                    if (game.boxes.sortedBy { it.col }.sortedBy { it.line }
                        == game.targets.sortedBy { it.col }.sortedBy { it.line })
                        game = game.changeLevel(game.level + 1)
                    warehouse.drawGame(game)
                }
            /*
            * Para a tecla "menos" é desenhado um novo objeto Game com o nível anterior,
            * com recurso à função de extensão de Game changeLevel;
             */
                MINUS_CODE, MINUS_NUMPAD_CODE -> {
                    game = game.changeLevel(game.level - 1)
                    warehouse.drawGame(game)
                }
            /*
            * Para a tecla "R"/"r" é desenhado um novo objeto Game com o estado inicial do nível atual
            * com recurso à função de extensão de Game changeLevel;
             */
                LOWER_R_CODE, UPPER_R_CODE -> {
                    game = game.changeLevel(game.level)
                    warehouse.drawGame(game)
                }
            /*
            * Para a tecla backspace é desenhado um novo objeto Game, cópia do game,
            * sendo alterados os valores de man e boxes com base na última posição do homem e das caixas,
            * bem como à direção anterior tomada pelo homem, guardados no último elemento da lista undo;
            * é decrementado o número de movimentos e é retirado o último elemento da lista undo;
            * os restantes elementos do objeto game mantêm-se inalterados.
             */
                BACKSPACE -> {
                    if(game.undo.isNotEmpty()) {
                        game = game.copy(
                            man = Man(game.undo[game.undo.size - 1].first, game.undo[game.undo.size - 1].third),
                            boxes = game.undo[game.undo.size - 1].second,
                            moves = game.moves - 1,
                            undo = game.undo.remove())
                    }
                    warehouse.drawGame(game)
                }
            }
        }
    }
        onFinish {
            print("Finish ")
        }
        print("End ")
}