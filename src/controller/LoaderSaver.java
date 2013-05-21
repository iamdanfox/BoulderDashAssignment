package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
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
                System.out.println(LevelStorer.writeToFile(SimpleLexer.antiLex(cave)));
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
                System.out.println("loadButton pressed");
                
                ArrayList<String> fnames = LevelStorer.getLevelFiles();
                
                int loadNow = (lastLoaded + 1) % fnames.size();
                String filename = fnames.get(loadNow);

                try {
                    Cave c = SimpleLexer.lex(LevelStorer.readFromFile(filename));
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
