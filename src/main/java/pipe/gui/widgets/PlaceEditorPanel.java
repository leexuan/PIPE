package pipe.gui.widgets;

import pipe.gui.ApplicationSettings;
import pipe.gui.PetriNetTab;
import pipe.utilities.Copier;
import pipe.views.*;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author  pere
 */
public class PlaceEditorPanel 
        extends javax.swing.JPanel {
   
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final PlaceView _placeView;
   private final Boolean attributesVisible;
   private final Integer capacity;
   Integer marking;
   private final String name;
   private final PetriNetView _pnmlData;
   private final PetriNetTab _view;
   private final JRootPane rootPane;
   
   
   /**
    * Creates new form PlaceEditor
    * @param _rootPane
    * @param _placeView
    * @param _pnmlData
    * @param _view
    */
   public PlaceEditorPanel(JRootPane _rootPane, PlaceView _placeView,
           PetriNetView _pnmlData, PetriNetTab _view) {
      this._placeView = _placeView;
      this._pnmlData = _pnmlData;
      this._view = _view;
      attributesVisible = this._placeView.getAttributesVisible();
      capacity = this._placeView.getCapacity();
      name = this._placeView.getName();
      rootPane = _rootPane;
      initComponents();
      rootPane.setDefaultButton(okButton);

     /* MarkingParameter[] markings = _pnmlData.getMarkingParameters();
      if (markings.length > 0) {
         markingComboBox.addItem("");
         for (int i = 0; i < markings.length; i++) {
            markingComboBox.addItem(markings[i]);
         }
      } else {
         markingComboBox.setEnabled(false);
      }  
      
      if (mParameter != null){
         for (int i = 1; i < markingComboBox.getItemCount(); i++) {
            if (mParameter == (MarkingParameter)markingComboBox.getItemAt(i)){
               markingComboBox.setSelectedIndex(i);
            }
         }
      }*/
   }
   
   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
   // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
   private void initComponents() {
	  inputtedMarkings = new LinkedList<JSpinner>();
	  inputtedTokenClassNames = new LinkedList<String>();
       LinkedList<TokenView> tokenViews = ApplicationSettings.getApplicationView().getCurrentPetriNetView().getTokenViews();
	  java.awt.GridBagConstraints gridBagConstraints;


       JPanel placeEditorPanel = new JPanel();
       JLabel nameLabel = new JLabel();
      nameTextField = new javax.swing.JTextField();
       JLabel capacityLabel = new JLabel();
      
      capacitySpinner = new javax.swing.JSpinner();
      capacitySpinner.setModel(new SpinnerNumberModel(_placeView.getCapacity(),0,Integer.MAX_VALUE,1));
      attributesCheckBox = new javax.swing.JCheckBox();
     // markingComboBox = new javax.swing.JComboBox();
      capacity0Label = new javax.swing.JLabel();
       JPanel buttonPanel = new JPanel();
      okButton = new javax.swing.JButton();
       JButton cancelButton = new JButton();

      setLayout(new java.awt.GridBagLayout());

      placeEditorPanel.setLayout(new java.awt.GridBagLayout());

      placeEditorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Place Editor"));
      nameLabel.setText("Name:");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
      placeEditorPanel.add(nameLabel, gridBagConstraints);

      nameTextField.setText(_placeView.getName());
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
      placeEditorPanel.add(nameTextField, gridBagConstraints);

		// Now set new dimension used in for loop below
		Dimension d = new Dimension();
		d.setSize(50, 19);
		int x = 0;
		int y = 2;
		List<MarkingView> markingViews = _placeView.getCurrentMarkingView();
		for (TokenView tc : tokenViews) {
			if (tc.isEnabled()) {
				JLabel tokenClassName = new JLabel();
				JSpinner tokenClassMarking = new JSpinner();
				inputtedMarkings.add(tokenClassMarking);

				tokenClassName.setText(tc.getID() + ": ");
				inputtedTokenClassNames.add(tc.getID());
				gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridx = x;
				gridBagConstraints.gridy = y;
				gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
				gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
				placeEditorPanel.add(tokenClassName, gridBagConstraints);
				tokenClassMarking.setValue(0);
				tokenClassMarking.setMinimumSize(new java.awt.Dimension(50, 20));
				tokenClassMarking.setPreferredSize(new java.awt.Dimension(50, 20));
				tokenClassMarking.addChangeListener(new javax.swing.event.ChangeListener() {
				    public void stateChanged(javax.swing.event.ChangeEvent evt) {
				        	markingSpinnerStateChanged(evt, inputtedMarkings.size()-1);
				    }
			    });
				for (MarkingView currentMarkingView : markingViews) {
					if (tc.hasSameId(currentMarkingView.getToken())) {
						tokenClassMarking.setValue(currentMarkingView
								.getCurrentMarking());
					}
				}

				gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridx = x + 1;
				gridBagConstraints.gridy = y;
				gridBagConstraints.gridwidth = 3;
				gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
				placeEditorPanel.add(tokenClassMarking, gridBagConstraints);
				y++;
			}
		}

      capacityLabel.setText("Capacity:");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = y;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
      placeEditorPanel.add(capacityLabel, gridBagConstraints);

/*      markingSpinner.setMinimumSize(new java.awt.Dimension(50, 20));
      markingSpinner.setPreferredSize(new java.awt.Dimension(50, 20));
      markingSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
         public void stateChanged(javax.swing.event.ChangeEvent evt) {
            markingSpinnerStateChanged(evt);
         }
      });

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
      placeEditorPanel.add(markingSpinner, gridBagConstraints);*/

      capacitySpinner.setMinimumSize(new java.awt.Dimension(50, 20));
      capacitySpinner.setPreferredSize(new java.awt.Dimension(50, 20));
      capacitySpinner.addChangeListener(new javax.swing.event.ChangeListener() {
         public void stateChanged(javax.swing.event.ChangeEvent evt) {
            capacitySpinnerStateChanged(evt);
         }
      });

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = y;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
      placeEditorPanel.add(capacitySpinner, gridBagConstraints);
      int capacityPos = y;
      y++;
      
      attributesCheckBox.setSelected(_placeView.getAttributesVisible());
      attributesCheckBox.setText("Show place attributes");
      attributesCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
      attributesCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = y;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
      placeEditorPanel.add(attributesCheckBox, gridBagConstraints);

/*      markingComboBox.setMaximumSize(new java.awt.Dimension(162, 22));
      markingComboBox.setMinimumSize(new java.awt.Dimension(162, 22));
      markingComboBox.setPreferredSize(new java.awt.Dimension(162, 22));
      markingComboBox.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            markingComboBoxActionPerformed(evt);
         }
      });*/

/*      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
      placeEditorPanel.add(markingComboBox, gridBagConstraints);*/

      capacity0Label.setText("(no capacity restriction)    ");
      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = capacityPos;
      gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
      placeEditorPanel.add(capacity0Label, gridBagConstraints);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      gridBagConstraints.insets = new java.awt.Insets(5, 8, 5, 8);
      add(placeEditorPanel, gridBagConstraints);

      buttonPanel.setLayout(new java.awt.GridBagLayout());

      okButton.setText("OK");
      okButton.setMaximumSize(new java.awt.Dimension(75, 25));
      okButton.setMinimumSize(new java.awt.Dimension(75, 25));
      okButton.setPreferredSize(new java.awt.Dimension(75, 25));
      okButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            okButtonHandler(evt);
         }
      });
      okButton.addKeyListener(new java.awt.event.KeyAdapter() {
         public void keyPressed(java.awt.event.KeyEvent evt) {
            okButtonKeyPressed(evt);
         }
      });

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 9);
      buttonPanel.add(okButton, gridBagConstraints);

      cancelButton.setText("Cancel");
      cancelButton.addActionListener(new java.awt.event.ActionListener()
      {
          public void actionPerformed(java.awt.event.ActionEvent evt)
          {
              cancelButtonHandler(evt);
          }
      });

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
      gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 10);
      buttonPanel.add(cancelButton, gridBagConstraints);

      gridBagConstraints = new java.awt.GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
      add(buttonPanel, gridBagConstraints);

   }// </editor-fold>//GEN-END:initComponents

   private void markingSpinnerStateChanged(javax.swing.event.ChangeEvent evt, int posInList) {//GEN-FIRST:event_markingSpinnerStateChanged
/*      Integer capacity = (Integer)capacitySpinner.getValue();
      int totalMarkings = 0;
      for(JSpinner inputtedMarking:inputtedMarkings){
    	  totalMarkings += (Integer)inputtedMarking.getValue();
      }
      int markingOfCurrentSpinner = (Integer)inputtedMarkings.get(posInList).getValue();
      if (capacity > 0) {
         if (totalMarkings > capacity) {
        	 int overMarkingLimit = totalMarkings - capacity;
        	 inputtedMarkings.get(posInList).setValue(markingOfCurrentSpinner - overMarkingLimit);
         }
      }*/
   }//GEN-LAST:event_markingSpinnerStateChanged
   
   private final ChangeListener changeListener = new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
         JSpinner spinner = (JSpinner)evt.getSource();
         JSpinner.NumberEditor numberEditor =
                 ((JSpinner.NumberEditor)spinner.getEditor());
         numberEditor.getTextField().setBackground(new Color(255,255,255));
         spinner.removeChangeListener(this);
      }
   };   

   
/*   private void markingComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_markingComboBoxActionPerformed
      Integer index = markingComboBox.getSelectedIndex();

      if (index > 0){
         Integer value = ((MarkingParameter)markingComboBox.getItemAt(index)).getValue();
         markingSpinner.setValue(value);
      } 
   }//GEN-LAST:event_markingComboBoxActionPerformed
*/
   
   private void okButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_okButtonKeyPressed
      if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
         doOK();
      }
   }//GEN-LAST:event_okButtonKeyPressed

   private void doOK(){

      Integer newCapacity;

      try {
         newCapacity = (Integer)capacitySpinner.getValue();
      } catch (Exception e) {
         JSpinner.NumberEditor numberEditor =
                 ((JSpinner.NumberEditor)capacitySpinner.getEditor());
         numberEditor.getTextField().setBackground(new Color(255,0,0));
         capacitySpinner.addChangeListener(changeListener);
         capacitySpinner.requestFocusInWindow();
         return;
      }
      _view.getHistoryManager().newEdit(); // new "transaction""
		List<MarkingView> newMarkingViews = Copier.mediumCopy(_placeView.getCurrentMarkingView());
		int totalMarkings = 0;
		for (int i = 0; i < inputtedMarkings.size(); i++) {
			String tokenClassName = inputtedTokenClassNames.get(i);

            int pos = ApplicationSettings.getApplicationView().getCurrentPetriNetView().positionInTheList(tokenClassName,
                                                                                                  newMarkingViews);
			MarkingView m;
			if (pos >= 0) {
				m = newMarkingViews.get(pos);
			} else {
                m = new MarkingView(ApplicationSettings.getApplicationView().getCurrentPetriNetView().getTokenClassFromID(
                        tokenClassName), 0+"");
				newMarkingViews.add(m);
			}
			int currentMarking = m.getCurrentMarking();
			int newMarking = Integer.valueOf((Integer)inputtedMarkings.get(i)
					.getValue());
			totalMarkings += newMarking;
			try {
				if (newMarking < 0) {
					JOptionPane.showMessageDialog(null,
							"Marking cannot be less than 0. Please re-enter");
					return;
				} else if (newMarking != currentMarking) {
					m.setCurrentMarking(newMarking);
                    ApplicationSettings.getApplicationView().getCurrentTab().getHistoryManager().addEdit(
							_placeView.setCurrentMarking(newMarkingViews));
				}
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(null,
						"Please enter a positive integer greater or equal to 0.",
						"Invalid entry", JOptionPane.ERROR_MESSAGE);
				return;
			} catch (Exception exc) {
				exc.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Please enter a positive integer greater or equal to 0.",
						"Invalid entry", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}   
      
      if ((newCapacity > 0) && (newCapacity < totalMarkings)){
			JOptionPane.showMessageDialog(null,
					"Marking exceeds capacity of place. Please alter capacity or markings");
         return;
      }



      String newName = nameTextField.getText();
      if (!newName.equals(name)){
         if (_pnmlData.checkPlaceIDAvailability(newName)){
            _view.getHistoryManager().addEdit(_placeView.setPNObjectName(newName));
         } else{
            // aquest nom no est disponible...
            JOptionPane.showMessageDialog(null,
                    "There is already a place named " + newName, "Error",
                                JOptionPane.WARNING_MESSAGE);
            return;
         }
      }
      
      if (!newCapacity.equals(capacity)) {
         _view.getHistoryManager().addEdit(_placeView.setCapacity(newCapacity));
      }        
      
     /* if (markingComboBox.getSelectedIndex() >0) {
         // There's a marking parameter selected
         MarkingParameter parameter = 
                 (MarkingParameter)markingComboBox.getSelectedItem() ;
         if (parameter != mParameter){

            if (mParameter != null) {
               // The marking parameter has been changed
               _view.getHistoryManager().addEdit(place.changeMarkingParameter(
                        (MarkingParameter)markingComboBox.getSelectedItem()));
            } else {
               //The marking parameter has been changed
               _view.getHistoryManager().addEdit(place.setMarkingParameter(
                       (MarkingParameter)markingComboBox.getSelectedItem()));
            }
         }
      } else {
         // There is no marking parameter selected
         if (mParameter != null) {
            // The rate parameter has been changed
            _view.getHistoryManager().addEdit(place.clearMarkingParameter());
         }
         if (newMarking != marking) {
            _view.getHistoryManager().addEdit(place.setCurrentMarking(newMarking));
         }
      }*/
      
      if (attributesVisible != attributesCheckBox.isSelected()){
         _placeView.toggleAttributesVisible();
      }    
      _placeView.repaint();
      
      updateArcAndTran();
      exit();
   }
   private void updateArcAndTran(){
       Collection<ArcView> arcs= ApplicationSettings.getApplicationView().getCurrentPetriNetView().getArcsArrayList();
       for (ArcView arc : arcs)
       {
           arc.repaint();
       }
       Collection<TransitionView> trans = ApplicationSettings.getApplicationView().getCurrentPetriNetView().getTransitionsArrayList();
       for (TransitionView transition : trans)
       {
           transition.update();
       }
  }
   
   private void okButtonHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonHandler
      doOK();
   }//GEN-LAST:event_okButtonHandler

   
   private void exit() {
      rootPane.getParent().setVisible(false);
   }
   
   
   private void cancelButtonHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonHandler
      exit();
   }//GEN-LAST:event_cancelButtonHandler

   
   private void capacitySpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_capacitySpinnerStateChanged
      Integer capacity = (Integer)capacitySpinner.getValue();
      
      int totalMarkings = 0;
      for(JSpinner inputtedMarking:inputtedMarkings){
    	  totalMarkings += (Integer)inputtedMarking.getValue();
      }
      
      if (capacity > 0) {
/*         capacity0Label.setVisible(false);
         if (totalMarkings > capacity) {
        	 capacitySpinner.setValue(0);
         }*/
      } else {
         capacity0Label.setVisible(true);
      }
   }//GEN-LAST:event_capacitySpinnerStateChanged
      
   
   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JCheckBox attributesCheckBox;
    private javax.swing.JLabel capacity0Label;
    private javax.swing.JSpinner capacitySpinner;
    private javax.swing.JTextField nameTextField;
   private javax.swing.JButton okButton;
    private LinkedList<JSpinner> inputtedMarkings;
   private LinkedList<String> inputtedTokenClassNames;
   // End of variables declaration//GEN-END:variables

}
