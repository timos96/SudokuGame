package core.gui;

import core.LanguageSettings;
import core.game.sudoku.Sudoku;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;

/** A JPanel for the sudoku game */
public class GUISudokuBoard extends JPanel {
    
    //CellSize
    protected static final Dimension CELL_SIZE = new Dimension(50, 50);
    
    //Borders
    private static final Border UL_BORDER = BorderFactory.createMatteBorder(2, 2, 1, 1, Color.BLACK);
    private static final Border U_BORDER  = BorderFactory.createMatteBorder(2, 1, 1, 1, Color.BLACK);
    private static final Border UR_BORDER = BorderFactory.createMatteBorder(2, 1, 1, 2, Color.BLACK);
    private static final Border R_BORDER  = BorderFactory.createMatteBorder(1, 1, 1, 2, Color.BLACK);
    private static final Border DR_BORDER = BorderFactory.createMatteBorder(1, 1, 2, 2, Color.BLACK);
    private static final Border D_BORDER  = BorderFactory.createMatteBorder(1, 1, 2, 1, Color.BLACK);
    private static final Border DL_BORDER = BorderFactory.createMatteBorder(1, 2, 2, 1, Color.BLACK);
    private static final Border L_BORDER  = BorderFactory.createMatteBorder(1, 2, 1, 1, Color.BLACK);
    private static final Border M_BORDER  = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
    
    //Parent window
    private final MainForm parent;
    //Handling game
    private final Sudoku game;
    //Cells
    private final List<JButton> guiCells = new LinkedList<>();
    //Wordoku toggle
    private boolean letters = false;
    
    /**
     * Constructs the object given its parent window
     * @param parent The parent window
     */
    public GUISudokuBoard(MainForm parent) {
        super(null);    //No layout
        
        if(parent == null)
            throw new NullPointerException("Parent is null");
        
        Sudoku game = parent.getGame();
        
        if(game == null)
            throw new NullPointerException("The sudoku game is null");
    
    
        int d = game.getBoardDimension();
        int b = game.getBoxDimension();
        
        setSize(d*CELL_SIZE.width, d*CELL_SIZE.height); //Size gameDimension*CELL_SIZE
        
        for(int j = 0; j < d; ++j) {
            for(int i = 0; i < d; ++i) {
                JButton guiCell = new JButton();
                guiCell.setSize(CELL_SIZE);
                guiCell.setLocation(i*CELL_SIZE.width, j*CELL_SIZE.height); //Location = cellsize .* [i j]
                guiCell.setBackground(Color.WHITE);
                guiCell.setForeground(Color.BLACK);
                //Set Borders
                if (j%b == 0) {
                    //UP
                    if(i%b == 0) {
                        //LEFT
                        guiCell.setBorder(UL_BORDER);
                    } else if (i%b + 1 != b) {
                        //MIDDLE
                        guiCell.setBorder(U_BORDER);
                    } else {
                        //RIGHT
                        guiCell.setBorder(UR_BORDER);
                    }
                } else if (j%b + 1 != b) {
                    //MIDDLE
                    if(i%b == 0) {
                        //LEFT
                        guiCell.setBorder(L_BORDER);
                    } else if (i%b + 1 != b) {
                        //MIDDLE
                        guiCell.setBorder(M_BORDER);
                    } else {
                        //RIGHT
                        guiCell.setBorder(R_BORDER);
                    }
                } else {
                    //BOTTOM
                    if(i%b == 0) {
                        //LEFT
                        guiCell.setBorder(DL_BORDER);
                    } else if (i%b + 1 != b) {
                        //MIDDLE
                        guiCell.setBorder(D_BORDER);
                    } else {
                        //RIGHT
                        guiCell.setBorder(DR_BORDER);
                    }
                }
                
                guiCell.setFont(new Font("Serif", Font.PLAIN, 30));
                guiCell.addActionListener(this::onClick);
            
                if(!game.isCellEmpty(game.get(i, j)))
                    guiCell.setEnabled(false);
                
                add(guiCell);
                guiCells.add(guiCell);
            }
        }
        this.parent = parent;
        this.game = game;
    }
    
    //On updateUI set wordoku and if complete lock all cells
    @Override
    public void updateUI() {
        super.updateUI();
        if (game != null) {
            
            boolean isComplete = game.isComplete();
            
            for(int j = 0; j < game.getBoardDimension(); ++j) {
                for(int i = 0; i < game.getBoardDimension(); ++i) {
                    JButton cell = guiCells.get(i + j*game.getBoardDimension());
                    if(isComplete)
                        cell.setEnabled(false);
                    int v = game.get(i, j);
                    if( v != 0 )
                        cell.setText(String.valueOf((char)((letters?'A':'1')+v-1)));
                    else
                        cell.setText("");
                }
            }
        }
    }
    
    /**
     * Toggles wordoku
     */
    public void toggleWordoku() {
        letters = !letters;
        updateUI();
    }
    
    /**
     * Executes on completion
     */
    protected void onCompletion() {
        JOptionPane.showMessageDialog(null, LanguageSettings.getMessageBundle().getString("completed_game"));
    }
    
    /**
     * Handler for the click event
     * @param e The action performed
     */
    private void onClick(ActionEvent e) {
        int i = Arrays.asList(getComponents()).indexOf(e.getSource());  //Get the index of the clicked cell
        if( i == -1 )   //If -1 then it does not exist ?
            return;
    
        String input = JOptionPane.showInputDialog(LanguageSettings.getMessageBundle().getString("value") + ":");   //Get the input
        if (input == null) //If cancel return
            return;
    
        if (input.equals("")) { //If empty then reset
            game.reset(i);
        } else {
            int v;
            if( letters ) { //If wordoku
                if(input.length() > 1) { //If not a single letter
                    JOptionPane.showMessageDialog(null, LanguageSettings.getMessageBundle().getString("not_acceptable_input"));
                    return;
                }
                char character = input.charAt(0);
                v = character - 'A' + 1;// Convert to number
            } else {// If normal sudoku
                try {   //Try convert to number (String)
                    v = Integer.parseInt(input);
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(null, LanguageSettings.getMessageBundle().getString("not_a_number"));
                    return;
                }
            }
        

            if( !game.replace(i, v) )   //Replace value
                JOptionPane.showMessageDialog(null, LanguageSettings.getMessageBundle().getString("not_legal"));
        }
    
        SwingUtilities.updateComponentTreeUI(parent);   //Update tree
    
    
        if(game.isComplete())   //If complete then run onCompletion
            onCompletion();
    }
    
    
    /**
     * Getter for the game
     * @return the game
     */
    public Sudoku getGame() {
        return game;
    }
}
