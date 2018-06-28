package logic;

import org.joml.Vector3f;

import area.AreaControle;
import main.Settings;
import main.grphics.Camera;
import main.grphics.Projection;

public class CameraHandler {

	public boolean implicitMovement;
	
	public Vector3f pos;
	public Vector3f rot;
	
	private Vector3f rotL;
	private Vector3f rotS;
	
	private static final float SMOOTH_ANGLE = 5f;
	
	private Camera camera;
	private Projection projection;
	
	private static float MAX_HEIGHT = 60;
	private static final float MIN_HEIGHT = 1.1f;
	private static final float MIN_HEIGHT_ABS = 0.9f;
	private static final float STANDARD_CAMERA_DIST = 1.9f;
	
	private static final float FHD = 3f;
	
	private int scrolls;
	private int scrollTerminater;
	
	private float flightHeight = MAX_HEIGHT;
	private float trueHeight = MAX_HEIGHT;
	
	public float mouseX;
	public float mouseY;
	
	private float moveToX;
	private float moveToY;
	private boolean moveNeeded;
	
	private final AreaControle area;
	
	public CameraHandler(AreaControle a){
		pos = new Vector3f(0, 0, MAX_HEIGHT);
		rot = new Vector3f(0, 0, 0);
		
		rotL = new Vector3f(0, 0, 0);
		rotS = new Vector3f(0, 0, 0);
		
		area = a;
	}
	
	public void link(Camera c, Projection p){
		projection = p;
		camera = c;
		camera.updateRayTrace(projection);
		projectionViewChanged();
	}
	
	public void sync(){
		sync(true);
	}
	
	private void sync(boolean scroll){
		if(scrollTerminater <= 0 && scrolls != 0)
			scrolls = 0;
		//Scrolling
		if(scrolls>0 && scroll){
			singleScroll(true);
			scrolls-=main.GameControle.timePassed;
			if(scrolls<0)scrolls = 0;
			scrollTerminater-=main.GameControle.timePassed;
		}
		if(scrolls<0 && scroll){
			singleScroll(false);
			scrolls+=main.GameControle.timePassed;
			if(scrolls>0)scrolls = 0;
			scrollTerminater-=main.GameControle.timePassed;
		}
		
		if(scroll){
			rotL.x = smoothAngle(rotL.x, rotS.x);
			rotL.z = smoothAngle(rotL.z, rotS.z);
		}
		
		Vector3f p = new Vector3f(0, 0, trueHeight+STANDARD_CAMERA_DIST);
		p.rotateX((float)Math.toRadians(rot.x+rotL.x));
		p.rotateZ(-(float)Math.toRadians(rot.z+rotL.z-180));
		
		camera.setPosition(pos.x+p.x, pos.y+p.y, pos.z+p.z+getHeightAdd(pos.x+p.x, pos.y+p.y));
		camera.setRotation(rot.x+rotL.x, rot.y, rot.z+rotL.z);
		
		camera.updateRayTrace(projection);
		
		Vector3f pf = camera.getRayTrace(mouseX, 1f-mouseY);
		toStandardPlane(pf, (trueHeight+STANDARD_CAMERA_DIST)*2, 1);
		camera.rotationPos = pf.add(camera.pos);//new Vector3f(moveToX, moveToY, flightHeight/FHD);
		
		if(moveNeeded && Math.abs(rotL.x) <= 0.01f){
			float px;
			float py;
			float lastDistX;
			float lastDistY;
			
			float mx;
			float my;
			
			float r = 100;
			
			for (int i = 50; i > 0; i--) {
				r /= 2;
				Vector3f v = camera.getRayTrace(mouseX, 1f-mouseY);
				//float f = (trueHeight+STANDARD_CAMERA_DIST)/v.z;
				//v.mul(f);
				toStandardPlane(v, (trueHeight+STANDARD_CAMERA_DIST)*2, 0);
				
				px = v.x+camera.pos.x;
				py = v.y+camera.pos.y;
				
				lastDistX = Math.abs(px-moveToX);
				lastDistY = Math.abs(py-moveToY);
				
				if(px<moveToX){
					mx = r;
				}else{
					mx = -r;
				}
				
				if(py<moveToY){
					my = r;
				}else{
					my = -r;
				}
				
				pos.x += mx;
				pos.y += my;
				
				moveNeeded = false;
				sync(false);
				
				v = camera.getRayTrace(mouseX, 1f-mouseY);
				//float f = (trueHeight+STANDARD_CAMERA_DIST)/v.z;
				//v.mul(f);
				toStandardPlane(v, (trueHeight+STANDARD_CAMERA_DIST)*2, 0);
				
				px = v.x+camera.pos.x;
				py = v.y+camera.pos.y;
				boolean roh = false;
				if(lastDistX<Math.abs(px-moveToX)){
					pos.x -= mx;
					roh = true;
				}
				if(lastDistY<Math.abs(py-moveToY)){
					pos.y -= my;
					roh = true;
				}
				if(roh)
					sync(false);
				
			}
		}
		
		if(scroll){
			boolean rs = false;
			if(pos.x > getMaxDistToCenter()){
				pos.x = getMaxDistToCenter();
				rs = true;
			}
			if(pos.x < -getMaxDistToCenter()){
				pos.x = -getMaxDistToCenter();
				rs = true;
			}
			if(pos.y > getMaxDistToCenter()){
				pos.y = getMaxDistToCenter();
				rs = true;
			}
			if(pos.y < -getMaxDistToCenter()){
				pos.y = -getMaxDistToCenter();
				rs = true;
			}
			if(rs)
				sync(false);
		}
	}
	
	private float getHeightAdd(float x, float y){
		float f = flightHeight/(MAX_HEIGHT*3f);
		if(f>1)return 0;
		float hAvrg = area.getNodeHeight(x, y);
		hAvrg += area.getNodeHeight(x+flightHeight/4, y);
		hAvrg += area.getNodeHeight(x, y+flightHeight/4);
		hAvrg += area.getNodeHeight(x-flightHeight/4, y);
		hAvrg += area.getNodeHeight(x, y-flightHeight/4);
		return (hAvrg/5f)*(1-f);
	}
	
	private float getMaxDistToCenter(){
		return (main.GameController.getMapSize()/2+main.GameController.getMapSize()/6)
				* (1-flightHeight/MAX_HEIGHT);
	}
	
	public void scroll(int ammount){
		ammount *= 12;
		ammount /= Settings.scrollDiv;
		scrolls += ammount;
		
		scrollTerminater = 200;//Scrolling continues for max. half second
	}
	
	public void singleScroll(boolean up){
		Vector3f v = camera.getRayTrace(mouseX, 1f-mouseY);
		//float f = (trueHeight+STANDARD_CAMERA_DIST)/v.z;
		//v.mul(f);
		toStandardPlane(v, (trueHeight+STANDARD_CAMERA_DIST)*2, 0);
		moveNeeded = true;
		moveToX = v.x+camera.pos.x;
		moveToY = v.y+camera.pos.y;
		
		float scrollSpeed = 0.003f*main.GameControle.timePassed;
		
		scrollSpeed += 1;
		if(up){
			flightHeight = flightHeight*scrollSpeed;
		}else{
			flightHeight = flightHeight*(1/scrollSpeed);
		}
		if(flightHeight>MAX_HEIGHT){
			flightHeight = MAX_HEIGHT;
			if(scrolls<0)
				scrolls = 0;
		}
		
		if(flightHeight>MIN_HEIGHT){
			trueHeight = flightHeight;
		}else{
			trueHeight = MIN_HEIGHT;
		}
		
		pos.z = flightHeight/FHD;
		
		setXrotToZoomLevel();
	}
	
	public void setXrotToZoomLevel(){
		float u = MAX_HEIGHT-flightHeight;
		u-=MAX_HEIGHT/3;
		if(u<0){
			rot.x = 0;
		}else if(flightHeight<MIN_HEIGHT_ABS){
			if(scrolls>0)
				scrolls = 0;
			flightHeight = MIN_HEIGHT_ABS;
		}else{
			u/=(MAX_HEIGHT*2)/3;
			rot.x = -45f*u*u;
		}
	}
	
	public void moveRot(float x, float y, float z){
		rot.x += x;
		rot.y += y;
		rot.z += z;
	}
	
	public void setRotFC(float x, float z){
		rotS.x = x;
		rotS.z = z;
	}
	
	public void moveRotFC(float x, float z){
		rotS.x += x;
		rotS.z += z;
		if(rot.x+rotS.x>0)rotS.x = -rot.x;
		if(rot.x+rotS.x<-89)rotS.x = -89-rot.x;
		if(rotS.z<-180)rotS.z += 360;
		if(rotS.z>180)rotS.z -= 360;
		
		rotL.x = rotS.x;
		rotL.z = rotS.z;
	}
	
	public void move(float x, float y){
		Vector3f v1 = camera.getRayTrace(0.1f, 0.1f);
		Vector3f v2 = camera.getRayTrace(0.9f, 0.1f);
		toStandardPlane(v1, 0, 0);
		toStandardPlane(v2, 0, 0);
		v1.z = 0;
		v2.z = 0;
		float m = v1.distance(v2);
		m/=30+(60*flightHeight/MAX_HEIGHT);
		x*=m;
		y*=m;
		if (x != 0) {
			pos.y += (float) Math.sin(Math.toRadians(rot.z+rotL.z)) * -1.0f * x;
			pos.x += (float) Math.cos(Math.toRadians(rot.z+rotL.z)) * x;
		}
		if (y != 0) {
			pos.y += (float) Math.sin(Math.toRadians(rot.z+rotL.z - 90)) * -1.0f * y;
			pos.x += (float) Math.cos(Math.toRadians(rot.z+rotL.z - 90)) * y;
		}
	}
	
	private static float smoothAngle(float i, float s){
		if(s>i){
			i += SMOOTH_ANGLE;
			if(s<i)return s;
			return i;
		}
		i -= SMOOTH_ANGLE;
		if(s>i)return s;
		return i;
	}
	
	private void toStandardPlane(Vector3f v, float max, float zTarget){
		zTarget = camera.pos.z-zTarget;
		
		if(Math.abs(v.z)<0.001f)
			return;
		
		zTarget /= v.z;
		
		v.mul(-zTarget);
	}
	
	public void setMapSize(int newSize){
		projectionViewChanged();
	}
	
	/**
	 * Needs to be called if FOV has changed;
	 * Updates MAX_HEIGHT so that the whole map can be seen
	 */
	public void projectionViewChanged(){
		float dis = main.GameController.getMapSize()/2f;
		dis = (float)Math.sqrt(dis*dis*2);
		
		MAX_HEIGHT = (float)(dis/Math.tan(Math.toRadians(Settings.fov)));
		MAX_HEIGHT *= 1.9f;
		flightHeight = MAX_HEIGHT;
		singleScroll(true);
	}
	
	public static float getMaxHeight(){
		return MAX_HEIGHT;
	}
}
