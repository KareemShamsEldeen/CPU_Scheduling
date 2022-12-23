package schedulers;

public class process {

	String name;
	int pid;
	int arrival_time;
	int Burst_time;
	int Remaining_Burst_time;
	int priority;
	int waiting_time;// =turnaround_time-Burst_time
	int turnaround_time;// =complete_time-arrival_time
	int complete_time;
	int quantum_time;
	int Remaining_quantum_time;
	int Used_quantum;
	int flag;//0 need work or 1 finish

	public process()// Default Constructor
	{
		pid = 0;
	}

	public process(process p)// copy Constructor
	{
		this.name = p.name;
		this.pid = p.pid;
		this.arrival_time = p.arrival_time;
		this.Burst_time = p.Burst_time;
		this.Remaining_Burst_time = p.Remaining_Burst_time;
		this.priority = p.priority;
		this.waiting_time = p.waiting_time;
		this.turnaround_time = p.turnaround_time;
		this.complete_time = p.complete_time;
		this.quantum_time = p.quantum_time;
		this.Remaining_quantum_time = p.Remaining_quantum_time;
		this.Used_quantum = p.Used_quantum;
		this.flag = p.flag;

	}

	public process(String name, int pid, int arrival_time, int Burst_time, int quantum_time, int priority) // Parameterized
																											// Constructor
	{
		this.name = name;
		this.pid = pid;
		this.arrival_time = arrival_time;
		this.Burst_time = Burst_time;
		Remaining_Burst_time = Burst_time;
		this.priority = priority;
		this.quantum_time = quantum_time;
		Remaining_quantum_time = quantum_time;
		waiting_time = 0;
		turnaround_time = 0;
		Used_quantum = 0;
		flag = 0;
	}

	void display() {
		System.out.print("name=" + name + " " + "pid=" + pid + " " + "arrival_time=" + arrival_time + " "
				+ "Burst_time=" + Burst_time + " " + "priority=" + priority + " " + "flag=" + flag + " "
				+ "quantum_time=" + quantum_time + " " + "waiting_time=" + waiting_time + " " + "turnaround_time="
				+ turnaround_time + " ");
	}

}
