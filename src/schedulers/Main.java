package schedulers;

import java.util.Scanner;


public class Main {
	public static void main(String[] args) {
		int start = 0, total_time = 0, no_processes, RR_time, CS_time, Aging_Value, choice;
		float avg_wt = 0, avg_ta = 0;
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter 1 for preemptive Shortest- Job First (SJF) Scheduling");
		System.out.println("Enter 2 for Round Robin (RR)");
		System.out.println("Enter 3 for preemptive Priority Scheduling");
		System.out.println("Enter 4 for AG Scheduling");
		System.out.print("\nYour choice: ");
		choice = sc.nextInt();

		System.out.print("\nNumber of processes:");
		no_processes = sc.nextInt();
		System.out.print("Context switching:");
		CS_time = sc.nextInt();

		// Initializing an array
		process[] dq = new process[no_processes];

		if (choice == 1) {

			for (int i = 0; i < no_processes; i++) {
				int c = i + 1;// id=i+1;
				System.out.print("\nProcess " + c + " Name:");
				String name = sc.next();
				System.out.print("Process " + c + " Arrival Time:");
				int AT = sc.nextInt();
				System.out.print("Process " + c + " Burst Time:");
				int BT = sc.nextInt();
				dq[i] = new process(name, c, AT, BT, 0, 0);
				System.out.println();
			}

			preemptive_SJF obj = new preemptive_SJF(dq, no_processes, CS_time);
			obj.SJF();

		} else if (choice == 2) {
			System.out.print("\nRound robin Time Quantum:");
			RR_time = sc.nextInt();
			for (int i = 0; i < no_processes; i++) {
				int c = i + 1;// id=i+1;
				System.out.print("\nProcess " + c + " Name:");
				String name = sc.next();
				System.out.print("Process " + c + " Arrival Time:");
				int AT = sc.nextInt();
				System.out.print("Process " + c + " Burst Time:");
				int BT = sc.nextInt();
				dq[i] = new process(name, c, AT, BT, 0, 0);
				System.out.println();
			}
			Round_Robin_Scheduling obj = new Round_Robin_Scheduling(no_processes, RR_time, CS_time, dq);
			obj.RR();
		} 
		else if (choice == 3)
		{
			System.out.print("\nAging Value:");
			Aging_Value = sc.nextInt();
			for (int i = 0; i < no_processes; i++) {
				int c = i + 1;// id=i+1;
				System.out.print("\nProcess " + c + " Name:");
				String name = sc.next();
				System.out.print("Process " + c + " Arrival Time:");
				int AT = sc.nextInt();
				System.out.print("Process " + c + " Burst Time:");
				int BT = sc.nextInt();
				System.out.print("Process " + c + " Priority:");
				int p = sc.nextInt();
				dq[i] = new process(name, c, AT, BT, 0, p);
				System.out.println();
			}
			preemptive_priority_scheduling obj = new preemptive_priority_scheduling(dq, no_processes, CS_time,
					Aging_Value);
			obj.PPS();

		} 
		else if (choice == 4) 
		{
			for (int i = 0; i < no_processes; i++)
			{
				int c = i + 1;// id=i+1;
				System.out.print("\nProcess " + c + " Name:");
				String name = sc.next();
				System.out.print("Process " + c + " Arrival Time:");
				int AT = sc.nextInt();
				System.out.print("Process " + c + " Burst Time:");
				int BT = sc.nextInt();
				System.out.print("Process " + c + " quantum:");
				int q = sc.nextInt();
				System.out.print("Process " + c + " Priority:");
				int p = sc.nextInt();
				dq[i] = new process(name, c, AT, BT, q, p);
				System.out.println();
			}
			AG_scheduling obj = new AG_scheduling(no_processes, CS_time, dq);
			obj.AGS();

		}		
		sc.close();
	}
}
