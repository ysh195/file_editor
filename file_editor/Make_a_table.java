package file_editor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; // 테이블 수정에 관련된 패키지

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Make_a_table { // 데이터 테이블 형식의 경우 저장 및 출력 방식이 복잡하여 다른 한글/워드 파일과 호환이 되지 않습니다.

	public Make_a_table(String[] input) { // 생성 시 편집할 파일 데이터를 입력받습니다.
		
		Dimension dim = new Dimension(800,400);
		
		JFrame frame = new JFrame(input[1]);
		frame.setLocation(200, 400);
		frame.setPreferredSize(dim);
		String[] inputArrStr = input;
		
		// 테이블 형식을 위해 파일 데이터를 분할하는 과정입니다. ^는 줄 나눔, |는 칸 나눔을 의미합니다.
		// 한 줄로 저장된 문자열 중에서 가장 앞에 있는 | 또는 ^를 찾아내고, 그 전에 있었던 데이터는 테이블에 입력한 뒤 칸 또는 줄 나눔을 실행합니다.
		// 그렇게 테이블 내 입력 및 줄/칸 나눔이 실행된 부분은 문자열에서 제거합니다.
		// 더 이상 문자열 내에 ^ 또는 |이 존재하지 않을 때까지 이 과정을 반복합니다.
		
		// 여기서 ^ 또는 | 중 뭐가 더 앞에 있는지에 대한 판단이 필요합니다.
		// indexOf 로 문자열 내에서 특정 문자를 찾을 경우, 해당 문자가 존재하지 않는다면 -1를 리턴합니다.
		// 마지막 줄에 도달할 경우 더 이상 ^가 존재하지 않기 때문에 ^의 위치는 계속 -1로 고정되는데, 그 경우에는 ^의 위치값이 |의 위치값보다 항상 작을 수밖에 없습니다.
		// 이러한 과정과 그 문제 해결로 인해 코드가 길어졌습니다.
		
		int y_tellingSign_count = (inputArrStr[2].length() - inputArrStr[2].replace("^", "").length())+1; // 찾는 기호를 모두 ""로 바꿔버리고 바꾸기 전의 문자열 길이와 바꾼 후의 문자열 길이를 비교하면 몇 개인지 알 수 있음

		int x_tellingSign_count = 0;
		String temp1 = inputArrStr[2];
		while(temp1.contains("^")) { // 이렇게 하면 ^가 없는 마지막줄은 빠짐
			String temp2 = temp1.substring(0, temp1.indexOf("^"));
			x_tellingSign_count = Math.max(x_tellingSign_count, temp2.length()-temp2.replace("|", "").length());
			temp1 = temp1.substring(temp1.indexOf("^")+1, temp1.length());
		}
		
		String temp2 = temp1.substring(0, temp1.length()); // 마지막 줄에 대한 걸 한 번 더 해서 보완. 
		x_tellingSign_count = Math.max(x_tellingSign_count, temp2.length()-temp2.replace("|", "").length())+1;
		
		String[][] contents = new String[y_tellingSign_count][x_tellingSign_count];		
		
		seperating_process : for(int i=0; i<y_tellingSign_count; i++) { // 이건 구조가 좀 복잡하겠네. 마지막에 ^가 없으면 -1되고 보정해도 0이니까 계산이 안됨
			for(int j=0; j<x_tellingSign_count; j++) {
				
				int sepX = inputArrStr[2].indexOf("|");
				int sepY = inputArrStr[2].indexOf("^");
				
				if((sepX == -1) && (sepY == -1)) {
					contents[i][j] = inputArrStr[2].substring(0, inputArrStr[2].length());
					break seperating_process;
				}
				else {
					int tempX = Math.max(sepX, 0);
					int tempY = Math.max(sepY, 0);
					
					if(((tempX <= 0) || (tempY <= tempX))&&(tempY >= 1)) { // 조건을 더 복잡하게 설정함으로써 문제점을 보완
						contents[i][j] = inputArrStr[2].substring(0, tempY);
						inputArrStr[2] = inputArrStr[2].substring(tempY+1, inputArrStr[2].length());
					}
					else {
						contents[i][j] = inputArrStr[2].substring(0, tempX);
						inputArrStr[2] = inputArrStr[2].substring(tempX+1, inputArrStr[2].length());
					}
				}
				
			}
		} // 테이블 데이터에 대한 처리가 끝났습니다.
		
		String[] header = new String[x_tellingSign_count];
		
		DefaultTableModel model = new DefaultTableModel(contents,header); // 테이블 수정과 관련된 것
		
		JTable table = new JTable(model); // 테이블 생성.
		// 내용을 모델에 넘기고, 모델을 테이블에 연결.
		table.setTableHeader(null);
		JScrollPane scrollpane = new JScrollPane(table);
		
		JPanel panel = new JPanel();
		JPanel panelA = new JPanel();
		
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS)); // 이건 진짜 뭔지 모르겠음
		panelA.setLayout(new BoxLayout(panelA,BoxLayout.X_AXIS)); //
		
		JTextField textField = new JTextField(8);
		
		JTextField sort_textField = new JTextField(inputArrStr[0],3);
		JTextField title_textField = new JTextField(inputArrStr[1],3);
		
		String[] comboText = {"한글","워드","엑셀","쿼리"};
		JComboBox sortCombo = new JComboBox(comboText);
		sortCombo.setSelectedItem(inputArrStr[0]);
		
		JLabel text_sortName = new JLabel(" 파일 유형 :   ");
		JLabel text_fileName = new JLabel("    파일 이름 :   ");
		
		
		panel.add(textField);
				
		JButton addYBtn = new JButton("Y 추가");
		// 테이블 데이터의 Y축을 추가합니다.
		// Y축 추가와 동시에 데이터를 입력하려면 각 데이터 사이에 "|"를 입력해야 합니다. 그렇지 않으면 한 칸에 모두 입력됩니다.
		addYBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int ColumnCount = table.getColumnCount(); // 가로 몇 칸인지 세고
				String str = textField.getText(); // 텍스트 내용 가져와서

				String[] insertStr = new String[ColumnCount]; // x축의 갯수만큼의 String 배열을 준비합니다.
				
				if(textField != null) { // 초반의 데이터 처리와 유사합니다.
					int count=0;
					if(str.contains("|")==false) { // "|"가 없다면 1칸만 입력하는 것이므로, [0]에 저장합니다.
						insertStr[count] = str; // 저장될 배열 순서를 의미하는 카운트입니다.
					}
					else { // "|" 는 각 데이터 사이에 들어가므로, "|"는 항상 입력될 데이터의 갯수보다 1개 적습니다.
						do{
							insertStr[count] = str.substring(0, str.indexOf("|")); // 문자열의 첫번째부터 "|" 중 가장 앞에 있는 것까지의 문자열을 저장합니다.
							str = str.substring(str.indexOf("|")+1, str.length()); // "|" 중 가장 앞에 있는 것의 뒤부터 끝까지 문자열을 덮어씌웁니다.
							count++; // 배열에 값이 저장되었으니 카운트를 올립니다.
						}while(str.contains("|") || (count >= ColumnCount - 1)); // 더 이상 "|" 존재하지 않거나 카운트가 (x축의 값 - 1) 에 도달할 때까지 이를 반복합니다.
						insertStr[count] = str; // 마지막 값을 저장합니다. 마지막 값이 들어갈 자리가 필요하기 때문에 do~while문을 (x축의 값 - 1)에서 멈췄습니다.
					}
				}
				else {
					return;
				}
				model.addRow(insertStr); // 해당 배열을 모델에 입력하면서 y축을 늘립니다.
				
				textField.setText(""); // 입력란을 비웁니다.
				
			}
			
		});
		
		JButton addXBtn = new JButton("X 추가"); // x축을 추가합니다.
		addXBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.addColumn(""); // 괄호 안에 헤더에 들어갈 텍스트를 넣어야 하는데, 헤더를 숨겼으니 그냥 ""로 처리
			}
			
		});
		
		JButton removeYBtn = new JButton("Y 삭제"); // 선택한 Y축 전체를 삭제합니다.
		removeYBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() == -1) {
					return;
				}
				else {
					model.removeRow(table.getSelectedRow());
				}
			}
			
		});
		
		JButton removeXBtn = new JButton("X 삭제"); // 맨 뒤부터 X축 전체를 삭제합니다.
		// Y와 달리, X 삭제는 그냥 없는 기능이라 어쩔 수 없겠다 싶었는데, 그냥 X축의 숫자를 줄이도록 설정하는 것으로 구현
		removeXBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ColumnCount = table.getColumnCount();
				if(ColumnCount <= 1) {
					return;
				}
				else {
					model.setColumnCount(ColumnCount-1);
				}
			}
			
		});
		
		JButton saveBtn = new JButton("저장");
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int size_x = model.getColumnCount();
				int size_y = model.getRowCount();
				
				String[] result = new String[3]; // 저장소 메서드에 전달해줄 파일 형식
				
				if(title_textField.getText().length() <= 0) { // 이유는 모르겠지만 입력된 게 없어도 ""이나 null이 아닌 걸로 인식함. 그래서 글자 길이로 판단
					title_textField.setText("NoName");
				}
				result[0] = String.valueOf(sortCombo.getSelectedItem()); // 현재 화면 내에서 입력된 것들을 전달해줄 파일 형식에 옮겨 담음
				result[1] = title_textField.getText();
				result[2] = ""; // 하지만 내용에 해당하는 [2]는 일단 보류. 그리고 앞으로 텍스트끼리 +해야 하니 ""로 입력.
				
				for(int i=0; i<size_y; i++) { // 테이블 데이터를 한 줄의 문자열로 전환하는 과정
					for(int j=0; j<size_x; j++) {
						result[2] += (String.valueOf(model.getValueAt(i, j)).length() <= 0) ? " " : model.getValueAt(i, j);
						if(j != size_x-1) {
							result[2] += "|";
						}
					}
					if(i != size_y-1) {
						result[2] += "^";
					}
				} // 전환 끝
				
				Storage.getInstance().save_edited(result); // 저장소에 저장
				System.out.println("편집 내용 저장 완료");
			}
			
		});
		panel.add(addXBtn);
		panel.add(addYBtn);
		panel.add(removeXBtn);
		panel.add(removeYBtn);		
		
		panelA.add(text_sortName);
		panelA.add(sortCombo);
		panelA.add(text_fileName);
		panelA.add(title_textField);
		panelA.add(saveBtn);
			
		frame.add(scrollpane,BorderLayout.CENTER);		
		
		
		frame.add(panelA,BorderLayout.NORTH);
		frame.add(panel,BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);

	}

}
