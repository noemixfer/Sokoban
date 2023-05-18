import pt.isel.canvas.*

/*
* Definição do tipo de dados Man,
* caracterizado por um valor do tipo Position relativo à posição do objeto,
* um valor do tipo Direction relativo à direção tomada pelo objeto
* e um valor booleano relativo ao estado do objeto (a empurrar ou não)
 */
data class Man(val pos: Position, val dir: Direction, val push: Boolean = false)

// Constantes correspondentes aos objectos Man
const val SPRITE_MAN_W = 241 / 6
const val SPRITE_MAN_H = 272 / 5
const val SPRITE_MAN_H_D = 272 / 5 + 1
const val MAN_W = SQUARE_W
const val MAN_H = SQUARE_H

/*
* Função de extensão de Man, responsável pela determinação da nova posição do homem;
* Recebe como parâmetro um objeto do tipo Direction correspondente à direção a ser tomada;
* De acordo com a direção passada, a função devolve um novo objeto Man com a nova posição,
* obtida através da soma/subtração de uma coluna/linha à posição atual do objeto Man.
 */
fun Man.move(dir: Direction) =
    when (dir) {
        Direction.RIGHT -> Man(this.pos + Position(1, 0), dir)
        Direction.LEFT -> Man(this.pos - Position(1, 0), dir)
        Direction.UP -> Man(this.pos - Position(0, 1), dir)
        Direction.DOWN -> Man(this.pos + Position(0, 1), dir)

    }

/*
* Função de extensão de Canvas que desenha o objeto correspondente ao homem;
* Recebe como parâmetro um objeto do tipo Man,
* caracterizado por um Sprite realizado pela função de extensão getImage()
* e a posição x, y do objeto no Canvas.
 */
fun Canvas.drawMan(man:Man, walls: List<Position>) =
    this.drawImage(
        man.getImage(),
        (man.pos.col + walls.levelOffset().width) * SQUARE_W,
        (man.pos.line + walls.levelOffset().height) * SQUARE_H
    )

/*
* Função de extensão de Man que determina o Sprite do ficheiro 'soko' presente na pasta resources
* com base no booleano 'push' do objeto Man e na direção tomada pelo mesmo.
 */
fun Man.getImage() =
    if (this.push){
        when(this.dir) {
            Direction.RIGHT -> "soko|${3 * SPRITE_MAN_W},$SPRITE_MAN_H,$MAN_W,$MAN_H"
            Direction.DOWN -> "soko|${3 * SPRITE_MAN_W},${2 * SPRITE_MAN_H_D},$MAN_W,$MAN_H"
            Direction.LEFT -> "soko|${3 * SPRITE_MAN_W},${3 * SPRITE_MAN_H},$MAN_W,$MAN_H"
            Direction.UP -> "soko|${3 * SPRITE_MAN_W},0,$MAN_W,$MAN_H"
        }
    }else{
        when(this.dir) {
            Direction.RIGHT -> "soko|$SPRITE_MAN_W,$SPRITE_MAN_H,$MAN_W,$MAN_H"
            Direction.DOWN -> "soko|$SPRITE_MAN_W,${2 * SPRITE_MAN_H_D},$MAN_W,$MAN_H"
            Direction.LEFT -> "soko|$SPRITE_MAN_W,${3 * SPRITE_MAN_H},$MAN_W,$MAN_H"
            Direction.UP -> "soko|$SPRITE_MAN_W,0,$MAN_W,$MAN_H"
        }

    }

