import java.awt.Color
import java.awt.EventQueue
import java.awt.GridLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.UIManager

class Minesweeper {
    private val start: JFrame = JFrame()
    private var game: JFrame? = null
    var mines = 0

    init {
        // Setup of start window
        start.setBounds(100, 100, 300, 100)
        start.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        start.contentPane.layout = GridLayout(1, 3, 0, 0)
        val easy = JButton("Easy")
        start.contentPane.add(easy)
        val medium = JButton("Medium")
        start.contentPane.add(medium)
        val hard = JButton("Hard")
        start.contentPane.add(hard)
        start.isVisible = true
        easy.addActionListener {
            mines = 10
            val board = BoardLayout(mines, 9, 9)
            createWindow(board)
        }
        medium.addActionListener {
            mines = 40
            val board = BoardLayout(mines, 16, 16)
            createWindow(board)
        }
        hard.addActionListener {
            mines = 99
            val board = BoardLayout(mines, 16, 30)
            createWindow(board)
        }
    }

    private fun createWindow(board: BoardLayout) {
        // setup of game window
        game = JFrame()
        game!!.setBounds(0, 0, 50 * board.getWidth(), 50 * board.getHeight())
        game!!.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        game!!.contentPane.layout = GridLayout(board.getHeight(), board.getWidth(), 0, 0)
        val buttons = Array(board.getHeight()) { arrayOfNulls<JButton>(board.getWidth()) }

        // creating all the buttons
        for (i in 0 until board.getHeight()) {
            for (j in 0 until board.getWidth()) {
                val button = JButton()
                button.background = Color.BLUE
                buttons[i][j] = button

                // left click to expose
                button.addActionListener {
                    if (board.getValue(i, j) == 'X') {
                        JOptionPane.showMessageDialog(null, "You hit a mine, you lose!")
                        start.isVisible = true
                        game!!.isVisible = false
                    } else {
                        if (board.getValue(i, j) == '0') {
                            floodFill(board, i, j, buttons)
                        } else {
                            buttons[i][j]!!.text = "" + board.getValue(i, j)
                            buttons[i][j]!!.background = Color.CYAN
                        }
                    }
                }
                // right click to set flag
                button.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        if (e.button == MouseEvent.BUTTON3) {
                            buttons[i][j]!!.background = Color.RED
                            if (board.getValue(i, j) == 'X') {
                                buttons[i][j]!!.isEnabled = false
                                mines--
                                if (mines == 0) {
                                    JOptionPane.showMessageDialog(null, "Congratulations, you win!")
                                    start.isVisible = true
                                    game!!.isVisible = false
                                }
                            }
                        }
                    }
                })
                game!!.contentPane.add(button)
            }
        }
        start.isVisible = false
        game!!.isVisible = true
    }

    private fun floodFill(board: BoardLayout, i: Int, j: Int, buttons: Array<Array<JButton?>>) {
        if (buttons[i][j]!!.background !== Color.CYAN) {
            buttons[i][j]!!.text = "" + board.getValue(i, j)
            buttons[i][j]!!.background = Color.CYAN
            if (board.getValue(i, j) == '0') {
                // First Row
                if (i == 0) {
                    // Top Left Corner
                    when (j) {
                        0 -> {
                            floodFill(board, 0, 0 + 1, buttons)
                            floodFill(board, 0 + 1, 0 + 1, buttons)
                            floodFill(board, 0, 0, buttons)
                        }

                        board.getWidth() - 1 -> {
                            floodFill(board, 1, board.getWidth() - 1, buttons)
                            floodFill(board, 1, board.getWidth() - 2, buttons)
                            floodFill(board, 0, board.getWidth() - 2, buttons)
                        }

                        else -> {
                            floodFill(board, 0, j + 1, buttons)
                            floodFill(board, 1, j + 1, buttons)
                            floodFill(board, 1, j, buttons)
                            floodFill(board, 1, j - 1, buttons)
                            floodFill(board, 0, j - 1, buttons)
                        }
                    }
                } else if (i == board.getHeight() - 1) {
                    // Bottom Left Corner
                    when (j) {
                        0 -> {
                            floodFill(board, board.getHeight() - 2, 0, buttons)
                            floodFill(board, board.getHeight() - 2, 1, buttons)
                            floodFill(board, board.getHeight() - 1, 1, buttons)
                        }

                        board.getWidth() - 1 -> {
                            floodFill(board, board.getHeight() - 1, board.getWidth() - 2, buttons)
                            floodFill(board, board.getHeight() - 2, board.getWidth() - 2, buttons)
                            floodFill(board, board.getHeight() - 2, board.getWidth() - 1, buttons)
                        }

                        else -> {
                            floodFill(board, board.getHeight() - 1, j - 1, buttons)
                            floodFill(board, board.getHeight() - 2, j - 1, buttons)
                            floodFill(board, board.getHeight() - 1, j, buttons)
                            floodFill(board, board.getHeight() - 2, j + 1, buttons)
                            floodFill(board, board.getHeight() - 1, j + 1, buttons)
                        }
                    }
                } else if (j == 0 && i != board.getHeight() - 1) {
                    floodFill(board, i - 1, 0, buttons)
                    floodFill(board, i - 1, 1, buttons)
                    floodFill(board, i, 1, buttons)
                    floodFill(board, i + 1, 1, buttons)
                    floodFill(board, i + 1, 0, buttons)
                } else if (j == board.getWidth() - 1 && i != board.getHeight() - 1) {
                    floodFill(board, i + 1, board.getWidth() - 1, buttons)
                    floodFill(board, i + 1, board.getWidth() - 2, buttons)
                    floodFill(board, i, board.getWidth() - 2, buttons)
                    floodFill(board, i - 1, board.getWidth() - 2, buttons)
                    floodFill(board, i - 1, board.getWidth() - 1, buttons)
                } else {
                    floodFill(board, i - 1, j, buttons)
                    floodFill(board, i - 1, j + 1, buttons)
                    floodFill(board, i, j + 1, buttons)
                    floodFill(board, i + 1, j + 1, buttons)
                    floodFill(board, i + 1, j, buttons)
                    floodFill(board, i + 1, j - 1, buttons)
                    floodFill(board, i, j - 1, buttons)
                    floodFill(board, i - 1, j - 1, buttons)
                }
            }
        }
    }
}

fun main() {
    EventQueue.invokeLater {
        try {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Minesweeper()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
