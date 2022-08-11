package MicrowaveOven;

import javax.swing.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import java.awt.*;
import javax.swing.*;

/* @SuppressWarnings("serial") */
public class MicrowaveOvenView extends javax.swing.JFrame implements Observer{
	
	private javax.swing.JToolBar m_ToolBar;

	private MicrowaveOvenControllerImpl m_MOC = new MicrowaveOvenControllerImpl();

	private javax.swing.JTextArea m_DebugOutputTextBox;

	private int m_nEventCounter;

	private javax.swing.JScrollPane jScrollPane1;

	private javax.swing.JScrollPane jScrollPane2;

	private javax.swing.JSplitPane jSplitPane1;

	private javax.swing.JTextField m_jTextField;

	private boolean timer_active = true;

	public MicrowaveOvenView() {

		m_MOC.getMicrowave_oven_model().register(this);
		
   		initComponents();

                   // add one ToolBarButton per possible call event:
                   for( final Action a : m_MOC.getCallEvents() ) {
			if (!((String) a.getValue("Name")).equals("count_down")) {
				m_ToolBar.add( new AbstractAction((String) a.getValue("Name")) {
                           		public void actionPerformed(ActionEvent e) {
                                   		a.actionPerformed(e);
   						m_nEventCounter += 1;
                                   		UpdateDisplay();
                               		}
                           	});
			} else {
				new javax.swing.Timer(1000, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (timer_active) {
							a.actionPerformed(e);
							m_nEventCounter += 1;
                                   			UpdateDisplay();
						}
					}
				}).start();
			}
                   }
   		
   		m_jTextField = new JTextField(5);
   		m_ToolBar.add(m_jTextField);
                m_MOC.Initialize();
                UpdateDisplay();

   	}

   	public static void main(String[] args){
   		
   		java.awt.EventQueue.invokeLater(new Runnable() {
   			public void run() {
   				new MicrowaveOvenView().setVisible(true);
   			}
   		});

   	}

   	public void initComponents(){
   	
   		m_ToolBar = new javax.swing.JToolBar();
   		m_ToolBar.setFloatable(false);
   		m_ToolBar.setRollover(true);

   		jScrollPane1 = new javax.swing.JScrollPane();
   		jScrollPane2 = new javax.swing.JScrollPane();
   		jSplitPane1 = new javax.swing.JSplitPane();

   		m_DebugOutputTextBox = new javax.swing.JTextArea();

   		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
   		getContentPane().setLayout(layout);
       		
   		jSplitPane1.setBorder(null);
   		jSplitPane1.setDividerLocation(300);
       		jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

       		jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createCompoundBorder(), "Microwave oven:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11)));
       		jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createCompoundBorder(), "Debug output messages:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11)));
       		jScrollPane2.setViewportView(m_DebugOutputTextBox);
    		jSplitPane1.setTopComponent(jScrollPane1);
       		jSplitPane1.setRightComponent(jScrollPane2);

      		m_DebugOutputTextBox.setColumns(20);
       		m_DebugOutputTextBox.setFont(new java.awt.Font("Tahoma", 0, 11));
       		m_DebugOutputTextBox.setRows(5);
       		m_DebugOutputTextBox.setBorder(javax.swing.BorderFactory.createEtchedBorder());

   		layout.setHorizontalGroup(
         			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         			.addComponent(m_ToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
         			.addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
   		);

       		layout.setVerticalGroup(
   		      	layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         			.addGroup(layout.createSequentialGroup()
           		.addComponent(m_ToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
   			.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
   			.addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE))
       		);

   		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
   		setTitle("Microwave oven STM Tester");

   		pack();

   	}
   	public void UpdateDisplay(){

		if (m_MOC.getRootState().getRegions()[0].getCurrentState().getName().equals("Suspended")) {
	   		timer_active = false;
		}

		if (m_MOC.getRootState().getRegions()[0].getCurrentState().getName().equals("Ready")) {
	   		timer_active = false;
		}

		if (m_MOC.getRootState().getRegions()[0].getCurrentState().getName().equals("Active")) {
	   		timer_active = true;
		}

	 	String str = "";
           	str += "======= Event " + m_nEventCounter + " =========\n";
           	str += m_MOC.m_sDebugMessage;
                   m_MOC.m_sDebugMessage = "";
           	m_DebugOutputTextBox.setText(str + m_DebugOutputTextBox.getText());
           	m_DebugOutputTextBox.select(0, 0);

   	}

	
   	public void update(Observable observable){
   	
		m_jTextField.setText(String.valueOf(observable.getTime()));
		System.out.println(observable);
           	ImageIcon icon = new ImageIcon(observable.getVisualization());
   	        JLabel label = new JLabel(icon);

   		jScrollPane1.setViewportView(label);
   		jScrollPane1.getViewport().setBackground(Color.WHITE);

   	}



   }
