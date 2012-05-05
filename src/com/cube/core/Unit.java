package com.cube.core;

import org.lwjgl.opengl.GL11;
import com.cube.util.Texture;
import com.cube.util.ShaderManager;

public class Unit extends Entity {
			
	public Unit(int _type, int _id, Clan c, Texture tex) {
		types.add(_type);
		objectID = _id;
		clanRef = c;
		
		show = true;

		this.tex = tex;
		selectionRingRotation = 0;
		
		position.x = c.position.x + 5;
		position.z = c.position.z + 5;
		
		maxHealth = curHealth = 100;
		
		max_v = .2f;
	}
	
	public void update(int timeElapsed) {
		super.update(timeElapsed);
	}
	
	@Override
	public void draw() {
		GL11.glPushMatrix();
			GL11.glLoadIdentity();
			
			GL11.glTranslated(position.x, position.y + Resources.map.getHeight((float)position.x, (float)position.z), position.z);
			GL11.glRotatef(rotation[0], 1, 0, 0);
			GL11.glRotatef(rotation[1], 0, 1, 0);
			GL11.glRotatef(rotation[2], 0, 0, 1);
			GL11.glScalef(scale, scale, scale);

			if (Graphics.colorPicking) {
				GL11.glColor3ub((byte)colorID[0], (byte)colorID[1], (byte)colorID[2]);
				Resources.objectLibrary.get(objectID).drawOBJ();
			}
			else {
				GL11.glColor3f(color[0], color[1], color[2]);
				
				//Bind shaders
				Graphics.shaderManager.bindShader(ShaderManager.HEMISPHERE);
				if(tex == null) {
					Resources.objectLibrary.get(objectID).draw();
				}else{
					Resources.objectLibrary.get(objectID).draw(tex);
				}
				
				//Unbind shaders
				Graphics.shaderManager.unbindShader(ShaderManager.HEMISPHERE);
				
				inventory.draw(this);
				
				if (this == Input.selectedEntity)
					drawSelectionRing();
			}
		GL11.glPopMatrix();
	}
}
