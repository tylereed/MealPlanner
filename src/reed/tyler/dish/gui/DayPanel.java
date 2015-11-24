package reed.tyler.dish.gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import reed.tyler.dish.DishInfo;
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
		setPreferredSize(new Dimension(200, 160));
		setMinimumSize(new Dimension(200, 160));
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("200px"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("40px"),
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("40px"),
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("40px"),}));

		JLabel lblWeekDay = new JLabel(label);
		add(lblWeekDay, "1, 2, fill, fill");

		txtMain = new JTextArea();
		txtMain.setFont(new Font("Arial Unicode MS", Font.PLAIN, 15));
		txtMain.setRows(2);
		txtMain.setEditable(false);
		txtMain.setWrapStyleWord(true);
		txtMain.setLineWrap(true);
		add(txtMain, "1, 4, fill, fill");

		txtVegetable = new JTextArea();
		txtVegetable.setFont(new Font("Arial Unicode MS", Font.PLAIN, 15));
		txtVegetable.setEditable(false);
		txtVegetable.setWrapStyleWord(true);
		txtVegetable.setLineWrap(true);
		add(txtVegetable, "1, 6, fill, fill");

		txtSide = new JTextArea();
		txtSide.setFont(new Font("Arial Unicode MS", Font.PLAIN, 15));
		txtSide.setEditable(false);
		txtSide.setWrapStyleWord(true);
		txtSide.setLineWrap(true);
		add(txtSide, "1, 8, fill, fill");

	}

	/**
	 * Sets the information for the main course to display
	 * @param main The dish to display
	 */
	@Override
	public void setMain(DishInfo main) {
		txtMain.setText(main.toString());
	}


	/**
	 * Sets the information for the vegetable to display
	 * @param veggie The dish to display
	 */
	@Override
	public void setVegetable(DishInfo veggie) {
		txtVegetable.setText(veggie.toString());
	}

	/**
	 * Sets the information for the side dish to display
	 * @param side The dish to display
	 */
	@Override
	public void setSide(DishInfo side) {
		txtSide.setText(side.toString());
	}

}
