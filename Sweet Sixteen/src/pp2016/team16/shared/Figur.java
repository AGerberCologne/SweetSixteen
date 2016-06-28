package pp2016.team16.shared;

import java.awt.Image;

public abstract class Figur {

	private int xPos, yPos;
	private Image image;
	
	private int health;
	private int schaden;
	
	private int maxHealth;
		
	
	// Getter und Setter
	
	public int getMaxHealth(){
		return maxHealth;
	}
	
	public void setMaxHealth(int maxHealth){
		this.maxHealth = maxHealth;
	}
	
	public void setSchaden(int schaden){
		this.schaden = schaden;
	}
	
	public int getSchaden(){
		return schaden;
	}
	
	public void changeHealth(int change){
		health = Math.min(health + change, getMaxHealth());
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public int getHealth(){
		return health;
	}
	
	public Image getImage(){
		return image;
	}
	
	public void setImage(Image img){
		image = img;
	}
	
	public void setPos(int xPos, int yPos){
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public int getYPos(){
		return yPos;
	}
	
	public int getXPos(){
		return xPos;
	}
	
	public void hoch(){
		yPos--;
	}
	
	public void runter(){
		yPos++;
	}
	
	public void links(){
		xPos--;
	}
	
	public void rechts(){
		xPos++;
	}
}
