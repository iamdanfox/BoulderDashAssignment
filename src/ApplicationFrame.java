import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.*;
import javax.swing.border.*;

import model.*;
import controller.*;



public class ApplicationFrame {

	private final JFrame frame;
	private final ApplicationState appState;

	/**
	 * Create the application, create the toolbar and create the view.
	 */
	private ApplicationFrame() {
		// window appearance and behaviour
		frame = new JFrame("Boulder Dash");
		frame.setLocation(300,150);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// window contents
		JPanel contentPane = new JPanel(new BorderLayout());
		frame.setContentPane(contentPane);

		// set up state
		appState = new ApplicationState(new Cave(10,10));
		Cave cave = appState.cave;
		
		// set up interaction modes
		final SelectingMode editMode = new EditMode();
		final CreatorMode cWallMode = new CreatorMode(new Wall(null)); 
		final CreatorMode cBoulder = new CreatorMode(new Boulder(null));
		final CreatorMode cDirt = new CreatorMode(new Dirt(null));
		final CreatorMode cDiamond = new CreatorMode(new Diamond(null));
		final InteractionMode cPlayer = new CreatorMode(new Player(null));
		final InteractionMode playMode = new PlayMode();
		appState.registerMode(editMode);
		appState.registerMode(cWallMode);
		appState.registerMode(cBoulder);
		appState.registerMode(cDirt);
		appState.registerMode(cDiamond);
		appState.registerMode(cPlayer);
		appState.registerMode(playMode);
		
		// place caveView in the middle of the frame
		JPanel caveContainer = new JPanel(new BorderLayout());
		caveContainer.setBorder(new EmptyBorder(20,20,20,20));
		contentPane.add(BorderLayout.CENTER, caveContainer);
		caveContainer.add(BorderLayout.CENTER, appState.caveView);
		
		// toolbar
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setMargin(new Insets(10,10,10,10));
		contentPane.add(BorderLayout.NORTH, toolbar);
		
		// set up interaction mode buttons
		toolbar.add(makeModeButton(editMode, "Edit"));
		toolbar.add(makeModeButton(cWallMode, "Wall"));
		toolbar.add(makeModeButton(cBoulder, "Boulder"));
		toolbar.add(makeModeButton(cDiamond, "Diamond"));
		toolbar.add(makeModeButton(cDirt, "Dirt"));
		toolbar.add(makeModeButton(cPlayer, "Player"));
		toolbar.add(makeModeButton(playMode, "Play"));
		
		toolbar.addSeparator();
		
		// delete button
		toolbar.add(new DeleteSelectionButton(cave,editMode));
		
		// wall colour combobox
		toolbar.add(new ColorWallSelectionBox(cave,editMode));
		
		// diamond target field
		toolbar.add(new DiamondTargetField(cave, editMode));
		

        toolbar.addSeparator();
        toolbar.add(new LoaderSaver(cave, editMode));
		
		// fill cave:
		loadCave("hard.txt"); 
		//loadCave("demo.txt");   
		
		// last minute settings, then curtains up
		frame.pack();
		appState.setActiveMode(editMode);
	}
	
	@SuppressWarnings("serial")
    private JToggleButton makeModeButton(final InteractionMode m, String text){
        return new JToggleButton(text) {{
            // make caveView react to button clicks
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    appState.setActiveMode(m);
                }
            });

            // make button listen for mode change
            m.registerModeListener(new ModeListener() {
                @Override
                public void madeActive(InteractionMode m) {
                    setSelected(true);
                }

                @Override
                public void madeNotActive(InteractionMode m) {
                    setSelected(false);
                }
            });
        }};
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ApplicationFrame window = new ApplicationFrame();
		window.frame.setVisible(true);
	}

	
	private void loadCave(String filename){
        try {
            Cave c = SimpleLexer.lex(LevelStorer.readFromFile(filename));
            appState.cave.setWidth(c.getWidth());
            appState.cave.setHeight(c.getHeight());
            appState.cave.copyStateFrom(c);
        } catch (FileNotFoundException e) {
            System.err.println(filename +" couldn't be loaded");
            e.printStackTrace();
        }
	}

}
