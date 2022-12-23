package schedulers;

import java.util.*;

public class AG_scheduling {
	int start, total_time, no_processes, CS_time;
	float avg_wt = 0, avg_ta = 0;
	process[] dq;
	Vector<String> order;

//----------------------------------------------------------------------CONSTRUCTORS
	public AG_scheduling()// Default Constructor
	{

	}

	public AG_scheduling(AG_scheduling p)// copy Constructor
	{
		this.start = p.start;
		this.total_time = p.total_time;
		this.no_processes = p.no_processes;
		this.CS_time = p.CS_time;
		this.dq = new process[no_processes];

		for (int i = 0; i < no_processes; i++) {
			this.dq[i] = p.dq[i];
		}

		order = p.order;
	}

	public AG_scheduling(int no_processes, int CS_time, process[] dq1) // Parameterized Constructor
	{
		start = 0;
		total_time = 0;
		this.no_processes = no_processes;
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
				"\nPID          Arrival          Brust       Remaining Brust    Complete         Turn           Waiting           Remaining Quantum");
		for (int i = 0; i < no_processes; i++) {
			System.out.println(dq[i].pid + "\t\t" + dq[i].arrival_time + "\t\t" + dq[i].Burst_time + "\t\t"
					+ dq[i].Remaining_Burst_time + "\t\t" + dq[i].complete_time + "\t\t" + dq[i].turnaround_time
					+ "\t\t" + dq[i].waiting_time + "\t\t" + dq[i].Remaining_quantum_time);
		}
		System.out.println("Average Turnaround Time is " + (float) (avg_ta / no_processes));
		System.out.println("Average Waiting Time is " + (float) (avg_wt / no_processes));
		avg_wt = 0;
		avg_ta = 0;
	}
	
//----------------------------------------------------------------------Printing Results
		void printing_history( process p1 ) 
		{
			System.out.println("updated quantum time: " + p1.name + " -> " + p1.quantum_time);
		}

// ----------------------------------------------------------------------Printing Results Vector
	void printingV(Vector<process> v) {
		for (process x : v) {
			avg_wt += x.waiting_time;
			avg_ta += x.turnaround_time;
		}
		System.out.println("Processes Execution Order: " + order);
		System.out.println(
				"\nPID          Arrival          Brust       Remaining Brust    Complete         Turn           Waiting           Remaining Quantum");
		for (process x : v) {
			System.out.println(x.pid + "\t\t" + x.arrival_time + "\t\t" + x.Burst_time + "\t\t" + x.Remaining_Burst_time
					+ "\t\t" + x.complete_time + "\t\t" + x.turnaround_time + "\t\t" + x.waiting_time + "\t\t"
					+ x.Remaining_quantum_time);
		}
		System.out.println("Average Turnaround Time is " + (float) (avg_ta / no_processes));
		System.out.println("Average Waiting Time is " + (float) (avg_wt / no_processes));
		avg_wt = 0;
		avg_ta = 0;
	}

//----------------------------------------------------------------------Sorting By Arrival time
	void sort_by_Arrival_time() {
		for (int i = 0; i < no_processes; i++) {
			for (int j = 0; j < no_processes - (i + 1); j++) {
				if (dq[j].arrival_time > dq[j + 1].arrival_time) {
					process temp = dq[j];
					dq[j] = dq[j + 1];
					dq[j + 1] = temp;
				}
			}
		}
	}

//----------------------------------------------------------------------Get Current Highest Priority
	int get_high_priority(Vector<process> v, process p1) {
		int max_priority = Integer.MAX_VALUE;
		int process_ID = -1;
		for (process x : v) {
			if (x.priority <= max_priority && (x.arrival_time <= start) && x.flag == 0) {
				max_priority = x.priority;
				process_ID = x.pid - 1;
			}
		}

		if (dq[process_ID].priority > p1.priority) {
			return (p1.pid - 1);
		}

		return (process_ID); // decrementing 1 to match the original place in deque
	}

//----------------------------------------------------------------------Calculate Shortest burst time in array
	int get_shortest_burst(Vector<process> v, process p1) {
		int min_burst = Integer.MAX_VALUE;
		int process_ID = -1;
		for (process x : v) {
			if (x.Remaining_Burst_time < min_burst && x.arrival_time <= start && x.flag == 0) {
				min_burst = x.Remaining_Burst_time;
				process_ID = (x.pid - 1);
			}
		}

		if (dq[process_ID].Remaining_Burst_time > p1.Remaining_Burst_time) {
			return (p1.pid - 1);
		}

		return process_ID;
	}

//----------------------------------------------------------------------Main Function of AG_SCHEDUALING
	void AGS() {
		sort_by_Arrival_time();

		Vector<process> v = new Vector<process>();

		for (process anObject : dq) {
			if (anObject.arrival_time == start) {
				v.add(anObject);
			}
		}

		while (v.isEmpty()) {
			start++;
			for (process anObject : dq) {
				if (anObject.arrival_time == start) {
					v.add(anObject);
				}
			}
		}

		//////////////////////////// AG Scheduling/////////////////////////////////////
		boolean final_check = true;// there is process need to run

		process curr_process = new process();
		while (final_check) {
			if (curr_process.pid == 0) {
				curr_process = v.firstElement();
				v.removeElement(curr_process);
			}

			/////////////////////////////// FCFS///////////////////////////////////////////
			int ceil = (int) Math.ceil(curr_process.Remaining_quantum_time * 0.25);
			///////////////////////////////////////////////////////////////////////////////			
			for (int i = 1; i <= ceil && curr_process.Remaining_Burst_time != 0; i++) {
				order.add(curr_process.name);
				curr_process.Remaining_Burst_time--;
				curr_process.Remaining_quantum_time--;
				curr_process.Used_quantum++;
				// ----------------------------------------------check vec
				start++;
				for (process anObject : dq) {
					if (anObject.arrival_time == start) {
						v.add(anObject);
					}
				}
			}
			if (curr_process.Remaining_Burst_time == 0)// if curr finished
			{
				curr_process.Remaining_quantum_time = 0;
				curr_process.quantum_time = 0;
				printing_history(curr_process);
				curr_process.complete_time = start;
				curr_process.flag = 1;
				curr_process.turnaround_time = curr_process.complete_time - curr_process.arrival_time;// =complete_time-arrival_time
				curr_process.waiting_time = curr_process.turnaround_time - curr_process.Burst_time;// =turnaround_time-Burst_time
				curr_process.Used_quantum = 0;
				dq[(curr_process.pid) - 1] = curr_process;// id=curr.pid-1

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

				while (get_high_priority(v, curr_process) < 0) {
					// ----------------------------------------------check vec
					start++;
					for (process anObject : dq) {
						if (anObject.arrival_time == start) {
							v.add(anObject);
						}
					}
				}

				curr_process = v.firstElement();
				v.removeElement(curr_process);
				// ----------------------------------------------check vec
				start += CS_time;
				for (process anObject : dq) {
					if (anObject.arrival_time == start) {
						v.add(anObject);
					}
				}

				check = true;
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

			} else// if Process not finished
			{
				while (get_high_priority(v, curr_process) < 0) {
					// ----------------------------------------------check vec
					start++;
					for (process anObject : dq) {
						if (anObject.arrival_time == start) {
							v.add(anObject);
						}
					}
				}

				int id = get_high_priority(v, curr_process);

				if (dq[id].name != (curr_process.name)) {
					curr_process.quantum_time += (int) Math.ceil(curr_process.Remaining_quantum_time / 2.0);
					curr_process.Remaining_quantum_time = curr_process.quantum_time;
					printing_history(curr_process);
					v.add(curr_process);

					int i = v.indexOf(dq[id]);
					curr_process = v.get(i);
					v.removeElement(curr_process);
					// ----------------------------------------------check vec
					start += CS_time;
					if (CS_time != 0) {
						for (process anObject : dq) {
							if (anObject.arrival_time == start) {
								v.add(anObject);
							}
						}
					}

				}

				else {
///////////////////////////non-preemptive Priority///////////////////////////////////////////////
					ceil = (int) Math.ceil(curr_process.Remaining_quantum_time * 0.25);
/////////////////////////////////////////////////////////////////////////////////////////////////
					for (int i = 1; i <= ceil && curr_process.Remaining_Burst_time != 0; i++) {
						order.add(curr_process.name);
						curr_process.Remaining_Burst_time--;
						curr_process.Remaining_quantum_time--;
						curr_process.Used_quantum++;
						// ----------------------------------------------check vec
						start++;
						for (process anObject : dq) {
							if (anObject.arrival_time == start) {
								v.add(anObject);
							}
						}
					}
					if (curr_process.Remaining_Burst_time == 0)// if curr finished
					{
						curr_process.Remaining_quantum_time = 0;
						curr_process.quantum_time = 0;
						printing_history(curr_process);
						curr_process.complete_time = start;
						curr_process.flag = 1;
						curr_process.turnaround_time = curr_process.complete_time - curr_process.arrival_time;// =complete_time-arrival_time
						curr_process.waiting_time = curr_process.turnaround_time - curr_process.Burst_time;// =turnaround_time-Burst_time
						curr_process.Used_quantum = 0;
						dq[(curr_process.pid) - 1] = curr_process;// id=curr.pid-1

						boolean check = true;
						for (process anObject : dq) 
						{
							if (anObject.flag == 0) 
							{
								check = false;
								break;
							}
						}
						if (check == true) {
							final_check = false;
							break;
						}

						while (get_shortest_burst(v, curr_process) < 0) {
							// ----------------------------------------------check vec
							start++;
							for (process anObject : dq) {
								if (anObject.arrival_time == start) {
									v.add(anObject);
								}
							}
						}

						id = get_shortest_burst(v, curr_process);

						curr_process = v.firstElement();
						v.removeElement(curr_process);
						// ----------------------------------------------check vec
						start += CS_time;
						if (CS_time != 0) {
							for (process anObject : dq) {
								if (anObject.arrival_time == start) {
									v.add(anObject);
								}
							}
						}

					} else// if Process not finished
					{
						while (get_shortest_burst(v, curr_process) < 0) {
							// ----------------------------------------------check vec
							start++;
							for (process anObject : dq) {
								if (anObject.arrival_time == start) {
									v.add(anObject);
								}
							}
						}

						id = get_shortest_burst(v, curr_process);

						if (dq[id].name != (curr_process.name)) {
							curr_process.Remaining_quantum_time += curr_process.quantum_time;
							curr_process.quantum_time = curr_process.Remaining_quantum_time;
							printing_history(curr_process);
							v.add(curr_process);

							int i = v.indexOf(dq[id]);
							curr_process = v.get(i);
							v.removeElement(curr_process);
							// ----------------------------------------------check vec
							if (CS_time != 0) {
								for (process anObject : dq) {
									if (anObject.arrival_time == start) {
										v.add(anObject);
									}
								}
							}
						} else {
/////////////////////////// preemptive Shortest- Job First (SJF)///////////////////////////////////////////////
///////////////////////////////// Working on a certain process		
							for (int i = 1; i <= (curr_process.Remaining_quantum_time)&& curr_process.Remaining_Burst_time != 0; i++) 
							{
								order.add(curr_process.name);
								curr_process.Remaining_Burst_time--;
								curr_process.Remaining_quantum_time--;
								curr_process.Used_quantum++;

								// ----------------------------------------------check vec
								start++;
								for (process anObject : dq) {
									if (anObject.arrival_time == start) {
										v.add(anObject);
									}
								}

								id = get_shortest_burst(v, curr_process);
								if (curr_process.pid - 1 != id) {
									break;
								}
							}
							///////////////////////////////// Executes when a certain process is finished
							if (curr_process.Remaining_Burst_time == 0)// 2,3
							{
								curr_process.Remaining_quantum_time = 0;
								curr_process.quantum_time = 0;
								printing_history(curr_process);
								curr_process.complete_time = start;
								curr_process.flag = 1;
								curr_process.turnaround_time = curr_process.complete_time - curr_process.arrival_time;// =complete_time-arrival_time
								curr_process.waiting_time = curr_process.turnaround_time - curr_process.Burst_time;// =turnaround_time-Burst_time
								curr_process.Used_quantum = 0;
								dq[(curr_process.pid) - 1] = curr_process;// id=curr.pid-1
								// ----------------------------------------------check vec
								start += CS_time;
								if (CS_time != 0) {
									for (process anObject : dq) {
										if (anObject.arrival_time == start) {
											v.add(anObject);
										}
									}
								}

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

							} else {
								// 1
								if (curr_process.Remaining_quantum_time == 0 && curr_process.Remaining_Burst_time > 0) {
									curr_process.quantum_time += 2;
									curr_process.Remaining_quantum_time = curr_process.quantum_time;
									printing_history(curr_process);
									v.add(curr_process);
									// ----------------------------------------------check vec
									start += CS_time;
									if (CS_time != 0) {
										for (process anObject : dq) {
											if (anObject.arrival_time == start) {
												v.add(anObject);
											}
										}
									}
								}
							} // SJF end
						}
					} // NPS end
				}

			} // FCFS end

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

		} // while end

		printing();
	}// AGS end

};
