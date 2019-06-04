package frame.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.json.simple.JSONObject;

import frame.component.JBScrollBar;
import frame.component.JBTableCellRenderer;
import jdbc.oracle.customer.Customers;
import model.OrderDefaultTableModel;

@SuppressWarnings("serial")
public class OrderPanel extends JPanel implements ActionListener, TableModelListener, AdjustmentListener {
	
	private static OrderDefaultTableModel mTable;
	private static JScrollPane spOrder;
	private static boolean autoScroll;
	
	private JButton bReset, bDelete, bOrder;
	private JTable tOrder;
	private JLabel lTotal;
	
	private int width;
	private int height;
	
	
	// 기본 생성자 등록
	public OrderPanel() {}
	
	/**
	 * 주문관련 테이블 및 버튼 객체 생성자 선언
	 * @param _width
	 * @param _height
	 */
	public OrderPanel(int _width, int _height) {
		
		width = _width;
		height = _height;
		
		// 기본 레이아웃 설정
		setLayout(new BorderLayout());
		
		// 패널 등록
		setNorthPanel();
		setSouthPanel();
		
		// 디자인 설정
		setOpaque(true);
		setBackground(Color.DARK_GRAY);
	}
	
	/**
	 * 주문 테이블 추가
	 */
	public void setNorthPanel() {
		
		// North Panel 선언 및 사이즈 조절
		JPanel pNorth = new JPanel(new BorderLayout());
		pNorth.setPreferredSize(new Dimension(width, height / 33 * 8));
		
		// Center Panel 선언
		JPanel pCenter = new JPanel(null);
		
		// 테이블 선언
		String[] hOrder = {
			"품명", "핫 / 아이스", "사이즈", "단가", "옵션 단가", "수량", "합계"	
		};
		String[][] cOrder = {};
		
		mTable = new OrderDefaultTableModel(cOrder, hOrder);
		tOrder = new JTable(mTable);
		
		// 테이블 헤더 순서 편집 불가 설정
		tOrder.getTableHeader().setReorderingAllowed(false);
				
		// 테이블 셀 정렬 설정
		boolean[] cAlign = { true, true, true, false, false, true, false};
		for (int _h = 0; _h < hOrder.length; _h++) {
			// 테이블 셀 설정
			tOrder.getColumn(hOrder[_h]).setCellRenderer(
					new JBTableCellRenderer(
							((cAlign[_h]) ? DefaultTableCellRenderer.CENTER :  DefaultTableCellRenderer.RIGHT)
					)
			);
		}
		
		// 테이블 헤더 디자인 설정
		tOrder.getTableHeader().setDefaultRenderer(new JBTableCellRenderer());
		
		// 컬럼 선택 불가 설정
		tOrder.setColumnSelectionAllowed(false);
		
		// 테이블 컬럼 디자인 설정
		tOrder.setOpaque(true);
		tOrder.setBackground(Color.DARK_GRAY);
		tOrder.setForeground(Color.WHITE);
		tOrder.setShowGrid(false);
		
		// 테이블 컬럼 사이즈 자동 조절
		int[] sColumn = { width / 3, width / 18 * 3, width / 18  * 3, width / 9, width / 18 * 3, width / 9, width / 9 };
		TableColumnModel mColumn = tOrder.getColumnModel(); 
		for (int c = 0; c < tOrder.getColumnCount(); c++) {
			mColumn.getColumn(c).setPreferredWidth(sColumn[c]); 
		} 
		
		
		/**
		 * 테이블 컬럼 이벤트 추가
		 */
		mTable.addTableModelListener(this);
		
		// 스크롤바 숨김처리
		spOrder = new JScrollPane(
			tOrder, 
			JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		
		spOrder.setPreferredSize(new Dimension(width, height / 33 * 8));
		
		// 스크롤 디자인 설정
		spOrder.setOpaque(true);
		spOrder.setBackground(Color.WHITE);
		spOrder.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		
		// 스크롤 숨김 상태에서 스크롤 가능하도록 처리
		spOrder.setVerticalScrollBar(new JBScrollBar(JScrollBar.VERTICAL));
		
		// 스크롤 배경 설정
		spOrder.getViewport().setBackground(Color.DARK_GRAY);
		
		
		/*
		 * 테이블 컬럼 추가시 자동 스크롤을 위한 리스너 등록
		 */
		spOrder.getVerticalScrollBar().addAdjustmentListener(this);

		
		// 총합 표기를 위한 라벨 선언
		lTotal = new JLabel("주문 합계:    0 원");
		lTotal.setHorizontalAlignment(JLabel.RIGHT);
		
		lTotal.setBounds(width - 160, 2, 140, 15);
		lTotal.setBackground(Color.DARK_GRAY);
		lTotal.setForeground(Color.WHITE);
		
		
		// 패널에 테이블 및 레이블 추가
		pNorth.add(spOrder, BorderLayout.NORTH);
		pCenter.add(lTotal);
		
		// ItemPanel에 등록하기 전, 디자인 설정
		pNorth.setOpaque(true);
		pNorth.setBackground(Color.DARK_GRAY);
		
		pCenter.setOpaque(true);
		pCenter.setBackground(Color.DARK_GRAY);
		
		// ItemPanel에 pCenter 패널 등록
		add(pNorth, BorderLayout.NORTH);
		add(pCenter, BorderLayout.CENTER);
	}
	
	/**
	 * 삭제 및 주문 버튼 패널 추가
	 */
	public void setSouthPanel() {
		
		JPanel pSouth = new JPanel(new BorderLayout());
		
		Image iReset = new ImageIcon("img/etc/휴지통.png").getImage();

		// 목록 초기화 버튼
		bReset = new JButton(new ImageIcon(iReset.getScaledInstance(24, 24, Image.SCALE_SMOOTH)));
		bReset.setPreferredSize(new Dimension(width / 32 * 3, 45));
						
		// 디자인 설정
		bReset.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		bReset.setFocusable(false);
		bReset.setBackground(Color.WHITE);
		bReset.setForeground(Color.DARK_GRAY);
		
		
		/*
		 * 초기화 버튼 이벤트 등록 
		 */
		bReset.addActionListener(this);
		
		
		// 삭제 버튼
		bDelete = new JButton("삭제");
		bDelete.setPreferredSize(new Dimension(width / 32 * 13, 45));
				
		// 디자인 설정
		bDelete.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		bDelete.setFocusable(false);
		bDelete.setBackground(Color.WHITE);
		bDelete.setForeground(Color.DARK_GRAY);
		
		
		/*
		 *  삭제 버튼 이벤트 등록
		 */
		bDelete.addActionListener(this);
		
		
		// 주문 버튼
		bOrder = new JButton("주문");
		bOrder.setPreferredSize(new Dimension(width / 2, 45));
						
		// 디자인 설정
		bOrder.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		bOrder.setFocusable(false);
		bOrder.setBackground(Color.WHITE);
		bOrder.setForeground(Color.DARK_GRAY);
		
		
		/*
		 * 주문 버튼 이벤ㅌ ㅡ등록
		 */
		bOrder.addActionListener(this);
		
		// 패널에 버튼 등록
		pSouth.add(bReset, BorderLayout.WEST);
		pSouth.add(bDelete, BorderLayout.CENTER);
		pSouth.add(bOrder, BorderLayout.EAST);
				
		// ItemPanel에 등록하기 전, 디자인 설정
		pSouth.setOpaque(true);
		pSouth.setBackground(Color.DARK_GRAY);
				
		// ItemPanel에 pCenter 패널 등록
		add(pSouth, BorderLayout.SOUTH);
	}
	
	/**
	 * 테이블에 행 추가
	 * @param _item
	 */
	public void setRow(String[] _item) {
		mTable.addRow(_item);
	}
	
	/**
	 * 자동 스크롤 기능 추가
	 * @param _autoScroll
	 */
	public void setAutoScroll(boolean _autoScroll) {
		autoScroll = _autoScroll;
	}

	
	/**
	 * bReset & bDelete & bOrder 이벤트 처리
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		// bReset 이벤트 처리
		if (e.getSource().equals(bReset)) {

			// 초기화전 경고 메시지를 띄운다.
			if (JOptionPane.showConfirmDialog(null, 
					"장바구니 목록을 모두 삭제하시겠습니까?", 
					"JavaBean - 경고",
					JOptionPane.WARNING_MESSAGE,
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				
				// --로 할 경우 동작
				for (int _r = mTable.getRowCount() - 1; _r > -1; _r--)
					mTable.removeRow(_r);
				
			}
		}
		
		// bDelete 이벤트 처리
		if (e.getSource().equals(bDelete)) {
			
			// 삭제할 목록이 선택되지 않은 경우 경고 메시지를 띄운다.
			if (tOrder.getSelectedRow() == -1) {
				JOptionPane.showConfirmDialog(
						null,
						"삭제할 장바구니 목록을 선택해주세요.", 
						"JavaBean - 경고", 
						JOptionPane.DEFAULT_OPTION, 
						JOptionPane.WARNING_MESSAGE
				);
				
				return;
			}
			
			// 선택한 행 삭제
			mTable.removeRow(tOrder.getSelectedRow());
		}
		
		// bOrder 이벤트 처리
		if (e.getSource().equals(bOrder)) {
			
			// 주문 시점의 총 주문 목록 행 갯수
			int _rowCount = mTable.getRowCount();
			
			if (_rowCount == 0) {
				JOptionPane.showConfirmDialog(null, 
						"장바구니에 물품을 추가한 후 다시 시도해주세요.", 
						"JavaBean - 경고",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			// 주문을 위한 필요 변수 선언
			JSONObject _list = new JSONObject();
			
			// 주문을 저장하기 위한 키 - 값 정보 입력
			_list.put("name", new Vector<String>());
			_list.put("detail", new Vector<String>());
			_list.put("quantity", new Vector<String>());
			
			// 주문을 위한 변수에 각 열 정보들을 등록
			for (int _r = 0; _r < _rowCount; _r++) {
				((Vector<String>) _list.get("name")).add(mTable.getValueAt(_r, 0).toString());
				((Vector<String>) _list.get("detail")).add(mTable.getValueAt(_r, 1) + " & " + mTable.getValueAt(_r, 2));
				((Vector<String>) _list.get("quantity")).add(mTable.getValueAt(_r, 5).toString());
			}
			
			// 초기화전 경고 메시지를 띄운다.
			if (JOptionPane.showConfirmDialog(null,
					"총 주문 금액은 " + lTotal.getText().split("    ")[1] + " 입니다.\n" +
					"주문하시겠습니까?",
					"JavaBean - 주문 확인",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					
				// 오류 처리
				try {
					
					// 주문을 접수하고 주문 번호를 받아온다.
					String nOrder = Customers.setOrders(_list);
					
					// --로 할 경우 동작
					for (int _r = mTable.getRowCount() - 1; _r > -1; _r--)
						mTable.removeRow(_r);
					
					// 주문번호 안내
					JOptionPane.showConfirmDialog(null, 
							"주문이 성공적으로 완료되었습니다.\n\n" +
							"※주문번호: " + nOrder, 
							"JavaBean - 주문 완료",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					
				// 예외 처리
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					
					// 오류 발생시 확인창을 띄우고, 프로그램 종료
					JOptionPane.showConfirmDialog(
							null,
							"예기치 않은 오류로 주문에 실패했습니다.\n잠시후 다시 시도해주세요.\n\n(ErrorCode: -4)", 
							"JavaBean - 주문 실패", 
							JOptionPane.DEFAULT_OPTION, 
							JOptionPane.ERROR_MESSAGE
					);
				}
			}
		}
	}

	/**
	 * 테이블에 행이 추가되거나 제거되었을 시 주문합계 정보 변경
	 */
	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		// 총 금액 계산
		int total = 0;
		for (int r = 0; r < mTable.getRowCount(); r++)
			total += Integer.parseInt(mTable.getValueAt(r, 6).toString());
		
		// 변경된 총 금액 표기
		lTotal.setText("주문 합계:    " + total + " 원");
	}

	/**
	 * 테이블에 행이 추가되었을 시 스크롤바를 맨 아래로 이동
	 * @param e
	 */
	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		// TODO Auto-generated method stub
		
		// 테이블에 아이템이 추가되었을 때에만 맨 아래로 이동
		if (autoScroll) {
						
			((JScrollBar) e.getSource()).setValue(((JScrollBar) e.getSource()).getMaximum());
			autoScroll = false;
		}
	}
}