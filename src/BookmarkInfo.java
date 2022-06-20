import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class BookmarkInfo extends JFrame {
	private String[] title = {"Group", "Name", "URL", "Memo"};
	private JButton jbtInputButton =  new JButton("Input");
    DefaultTableModel model;
    JTable table;
    JScrollPane scrollPane;
    BookmarkInfo() {}    
	public BookmarkInfo(BookmarkListPanel BP, BookmarkList BMList){
        super("Input New Bookmark");
        setLayout(new BorderLayout());
        model = new DefaultTableModel();
        model.setColumnCount(title.length);
        model.setColumnIdentifiers(title);

        String[] str = {"","","",""};
        model.addRow(str);
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(40); 
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getTableHeader().setReorderingAllowed(false); //
        scrollPane = new JScrollPane(table); 
        jbtInputButton.setSize(200,100);
       
        add(scrollPane,BorderLayout.CENTER); //
        add(jbtInputButton,BorderLayout.EAST); // 
        pack();
        this.setSize(700,120);
        this.setLocation(450, 350);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //
        setVisible(true);
        
        jbtInputButton.addActionListener(new ActionListener() { 
        	@Override
        	public void actionPerformed(ActionEvent e) {	
        		String[] input = new String[4];
        		if(table.getValueAt(0, 2).equals("")) {
        			System.out.println("URL은 필수 입력 항목입니다");
        			return;
        		} 		
        		for(int i=0; i<4; i++) {     
        			input[i] = (String) table.getValueAt(0, i);
        			table.setValueAt("", 0, i);
        		}	
	        	insert(BP, BMList, input);       	
        		}        	
        });    
    }
	
	public void insert(BookmarkListPanel BP, BookmarkList BMList, String[] input) {
		if(BMList.isURLright(input[2]) == 1) { // url이 형식에 맞을 경우
			Bookmark newBM = new Bookmark(input[2]); // url만 입력받아 현재시간까지 저장하는 생성자 호출
	    	newBM.SetOtherelement(input[0], input[1], input[3]); // 그룹명, 이름, 메모를 북마크 객체에 담음.
	    	String[] groupcheck = {">", newBM.getgroupname(), "", "", "", ""}; // 그룹체커
	    	String[] newRow = {"", newBM.getgroupname(), newBM.getname(), newBM.geturl(), newBM.getdatetime(), newBM.getmemo()};
	    	// 테이블 삽입 전용 문자열 배열 newRow
	    	
	        if(!newBM.getgroupname().isEmpty()) { // 추가할 북마크의 그룹명이 공백이 아닌 경우
	            for(int i=0; i<BP.table.getRowCount(); i++) {
		        	if(newBM.getgroupname().equals(BP.table.getValueAt(i, 1))) { // 현재 테이블 내에 존재하는 그룹명을 가진 북마크라면?
		        		BMList.addBookmark(newBM); // 리스트에 북마크 추가까지만 (이미 존재하는 그룹체커 밑에 북마크가 들어갈 예정)
		        		BMList.mergeByGroup(); // 리스트 내 그룹화를 위한 정렬 실행, 테이블 변화 없이 메서드 종료
		        		dispose();
		        		return;
		        	}
		        }
	        	// 그룹명이 테이블에 존재하지 않음, 즉 새로운 그룹명의 경우
	            BMList.addBookmark(newBM); // 리스트에 북마크 추가
	        	BP.model.addRow(groupcheck); // 테이블엔 그룹체커만
	        } else { // 그룹명이 공백이면	        
		    	BMList.addBookmark(newBM); //리스트 추가 및 가장 밑에 북마크 삽입, 
		        BP.model.addRow(newRow);
	        }
	        dispose();
		}
	}
}