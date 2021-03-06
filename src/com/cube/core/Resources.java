package com.cube.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;


public class Resources {
	
	private static FileInputStream fstream;
	private static DataInputStream in;
	private static BufferedReader br;
	public static Map map;
	public static ArrayList<Light> lights;
	public static ArrayList<Clan> clans;
	public static ArrayList<Entity> entities;
	public static Object[] objectLibrary;
	
	public static void initialize() {
		entities = new ArrayList<Entity>();
		lights = new ArrayList<Light>();
		clans = new ArrayList<Clan>();
		
		map = new Map(10);
		
		FileLogger.logger.log(Level.INFO, "Resources initialized");
	}
	
	public static void loadLevel(String file) {
		try {
			//sound = new Sound();
			fstream = new FileInputStream(file);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			parse(br);
			for (Clan c : clans) {
				c.process();
			}
			in.close();
			//if (sound.soundCount() > 0) sound.create();
		} catch (Exception e) {
			System.err.println("Error loading level: " + e.getMessage());
			e.printStackTrace();
		}
		//Graphics.loadCamera();
	}
	
	public static void parse(BufferedReader br) throws IOException {
		String strLine;
		
		while ((strLine = br.readLine()) != null) {
			
			if (strLine.equals("<info>")) {
				while (!(strLine = br.readLine().trim()).equals("</info>")) {
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("libcount")) {
						int libcount = Integer.parseInt(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
						objectLibrary = new Object[libcount];
					}
				}
			}
			if (strLine.equals("<clan>")) {
				Clan clan = new Clan();
				while (!(strLine = br.readLine().trim()).equals("</clan>")) {
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("id")) {
						clan.id = Integer.parseInt(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("meat")) {
						clan.meat = Integer.parseInt(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("berry")) {
						clan.berry = Integer.parseInt(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("farmer")) {
						clan.farmer = Integer.parseInt(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("hunter")) {
						clan.hunter = Integer.parseInt(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("builder")) {
						clan.builder = Integer.parseInt(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("warrior")) {
						clan.warrior = Integer.parseInt(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("color")) {
						clan.color = parseFloatArray(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
				}
				clans.add(clan);
			}
			if (strLine.equals("<map>")) {
				while (!(strLine = br.readLine().trim()).equals("</map>")) {
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("file")) {
						map.file = strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<'));
						//loadLocalFile(map.file, map);
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("scale")) {
						map.scale = Integer.parseInt(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("colorID")) {
						map.colorID = parseIntArray(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
				}
			}
			if (strLine.equals("<effect>")) {
				Effect effect = new Effect();
				while (!(strLine = br.readLine().trim()).equals("</effect>")) {
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("position")) {
						effect.position = parseFloatArray(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("scale")) {
						effect.scale = parseFloatArray(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("count")) {
						effect.particleCount = Integer.parseInt(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("color")) {
						effect.color = parseFloatArray(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
				}
				effect.initializeParticles();
				Physics.effects.add(effect);
			}
			/* Objects in the world */
			if (strLine.equals("<entity>")) {
				Entity entity = new Entity();
				while (!(strLine = br.readLine().trim()).equals("</entity>")) {
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("id")) {
						entity.id = Integer.parseInt(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("position")) {
						entity.position = parseFloatArray(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("scale")) {
						entity.scale = Float.parseFloat(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("rotation")) {
						entity.rotation = parseFloatArray(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
				}
				entities.add(entity);
			}
			/* Add models to the model library */
			if (strLine.equals("<lib>")) {
				Object object = new Object();
				while (!(strLine = br.readLine().trim()).equals("</lib>")) {
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("id")) {
						object.id = Integer.parseInt(strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<')));
					}
					if (strLine.substring(strLine.indexOf('<')+1,strLine.indexOf('>')).equals("file")) {
						object.file = strLine.substring(strLine.indexOf('>')+1,strLine.lastIndexOf('<'));
					}
				}
				loadLocalFile(object.file, object);
				objectLibrary[object.id] = object;
			}
		}
	}
	
	public static void loadLocalFile(String filename, Object we) {
		try {
			OBJParser parser = new OBJParser(filename);
			we.vertexArray = parser.v;
			we.vertexNormalArray = parser.vn;
			we.textureArray = parser.t;
			we.polyfaceArray = parser.f;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Function to read in 3 space-separated floats (e.g. 8.1 3.7 9.9) and store them in a float array
	 * (e.g. ar[3] = {8.1, 3.7, 9.9}
	 */
	public static float[] parseFloatArray(String s) {
		int idx = 0;
		float ret[] = new float[3];
		String[] tokens = s.split("\\s");

		for (String token : tokens) {
			ret[idx++] = Float.parseFloat(token);
		}
		return ret;
	}
	
	/* Function to read in 3 space-separated ints (e.g. 8 3 9) and store them in an int array
	 * (e.g. ar[3] = {8, 3, 9}
	 */
	public static int[] parseIntArray(String s) {
		int idx = 0;
		int ret[] = new int[3];
		String[] tokens = s.split("\\s");
		
		for (String token : tokens) {
			ret[idx++] = Integer.parseInt(token);
		}
		return ret;
	}
}
