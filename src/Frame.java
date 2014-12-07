
public class Frame {
	
	private int processID;
	private int pageNumber;
	
	public Frame(){
		this.processID = 0;
		this.pageNumber = 0;
	}
	
	public int getProcessID(){ return this.processID; }
	
	public int getPageNumber(){ return this.pageNumber; }
	
	public void setProcessID(int ID){ this.processID = ID; }
	
	public void setPageNumber(int page){ this.pageNumber = page; }

}
