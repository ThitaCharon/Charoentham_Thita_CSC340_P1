import java.util.ArrayList;

public class Student extends Thread implements Runnable {

	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+ getName()+": "+ m);
	}

	//private final static int numStudens = 14;
	public int id;
	private Instructor prof;
	private Classroom cs;
	private String name;
	public static int MAX =10;
	private final ArrayList<Thread> sharedQueue;
	private int count;
	//private boolean wait = true;


	Student(int idN, Instructor instructor, Classroom csc, ArrayList<Thread> share){
		prof = instructor;
		sharedQueue = share;
		name = ("student_"+ idN);
		setName(name);
		id = idN;
		cs= csc;	
		// student come to school
	}

	public void run(){

		// sleep for random time 
		try{
			//msg("id   "+(int) Thread.currentThread().getId());
			msg("is coming to school");
			Thread.sleep((long) (6000 * Math.random()));

			// count how many exam student already take 
			while(cs.Nexam < 3){
				//if already took 2 exam then dont need the take the 3rd exam 
				if (count ==2){
					break;	}
				takeExam(); /* in takeExam */
				if (count ==2){
					break;	}
				// student is miss the exam will be yield twice 
				whoMiss();
				//	System.out.println(" Any student in queue" + sharedQueue.size());
			}

			// if student don't need to take final exam will wait until grade is posted
			if (count ==2 && prof.noGradePost()){ 
				msg("already took 2 exam just wait until grade is posted"); }
			
			// Everyone will wait until instructor post the grade
			while(prof.noGradePost()){}
		}catch(InterruptedException e) { e.printStackTrace(); 	}

		joinAndLeave();

	}// end run

	private void joinAndLeave() {
		
		if( id < cs.getNstudents()-1 && DriveApp.students.get(id+1).isAlive()){
			try{
				msg("Wait to join student " + DriveApp.students.get(id+1).getName());
				DriveApp.students.get(id+1).join();
				msg("I am done");
			}catch(InterruptedException e) { e.printStackTrace(); 	}
		}
	}

	private void inCount(){
		++count;
	}

	private void takeABreak() {
		msg("will take a break too");
		try{
			Thread.sleep((long) (6000 * Math.random()));
		}catch(InterruptedException e) { e.printStackTrace(); 	}

	}

	private void longSleeping(){
		try{
			msg(".......zZZZzzz..........in Exam " + cs.getNexam());
			Thread.sleep(200000);
			if(Thread.currentThread().isInterrupted()){}
			// after exam end will sleep for random time
		}catch(InterruptedException e) {  msg("--- is interrupted -- will -- leave ---");} //???????????? Student should display this massage

	}

	private void inCreasePriority() {
		try{
			Thread.currentThread().setPriority(10);
			Thread.sleep(200);
			Thread.currentThread().setPriority(5);
		}catch(InterruptedException e) { e.printStackTrace(); 	}

	}

	private void whoMiss(){
		msg("miss exam");
		Thread.yield();
		Thread.yield();		
	}


	private void takeExam(){
		// student wait for professor 
		msg("wait for instructor");
		while(prof.isArrive()){ }

		// student rush to get in class will set height priority for short time
		inCreasePriority();

		// if have room for them increment class capacity 
		if(cs.getCapacity() < MAX )
		{
			// get in class until reach max capacity
			cs.incCapacity();
			msg("          waiting for exam " + cs.getNexam());
			// add student in queue by the order of they get in 
			sharedQueue.add(Thread.currentThread());

			//who get in side will wait until exam start
			while(prof.waitExam()){}
			inCount();
			// when exam starts student will sleep for long until instructor wake him/her up	
			longSleeping();

			// after instructor collect the exam and let them leave student will take a break
			takeABreak();
			cs.decCapacity();

		}//end if student in class
	}

}