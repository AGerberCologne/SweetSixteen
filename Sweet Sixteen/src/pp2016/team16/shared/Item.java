package pp2016.team16.shared;

public class Item {

	public int itemId;
	public String name;
	public int[] pos;
	
	public Item(int ID){
		this.itemId = ID;
	}
//GETTER
	public int getItemId() {
		return itemId;
	}

	public int[] getPosition() {
		return pos;
	}
	
	public String getName() {
		return name;
	}

	
//SETTER
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPosition(int[] pos) {
		this.pos = pos;
	}

}


