package file_editor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; // 테이블 수정에 관련된 패키지
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainFrame {
	
	public static void main(String[] args) { // 저장소와 사용자가 보는 파일 목록은 서로 연동되지 않고, 별개로 동작합니다.
	
		Dimension dim = new Dimension(1000,600);
		
		JFrame frame = new JFrame("문서 편집기");
		frame.setLocation(200, 400);
		frame.setPreferredSize(dim);
		
		String[][] inputArrStr = new String[Storage.getInstance().FileList.length][2]; // 보여줄 파일 목록을 준비하는 과정입니다.
		
		for(int i=0; i<Storage.getInstance().FileList.length; i++) { // 파일 목록에는 파일의 유형과 이름만 들어갑니다.
			for(int j=0; j<2; j++) {
				inputArrStr[i][j] = Storage.getInstance().FileList[i][j];
			}
		}	
		String[][] contents = inputArrStr;
		String[] header = {"파일 유형", "파일 이름"};
		DefaultTableModel model = new DefaultTableModel(contents,header); // 테이블 세팅과 관련된 것
		JTable table = new JTable(model); // 테이블 생성.
		JScrollPane scrollpane = new JScrollPane(table);
		// 내용을 모델에 넘기고, 모델을 테이블에 연결.
		
		JPanel panel = new JPanel();
		
		panel.setLayout(new BoxLayout(panel,BoxLayout.LINE_AXIS)); //
		
		JTextField textField = new JTextField(8);

		String[] comboText = {"한글","워드","엑셀","쿼리"}; // 파일 유형은 이 4가지로 제한됩니다.
		JComboBox sortCombo = new JComboBox(comboText); // 파일의 편집 및 선택은 콤보와 텍스트 필드에 의해 결정됩니다.
		
		table.getColumn("파일 유형").setPreferredWidth(100);
		table.getColumn("파일 이름").setPreferredWidth(700);
		
		JLabel text_sortName = new JLabel(" 파일 유형 :   ");
		JLabel text_fileName = new JLabel("    파일 이름 :   ");
		
		table.addMouseListener(new MouseListener() { // 마우스로 항목을 클릭하면 해당 항목의 유형과 이름이 각각 콤보목록과 텍스트필드에 출력됩니다.

			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow(); // 선택된 y축 값을 가져옵니다.
				sortCombo.setSelectedItem(table.getValueAt(row, 0)); // 해당 y축에 존재하는 값들을 콤보목록과 텍스트필드에 출력합니다.
				textField.setText((String) table.getValueAt(row, 1));
			}

			@Override
			public void mousePressed(MouseEvent e) { // 그 외의 기능은 불필요하여 구현하지 않았습니다.
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
			
		});
		
		JButton add_create_new_Btn = new JButton("새로 만들기"); // 파일을 새로 만드는 버튼입니다.
		add_create_new_Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((textField.getText() == null) | (sortCombo.getSelectedItem() == null)) { // 유형과 제목이 입력되었는지를 확인합니다.
					System.out.println("유형 또는 제목을 입력하세요.");
					return;
				}
				else {
					for(int i=0; i<inputArrStr.length; i++) { // 중복되는 항목이 존재하는지 확인합니다.
						if(inputArrStr[i][0].equals((String)sortCombo.getSelectedItem())&&inputArrStr[i][1].equals(textField.getText())) {
							System.out.println("유형과 이름이 중복되는 파일이 존재합니다.");
							return;
						}
						
					}
				} // 이러한 조건을 모두 통과한다면 다음으로 진행합니다.
				
				String[] new_save = new String[3]; // 데이터 저장소에 입력할 파일 양식입니다.
				new_save[0] = String.valueOf((String)sortCombo.getSelectedItem()) ; // 유형과 이름만 입력하고, 내용은 공백으로 둡니다.
				new_save[1] = textField.getText();
				new_save[2] = "";
				
				Storage.getInstance().save_in_storage(new_save); // 저장소에 저장합니다.
				System.out.println("새로운 파일이 생성되었습니다.");
				System.out.println("파일 유형 : " + new_save[0] + ", 파일 이름 : " + new_save[1]);
				
				String[] insertStr = new String[2]; // 이번에는 목록에 표현할 파일 양식입니다.
				insertStr[0] = new_save[0];
				insertStr[1] = new_save[1];
				
				model.addRow(insertStr); // y축을 추가하면서 해당 내용을 입력합니다.
			}
			
		});
		
		JButton add_del_Btn = new JButton("삭제");
		add_del_Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if((textField.getText() != null) && (sortCombo.getSelectedItem() != null)) { // 유형과 제목이 입력되었는지를 확인합니다.
					
					String del_sort = String.valueOf(sortCombo.getSelectedItem()); // 저장소에서 해당 데이터를 삭제하는 과정입니다.
					String del_name = textField.getText();					
					Storage.getInstance().delete_in_storage(del_sort,del_name);
					
					int ySzie = model.getRowCount(); // 사용자가 보는 파일 목록에서 해당 데이터를 삭제하는 과정입니다.
					for(int i=0; i<ySzie; i++) { // 저장소의 데이터는 저장소 내부의 메서드가 삭제하지만, 목록은 별개이기 때문에 별도의 절차가 필요합니다.
						if(model.getValueAt(i, 0).equals(sortCombo.getSelectedItem())&&model.getValueAt(i, 1).equals(textField.getText())) {
							model.removeRow(i); // 파일 목록 내에서 유형과 이름이 일치하는 항목을 찾았다면, 그 항목이 위치한 y축 전체를 삭제합니다.
							System.out.println("파일 목록에서 삭제되었습니다.");
							break;
						}
					}

				}
				else { // 유형과 제목이 입력되지 않았다면 절차를 종료합니다.
					System.out.println("삭제할 파일을 지정 또는 선택하세요.");
					return;
				}		
			}
			
		});
		
		JButton add_open_file_Btn = new JButton("편집");
		add_open_file_Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int location = -1; // 저장소 내에 편집할 항목의 y축 위치입니다.
				// 배열 내에서의 위치를 탐색하므로, -1은 존재할 수 없는 값입니다.
				// 따라서 탐색에 실패했다면 location에는 여전히 -1이 저장되어 있을 것이고, 이를 통해 탐색에 실패했음을 확인합니다. 
				
				if((textField.getText() == null) | (sortCombo.getSelectedItem() == null)) { // 유형과 제목이 입력되었는지를 확인합니다.
					System.out.println("유형 또는 제목을 입력하세요."); // 입력되지 않았다면 절차를 종료합니다.
					return;
				}
				
				for(int i=0; i<Storage.getInstance().FileList.length; i++) { // 해당 파일이 실제로 존재하는지 체크합니다.
					if(Storage.getInstance().FileList[i][0].equals(String.valueOf(sortCombo.getSelectedItem()))&&Storage.getInstance().FileList[i][1].equals(textField.getText())) {
						location = i; // 위치를 찾았다면 그 y축의 위치값을 location에 저장합니다.
						break;
					}						
				}
				
				if(location <= -1) { // 탐색에 실패했다면 절차를 종료합니다.
					System.out.println("해당 파일이 존재하지 않습니다.");
					return;
				}
				else { // 탐색에 성공했다면 절차를 진행합니다.
					System.out.println("파일 편집창을 활성화합니다.");
				}
				
				String sortType = String.valueOf(sortCombo.getSelectedItem()); // 유형에 따라 다른 편집 화면을 출력해야 하므로 이를 저장해둡니다.
				
				System.out.println("선택된 파일 유형[" + sortType + "]에 적합한 편집 환경을 구성합니다."); // 원래 구상으로는 여기서 FileGender로 뭔가 더 할 생각이었음
				// 출력되는 문구와 달리 아무런 의미가 없습니다.
				
				if(sortType.equals("엑셀")||sortType.equals("쿼리")) {
					Make_a_table openedFile = new Make_a_table(Storage.bring_file(location));

				}
				else {
					Make_a_text openedFile = new Make_a_text(Storage.bring_file(location));
				}		
			}
			
		});
				
		panel.add(text_sortName);
		panel.add(sortCombo);
		panel.add(text_fileName);
		panel.add(textField);
		panel.add(add_create_new_Btn);
		panel.add(add_del_Btn);
		panel.add(add_open_file_Btn);
			
		frame.add(scrollpane,BorderLayout.CENTER);
		
		frame.add(panel,BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);

	}


}
