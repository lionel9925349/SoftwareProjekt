package MicrowaveOven;

public class MicrowaveOvenModel implements Observable {

	private int time = 0;
	private Observer observer_list;
	private boolean light = false;
	private boolean heater = false;
	private boolean door_open = false;

	public void setTime(int time){
		this.time = time;
		notify_observer();
	}

	public int getTime(){
		return time;
	}

	public void register(Observer observer){
		observer_list = observer;
	}

	public void unregister(Observer observer){
		observer_list = null;
	}

	public void notify_observer(){
		if (observer_list!=null) observer_list.update(this);
	}

	public void setDoor_open(boolean door_open){
		this.door_open = door_open;
		notify_observer();
	}

	public boolean isDoor_open(){
		return door_open;
	}

	public void setLight(boolean light){
		this.light = light;
		notify_observer();
	}

	public boolean isLight(){
		return light;
	}

	public void setHeater(boolean heater){
		this.heater = heater;
		notify_observer();
	}

	public boolean isHeater(){
		return heater;
	}

	public String getVisualization(){
		
		if (light==false && heater==false && door_open==false && time<=0) return "../../images/microwave_closed.png";
		if (light==false && heater==false && door_open==false && time>0) return "../../images/microwave_undefined.png";
		if (light==false && heater==false && door_open==true && time<=0) return "../../images/microwave_undefined.png";
		if (light==false && heater==false && door_open==true && time>0) return "../../images/microwave_undefined.png";

		if (light==false && heater==true && door_open==false && time<=0) return "../../images/microwave_undefined.png";
		if (light==false && heater==true && door_open==false && time>0) return "../../images/microwave_undefined.png";
		if (light==false && heater==true && door_open==true && time<=0) return "../../images/microwave_undefined.png"; //hazard
		if (light==false && heater==true && door_open==true && time>0) return "../../images/microwave_undefined.png"; //hazard

		if (light==true && heater==false && door_open==false && time<=0) return "../../images/microwave_closed_light.png"; // state Ready (waiting for entry/do activity)
		if (light==true && heater==false && door_open==false && time>0) return "../../images/microwave_undefined.png";
		if (light==true && heater==false && door_open==true && time<=0) return "../../images/microwave_open_light.png";
		if (light==true && heater==false && door_open==true && time>0) return "../../images/microwave_open_light.png";

		if (light==true && heater==true && door_open==false && time<=0) return "../../images/microwave_closed_light.png"; // state Ready (waiting for entry/do activity)
		if (light==true && heater==true && door_open==false && time>0) return "../../images/microwave_closed_light.png";
		if (light==true && heater==true && door_open==true && time<=0) return "../../images/microwave_undefined.png";  //hazard
		if (light==true && heater==true && door_open==true && time>0) return "../../images/microwave_undefined.png"; //hazard

		return "../../images/microwave_undefined.png";

	}

	public String toString(){

		return "[light=" + light + ", heater=" + heater + ", door_open=" + door_open + ", time=" + time + "]";

	}


}

