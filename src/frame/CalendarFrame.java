package frame;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;


@SuppressWarnings("serial")
public class CalendarFrame extends JFrame {

	private DefaultTableModel model;
	Calendar cal = new GregorianCalendar();
	JLabel label;
	private int year;
	private String month;
	private JTable table;

	public CalendarFrame(Point x) {

		this.setTitle("달력");
		this.setLocation(x);
		this.setSize(250,200);
		this.setLayout(new BorderLayout());
		this.setVisible(true);

		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // 현재 프레임만 닫음

		label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);

		JButton b1 = new JButton("<-");
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cal.add(Calendar.MONTH, -1);
				updateMonth();
			}
		});

		JButton b2 = new JButton("->");
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cal.add(Calendar.MONTH, +1);
				updateMonth();
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(b1,BorderLayout.WEST);
		panel.add(label,BorderLayout.CENTER);
		panel.add(b2,BorderLayout.EAST);


		String [] header = {"일","월","화","수","목","금","토"};
		model = new DefaultTableModel(null, header){
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		table = new JTable(model);
		table.getTableHeader().setResizingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 단일선택

		JScrollPane pane = new JScrollPane(table);

		this.add(panel,BorderLayout.NORTH);
		this.add(pane,BorderLayout.CENTER);

		this.updateMonth();
		
	}

	void updateMonth() {
		cal.set(Calendar.DAY_OF_MONTH, 1);

		month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.KOREA);
		year = cal.get(Calendar.YEAR);
		label.setText(year + "년 " + month);

		int startDay = cal.get(Calendar.DAY_OF_WEEK);
		int numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int weeks = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);

		model.setRowCount(0);
		model.setRowCount(weeks);

		int i = startDay-1;
		for(int day=1;day<=numberOfDays;day++){
			model.setValueAt(day, i/7 , i%7 );    
			i = i + 1;
		}

	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}


}
