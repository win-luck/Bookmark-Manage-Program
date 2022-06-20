public class BookmarkDelete {
	BookmarkDelete(){}
	
	 public boolean isGroupChecker(BookmarkListPanel BP, int index) {
		 if(BP.table.getValueAt(index, 0).equals(">") || BP.table.getValueAt(index, 0).equals("V")) {
			 return true;
		 }
		 return false;
     }
	
	BookmarkDelete(BookmarkListPanel BP, BookmarkList BMList) {
		if(BP.table.getSelectedRow() != -1) {
    		if(isGroupChecker(BP, BP.table.getSelectedRow())) { // 만약 그룹체커가 선택된 상태에서 delete일 경우 그룹에 속한 모든 북마크가 통째로 삭제
    			
    			if(BP.table.getValueAt(BP.table.getSelectedRow(), 0).equals(">")) { // Closed 상태
    			String groupname = (String) BP.table.getValueAt(BP.table.getSelectedRow(), 1);
    			BP.model.removeRow(BP.table.getSelectedRow());
    			for(int i=BMList.numBookmarks()-1; i>=0; i--) {
    				if(groupname.equals(BMList.getBookmark(i).getgroupname())) {
    					BMList.DeleteBookmark(i);
    				}
    			}
    			
    			} else { // Open 상태에선 그룹체커 삭제 불가능하도록 설정
    				System.out.println("그룹은 Closed 상태일 때만 그룹 단위 삭제가 가능합니다.");
    			}			
    			return;
    		}
    		
    		int index = -1;
    		String targeturl = (String) BP.table.getValueAt(BP.table.getSelectedRow(), 3); // 리스트에서 제거할 북마크의 인덱스 탐색
    		System.out.println(targeturl);
    		for(int i=0; i<BMList.numBookmarks(); i++) {
    			if(BMList.getBookmark(i).geturl().equals(targeturl)) {
    				index = i;
    				break;
    			}
    		}
    		 		
    		// 그룹체커가 아닐 경우(즉, 북마크인 경우)
    		BMList.DeleteBookmark(index); // 데이터베이스에서 선택된 행에 대한 데이터 탐색 후 제거
    		BP.model.removeRow(BP.table.getSelectedRow()); // 테이블에서 선택된 행 제거
    		
    	} else { // 행이 선택되지 않았음.
    		System.out.println("NOT SELECTED ROW");
    	}
	
	}
}
