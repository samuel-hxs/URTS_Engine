package entitys.paint;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import mdesl.graphics.Texture;
import mdesl.graphics.TextureRegion;
import mdesl.graphics.glutils.VertexArray;

/**
 * Will Import .obj (Object-Files) 
 * @author Sven T. Schneider
 */
public class FileImport_Obj extends FileImport{

	private List<Vector3f> vertices;
	private List<Vector2f> texture;
	private List<Vector3f> normals;
	
	public String name = "EMPTY";
	
	private String currentGroupName = "";
	private int currentSmooth = 0;
	private int triangleCount;
	
	private List<Group> groups;
	private Group currentGroup;
	
	public OnMapObject[] compiled;
	
	private TextureRegion tr;
	
	/**
	 * A Texture shared by every Model on the map
	 */
	public static Texture allModelTexture;
	
	public FileImport_Obj(){
		vertices = new ArrayList<>();
		texture = new ArrayList<>();
		normals = new ArrayList<>();
		groups = new ArrayList<>();
		
		if(allModelTexture == null)
			loadTexture();
		tr = new TextureRegion(allModelTexture, 0, 0, 100, 100);
	}
	
	public static void loadTexture(){
		try {
			allModelTexture = new Texture(utility.ResourceLoader.loadResource("res/m3d/tex.png"));
		} catch (IOException e) {
			debug.Debug.println("FATAL: Error loading resource!", debug.Debug.FATAL);
			debug.Debug.printException(e);
		} 
	}

	@Override
	protected OnMapObject[] load(BufferedReader b) throws Exception {
		debug.Debug.println("*Loading 3D-Model (.obj)...");
		vertices.clear();
		texture.clear();
		normals.clear();
		groups.clear();
		triangleCount = 0;
		String s;
		while ((s = b.readLine()) != null) {
			processString(s);		
		}
		
		postProcess();
		compile();
		
		debug.Debug.println("Done! ("+triangleCount+" Triangle in "+compiled.length+" Group(s) )");
		return compiled;
	}
	
	private void processString(String s){
		s = s.trim();
		if(s.startsWith("#"))return;//Comments
		if(s.startsWith("o")){
			name = s.substring(2);
		}
		if(s.startsWith("g")){
			currentGroupName = s.substring(2);
			currentGroup = getGroup(currentGroupName);
		}
		if(s.startsWith("s")){
			if(s.contains("off"))currentSmooth = 0;
			else currentSmooth = Integer.parseInt(s.substring(1).trim());
		}
		
		String[] st = s.split(" ");
		
		if(s.startsWith("v")){
			vertices.add(new Vector3f(Float.parseFloat(st[1]), Float.parseFloat(st[2]), Float.parseFloat(st[3])));
		}
		if(s.startsWith("vn")){
			normals.add(new Vector3f(Float.parseFloat(st[1]), Float.parseFloat(st[2]), Float.parseFloat(st[3])));
		}
		if(s.startsWith("vt")){
			texture.add(new Vector2f(Float.parseFloat(st[1]), Float.parseFloat(st[2])));
		}
		
		//Face
		if(st[0].startsWith("f")){
			if(currentGroup == null) currentGroup = getGroup("default");
			
			Face f = new Face();
			fillFace(f, st[1], 0);
			fillFace(f, st[2], 1);
			fillFace(f, st[3], 2);
			f.smooth = currentSmooth;
			triangleCount++;
			
			currentGroup.faces.add(f);
			
			if(st.length == 5){
				f = new Face();
				fillFace(f, st[1], 0);
				fillFace(f, st[4], 1);
				fillFace(f, st[3], 2);
				f.smooth = currentSmooth;
				triangleCount++;
				
				currentGroup.faces.add(f);
			}
		}
	}
	
	private void fillFace(Face f, String s, int i){
		String[] st = s.split("/");
		
		f.vertices[i] = Integer.parseInt(st[0]);
		if(st.length==1)return;
		if(st[1].length()>=1){
			f.texture[i] = Integer.parseInt(st[1]);
		}
		if(st.length==2)return;
		if(st[2].length()>=1){
			f.normal[i] = Integer.parseInt(st[2]);
		}
	}
	
	private Group getGroup(String g){
		for (Group o : groups) {
			if(o.name.compareTo(g) == 0)
				return o;
		}
		Group o = new Group(g);
		groups.add(o);
		return o;
	}
	
	/**
	 * A List of triangles inside a Group, will be later compiled to an OnMapObject
	 */
	private class Group{
		
		private final String name;
		
		private List<Face> faces;
		
		private Group(String n){
			name = n;
			faces = new ArrayList<>();
		}
	}
	
	/**
	 * A single Triangle
	 */
	private class Face{
		//Indices of the corresponding Vectors.
		private int[] vertices;
		private int[] texture;
		private int[] normal;
		private Vector3f[] processedNormals;
		
		private int smooth;
		
		private Face(){
			vertices = new int[3];
			texture = new int[3];
			normal = new int[3];
			processedNormals = new Vector3f[3];
		}
	}
	
	/**
	 * Takes the Group and Face information and compiles it to {@link OnMapObject}
	 */
	private void compile(){
		compiled = new OnMapObject[groups.size()];
		int i = 0;
		for (Group g : groups) {
			compiled[i] = singleCompile(g);
			i++;
		}
	}
	
	/**
	 * @return A single compiled group
	 */
	private OnMapObject singleCompile(Group g){
		OnMapObject obj = new OnMapObject(g.faces.size()*3, tr.getTexture());
		VertexArray data = obj.getData();
		
		Vector2f[] empty = new Vector2f[]{
				 new Vector2f(tr.getU(), tr.getV()),
				 new Vector2f(tr.getU2(), tr.getV()),
				 new Vector2f(tr.getU(), tr.getV2())
		};
		
		for (Face f : g.faces) {
			for (int i = 0; i < f.vertices.length; i++) {
				Vector2f t = empty[i];
				if(f.texture[i] != 0) t = texture.get(f.texture[i]-1);
				put(data, vertices.get(f.vertices[i]-1), t, f.processedNormals[i]);
			}
		}
		obj.flip();
		return obj;
	}
	
	private void put(VertexArray data, Vector3f v, Vector2f t, Vector3f n){
		data.put(v.x).put(v.y).put(v.z).put(1).put(1).put(1).put(t.x).put(t.y).put(n.x).put(n.y).put(n.z);
		data.countIncr();
	}
	
	/**
	 * Takes Normals and Smooth-Values and processes them to the processedNormals
	 */
	private void postProcess(){
		for (Group g : groups) {
			for(Face f : g.faces){
				for (int i = 0; i < f.normal.length; i++) {
					if(f.normal[i] == 0){//TODO 0-index: process Normal from face
						f.processedNormals[i] = new Vector3f();
					}else{
						f.processedNormals[i] = normals.get(f.normal[i]-1);
					}
				}
			}
		}
		
		//Smoothness
		for (Group g : groups) {
			for(Face f : g.faces){
				if(f.smooth > 0)
				for (int i = 0; i < f.normal.length; i++) {
					f.processedNormals[i] = processSmooth(f.smooth, f.vertices[i], true);
				}
			}
		}
	}
	
	private Vector3f processSmooth(int s, int edge, boolean grouped){
		Vector3f v = new Vector3f();
		
		for (Group g : groups) {
			for(Face f : g.faces){
				if(f.smooth != s && grouped)continue;
				
				for (int j = 0; j < f.normal.length; j++) {
					if(f.vertices[j] == edge && f.normal[j] > 0)
						v.add(normals.get(f.normal[j]-1));
				}
			}
		}
		
		return v.normalize();
	}
	
}
