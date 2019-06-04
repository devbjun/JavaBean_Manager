package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import component.MutableTable;
import jdbc.oracle.manager.Managers;

@SuppressWarnings("serial")
public class MainFrame extends BasicFrame implements ActionListener, Runnable{

	private JPanel uppanLeft;
	private JPanel uppanRight;
	private JPanel tablePanel;
	private JPanel buttonPanel;

	private JButton managerBtn;
	private JButton cancel;
	private JButton ok;
	private ImageIcon managerIcon;
	private JLabel lb;
	private JPanel uppan;
	ManagerFrame mf;
	MutableTable tableMutable;
	DefaultListSelectionModel dlsm;
	Thread th;
	private int width = 890;

	/**
	 * MainFrame 생성자
	 * @param _title
	 * @param _width
	 * @param _height
	 */
	public MainFrame(String _title, int _width, int _height) {
		super(_title, _width, _height);

		Layout();



		setVisible(true);
	}

	public void Layout() {
		setLayout(new BorderLayout());
		UpPan();
		orderList();
		Bottom();

		th = new Thread(this);
		th.start();

		add(uppan, BorderLayout.NORTH);
		add(tablePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * 
	 *  카페 네임 및 관리자 버튼 구현
	 */
	public void UpPan() {


		uppan = new JPanel(new BorderLayout());

		uppanLeft = new JPanel();
		uppanRight = new JPanel();

		uppan.setBackground(Color.DARK_GRAY);
		uppanLeft.setBackground(Color.DARK_GRAY);
		uppanRight.setBackground(Color.DARK_GRAY);

		// "주문내역" 글씨 출력
		lb = new JLabel("주문내역");
		lb.setForeground(Color.WHITE);
		lb.setFont(new Font("Malgun Gothic", Font.BOLD, 25)); // 폰트 지정

		uppanLeft.add(lb);

		// 매니저프레임 버튼
		managerIcon = new ImageIcon("img/icon1.png");	// 매니저프레임 버튼 이미지

		managerBtn = new JButton(managerIcon);
		managerBtn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		managerBtn.setFocusable(false);
		managerBtn.setBackground(Color.WHITE);
		managerBtn.setForeground(Color.DARK_GRAY);
		managerBtn.setPreferredSize(new Dimension(60, 30)); // 버튼크기 지정
		// 매니저프레임 띄우는 코드
		managerBtn.addActionListener((e) -> {
			mf = new ManagerFrame("관리자", 600, 500);
		});
		

		uppanRight.add(managerBtn);


		uppan.add(uppanLeft, BorderLayout.WEST);
		uppan.add(uppanRight, BorderLayout.EAST);

		tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1,1));
	}

	/**
	 * 
	 * 테이블 출력
	 */
	public void orderList() {


/*
		Date time = new Date();

		SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd");
		String _startDay = format.format(time);	// _startDay 오늘날짜
*/


		try {
			// 동적 테이블 선언

			tableMutable = new MutableTable(Managers.getOrderNotReceivedAtToday());

			/**
			 *  각 셀의 넓이 지정
			 */
			int[] sColumn = { width / 151 * 14, width / 151 * 36, width / 151 * 25, width / 151 * 9, width / 151 * 14,width / 151 * 7,width / 151 * 11,width / 151 * 35};
			for (int i = 0; i < tableMutable.getHeader().length; i++) {
				MutableTable.getTable().getColumnModel().getColumn(i).setPreferredWidth(sColumn[i]);
			}
			
			MutableTable.getTable().getColumnModel().getColumn(4).setCellRenderer(MutableTable.getCelAlignRight());
			MutableTable.getTable().getColumnModel().getColumn(6).setCellRenderer(MutableTable.getCelAlignRight());
			
			// 동적 테이블 선언 및 람타식 형태의 이벤트 처리기 등록
			tableMutable.addListSelectionListener((e) -> {
				dlsm = (DefaultListSelectionModel) e.getSource();

				// TODO Auto-generated method stub
				if(!e.getValueIsAdjusting()) {

					// 선택된 행 전체 출력
					for (int i = 0; i < tableMutable.getHeader().length; i++) {
						System.out.print(((i == 0) ? "" : ", ") + tableMutable.getHeader()[i] + ": " + tableMutable.getContents()[dlsm.getAnchorSelectionIndex()][i]);
					}
					System.out.println();
				}
			});

			// 프레임에 동적 테이블 추가

			//테이블 갱신
			tablePanel.removeAll();
			tablePanel.add(tableMutable.getScrollTable());
			revalidate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * 주문취소, 수령완료 버튼
	 */
	public void Bottom() {
		buttonPanel = new JPanel(new GridLayout(1,2));
		
		cancel = new JButton("주문취소");
		cancel.addActionListener(this);
		cancel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		cancel.setFocusable(false);
		cancel.setBackground(Color.WHITE);
		cancel.setForeground(Color.DARK_GRAY);
		cancel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
		cancel.setPreferredSize(new Dimension(0, 40));
		
		ok = new JButton("수령완료");
		ok.addActionListener(this);
		ok.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		ok.setFocusable(false);
		ok.setBackground(Color.WHITE);
		ok.setForeground(Color.DARK_GRAY);
		ok.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
		ok.setPreferredSize(new Dimension(0, 40));
		
		revalidate();
		
		

		buttonPanel.add(cancel);
		buttonPanel.add(ok);
	}

	/**
	 * 날짜를 하루 더해주는 기능
	 * _startDay : 더하려는 날짜
	 * _endDay : _startDay에 하루를 더한 날짜
	 * @return 
	 */
/*
	public String dayPlus(Date _Day) {
		String _startDay = "";	// _startDay :더하려는 날짜
		String _endDay = "";	// _endDay : _startDay 에 하루를 더한 날짜
		SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd");	
		_startDay = format.format(_Day);	// 받아온 Date의 형식을 String 타입의 yyyy-MM-dd형식으로 변경
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(_startDay));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		c.add(Calendar.DATE, 1);  // 하루를 더해준다
		_endDay = format.format(c.getTime());  // 하루를 더한 날짜를  String 타입의 yyyy-MM-dd 형식으로 변경
		return _endDay;
	}
*/
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub


		/**
		 * 
		 * 수령완료
		 */
		if (e.getSource().equals(ok)) {
			try {
				Managers.setOrderComplete(tableMutable.getContents()[dlsm.getAnchorSelectionIndex()][0], "수령 완료");
				//				tableMutable.repaint();
				orderList();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		/**
		 * 
		 * 주문취소
		 */
		if(e.getSource().equals(cancel)) {
			if(JOptionPane.showConfirmDialog(null, "해당 주문을 취소하시겠습니까?", "주문 취소", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				try {
					Managers.setOrderComplete(tableMutable.getContents()[dlsm.getAnchorSelectionIndex()][0], "주문 취소");
					orderList();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				orderList();
				Thread.sleep(3000);
				System.out.println("ddd");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}