package MicrowaveOven;

public interface Observable{
	void register(Observer observer);

	void unregister(Observer observer);

	void setTime(int time);

	int getTime();

	void notify_observer();

	String getVisualization();

}
