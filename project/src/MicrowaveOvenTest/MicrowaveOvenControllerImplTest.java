package MicrowaveOvenTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import MicrowaveOven.MicrowaveOvenController.TStateId;
import MicrowaveOven.MicrowaveOvenController.IState;

public class MicrowaveOvenControllerImplTest {

	@Test
	public void test_transition0() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		assertEquals(m_MOC.getCurrentTopLevelState().getId(), TStateId.MicrowaveOven_Region1_Init);
	}

	@Test
	public void test_transition1() {
		// Setup
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		assertEquals(m_MOC.getCurrentTopLevelState().getId(), TStateId.MicrowaveOven_Region1_Init);
		// Test begin
		m_MOC.count_down();
		assertEquals(m_MOC.getCurrentTopLevelState().getId(), TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition2() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
	}

	@Test
	public void test_transition4() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition5() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
	}

	@Test
	public void test_transition6() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.getMicrowave_oven_model().setTime(0);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition7() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.button_stop();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition8() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
	}

	@Test
	public void test_transition9() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
	}

	@Test
	public void test_transition10() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
		m_MOC.button_stop();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
	}

	@Test
	public void test_transition11() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
	}

	@Test
	public void test_transition12() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
	}

	@Test
	public void test_transition13() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.door_open_close();
		TestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition14() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.button_start_time();
		TestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition15() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.button_stop();
		TestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition16() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.count_down();
		TestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
	}

	@Test
	public void test_transition17() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_stop();
		TestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
	}

	@Test
	public void test_transition18() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
		m_MOC.count_down();
		TestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition19() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
		m_MOC.button_start_time();
		TestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition20() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
		m_MOC.button_stop();
		TestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition21() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
		m_MOC.button_start_time();
		TestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
	}

	@Test
	public void test_transition22() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
		m_MOC.count_down();
		TestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
	}

	@Test
	public void test_transition23() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
	}

	@Test
	public void test_transition24() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
	}

	@Test
	public void test_transition25() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.getMicrowave_oven_model().setTime(0);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
	}

	@Test
	public void test_transition26() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.button_stop();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
	}

	@Test
	public void test_transition27() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.getMicrowave_oven_model().setTime(0);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
	}

	@Test
	public void test_transition28() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.button_stop();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
	}

	@Test
	public void test_transition29() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.getMicrowave_oven_model().setTime(0);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition30() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.button_stop();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition31() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
	}

	@Test
	public void test_transition32() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
	}

	@Test
	public void test_transition33() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.getMicrowave_oven_model().setTime(0);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition34() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.button_stop();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition35() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
	}

	@Test
	public void test_transition36() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
	}

	@Test
	public void test_transition37() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.getMicrowave_oven_model().setTime(0);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition38() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.button_stop();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_transition39() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
	}

	@Test
	public void test_transition40() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
	}

	@Test
	public void test_transition41() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
	}

	@Test
	public void test_transition42() {
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Init);
		m_MOC.count_down();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
		m_MOC.button_start_time();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Active);
		m_MOC.door_open_close();
		// Test begin;
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Suspended);
		m_MOC.button_stop();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Open);
		m_MOC.door_open_close();
		TestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.MicrowaveOven_Region1_Ready);
	}

	@Test
	public void test_state_Ready() {
		Object temp_bool = new Object();
		Object obj1 = new Object();
		// Setup
		MicrowaveOvenControllerImplLogger m_MOC = new MicrowaveOvenControllerImplLogger();
		obj1 = m_MOC.getCurrentTopLevelState();
		obj1 = ((IState) obj1).getId();
		TestUtil.call_assertEquals(obj1, TStateId.MicrowaveOven_Region1_Init);
		// Test begin
		temp_bool = m_MOC.isAction_heater_off();
		TestUtil.call_assertFalse(temp_bool);
		temp_bool = m_MOC.isAction_light_off();
		TestUtil.call_assertFalse(temp_bool);
		m_MOC.count_down();
		temp_bool = m_MOC.isAction_heater_off();
		TestUtil.call_assertTrue(temp_bool);
		temp_bool = m_MOC.isAction_light_off();
		TestUtil.call_assertTrue(temp_bool);
		obj1 = m_MOC.getCurrentTopLevelState();
		obj1 = ((IState) obj1).getId();
		TestUtil.call_assertEquals(obj1, TStateId.MicrowaveOven_Region1_Ready);
		/*
		 * // Setup MicrowaveOvenControllerImplLogger m_MOC = new
		 * MicrowaveOvenControllerImplLogger();
		 * assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.
		 * MicrowaveOven_Region1_Init); // Test begin
		 * assertTrue(m_MOC.isAction_heater_off()==false);
		 * assertTrue(m_MOC.isAction_light_off()==false); m_MOC.count_down();
		 * assertTrue(m_MOC.isAction_heater_off()==true);
		 * assertTrue(m_MOC.isAction_light_off()==true);
		 * assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId.
		 * MicrowaveOven_Region1_Ready);
		 */
	}

}
