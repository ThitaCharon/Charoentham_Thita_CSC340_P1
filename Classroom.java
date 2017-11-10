import java.util.ArrayList;

public class Classroom {
	public String [] namelist;
	public int capacity;
	public int Nstudents; 
	public int Nexam; // count number of exam offer
	ArrayList<String> stulist;
	public ArrayList<Thread> studentInClass;
	public Classroom ( ){
		stulist = new ArrayList<String>();
		Nstudents = 0;
		capacity = 0;
		Nexam = 1;
	}

	public synchronized int getCapacity(){
		return capacity;
	}
	public synchronized void incCapacity(){
		++capacity;
	}
	public synchronized int decCapacity(){
		return --capacity;
	}
	public synchronized void incNstudents(){
		++Nstudents;
	}
	public synchronized int getNstudents(){
		return Nstudents;
	}
	public synchronized void incNexam(){
		++Nexam;
	}
	public synchronized int getNexam(){
		return Nexam;
	}


}
