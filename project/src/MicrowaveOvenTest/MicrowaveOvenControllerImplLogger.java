package MicrowaveOvenTest;

import MicrowaveOven.MicrowaveOvenControllerImpl;
import MicrowaveOven.MicrowaveOvenModel;

public class MicrowaveOvenControllerImplLogger extends MicrowaveOvenControllerImpl {

	private boolean action_open_door = false;
	
	private boolean action_heater_off = false;
	
	private boolean action_light_off = false;
	
	private boolean action_light_on = false;
	
	public MicrowaveOvenControllerImplLogger() {
		super();
		microwave_oven_model = new MicrowaveOvenModel();
	}

	public void action_reset() {
		action_open_door = false;
		action_heater_off = false;
		action_light_off = false;
		action_light_on = false;
	}
	
	@Override
	public void open_door() {
		action_open_door = true;
		super.open_door();
	}
	
	@Override
	public void heater_off() {
		action_heater_off = true;
		super.heater_off();
	}

	@Override
	public void light_off() {
		action_light_off = true;
		super.light_off();
	}

	@Override
	public void light_on() {
		action_light_on = true;
		super.light_on();
	}
	
	@Override
	public IState getCurrentTopLevelState() {
		return super.getCurrentTopLevelState();
	}
	
	public boolean isAction_opend_door() {
		return action_open_door;
	}


	public boolean isAction_heater_off() {
		return action_heater_off;
	}


	public boolean isAction_light_off() {
		return action_light_off;
	}

	public boolean isAction_light_on() {
		return action_light_on;
	}
	
		
}
