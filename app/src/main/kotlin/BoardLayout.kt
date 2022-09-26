class BoardLayout(private var mines: Int, private val height: Int, private val width: Int) {
    private var mineLocations: Array<CharArray> = Array(height) { CharArray(width) }

    init {
        setBoard()
    }

    fun getHeight(): Int {
        return this.height
    }
    fun getWidth(): Int {
        return this.width
    }

    fun getValue(row: Int, col: Int): Char {
        return mineLocations[row][col]
    }

    fun printBoard() {
        // Print out mine_locations
        for (i in 0 until height) {
            for (j in 0 until width) {
                print(getValue(i, j).toString() + "  ")
            }
            println(" ")
        }
    }

    private fun isX(row: Int, col: Int): Boolean {
        return getValue(row, col) == 'X'
    }

    private fun setBoard() {
        // Mine Placement in mine_locations
        var randrow: Int
        var randcol: Int
        while (mines > 0) {
            randrow = (Math.random() * height).toInt()
            randcol = (Math.random() * width).toInt()
            // System.out.println(randrow + " " + randcol);
            if (mineLocations[randrow][randcol] != 'X') {
                mineLocations[randrow][randcol] = 'X'
                mines--
            }
        }

        for (i in 0 until height) {
            for (j in 0 until width) {
                if (mineLocations[i][j] != 'X') {
                    // Utility for counting mines around a square
                    val adjacentMines: Int
                    fun countMines(coords: List<Pair<Int, Int>>): Int {
                        return coords.fold(0) { minesSoFar: Int, coord: Pair<Int, Int> ->
                            if (isX(coord.first, coord.second)) {
                                minesSoFar + 1
                            } else {
                                minesSoFar
                            }
                        }
                    }

                    val coords: List<Pair<Int, Int>> =
                        // First Row
                        if (i == 0) {
                            // Top Left Corner
                            when (j) {
                                0 -> {
                                    listOf(Pair(0, 1), Pair(1, 0), Pair(1, 1))
                                }
                                // Top Right Corner
                                width - 1 -> {
                                    listOf(Pair(0, j - 1), Pair(1, j - 1), Pair(0, j))
                                }
                                // Top Edge
                                else -> {
                                    listOf(
                                        Pair(0, j - 1),
                                        Pair(1, j - 1),
                                        Pair(1, j),
                                        Pair(1, j + 1),
                                        Pair(0, j + 1)
                                    )
                                }
                            }
                        }
                        // Bottom Row
                        else if (i == height - 1) {
                            // Bottom Left Corner
                            when (j) {
                                0 -> {
                                    listOf(Pair(i - 1, 0), Pair(i - 1, 1), Pair(i, 1))
                                }
                                // Bottom Right Corner
                                width - 1 -> {
                                    listOf(Pair(i, j - 1), Pair(i - 1, j - 1), Pair(i - 1, j))
                                }
                                // Bottom Edge
                                else -> {
                                    listOf(
                                        Pair(i, j - 1),
                                        Pair(i - 1, j - 1),
                                        Pair(i - 1, j),
                                        Pair(i - 1, j + 1),
                                        Pair(i, j + 1)
                                    )
                                }
                            }
                        }
                        // Left Edge
                        else if (j == 0 && i != height - 1) {
                            listOf(
                                Pair(i - 1, 0),
                                Pair(i - 1, 1),
                                Pair(i, 1),
                                Pair(i + 1, 1),
                                Pair(i + 1, 0)
                            )
                        }
                        // Right Edge
                        else if (j == width - 1 && i != height - 1) {
                            listOf(
                                Pair(i + 1, j),
                                Pair(i + 1, j - 1),
                                Pair(i, j - 1),
                                Pair(i - 1, j - 1),
                                Pair(i - 1, j)
                            )
                        }
                        // Interior
                        else {
                            listOf(
                                Pair(i - 1, j),
                                Pair(i - 1, j + 1),
                                Pair(i, j + 1),
                                Pair(i + 1, j + 1),
                                Pair(i + 1, j),
                                Pair(i + 1, j - 1),
                                Pair(i, j - 1),
                                Pair(i - 1, j - 1)
                            )
                        }
                    adjacentMines = countMines(coords)
                    mineLocations[i][j] = (adjacentMines + 48).toChar()
                }
            }
        }
    }
}

// helper to visualize board creation
fun main() {
    val board = BoardLayout(20, 10, 15)

    // Print out nearby_mines
    board.printBoard()
}
