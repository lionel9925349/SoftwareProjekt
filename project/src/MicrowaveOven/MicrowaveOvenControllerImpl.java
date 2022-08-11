package MicrowaveOven;

public class MicrowaveOvenControllerImpl extends MicrowaveOvenController/* MicrowaveOvenController */ {

	public String m_sDebugMessage = "";

	/* @Override */
	public void OnDebugMessage(String str){
 		    
 		if (!str.startsWith("BEGIN_EVENT") && !str.startsWith("END_EVENT") && !str.startsWith("BEGIN_INITIALIZE") && !str.startsWith("END_INITIALIZE"))
               		m_sDebugMessage += "\t";
   	                m_sDebugMessage += str + "\n";

	}

	/* @Override */
	public void add_time(){
		microwave_oven_model.setTime(microwave_oven_model.getTime()+10);
	}

	/* @Override */
	public MicrowaveOvenModel getMicrowave_oven_model(){
		return microwave_oven_model;
	}

	/* @Override */
	public void sub_time(){
		microwave_oven_model.setTime(microwave_oven_model.getTime()-1);
	}

	/* @Override */
	public void rst_time(){
		microwave_oven_model.setTime(0);
	}

	/* @Override */
	public void open_door(){
		microwave_oven_model.setDoor_open(true);
	}

	/* @Override */
	public void close_door(){
		microwave_oven_model.setDoor_open(false);
	}

	/* @Override */
	public void heater_on(){
		microwave_oven_model.setHeater(true);
	}

	/* @Override */
	public void heater_off(){
		microwave_oven_model.setHeater(false);
	}

	/* @Override */
	public void light_on(){
		microwave_oven_model.setLight(true);
	}

	/* @Override */
	public void light_off(){
		microwave_oven_model.setLight(false);
	}

}
