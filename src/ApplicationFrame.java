import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		frame.setLocation(400,300);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// window contents
		JPanel contentPane = new JPanel(new BorderLayout());
		frame.setContentPane(contentPane);

		// set up state
		appState = new ApplicationState();
		Cave cave = appState.cave;
		
		// set up interaction modes
		final SelectingMode editMode = new EditMode();
		final CreatorMode cWallMode = new CreatorMode(new Wall(cave)); 
		final CreatorMode cBoulder = new CreatorMode(new Boulder(cave));
		final CreatorMode cDirt = new CreatorMode(new Dirt(cave));
		final CreatorMode cDiamond = new CreatorMode(new Diamond(cave));
		final InteractionMode cPlayer = new CreatorMode(new Player(cave));
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
		toolbar.add(makeModeButton(cWallMode, "Create Wall"));
		toolbar.add(makeModeButton(cBoulder, "Create Boulder"));
		toolbar.add(makeModeButton(cDiamond, "Create Diamond"));
		toolbar.add(makeModeButton(cDirt, "Create Dirt"));
		toolbar.add(makeModeButton(cPlayer, "Create Player"));
		toolbar.add(makeModeButton(playMode, "Play"));
		
		toolbar.addSeparator();
		
		// delete button
		toolbar.add(new DeleteSelectionButton(cave,editMode));
		
		// wall colour combobox
		toolbar.add(new ColorWallSelectionBox(cave,editMode));
		
		// diamond target field
		toolbar.add(new DiamondTargetField(cave, editMode));
		
		// fill cave:
		loadHardCave();   // play this if you dare!
		//loadDemoCave();
		
		// last minute settings, then curtains up
		frame.pack();
		appState.setActiveMode(editMode);
	}
	
	private JToggleButton makeModeButton(final InteractionMode m, String text){
		final JToggleButton button = new JToggleButton(text);
		
		// make caveView listen to button
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) { // button clicked
				appState.setActiveMode(m);
				if (!button.isSelected()) { // don't allow user to unselect a mode (otherwise appState could have no active mode).
					button.setSelected(true);
				}
			}
		});
		
		// make button listen for mode change
		m.registerModeListener(new ModeListener(){
			@Override
            public void madeActive(InteractionMode m) {
				button.setSelected(true);
			}
			@Override
            public void madeNotActive(InteractionMode m) {
				button.setSelected(false);
			}			
		});
		
		return button;
	}
	
	private void bulkCreate(CaveElement e, int x0, int y0, int x1, int y1){
		for(int x=x0;x<=x1;x++) for (int y=y0;y<=y1;y++)	appState.cave.setElementAt(new Point(x,y), e.cloneToCave(appState.cave));
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ApplicationFrame window = new ApplicationFrame();
		window.frame.setVisible(true);
	}

	
	private void loadHardCave(){
		Cave c = appState.cave;
		c.setDiamondTarget(41);
		bulkCreate(new Boulder(c),0,0,19,3);
		bulkCreate(new Diamond(c),0,4,17,5);
		bulkCreate(new Dirt(c), 2,6,17,6);
		bulkCreate(new Wall(c,Color.GREEN),3,0,3,7);
		bulkCreate(new Wall(c,Color.GREEN),3,0,3,7);
		bulkCreate(new Wall(c,Color.GREEN),14,0,14,6);
		bulkCreate(new Dirt(c), 16,0,18,0);
		bulkCreate(new Dirt(c), 0,3,3,3);
		bulkCreate(new Dirt(c), 0,7,1,9);
		bulkCreate(new Dirt(c), 15,3,16,6);
		bulkCreate(new Wall(c),16,1,17,1);
		bulkCreate(new Wall(c,Color.GREEN),15,7,16,7);
		bulkCreate(new Wall(c,Color.GREEN),17,8,18,8);
		bulkCreate(new Wall(c,Color.GREEN),19,9,19,9);
		bulkCreate(new Wall(c,Color.GREEN),2,8,2,8);
		bulkCreate(new Wall(c,Color.GREEN),1,9,1,9);
		bulkCreate(new Diamond(c),0,6,0,7);
		bulkCreate(new Wall(c),1,6,1,6);
		bulkCreate(new Wall(c),2,7,2,7);
		bulkCreate(new Wall(c,Color.blue),7,7,7,9);
		bulkCreate(new Diamond(c),15,1,15,3);
		bulkCreate(new Diamond(c),18,3,18,3);
		bulkCreate(new Boulder(c),18,4,18,4);
		bulkCreate(new Diamond(c),19,2,19,2);
		bulkCreate(new Boulder(c),19,3,19,3);
		bulkCreate(new Boulder(c),17,5,17,5);
		c.setElementAt(new Point(8,3),new Player(c));
		c.setElementAt(new Point(0,2),new Diamond(c));
		c.setElementAt(new Point(1,1),new Diamond(c));
		c.setElementAt(new Point(4,2),new Diamond(c));
		c.setElementAt(new Point(6,1),new Diamond(c));
		c.setElementAt(new Point(11,1),new Diamond(c));
		c.setElementAt(new Point(12,2),new Diamond(c));
		c.setElementAt(new Point(19,8),new Diamond(c));
		c.setElementAt(new Point(18,9),new Diamond(c));
		c.setElementAt(new Point(2,3),new Diamond(c));
		c.setElementAt(new Point(1,4),new Dirt(c));
		c.setElementAt(new Point(14,1),new Dirt(c));
		c.setElementAt(new Point(1,5),new Boulder(c));
		c.setElementAt(new Point(12,6),new Wall(c,Color.red));
		c.setElementAt(new Point(16,2),new Dirt(c));
		c.setElementAt(new Point(18,5),new Dirt(c));
		c.setElementAt(new Point(19,4),new Dirt(c));
		c.setElementAt(new Point(12,9),new Boulder(c));
		c.setElementAt(new Point(14,9),new Boulder(c));
		c.setElementAt(new Point(9,9),new Boulder(c));
	}
	
    private void loadDemoCave(){
		Cave c = appState.cave;
		c.setDiamondTarget(30);
		bulkCreate(new Diamond(c),0,0,0,9);
		bulkCreate(new Dirt(c),1,0,7,9);
		bulkCreate(new Boulder(c),1,0,1,8);
		bulkCreate(new Wall(c,Color.BLUE),2,0,2,8);
		bulkCreate(new Wall(c,Color.GREEN),10,0,11,2);
		bulkCreate(new Boulder(c),6,0,10,1);
		bulkCreate(new Diamond(c),7,2,9,2);
		bulkCreate(new Boulder(c),7,3,9,3);
		bulkCreate(new Boulder(c),9,4,10,4);
		bulkCreate(new Wall(c),6,4,7,5);
		bulkCreate(new Wall(c),8,5,10,5);
		c.setElementAt(new Point(8,3), new Player(c));
		bulkCreate(new Dirt(c),6,7,19,9);
		bulkCreate(new Diamond(c),6,6,10,6);
		bulkCreate(new Boulder(c),7,7,9,7);
		bulkCreate(new Dirt(c),8,7,8,7);
		bulkCreate(new Diamond(c),7,8,9,8);
		bulkCreate(new Dirt(c),11,6,11,6);
		bulkCreate(new Dirt(c),12,0,19,2);
		bulkCreate(new Dirt(c),15,3,19,6);
		bulkCreate(new Diamond(c),17,0,17,2);
		bulkCreate(new Wall(c),15,4,16,6);
		bulkCreate(new Diamond(c),15,7,16,7);
		bulkCreate(new Diamond(c),14,8,17,8);
	}

}
