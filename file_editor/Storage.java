package file_editor;

public class Storage { 
	
	private static Storage storage = new Storage(); // 단 하나의 저장소를 공유해야 하므로 싱글톤으로 구성했습니다.
	private Storage() {}
	public static Storage getInstance() {
		return storage;
	}
	
	public static String[][] FileList = { // 저장된 파일 데이터입니다.
			{"한글","23.Q1 실적 보고서","AAAAAA"},
			{"엑셀","고객명단","회원 아이디|회원 이름|전화번호|회원등급^13254|김철수|010-0000-0000|브론즈^18742|김영희|010-1234-5678|골드^39615|이민수|010-9126-7913|플래티넘"},
			{"워드","23.Q1 재무제표","619843154"},
			{"쿼리","매출액 계산","회원 아이디|회원 이름|전화번호|사용액|김철수|010-0000-0000|500000"}
			};
	// 2차원 배열이며, {"파일 유형", "파일 이름", "내용"} 순으로 저장합니다.

	public static void save_in_storage(String[] saveFile) {
		// 새로운 파일을 저장하는 메서드입니다. String[3] 형태의 파일 정보를 입력받아 배열에 저장합니다.
		
		int num = FileList.length;
		
		// 기존보다 1개 더 큰 배열을 생성하고, 생성된 배열에 기존의 내용을 옮겨 담은 후, 마지막에 입력 받은 새로운 파일 정보를 저장합니다. 
		String[][] newTemp = new String[num+1][3]; // 기존보다 1개 더 큰 배열을 생성합니다.
		for(int i=0; i<num; i++) { // 새로운 배열에 기존의 내용을 옮겨 담는 과정입니다.
			for(int j=0; j<3; j++) {
				newTemp[i][j] = FileList[i][j];
			}
		}
		
		newTemp[num][0] = saveFile[0]; // 마지막에 입력 받은 새로운 파일 정보를 저장합니다.
		newTemp[num][1] = saveFile[1];
		newTemp[num][2] = saveFile[2];
		
		FileList = newTemp; // 그리고 새로운 배열로 기존의 배열을 덮어 씌웁니다.
		
		System.out.println("저장소 내에 새로운 파일이 생성되었습니다.");

	}
	
	public static void delete_in_storage(String sort, String fileName) {
		// 기존의 파일 정보를 삭제하는 메서드입니다. 삭제할 파일의 "파일 유형"과 "파일 이름"을 입력 받아 해당 파일을 삭제합니다.
		// 기존보다 1개 더 작은 배열을 생성하고, 해당 파일을 제외한 나머지 파일 정보를 저장합니다. 
		
		if(FileList.length<=1) { // 데이터의 최소 숫자를 유지하기 위한 것입니다.
			System.out.println("저장된 파일의 숫자가 적습니다. 최소 1개 이상으로 유지해주세요.");
			return;
		}
		
		int location = -1;
		// 배열 내에서의 위치를 탐색하므로, -1은 존재할 수 없는 값입니다.
		// 따라서 탐색에 실패했다면 location에는 여전히 -1이 저장되어 있을 것이고, 이를 통해 탐색에 실패했음을 확인합니다.
		
		for(int i=0; i<FileList.length; i++) {
			if(FileList[i][1].equals(fileName)&&FileList[i][0].equals(sort)) {
				location = i;
			}
		}
		
		if(location == -1) { // 탐색에 실패했다면 절차를 종료합니다.
			System.out.println("해당 파일을 찾을 수 없습니다.");
			return;
		}
		
		String[][] temp = new String[FileList.length-1][3];
		// 탐색에 성공했다면 절차를 진행합니다.
		// 기존 파일보다 1개 더 작은 배열을 생성합니다.
		
		for(int i=0; i<location; i++) { // 삭제 대상 파일의 위치보다 앞에 있는 파일 정보들을 옮겨 담습니다.
			temp[i][0] = FileList[i][0];
			temp[i][1] = FileList[i][1];
			temp[i][2] = FileList[i][2];
		}
		
		for(int i=location+1; i<FileList.length; i++) { // 삭제 대상 파일의 위치보다 뒤에 있는 파일 정보들을 옮겨 담습니다.
			temp[i-1][0] = FileList[i][0];
			temp[i-1][1] = FileList[i][1];
			temp[i-1][2] = FileList[i][2];
		}
		
		FileList = temp; // 새로운 배열을 기존의 배열에 덮어씌웁니다.
		// 삭제 대상 파일을 제외하고 나머지 파일 정보만을 저장한 배열을 덮어씌움으로써 삭제 대상 파일을 저장소에서 배제하였습니다.
		System.out.println("저장소에서 삭제되었습니다.");
	}
	
	public static String[] bring_file(int location){  // 파일의 위치 정보를 입력받아 해당 위치에 있는 파일의 정보를 리턴합니다.
		
		String[] bringOut = new String[3];
	
		bringOut[0] = FileList[location][0];
		bringOut[1] = FileList[location][1];
		bringOut[2] = FileList[location][2];
		
		return bringOut;
	}
	
	public static void save_edited(String[] input) { // 파일 데이터 수정 시의 저장 메서드입니다.
		// String[3] 형식의 파일 데이터를 입력받아 파일 유형과 파일 이름이 일치하는 파일 데이터에 입력 받는 데이터를 덮어씌웁니다.
		
		if(input.length != 3) { // 저장을 위해서 String[3]의 형식은 불가피합니다. 형식에 맞지 않다면 저장하지 않고 절차를 종료합니다.
			System.out.println("저장하기에 적합하지 않은 크기입니다.");
			return;
		}
		
		int location = -1;
		// 배열 내에서의 위치를 탐색하므로, -1은 존재할 수 없는 값입니다.
		// 따라서 탐색에 실패했다면 location에는 여전히 -1이 저장되어 있을 것이고, 이를 통해 탐색에 실패했음을 확인합니다.

		for(int i=0; i<FileList.length; i++) {
			if(FileList[i][0].equals(input[0])&&FileList[i][1].equals(input[1])) {
				location = i; // 탐색에 성공했다면 그 y축 위치 정보를 location에 저장합니다.
				break;
			}
		}
		
		if(location == -1) { // 탐색에 실패했다면 절차를 종료합니다.
			System.out.println("해당 파일을 찾을 수 없습니다.");
			return;
		}
		
		FileList[location][2] = input[2];
		System.out.println("저장소 내에 파일 데이터가 수정되었습니다.");
		// 파일 유형과 파일 이름에 해당하는 [0]과 [1]은 일치하기 때문에 덮어씌울 필요가 없습니다.
		// 파일 데이터를 중에서도 "내용"에 해당하는 [2]만을 덮어씌웁니다.
	}
	
	
}
