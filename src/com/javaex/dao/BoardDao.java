package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs= null;
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";
	
	//connection
	private void getConnection(){
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pw);
		}catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}
	
	//닫기
	private void close() {
		try {               
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}
	
	//게시판 리스트 가져오기
	public List<BoardVo> getBoardList() {
	
		getConnection();
		
		List<BoardVo> boardList = new ArrayList<BoardVo>();
		
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			//문자열준비
			String query = "";
			query += " select  	b.no, ";
			query += " 			title, ";
			query += " 			content, ";
			query += " 			hit, ";
			query += " 			regdate, ";
			query += " 			b.user_no, ";
			query += "         	u.name ";
			query += " from	board b, users u ";
			query += " where b.user_no = u.user_no ";
			//쿼리문 만들기
			pstmt = conn.prepareStatement(query);
			//바인딩 없음.
	
			//실행
			rs = pstmt.executeQuery();
			// 4.결과처리
			while(rs.next()) {
				int no = rs.getInt("b.no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regdate = rs.getString("regdate");
				int userNo = rs.getInt("b.user_no");
				String name = rs.getString("name");
				
				BoardVo boardVo = new BoardVo(no, title, content, hit, regdate, userNo, name);
				boardList.add(boardVo);
			}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		
			close();
			return boardList;
	}
	
	// 게시글 불러오기(하나만)
	public BoardVo getBoard(int num) {
		getConnection();
		
		BoardVo boardVo = null;
		
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			//문자열준비
			String query = "";
			query += " select  	b.no, ";
			query += "			title, ";
			query += " 			content, ";
			query += " 			hit, ";
			query += " 			regdate, ";
			query += " 			b.user_no, ";
			query += "         	u.name ";
			query += " from board b, users u ";
			query += " where b.user_no = u.user_no ";
			query += " and b.user_no = ? ";
			//쿼리문 만들기
			pstmt = conn.prepareStatement(query);
			//바인딩
			pstmt.setInt(1, num);
			//실행
			rs = pstmt.executeQuery();
			// 4.결과처리
			while(rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regdate = rs.getString("regdate");
				int userNo = rs.getInt("user_no");
				String name = rs.getString("name");
				
				boardVo = new BoardVo(no, title, content, hit, regdate, userNo, name);
			}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		
			close();
			
			return boardVo;
	}

	//게시글 쓰기
	public int insert(BoardVo boardVo) {

		getConnection();
		
		int count = 0;
		
		try {
		
			// 3. SQL문 준비 / 바인딩 / 실행
			//문자열준비
			String query = "";
			query += " insert into board ";
			query += " values(seq_board_no.nextval, ?, ?, 0, sysdate, ?) ";
			//쿼리문 만들기
			pstmt = conn.prepareStatement(query);
			//바인딩
			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getUserNo());
			//실행
			count = pstmt.executeUpdate();
			// 4.결과처리
			System.out.println(count + "건 삽입");
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	
		close();
		
		return count;
		
	}
	
	//게시글 삭제
	public int delete(int num) {

		getConnection();
		
		int count = 0;
		
		try {
		
		// 3. SQL문 준비 / 바인딩 / 실행
		//문자열준비
		String query = "";
		query += " delete from board ";
		query += " where user_no = ? ";
		
		//쿼리문 만들기
		pstmt = conn.prepareStatement(query);
		
		//바인딩
		pstmt.setInt(1, num);
		
		//실행
		count = pstmt.executeUpdate();
			
		// 4.결과처리
		System.out.println(count + "건 삭제");
		
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	
		close();
		
		return count;
		
	}
	
	//게시글 수정
	public int update(BoardVo boardVo) {
		getConnection();
		
		int count = 0; 
		try {
			getConnection();
			//3.
			//문자열
			String query ="";
			query += " update board ";
			query += " set title = ?, ";
			query += " 	   content = ? ";
			query += " where no = ? " ;
			//쿼리문으로
			pstmt = conn.prepareStatement(query);
			//바인딩
			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getNo());
			//실행
			count = pstmt.executeUpdate();  
			//결과처리
			System.out.println(count + " 건이 수정되었습니다.[board]");
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		close();
		
		return count;
	}
	
}
