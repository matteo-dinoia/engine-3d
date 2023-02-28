package backend;

import utils.vectors.*;

public class Camera3D {

	//CONSTRUCTOR
	private final int HEIGHT, WIDTH;
	private final double ANGLE_VISIBLE_HORIZONTAL, ANGLE_VISIBLE_VERTICAL;
	public Camera3D(int heightPanel, int widthPanel, double visibleAngleHorizontal, double visibleAngleVertical) {
		this.HEIGHT=heightPanel;
		this.WIDTH=widthPanel;
		this.ANGLE_VISIBLE_HORIZONTAL=visibleAngleHorizontal;
		this.ANGLE_VISIBLE_VERTICAL=visibleAngleVertical;

	}


	//GETTER AND SETTER  --------------------------------------------------
	private Vector3D position=new Vector3D(0,0,0);
	private Vector3D lookingDirection=new Vector3D(1,0,0);
	public Vector3D getPosition() {
		return position;
	}
	public Vector3D getLookingDirection() {
		return lookingDirection;
	}

	//COORD conversion
	public Vector2D getOnScreenCoord(Vector3D worldCoord) {
		Vector2D v=getScreenFromLocalCoord(getLocalCoordFromWorld(worldCoord));

		if(v==null) return null;
		return new Vector2D((HEIGHT/2)+v.getX(), (WIDTH/2)-v.getY());
	}
	private Vector2D getScreenFromLocalCoord(Vector3D localCoord){
		double vAngle = localCoord.getLatitude();
		double hAngle = localCoord.getLongitude();

		if(localCoord.getX() < 0) return null;

		hAngle = localCoord.getZ()/localCoord.getX();
		vAngle = localCoord.getY()/localCoord.getX();

		//System.out.println("positionCamera -> "+position+"\nlookingCamera -> "+lookingDirection);  //DEBUG

		if(Math.abs(vAngle)>ANGLE_VISIBLE_HORIZONTAL || Math.abs(vAngle)>ANGLE_VISIBLE_VERTICAL) {
			return null;
		}
		return new Vector2D(hAngle/ANGLE_VISIBLE_HORIZONTAL*WIDTH, vAngle/ANGLE_VISIBLE_VERTICAL*HEIGHT);
	}
	private Vector3D getLocalCoordFromWorld(Vector3D worldCoord){
		Vector3D ris = Vector3D.getDiffernce(worldCoord, position);

		ris.rotateLatitude(-lookingDirection.getLatitude());
		ris.rotateLongitude(-lookingDirection.getLongitude());

		return ris;
	}

}
