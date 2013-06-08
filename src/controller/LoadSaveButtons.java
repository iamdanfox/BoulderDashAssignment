package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Cave;
import model.SimpleLexer;
import model.SimpleLexer.LexerException;

/**
 * Allows the user to load/save a text file representating a cave.
 * @author danfox
 *
 */
@SuppressWarnings("serial")
public class LoadSaveButtons extends JPanel implements ModeListener {
    private final Cave cave;
    
    public LoadSaveButtons(Cave cave, InteractionMode mode){
        this.add(loadButton);
        this.add(saveButton);
        this.cave = cave;
        
        mode.registerModeListener(this);
    }
    
    /**
     * saveButton simply saves the current cave to an unused filename.
     * Should only be enabled in editMode.
     */
    final JButton saveButton = new JButton("Save"){{
        this.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("saveButton pressed");
                ArrayList<String> taken = LevelStorer.getLevelFiles();
                String name = JOptionPane.showInputDialog(
                        "Please enter a name for this level",
                        LevelStorer.findUnusedLevelname());
                if (name != null) {
                    while (taken.contains(name + ".txt")) {
                        name = JOptionPane
                                .showInputDialog(
                                        "Sorry, " + name + " is taken. Please enter another name for this level",
                                        LevelStorer.findUnusedLevelname());
                    }
                    // POST: taken.contains(name+".txt")=false

                    LevelStorer.writeToFile(name+".txt",SimpleLexer.antiLex(cave));
                    System.out.println("Saved to "+name+ ".txt");
                }
            }
        });
    }};
    
    /**
     * When clicked, this button allows the user to select a file, then
     * replace the appState's cave with the stored version (triggering a view resize).
     */
    final JButton loadButton = new JButton("Load"){{
        this.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a file chooser
                final JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(LevelStorer.DIRFILE);
                
                // Show chooser, react to 
                if (fc.showOpenDialog(LoadSaveButtons.this) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        Cave c = SimpleLexer.lex(LevelStorer.readFromFile(file));
                        cave.copyStateFrom(c);
                        System.out.println("Loaded cave from "+file.getName()+" with dimensions "
                                + c.getWidth() + "x" + c.getHeight());
                    } catch (FileNotFoundException ex) {
                        showError(file.getName() + " couldn't be loaded");
                    } catch (LexerException ex) {
                        System.err.println("Couldn't lex "+file.getName());
                        showError(file.getName() + " isn't a valid Boulder Dash Level.");
                    }
                } 
            }
        });
    }};
    
    private void showError(String msg){
        JOptionPane.showMessageDialog(null, msg, "Error", 0);
    }

    @Override
    public void madeActive(InteractionMode m) {
        saveButton.setEnabled(true);
        loadButton.setEnabled(true);
    }

    @Override
    public void madeNotActive(InteractionMode m) {
        saveButton.setEnabled(false);
        loadButton.setEnabled(false);
    }
    
    
}
