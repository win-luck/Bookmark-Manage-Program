import javax.swing.*;

import javax.swing.table.DefaultTableModel;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

class BookmarkManager extends JFrame{
    
	BookmarkManager(BookmarkList BMList) {
		super("Bookmark Manager"); 
		setLayout(new BorderLayout()); 
        
        BookmarkListPanel BLP = new BookmarkListPanel(BMList);
        BookmarkListButton BLB = new BookmarkListButton(BLP, BMList);
        add(BLP,BorderLayout.CENTER); 
        add(BLB,BorderLayout.EAST); 
        
        pack();
        setSize(900,300);
        setLocation(400, 300);  
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class BookmarkListPanel extends JPanel{
	private String[] title = {" ", "Group", "Name", "URL", "Created Time", "Memo"};
	private String[] flag = null;
	DefaultTableModel model;		
    JTable table;
    JScrollPane scrollPane;

	    BookmarkListPanel(BookmarkList BMList){
	    model = new DefaultTableModel() {
	    		@Override
			public boolean isCellEditable(int row, int column) {
			       //all cells false
			      return false;
			   }
		}; 
	    model.setColumnCount(title.length);
	    model.setColumnIdentifiers(title);	   
	    for(int i=0; i<BMList.numBookmarks(); i++) {
		     String[] groupchecker = {">", BMList.getBookmark(i).getgroupname(), "","","",""};
		   	 String[] str = {"", BMList.getBookmark(i).getgroupname(), BMList.getBookmark(i).getname(), BMList.getBookmark(i).geturl(), BMList.getBookmark(i).getdatetime(), BMList.getBookmark(i).getmemo()};
		   	 String check = BMList.getBookmark(i).getgroupname();
		   	 
		   	 if(flag != null && check.equals(flag[1])) { 
		   		 continue;
		   	 }
	
		   	 if(check.isEmpty()) { // 그룹명이 공백인 경우는 바로바로 테이블에 들어감.
		   		 model.addRow(str);
		   	 } else { 	// 그렇지 않은 경우는 그룹체커가 삽입됨. 이후 대처는 이벤트리스너에서
		   		 model.addRow(groupchecker);
		   		 flag = groupchecker;
		   	 }
	   	 }
	
	    table = new JTable(model);
	    table.getColumnModel().getColumn(0).setPreferredWidth(5);  //JTable 레이아웃 크기 조정
	    table.getColumnModel().getColumn(1).setPreferredWidth(50);
	    table.getColumnModel().getColumn(2).setPreferredWidth(50);
	    table.getColumnModel().getColumn(3).setPreferredWidth(300);
	    table.getColumnModel().getColumn(4).setPreferredWidth(200);
	    table.getColumnModel().getColumn(5).setPreferredWidth(50);
	    table.getTableHeader().setReorderingAllowed(false); // 열의 임의적인 이동 금지
	    
	    scrollPane = new JScrollPane(table);
	    scrollPane.setPreferredSize(new Dimension(800, 300));
	    add(scrollPane);  
	    
	    table.addMouseListener(new MouseListener() { // 그룹화 이벤트리스너
	    	@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getClickCount()>0) {
					JTable target = (JTable)e.getSource();
					if(target!=table) return;
				
				if(table.getSelectedColumn() == 0) {
					String openMark = (String)table.getValueAt(table.getSelectedRow(), 0); // 그룹의 현재 모드 확인
					String sGroup = (String)table.getValueAt(table.getSelectedRow(), 1); // 그룹명 저장
					if(openMark.equals(">")) { // 누르기 직전 상황이 >면 그룹 Open 모드로 확대
						table.setValueAt("V", table.getSelectedRow(), 0);
						int index = table.getSelectedRow();
						for(int i=BMList.numBookmarks()-1; i>=0; i--) {
							if(BMList.getBookmark(i).getgroupname().equals(sGroup)) {
								String[] inputstr = {"", BMList.getBookmark(i).getgroupname(), BMList.getBookmark(i).getname(), BMList.getBookmark(i).geturl(), BMList.getBookmark(i).getdatetime(), BMList.getBookmark(i).getmemo()};
								model.insertRow(index+1, inputstr);
							}					
						}				
					} else if(openMark.equals("V")){ // 누르기 직전 상황이 V면 그룹 Close 모드로 축소
						table.setValueAt(">", table.getSelectedRow(), 0);
						int index = table.getSelectedRow();
						for(int i=table.getRowCount()-1; i>=index+1; i--) {
							if(table.getValueAt(i, 1).equals(sGroup)) {
								model.removeRow(i);	
							}				
						}					
					}					
				}				
			}				
		}

			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
	    });
	    
	 }
}

class BookmarkListButton extends JPanel{
	 private JButton jbtAdd = new JButton("ADD");
	 private JButton jbtDelete = new JButton("DELETE");
	 private JButton jbtUp = new JButton("UP");
	 private JButton jbtDown = new JButton("DOWN");
	 private JButton jbtSave = new JButton("SAVE");
	 	 
    BookmarkListButton(BookmarkListPanel BP, BookmarkList BMList){
        setLayout(new GridLayout(5,1)); 
        add(jbtAdd);
        add(jbtDelete);
        add(jbtUp);
        add(jbtDown);
        add(jbtSave); 
        
        jbtAdd.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                new BookmarkInfo(BP, BMList); 
            }
        }); 
        // ADD

        jbtDelete.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            	new BookmarkDelete(BP, BMList);
            }
        }); 
        // DELETE
        
        jbtUp.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {               	
            	 new BookmarkUp(BP, BMList);     
            }
        }); 
        // UP
             
        jbtDown.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {         	
            	 new BookmarkDown(BP, BMList);          	
            }
        }); 
        // DOWN


        jbtSave.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
            	BMList.WriteBookmarkToFile();
            }
        }); 
        // SAVE
    }
 
}