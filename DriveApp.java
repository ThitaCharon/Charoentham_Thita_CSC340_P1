import java.util.ArrayList;
import java.util.Vector;


public class DriveApp {

	public static long time = System.currentTimeMillis();
	public static 	ArrayList<Thread> students ;

	public static void main(String[] args) {
		// list of student and id
		students = new ArrayList<Thread>();
		
		// keep tracking of students' name and their grades
		ArrayList<Thread>  sharedQueue = new ArrayList<Thread>();

		Classroom csc = new Classroom();	
		Instructor instructor = new Instructor(csc,sharedQueue); 		// create instructor

		// create 14 students Threads
		for (int i=0; i< 14;i++){
			csc.incNstudents();
			students.add(new Student(i, instructor, csc,sharedQueue));
			students.get(i).start();
		}
	
		instructor.start();
	}
	

}
