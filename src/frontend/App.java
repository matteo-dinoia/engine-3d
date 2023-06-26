package frontend;

import java.awt.*;
import java.util.ArrayList;

import backend.*;
import templetes.canvas.*;
import utils.clocks.*;
import utils.settings.ConstantsManager;
import utils.vectors.*;

public class App implements CanvasRepaintListener{

	private Camera3D camera;
	private PlayerMovement player;
	private final int PIXEL_SPACING, DIMENSION, SIDE;


	//BASE
	public static void main(String[] args) {
		ConstantsManager.addDefaultConstantsThenReadFromFile(null);
		//ConstantsManager.debugPrintValue();

		new App();
	}
	public App() {

		//FRAME
		CanvasFrame canvas=new CanvasFrame(this);
		canvas.getFrame().setResizable(false);


		//PANEL GAME
		this.PIXEL_SPACING=ConstantsManager.getIntValueOrCrash("pixelSpacing");
		this.DIMENSION=ConstantsManager.getIntValueOrCrash("dimension");
		this.SIDE=PIXEL_SPACING*DIMENSION;
		canvas.setPreferredSize(new Dimension(SIDE, SIDE));

		//TEST CUBE
		test();

		//PLAYER
		camera=new Camera3D(SIDE, SIDE, Math.PI/2, Math.PI/2); //ultimi 2 valori moltiplicati per 2
		player=new PlayerMovement(camera);
		canvas.getFrame().addKeyListener(player);

		//StartClock and setvisible
		canvas.setVisible(true);
		canvas.start(ConstantsManager.getIntValueOrCrash("fps"));
		new Clock(player, (int) (1000/ConstantsManager.getIntValueOrCrash("cps"))).start();
	}


	//TEST CUBES
	private ArrayList<Vector3D> testCubi=new ArrayList<Vector3D>();
	//private ArrayList<Vector3D> testGraph=new ArrayList<Vector3D>();
	private void test() {
		int max, pixel;
		/*for(int i=0; i<1; i++) {
			for(int i2=0; i2<1; i2++) {
				testCubi.add(new Vector3D(i, -3, i2));

			}
		}*/

		max=0; pixel=9;
		max = max/2;
		for(int i=-max; i<=max; i++) {
			for(int i2=-max; i2<=max; i2++) {
				for(int i3=-max; i3<=max; i3++) {
					testCubi.add(new Vector3D(i*pixel, i2*pixel, i3*pixel));

				}
			}
		}
		/*
		max=10; pixel=2;
		for(int i=-max; i<=max; i++) {
			for(int i2=-max; i2<=max; i2++) {
				testGraph.add(new Vector3D(i*pixel,  (i*i+i2*i2)*pixel/10, i2*pixel)); // extra Math.abs(i2*i*pixel)/10
			}
		}*/
	}

	//PAINT
	/**White is DefaultColor*/
	private void setColorByCostant(String key, Graphics2D graphics) {
		String colorExa=ConstantsManager.getStringValue(key);

		graphics.setColor(Color.white);
		if(colorExa!=null){
			graphics.setColor(Color.decode("#"+colorExa));
		}
	}
	@Override public void paintComponent(Graphics2D graphics) {
		//BACKGROUND
		setColorByCostant("backgroundColor", graphics);
		graphics.fillRect(0, 0, SIDE, SIDE);


		//GRID
		setColorByCostant("gridColor", graphics);
		for(int i=0; i<DIMENSION; i++) {
			int l=PIXEL_SPACING*i;
			graphics.drawLine(0, l, SIDE, l);
			graphics.drawLine(l, 0, l, SIDE);
		}
		graphics.drawOval(0, 0, SIDE, SIDE);

		//TEXT
		setColorByCostant("textColor", graphics);
		String strings[]=("GUARDO: "+camera.getLookingDirection().toStringAbbrev()+
				"\nPOSIZ.: "+camera.getPosition().toStringAbbrev()).split("\n");

		for(int i=0; i<strings.length; i++) {
			graphics.drawString(strings[i], 10, 20*(i+1));
		}


		//CUBE
		setColorByCostant("lineColor", graphics);
		for(int i=0; i<testCubi.size(); i++) {
			drawCube(graphics, testCubi.get(i));
		}

		//GRAPH
		/*for(int i=0; i<testGraph.size(); i++) {
			for(int i2=0; i2<testGraph.size(); i2++) {
				Vector3D v1=testGraph.get(i);
				Vector3D v2=testGraph.get(i2);
				Vector2D b1=new Vector2D(v1.getX(), v1.getZ());
				Vector2D b2=new Vector2D(v2.getX(), v2.getZ());

				double dist=b1.distance(b2);
				if(dist<=2.5) { //TODO remove (remove line useless)
					drawLine(graphics, v1, v2);
				}

			}
		}*/


	}
	private final Vector3D up=new Vector3D(0, 1, 0), right=new Vector3D(0, 0, 1), forward=new Vector3D(1, 0, 0),
			forwardUp=new Vector3D(1, 1, 0), forwardRight=new Vector3D(1, 0, 1), upRight=new Vector3D(0, 1, 1),
			forwardUpRight=new Vector3D(1, 1, 1);
	private void drawCube(Graphics2D graphics, Vector3D position) {

		drawLine(graphics, position, Vector3D.getSummed(position, up));
		drawLine(graphics, position, Vector3D.getSummed(position, right));
		drawLine(graphics, position, Vector3D.getSummed(position, forward));


		drawLine(graphics, Vector3D.getSummed(position, up), Vector3D.getSummed(position, upRight));
		drawLine(graphics, Vector3D.getSummed(position, up), Vector3D.getSummed(position, forwardUp));
		drawLine(graphics, Vector3D.getSummed(position, right), Vector3D.getSummed(position, upRight));
		drawLine(graphics, Vector3D.getSummed(position, right), Vector3D.getSummed(position, forwardRight));
		drawLine(graphics, Vector3D.getSummed(position, forward), Vector3D.getSummed(position, forwardUp));
		drawLine(graphics, Vector3D.getSummed(position, forward), Vector3D.getSummed(position, forwardRight));


		drawLine(graphics, Vector3D.getSummed(position, forwardRight), Vector3D.getSummed(position, forwardUpRight));
		drawLine(graphics, Vector3D.getSummed(position, upRight), Vector3D.getSummed(position, forwardUpRight));
		drawLine(graphics, Vector3D.getSummed(position, forwardUp), Vector3D.getSummed(position, forwardUpRight));
	}
	private void drawLine(Graphics2D graphics, Vector3D v1,  Vector3D v2) {
		Vector2D a, b;

		if((a=camera.getOnScreenCoord(v1))==null) return;
		if((b=camera.getOnScreenCoord(v2))==null) return;

		graphics.drawLine((int)a.getX(), (int)a.getY(), (int)b.getX(), (int)b.getY());
	}


}
