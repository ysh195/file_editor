package file_editor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; // 테이블 수정에 관련된 패키지

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Make_a_text {  // 데이터 테이블 형식의 경우 저장 및 출력 방식이 복잡하여 다른 한글/워드 파일과 호환이 되지 않습니다.

	public Make_a_text(String[] input) { // 생성 시 편집할 파일 데이터를 입력받습니다.
		
		Dimension dim = new Dimension(800,400);
		
		JFrame frame = new JFrame(input[1]);
		frame.setLocation(200, 400);
		frame.setPreferredSize(dim);
		String[] inputArrStr = input;
		
		JTextArea textArea = new JTextArea();
		JScrollPane scrollpane = new JScrollPane(textArea);
		textArea.setText(inputArrStr[2]);
		
		JPanel panelA = new JPanel();
		
		panelA.setLayout(new BoxLayout(panelA,BoxLayout.X_AXIS)); //
		
		JTextField textField = new JTextField(8);
		
		JTextField sort_textField = new JTextField(inputArrStr[0],3);
		JTextField title_textField = new JTextField(inputArrStr[1],3);
		
		String[] comboText = {"한글","워드","엑셀","쿼리"};
		JComboBox sortCombo = new JComboBox(comboText);
		sortCombo.setSelectedItem(inputArrStr[0]);
		
		JLabel text_sortName = new JLabel(" 파일 유형 :   ");
		JLabel text_fileName = new JLabel("    파일 이름 :   ");
		
		JButton saveBtn = new JButton("저장");
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] result = new String[3]; // 저장소 메서드에 전달해줄 파일 형식
				result[0] = String.valueOf(sortCombo.getSelectedItem());
				result[1] = title_textField.getText();
				result[2] = textArea.getText();
				Storage.getInstance().save_edited(result);
				System.out.println("편집 내용 저장 완료");
				
			}
			
		});

		panelA.add(text_sortName);
		panelA.add(sortCombo);
		panelA.add(text_fileName);
		panelA.add(title_textField);
		panelA.add(saveBtn);
			
		frame.add(scrollpane,BorderLayout.CENTER);		
		
		
		frame.add(panelA,BorderLayout.NORTH);
		
		frame.pack();
		frame.setVisible(true);

	}

}
