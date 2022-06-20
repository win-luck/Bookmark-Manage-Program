import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

public class BookmarkList {
	private ArrayList<Bookmark> bmdata;
	public static String FileName;
	
	public int isBMcheck(String[] str) {
		if(isDateTimeright(str[1])==1 && isURLright(str[2])==1) {
			return 1;
		} else {
			return 0;
		}
	}// ����
	
	public int isBMcheck(Bookmark bm) { // 오버라이딩
		if(isDateTimeright(bm.getdatetime()) == 1 && isURLright(bm.geturl()) == 1){
			return 1;
		}else {
			return 0;
		}
	}
	
	public int isURLright(String input) {
		try {
			URL nurl = new URL(input);
			return 1;	
		} catch(MalformedURLException e){ //���� url ������ �����ϸ� 
			System.out.print("MalformedURLException: wrong URL - No URL ; invalid Bookmark info line: ");
		} catch(Exception e){ // �� �� ����ġ ���� ������ ��� ���� ���� ���
			e.printStackTrace();
		}
		return 0;
	}
	private static int isDateTimeright(String input) {
		final String regex = "^19[0-9][0-9]|20[0-2][0-2]-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])_([0-1][0-9]|2[0-3]):[0-5][0-9]$";
		// yyyy-MM-dd_HH:mm�̶�� ��¥/�ð��� ����� ��Ÿ���� ����ǥ����
		try {
			if(input.matches(regex)) {
				return 1;
			} else { // ���Ŀ� ���� �ʴ´ٸ� ���� ������ �߻�
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
				if(line.startsWith("//") || line.isEmpty()){ // �ּ� �Ǵ� ������ ��� �ϸ�ũ ��ü�� ��� �� ����.
					 continue;
				}
				String[] data = line.split(",|;", 5);
				String[] information = new String[5]; // ũ�Ⱑ 5�� ������ ���ڿ� �迭 ����
				Arrays.fill(information, ""); // ���� �̸� ������ �Է��ص�
				for(int i=0; i<data.length; i++) { // data �迭�� ũ�⸸ŭ ��ȸ
					data[i] = data[i].trim(); // ���� ����
					information[i] = data[i]; // information �迭�� ����.
				}	
				
				if(isBMcheck(information)==1) { // ���� ������ �����ϸ� �ϸ�ũ ��ü�� ����. 
					bmdata.add(new Bookmark(information[0], information[1], information[2], information[3], information[4]));	
					// ���� �Ľ̵� �����͸� ���� data �迭�� ũ�Ⱑ 5�� �ƴϴ���, �̹� information�� �ε��� 5������ ���� �̹� �������� �����ϰ� �ֱ� ������,
					// �� �������� ArrayIndexOutOfBoundsException�� �߻����� ����.
				} else {
					System.out.println(line); // ���� ������ ������� ���ϸ� �� line�� �����. �� line�� �ϸ�ũ�� ����� ����.
				}
			}
			input.close();
			mergeByGroup();
		} catch(IOException e) { // ���� �б� ����
			System.out.println("Unknown BookmarkList data File");
		} catch(Exception e) { // �� ���� ����ó��
			e.printStackTrace();
		}
	} 
	
	
	BookmarkList(String inputFile) {
		bmdata = new ArrayList<Bookmark>(100); // �ϸ�ũ�� �ִ� ���뷮�� 100
		try {
			File file = new File(inputFile);
			FileName = inputFile;
			makeBookmark(file);
		} catch(Exception e) {
			e.printStackTrace();
		}
	} // �ϸ�ũ ��� ����
	
	public Bookmark getBookmark(int i) {
		return bmdata.get(i); 
	} 
	
	public void mergeByGroup() {
		try {
			for(int i=0; i<numBookmarks(); i++) {
				for(int j=i+1; j<numBookmarks(); j++) {
					if(getBookmark(i).getgroupname().equals(getBookmark(j).getgroupname()) && !getBookmark(i).getgroupname().isEmpty()){				
						Bookmark tmp = getBookmark(j); 
						bmdata.remove(j);
						bmdata.add(i+1, tmp);
						i++;
					}
				}
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
	} 
	
	public ArrayList<Bookmark> getlist(){ // private로 보호되어 있는 리스트에 접근
		return bmdata;
	}
	
	public int numBookmarks() { // 리스트의 크기
		return bmdata.size();
	} 
	
	public void addBookmark(Bookmark BM) { // 리스트 맨 뒤에 북마크 추가
		bmdata.add(BM);
	}
	
	public void addBookmark(int inputindex, Bookmark BM) { // 리스트 특정 인덱스에 북마크 삽입
		bmdata.add(inputindex, BM);
	}
	
	public Bookmark DeleteBookmark(int BMindex) { // 특정 인덱스 북마크 삭제
		return bmdata.remove(BMindex);
	}
	
	public void WriteBookmarkToFile() {
		try {
		File tofile = new File(FileName);
		PrintWriter pw = new PrintWriter(tofile);
		for(int i=0; i<numBookmarks(); i++) {
			pw.println(getBookmark(i).getname()+ "," + getBookmark(i).getdatetime()+ "," + getBookmark(i).geturl()+ "," + getBookmark(i).getgroupname()+ "," +getBookmark(i).getmemo());
		}
		pw.flush();
		pw.close();
	} catch (Exception e) {
		e.printStackTrace();
		}
	}
}