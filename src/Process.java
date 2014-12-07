
public class Process {

	private int ID;
	private int size;
	
	private int remainingReferences;
	private int currentWord;
	
	public Process(int ID, int size){
		this.ID = ID;
		this.size = size;
		
		this.remainingReferences = 0;
		this.currentWord = 0;
	}
	
	public int getID(){ return this.ID; }
	
	public int getSize(){ return this.size;	}
	
	public int getCurrentWord(){ return this.currentWord; }
	
	public void setCurrentWord(int word){ this.currentWord = word; }
	
	public int decRemainingReferences(){ return --this.remainingReferences; }
	
}
