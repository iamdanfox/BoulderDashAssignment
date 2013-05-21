package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.Cave;
import model.SimpleLexer;

/**
 * Allows the user to load/save a text file representation of a cave.
 * @author danfox
 *
 */
@SuppressWarnings("serial")
public class LoaderSaver extends JPanel implements ModeListener {
    private final Cave cave;
    
    /**
     * saveButton simply saves the current cave to an unused filename.
     * Should only be enabled in editMode.
     */
    final JButton saveButton = new JButton("Save"){{
        this.addActionListener(new ActionListener(){
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
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub                
            }
        });
    }};
    
    public LoaderSaver(Cave cave, InteractionMode mode){
        //this.add(loadButton);
        this.add(saveButton);
        this.cave = cave;
        
        mode.registerModeListener(this);
    }

    public void madeActive(InteractionMode m) {
        saveButton.setEnabled(true);
        loadButton.setEnabled(true);
    }

    public void madeNotActive(InteractionMode m) {
        saveButton.setEnabled(false);
        loadButton.setEnabled(false);
    }
    
    
}
