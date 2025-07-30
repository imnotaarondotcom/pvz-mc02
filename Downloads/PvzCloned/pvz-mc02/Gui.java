import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Manages the graphical user interface for the Plants Vs. Zombies game.
 * This includes setting up the main window, displaying different game panels
 * (main menu, game board, game over screen), and loading visual assets.
 *
 * @author Lim, Israel Sy
 * @author Enriquez, Aaron Mikael Cruz
 * @version 1.0
 * @since 2025-07-30
 */
public class Gui
{
    /** The width of the game screen in pixels. */
    private int screenWidth;
    /** The height of the game screen in pixels. */
    private int screenHeight;
    /** The height reserved for the plant selection panel. */
    private int plantSelectionPanelHeight;
    /** The number of borders used in the plant selection panel. */
    private int numPlantPanelBorders;

    /** The main JFrame window for the game. */
    private JFrame frame;
    /** Manages all game animations and images. */
    private AnimationManager animations;

    /** Panel for the main menu screen. */
    private JPanel mainMenuPanel;
    /** Panel for the game over screen. */
    private JPanel gameOverPanel;
    /** Panel for the winning screen (currently unused, integrated into gameOverPanel). */
    private JPanel winningPanel; // This variable is declared but not explicitly used for a separate panel
    /** Panel for selecting plants during gameplay. */
    private PlantSelectionPanel plantSelectionPanel;
    /** Panel for displaying the game board and entities. */
    private BoardPanel boardPanel;

    /** Button for starting Level 1. */
    private JButton levelOneButton;
    /** Button for starting Level 2. */
    private JButton levelTwoButton;
    /** Button for starting Level 3. */
    private JButton levelThreeButton;
    /** Button to return to the main menu from game over. */
    private JButton mainMenuButton;
    /** Button to retry the current level after game over. */
    private JButton tryAgainButton;
    /** Button to exit the game. */
    private JButton exitButton;
    /** Button to start Level 2 from the game over screen after winning Level 1. */
    private JButton levelTwoButtonGameOver;
    /** Button to start Level 3 from the game over screen after winning Level 2. */
    private JButton levelThreeButtonGameOver;

    /**
     * Constructs the GUI, initializing screen dimensions, loading animations,
     * creating buttons, and setting up the main frame.
     */
    public Gui()
    {
        animations = new AnimationManager();

        numPlantPanelBorders = 6;
        plantSelectionPanelHeight = 200;
        screenHeight = 1080;
        screenWidth = 1920;

        initializeButtons();
        initializeAnimations(animations);
        initializeGui();
    }

    /**
     * Initializes and styles all JButtons used throughout the GUI.
     * Sets font, colors, and preferred size, and adds an action listener to the exit button.
     */
    private void initializeButtons()
    {
        // Main menu buttons
        levelOneButton = new JButton("Level 1");
        levelTwoButton = new JButton("Level 2");
        levelThreeButton = new JButton("Level 3");
        exitButton = new JButton("Exit");

        // Game over panel buttons
        tryAgainButton = new JButton("Try Again");
        mainMenuButton = new JButton("Main Menu");
        levelTwoButtonGameOver = new JButton("Level 2");
        levelThreeButtonGameOver = new JButton("Level 3");

        // Style all buttons
        styleButton(levelOneButton);
        styleButton(levelTwoButton);
        styleButton(levelThreeButton);
        styleButton(exitButton);
        styleButton(tryAgainButton);
        styleButton(mainMenuButton);
        styleButton(levelTwoButtonGameOver);
        styleButton(levelThreeButtonGameOver);

        // Add exit action to exit button
        exitButton.addActionListener(e -> System.exit(0));
    }

    /**
     * Loads all necessary game animations and images using the provided {@link AnimationManager}.
     * This includes environment tiles, UI icons, and character animations for plants and zombies.
     *
     * @param animations The {@link AnimationManager} instance to load assets into.
     */
    public void initializeAnimations(AnimationManager animations)
    {
        // Environment
        animations.loadImages("environment", "Grass", 2);

        // Icons
        animations.loadAnimation("icons", "sun_idle", 1);
        animations.loadImages("icons", "shovel", 1);
        animations.loadImages("borders" , "sunbox" , 1);
        animations.loadImages("borders", "border" , 1);
        animations.loadImages("icons", "peashooter_icon", 1);
        animations.loadImages("icons", "sunflower_icon", 1);
        animations.loadImages("icons", "wallnut_icon", 1);
        animations.loadImages("icons", "cherrybomb_icon", 1);
        animations.loadImages("icons", "gameover", 1);
        animations.loadImages("icons", "title", 1);

        // Normal zombie
        animations.loadAnimation("zombie_walk", "zombie_walkA", 7);
        animations.loadAnimation("zombie_walk", "zombie_walkB", 7);
        animations.loadAnimation("zombie_walk", "zombie_walkC", 7);
        animations.loadAnimation("zombie_eat", "zombie_eatA", 7);
        animations.loadAnimation("zombie_eat", "zombie_eatB", 7);
        animations.loadAnimation("zombie_eat", "zombie_eatC", 5);

        // Conehead zombie
        animations.loadAnimation("zombie_walk", "zombiecone_walkA", 7);
        animations.loadAnimation("zombie_walk", "zombiecone_walkB", 7);
        animations.loadAnimation("zombie_walk", "zombiecone_walkC", 7);
        animations.loadAnimation("zombie_eat", "zombiecone_eatA", 7);
        animations.loadAnimation("zombie_eat", "zombiecone_eatB", 7);
        animations.loadAnimation("zombie_eat", "zombiecone_eatC", 7);

        // Flag zombie
        animations.loadAnimation("zombie_walk", "zombieflag_walkA", 7);
        animations.loadAnimation("zombie_walk", "zombieflag_walkB", 7);
        animations.loadAnimation("zombie_eat", "zombieflag_eatA", 7);
        animations.loadAnimation("zombie_eat", "zombieflag_eatB", 7);

        // Plants
        animations.loadAnimation("plants/peashooter", "peashooter_idle", 8);
        animations.loadAnimation("plants/sunflower", "sunflower_idle", 10);
        animations.loadAnimation("plants/peashooter", "pea_idle", 1);
        animations.loadAnimation("plants/cherrybomb", "cherrybomb_exploding", 6);
        animations.loadAnimation("plants/cherrybomb", "cherrybomb_exploded", 8);
        animations.loadAnimation("plants/wallnut", "wallnut_idleA", 5);
        animations.loadAnimation("plants/wallnut", "wallnut_idleB", 5);
        animations.loadAnimation("plants/wallnut", "wallnut_idleC", 5);
    }

    /**
     * Initializes the main JFrame and sets up the initial game panels.
     * Sets the frame properties and displays the main menu.
     */
    public void initializeGui()
    {
        frame = new JFrame("Plants Vs Zombies");
        frame.setSize(screenWidth, screenHeight);

        frame.setBackground(Color.black);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        // Initialize the board panel and plant selection panel
        boardPanel = new BoardPanel(animations, screenHeight, screenWidth, plantSelectionPanelHeight);
        // Default level 1 for initial panel setup
        plantSelectionPanel = new PlantSelectionPanel(animations, screenWidth, plantSelectionPanelHeight, numPlantPanelBorders, 1);

        // Set to not be visible initially
        plantSelectionPanel.setVisible(false);
        boardPanel.setVisible(false);

        initializeMainMenu(frame);
        frame.setVisible(true);
    }

    /**
     * Initializes the main menu panel, including its background and layout.
     * Adds level selection buttons and an exit button.
     *
     * @param frame The main JFrame to add the panel to.
     */
    public void initializeMainMenu(JFrame frame)
    {
        mainMenuPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                BufferedImage titleArt = animations.getImages("title")[0];
                if (titleArt != null)
                {
                    int panelWidth = getWidth();
                    int panelHeight = getHeight();

                    int imageWidth = titleArt.getWidth();
                    int imageHeight = titleArt.getHeight();

                    // Calculate position to center horizontally and place in upper quarter vertically
                    int xPos = (panelWidth - imageWidth) / 2;
                    int yPos = ((panelHeight - imageHeight) / 4);

                    g.drawImage(titleArt, xPos, yPos, this);
                }
            }
        };

        mainMenuPanel.setBackground(Color.black);
        mainMenuPanel.setLayout(new GridBagLayout()); // uses GridBagLayout for flexible button placement
        mainMenuPanel.setPreferredSize(new Dimension(screenWidth, screenHeight));
        mainMenuPanel.setOpaque(true);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; // keep the buttons in the same column
        gbc.anchor = GridBagConstraints.CENTER; // keep buttons centered horizontally

        // add a spacer at the top to push buttons down
        mainMenuPanel.add(Box.createVerticalStrut(500), gbc);

        // add the buttons
        gbc.insets.bottom = 20; // padding between buttons
        gbc.gridy = 1;
        mainMenuPanel.add(levelOneButton, gbc);

        gbc.gridy = 2;
        mainMenuPanel.add(levelTwoButton, gbc);

        gbc.gridy = 3;
        mainMenuPanel.add(levelThreeButton, gbc);

        gbc.gridy = 4;
        mainMenuPanel.add(exitButton, gbc);

        frame.add(mainMenuPanel, BorderLayout.CENTER);
    }

    /**
     * Initializes the game over screen panel, displaying either a win message
     * or a game over image, along with relevant action buttons.
     *
     * @param frame The main JFrame to add the panel to.
     * @param didPlayerWin {@code true} if the player won the level, {@code false} otherwise.
     * @param level The level that was just completed.
     */
    public void initializeGameOverScreen(JFrame frame, boolean didPlayerWin, int level)
    {
        if (didPlayerWin)
        {
            // Show win screen using JLabel
            JLabel winLabel = new JLabel("You beat level " + level + "!");
            winLabel.setFont(new Font("SansSerif", Font.BOLD, 64));
            winLabel.setForeground(Color.WHITE);
            winLabel.setHorizontalAlignment(SwingConstants.CENTER);
            winLabel.setVerticalAlignment(SwingConstants.CENTER);

            gameOverPanel = new JPanel(new BorderLayout());
            gameOverPanel.add(winLabel, BorderLayout.CENTER);
        }
        else
        {
            gameOverPanel = new JPanel()
            {
                @Override
                protected void paintComponent(Graphics g)
                {
                    super.paintComponent(g);
                    BufferedImage gameOverArt = animations.getImages("gameover")[0];
                    if (gameOverArt != null) {
                        int panelWidth = getWidth();
                        int panelHeight = getHeight();

                        int imageWidth = gameOverArt.getWidth();
                        int imageHeight = gameOverArt.getHeight();

                        int xPos = (panelWidth - imageWidth) / 2;
                        int yPos = (panelHeight - imageHeight) / 4;

                        g.drawImage(gameOverArt, xPos, yPos, this);
                    }
                }
            };
        }

        gameOverPanel.setBackground(Color.black);
        gameOverPanel.setLayout(new GridBagLayout()); // uses GridBagLayout
        gameOverPanel.setPreferredSize(new Dimension(screenWidth, screenHeight));
        gameOverPanel.setOpaque(true);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; // keep the buttons in the same column
        gbc.insets.bottom = 20; // padding between buttons
        gbc.anchor = GridBagConstraints.CENTER; // keep buttons centered horizontally

        if (didPlayerWin)
        {
            gameOverPanel.add(Box.createVerticalStrut(150), gbc); // spacer
            switch (level)
            {
                case 1:
                    gbc.gridy = 1;
                    gameOverPanel.add(levelTwoButtonGameOver, gbc);

                    gbc.gridy = 2;
                    gameOverPanel.add(mainMenuButton, gbc);
                    break;
                case 2:
                    gbc.gridy = 1;
                    gameOverPanel.add(levelThreeButtonGameOver, gbc);

                    gbc.gridy = 2;
                    gameOverPanel.add(mainMenuButton, gbc);
                    break;
                default: // For level 3 or any other level
                    gbc.gridy = 1;
                    gameOverPanel.add(mainMenuButton, gbc);
                    break;
            }
        }
        else // Player lost
        {
            gameOverPanel.add(Box.createVerticalStrut(500), gbc); // spacer
            gbc.gridy = 1;
            gameOverPanel.add(tryAgainButton, gbc);

            gbc.gridy = 2;
            gameOverPanel.add(mainMenuButton, gbc);
        }

        frame.add(gameOverPanel, BorderLayout.CENTER);
    }

    /**
     * Applies a consistent style to a given JButton.
     * Sets font, foreground/background colors, and preferred size.
     *
     * @param button The {@link JButton} to style.
     */
    private void styleButton(JButton button)
    {
        button.setFont(new Font("Arial", Font.BOLD, 30));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 150, 50));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 60));
    }

    /**
     * Retrieves a specific JButton instance by its name.
     *
     * @param button The string name of the button to retrieve.
     * @return The requested {@link JButton}, or {@code null} if not found.
     */
    public JButton getButton(String button)
    {
        switch (button)
        {
            case "levelOneButton":
                return levelOneButton;
            case "levelTwoButton":
                return levelTwoButton;
            case "levelThreeButton":
                return levelThreeButton;
            case "tryAgainButton":
                return tryAgainButton;
            case "mainMenuButton":
                return mainMenuButton;
            case "levelTwoButtonGameOver":
                return levelTwoButtonGameOver;
            case "levelThreeButtonGameOver":
                return levelThreeButtonGameOver;
            default:
                return null;
        }
    }

    /**
     * Updates the plant selection panel for a new game level.
     * Creates a new {@link PlantSelectionPanel} instance with the specified level.
     *
     * @param level The new game level.
     */
    public void updatePlantSelectionPanel(int level)
    {
        // Create new panel with the current level
        plantSelectionPanel = new PlantSelectionPanel(animations, screenWidth, plantSelectionPanelHeight, numPlantPanelBorders, level);
    }

    /**
     * Displays the main game UI, including the plant selection panel and the board panel.
     * Removes any existing panels from the frame before adding the game UI.
     */
    public void showGameUI()
    {
        // Clear all content from the frame
        frame.getContentPane().removeAll();

        // Add the plant selection panel to the top and the board to the center
        frame.add(plantSelectionPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);

        // Make panels visible
        plantSelectionPanel.setVisible(true);
        boardPanel.setVisible(true);

        // Revalidate and repaint the frame to ensure changes are displayed
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Displays the main menu panel.
     * Removes any existing panels from the frame before showing the main menu.
     */
    public void showMainMenuPanel()
    {
        // Clear all content from the frame
        frame.getContentPane().removeAll();

        // Add the main menu panel to the center
        frame.add(mainMenuPanel, BorderLayout.CENTER);

        // Make the main menu panel visible
        mainMenuPanel.setVisible(true);

        // Revalidate and repaint the frame
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Displays the game over screen.
     * Initializes the game over panel based on win/loss status and then displays it.
     *
     * @param didPlayerWin {@code true} if the player won, {@code false} if lost.
     * @param level The level that was just played.
     */
    public void showGameOverPanel(boolean didPlayerWin, int level)
    {
        initializeGameOverScreen(frame, didPlayerWin, level);

        // Clear all content from the frame
        frame.getContentPane().removeAll();

        // Add the game over panel to the center
        frame.add(gameOverPanel, BorderLayout.CENTER);

        // Make the game over panel visible
        gameOverPanel.setVisible(true);

        // Revalidate and repaint the frame
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Retrieves the {@link PlantSelectionPanel} instance.
     * @return The plant selection panel.
     */
    public PlantSelectionPanel getPlantSelectionPanel()
    {
        return plantSelectionPanel;
    }

    /**
     * Retrieves the {@link BoardPanel} instance.
     * @return The board panel.
     */
    public BoardPanel getBoardPanel()
    {
        return boardPanel;
    }
}
