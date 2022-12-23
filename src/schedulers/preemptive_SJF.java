package schedulers;

import java.util.Vector;

//Kareem Hossam Mahmoud 20205002 (NSC1)
//Donia Waleed Gamal Hagag 20205010 (NSC1)
//Emad Eldin Ali Hany 20205004 (NSC1)
//Ahmed Mohamed Abd Elaziz 20205014 (NSC2)

public class preemptive_SJF {
	int start = 0, total_time = 0, no_processes, CS_time;
	float avg_wt = 0, avg_ta = 0;
	process[] dq;
	Vector<String> Processes_execution_order = new Vector<String>();

//----------------------------------------------------------------------CONSTRUCTORS
	public preemptive_SJF()// Default Constructor
	{
	}

	public preemptive_SJF(preemptive_SJF obj)// Copy Constructor
	{
		start = obj.start;
		total_time = obj.total_time;
		no_processes = obj.no_processes;
		CS_time = obj.CS_time;
		avg_wt = obj.avg_wt;
		avg_ta = obj.avg_ta;
		dq = new process[no_processes];
		for (int i = 0; i < no_processes; i++) 
		{
			dq[i] = obj.dq[i];
		}
	}

	public preemptive_SJF(process[] dq1, int no_processes, int context_switching)// Parameterized Constructor
	{
		this.no_processes = no_processes;
		CS_time = context_switching;
		dq = new process[no_processes];
		for (int i = 0; i < no_processes; i++) {
			dq[i] = dq1[i];
		}
	}

//----------------------------------------------------------------------Calculate Shortest burst time in array
	int get_shortest_burst() {
		int min_burst = Integer.MAX_VALUE;
		int process_ID = -1;
		for (int i = 0; i < no_processes; i++) {
			if (dq[i].Remaining_Burst_time < min_burst && dq[i].arrival_time <= start && dq[i].flag == 0) {
				min_burst = dq[i].Remaining_Burst_time;
				process_ID = (dq[i].pid - 1);
			}
		}

		return process_ID;
	}

//----------------------------------------------------------------------Sorting according to Burst time
	void sort_by_Burst_time() {
		for (int i = 0; i < no_processes; i++) {
			for (int j = 0; j < no_processes - (i + 1); j++) {
				if (dq[j].Burst_time > dq[j + 1].Burst_time) {
					process temp = dq[j];
					dq[j] = dq[j + 1];
					dq[j + 1] = temp;
				}
			}
		}
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
		System.out.println("Processes Execution Order: " + Processes_execution_order);
		System.out.println(
				"\nPID          Arrival          Brust       Remaining Brust    Complete         Turn           Waiting");
		for (int i = 0; i < no_processes; i++) {
			avg_wt += dq[i].waiting_time;
			avg_ta += dq[i].turnaround_time;
			System.out.println(dq[i].pid + "\t\t" + dq[i].arrival_time + "\t\t" + dq[i].Burst_time + "\t\t"
					+ dq[i].Remaining_Burst_time + "\t\t" + dq[i].complete_time + "\t\t" + dq[i].turnaround_time
					+ "\t\t" + dq[i].waiting_time);
		}
		System.out.println("\nAverage Waiting time: " + avg_wt / (double) no_processes);
		System.out.println("\nAverage Turn around time: " + avg_ta / (double) no_processes);
		avg_wt = 0;
		avg_ta = 0;
	}

//----------------------------------------------------------------------Main Function of SHORTEST JOB FIRST
	void SJF() {
		process curr_process = new process();
		boolean final_check = true;// there is a process that needs to run
		while (final_check) {
			///////////////////////////////// Getting the current minimum burst time
			int min_burst_id = Integer.MAX_VALUE;
			// sort_by_Burst_time();
			for (int i = 0; i < no_processes; i++) {
				min_burst_id = get_shortest_burst();
				if (dq[i].arrival_time <= start && dq[i].flag == 0
						&& dq[i].Remaining_Burst_time == dq[min_burst_id].Remaining_Burst_time) {
					curr_process = dq[i];
					break;
				}
			}

			if (curr_process.pid == 0)// if there is not any process arrive yet
			{
				start++;
			} 																	//   1  2  3
			else///////////////////////////////// Working on a certain process //p1 10  9  8
			{
				for (int i = 1; i <= (curr_process.Remaining_Burst_time); i++) 
				{
					Processes_execution_order.add(curr_process.name);
					start++;
					curr_process.Remaining_Burst_time--;
					int id = get_shortest_burst();
					if (curr_process.pid - 1 != id) 
					{
						dq[curr_process.pid - 1] = curr_process;// id=curr.pid-1
						start += CS_time;// adding context switching time
						break;
					}
				}
				///////////////////////////////// Executes when a certain process is finished
				if (curr_process.Remaining_Burst_time == 0) {
					curr_process.Remaining_quantum_time = 0;
					curr_process.complete_time = start;
					curr_process.flag = 1;// setting flag to 1 to prove that process is done
					curr_process.turnaround_time = curr_process.complete_time - curr_process.arrival_time;// =complete_time-arrival_time
					curr_process.waiting_time = curr_process.turnaround_time - curr_process.Burst_time;// =turnaround_time-Burst_time
					curr_process.Used_quantum = 0;
					dq[(curr_process.pid) - 1] = curr_process;// id=curr.pid-1
					start += CS_time;// adding context switching time
				}
				///////////////////////////////// Checks if all processes are finished or not
				boolean check = true;
				for (process anObject : dq) {
					if (anObject.flag == 0) {
						check = false;
						break;
					}
				}
				if (check == true) {
					final_check = false;
				}

/////////////////////////////////PRINTING ANSWER
				printing();
			}
		}
	}
}