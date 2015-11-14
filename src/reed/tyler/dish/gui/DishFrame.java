package reed.tyler.dish.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import reed.tyler.dish.SetDish;

public class DishFrame extends JFrame {

	private static final long serialVersionUID = -283404218178348716L;

	public static void main(String[] args) {
		new DishFrame().setVisible(true);
	}

	private List<SetDish> setters;

	public DishFrame() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		setters = new ArrayList<>();
		
		JPanel scroller = new JPanel();
		scroller.setLayout(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane(scroller);
		setContentPane(scrollPane);

		JButton roller = new JButton("Roll Week");
		roller.addActionListener(new RollWeekActionListener(setters));
		scroller.add(roller, BorderLayout.NORTH);

		JPanel week = new JPanel();

		scroller.add(week, BorderLayout.CENTER);
		GridBagLayout gbl_week = new GridBagLayout();
		gbl_week.columnWidths = new int[] { 200, 200, 200, 200, 200, 200, 200};
		gbl_week.rowHeights = new int[] { 120, };
		gbl_week.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		gbl_week.rowWeights = new double[] { 0.0 };
		week.setLayout(gbl_week);

		DayPanel pnlSunday = new DayPanel("Sunday");
		GridBagConstraints gbc_pnlSunday = new GridBagConstraints();
		gbc_pnlSunday.insets = new Insets(0, 0, 0, 5);
		gbc_pnlSunday.gridx = 0;
		gbc_pnlSunday.gridy = 0;
		week.add(pnlSunday, gbc_pnlSunday);
		setters.add(pnlSunday);

		DayPanel pnlMonday = new DayPanel("Monday");
		GridBagConstraints gbc_pnlMonday = new GridBagConstraints();
		gbc_pnlMonday.insets = new Insets(0, 0, 0, 5);
		gbc_pnlMonday.gridx = 1;
		gbc_pnlMonday.gridy = 0;
		week.add(pnlMonday, gbc_pnlMonday);
		setters.add(pnlMonday);

		DayPanel pnlTuesday = new DayPanel("Tuesday");
		GridBagConstraints gbc_pnlTuesday = new GridBagConstraints();
		gbc_pnlTuesday.insets = new Insets(0, 0, 0, 5);
		gbc_pnlTuesday.gridx = 2;
		gbc_pnlTuesday.gridy = 0;
		week.add(pnlTuesday, gbc_pnlTuesday);
		setters.add(pnlTuesday);

		DayPanel pnlWednesday = new DayPanel("Wednesday");
		GridBagConstraints gbc_pnlWednesday = new GridBagConstraints();
		gbc_pnlWednesday.insets = new Insets(0, 0, 0, 5);
		gbc_pnlWednesday.gridx = 3;
		gbc_pnlWednesday.gridy = 0;
		week.add(pnlWednesday, gbc_pnlWednesday);
		setters.add(pnlWednesday);

		DayPanel pnlThursday = new DayPanel("Thursday");
		GridBagConstraints gbc_pnlThursday = new GridBagConstraints();
		gbc_pnlThursday.insets = new Insets(0, 0, 0, 5);
		gbc_pnlThursday.gridx = 4;
		gbc_pnlThursday.gridy = 0;
		week.add(pnlThursday, gbc_pnlThursday);
		setters.add(pnlThursday);

		DayPanel pnlFriday = new DayPanel("Friday");
		GridBagConstraints gbc_pnlFriday = new GridBagConstraints();
		gbc_pnlFriday.insets = new Insets(0, 0, 0, 5);
		gbc_pnlFriday.gridx = 5;
		gbc_pnlFriday.gridy = 0;
		week.add(pnlFriday, gbc_pnlFriday);
		setters.add(pnlFriday);

		DayPanel pnlSaturday = new DayPanel("Saturday");
		GridBagConstraints gbc_pnlSaturday = new GridBagConstraints();
		gbc_pnlSaturday.gridx = 6;
		gbc_pnlSaturday.gridy = 0;
		week.add(pnlSaturday, gbc_pnlSaturday);
		setters.add(pnlSaturday);

		this.pack();
	}

}
