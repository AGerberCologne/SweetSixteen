package test;

import pp2016.team16.client.comm.ClientComm;
import pp2016.team16.client.engine.ClientEngine;
import pp2016.team16.client.gui.HindiBones;
import pp2016.team16.server.comm.ServerComm;


public class HindiBonesMain {

	public static final int BOX = 32;
	public static final int WIDTH = 17, HEIGHT = 17;
	
	public static void main(String[] args){
			
		 //*new HindiBones(BOX*WIDTH, BOX*HEIGHT, "Hindi");
		ClientComm com = new ClientComm("localhost", 10000);
		 ServerComm serv = new ServerComm(10000);
		 System.out.println("Starte Server");
		 ClientEngine client = new ClientEngine();
		 client.levelzaehler = 1;
		 try {
			client.changeLevel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}