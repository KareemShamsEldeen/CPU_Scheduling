package schedulers;

import java.util.Vector;

public class Round_Robin_Scheduling {
	int start, total_time, no_processes, RR_time, CS_time;
	float avg_wt = 0, avg_ta = 0;
	process[] dq;
	Vector<String> order;

//----------------------------------------------------------------------CONSTRUCTORS
	public Round_Robin_Scheduling()// Default Constructor
	{

	}

	public Round_Robin_Scheduling(Round_Robin_Scheduling p)// copy Constructor
	{
		this.start = p.start;
		this.total_time = p.total_time;
		this.no_processes = p.no_processes;
		this.RR_time = p.RR_time;
		this.CS_time = p.CS_time;
		this.dq = new process[no_processes];

		for (int i = 0; i < no_processes; i++) {
			this.dq[i] = p.dq[i];
		}

		order = p.order;
	}

	public Round_Robin_Scheduling(int no_processes, int RR_time, int CS_time, process[] dq1) // Parameterized
																								// Constructor
	{
		start = 0;
		total_time = 0;
		this.no_processes = no_processes;
		this.RR_time = RR_time;
		this.CS_time = CS_time;
		dq = new process[no_processes];

		for (int i = 0; i < no_processes; i++) {
			dq[i] = dq1[i];
		}

		order = new Vector<String>();
	}

//----------------------------------------------------------------------Modulus Function
	int mod(int n, int m) {
		double divide = 0.0;
		int remainder = 0;
		int int_divide = 0;
		divide = n / (double) m;
		int_divide = n / m;
		remainder = (int) (m * (divide - int_divide));
		return remainder;
	}

//----------------------------------------------------------------------Printing Results
	void printing() {
		for (process anObject : dq) {
			avg_wt += anObject.waiting_time;
			avg_ta += anObject.turnaround_time;
		}
		System.out.println("Processes Execution Order: " + order);
		System.out.println(
				"\nPID          Arrival          Brust       Remaining Brust    Complete         Turn           Waiting");
		for (int i = 0; i < no_processes; i++) {
			System.out.println(dq[i].pid + "\t\t" + dq[i].arrival_time + "\t\t" + dq[i].Burst_time + "\t\t"
					+ dq[i].Remaining_Burst_time + "\t\t" + dq[i].complete_time + "\t\t" + dq[i].turnaround_time
					+ "\t\t" + dq[i].waiting_time);
		}
		System.out.println("Average Turnaround Time is " + (float) (avg_ta / no_processes));
		System.out.println("Average Waiting Time is " + (float) (avg_wt / no_processes));
		avg_wt = 0;
		avg_ta = 0;
	}

//----------------------------------------------------------------------Main Function of Round Robin (RR)
	void RR() {
		boolean final_check = true;// there is process need to run
		while (final_check) {
			///////////////////////// select the first process to run it//////////////////
			process curr = new process();// the current process that will be run
			///////////////////////// to get the head of array
			for (process anObject : dq) 
			{
				if (anObject.flag == 0 && (anObject.arrival_time <= start)) {
					curr = anObject;
					break;
				}
			}
			///////////////////////// if there is not any process arrive yet
			if (curr.pid == 0) {
				start++;
			}
			///////////////////////// start to run RR
			else {
				while (final_check) {
					///////////////////////// Decrement curr.Remaining_Burst_time
					for (int i = 1; i <= RR_time && curr.Remaining_Burst_time != 0; i++) {
						curr.Remaining_Burst_time = curr.Remaining_Burst_time - 1;
						start++;
					}

					///////////////////////////////// Executes when a certain process is finished
					if (curr.Remaining_Burst_time == 0) {
						curr.Remaining_quantum_time = 0;
						curr.complete_time = start;
						curr.flag = 1;
						curr.turnaround_time = curr.complete_time - curr.arrival_time;// =complete_time-arrival_time
						curr.waiting_time = curr.turnaround_time - curr.Burst_time;// =turnaround_time-Burst_time
						curr.Used_quantum = 0;
						order.add(curr.name);// to put name of process in Processes execution order
						dq[(curr.pid) - 1] = curr;// id=curr.pid-1
						start += CS_time;

						///////////////////////////////////////////// to get the next ID
						int a = (curr.pid - 1) + 1;
						int id = mod(a, no_processes);

						///////////////////////////////// Checks if all processes are finished or not
						///////////////////////////////// before get the next ID
						boolean check = true;
						for (process anObject : dq) {
							if (anObject.flag == 0) {
								check = false;
								break;
							}
						}
						if (check == true) {
							final_check = false;
							break;
						}

						///////////////////////////////// increment ID if this process done or increment
						///////////////////////////////// start until i get process
						while (dq[id].flag == 1) {
							id = mod(id + 1, no_processes);
						}
						while (dq[id].arrival_time > start && dq[id].flag == 0) {
							start++;
						}

						///////////////////////////////// get the current process by the ID
						curr = dq[id];

					}

					///////////////////////////////// Executes when a certain process is not
					///////////////////////////////// finished
					else {
						///////////////////////////////////////////// to get the next ID
						int a = (curr.pid - 1) + 1;
						int id = mod(a, no_processes);

						////////////////////////////////////////////
						order.add(curr.name);
						dq[(curr.pid) - 1] = curr;// to put name of process in Processes execution order
						start += CS_time;

						///////////////////////////////// Checks if all processes are finished or not
						///////////////////////////////// before get the next ID
						boolean check = true;
						for (process anObject : dq) {
							if (anObject.flag == 0) {
								check = false;
								break;
							}
						}
						if (check == true) {
							final_check = false;
							break;
						}

						///////////////////////////////// increment ID if this process done or increment
						///////////////////////////////// start until i get process
						while (dq[id].flag == 1) {
							id = mod(id + 1, no_processes);
						}
						while (dq[id].arrival_time > start && dq[id].flag == 0) {
							start++;
						}

						///////////////////////////////// get the current process by the ID
						curr = dq[id];

					}

					///////////////////////////////// Checks if all processes are finished or not
					///////////////////////////////// before get the next ID
					printing();
					boolean check = true;
					for (process anObject : dq) {
						if (anObject.flag == 0) {
							check = false;
							break;
						}
					}
					if (check == true) {
						final_check = false;
						printing();
					}

				}
			}
		}

	}

}
