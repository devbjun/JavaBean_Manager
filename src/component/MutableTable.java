package component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.json.simple.JSONObject;

@SuppressWarnings("serial")
public class MutableTable extends JTable {
	
	private DefaultTableCellRenderer celAlignCenter;
	private static DefaultTableCellRenderer celAlignRight;
	private MutableDefaultTableModel model;
	private JScrollPane spTable;
	
	private static JTable table;
	private String[] header;
	private String[][] contents;
	
	/**
	 * 릴레이션의 내포를 기반으로 테이블을 만드는 클래스
	 * @param Vector<JSONObject> _intension
	 * @param ListSelectionListener _listSelectionListener
	 * _intension 테이블로 표시할 릴레이션의 내포 정보
	 * _listSelectionListener 테이블의 행이 선택되었을 때 처리할 이벤트 처리 담당 클래스
	 */
	public MutableTable(Vector<JSONObject> _intension) throws Exception {
		
		
		setCelAlignRight(new DefaultTableCellRenderer());
		
		getCelAlignRight().setPreferredSize(new Dimension(getCelAlignRight().getSize().width, 50));
		getCelAlignRight().setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
		
		// 테이블 컬럼 가운데 정렬을 위한 변수 선언
		celAlignCenter = new DefaultTableCellRenderer();
		
		celAlignCenter.setPreferredSize(new Dimension(celAlignCenter.getSize().width, 50));
		celAlignCenter.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		
		header = new String[_intension.get(0).keySet().size() - 1];
		
				
		// 외연을 String[]로 변환
		for (int i = 0; i < header.length; i++) {
			header[i] = ((String[]) _intension.get(0).get("order"))[i];
		}

		// 내포를 String[][]로 변환
		contents = new String[_intension.size()][];
				
		if (!_intension.isEmpty()) {
			for (JSONObject _json : _intension) {
				String[] _tuple = new String[header.length];
								
				for (int i = 0; i < _tuple.length; i++) {
					_tuple[i] = (_json.get(header[i]) == null) ? null : _json.get(header[i]).toString();
				}
								
			contents[_intension.indexOf(_json)] = _tuple;
			}
		}
				
		// 모델 설정 및 모델이 적용된 테이블 생성
		model = new MutableDefaultTableModel(contents, header);
				
		// model이 적용된 테이블 생성
		setTable(new JTable(model));
		
		
	    
	    
		// 테이블 헤더 순서 편집 불가 설정
		getTable().getTableHeader().setReorderingAllowed(false);
		
		// 테이블 헤더 및 셀 중앙 정렬 설정
		for (String _h : header) {
			getTable().getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
				
				@Override 
			    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { 
			     
					JComponent component = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
					((JLabel) component).setHorizontalAlignment(JLabel.CENTER);
					
					// 헤더 디자인 설정
					component.setPreferredSize(new Dimension(component.getSize().width, 30));
					component.setBorder(BorderFactory.createEmptyBorder());
					
					// 헤더 배경 색상 설정
					component.setBackground(Color.WHITE);
					
					// 헤더 폰트 및 폰트 색상 설정
					component.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
					component.setForeground(Color.DARK_GRAY);
					
					return component; 
			     
			    }
			});
			getTable().getColumn(_h).setCellRenderer(celAlignCenter);
		}
		 		    
		
		// 테이블 열 크기 자동 조정
		//resizeColumnWidth(table);
		
		
		
		// 테이블 디자인 설정
		getTable().setOpaque(true);
		getTable().setBackground(Color.DARK_GRAY);
		getTable().setForeground(Color.WHITE);
		getTable().setShowGrid(false);
		 			
		// 스크롤이 가능하도록 처리
		spTable = new JScrollPane(
				getTable(), 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		
		// 스크롤 디자인 설정
		spTable.setOpaque(true);
		spTable.setBackground(Color.WHITE);
		//spTable.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		
		// 스크롤 배경 설정
		spTable.getViewport().setBackground(Color.DARK_GRAY);
	}
	
	/**
	 * 이벤트 처리기를 등록하기 위한 함수
	 * @param _listSelectionListener
	 */
	public void addListSelectionListener(ListSelectionListener _listSelectionListener) {
		getTable().getSelectionModel().addListSelectionListener(_listSelectionListener);
	}
	
	public JScrollPane getScrollTable() {
		return spTable;
	}
	
	public String[] getHeader() {
		return header;
	}
	
	public String[][] getContents() {
		return contents;
	}
	
	/**
	 * Column 내용이 모두 보여지도록 가로 사이즈를 자동 조절해주는 함수
	 * @param table
	 */
	private void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel(); 
		for (int column = 0; column < table.getColumnCount(); column++) {
			
			int width = 50;
			for (int row = 0; row < table.getRowCount(); row++) { 
				TableCellRenderer renderer = table.getCellRenderer(row, column); 
				Component comp = table.prepareRenderer(renderer, row, column); 
				width = Math.max(comp.getPreferredSize().width + 1 , width); 
			}
			
			columnModel.getColumn(column).setPreferredWidth(width); 
		} 
	}

	public static JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public static DefaultTableCellRenderer getCelAlignRight() {
		return celAlignRight;
	}

	public void setCelAlignRight(DefaultTableCellRenderer celAlignRight) {
		this.celAlignRight = celAlignRight;
	}
}
