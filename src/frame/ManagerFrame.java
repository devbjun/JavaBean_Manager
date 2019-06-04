package frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractButton;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import component.MutableTable;
import jdbc.oracle.manager.Items;
import jdbc.oracle.manager.Managers;

@SuppressWarnings("serial")
public class ManagerFrame extends BasicFrame implements ActionListener {

	private static JPanel panCalendar;
	private JPanel menuList;
	private JPanel leftCalendar;
	private JPanel rightCalendar;
	private JTabbedPane tapPanel;
	private JTextArea textStartDay;
	private JTextArea textEndDay;
	private ImageIcon iconCalendar = new ImageIcon("img/icon2.png");;
	private JButton calBtnL;
	private JButton calBtnR;

	private JLabel lb;
	private JButton inquire;
	private JPanel panInquire;
	private boolean check;
	private JPanel revenueConfirm;
	private JPanel panRevenue;
	CalendarFrame cf;
	private String startDate;
	private String endDate;
	private JPanel panTotalPrice;
	private JLabel totalPriceLb;
	private JLabel totalPrice;
	private JPanel panTotalOrderConfirm;
	private JPanel panOList;
	private String _endDay;
	private JButton orderInquire;
	private JButton revenueInquire;
	private JButton calBtnl;
	private JButton calBtnr;
	private JPanel howManySell;
	private JButton calBtnlL;
	private JButton calBtnrR;
	private JButton sellInquire;
	private JPanel numToOrder;
	private JPanel topPanel;
	private JButton inquireBtn;
	private TextField orderNum;
	private JPanel panSellTotal;
	private JPanel numToOrderPan;



	/**
	 * MainFrame 생성자
	 * @param _title
	 * @param _width
	 * @param _height
	 */
	public ManagerFrame(String _title, int _width, int _height) {
		super(_title, _width, _height);
		// TODO Auto-generated constructor stub
		Tab();

		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // 현재 프레임만 닫음
		setVisible(true);
	}

	public void Tab() {
		tapPanel = new JTabbedPane();
		tapPanel.setFont(new Font("Malgun Gothic", Font.BOLD, 13));

		//Calendar();
		menuList();
		RevenueConfirm();
		TotalOrderList();
		HowManySell();
		NumToOrder();

		tapPanel.addTab("매출 확인", revenueConfirm);
		tapPanel.addTab("주문내역 확인", panTotalOrderConfirm);
		tapPanel.addTab("판매 메뉴", menuList);
		tapPanel.addTab("판매량", howManySell);
		tapPanel.addTab("주문조회", numToOrder);

		add(tapPanel);
	}

	public void RevenueConfirm() {
		revenueConfirm = new JPanel(new BorderLayout());
		revenueConfirm.setBackground(Color.DARK_GRAY);

		//달력버튼
		calBtnl = new JButton(iconCalendar);
		calBtnl.addActionListener(this);

		calBtnr = new JButton(iconCalendar);
		calBtnr.addActionListener(this);

		//조회버튼
		revenueInquire = new JButton("조회");
		revenueInquire.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
		revenueInquire.addActionListener(this);

		Calendar(calBtnl, calBtnr, revenueInquire);

		/**
		 * 지정한 기간동안의 총 매출을 표시할 패널
		 */

		panTotalPrice = new JPanel();

		totalPriceLb = new JLabel("총 매출");
		totalPriceLb.setFont(new Font("",Font.PLAIN,12));
		totalPrice = new JLabel("              0원");
		totalPrice.setAlignmentX(RIGHT_ALIGNMENT);
		totalPrice.setFont(new Font("",Font.PLAIN,12));

		panTotalPrice.add(totalPriceLb);
		panTotalPrice.add(totalPrice);

		panCalendar.add(panTotalPrice);
		revenueConfirm.add(panCalendar, BorderLayout.NORTH);

		add(revenueConfirm);
	}

	public void TotalOrderList() {
		panTotalOrderConfirm = new JPanel(new BorderLayout());
		panTotalOrderConfirm.setBackground(Color.DARK_GRAY);
		//달력버튼
		calBtnL = new JButton(iconCalendar);
		calBtnL.addActionListener(this);

		calBtnR = new JButton(iconCalendar);
		calBtnR.addActionListener(this);
		//조회버튼
		orderInquire = new JButton("조회");
		orderInquire.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
		orderInquire.addActionListener(this);

		Calendar(calBtnL, calBtnR, orderInquire);

		panTotalOrderConfirm.add(panCalendar, BorderLayout.NORTH);

		add(panTotalOrderConfirm);
	}

	public void HowManySell() {
		howManySell = new JPanel(new BorderLayout());
		howManySell.setBackground(Color.DARK_GRAY);
		//달력버튼
		calBtnlL = new JButton(iconCalendar);
		calBtnlL.addActionListener(this);

		calBtnrR = new JButton(iconCalendar);
		calBtnrR.addActionListener(this);
		//조회버튼
		sellInquire = new JButton("조회");
		sellInquire.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
		sellInquire.addActionListener(this);

		Calendar(calBtnlL, calBtnrR, sellInquire);

		howManySell.add(panCalendar, BorderLayout.NORTH);

		add(howManySell);

	}
	
	public void NumToOrder() {
		numToOrder = new JPanel(new BorderLayout());
		numToOrder.setBackground(Color.DARK_GRAY);
		
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());;
		orderNum = new TextField();
		topPanel.add(orderNum, BorderLayout.CENTER);
		inquireBtn = new JButton();
		inquireBtn.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
		inquireBtn.addActionListener(this);
		topPanel.add(inquireBtn, BorderLayout.EAST);
		
		numToOrder.add(topPanel, BorderLayout.NORTH);
		
		add(numToOrder);
		
	}

	public void Calendar(JButton lfCadr, JButton rtCadr,JButton inquire) {


		//달력 아이콘
		//iconCalendar = new ImageIcon("img/icon2.png");

		//날짜설정패널
		panCalendar = new JPanel();
		panCalendar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

		//날짜 포맷설정
		Date today = new Date();
		SimpleDateFormat format;
		format = new SimpleDateFormat("yyyy년 MM월 dd일");

		/**
		 * 시작날짜 달력, 텍스트
		 */
		leftCalendar = new JPanel();
		leftCalendar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		//시작날짜 출력할 공간
		textStartDay = new JTextArea(1,10);
		textStartDay.setText(format.format(today));
		textStartDay.setBounds(0, 0, 0, 50);
		textStartDay.setEditable(false);
		//달력버튼
		//lfCadr = new JButton(iconCalendar);
		//calBtnL.addActionListener(this);

		leftCalendar.add(textStartDay);
		leftCalendar.add(lfCadr);

		lb = new JLabel("~");


		/**
		 * 종료날짜 달력, 텍스트
		 */
		rightCalendar = new JPanel();
		rightCalendar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		//종료날짜 출력할 공간
		textEndDay = new JTextArea(1,10);
		textEndDay.setText(format.format(today));
		textEndDay.setBounds(0, 0, 0, 50);
		textEndDay.setEditable(false);
		//달력버튼
		//calBtnR = new JButton(iconCalendar);
		//calBtnR.addActionListener(this);

		rightCalendar.add(textEndDay);
		rightCalendar.add(rtCadr);


		/**
		 * 조회버튼 넣을 패널
		 */
		panInquire = new JPanel();
		//조회버튼
		//inquire = new JButton("조회");
		//j.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
		//j.addActionListener(this);

		panInquire.add(inquire);

		/**
		 * 지정한 기간동안의 총 매출을 표시할 패널
		 */
		/*
		panTotalPrice = new JPanel();

		totalPriceLb = new JLabel("총 매출");
		totalPriceLb.setFont(new Font("",Font.PLAIN,12));



		totalPrice = new JLabel("              0원");
		totalPrice.setAlignmentX(RIGHT_ALIGNMENT);
		totalPrice.setFont(new Font("",Font.PLAIN,12));

		panTotalPrice.add(totalPriceLb);
		panTotalPrice.add(totalPrice);

		panCalendar.add(panTotalPrice);
		 */

		panCalendar.add(leftCalendar);
		panCalendar.add(lb);
		panCalendar.add(rightCalendar);
		panCalendar.add(panInquire);


	}

	// 현재 판매중인 메뉴
	public void menuList() {
		menuList = new JPanel();
		menuList.setLayout(new GridLayout(1,1));
		try {

			// 동적 테이블 선언
			MutableTable tableMutable = new MutableTable(Items.getList());
			// 동적 테이블 선언 및 람타식 형태의 이벤트 처리기 등록
			tableMutable.addListSelectionListener((e) -> {
				DefaultListSelectionModel dlsm = (DefaultListSelectionModel) e.getSource();

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
			menuList.add(tableMutable.getScrollTable());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 주문내역 확인
	public void totalOrderList(String _startDate, String _endDate) {
		panOList = new JPanel();
		panOList.setLayout(new GridLayout(1,1));

		try {

			// 동적 테이블 선언
			MutableTable tableMutable = new MutableTable(Managers.getOrderAtPeriod(_startDate, _endDate));

			// 동적 테이블 선언 및 람타식 형태의 이벤트 처리기 등록
			tableMutable.addListSelectionListener((e) -> {
				DefaultListSelectionModel dlsm = (DefaultListSelectionModel) e.getSource();

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
			panOList.add(tableMutable.getScrollTable());
			panTotalOrderConfirm.add(panOList, BorderLayout.CENTER);
			panTotalOrderConfirm.revalidate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 매출 확인
	public void revenueConfirmList(String _startDate, String _endDate) {
		panRevenue = new JPanel();
		panRevenue.setLayout(new GridLayout(1,1));
		try {

			// 동적 테이블 선언
			MutableTable tableMutable = new MutableTable(Managers.getStoreTotalPricePerDayAtPeriod(_startDate, _endDate));

			// 동적 테이블 선언 및 람타식 형태의 이벤트 처리기 등록
			tableMutable.addListSelectionListener((e) -> {
				DefaultListSelectionModel dlsm = (DefaultListSelectionModel) e.getSource();

				// TODO Auto-generated method stub
				if(!e.getValueIsAdjusting()) {

					// 선택된 행 전체 출력
					for (int i = 0; i < tableMutable.getHeader().length; i++) {
						System.out.print(((i == 0) ? "" : ", ") + tableMutable.getHeader()[i] + ": " + tableMutable.getContents()[dlsm.getAnchorSelectionIndex()][i]);
					}
					System.out.println();
				}
			});
			panRevenue.add(tableMutable.getScrollTable());
			revenueConfirm.add(panRevenue, BorderLayout.CENTER);
			revenueConfirm.revalidate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void howManySellList(String _startDate, String _endDate) {
		panSellTotal = new JPanel();
		panSellTotal.setLayout(new GridLayout(1,1));
		try {

			// 동적 테이블 선언
			MutableTable tableMutable = new MutableTable(Items.getOrderedQuantityListAtPeriod(_startDate, _endDate));

			// 동적 테이블 선언 및 람타식 형태의 이벤트 처리기 등록
			tableMutable.addListSelectionListener((e) -> {
				DefaultListSelectionModel dlsm = (DefaultListSelectionModel) e.getSource();

				// TODO Auto-generated method stub
				if(!e.getValueIsAdjusting()) {

					// 선택된 행 전체 출력
					for (int i = 0; i < tableMutable.getHeader().length; i++) {
						System.out.print(((i == 0) ? "" : ", ") + tableMutable.getHeader()[i] + ": " + tableMutable.getContents()[dlsm.getAnchorSelectionIndex()][i]);
					}
					System.out.println();
				}
			});
			panSellTotal.add(tableMutable.getScrollTable());
			howManySell.add(panSellTotal, BorderLayout.CENTER);
			howManySell.revalidate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void numToOrderList(String _orderNum) {
		numToOrderPan = new JPanel();
		numToOrderPan.setLayout(new GridLayout(1,1));
		try {

			// 동적 테이블 선언
			MutableTable tableMutable = new MutableTable(Managers.getOrderDetailAtNumber(_orderNum));

			// 동적 테이블 선언 및 람타식 형태의 이벤트 처리기 등록
			tableMutable.addListSelectionListener((e) -> {
				DefaultListSelectionModel dlsm = (DefaultListSelectionModel) e.getSource();

				// TODO Auto-generated method stub
				if(!e.getValueIsAdjusting()) {

					// 선택된 행 전체 출력
					for (int i = 0; i < tableMutable.getHeader().length; i++) {
						System.out.print(((i == 0) ? "" : ", ") + tableMutable.getHeader()[i] + ": " + tableMutable.getContents()[dlsm.getAnchorSelectionIndex()][i]);
					}
					System.out.println();
				}
			});
			numToOrderPan.add(tableMutable.getScrollTable());
			numToOrder.add(numToOrderPan, BorderLayout.CENTER);
			numToOrder.revalidate();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void changeDate() {
		try {
			startDate = textStartDay.getText(0, 4) + "-" + textStartDay.getText(6, 2) + "-" + textStartDay.getText(10, 2);
			endDate = textEndDay.getText(0, 4) + "-" + textEndDay.getText(6, 2) + "-" + textEndDay.getText(10, 2);
		} catch (BadLocationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if(textStartDay.getText().compareTo(textEndDay.getText()) > 0) {
			JOptionPane.showMessageDialog(null, "정신차려!!!!!!!");
			return;
		}else {


			SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd");
			String _endDay1 = endDate;	// _endDay1 하루를 더할 날짜
			_endDay = "";	// _endDay 하루 더한 날짜
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(format.parse(_endDay1));

			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			c.add(Calendar.DATE, 1);  // 하루를 더해준다.
			_endDay = format.format(c.getTime());  // _endDay 하루를 더한 날짜

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();

		//캘린더버튼을 눌렀을때 동작
		if(obj == calBtnL || obj == calBtnR || obj == calBtnl || obj == calBtnr || obj == calBtnlL || obj == calBtnrR) {

			if(obj == calBtnL || obj == calBtnl || obj == calBtnlL ) {
				check = false;
			}else if(obj == calBtnR || obj == calBtnr ||obj == calBtnrR ) {
				check = true;
			}

			//캘린더가 마우스위치에 뜨도록 설정
			cf = new CalendarFrame(MouseInfo.getPointerInfo().getLocation());
			//캘린더에서 날짜를 선택하면 라벨에 날짜입력
			cf.getTable().addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e1) {

					String day1 = null;
					String month1 = null;

					int year = cf.getYear();

					String month = cf.getMonth();

					try {
						int row = cf.getTable().getSelectedRow();
						int col = cf.getTable().getSelectedColumn();
						int day = (int)cf.getTable().getValueAt(row, col);
						day1 = "" + day;
						if(day1.length() < 2) {
							day1 = "0" + day;
						}

						month1 = "" + month;
						if(month1.length()<3) {
							month1 = "0" + month;
						}

					}catch (Exception e11) {
						// 오류 발생시 확인창을 띄우고, 프로그램 종료
						return;
						/*if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -3)", "JavaBean - 오류 안내", JOptionPane.ERROR_MESSAGE,JOptionPane.OK_OPTION) == JOptionPane.OK_OPTION) {
							System.exit(-3);
						};*/
					}


					String date = year + "년 " + month1 + " " + day1 + "일";
					if(check == false){
						textStartDay.setText(date);
					}else if(check == true) {
						textEndDay.setText(date);
					}
					try {
						cf.dispose();
					}catch (Exception e11) {
						// 오류 발생시 확인창을 띄우고, 프로그램 종료
						if (JOptionPane.showConfirmDialog(null, "예기치 않은 오류가 발생하여 프로그램을 종료합니다.\n(ErrorCode: -3)", "JavaBean - 오류 안내", JOptionPane.ERROR_MESSAGE,JOptionPane.OK_OPTION) == JOptionPane.OK_OPTION) {
							System.exit(-3);
						};
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
				}
			});

		}
		//각종 확인버튼들
		if(obj == revenueInquire) {

			changeDate();
			revenueConfirmList(startDate, _endDay);
			try {
				totalPrice.setText(Integer.toString(Managers.getStoreTotalPriceAtPeriod(startDate, _endDay))+"원");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if(obj == orderInquire) {

			changeDate();
			try {
				totalOrderList(startDate, _endDay);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if(obj == sellInquire) {
			
			changeDate();
			try {
				howManySellList(startDate, _endDay);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if(obj == inquireBtn) {
			
			System.out.println("야~~~~~~호!!!!!!");
			try {
				numToOrderList(orderNum.getText());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}