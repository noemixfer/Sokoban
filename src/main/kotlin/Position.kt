/*
* Definição do tipo de dados Position,
* caracterizado por dois valores inteiros correspondentes à coluna e à linha da grelha do jogo
 */
data class Position(val col:Int, val line:Int)

/*
* Sobrecarga de operador 'plus',
* que faz a soma dessa posição com outra passada por parâmetro.
 */
operator fun Position.plus (p:Position) =
    Position(this.col + p.col,this.line + p.line)

/*
* Sobrecarga de operador 'minus',
* que faz a subtração dessa posição com outra passada por parâmetro.
 */
operator fun Position.minus (p:Position) =
    Position(this.col - p.col,this.line - p.line)

/*
* Função de extensão de Position, responsável pela determinação da nova posição da caixa a ser empurrada;
* Recebe como parâmetro um objeto do tipo Direction correspondente à direção a ser tomada;
* De acordo com a direção passada, a função devolve um novo objeto Position,
* obtida através da soma/subtração de uma coluna/linha à posição atual do objeto.
 */
fun Position.moveBox(dir: Direction): Position =
    when (dir) {
        Direction.RIGHT -> Position(this.col + 1, this.line)
        Direction.LEFT -> Position(this.col - 1, this.line)
        Direction.UP -> Position(this.col, this.line - 1)
        Direction.DOWN -> Position(this.col, this.line + 1)
    }

/*
* Função de extensão de Position, responsável pela determinação da posição do espaço a seguir à caixa
* após esta ser empurrada;
* Recebe como parâmetro um objeto do tipo Direction correspondente à direção a ser tomada pela caixa;
* De acordo com a direção passada, a função devolve um novo objeto Position,
* obtida através da soma/subtração de uma coluna/linha à posição atual do objeto.
 */
fun Position.detectObject2Steps(dir: Direction): Position =
    when (dir) {
        Direction.RIGHT -> Position(this.col + 2, this.line)
        Direction.LEFT -> Position(this.col - 2, this.line)
        Direction.UP -> Position(this.col, this.line - 2)
        Direction.DOWN -> Position(this.col, this.line + 2)
    }

/*
* Função de extensão de uma lista de objetos do tipo Position
* Recebe como parâmetro um objeto do tipo Position correspondente à posição a ser avaliada;
* Retorna uma lista de objetos do tipo Position que coincidam com a posição passada em parâmetro.
 */
fun List<Position>.checkCollision(pos: Position): List<Position> =
    this.filter { it == pos }

/*
* Função de extensão de Position, que verifica se algum dos alvos lhe corresponde;
* Recebe como parâmetro uma lista de objetos do tipo Position correspondentes aos alvos;
* É utilizada para verificar se uma dada caixa se encontra em algum dos alvos;
 */
fun Position.onTarget(targets: List<Position>): Boolean {
    return targets.any { it == this }
}

/*
* Função de extensão de uma lista de objetos do tipo Position
* Calcula a largura e altura do nível do jogo.
* Retorna um objeto do tipo Dimension,
* composto por metade da diferença entre a largura do Canvas e a largura do nível do jogo
* e por metade da diferença entre a altura do Canvas e a altura do nível do jogo.
 */
fun List<Position>.levelOffset(): Dimension {
    val levelWidth = this.maxBy { it.col } - this.minBy { it.col }
    val levelHeight = this.maxBy { it.line } - this.minBy { it.line }
    return Dimension((COLUMNS - (levelWidth.col + 1)) / 2, (LINES - (levelHeight.line + 1)) / 2)
}

/*
* Função de extensão de uma lista de Triplos, constituídos por uma posição, uma lista de posições e uma direção;
* Recebe como parâmetros uma posição (do homem),
* uma lista de posições (das caixas) e uma direção (do homem);
* É utilizada para adicionar os valores dos parâmetros na lista chamadora da função;
 */
fun List<Triple<Position, List<Position>, Direction>>.update(manPos: Position, boxesPos: List<Position>, manDir: Direction): List<Triple<Position, List<Position>, Direction>> =
    this + Triple(manPos, boxesPos, manDir)

/*
* Função de extensão de uma lista de Triplos, constituídos por uma posição, uma lista de posições e uma direção;
* É utilizada para retirar o último triplo da lista;
 */
fun List<Triple<Position, List<Position>, Direction>>.remove(): List<Triple<Position, List<Position>, Direction>> =
    this - this[this.size - 1]