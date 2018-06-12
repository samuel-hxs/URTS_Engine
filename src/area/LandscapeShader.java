package area;

import java.util.Arrays;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import main.grphics.Render3D;
import mdesl.graphics.glutils.ShaderProgram;
import mdesl.graphics.glutils.VertexAttrib;
import mdesl.graphics.glutils.VertexData;

/**
 * Handles the Landscape-Shader-Programm, use this shader for Landscape and Unit-Drawing
 * @author Sven T. Schneider
 */
public class LandscapeShader {

	public static final String ATTR_POSITION = "Position";
	public static final String ATTR_TEXTURE = "TextureC";
	public static final String ATTR_TEX_POS = "Tex_Pos";
	public static final String ATTR_NORMAL = "Normal";
	
	public static final String U_PROJECTION = mdesl.graphics.SpriteBatch.U_PROJ_VIEW;
	public static final String U_TRANSLATION = mdesl.graphics.SpriteBatch.U_OFFSET_MAT;
	
	public static final String U_LIGHT_STRUCT = "light";
	public static final String U_LIGHT_ATTENUATION_STRUCT = "att";
	public static final String U_LIGHT_POS = "pos";
	public static final String U_LIGHT_COLOR = "color";
	public static final String U_LIGHT_INTENSITY = "intensity";
	public static final String U_LIGHT_REFLECTANCE = "reflectanceMod";
	
	public static final String U_MATERIAL_STRUCT = "material";
	public static final String U_MATERIAL_SPECULAR_POWER = "specularPower";
	public static final String U_MATERIAL_SPECULAR_COLOR = "specularColor";
	public static final String U_MATERIAL_REFLECTANCE = "reflectance";
	
	public static final String U_ATTENUATION_CONSTANT = "constant";
	public static final String U_ATTENUATION_LINEAR = "linear";
	public static final String U_ATTENUATION_EXPONENT = "exponent";
	
	public static final String U_CAMERA_POS = "u_cameraPos";
	
	public static final String U_SKY_LIGHT_DIR = "u_skyLightDir";
	public static final String U_SKY_LIGHT_INTENSITY = "u_skyLightIntens";
	public static final String U_SKY_LIGHT_POWER = "u_skyLightPow";
	
	public static final String U_TEXTURE_MODE = "u_textureMode";
	
	public static final List<VertexAttrib> ATTRIBUTES = Arrays.asList(new VertexAttrib(0,
			ATTR_POSITION, 3), new VertexAttrib(1, ATTR_TEXTURE, 3),
			new VertexAttrib(2, ATTR_TEX_POS, 2), new VertexAttrib(3, ATTR_NORMAL, 3));
	
	public ShaderProgram shader;
	
	public LandscapeShader() throws Exception{
		shader = new ShaderProgram("res/sha/landscape", ATTRIBUTES);
	}
	
	public void prepare(Render3D r3d){
		r3d.setShader(shader, false);
		r3d.updateProjectionView();
		shader.setUniformMatrix(U_TRANSLATION, false, new Matrix4f());
		
		shader.setUniformf(U_CAMERA_POS, r3d.getCamera().pos.x, r3d.getCamera().pos.y, r3d.getCamera().pos.z);
		
		shader.setUniformf(U_LIGHT_STRUCT+"1."+U_LIGHT_INTENSITY, 0.51f);
		shader.setUniformf(U_LIGHT_STRUCT+"1."+U_LIGHT_COLOR, 1, 1, 1);
		shader.setUniformf(U_LIGHT_STRUCT+"1."+U_LIGHT_POS, 0, 0, 30);
		shader.setUniformf(U_LIGHT_STRUCT+"1."+U_LIGHT_REFLECTANCE, 1f);
		shader.setUniformf(U_MATERIAL_STRUCT+"."+U_MATERIAL_REFLECTANCE, 1f);
		shader.setUniformf(U_MATERIAL_STRUCT+"."+U_MATERIAL_SPECULAR_POWER, 1.1f);
		shader.setUniformf(U_MATERIAL_STRUCT+"."+U_MATERIAL_SPECULAR_COLOR, 0, 0, 1);
		
		shader.setUniformf(U_SKY_LIGHT_DIR, 0, 0, 1);
		shader.setUniformf(U_SKY_LIGHT_INTENSITY, 1.1f);
		shader.setUniformf(U_SKY_LIGHT_POWER, 200f);
	}
	
	public ShaderProgram getShader() {
		return shader;
	}
	
	public static void drawPlane(Vector3f[] e, float[] u, float[] v, VertexData d){
		r.zero();
		getNormal(r, e);
		Vector3f bu = e[0];
		e[0] = e[3];
		r.mul(-1);
		getNormal(r, e);
		//r.mul(-1);
		e[3] = e[0];
		e[0] = bu;
		
		r.normalize();
		
		vertexPoint(0, e, r, u, v, d);
		vertexPoint(1, e, r, u, v, d);
		vertexPoint(2, e, r, u, v, d);
		
		vertexPoint(1, e, r, u, v, d);
		vertexPoint(3, e, r, u, v, d);
		vertexPoint(2, e, r, u, v, d);
	}
	
	private static void vertexPoint(int p, Vector3f[] e, Vector3f normal, float[] u, float[] v, VertexData data){
		data.put(e[p].x).put(e[p].y).put(e[p].z).put(1).put(1).put(1)
			.put(u[p]).put(v[p]).put(normal.x).put(normal.y).put(normal.z);
		data.countIncr();
	}
	
	private static Vector3f v = new Vector3f();
	private static Vector3f u = new Vector3f();
	private static Vector3f r = new Vector3f();
	public static Vector3f getNormal(Vector3f res, Vector3f[] edges){
		u.set(edges[1]);
		u.sub(edges[0]);
		v.set(edges[2]);
		v.sub(edges[0]);
		
		res.x += (u.y * v.z) - (u.z * v.y);
		res.y += (u.z * v.x) - (u.x * v.z);
		res.z += (u.x * v.y) - (u.y * v.x);
		
		return res;
	}
	
}
