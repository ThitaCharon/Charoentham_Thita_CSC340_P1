import java.util.ArrayList;

public class Instructor extends Thread {

	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+ getName()+": "+ m);
	}
	private final ArrayList<Thread> sharedQueue;
	private boolean waitExam = true; 
	private boolean postGrade = true; 
	private boolean busywait = true;
	private Classroom cs;
	private int gradeTable[][];

	Instructor(Classroom cs, ArrayList<Thread> share){
		gradeTable = new int[14][3];
		sharedQueue = share;
		this.cs = cs;
		setName("Instructor ");
		msg("is walking to class");
	}


	public void run(){
		try {

			while (cs.getNexam()<=3)
			{

				setArrive(true);
				Thread.sleep((long) (5000 * Math.random()));
				msg("let studens.... Goo!!");
				setArrive(false);	//set wait to false let student in class

				msg("wait students find seat");		
				Thread.sleep(5000);
				setArrive(true);
				
				setWaitExam(false);
				// prof will start the exam and will sleep through out the exam for 5 secound
				msg("Prof is sleeping...zZZZzzz... during Exam : "+ cs.getNexam());
				
				// sleep during exam
				Thread.sleep(6000);
				
				msg("Beging collect exam : " + cs.getNexam());
				cs.incNexam();
				
				// when exam end professor wake up each student in order and give the grade
				gradedAndInterrupptToLeave();
			
				// clear shareQueue for the next exam
				for (int i=sharedQueue.size()-1; i>=0;i--){
					sharedQueue.remove(i);	}
				
				msg("will take a break");
				Thread.sleep((long) (5000 * Math.random()));

			}

			postGrade();
			setPostGrade(); // to allow students to go home


		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void gradedAndInterrupptToLeave(){
		for (int i=0; i<sharedQueue.size();i++){	
			// get student id get a grade and let student leave the room
			int id = Integer.valueOf((sharedQueue.get(i).getName().substring(8)));
			gradeTable[id][cs.getNexam()-2] = getPoint();
			// instructor interrupt students in order
			msg("prof interrupted " + sharedQueue.get(i).getName());
			sharedQueue.get(i).interrupt();
		}
	}

	private void postGrade() {
		for (int i=0; i<14;i++){	
			System.out.print("Student_"+ i);
			for(int j=0; j<3; j++){
				System.out.print("    " + gradeTable[i][j]);
			}
			System.out.println();
		}
	}
	public synchronized boolean noGradePost(){
		return postGrade;
	}

	public synchronized void setPostGrade(){
		postGrade = false;
	}
	private int getPoint() {
		return  ((int) ((Math.random() * (100 - 10))) + 10);
	}

	private synchronized boolean setArrive(boolean bool){
		return busywait = bool;
	}
	public synchronized boolean isArrive(){
		return busywait;
	}

	public synchronized boolean waitExam(){
		return waitExam;
	}
	public synchronized void setWaitExam(boolean bool){
		waitExam = bool;
	}
}