package backend;

import java.awt.event.*;
import utils.clocks.ClockListener;
import utils.settings.ConstantsManager;
import utils.vectors.*;

public class PlayerMovement implements KeyListener, ClockListener{
	private Camera3D camera;
	public PlayerMovement(Camera3D camera) {
		this.camera = camera;
	}


	//KEYBORD
	@Override public void keyTyped(KeyEvent e) {}
	@Override public void keyPressed(KeyEvent e) {
		setPlayerMovementByKey(e.getKeyChar(), true);
	}
	@Override public void keyReleased(KeyEvent e) {
		setPlayerMovementByKey(e.getKeyChar(), false);
	}

	//MOVEMENT
	private boolean left=false, right=false, forward=false, backward=false, up=false, down=false;
	private boolean lookLeft=false, lookRight=false, lookUp=false, lookDown=false;
	private void setPlayerMovementByKey(char key, boolean value) {
		//MOVEMENT
		if(isAxistPressed(key, "left"))  left=value;
		if(isAxistPressed(key, "right"))  right=value;
		if(isAxistPressed(key, "forward"))  forward=value;
		if(isAxistPressed(key, "backward"))  backward=value;
		if(isAxistPressed(key, "up"))  up=value;
		if(isAxistPressed(key, "down"))  down=value;

		//LOOK
		if(isAxistPressed(key, "lookLeft"))  lookLeft=value;
		if(isAxistPressed(key, "lookRight"))  lookRight=value;
		if(isAxistPressed(key, "lookUp"))  lookUp=value;
		if(isAxistPressed(key, "lookDown"))  lookDown=value;
	}
	private boolean isAxistPressed(char key, String nameKeyConstant) {
		String keySetted=ConstantsManager.getStringValue(nameKeyConstant);
		return (keySetted!=null && keySetted.length()>=1 && keySetted.charAt(0)==key);
	}


	//UPDATE  ---------------------------------------------
	@Override
	public void clockUpdate(int millsPassed) {
		//MOVEMENT
		double speed=ConstantsManager.getIntValueOrCrash("speed")/1000.0/ConstantsManager.getIntValueOrCrash("cps");
		double x=(-boolToInt(backward)+boolToInt(forward))*speed;
		double y=(-boolToInt(down)+boolToInt(up))*speed;
		double z=(-boolToInt(left)+boolToInt(right))*speed;

		//TODO CHECK
		Vector2D temp=new Vector2D(x, z);
		//temp.rotateRadiant(camera.getLookingDirection().getLongitude());
		camera.getPosition().sum(Vector3D.getMultiplied(camera.getLookingDirection(), z));
		camera.getPosition().sum(Vector3D.getMultiplied(camera.getUpDirection(), y));
		camera.getPosition().sum(Vector3D.getMultiplied(camera.getRightDirection(), y));

		//ROTATION
		double speedRotation=ConstantsManager.getIntValueOrCrash("speedRotation")/1000.0/ConstantsManager.getIntValueOrCrash("cps");
		double angleV=(-boolToInt(lookDown)+boolToInt(lookUp))*speedRotation;
		double angleH=(-boolToInt(lookLeft)+boolToInt(lookRight))*speedRotation;

		camera.getLookingDirection().rotateLatitude(angleV);
		camera.getLookingDirection().rotateLongitude(angleH);


	}
	private int boolToInt(boolean b) {
		if(b)return 1;
		else return 0;
	}

}
