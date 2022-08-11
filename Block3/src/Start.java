import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Expression;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.LiteralString;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
import org.eclipse.uml2.uml.MessageSort;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.BehaviorExecutionSpecification;
import org.eclipse.uml2.uml.CallEvent;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValueSpecification;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.eclipse.uml2.uml.internal.resource.UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.UMLResource;

@SuppressWarnings("restriction")
public class Start {

	public static void main(String[] args) {

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		UMLResourcesUtil.init(resourceSet);

		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("uml", new UMLResourceFactoryImpl());

		URI uri = URI.createURI("file:/C:\\Users\\ruben\\Downloads\\Block2\\Block2.uml");
		// URI uri =
		// URI.createURI("file:/C:\\Users\\ruben\\Downloads\\Block3\\model\\Block3.uml");

		Resource resource = resourceSet.getResource(uri, true);
		EcoreUtil.resolveAll(resourceSet);

		Model uml_model = (Model) resource.getContents().get(0);

		List<Element> model_elements = uml_model.getOwnedElements();

		for (Element element : model_elements) {

			System.out.println(element);

			if (element instanceof Package) {

				Package my_package = (Package) element;

				if (my_package.getName().equals("project")) {

					for (Package sub_package : my_package.getNestedPackages()) {

						System.out.println(sub_package.getName());

						if (sub_package.getName().equals("MicrowaveOven"))
							show_Classes_in_Package(sub_package);
						if (sub_package.getName().equals("MicrowaveOvenTest"))
							show_Classes_in_Package(sub_package);

					}

				}

			}
		}
	}

	private static void show_Classes_in_Package(Package sub_package) {

		System.out.println("Package '" + sub_package.getName() + "' contains:");

		for (Element element : sub_package.getOwnedElements()) {

			// System.out.println(element);

			if (element instanceof Class) {

				Class clazz = (Class) element;
				System.out.println(clazz.getName());

				if (clazz.getName().equals("MicrowaveOvenController"))
					process_Implementation_Class(clazz);
				if (clazz.getName().equals("MicrowaveOvenControllerImplTest"))
					process_Test_Class(clazz);

			}
		}

	}

	private static void process_Implementation_Class(Class clazz) {

		if (!clazz.getOwnedBehaviors().isEmpty()) {
			Behavior stm = clazz.getOwnedBehaviors().get(0);
			System.out.println("Found state machine modell (" + stm.getName() + ")");
			process_statemachine_Model_to_Code((StateMachine) stm);
			process_statemachine_Model_to_Model((StateMachine) stm);
		}

	}

	private static void process_Test_Class(Class clazz) {

		Behavior sqm = clazz.getOwnedBehaviors().get(0);
		System.out.println("Found sequence modell (" + sqm.getName() + ")");
		process_sequence_Model((Interaction) sqm);

	}

	private static String result(int index, Region region, StateMachine stm, Transition transition1,
			Transition transition2, State state_source_setup) {

		String result = "\n";
		// setup

		State state_source = (State) transition2.getSource();
		State state_target = (State) transition2.getTarget();

		result = result + "@Test\n";
		result = result + "public void test_transition" + index + "() {\n";

		result = result + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result = result + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result = result + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result = result + "\t// Test begin;\n";
		result = result + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source.getName() + ");\n";
		result = result + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition2.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result = result + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target.getName() + ");\n";

		result = result + "}\n";
		return result;
	}

	private static String result2(int index, Region region, StateMachine stm, Transition transition4,
			Transition transition1, State state_source_setup, State state_target_setup) {

		String result2 = "\n";

		State state_source1 = (State) transition4.getSource();
		State state_target1 = (State) transition4.getTarget();

		result2 = result2 + "@Test\n";
		result2 = result2 + "public void test_transition" + index + "() {\n";

		// Setup
		result2 = result2 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result2 = result2 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result2 = result2 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result2 = result2 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";
		result2 = result2 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition4.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result2 = result2 + "\t// Test begin;\n";
		result2 = result2 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source1.getName() + ");\n";
		result2 = result2 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition4.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result2 = result2 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target1.getName() + ");\n";

		result2 = result2 + "}\n";

		return result2;
	}

	private static String result3(int index, Region region, StateMachine stm, Transition transition5,
			Transition transition1, State state_source_setup) {
		String result3 = "\n";

		State state_source0 = (State) transition5.getSource();
		State state_target0 = (State) transition5.getTarget();

		result3 = result3 + "@Test\n";
		result3 = result3 + "public void test_transition" + index + "() {\n";

		// Setup
		result3 = result3 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result3 = result3 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result3 = result3 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result3 = result3 + "\t// Test begin;\n";
		result3 = result3 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source0.getName() + ");\n";
		result3 = result3 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result3 = result3 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target0.getName() + ");\n";

		result3 = result3 + "}\n";

		return result3;
	}

	private static String result4(int index, Region region, StateMachine stm, Transition transition6,
			Transition transition1, Transition transition5, State state_source_setup, State state_target_setup) {

		String result4 = "\n";

		State state_source2 = (State) transition6.getSource();
		State state_target2 = (State) transition6.getTarget();

		result4 = result4 + "@Test\n";
		result4 = result4 + "public void test_transition" + index + "() {\n";

		// Setup
		result4 = result4 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result4 = result4 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result4 = result4 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result4 = result4 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";
		result4 = result4 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result4 = result4 + "\t// Test begin;\n";
		result4 = result4 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source2.getName() + ");\n";
		result4 = result4 + "\tm_MOC.getMicrowave_oven_model().setTime(0);\n";
		result4 = result4 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition6.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result4 = result4 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target2.getName() + ");\n";
		result4 = result4 + "}\n";

		return result4;
	}

	private static String result5(int index, Region region, StateMachine stm, Transition transition7,
			Transition transition1, Transition transition5, State state_source_setup, State state_target_setup) {

		String result5 = "\n";

		State state_source3 = (State) transition7.getSource();
		State state_target3 = (State) transition7.getTarget();

		result5 = result5 + "@Test\n";
		result5 = result5 + "public void test_transition" + index + "() {\n";

		// Setup
		result5 = result5 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result5 = result5 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result5 = result5 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result5 = result5 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result5 = result5 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result5 = result5 + "\t// Test begin;\n";
		result5 = result5 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source3.getName() + ");\n";
		result5 = result5 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition7.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result5 = result5 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target3.getName() + ");\n";
		result5 = result5 + "}\n";

		return result5;
	}

	private static String result6(int index, Region region, StateMachine stm, Transition transition8,
			Transition transition1, Transition transition5, State state_source_setup, State state_target_setup) {

		String result6 = "\n";

		State state_source4 = (State) transition8.getSource();
		State state_target4 = (State) transition8.getTarget();

		result6 = result6 + "@Test\n";
		result6 = result6 + "public void test_transition" + index + "() {\n";

		// Setup
		result6 = result6 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result6 = result6 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result6 = result6 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result6 = result6 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result6 = result6 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result6 = result6 + "\t// Test begin;\n";
		result6 = result6 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source4.getName() + ");\n";
		result6 = result6 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result6 = result6 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target4.getName() + ");\n";
		result6 = result6 + "}\n";

		return result6;
	}

	private static String result7(int index, Region region, StateMachine stm, Transition transition9,
			Transition transition1, Transition transition5, Transition transition8, State state_source_setup,
			State state_target_setup) {

		String result7 = "\n";

		State state_source5 = (State) transition9.getSource();
		State state_target5 = (State) transition9.getTarget();

		result7 = result7 + "@Test\n";
		result7 = result7 + "public void test_transition" + index + "() {\n";

		// Setup
		result7 = result7 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result7 = result7 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result7 = result7 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result7 = result7 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result7 = result7 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result7 = result7 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition8.getSource().getName() + ");\n";
		result7 = result7 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result7 = result7 + "\t// Test begin;\n";
		result7 = result7 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source5.getName() + ");\n";
		result7 = result7 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition9.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result7 = result7 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target5.getName() + ");\n";
		result7 = result7 + "}\n";

		return result7;
	}

	private static String result8(int index, Region region, StateMachine stm, Transition transition10,
			Transition transition1, Transition transition5, Transition transition8, State state_source_setup,
			State state_target_setup) {

		String result8 = "\n";

		State state_source6 = (State) transition10.getSource();
		State state_target6 = (State) transition10.getTarget();

		result8 = result8 + "@Test\n";
		result8 = result8 + "public void test_transition" + index + "() {\n";

		// Setup
		result8 = result8 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result8 = result8 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result8 = result8 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result8 = result8 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result8 = result8 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result8 = result8 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition8.getSource().getName() + ");\n";
		result8 = result8 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result8 = result8 + "\t// Test begin;\n";
		result8 = result8 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source6.getName() + ");\n";
		result8 = result8 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition10.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result8 = result8 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target6.getName() + ");\n";
		result8 = result8 + "}\n";

		return result8;
	}

	private static String result9(int index, Region region, StateMachine stm, Transition transition11,
			Transition transition1, Transition transition5, State state_source_setup, State state_target_setup) {

		// next step, transition
		String result9 = "\n";

		State state_source7 = (State) transition11.getSource();
		State state_target7 = (State) transition11.getTarget();

		result9 = result9 + "@Test\n";
		result9 = result9 + "public void test_transition" + index + "() {\n";

		// Setup
		result9 = result9 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result9 = result9 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result9 = result9 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result9 = result9 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result9 = result9 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result9 = result9 + "\t// Test begin;\n";
		result9 = result9 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source7.getName() + ");\n";
		result9 = result9 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition11.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result9 = result9 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target7.getName() + ");\n";
		result9 = result9 + "}\n";

		return result9;
	}

	private static String result10(int index, Region region, StateMachine stm, Transition transition12,
			Transition transition1, Transition transition5, State state_source_setup, State state_target_setup) {

		String result10 = "\n";

		State state_source8 = (State) transition12.getSource();
		State state_target8 = (State) transition12.getTarget();

		result10 = result10 + "@Test\n";
		result10 = result10 + "public void test_transition" + index + "() {\n";

		// Setup
		result10 = result10 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result10 = result10 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result10 = result10 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result10 = result10 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result10 = result10 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result10 = result10 + "\t// Test begin;\n";
		result10 = result10 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source8.getName() + ");\n";
		result10 = result10 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition12.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result10 = result10 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target8.getName() + ");\n";
		result10 = result10 + "}\n";

		return result10;
	}

	private static String result11(int index, Region region, StateMachine stm, Transition transition1,
			Transition transition2) {

		String result11 = "\n";

		State state_source9 = (State) transition1.getSource();
		State state_target9 = (State) transition1.getTarget();

		result11 = result11 + "@Test\n";
		result11 = result11 + "public void test_transition" + index + "() {\n";

		// Setup
		result11 = result11 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result11 = result11 + "\t// Test begin;\n";
		result11 = result11 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source9.getName() + ");\n";
		result11 = result11 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition2.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result11 = result11 + "\tTestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target9.getName() + ");\n";
		result11 = result11 + "}\n";

		return result11;
	}

	private static String result12(int index, Region region, StateMachine stm, Transition transition1,
			Transition transition5) {

		String result12 = "\n";

		State state_source10 = (State) transition1.getSource();
		State state_target10 = (State) transition1.getTarget();

		result12 = result12 + "@Test\n";
		result12 = result12 + "public void test_transition" + index + "() {\n";

		// Setup
		result12 = result12 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result12 = result12 + "\t// Test begin;\n";
		result12 = result12 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source10.getName() + ");\n";
		result12 = result12 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result12 = result12 + "\tTestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target10.getName() + ");\n";
		result12 = result12 + "}\n";

		return result12;
	}

	private static String result13(int index, Region region, StateMachine stm, Transition transition1,
			Transition transition7) {

		String result13 = "\n";

		State state_source11 = (State) transition1.getSource();
		State state_target11 = (State) transition1.getTarget();

		result13 = result13 + "@Test\n";
		result13 = result13 + "public void test_transition" + index + "() {\n";

		// Setup
		result13 = result13 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result13 = result13 + "\t// Test begin;\n";
		result13 = result13 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source11.getName() + ");\n";
		result13 = result13 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition7.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result13 = result13 + "\tTestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target11.getName() + ");\n";
		result13 = result13 + "}\n";

		return result13;
	}

	private static String result14(int index, Region region, StateMachine stm, Transition transition2,
			Transition transition1, State state_source_setup) {

		String result14 = "\n";

		State state_source12 = (State) transition2.getSource();
		State state_target12 = (State) transition2.getTarget();

		result14 = result14 + "@Test\n";
		result14 = result14 + "public void test_transition" + index + "() {\n";

		// Setup
		result14 = result14 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result14 = result14 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result14 = result14 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result14 = result14 + "\t// Test begin;\n";
		result14 = result14 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source12.getName() + ");\n";
		result14 = result14 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result14 = result14 + "\tTestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target12.getName() + ");\n";

		result14 = result14 + "}\n";

		return result14;
	}

	private static String result15(int index, Region region, StateMachine stm, Transition transition2,
			Transition transition1, Transition transition7, State state_source_setup) {

		String result15 = "\n";

		State state_source13 = (State) transition2.getSource();
		State state_target13 = (State) transition2.getTarget();

		result15 = result15 + "@Test\n";
		result15 = result15 + "public void test_transition" + index + "() {\n";

		// Setup
		result15 = result15 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result15 = result15 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result15 = result15 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result15 = result15 + "\t// Test begin;\n";
		result15 = result15 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source13.getName() + ");\n";
		result15 = result15 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition7.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result15 = result15 + "\tTestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target13.getName() + ");\n";

		result15 = result15 + "}\n";

		return result15;
	}

	private static String result16(int index, Region region, StateMachine stm, Transition transition4,
			Transition transition1, State state_source_setup, State state_target_setup) {

		String result16 = "\n";

		State state_source14 = (State) transition4.getSource();
		State state_target14 = (State) transition4.getTarget();

		result16 = result16 + "@Test\n";
		result16 = result16 + "public void test_transition" + index + "() {\n";

		// Setup
		result16 = result16 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result16 = result16 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result16 = result16 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result16 = result16 + "\t// Test begin;\n";
		result16 = result16 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";
		result16 = result16 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition4.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result16 = result16 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source14.getName() + ");\n";
		result16 = result16 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n"; // TO MODIFY: FALSE-TRIGGER; HERE COUNT_DOWN => TRIGGER TRANSITION1
		result16 = result16 + "\tTestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target14.getName() + ");\n";

		result16 = result16 + "}\n";

		return result16;
	}

	private static String result17(int index, Region region, StateMachine stm, Transition transition4,
			Transition transition1, Transition transition5, State state_source_setup, State state_target_setup) {

		String result17 = "\n";

		State state_source15 = (State) transition4.getSource();
		State state_target15 = (State) transition4.getTarget();

		result17 = result17 + "@Test\n";
		result17 = result17 + "public void test_transition" + index + "() {\n";

		// Setup
		result17 = result17 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result17 = result17 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result17 = result17 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result17 = result17 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";
		result17 = result17 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition4.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result17 = result17 + "\t// Test begin;\n";
		result17 = result17 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source15.getName() + ");\n";
		result17 = result17 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result17 = result17 + "\tTestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target15.getName() + ");\n";

		result17 = result17 + "}\n";
		return result17;
	}

	private static String result18(int index, Region region, StateMachine stm, Transition transition4,
			Transition transition1, Transition transition7, State state_source_setup, State state_target_setup) {

		String result18 = "\n";

		State state_source16 = (State) transition4.getSource();
		State state_target16 = (State) transition4.getTarget();

		result18 = result18 + "@Test\n";
		result18 = result18 + "public void test_transition" + index + "() {\n";

		// Setup
		result18 = result18 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result18 = result18 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result18 = result18 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result18 = result18 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";
		result18 = result18 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition4.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result18 = result18 + "\t// Test begin;\n";
		result18 = result18 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source16.getName() + ");\n";
		result18 = result18 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition7.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result18 = result18 + "\tTestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target16.getName() + ");\n";

		result18 = result18 + "}\n";

		return result18;
	}

	private static String result19(int index, Region region, StateMachine stm, Transition transition9,
			Transition transition1, Transition transition5, Transition transition8, State state_source_setup,
			State state_target_setup) {

		String result19 = "\n";

		State state_source17 = (State) transition9.getSource();
		State state_target17 = (State) transition9.getTarget();

		result19 = result19 + "@Test\n";
		result19 = result19 + "public void test_transition" + index + "() {\n";

		// Setup
		result19 = result19 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result19 = result19 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result19 = result19 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result19 = result19 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result19 = result19 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result19 = result19 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition8.getSource().getName() + ");\n";
		result19 = result19 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result19 = result19 + "\t// Test begin;\n";
		result19 = result19 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source17.getName() + ");\n";
		result19 = result19 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result19 = result19 + "\tTestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target17.getName() + ");\n";
		result19 = result19 + "}\n";

		return result19;
	}

	private static String result20(int index, Region region, StateMachine stm, Transition transition9,
			Transition transition1, Transition transition5, Transition transition8, State state_source_setup,
			State state_target_setup) {

		String result20 = "\n";

		State state_source18 = (State) transition9.getSource();
		State state_target18 = (State) transition9.getTarget();

		result20 = result20 + "@Test\n";
		result20 = result20 + "public void test_transition" + index + "() {\n";

		// Setup
		result20 = result20 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result20 = result20 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result20 = result20 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result20 = result20 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result20 = result20 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result20 = result20 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition8.getSource().getName() + ");\n";
		result20 = result20 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result20 = result20 + "\t// Test begin;\n";
		result20 = result20 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source18.getName() + ");\n";
		result20 = result20 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result20 = result20 + "\tTestUtil.call_assertNotEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target18.getName() + ");\n";
		result20 = result20 + "}\n";

		return result20;
	}

	private static String result21(int index, Region region, StateMachine stm, Transition transition4,
			Transition transition1, Transition transition2, State state_source_setup, State state_target_setup) {

		String result21 = "\n";

		State state_source19 = (State) transition4.getSource();
		State state_target19 = (State) transition4.getTarget();

		result21 = result21 + "@Test\n";
		result21 = result21 + "public void test_transition" + index + "() {\n";

		// Setup
		result21 = result21 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result21 = result21 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result21 = result21 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result21 = result21 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";
		result21 = result21 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition2.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result21 = result21 + "\t// Test begin;\n";
		result21 = result21 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source19.getName() + ");\n";
		result21 = result21 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition4.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result21 = result21 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target19.getName() + ");\n";
		result21 = result21 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition2.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result21 = result21 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source19.getName() + ");\n";

		result21 = result21 + "}\n";

		return result21;
	}

	private static String result22(int index, Region region, StateMachine stm, Transition transition4,
			Transition transition1, Transition transition2, Transition transition5, State state_source_setup,
			State state_target_setup) {

		String result22 = "\n";

		State state_source20 = (State) transition4.getSource();
		State state_target20 = (State) transition4.getTarget();

		result22 = result22 + "@Test\n";
		result22 = result22 + "public void test_transition" + index + "() {\n";

		// Setup
		result22 = result22 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result22 = result22 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result22 = result22 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result22 = result22 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";
		result22 = result22 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition2.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result22 = result22 + "\t// Test begin;\n";
		result22 = result22 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source20.getName() + ");\n";
		result22 = result22 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition4.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result22 = result22 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target20.getName() + ");\n";
		result22 = result22 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result22 = result22 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition5.getTarget().getName() + ");\n";

		result22 = result22 + "}\n";

		return result22;
	}

	private static String result23(int index, Region region, StateMachine stm, Transition transition6,
			Transition transition1, Transition transition5, Transition transition2, State state_source_setup,
			State state_target_setup) {

		String result23 = "\n";

		State state_source21 = (State) transition6.getSource();
		State state_target21 = (State) transition6.getTarget();

		result23 = result23 + "@Test\n";
		result23 = result23 + "public void test_transition" + index + "() {\n";

		// Setup
		result23 = result23 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result23 = result23 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result23 = result23 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result23 = result23 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";
		result23 = result23 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result23 = result23 + "\t// Test begin;\n";
		result23 = result23 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source21.getName() + ");\n";
		result23 = result23 + "\tm_MOC.getMicrowave_oven_model().setTime(0);\n";
		result23 = result23 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition6.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result23 = result23 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target21.getName() + ");\n";
		result23 = result23 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition2.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result23 = result23 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition2.getTarget().getName() + ");\n";

		result23 = result23 + "}\n";

		return result23;
	}

	private static String result24(int index, Region region, StateMachine stm, Transition transition7,
			Transition transition1, Transition transition5, Transition transition2, State state_source_setup,
			State state_target_setup) {

		String result24 = "\n";

		State state_source22 = (State) transition7.getSource();
		State state_target22 = (State) transition7.getTarget();

		result24 = result24 + "@Test\n";
		result24 = result24 + "public void test_transition" + index + "() {\n";

		// Setup
		result24 = result24 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result24 = result24 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result24 = result24 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result24 = result24 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";
		result24 = result24 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result24 = result24 + "\t// Test begin;\n";
		result24 = result24 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source22.getName() + ");\n";
		result24 = result24 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition7.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result24 = result24 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target22.getName() + ");\n";
		result24 = result24 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition2.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result24 = result24 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition2.getTarget().getName() + ");\n";

		result24 = result24 + "}\n";

		return result24;
	}

	private static String result25(int index, Region region, StateMachine stm, Transition transition6,
			Transition transition1, Transition transition5, Transition transition4, State state_source_setup,
			State state_target_setup) {
		String result25 = "\n";

		State state_source23 = (State) transition6.getSource();
		State state_target23 = (State) transition6.getTarget();

		result25 = result25 + "@Test\n";
		result25 = result25 + "public void test_transition" + index + "() {\n";

		// Setup
		result25 = result25 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result25 = result25 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result25 = result25 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result25 = result25 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";
		result25 = result25 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result25 = result25 + "\t// Test begin;\n";
		result25 = result25 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source23.getName() + ");\n";
		result25 = result25 + "\tm_MOC.getMicrowave_oven_model().setTime(0);\n";
		result25 = result25 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition6.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result25 = result25 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target23.getName() + ");\n";
		result25 = result25 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result25 = result25 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition5.getTarget().getName() + ");\n";

		result25 = result25 + "}\n";
		return result25;
	}

	private static String result26(int index, Region region, StateMachine stm, Transition transition7,
			Transition transition1, Transition transition5, Transition transition4, State state_source_setup,
			State state_target_setup) {

		String result26 = "\n";

		State state_source24 = (State) transition7.getSource();
		State state_target24 = (State) transition7.getTarget();

		result26 = result26 + "@Test\n";
		result26 = result26 + "public void test_transition" + index + "() {\n";

		// Setup
		result26 = result26 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result26 = result26 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result26 = result26 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result26 = result26 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";
		result26 = result26 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result26 = result26 + "\t// Test begin;\n";
		result26 = result26 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source24.getName() + ");\n";
		result26 = result26 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition7.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result26 = result26 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target24.getName() + ");\n";
		result26 = result26 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result26 = result26 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition5.getTarget().getName() + ");\n";

		result26 = result26 + "}\n";

		return result26;
	}

	private static String result27(int index, Region region, StateMachine stm, Transition transition5,
			Transition transition1, Transition transition11, Transition transition6, State state_source_setup,
			State state_target_setup) {

		String result27 = "\n";

		State state_source25 = (State) transition5.getSource();
		State state_target25 = (State) transition5.getTarget();

		result27 = result27 + "@Test\n";
		result27 = result27 + "public void test_transition" + index + "() {\n";

		// Setup
		result27 = result27 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result27 = result27 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result27 = result27 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result27 = result27 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source25.getName() + ");\n";
		result27 = result27 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result27 = result27 + "\t// Test begin;\n";
		result27 = result27 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target25.getName() + ");\n";
		result27 = result27 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition11.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result27 = result27 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition11.getTarget().getName() + ");\n";
		result27 = result27 + "\tm_MOC.getMicrowave_oven_model().setTime(0);\n";
		result27 = result27 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition6.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result27 = result27 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition6.getTarget().getName() + ");\n";

		result27 = result27 + "}\n";
		return result27;
	}

	private static String result28(int index, Region region, StateMachine stm, Transition transition5,
			Transition transition1, Transition transition11, Transition transition7, State state_source_setup,
			State state_target_setup) {

		String result28 = "\n";

		State state_source26 = (State) transition5.getSource();
		State state_target26 = (State) transition5.getTarget();

		result28 = result28 + "@Test\n";
		result28 = result28 + "public void test_transition" + index + "() {\n";
		// Setup
		result28 = result28 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result28 = result28 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result28 = result28 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result28 = result28 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source26.getName() + ");\n";
		result28 = result28 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result28 = result28 + "\t// Test begin;\n";
		result28 = result28 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target26.getName() + ");\n";
		result28 = result28 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition11.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result28 = result28 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition11.getTarget().getName() + ");\n";
		result28 = result28 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition7.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result28 = result28 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition7.getTarget().getName() + ");\n";

		result28 = result28 + "}\n";

		return result28;
	}

	private static String result29(int index, Region region, StateMachine stm, Transition transition5,
			Transition transition1, Transition transition11, Transition transition8, State state_source_setup,
			State state_target_setup) {

		String result29 = "\n";

		State state_source27 = (State) transition5.getSource();
		State state_target27 = (State) transition5.getTarget();

		result29 = result29 + "@Test\n";
		result29 = result29 + "public void test_transition" + index + "() {\n";
		// Setup
		result29 = result29 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result29 = result29 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result29 = result29 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result29 = result29 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source27.getName() + ");\n";
		result29 = result29 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result29 = result29 + "\t// Test begin;\n";
		result29 = result29 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target27.getName() + ");\n";
		result29 = result29 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition11.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result29 = result29 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition11.getTarget().getName() + ");\n";
		result29 = result29 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result29 = result29 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition8.getTarget().getName() + ");\n";

		result29 = result29 + "}\n";

		return result29;
	}

	private static String result30(int index, Region region, StateMachine stm, Transition transition5,
			Transition transition1, Transition transition11, Transition transition12, State state_source_setup,
			State state_target_setup) {

		String result30 = "\n";

		State state_source28 = (State) transition5.getSource();
		State state_target28 = (State) transition5.getTarget();

		result30 = result30 + "@Test\n";
		result30 = result30 + "public void test_transition" + index + "() {\n";
		// Setup
		result30 = result30 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result30 = result30 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result30 = result30 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result30 = result30 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source28.getName() + ");\n";
		result30 = result30 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result30 = result30 + "\t// Test begin;\n";
		result30 = result30 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target28.getName() + ");\n";
		result30 = result30 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition11.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result30 = result30 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition11.getTarget().getName() + ");\n";
		result30 = result30 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition12.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result30 = result30 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition12.getTarget().getName() + ");\n";

		result30 = result30 + "}\n";

		return result30;
	}

	private static String result31(int index, Region region, StateMachine stm, Transition transition5,
			Transition transition1, Transition transition12, Transition transition11, Transition transition6,
			State state_source_setup, State state_target_setup) {

		String result31 = "\n";

		State state_source29 = (State) transition5.getSource();
		State state_target29 = (State) transition5.getTarget();

		result31 = result31 + "@Test\n";
		result31 = result31 + "public void test_transition" + index + "() {\n";

		// Setup
		result31 = result31 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result31 = result31 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result31 = result31 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result31 = result31 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source29.getName() + ");\n";
		result31 = result31 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result31 = result31 + "\t// Test begin;\n";
		result31 = result31 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target29.getName() + ");\n";
		result31 = result31 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition12.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result31 = result31 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition11.getTarget().getName() + ");\n";
		result31 = result31 + "\tm_MOC.getMicrowave_oven_model().setTime(0);\n";

		result31 = result31 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition6.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result31 = result31 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition6.getTarget().getName() + ");\n";

		result31 = result31 + "}\n";

		return result31;
	}

	private static String result32(int index, Region region, StateMachine stm, Transition transition5,
			Transition transition1, Transition transition12, Transition transition11, Transition transition7,
			Transition transition6, State state_source_setup, State state_target_setup) {

		String result32 = "\n";

		State state_source30 = (State) transition5.getSource();
		State state_target30 = (State) transition5.getTarget();

		result32 = result32 + "@Test\n";
		result32 = result32 + "public void test_transition" + index + "() {\n";

		// Setup
		result32 = result32 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result32 = result32 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result32 = result32 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result32 = result32 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source30.getName() + ");\n";
		result32 = result32 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result32 = result32 + "\t// Test begin;\n";
		result32 = result32 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target30.getName() + ");\n";
		result32 = result32 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition12.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result32 = result32 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition11.getTarget().getName() + ");\n";
		result32 = result32 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition7.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result32 = result32 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition6.getTarget().getName() + ");\n";

		result32 = result32 + "}\n";

		return result32;
	}

	private static String result33(int index, Region region, StateMachine stm, Transition transition5,
			Transition transition1, Transition transition12, Transition transition11, Transition transition8,
			State state_source_setup, State state_target_setup) {

		String result33 = "\n";

		State state_source31 = (State) transition5.getSource();
		State state_target31 = (State) transition5.getTarget();

		result33 = result33 + "@Test\n";
		result33 = result33 + "public void test_transition" + index + "() {\n";

		// Setup
		result33 = result33 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result33 = result33 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result33 = result33 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result33 = result33 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source31.getName() + ");\n";
		result33 = result33 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result33 = result33 + "\t// Test begin;\n";
		result33 = result33 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target31.getName() + ");\n";
		result33 = result33 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition12.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result33 = result33 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition11.getTarget().getName() + ");\n";
		result33 = result33 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result33 = result33 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition8.getTarget().getName() + ");\n";

		result33 = result33 + "}\n";

		return result33;
	}

	private static String result34(int index, Region region, StateMachine stm, Transition transition5,
			Transition transition1, Transition transition12, Transition transition11, State state_source_setup,
			State state_target_setup) {

		String result34 = "\n";

		State state_source32 = (State) transition5.getSource();
		State state_target32 = (State) transition5.getTarget();

		result34 = result34 + "@Test\n";
		result34 = result34 + "public void test_transition" + index + "() {\n";

		// Setup
		result34 = result34 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result34 = result34 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result34 = result34 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result34 = result34 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source32.getName() + ");\n";
		result34 = result34 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result34 = result34 + "\t// Test begin;\n";
		result34 = result34 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target32.getName() + ");\n";
		result34 = result34 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition12.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result34 = result34 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition11.getTarget().getName() + ");\n";
		result34 = result34 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition11.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result34 = result34 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition11.getTarget().getName() + ");\n";

		result34 = result34 + "}\n";

		return result34;
	}

	private static String result35(int index, Region region, StateMachine stm, Transition transition8,
			Transition transition1, Transition transition5, Transition transition9, Transition transition6,
			State state_source_setup, State state_target_setup) {

		String result35 = "\n";

		State state_source33 = (State) transition8.getSource();
		State state_target33 = (State) transition8.getTarget();

		result35 = result35 + "@Test\n";
		result35 = result35 + "public void test_transition" + index + "() {\n";

		// Setup
		result35 = result35 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result35 = result35 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result35 = result35 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result35 = result35 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result35 = result35 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result35 = result35 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source33.getName() + ");\n";
		result35 = result35 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result35 = result35 + "\t// Test begin;\n";
		result35 = result35 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target33.getName() + ");\n";

		result35 = result35 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition9.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result35 = result35 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition9.getTarget().getName() + ");\n";
		result35 = result35 + "\tm_MOC.getMicrowave_oven_model().setTime(0);\n";
		result35 = result35 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition6.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result35 = result35 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition6.getTarget().getName() + ");\n";

		result35 = result35 + "}\n";

		return result35;
	}

	private static String result36(int index, Region region, StateMachine stm, Transition transition8,
			Transition transition1, Transition transition5, Transition transition9, Transition transition7,
			State state_source_setup, State state_target_setup) {

		String result36 = "\n";

		State state_source34 = (State) transition8.getSource();
		State state_target34 = (State) transition8.getTarget();

		result36 = result36 + "@Test\n";
		result36 = result36 + "public void test_transition" + index + "() {\n";

		// Setup
		result36 = result36 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result36 = result36 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result36 = result36 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result36 = result36 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result36 = result36 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result36 = result36 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source34.getName() + ");\n";
		result36 = result36 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result36 = result36 + "\t// Test begin;\n";
		result36 = result36 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target34.getName() + ");\n";

		result36 = result36 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition9.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result36 = result36 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition9.getTarget().getName() + ");\n";

		result36 = result36 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition7.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result36 = result36 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition7.getTarget().getName() + ");\n";

		result36 = result36 + "}\n";

		return result36;
	}

	private static String result37(int index, Region region, StateMachine stm, Transition transition8,
			Transition transition1, Transition transition5, Transition transition9, Transition transition11,
			State state_source_setup, State state_target_setup) {

		String result37 = "\n";

		State state_source35 = (State) transition8.getSource();
		State state_target35 = (State) transition8.getTarget();

		result37 = result37 + "@Test\n";
		result37 = result37 + "public void test_transition" + index + "() {\n";

		// Setup
		result37 = result37 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result37 = result37 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result37 = result37 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result37 = result37 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result37 = result37 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result37 = result37 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source35.getName() + ");\n";
		result37 = result37 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result37 = result37 + "\t// Test begin;\n";
		result37 = result37 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target35.getName() + ");\n";

		result37 = result37 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition9.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result37 = result37 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition9.getTarget().getName() + ");\n";

		result37 = result37 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition11.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result37 = result37 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition11.getTarget().getName() + ");\n";

		result37 = result37 + "}\n";

		return result37;
	}

	private static String result38(int index, Region region, StateMachine stm, Transition transition8,
			Transition transition1, Transition transition5, Transition transition9, Transition transition12,
			State state_source_setup, State state_target_setup) {

		String result38 = "\n";

		State state_source36 = (State) transition8.getSource();
		State state_target36 = (State) transition8.getTarget();

		result38 = result38 + "@Test\n";
		result38 = result38 + "public void test_transition" + index + "() {\n";

		// Setup
		result38 = result38 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result38 = result38 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result38 = result38 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result38 = result38 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result38 = result38 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result38 = result38 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source36.getName() + ");\n";
		result38 = result38 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result38 = result38 + "\t// Test begin;\n";
		result38 = result38 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target36.getName() + ");\n";

		result38 = result38 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition9.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result38 = result38 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition9.getTarget().getName() + ");\n";

		result38 = result38 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition12.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result38 = result38 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition12.getTarget().getName() + ");\n";

		result38 = result38 + "}\n";

		return result38;
	}

	private static String result39(int index, Region region, StateMachine stm, Transition transition8,
			Transition transition1, Transition transition5, Transition transition9, State state_source_setup,
			State state_target_setup) {

		String result39 = "\n";

		State state_source37 = (State) transition8.getSource();
		State state_target37 = (State) transition8.getTarget();

		result39 = result39 + "@Test\n";
		result39 = result39 + "public void test_transition" + index + "() {\n";

		// Setup
		result39 = result39 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result39 = result39 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result39 = result39 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result39 = result39 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result39 = result39 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result39 = result39 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source37.getName() + ");\n";
		result39 = result39 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result39 = result39 + "\t// Test begin;\n";
		result39 = result39 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target37.getName() + ");\n";

		result39 = result39 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition9.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result39 = result39 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition9.getTarget().getName() + ");\n";

		result39 = result39 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result39 = result39 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition8.getTarget().getName() + ");\n";

		result39 = result39 + "}\n";

		return result39;
	}

	private static String result40(int index, Region region, StateMachine stm, Transition transition8,
			Transition transition1, Transition transition5, Transition transition10, Transition transition4,
			State state_source_setup, State state_target_setup) {

		String result40 = "\n";

		State state_source38 = (State) transition8.getSource();
		State state_target38 = (State) transition8.getTarget();

		result40 = result40 + "@Test\n";
		result40 = result40 + "public void test_transition" + index + "() {\n";

		// Setup
		result40 = result40 + "\t" + stm.getName() + "ControllerImplLogger m_MOC = new " + stm.getName()
				+ "ControllerImplLogger();\n";
		result40 = result40 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source_setup.getName() + ");\n";
		result40 = result40 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition1.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";

		result40 = result40 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target_setup.getName() + ");\n";

		result40 = result40 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition5.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result40 = result40 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_source38.getName() + ");\n";
		result40 = result40 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition8.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result40 = result40 + "\t// Test begin;\n";
		result40 = result40 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + state_target38.getName() + ");\n";

		result40 = result40 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition10.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result40 = result40 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition10.getTarget().getName() + ");\n";
		result40 = result40 + "\tm_MOC."
				+ ((CallEvent) ((Trigger) transition4.getTriggers().get(0)).getEvent()).getOperation().getName()
				+ "();\n";
		result40 = result40 + "\tTestUtil.call_assertEquals(m_MOC.getCurrentTopLevelState().getId(),TStateId."
				+ stm.getName() + "_" + region.getName() + "_" + transition4.getTarget().getName() + ");\n";

		result40 = result40 + "}\n";

		return result40;
	}

	/*
	 * Gruppe 1 (M2C)
	 */
	private static void process_statemachine_Model_to_Code(StateMachine stm) {

		StateMachine statemachine = stm;
		Region region = statemachine.getRegions().get(0);

		ArrayList<Transition> transitionsList = new ArrayList<Transition>();
		transitionsList.add(null);

		// All 11 Transitions
		for (int i = 1; i < 12; i++) {
			transitionsList.add(region.getTransitions().get(i));
		}

		int index = 2; // start Transition_number

		State state_source_setup = (State) transitionsList.get(1).getSource();
		State state_target_setup = (State) transitionsList.get(1).getTarget();

		String result = result(index++, region, stm, transitionsList.get(1), transitionsList.get(7),
				state_source_setup);
		index++; // Transition3 does not exist
		String result2 = result2(index++, region, stm, transitionsList.get(8), transitionsList.get(1),
				state_source_setup, state_target_setup);
		String result3 = result3(index++, region, stm, transitionsList.get(2), transitionsList.get(1),
				state_source_setup);
		String result4 = result4(index++, region, stm, transitionsList.get(3), transitionsList.get(1),
				transitionsList.get(2), state_source_setup, state_target_setup);
		String result5 = result5(index++, region, stm, transitionsList.get(10), transitionsList.get(1),
				transitionsList.get(2), state_source_setup, state_target_setup);
		String result6 = result6(index++, region, stm, transitionsList.get(5), transitionsList.get(1),
				transitionsList.get(2), state_source_setup, state_target_setup);
		String result7 = result7(index++, region, stm, transitionsList.get(6), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(5), state_source_setup, state_target_setup);
		String result8 = result8(index++, region, stm, transitionsList.get(9), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(5), state_source_setup, state_target_setup);
		String result9 = result9(index++, region, stm, transitionsList.get(4), transitionsList.get(1),
				transitionsList.get(2), state_source_setup, state_target_setup);
		String result10 = result10(index++, region, stm, transitionsList.get(11), transitionsList.get(1),
				transitionsList.get(2), state_source_setup, state_target_setup);
		String result11 = result11(index++, region, stm, transitionsList.get(1), transitionsList.get(7));
		String result12 = result12(index++, region, stm, transitionsList.get(1), transitionsList.get(2));
		String result13 = result13(index++, region, stm, transitionsList.get(1), transitionsList.get(10));
		String result14 = result14(index++, region, stm, transitionsList.get(7), transitionsList.get(1),
				state_source_setup);
		String result15 = result15(index++, region, stm, transitionsList.get(7), transitionsList.get(1),
				transitionsList.get(10), state_source_setup);
		String result16 = result16(index++, region, stm, transitionsList.get(8), transitionsList.get(1),
				state_source_setup, state_target_setup);
		String result17 = result17(index++, region, stm, transitionsList.get(8), transitionsList.get(1),
				transitionsList.get(2), state_source_setup, state_target_setup);
		String result18 = result18(index++, region, stm, transitionsList.get(8), transitionsList.get(1),
				transitionsList.get(10), state_source_setup, state_target_setup);
		String result19 = result19(index++, region, stm, transitionsList.get(6), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(5), state_source_setup, state_target_setup);
		String result20 = result20(index++, region, stm, transitionsList.get(6), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(5), state_source_setup, state_target_setup);
		String result21 = result21(index++, region, stm, transitionsList.get(8), transitionsList.get(1),
				transitionsList.get(7), state_source_setup, state_target_setup);
		String result22 = result22(index++, region, stm, transitionsList.get(8), transitionsList.get(1),
				transitionsList.get(7), transitionsList.get(2), state_source_setup, state_target_setup);
		String result23 = result23(index++, region, stm, transitionsList.get(3), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(7), state_source_setup, state_target_setup);
		String result24 = result24(index++, region, stm, transitionsList.get(10), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(7), state_source_setup, state_target_setup);
		String result25 = result25(index++, region, stm, transitionsList.get(3), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(8), state_source_setup, state_target_setup);
		String result26 = result26(index++, region, stm, transitionsList.get(10), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(8), state_source_setup, state_target_setup);
		String result27 = result27(index++, region, stm, transitionsList.get(2), transitionsList.get(1),
				transitionsList.get(4), transitionsList.get(3), state_source_setup, state_target_setup);
		String result28 = result28(index++, region, stm, transitionsList.get(2), transitionsList.get(1),
				transitionsList.get(4), transitionsList.get(10), state_source_setup, state_target_setup);
		String result29 = result29(index++, region, stm, transitionsList.get(2), transitionsList.get(1),
				transitionsList.get(4), transitionsList.get(5), state_source_setup, state_target_setup);
		String result30 = result30(index++, region, stm, transitionsList.get(2), transitionsList.get(1),
				transitionsList.get(4), transitionsList.get(5), state_source_setup, state_target_setup);
		String result31 = result31(index++, region, stm, transitionsList.get(2), transitionsList.get(1),
				transitionsList.get(11), transitionsList.get(4), transitionsList.get(3), state_source_setup,
				state_target_setup);
		String result32 = result32(index++, region, stm, transitionsList.get(2), transitionsList.get(1),
				transitionsList.get(11), transitionsList.get(4), transitionsList.get(10), transitionsList.get(3),
				state_source_setup, state_target_setup);
		String result33 = result33(index++, region, stm, transitionsList.get(2), transitionsList.get(1),
				transitionsList.get(11), transitionsList.get(4), transitionsList.get(5), state_source_setup,
				state_target_setup);
		String result34 = result34(index++, region, stm, transitionsList.get(2), transitionsList.get(1),
				transitionsList.get(11), transitionsList.get(4), state_source_setup, state_target_setup);
		String result35 = result35(index++, region, stm, transitionsList.get(5), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(6), transitionsList.get(3), state_source_setup,
				state_target_setup);
		String result36 = result36(index++, region, stm, transitionsList.get(5), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(6), transitionsList.get(10), state_source_setup,
				state_target_setup);
		String result37 = result37(index++, region, stm, transitionsList.get(5), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(6), transitionsList.get(4), state_source_setup,
				state_target_setup);
		String result38 = result38(index++, region, stm, transitionsList.get(5), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(6), transitionsList.get(11), state_source_setup,
				state_target_setup);
		String result39 = result39(index++, region, stm, transitionsList.get(5), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(6), state_source_setup, state_target_setup);
		String result40 = result40(index++, region, stm, transitionsList.get(5), transitionsList.get(1),
				transitionsList.get(2), transitionsList.get(9), transitionsList.get(8), state_source_setup,
				state_target_setup);

		System.out.println(result + result2 + result3 + result4 + result5 + result6 + result7 + result8 + result9
				+ result10 + result11 + result12 + result13 + result14 + result15 + result16 + result17 + result18
				+ result19 + result20 + result21 + result22 + result23 + result24 + result25 + result26 + result27
				+ result28 + result29 + result30 + result31 + result32 + result33 + result34 + result35 + result36
				+ result37 + result38 + result39 + result40); // print all Test results
	}

	/*
	 * Beispiel für Gruppe 2 (M2M)
	 */
	private static void process_statemachine_Model_to_Model(StateMachine stm) {

		StateMachine statemachine = stm;

		Region region = statemachine.getRegions().get(0);
		Transition transition1 = region.getTransitions().get(1);
		State state_target = (State) transition1.getTarget();
		State state_source = (State) transition1.getSource();

		UMLPackage.eINSTANCE.eClass();
		UMLFactory factory = UMLFactory.eINSTANCE;

		Model model = factory.createModel();
		model.setName("Block3");

		Package package_project = factory.createPackage();
		package_project.setName("project");

		model.getPackagedElements().add(package_project);

		Package package_impl = factory.createPackage();
		package_impl.setName(stm.getName());

		Class class_controller = factory.createClass();
		class_controller.setName(stm.getName() + "Controller");
		class_controller.setIsAbstract(true);
		package_impl.getPackagedElements().add(class_controller);

		Interface istate = factory.createInterface();
		istate.setName("IState");
		Operation istate_get_Id = factory.createOperation();
		istate_get_Id.setName("getId");
		istate.getOwnedOperations().add(istate_get_Id);
		class_controller.getNestedClassifiers().add(istate);

		Package package_test = factory.createPackage();
		package_test.setName(stm.getName() + "Test");

		package_project.getPackagedElements().add(package_impl);
		package_project.getPackagedElements().add(package_test);

		Class class_test = factory.createClass();
		class_test.setName(stm.getName() + "ControllerImplTest");
		package_test.getPackagedElements().add(class_test);

		Interaction interaction = factory.createInteraction();
		interaction.setName("test_state_" + state_target.getName() + "()");
		class_test.getOwnedBehaviors().add(interaction);

		Property property_a = factory.createProperty();
		property_a.setName("a");
		property_a.setType(class_test);
		interaction.getOwnedAttributes().add(property_a);

		Property property_temp_bool = factory.createProperty();
		property_temp_bool.setName("temp_bool");
		// Use proxy for primitive data types
		PrimitiveType primitiveType = factory.createPrimitiveType();
		InternalEObject io = (InternalEObject) EcoreUtil.create(primitiveType.eClass());
		io.eSetProxyURI(URI.createURI("pathmap://UML_LIBRARIES/EcorePrimitiveTypes.library.uml#EJavaObject"));
		property_temp_bool.setType((PrimitiveType) io);
		interaction.getOwnedAttributes().add(property_temp_bool);

		Property property_obj1 = factory.createProperty();
		property_obj1.setName("obj1");
		// Use proxy for primitive data types
		primitiveType = factory.createPrimitiveType();
		io = (InternalEObject) EcoreUtil.create(primitiveType.eClass());
		io.eSetProxyURI(URI.createURI("pathmap://UML_LIBRARIES/EcorePrimitiveTypes.library.uml#EJavaObject"));
		property_obj1.setType((PrimitiveType) io);
		interaction.getOwnedAttributes().add(property_obj1);

		Property property_obj2 = factory.createProperty();
		property_obj2.setName("obj2");
		// Use proxy for primitive data types
		primitiveType = factory.createPrimitiveType();
		io = (InternalEObject) EcoreUtil.create(primitiveType.eClass());
		io.eSetProxyURI(URI.createURI("pathmap://UML_LIBRARIES/EcorePrimitiveTypes.library.uml#EJavaObject"));
		property_obj2.setType((PrimitiveType) io);
		interaction.getOwnedAttributes().add(property_obj2);

		Class class_logger = factory.createClass();
		class_logger.setName(stm.getName() + "ControllerImplLogger");

		Operation class_logger_constructor = factory.createOperation();
		class_logger_constructor.setName(stm.getName() + "ControllerImplLogger");
		class_logger.getOwnedOperations().add(class_logger_constructor);

		Operation class_logger_get_Current_Top_Level_State = factory.createOperation();
		class_logger_get_Current_Top_Level_State.setName("getCurrentTopLevelState");
		class_logger.getOwnedOperations().add(class_logger_get_Current_Top_Level_State);

		package_test.getPackagedElements().add(class_logger);

		Property property_m_Moc = factory.createProperty();
		property_m_Moc.setName("m_Moc");
		property_m_Moc.setType(class_logger);
		interaction.getOwnedAttributes().add(property_m_Moc);

		Property property_obj1_1 = factory.createProperty();
		property_obj1_1.setName("obj1");
		property_obj1_1.setType(istate);
		interaction.getOwnedAttributes().add(property_obj1_1);

		Class class_testutil = factory.createClass();
		class_testutil.setName("TestUtil");
		Operation class_testutil_call_assert_Equals = factory.createOperation();
		class_testutil_call_assert_Equals.setName("call_assertEquals");
		class_testutil_call_assert_Equals.setIsStatic(true);
		class_testutil.getOwnedOperations().add(class_testutil_call_assert_Equals);
		package_test.getPackagedElements().add(class_testutil);

		Property property_b = factory.createProperty();
		property_b.setName("b");
		property_b.setType(class_testutil);
		interaction.getOwnedAttributes().add(property_b);

		ArrayList<Lifeline> lifelines = new ArrayList<Lifeline>();

		for (int i = 0; i < 7; i++) {
			Lifeline lifeline = factory.createLifeline();
			lifeline.setName("Lifeline" + (i + 1));
			interaction.getLifelines().add(lifeline);
			lifelines.add(lifeline);

			switch (i) {
			case 0:
				lifeline.setRepresents(property_a);
				break;
			case 1:
				lifeline.setRepresents(property_temp_bool);
				break;
			case 2:
				lifeline.setRepresents(property_obj1);
				break;
			case 3:
				lifeline.setRepresents(property_obj2);
				break;
			case 4:
				lifeline.setRepresents(property_m_Moc);
				break;
			case 5:
				lifeline.setRepresents(property_obj1_1);
				break;
			case 6:
				lifeline.setRepresents(property_b);
				break;
			}

		}

		int counter = 1;
		int counter_bes = 1;

		// Sub-sequence 1/7
		Message message1 = factory.createMessage();
		message1.setName("new Object()");
		message1.setMessageSort(MessageSort.CREATE_MESSAGE_LITERAL);

		MessageOccurrenceSpecification mos_send1 = factory.createMessageOccurrenceSpecification();
		mos_send1.setName("Message" + counter + "SendEvent");
		mos_send1.setCovered(lifelines.get(0));

		MessageOccurrenceSpecification mos_receive1 = factory.createMessageOccurrenceSpecification();
		mos_receive1.setName("Message" + counter + "ReceiveEvent");
		mos_receive1.setCovered(lifelines.get(1));

		message1.setSendEvent(mos_send1);
		message1.setReceiveEvent(mos_receive1);
		mos_send1.setMessage(message1);
		mos_receive1.setMessage(message1);

		BehaviorExecutionSpecification bes1 = factory.createBehaviorExecutionSpecification();
		bes1.getCovereds().add(lifelines.get(1));
		bes1.setName("BehaviorExecutionSpecification" + counter_bes);
		counter_bes = counter_bes + 1;

		interaction.getMessages().add(message1);
		interaction.getFragments().add(mos_send1);
		interaction.getFragments().add(mos_receive1);
		interaction.getFragments().add(bes1);

		counter = counter + 1;

		Message message2 = factory.createMessage();
		message2.setMessageSort(MessageSort.REPLY_LITERAL);

		MessageOccurrenceSpecification mos_send2 = factory.createMessageOccurrenceSpecification();
		mos_send2.setName("Message" + counter + "SendEvent");
		mos_send2.setCovered(lifelines.get(1));

		MessageOccurrenceSpecification mos_receive2 = factory.createMessageOccurrenceSpecification();
		mos_receive2.setName("Message" + counter + "ReceiveEvent");
		mos_receive2.setCovered(lifelines.get(0));

		message2.setSendEvent(mos_send2);
		message2.setReceiveEvent(mos_receive2);
		mos_send2.setMessage(message2);
		mos_receive2.setMessage(message2);

		bes1.setStart(mos_receive1);
		bes1.setFinish(mos_send2);

		interaction.getMessages().add(message2);
		interaction.getFragments().add(mos_send2);
		interaction.getFragments().add(mos_receive2);

		counter = counter + 1;

		// Sub-sequence 2/7
		Message message3 = factory.createMessage();
		message3.setName("new Object()");
		message3.setMessageSort(MessageSort.CREATE_MESSAGE_LITERAL);

		MessageOccurrenceSpecification mos_send3 = factory.createMessageOccurrenceSpecification();
		mos_send3.setName("Message" + counter + "SendEvent");
		mos_send3.setCovered(lifelines.get(0));

		MessageOccurrenceSpecification mos_receive3 = factory.createMessageOccurrenceSpecification();
		mos_receive3.setName("Message" + counter + "ReceiveEvent");
		mos_receive3.setCovered(lifelines.get(2));

		message3.setSendEvent(mos_send3);
		message3.setReceiveEvent(mos_receive3);
		mos_send3.setMessage(message3);
		mos_receive3.setMessage(message3);

		BehaviorExecutionSpecification bes2 = factory.createBehaviorExecutionSpecification();
		bes2.getCovereds().add(lifelines.get(2));
		bes2.setName("BehaviorExecutionSpecification" + counter_bes);
		counter_bes = counter_bes + 1;

		interaction.getMessages().add(message3);
		interaction.getFragments().add(mos_send3);
		interaction.getFragments().add(mos_receive3);
		interaction.getFragments().add(bes2);

		counter = counter + 1;

		Message message4 = factory.createMessage();
		message4.setMessageSort(MessageSort.REPLY_LITERAL);

		MessageOccurrenceSpecification mos_send4 = factory.createMessageOccurrenceSpecification();
		mos_send4.setName("Message" + counter + "SendEvent");
		mos_send4.setCovered(lifelines.get(2));

		MessageOccurrenceSpecification mos_receive4 = factory.createMessageOccurrenceSpecification();
		mos_receive4.setName("Message" + counter + "ReceiveEvent");
		mos_receive4.setCovered(lifelines.get(0));

		message4.setSendEvent(mos_send4);
		message4.setReceiveEvent(mos_receive4);
		mos_send4.setMessage(message4);
		mos_receive4.setMessage(message4);

		bes2.setStart(mos_receive3);
		bes2.setFinish(mos_send4);

		interaction.getMessages().add(message4);
		interaction.getFragments().add(mos_send4);
		interaction.getFragments().add(mos_receive4);

		counter = counter + 1;

		// Sub-sequence 3/7
		Message message5 = factory.createMessage();
		message5.setName("new Object()");
		message5.setMessageSort(MessageSort.CREATE_MESSAGE_LITERAL);

		MessageOccurrenceSpecification mos_send5 = factory.createMessageOccurrenceSpecification();
		mos_send5.setName("Message" + counter + "SendEvent");
		mos_send5.setCovered(lifelines.get(0));

		MessageOccurrenceSpecification mos_receive5 = factory.createMessageOccurrenceSpecification();
		mos_receive5.setName("Message" + counter + "ReceiveEvent");
		mos_receive5.setCovered(lifelines.get(3));

		message5.setSendEvent(mos_send5);
		message5.setReceiveEvent(mos_receive5);
		mos_send5.setMessage(message5);
		mos_receive5.setMessage(message5);

		BehaviorExecutionSpecification bes3 = factory.createBehaviorExecutionSpecification();
		bes3.getCovereds().add(lifelines.get(3));
		bes3.setName("BehaviorExecutionSpecification" + counter_bes);
		counter_bes = counter_bes + 1;

		interaction.getMessages().add(message5);
		interaction.getFragments().add(mos_send5);
		interaction.getFragments().add(mos_receive5);
		interaction.getFragments().add(bes3);

		counter = counter + 1;

		Message message6 = factory.createMessage();
		message6.setMessageSort(MessageSort.REPLY_LITERAL);

		MessageOccurrenceSpecification mos_send6 = factory.createMessageOccurrenceSpecification();
		mos_send6.setName("Message" + counter + "SendEvent");
		mos_send6.setCovered(lifelines.get(3));

		MessageOccurrenceSpecification mos_receive6 = factory.createMessageOccurrenceSpecification();
		mos_receive6.setName("Message" + counter + "ReceiveEvent");
		mos_receive6.setCovered(lifelines.get(0));

		message6.setSendEvent(mos_send6);
		message6.setReceiveEvent(mos_receive6);
		mos_send6.setMessage(message6);
		mos_receive6.setMessage(message6);

		bes3.setStart(mos_receive5);
		bes3.setFinish(mos_send6);

		interaction.getMessages().add(message6);
		interaction.getFragments().add(mos_send6);
		interaction.getFragments().add(mos_receive6);

		counter = counter + 1;

		// Sub-sequence 4/7
		Message message7 = factory.createMessage();
		message7.setSignature(class_logger_constructor);
		message7.setMessageSort(MessageSort.CREATE_MESSAGE_LITERAL);

		MessageOccurrenceSpecification mos_send7 = factory.createMessageOccurrenceSpecification();
		mos_send7.setName("Message" + counter + "SendEvent");
		mos_send7.setCovered(lifelines.get(0));

		MessageOccurrenceSpecification mos_receive7 = factory.createMessageOccurrenceSpecification();
		mos_receive7.setName("Message" + counter + "ReceiveEvent");
		mos_receive7.setCovered(lifelines.get(4));

		message7.setSendEvent(mos_send7);
		message7.setReceiveEvent(mos_receive7);
		mos_send7.setMessage(message7);
		mos_receive7.setMessage(message7);

		BehaviorExecutionSpecification bes4 = factory.createBehaviorExecutionSpecification();
		bes4.getCovereds().add(lifelines.get(4));
		bes4.setName("BehaviorExecutionSpecification" + counter_bes);
		counter_bes = counter_bes + 1;

		interaction.getMessages().add(message7);
		interaction.getFragments().add(mos_send7);
		interaction.getFragments().add(mos_receive7);
		interaction.getFragments().add(bes4);

		counter = counter + 1;

		Message message8 = factory.createMessage();
		message6.setMessageSort(MessageSort.REPLY_LITERAL);

		MessageOccurrenceSpecification mos_send8 = factory.createMessageOccurrenceSpecification();
		mos_send8.setName("Message" + counter + "SendEvent");
		mos_send8.setCovered(lifelines.get(4));

		MessageOccurrenceSpecification mos_receive8 = factory.createMessageOccurrenceSpecification();
		mos_receive8.setName("Message" + counter + "ReceiveEvent");
		mos_receive8.setCovered(lifelines.get(0));

		message8.setSendEvent(mos_send8);
		message8.setReceiveEvent(mos_receive8);
		mos_send8.setMessage(message8);
		mos_receive8.setMessage(message8);

		bes4.setStart(mos_receive7);
		bes4.setFinish(mos_send8);

		interaction.getMessages().add(message8);
		interaction.getFragments().add(mos_send8);
		interaction.getFragments().add(mos_receive8);

		counter = counter + 1;

		// Sub-sequence 5/7
		Message message9 = factory.createMessage();
		message9.setSignature(class_logger_get_Current_Top_Level_State);
		message9.setMessageSort(MessageSort.SYNCH_CALL_LITERAL);

		MessageOccurrenceSpecification mos_send9 = factory.createMessageOccurrenceSpecification();
		mos_send9.setName("Message" + counter + "SendEvent");
		mos_send9.setCovered(lifelines.get(0));

		MessageOccurrenceSpecification mos_receive9 = factory.createMessageOccurrenceSpecification();
		mos_receive9.setName("Message" + counter + "ReceiveEvent");
		mos_receive9.setCovered(lifelines.get(4));

		message9.setSendEvent(mos_send9);
		message9.setReceiveEvent(mos_receive9);
		mos_send9.setMessage(message9);
		mos_receive9.setMessage(message9);

		BehaviorExecutionSpecification bes5 = factory.createBehaviorExecutionSpecification();
		bes5.getCovereds().add(lifelines.get(4));
		bes5.setName("BehaviorExecutionSpecification" + counter_bes);
		counter_bes = counter_bes + 1;

		interaction.getMessages().add(message9);
		interaction.getFragments().add(mos_send9);
		interaction.getFragments().add(mos_receive9);
		interaction.getFragments().add(bes5);

		counter = counter + 1;

		Message message10 = factory.createMessage();
		message10.setMessageSort(MessageSort.REPLY_LITERAL);
		Expression expr1 = factory.createExpression();
		expr1.setName("obj1");
		message10.getArguments().add(expr1);

		MessageOccurrenceSpecification mos_send10 = factory.createMessageOccurrenceSpecification();
		mos_send10.setName("Message" + counter + "SendEvent");
		mos_send10.setCovered(lifelines.get(4));

		MessageOccurrenceSpecification mos_receive10 = factory.createMessageOccurrenceSpecification();
		mos_receive10.setName("Message" + counter + "ReceiveEvent");
		mos_receive10.setCovered(lifelines.get(0));

		message10.setSendEvent(mos_send10);
		message10.setReceiveEvent(mos_receive10);
		mos_send10.setMessage(message10);
		mos_receive10.setMessage(message10);

		bes5.setStart(mos_receive9);
		bes5.setFinish(mos_send10);

		interaction.getMessages().add(message10);
		interaction.getFragments().add(mos_send10);
		interaction.getFragments().add(mos_receive10);

		counter = counter + 1;

		// Sub-sequence 6/7
		Message message11 = factory.createMessage();
		message11.setSignature(istate_get_Id);
		message11.setMessageSort(MessageSort.SYNCH_CALL_LITERAL);

		MessageOccurrenceSpecification mos_send11 = factory.createMessageOccurrenceSpecification();
		mos_send11.setName("Message" + counter + "SendEvent");
		mos_send11.setCovered(lifelines.get(0));

		MessageOccurrenceSpecification mos_receive11 = factory.createMessageOccurrenceSpecification();
		mos_receive11.setName("Message" + counter + "ReceiveEvent");
		mos_receive11.setCovered(lifelines.get(5));

		message11.setSendEvent(mos_send11);
		message11.setReceiveEvent(mos_receive11);
		mos_send11.setMessage(message11);
		mos_receive11.setMessage(message11);

		BehaviorExecutionSpecification bes6 = factory.createBehaviorExecutionSpecification();
		bes6.getCovereds().add(lifelines.get(5));
		bes6.setName("BehaviorExecutionSpecification" + counter_bes);
		counter_bes = counter_bes + 1;

		interaction.getMessages().add(message11);
		interaction.getFragments().add(mos_send11);
		interaction.getFragments().add(mos_receive11);
		interaction.getFragments().add(bes6);

		counter = counter + 1;

		Message message12 = factory.createMessage();
		message12.setMessageSort(MessageSort.REPLY_LITERAL);
		Expression expr2 = factory.createExpression();
		expr2.setName("obj1");
		message12.getArguments().add(expr2);

		MessageOccurrenceSpecification mos_send12 = factory.createMessageOccurrenceSpecification();
		mos_send12.setName("Message" + counter + "SendEvent");
		mos_send12.setCovered(lifelines.get(5));

		MessageOccurrenceSpecification mos_receive12 = factory.createMessageOccurrenceSpecification();
		mos_receive12.setName("Message" + counter + "ReceiveEvent");
		mos_receive12.setCovered(lifelines.get(0));

		message12.setSendEvent(mos_send12);
		message12.setReceiveEvent(mos_receive12);
		mos_send12.setMessage(message12);
		mos_receive12.setMessage(message12);

		bes6.setStart(mos_receive11);
		bes6.setFinish(mos_send12);

		interaction.getMessages().add(message12);
		interaction.getFragments().add(mos_send12);
		interaction.getFragments().add(mos_receive12);

		counter = counter + 1;

		// Sub-sequence 7/7
		Message message13 = factory.createMessage();
		message13.setSignature(class_testutil_call_assert_Equals);
		Expression expr3 = factory.createExpression();
		expr3.setName("obj1");
		message13.getArguments().add(expr3);
		LiteralString ls = factory.createLiteralString();
		ls.setName("obj2");
		ls.setValue("TStateId." + stm.getName() + "_" + region.getName() + "_" + state_source.getName());
		message13.getArguments().add(ls);
		message13.setMessageSort(MessageSort.SYNCH_CALL_LITERAL);

		MessageOccurrenceSpecification mos_send13 = factory.createMessageOccurrenceSpecification();
		mos_send13.setName("Message" + counter + "SendEvent");
		mos_send13.setCovered(lifelines.get(0));

		MessageOccurrenceSpecification mos_receive13 = factory.createMessageOccurrenceSpecification();
		mos_receive13.setName("Message" + counter + "ReceiveEvent");
		mos_receive13.setCovered(lifelines.get(6));

		message13.setSendEvent(mos_send13);
		message13.setReceiveEvent(mos_receive13);
		mos_send13.setMessage(message13);
		mos_receive13.setMessage(message13);

		BehaviorExecutionSpecification bes7 = factory.createBehaviorExecutionSpecification();
		bes7.getCovereds().add(lifelines.get(6));
		bes7.setName("BehaviorExecutionSpecification" + counter_bes);
		counter_bes = counter_bes + 1;

		interaction.getMessages().add(message13);
		interaction.getFragments().add(mos_send13);
		interaction.getFragments().add(mos_receive13);
		interaction.getFragments().add(bes7);

		counter = counter + 1;

		Message message14 = factory.createMessage();
		message14.setMessageSort(MessageSort.REPLY_LITERAL);

		MessageOccurrenceSpecification mos_send14 = factory.createMessageOccurrenceSpecification();
		mos_send14.setName("Message" + counter + "SendEvent");
		mos_send14.setCovered(lifelines.get(6));

		MessageOccurrenceSpecification mos_receive14 = factory.createMessageOccurrenceSpecification();
		mos_receive14.setName("Message" + counter + "ReceiveEvent");
		mos_receive14.setCovered(lifelines.get(0));

		message14.setSendEvent(mos_send14);
		message14.setReceiveEvent(mos_receive14);
		mos_send14.setMessage(message14);
		mos_receive14.setMessage(message14);

		bes7.setStart(mos_receive13);
		bes7.setFinish(mos_send14);

		interaction.getMessages().add(message14);
		interaction.getFragments().add(mos_send14);
		interaction.getFragments().add(mos_receive14);

		counter = counter + 1;

		// Save the model
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("uml", new XMIResourceFactoryImpl());

		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("model/Block3.uml"));
		resource.getContents().add(model);

		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Beispiel für Gruppe 3 (M2C)
	 */
	private static void process_sequence_Model(Interaction sqm) {

		String result = "\n";

		Interaction interaction = sqm;

		result = result + "@Test\n";
		result = result + "public void " + interaction.getName() + " {\n";

		Lifeline lifeline_send = interaction.getLifelines().get(0);

		for (InteractionFragment interaction_fragment : lifeline_send.getCoveredBys()) {

			System.out.println(interaction_fragment);

			if (interaction_fragment instanceof MessageOccurrenceSpecification) {

				// Note: Do not use model comments for code generation
				MessageOccurrenceSpecification mos_send = (MessageOccurrenceSpecification) interaction_fragment;

				// Create Message (without signature aka. operation)
				if (mos_send.getMessage().getMessageSort().equals(MessageSort.CREATE_MESSAGE_LITERAL)
						&& mos_send.getMessage().getSignature() == null) {
					// ReceiveEvent
					MessageOccurrenceSpecification mos_receive = (MessageOccurrenceSpecification) mos_send.getMessage()
							.getReceiveEvent();
					Lifeline lifeline_receive = mos_receive.getCovered();
					String type = "undefined";
					if (lifeline_receive.getRepresents().getType().getName().equals("EJavaObject"))
						type = "Object";
					result = result + "\t" + type + " " + lifeline_receive.getRepresents().getName() + " = "
							+ mos_send.getMessage().getName() + ";\n";
				}
				;

				// Create Message (with signature aka. operation)
				if (mos_send.getMessage().getMessageSort().equals(MessageSort.CREATE_MESSAGE_LITERAL)
						&& mos_send.getMessage().getSignature() != null) {
					// ReceiveEvent
					MessageOccurrenceSpecification mos_receive = (MessageOccurrenceSpecification) mos_send.getMessage()
							.getReceiveEvent();
					Lifeline lifeline_receive = mos_receive.getCovered();
					String type = lifeline_receive.getRepresents().getType().getName();
					result = result + "\t" + type + " " + lifeline_receive.getRepresents().getName() + " = new "
							+ mos_send.getMessage().getSignature().getName() + "();\n";
				}
				;

				// Synch Call Message (with signature aka. operation)
				if (mos_send.getMessage().getMessageSort().equals(MessageSort.SYNCH_CALL_LITERAL)
						&& mos_send.getMessage().getSignature() != null) {

					// 1. Find return message (to get left-handed assignment field, maybe empty)

					// 1.1 Return message
					Message return_message = null;

					MessageOccurrenceSpecification mos_receive = (MessageOccurrenceSpecification) mos_send.getMessage()
							.getReceiveEvent();
					Lifeline lifeline_receive = mos_receive.getCovered();
					for (Element element : lifeline_receive.getCoveredBys()) {
						if (element instanceof BehaviorExecutionSpecification) {
							BehaviorExecutionSpecification bes = (BehaviorExecutionSpecification) element;
							if (bes.getStart().equals(mos_receive)) {
								System.out.println(bes.getName());
								bes.getFinish();
								return_message = ((MessageOccurrenceSpecification) bes.getFinish()).getMessage();
							}
						}
					}

					// 1.2 Find left-handed assignment field (aka. value specifications) - usually
					// only one argument (return)
					ValueSpecification return_field_specification = null;
					boolean left_handside_field_existing = false;

					for (ValueSpecification value_specification : return_message.getArguments()) {
						return_field_specification = value_specification;
						result = result + "\t" + value_specification.getName() + " = ";
						left_handside_field_existing = true;
					}

					// 2. Add operation context (object)

					// 2.1 Check whether type-cast needed (iff field is used reflexive i. e. obj1 =
					// .. obj1.operation();)
					boolean type_cast_needed = false;
					if (return_field_specification != null && return_field_specification.getName()
							.equals(lifeline_receive.getRepresents().getName())) {
						result = result + "((" + lifeline_receive.getRepresents().getType().getName() + ") ";
						type_cast_needed = true;
					}

					// 2.2 Insert object
					if (!left_handside_field_existing)
						result = result + "\t";

					// 2.3 Check whether operation call is static
					Operation operation = (Operation) mos_send.getMessage().getSignature();
					if (operation.isStatic()) {
						result = result + lifeline_receive.getRepresents().getType().getName();
					} else {
						result = result + lifeline_receive.getRepresents().getName();
					}

					if (type_cast_needed) {
						result = result + ").";
					} else {
						result = result + ".";
					}

					// 3. Assign right-handed expression (operation call)
					result = result + mos_send.getMessage().getSignature().getName() + "(";

					// 3.1 Insert operation call arguments - usually more than one (order of value
					// specifications/arguments match to order of operation parameters)
					boolean separator_neeeded = false;
					for (ValueSpecification value_specification : mos_send.getMessage().getArguments()) {

						String reference_or_value = "";

						if (value_specification instanceof LiteralString)
							reference_or_value = ((LiteralString) value_specification).getValue();
						if (value_specification instanceof Expression)
							reference_or_value = ((Expression) value_specification).getName();

						if (separator_neeeded) {
							result = result + ", " + reference_or_value;
						} else {
							result = result + reference_or_value;
						}
						separator_neeeded = true;
					}
					result = result + ");\n";

				}
			}

		}

		result = result + "\t//More todo ... \n";
		result = result + "}\n";
		System.out.println(result);
	}

}