import pt.isel.canvas.Canvas

/*
* Definição do tipo de dados Game,
* caracterizado por um valor do tipo Dimension que determina o tamanho do jogo (largura e altura),
* um valor do tipo Man relativo ao objeto homem,
* três listas de objetos do tipo Position, correspondentes às paredes, às caixas e aos alvos,
* um valor inteiro indicativo do nível do jogo atual,
* um valor inteiro que contabiliza o número de movimentos dados pelo objeto homem
* e uma lista de Triplos, constituídos por uma posição (do homem), uma lista de posições (das caixas) e
* uma direção (do homem), que guarda a informação relativa a cada movimento dado no jogo.
 */
data class Game(
    val dim: Dimension,
    val man: Man,
    val walls: List<Position>,
    val boxes: List<Position>,
    val targets: List<Position>,
    val level: Int,
    val moves: Int,
    val undo: List<Triple<Position, List<Position>, Direction>>
)

/*
* Função de extensão de Game responsável pelo movimento do objeto Man;
* Recebe como parâmetro um objeto do tipo Direction, correspondente à direção a tomar para o movimento;
* Retorna um novo objeto Game com a nova posição do homem caso esta não coincida com a posição de uma parede
* ou de uma caixa nõo movimentável.
* Esta função incrementa ainda o valor "moves" por 1
* e guarda a posição atual do homem e das caixas, bem como a direção tomada pelo homem na lista "undo".
 */
fun Game.moveMan(dir: Direction) =
    if (walls.checkCollision(man.move(dir).pos).isEmpty() &&
        ((this.man.pos.moveBox(dir) !in this.pushBox(dir).boxes) ||
                (this.man.pos.detectObject2Steps(dir) !in this.walls)
                && (this.man.pos.detectObject2Steps(dir) !in this.boxes)
        )
    )
        Game(this.dim, this.man.move(dir), this.walls, this.boxes, this.targets, this.level, this.moves + 1, this.undo.update(this.man.pos, this.boxes, dir))
    else
        Game(this.dim, Man(this.man.pos, dir), this.walls, this.boxes, this.targets, this.level, this.moves, this.undo)


/*
* Função de extensão de Game, responsável pelo movimento das caixas;
* Recebe como parâmetro um objeto do tipo Direction, correspondente à direção a tomar para o movimento;
* Primeiro é feita a verificação de qual das caixas vai ser empurrada pelo homem com base na sua localização;
* Retorna um novo objeto Game com a nova posição da caixa caso esta não coincida com a posição de uma parede
 */
fun Game.pushBox(dir: Direction): Game {
    val box = this.boxes.checkCollision(this.man.pos)
    return if (box.isNotEmpty()
        && walls.checkCollision(box[0].moveBox(dir)).isEmpty()
        && boxes.checkCollision(box[0].moveBox(dir)).isEmpty()) {
        Game(this.dim,
            Man(this.man.pos, this.man.dir, push = true),
            this.walls,
            this.boxes.map { boxPos -> if (boxPos == box[0]) boxPos.moveBox(dir) else boxPos},
            this.targets,
            this.level,
            this.moves,
            this.undo)
    }
    else this
}

/*
* Função de extensão de Game, responsável pela alteração do nível do jogo;
* Recebe como parâmetro um objeto do tipo inteiro, correspondente ao nível do jogo;
* Retorna um novo objeto Game com o mapa do novo nível caso este novo nível não seja o primeiro.
 */
fun Game.changeLevel (level: Int) =
    if (level in 1..levels.size)
        this.copy(
            man = Man((levels[level-1].positionOfType(Type.MAN)),Direction.RIGHT),
            walls = levels[level-1].positionsOfType(Type.WALL),
            boxes = levels[level-1].positionsOfType(Type.BOX),
            targets = levels[level-1].positionsOfType(Type.TARGET),
            level = level,
            moves = 0,
            undo = emptyList<Triple<Position, List<Position>, Direction>>()
        )
    else
        this

/*
* Função de extensão de Game, responsável pela validação do movimento do homem;
* Recebe como parâmetro um objeto do tipo Direction, correspondente à direção a tomar para o movimento;
* Primeiro é verificado se as caixas não se encontram todas nos devidos alvos;
* Se a verificação retornar verdadeiro, retorna um novo objeto Game,
* consequência da chamada às funções de extensão de Game moveMan e pushBox;
* Caso as caixas já se encontrem todas nos alvos, o jogo mantém-se em modo de pausa.
 */
fun Game.tryMove(direction: Direction) =
    if (this.boxes.sortedBy { it.col }.sortedBy { it.line }
        != this.targets.sortedBy { it.col }.sortedBy { it.line })
        this.moveMan(direction).pushBox(direction)
    else
        this

/*
* Função de extensão de Canvas que desenha o jogo;
* Recebe como parâmetro um objeto do tipo Game;
* Começa por apagar o Canvas e de seguida desenha todos os objetos que o compõem:
* paredes (walls), alvos (targets), caixas (boxes) e homem (man), bem como a barra de estado do jogo.
 */
fun Canvas.drawGame(game: Game) {
    this.erase()
    this.drawWalls(game.walls)
    this.drawTargets(game.targets, game.walls)
    this.drawBoxes(game.boxes, game.targets, game.walls)
    this.drawMan(game.man, game.walls)
    this.drawScore(game)
}