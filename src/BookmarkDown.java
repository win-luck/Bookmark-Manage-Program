import java.util.ArrayList;
import java.util.Collections;

public class BookmarkDown {
	BookmarkDown(){}
	
	BookmarkDown(BookmarkListPanel BP, BookmarkList BMList){
		 int Rowindex= BP.table.getSelectedRow(); // 테이블에서 선택된 행의 인덱스    	 
    	 if(Rowindex == -1) {
    		 System.out.println("NOT SELECTED ROW");
    		 return;
    	 }
         int target = Rowindex+1; // 버튼을 누르면 선택된 행이 이동하게 될 행 (즉, 바로 밑)  
         if(target>=BP.table.getRowCount()) { // 테이블의 제일 밑까지 도달한 경우 이동 불가
        	 System.out.println("더 이상 내려갈 수 없습니다.");
         	return;
         } else if(BP.table.getValueAt(Rowindex, 0).equals("V")) { // 선택된 행이 Open 그룹체커면 이동불가
        	 System.out.println("Open된 그룹체커는 이동이 불가능합니다.");
        	 return;
         }
         String DownRowtype = (String) BP.table.getValueAt(target, 0); // 아래 행의 타입
      	 String NowRowtype = (String) BP.table.getValueAt(Rowindex, 0); // 현재 행의 타입
    	 String checkurl = (String) BP.table.getValueAt(Rowindex, 3); // 현재 행의 url
    	 String checkDownurl = (String) BP.table.getValueAt(target, 3); // 아래 행의 url
      	 String DownGroup = (String) BP.table.getValueAt(target, 1); // 아래 행의 그룹명
      	 String NowGroup = (String) BP.table.getValueAt(Rowindex, 1); // 현재 행의 그룹명
    
    	if(DownRowtype.equals("V")) { // 아래 행이 Open 그룹체커인 경우 이동 금지
    		System.out.println("아래 그룹을 close해주세요.");
    		return;
    	}
    	
    	if(NowRowtype.equals(">")) { // (1)현재 Closed 그룹이 선택된 상황
    		if(DownRowtype.equals(">")) { // (1-1) 아래가 Closed 그룹일 때		 			
    			 ArrayList<Bookmark> movelist = new ArrayList<Bookmark>(100); // 옮겨담기 위한 임시 리스트
        		 int Groupindex= -1;
        		 int inputindex = -1;
        		 for(int i=BMList.numBookmarks()-1; i>=0; i--) {                			               			 
        			 if(BMList.getBookmark(i).getgroupname().equals(DownGroup)) { // 아래 그룹에 속하는 북마크들 임시리스트에 담음
        				 Groupindex = i;
        				 movelist.add(BMList.getBookmark(i));                				 
        			 }
        			 if(BMList.getBookmark(i).getgroupname().equals(NowGroup)) { // 현재 그룹에 속하는 북마크 인덱스 갱신
        				inputindex = i;
        			 } // 마지막 inputindex에 임시 리스트 삽입하면 됨.
        		 }      	           		 
        		 if(inputindex == -1 || Groupindex == -1) { // 그룹체커 안에 북마크가 없음. 즉 빈 그룹, 이때는 테이블에서 위치만 조정해주면 됨.
            		 BP.model.moveRow(Rowindex, Rowindex, target);               
                     BP.table.setRowSelectionInterval(target,target);  
            		 return;
            	 }	                		                		 
        		 for(int i=BMList.numBookmarks()-1; i>=0; i--) {
        			 if(BMList.getBookmark(i).getgroupname().equals(DownGroup)) {
        				 BMList.DeleteBookmark(i);            				 
        			 }
        		 }          		 
        		 for(int i=0; i<movelist.size(); i++) {
        			 BMList.addBookmark(inputindex, movelist.get(i));
        		 }        		 
        		 BP.model.moveRow(Rowindex, Rowindex, target);               
                 BP.table.setRowSelectionInterval(target, target);	
    		} else { // (1-2) 아래가 북마크일 때            			
    			 int Downindex = -1;
        		 int moveindex= -1;
        		 for(int i=0; i<BMList.numBookmarks(); i++) {
        			 if(BMList.getBookmark(i).geturl().equals(checkDownurl)) { // 아래 공백 북마크의 인덱스 확보
        				 Downindex = i;
        				 break;
        			 }
        		 } 
    			
    			 for(int i=0; i<BMList.numBookmarks(); i++) {
        			 if(BMList.getBookmark(i).getgroupname().equals(NowGroup)) { // 리스트 내 현재 그룹 북마크의 첫 인덱스 확보
        					 moveindex = i;
        					 break;
        			 }		 
        		 }   
    			 
    			  if(moveindex!=-1) { // -1인 경우 빈 그룹이므로 테이블 이동까지만, -1이 아닌 경우 리스트에서 위치 조정
    				 Bookmark Down = BMList.getlist().remove(Downindex);  
             		 BMList.addBookmark(moveindex, Down);
                  }		
                  BP.model.moveRow(Rowindex, Rowindex, target);               
                  BP.table.setRowSelectionInterval(target, target); 			
    		}
    	} else { // (2) 현재 행이 북마크일 때
    		if(DownRowtype.equals(">")) {// (2-1) 아래가 Closed 그룹일 떄
    			 int Nowindex = -1;
        		 int moveindex= -1;
        		 for(int i=0; i<BMList.numBookmarks(); i++) {
        			 if(BMList.getBookmark(i).geturl().equals(checkurl)) { // 현재 선택된 북마크의 인덱스 확보
        				 Nowindex = i;
        				 break;
        			 }
        		 } 
        		 if(!BMList.getBookmark(Nowindex).getgroupname().equals("")) { //현재 북마크가 특정 그룹명을 가지고 있으면 움직일 수 없음.
        			 System.out.println("그룹을 이탈할 수 없습니다.");
        			 return;
        		 }        		 
        		 for(int i=BMList.numBookmarks()-1; i>=0; i--) {
        			 if(BMList.getBookmark(i).getgroupname().equals(DownGroup)) { // 리스트 내 아래 그룹 북마크의 마지막 인덱스 확보
        					 moveindex = i+1; 
        					 break;
        			 }		 
        		 }     	
                 BP.model.moveRow(Rowindex, Rowindex, target);               
                 BP.table.setRowSelectionInterval(target, target); 
                 
                 if(moveindex!=-1) { // -1인 경우 빈 그룹이므로 테이블 이동까지만, -1이 아닌 경우 리스트에서 위치 조정
           		 BMList.addBookmark(moveindex, BMList.getBookmark(Nowindex));
           		 BMList.DeleteBookmark(Nowindex);  
                }	
    		} else { // (2-2) 아래 행이 북마크	 		
    		if(!NowGroup.equals("") && !NowGroup.equals(DownGroup)) { // 아래가 공백 북마크임
           		 System.out.println("그룹을 이탈할 수 없습니다.");
           		 return;
    		} //현재 북마크의 그룹명이 공백이 아니고 아래 북마크와 그룹명이 서로 다름 -> 그룹 내부에서 북마크가 이탈하려는 시도 
    		    for(int i=0; i<BMList.numBookmarks()-1; i++) {
            	 if(BMList.getBookmark(i).geturl().equals(checkurl)) { // 리스트에서 현재 북마크 탐색
            		 Collections.swap(BMList.getlist(), i, i+1);           		 
            		 break;                 		 
            	 	}
             	}// 그룹 내부 북마크끼리의 위치이동은 허용
             BP.model.moveRow(Rowindex, Rowindex, target);        
             BP.table.setRowSelectionInterval(target,target); 
    		}
    	}	
	}
}
