import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.DateTimeException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class BookmarkList {
	private int index;
	private Bookmark[] bm;
	
	private int isBMcheck(String[] str) {
		if(isDateTimeright(str[1])==1 && isURLright(str[2])==1) {
			return 1;
		} else {
			return 0;
		}
	}
	private int isURLright(String input) {
		try {
			URL nurl = new URL(input);
			return 1;	
		} catch(MalformedURLException e){ //만약 url 생성이 실패하면 
			System.out.print("MalformedURLException: wrong URL - No URL ; invalid Bookmark info line: ");
		} catch(Exception e){ // 그 외 예상치 못한 예외의 경우 관련 내용 출력
			e.printStackTrace();
		}
		return 0;
	}
	private int isDateTimeright(String input) {
		String regex = "^19[0-9][0-9]|20[0-2][0-2]-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])_([0-1][0-9]|2[0-3]):[0-5][0-9]$";
		// yyyy-MM-dd_HH:mm이라는 날짜/시간의 양식을 나타내는 정규표현식
		try {
			if(input.matches(regex)) {
				return 1;
			} else { // 형식에 맞지 않는다면 예외 강제로 발생
				throw new Exception();
			}
		} catch(Exception e){ 
			System.out.print("Date Format Error -> No Created Time invalid Bookmark info line: ");
		}
		return 0;
	}
	
	public void makeBookmark(File file) {
		try {
			Scanner input = new Scanner(file);
			while(input.hasNext()) {
				String line = input.nextLine();
				if(line.startsWith("//") || line.isEmpty()){ // 주석 또는 빈줄인 경우 북마크 객체에 담길 수 없음.
					 continue;
				}
				String[] data = line.split(",|;", 5);
				String[] information = new String[5]; // 크기가 5로 고정된 문자열 배열 선언
				Arrays.fill(information, ""); // 이후 미리 공백을 입력해둠
				for(int i=0; i<data.length; i++) { // data 배열의 크기만큼 순회
					data[i] = data[i].trim(); // 공백 제거
					information[i] = data[i]; // information 배열에 담음.
				}	
				
				if(isBMcheck(information)==1) { // 형식 검증이 성공하면 북마크 객체에 담음. 
					bm[index] = new Bookmark(information[0], information[1], information[2], information[3], information[4]);
					// 만약 파싱된 데이터를 담은 data 배열의 크기가 5가 아니더라도, 이미 information은 인덱스 5까지의 값이 이미 공백으로 존재하고 있기 때문에,
					// 이 구간에서 ArrayIndexOutOfBoundsException은 발생하지 않음.
					bm[index].print();
					index++;
				} else {
					System.out.println(line); // 형식 검증을 통과하지 못하면 그 line을 출력함. 이 line은 북마크에 담기지 못함.
				}
			}
			input.close();
		} catch(IOException e) { // 파일 읽기 실패
			System.out.println("Unknown BookmarkList data File");
		} catch(Exception e) { // 그 외의 예외처리
			e.printStackTrace();
		}
	} 
	
	
	BookmarkList(String FileName) {
		bm = new Bookmark[100];
		index = 0;
		try {
			File file = new File(FileName);
			makeBookmark(file);
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // 북마크 목록 생성
	public int numBookmarks() {
		return index;
	} // 전체 북마크의 개수를 알려주는 메서드
	public Bookmark getBookmark(int i) {
		return bm[i];
	} // i번째 북마크에 대한 내부 정보를 출력하는 메서드
	
	public void mergeByGroup() {
		try {
			for(int i=0; i<index; i++) {
				for(int j=i+1; j<index; j++) {
					if(getBookmark(i).groupname.equals(getBookmark(j).groupname) && !getBookmark(i).groupname.isEmpty()){
						// 두 북마크의 그룹명이 같은 경우 재정리 실행, 단 그룹명이 없는(비어있는) 경우 독립적으로 처리.
						Bookmark tmp = bm[j]; // j인덱스 북마크를 받아옴					
						for(int k=j; k>i+1; k--) { // j부터 i+2까지 역순으로 순회
							bm[k] = bm[k-1]; // i+1부터 j-1까지의 북마크 배열을 통째로 한 칸씩 오른쪽으로 밀어냄.
						}	
						bm[i+1] = tmp; // i+1번째 인덱스에 j번째 인덱스 북마크 객체를 넣음.
						i++;
					}
				}
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // 같은 그룹 이름을 갖는 북마크들을 묶어서 재정리하는 메서드
	
	public void WriteBookmarkToFile(String file) {
		try {
		File tofile = new File(file);
		PrintWriter pw = new PrintWriter(tofile);
		for(int i=0; i<index; i++) {
			pw.println(getBookmark(i).name+ "," + getBookmark(i).datetime+ "," + getBookmark(i).url+ "," + getBookmark(i).groupname+ "," +getBookmark(i).memo);
		}
		pw.flush();
		pw.close();
	} catch (Exception e) {
		e.printStackTrace();
		}
	} // BookmarkList에 담긴 북마크 객체들을 역으로 파일에 집어넣는 메서드
}