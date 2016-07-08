package test;

import pp2016.team16.client.comm.ClientComm;
import pp2016.team16.client.engine.ClientEngine;
import pp2016.team16.client.gui.HindiBones;
import pp2016.team16.server.comm.ServerComm;
import pp2016.team16.server.engine.ServerEngine;


public class HindiBonesMain {

	public static final int BOX = 32;
	public static final int WIDTH = 17, HEIGHT = 17;
	
	public static void main(String[] args){
			
	 new HindiBones(BOX*WIDTH, BOX*HEIGHT, "Hindi"); 
		
		/* ClientEngine client = new ClientEngine();
		 try {
			client.changeLevel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */ 
	}
	

}