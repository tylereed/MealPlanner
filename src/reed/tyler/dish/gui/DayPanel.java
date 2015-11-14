package reed.tyler.dish.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import reed.tyler.dish.Dish;
import reed.tyler.dish.SetDish;

public class DayPanel extends JPanel implements SetDish {

	private static final long serialVersionUID = 338481639074246096L;
	private JTextArea txtMain;
	private JTextArea txtVegetable;
	private JTextArea txtSide;

	/**
	 * Creates a panel that displays information about a meal for a day
	 * @param label The day of the week this panel is for
	 */
	public DayPanel(String label) {
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] {200};
		layout.rowHeights = new int[] {30};
		layout.columnWeights = new double[] { 0.0 };
		layout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		setLayout(layout);

		JLabel lblWeekDay = new JLabel(label);
		GridBagConstraints gbc_lblWeekDay = new GridBagConstraints();
		gbc_lblWeekDay.fill = GridBagConstraints.BOTH;
		gbc_lblWeekDay.insets = new Insets(0, 0, 5, 0);
		gbc_lblWeekDay.gridx = 0;
		gbc_lblWeekDay.gridy = 0;
		add(lblWeekDay, gbc_lblWeekDay);

		txtMain = new JTextArea();
		txtMain.setEditable(false);
		txtMain.setWrapStyleWord(true);
		txtMain.setLineWrap(true);
		txtMain.setMinimumSize(new Dimension(250, 20));
		GridBagConstraints gbc_txtMain = new GridBagConstraints();
		gbc_txtMain.fill = GridBagConstraints.BOTH;
		gbc_txtMain.insets = new Insets(0, 0, 5, 0);
		gbc_txtMain.gridx = 0;
		gbc_txtMain.gridy = 1;
		add(txtMain, gbc_txtMain);
		txtMain.setMinimumSize(new Dimension(200, 20));
		txtMain.setMinimumSize(new Dimension(200, 20));

		txtVegetable = new JTextArea();
		txtVegetable.setEditable(false);
		txtVegetable.setWrapStyleWord(true);
		txtVegetable.setLineWrap(true);
		GridBagConstraints gbc_txtVegetable = new GridBagConstraints();
		gbc_txtVegetable.fill = GridBagConstraints.BOTH;
		gbc_txtVegetable.insets = new Insets(0, 0, 5, 0);
		gbc_txtVegetable.gridx = 0;
		gbc_txtVegetable.gridy = 2;
		add(txtVegetable, gbc_txtVegetable);

		txtSide = new JTextArea();
		txtSide.setEditable(false);
		txtSide.setWrapStyleWord(true);
		txtSide.setLineWrap(true);
		GridBagConstraints gbc_txtSide = new GridBagConstraints();
		gbc_txtSide.fill = GridBagConstraints.BOTH;
		gbc_txtSide.gridx = 0;
		gbc_txtSide.gridy = 3;
		add(txtSide, gbc_txtSide);

	}

	/**
	 * Sets the information for the main course to display
	 * @param main The dish to display
	 */
	@Override
	public void setMain(Dish main) {
		txtMain.setText(main.getName());
	}


	/**
	 * Sets the information for the vegetable to display
	 * @param veggie The dish to display
	 */
	@Override
	public void setVegetable(Dish veggie) {
		txtVegetable.setText(veggie.getName());
	}

	/**
	 * Sets the information for the side dish to display
	 * @param side The dish to display
	 */
	@Override
	public void setSide(Dish side) {
		txtSide.setText(side.getName());
	}

}
