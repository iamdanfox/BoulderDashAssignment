package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import model.Boulder;
import model.Cave;
import model.CaveElement;
import model.CaveListener;
import model.Diamond;
import model.Dirt;
import model.Player;
import model.Wall;
import controller.ApplicationState;


/**
 * Responsible for showing the contents of the cave, using a painter for each class of CaveElement.
 * Automatically repaints whenever the cave's grid changes.
 */
@SuppressWarnings("serial")
public class CaveView extends JPanel implements CaveListener {
	public static final int squareSize = 35;
	public static final Map<Class<? extends CaveElement>,CaveElementPainter> painters= 
			new HashMap<Class<? extends CaveElement>,CaveElementPainter>();
	static {
		painters.put(Wall.class, new WallPainter());
		painters.put(Player.class, new PlayerPainter());
		painters.put(Dirt.class, new DirtPainter());
		painters.put(Diamond.class, new SlipGravElementPainter("images/diamond.png"));
		painters.put(Boulder.class, new SlipGravElementPainter("images/boulder.png"));
	}

	public final ApplicationState appState;
	private final int borderWidth;
	
	/**
	 * Create the CaveView JPanel and initialise the parts of its appearance common to all modes.
	 * @param appState
	 */
	public CaveView(final ApplicationState appState){
		super(new BorderLayout());
		this.setFocusable(true);
		Border bevelBorder = new BevelBorder(BevelBorder.LOWERED);
		this.setBorder(bevelBorder);
		borderWidth = bevelBorder.getBorderInsets(this).top;
		
		this.appState = appState;
		
		// listen to cave grid & dimensions
		appState.cave.addCaveListener(this);

        // set size
        dimensionsChanged();
    }

    /**
     * Show the contents of the cave. Calls paintInterior to allow the active
     * Interaction Mode to paint on top the basic cave.
     */
    @Override
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		
		// The state of the Graphics object should stay the same after the painting is finished.
		Color oldColor = g2d.getColor();
		Shape oldClip = g2d.getClip();
		
		g2d.setClip(borderWidth,borderWidth,this.getWidth()-borderWidth,this.getHeight()-borderWidth);
		
		// set origin at the first white pixel in cave
		g2d.translate(borderWidth,borderWidth);
		
		// paint background
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		// paint elements
		Cave cave = appState.cave;
		for(int y=0;y<cave.getHeight();y++){
			for(int x=0;x<cave.getWidth();x++){
				CaveElement e = cave.getElementAt(new Point(x,y));
				// execute for non blank squares
				if (e != null)	painters.get(e.getClass()).paint(g2d,e, CaveView.squareSize); 
			}
		}
		
		// run mode painter
		if (appState.getActiveMode()!=null) appState.getActiveMode().paintInterior(g2d);

		// restore the original state of the Graphics object
		g2d.translate(-borderWidth,-borderWidth);
		g2d.setColor(oldColor);
		g2d.setClip(oldClip);
	}
    
    @Override
    public void wonStateChanged() { }
    @Override
    public void lostStateChanged() { }
    @Override
    public void diamondTargetChanged() { }
    
    @Override
    public void gridChanged() {
        this.repaint();
    }

    @Override
    public void dimensionsChanged() {
        CaveView.this.setPreferredSize(new Dimension(appState.cave
                .getWidth() * squareSize + 2 * borderWidth,
                appState.cave.getHeight() * squareSize + 2
                        * borderWidth));
        CaveView.this.setSize(new Dimension(appState.cave
                .getWidth() * squareSize + 2 * borderWidth,
                appState.cave.getHeight() * squareSize + 2
                        * borderWidth));
        
        System.out.println("CaveView preferredSize="+this.getPreferredSize());
    }

    @Override
    public void frozenStateChanged() { }
}
