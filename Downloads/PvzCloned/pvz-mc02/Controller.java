import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.JButton;

/**
 * Manages user interactions, game flow, and updates between the GUI and the game board.
 * This class handles mouse events for planting plants, collecting sun, and navigating
 * between different game screens (main menu, game board, game over).
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public class Controller extends MouseAdapter
{
    /** Flag indicating if a plant was successfully placed in the current interaction. */
    private boolean didPlant = false;
    /** Flag indicating if a plant selection button is currently active. */
    private boolean buttonSelected = false;
    /** The GUI instance managed by this controller. */
    private Gui gui;
    /** The game board instance managed by this controller. */
    private Board board;
    /** Flag indicating if the game is currently over. */
    private boolean gameOver;
    /** The player instance managing sun and plant placement. */
    private Player player;
    /** The last time the board was updated, used for delta time calculations. */
    private double lastBoardUpdate;
    /** The current game time in seconds. */
    private int gameTime;
    /** The index of the currently selected plant type. */
    private int plantNo;
    /** Timer for the main game animation loop. */
    private Timer animationTimer;
    /** The timestamp of the last frame update. */
    private long lastTimeUpdate;

    /**
     * Constructs a new Controller instance, linking it to the GUI and game board.
     * Initializes game over state.
     *
     * @param g The GUI instance.
     * @param b The game board instance.
     */
    public Controller(Gui g , Board b)
    {
        gui = g;
        board = b;
        gameOver = false;
    }

    /**
     * Adds mouse listeners to the plant selection buttons and the game board.
     * Handles plant selection, plant placement, and sun collection.
     */
    public void addMouseListeners()
    {
        // Mouse listener for plant selection buttons
        gui.getPlantSelectionPanel().getPlantButtons().forEach(btn ->
        {
            btn.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    // Always reset all buttons first
                    gui.getPlantSelectionPanel().resetSelectedButtons();

                    // If player has enough sun and cooldown is finished, set button as selected
                    if (btn.isReady())
                    {
                        btn.setIsSelected(true);
                        plantNo = btn.getPlantIndex();
                        buttonSelected = true;
                        System.out.println("> You selected " + btn.getPlantIndex());
                    }
                    else
                    {
                        plantNo = 0; // reset if not ready
                    }
                }
            });
        });

        // Mouse listener for clicking on the board
        gui.getBoardPanel().addMouseListener(new MouseAdapter()
        {
            /**
             * Handles mouse press events on the game board.
             * Attempts to place a plant if a button is selected, or collects sun.
             *
             * @param e The mouse event.
             */
            @Override
            public void mousePressed(MouseEvent e)
            {
                int laneNo = (int) e.getY()  / gui.getBoardPanel().getTileSizeY();
                int tileNo = (int) e.getX() / gui.getBoardPanel().getTileSizeX();

                // Only try to plant if there's a button selected
                if (buttonSelected)
                {
                    didPlant = player.tryToPlacePlant(plantNo, laneNo, tileNo);
                }

                // If player placed a plant, start the plant button's cooldown and remove its selected status
                if (didPlant)
                {
                    gui.getPlantSelectionPanel().getPlantButtons().forEach(btn ->
                    {
                        if (plantNo == btn.getPlantIndex())
                        {
                            btn.setClicked(); // starts cooldown
                            btn.setIsSelected(false);
                            System.out.println("> You planted " + plantNo);
                        }
                    });
                    buttonSelected = false;
                }
                // Else just remove selected status
                else
                {
                    gui.getPlantSelectionPanel().getPlantButtons().forEach(btn ->
                    {
                        if (plantNo == btn.getPlantIndex())
                        {
                            btn.setIsSelected(false);
                        }
                    });
                    buttonSelected = false;
                }

                System.out.printf("Clicked on Lane %d, Tile %d\n", (int) e.getY()  / gui.getBoardPanel().getTileSizeY() + 1, (int) e.getX() / gui.getBoardPanel().getTileSizeX() + 1);

                player.collectSun(board.getTile(laneNo, tileNo).getSunList());
                gui.getPlantSelectionPanel().updateSunCount(Player.getTotalSun());
                plantNo = 0; // Reset selected plant number
            }
        });
    }

    /**
     * Navigates the game to the main menu screen.
     * Sets up action listeners for level selection buttons.
     */
    public void goMainMenu()
    {
        // Show main menu first
        gui.showMainMenuPanel();

        // Get all main menu buttons
        JButton levelOneBtn = gui.getButton("levelOneButton");
        JButton levelTwoBtn = gui.getButton("levelTwoButton");
        JButton levelThreeBtn = gui.getButton("levelThreeButton");

        // Remove existing listeners and add new ones to prevent duplicate events
        if (levelOneBtn != null) {
            removeAllActionListeners(levelOneBtn);
            levelOneBtn.addActionListener(e -> {
                System.out.println("> Level one button clicked.");
                startGame(1);
            });
        }

        if (levelTwoBtn != null) {
            removeAllActionListeners(levelTwoBtn);
            levelTwoBtn.addActionListener(e -> {
                System.out.println("> Level two button clicked.");
                startGame(2);
            });
        }

        if (levelThreeBtn != null) {
            removeAllActionListeners(levelThreeBtn);
            levelThreeBtn.addActionListener(e -> {
                System.out.println("> Level three button clicked.");
                startGame(3);
            });
        }
    }

    /**
     * Removes all {@link ActionListener}s from a given {@link JButton}.
     * This is used to prevent duplicate event handling when re-assigning listeners.
     *
     * @param button The JButton from which to remove listeners.
     */
    private void removeAllActionListeners(JButton button)
    {
        if (button != null)
        {
            ActionListener[] listeners = button.getActionListeners();
            for (ActionListener listener : listeners)
            {
                button.removeActionListener(listener);
            }
        }
    }

    /**
     * Displays the game over screen, indicating whether the player won or lost.
     * Sets up action listeners for "Try Again" and "Main Menu" buttons,
     * and "Next Level" buttons if the player won.
     *
     * @param didPlayerWin {@code true} if the player won the level, {@code false} otherwise.
     * @param level The level that was just played.
     */
    public void goGameOverScreen(boolean didPlayerWin, int level)
    {
        gui.showGameOverPanel(didPlayerWin, level);

        // Try again button (visible on loss)
        JButton tryAgainButton = gui.getButton("tryAgainButton");
        if (!didPlayerWin && tryAgainButton != null)
        {
            removeAllActionListeners(tryAgainButton);
            tryAgainButton.addActionListener(e -> startGame(board.getLevel()));
        }

        // Main menu button (always visible)
        JButton mainMenuButton = gui.getButton("mainMenuButton");
        if (mainMenuButton != null)
        {
            removeAllActionListeners(mainMenuButton);
            mainMenuButton.addActionListener(e -> goMainMenu());
        }

        // Level two button (visible if player won level 1)
        JButton levelTwoButton = gui.getButton("levelTwoButtonGameOver");
        if (didPlayerWin && level == 1 && levelTwoButton != null)
        {
            removeAllActionListeners(levelTwoButton);
            levelTwoButton.addActionListener(e -> startGame(2));
        }

        // Level three button (visible if player won level 2)
        JButton levelThreeButton = gui.getButton("levelThreeButtonGameOver");
        if (didPlayerWin && level == 2 && levelThreeButton != null)
        {
            removeAllActionListeners(levelThreeButton);
            levelThreeButton.addActionListener(e -> startGame(3));
        }
    }

    /**
     * Starts a new game at the specified level.
     * Initializes player and board, sets up GUI, and starts the game loop timer.
     *
     * @param level The level number to start.
     */
    public void startGame(int level)
    {
        player = new Player(board, level);
        board.loadLevel(level);

        gui.updatePlantSelectionPanel(level);
        gui.showGameUI();
        addMouseListeners();

        System.out.println("*** LEVEL " + level + " ***");

        GameClock clock = new GameClock(); // constructor resets GameClock's time

        lastTimeUpdate = System.currentTimeMillis();

        int delay = 1000 / 60; // 17ms for 60 FPS
        // This runs 60 times a second
        animationTimer = new Timer(delay, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                long currentTime = System.currentTimeMillis();
                // Calculate delta time in seconds
                double timeElapsed = (currentTime - lastTimeUpdate) / 1000.0;
                lastTimeUpdate = currentTime; // Update for next frame

                // Update the game time
                GameClock.addTime(timeElapsed);
                gameOver = updateModel(timeElapsed);

                // Reset game if game over
                if (gameOver)
                {
                    animationTimer.stop();
                    board.resetBoard();
                    player.reset();
                    gui.getBoardPanel().resetFrameCount();
                    plantNo = 0; // Reset selected plant

                    gui.getPlantSelectionPanel().getPlantButtons().forEach(btn ->
                    {
                        btn.resetButton();
                    });

                    // Show winning screen if game time passed the winning time, else losing screen
                    if (gameTime >= 180)
                        goGameOverScreen(true, level);
                    else
                        goGameOverScreen(false, level);
                    return; // Exits the timer
                }

                // Get updated entities for rendering
                gui.getBoardPanel().setEntities(board.getEntities());

                // Update plant buttons (cooldowns, sun cost availability)
                gui.getPlantSelectionPanel().getPlantButtons().forEach(btn ->
                {
                    btn.updatePlantButton();
                });

                // Update sun tracker display
                gui.getPlantSelectionPanel().updateSunCount(player.getTotalSun());

                // Repaint board visuals
                gui.getBoardPanel().repaint();
            }
        });
        animationTimer.start(); // Start the animation loop
    }

    /**
     * Updates the game model (board state, entities) based on elapsed time.
     * Checks for game over conditions.
     *
     * @param timeElapsed Time elapsed since the last update.
     * @return {@code true} if the game is over, {@code false} otherwise.
     */
    public boolean updateModel(double timeElapsed)
    {
        boolean gameOver = false;
        gameTime = GameClock.getTotalTimeSecondsInt();

        gameOver = board.updateBoard(timeElapsed);

        if(gameTime >= 180 && !gameOver)
        {
            GameClock.printTime();
            System.out.println("*** GAME OVER | PLAYER WINS! ***");
            gameOver = true;
        }

        return gameOver;
    }
}
