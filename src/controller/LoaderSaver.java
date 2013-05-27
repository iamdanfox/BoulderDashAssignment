package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Cave;
import model.SimpleLexer;
import model.SimpleLexer.LexerException;

/**
 * Allows the user to load/save a text file representation of a cave.
 * @author danfox
 *
 */
@SuppressWarnings("serial")
public class LoaderSaver extends JPanel implements ModeListener {
    private final Cave cave;
    private int lastLoaded =-1;
    
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

                    System.out.println("Attempting save to "+name+ ".txt");
                    try {
                        LevelStorer.writeToFile(name+".txt",SimpleLexer.antiLex(cave));
                    } catch (FileNotFoundException e) {
                        JOptionPane.showMessageDialog(null, "Couldn't save level to "+name+".txt", "Error", 0);
                    }
                }
            }
        });
    }};
    
    /**
     * When clicked, this should allow the user to select a file, then
     * replace the appState's cave with the stored version (triggering a view resize).
     */
    final JButton loadButton = new JButton("Load"){{
        this.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                ArrayList<String> fnames = LevelStorer.getLevelFiles();
                
                int loadNow = (lastLoaded + 1) % fnames.size();
                String filename = fnames.get(loadNow);
                
                System.out.println("loadButton pressed, loading "+filename);

                try {
                    Cave c = SimpleLexer.lex(LevelStorer.readFromFile(filename));
                    System.out.println("Loaded cave with dimensions "+c.getWidth()+"x"+c.getHeight());
                    cave.copyStateFrom(c);
                    lastLoaded = loadNow;
                } catch (FileNotFoundException ex) {
                    System.err.println(filename +" couldn't be loaded");
                } catch (LexerException ex) {
                    System.err.println(filename +" couldn't be lexed");
                }
            }
        });
    }};
    
    public LoaderSaver(Cave cave, InteractionMode mode){
        this.add(loadButton);
        this.add(saveButton);
        this.cave = cave;
        
        mode.registerModeListener(this);
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
