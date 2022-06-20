import java.util.ArrayList;
import java.util.Collections;

public class BookmarkUp {
	BookmarkUp(){}
	
	BookmarkUp(BookmarkListPanel BP, BookmarkList BMList){
		 int Rowindex= BP.table.getSelectedRow(); // 테이블에서 선택된 행의 인덱스
    	 if(Rowindex == -1) {
    		 System.out.println("NOT SELECTED ROW");
    		 return;
    	 }
         int target = Rowindex-1; // 버튼을 누르면 선택된 행이 이동하게 될 행 (즉, 바로 위)                       
         if(target<0) { // 테이블의 제일 위까지 도달한 경우 이동 불가
        	 System.out.println("더 이상 올라갈 수 없습니다.");
         	return;
         } else if(BP.table.getValueAt(Rowindex, 0).equals("V")) { // 선택된 행이 Open 그룹체커면 이동불가
        	 System.out.println("Open된 그룹체커는 이동이 불가능합니다.");
        	 return;
         }
         String UpRowtype = (String) BP.table.getValueAt(target, 0); // 위 행의 타입
      	 String NowRowtype = (String) BP.table.getValueAt(Rowindex, 0); // 현재 행의 타입
    	 String checkurl = (String) BP.table.getValueAt(Rowindex, 3); // 현재 행의 url
    	 String checkUpurl = (String) BP.table.getValueAt(target, 3); // 바로 위 행의 url
      	 String UpGroup = (String) BP.table.getValueAt(target, 1); // 바로 위 행의 그룹명
      	 String NowGroup = (String) BP.table.getValueAt(Rowindex, 1); // 현재 행의 그룹명
         
         if(NowRowtype.equals(">")) { // (1) 선택된 행이 Closed 그룹임
        	 	if(UpRowtype.equals("V")) { // (1-1) 위가 Open 그룹체커인 경우
        	 		System.out.println("위 그룹을 Close해주세요");
        	 	}else if(UpRowtype.equals(">")) { // (1-2) 위가 Closed 그룹체커인 경우     
        		 // 두 그룹의 리스트 내부 위치교환             		                		 
        		 ArrayList<Bookmark> movelist = new ArrayList<Bookmark>(100); // 옮겨담기 위한 임시 리스트
        		 int Groupindex= -1;
        		 int inputindex = -1;
        		 for(int i=BMList.numBookmarks()-1; i>=0; i--) {                    			 
        			 if(BMList.getBookmark(i).getgroupname().equals(NowGroup)) {
        				 Groupindex = i;
        				 movelist.add(BMList.getBookmark(i));                				 
        			 } // 현재 그룹명을 가진 북마크 모두 임시리스트에 담음.       			 
        			 if(BMList.getBookmark(i).getgroupname().equals(UpGroup)) {
        				 inputindex = i; // 위 그룹명을 가진 북마크의 인덱스 확보 
        			 } // inputindex의 마지막 값이 바로 임시 리스트가 진입할 인덱스
        		 }  
        		 if(inputindex == -1 || Groupindex == -1) { //그룹체커 안에 북마크가 없음. 즉 빈 그룹, 이때는 테이블에서 위치만 조정해주면 됨.
            		 BP.model.moveRow(Rowindex, Rowindex, target);               
                     BP.table.setRowSelectionInterval(target,target);  
            		 return;
            	 }		 
        		 for(int i=BMList.numBookmarks()-1; i>=0; i--) { // 현재 그룹명 북마크 모두 리스트에서 삭제처리
        			 if(BMList.getBookmark(i).getgroupname().equals(NowGroup)) {
        				 BMList.DeleteBookmark(i);           				 
        			 }
        		 }                		       		
        		 for(int i=0; i<movelist.size(); i++) { // 위 그룹명의 북마크가 존재하는 첫번째 인덱스에 임시 리스트 투입
        			 BMList.addBookmark(inputindex, movelist.get(i));
        		 }       		 
        		 BP.model.moveRow(Rowindex, Rowindex, target);               
                 BP.table.setRowSelectionInterval(target,target);
        	 } else { // (1-3) 위가 북마크 
        		// 북마크의 인덱스를 찾고, 그룹의 끝부분에 해당하는 인덱스로 위치 이동
        		 int Upindex = -1;
        		 int inputindex= -1;
        		 for(int i=0; i<BMList.numBookmarks(); i++) { // 위 북마크의 리스트 내부 인덱스 확보
        			 if(BMList.getBookmark(i).geturl().equals(checkUpurl)) {
        				 Upindex = i;
        				 break;
        			 }
        		 }       		 
        		 if(!BMList.getBookmark(Upindex).getgroupname().equals("")) { // 만약 위 북마크가 특정 그룹명을 가지고 있으면 움직일 수 없음.
        			 System.out.println("다른 그룹 내부에 진입할 수 없습니다. 그룹을 Close해주세요.");
        			 return;
        		 }      		 
        		 for(int i=BMList.numBookmarks()-1; i>=0; i--) {
        			 if(BMList.getBookmark(i).getgroupname().equals(NowGroup)) { // 리스트 내 그룹의 마지막 인덱스 탐색
        					 inputindex = i+1; // 삽입할 지점
        					 break;
        			 }		 
        		 }      
        		 BP.model.moveRow(Rowindex, Rowindex, target);               
                 BP.table.setRowSelectionInterval(target,target); 
                 if(inputindex!=-1) { // -1인 경우 리스트에 없는 빈 그룹이므로 테이블 위치 이동만, -1이 아닌 경우 리스트에서도 위치 조정
            		 BMList.addBookmark(inputindex, BMList.getBookmark(Upindex));
            		 BMList.DeleteBookmark(Upindex);  
                 }
        	 }        	 	
         } else { // (2) 선택된 행이 북마크일 때
         if(UpRowtype.equals("V")) { // (2-1) 위가 Open된 그룹체커라면
        	 if(NowGroup.equals(UpGroup)) {
        		 System.out.println("그룹을 벗어날 수 없습니다.");
        		 return; // 첫번째, 그룹 내부의 북마크가 Open된 그룹체커 위로 올라가려는 시도, 비정상적인 명령 통제
        	 } 
        	 System.out.println("위 그룹을 close해주세요."); // 두번째, 빈 그룹이 열려있는 경우
         } else if(UpRowtype.equals(">")) { // (2-2) 위가 Closed된 그룹체커라면        
        	int indexForMove = -1, indexNow = -1;
        	boolean findMoveindex = false;
        	for(int i=0; i<BMList.numBookmarks(); i++) {
        		if(BMList.getBookmark(i).getgroupname().equals(UpGroup) && !findMoveindex) {
        			findMoveindex = true; // 그룹 맨 앞 인덱스로 현재 선택된 북마크의 위치를 이동해야 함
        			indexForMove = i;
        		}
        		if(BMList.getBookmark(i).geturl().equals(checkurl)) { // 리스트 내부에서 현재 북마크의 url을 통해 인덱스 탐색
        			indexNow = i;
        			break;
        		}         			
        	}                	
        	
        	 if(indexForMove == -1) { // 그룹체커 안에 개체가 없음. 즉 빈 그룹, 이때는 테이블에서 위치만 조정해주면 됨.
        		 BP.model.moveRow(Rowindex, Rowindex, target);               
                 BP.table.setRowSelectionInterval(target,target);  
        		 return;
        	 }              	 
        	 Bookmark move = BMList.DeleteBookmark(indexNow); // 현재 북마크 리스트에서 삭제 후
        	 BMList.addBookmark(indexForMove, move); // 그룹의 가장 앞부분에 삽입
        	 BP.model.moveRow(Rowindex, Rowindex, target); // 첫번째인덱스~두번째인덱스까지의 행을 target 행으로 이동               
             BP.table.setRowSelectionInterval(target,target); // 선택되었음을 의미하는 파란칸을 첫번째인자부터 두번째인자까지 표시
         } else { // (2-3) 위가 북마크라면
        	 if(!UpGroup.equals("") && !NowGroup.equals(UpGroup)) {
        		 System.out.println("다른 그룹 내부에 진입할 수 없습니다. 그룹을 Close해주세요.");
        		 return;
        	 } // 위 북마크의 그룹이 공백이 아니면서, 위와 현재의 그룹명이 서로 다름 (즉, 그룹 내부에 진입하려고 하는 비정상적인 시도)
        	 
             for(int i=0; i<BMList.numBookmarks(); i++) {
            	 if(BMList.getBookmark(i).geturl().equals(checkurl)) { // 북마크 발견
            		 Collections.swap(BMList.getlist(), i-1, i); //리스트에서 SWAP                		 
            		 break;                 		 
            	 	}
             	} // 그룹 내부에서의 위치 이동은 허용
        	 BP.model.moveRow(Rowindex, Rowindex, target);        
             BP.table.setRowSelectionInterval(target,target);  
             }
         }   		
	}
}