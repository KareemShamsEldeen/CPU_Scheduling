package schedulers;

import java.util.Vector;

//Kareem Hossam Mahmoud 20205002 (NSC1)
//Donia Waleed Gamal Hagag 20205010 (NSC1)
//Emad Eldin Ali Hany 20205004 (NSC1)
//Ahmed Mohamed Abd Elaziz 20205014 (NSC2)

public class preemptive_priority_scheduling {
	int start = 0, total_time = 0, no_processes, CS_time, Aging_Value;
	float avg_wt = 0, avg_ta = 0;
	process[] dq;
	Vector<String> Processes_execution_order = new Vector<String>();

//----------------------------------------------------------------------CONSTRUCTORS
	public preemptive_priority_scheduling()// Default Constructor
	{
	}

	public preemptive_priority_scheduling(preemptive_priority_scheduling obj)// Copy Constructor
	{
		start = obj.start;
		total_time = obj.total_time;
		no_processes = obj.no_processes;
		CS_time = obj.CS_time;
		Aging_Value = obj.Aging_Value;
		avg_wt = obj.avg_wt;
		avg_ta = obj.avg_ta;
		dq = new process[no_processes];
		for (int i = 0; i < no_processes; i++) {
			dq[i] = obj.dq[i];
		}
	}

	public preemptive_priority_scheduling(process[] dq1, int no_processes, int context_switching, int Aging_Value)// Parameterized
	// Constructor
	{
		this.no_processes = no_processes;
		CS_time = context_switching;
		this.Aging_Value = Aging_Value;
		dq = new process[no_processes];
		for (int i = 0; i < no_processes; i++) {
			dq[i] = dq1[i];
		}
	}

//----------------------------------------------------------------------Get Current Highest Priority
	int get_high_priority() {

		int max_priority = Integer.MAX_VALUE;
		int process_ID = -1;
		for (int i = 0; i < no_processes; i++) {
			if (dq[i].priority <= max_priority && (dq[i].arrival_time <= start) && dq[i].flag == 0) {
				max_priority = dq[i].priority;
				process_ID = dq[i].pid;
			}
		}
		return (process_ID - 1); // decrementing 1 to match the original place in deque
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

//----------------------------------------------------------------------Aging Solution
	void aging() {
		if (Aging_Value != 0) {
			if (mod(start, Aging_Value) == 0 && start != 0) {
				int max_priority = get_high_priority();
				for (int i = 0; i < no_processes; i++) {
					if (dq[i].pid != max_priority && dq[i].priority > 0 && (dq[i].arrival_time <= start)
							&& dq[i].flag == 0) {
						dq[i].priority--;
					}
				}
			}
		}
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

//----------------------------------------------------------------------Main Function of PREEMPTIVE PRIORITY SCHEDULING
	void PPS() {
		process curr_process = new process();
		boolean final_check = true;// there is a process that needs to run
		while (final_check) {
			///////////////////////////////// Getting the current Highest Priority
			int max_priority = Integer.MAX_VALUE;
			for (int i = 0; i < no_processes; i++) {
				max_priority = get_high_priority();
				if (dq[i].arrival_time <= start && dq[i].flag == 0 && dq[i].priority == dq[max_priority].priority) {
					curr_process = dq[i];
					break;
				}
			}

			if (curr_process.pid == 0)// if there is not any process arrive yet
			{
				start++;
				aging();
			} else///////////////////////////////// Working on a certain process
			{
				for (int i = 1; i <= (curr_process.Remaining_Burst_time); i++) {
					Processes_execution_order.add(curr_process.name);
					curr_process.Remaining_Burst_time--;
					int id = get_high_priority();
					if (curr_process.pid - 1 != id) {
						dq[curr_process.pid - 1] = curr_process;// id=curr.pid-1
						start += CS_time;// adding context switching time
						aging();
						break;
					}
					start++;
					aging();
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
					aging();
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