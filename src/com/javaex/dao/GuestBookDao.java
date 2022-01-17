package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestBookVo;



public class GuestBookDao {
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs= null;
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";
	
	//일반 메소드
	//connection
	private void getConnection(){
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
			
		}catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}
	
	//닫기
	private void close() {
		// 5. 자원정리
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
	
	//방명록쓰기
	public void addGuestBook(GuestBookVo gVo) {

		try {
			getConnection();
			//3.SQL준비/바인딩/실행
			//문자열
			String query ="";
			query += " insert into guestbook ";
			query += " values(seq_guestbook_no.nextval, ?, ?, ?, sysdate) " ;
			//쿼리문으로
			pstmt = conn.prepareStatement(query);
			//바인딩
			pstmt.setString(1, gVo.getName());    
			pstmt.setString(2, gVo.getPassword());
			pstmt.setString(3, gVo.getContent());
			//실행
			int count = pstmt.executeUpdate();  
			//4.결과처리
			System.out.println(count + " 건이 등록되었습니다.[guestbook]");
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		close();
	}
	
	//방명록 삭제
	public void deleteGuestBook(GuestBookVo gVo) {
		
		try {
			getConnection();
			//3.
			//문자열
			String query ="";
			query += " delete from guestbook ";
		    query += " where no = ? and password = ? ";
		    //쿼리문으로
			pstmt = conn.prepareStatement(query);
			//바인딩
			pstmt.setInt(1, gVo.getNo());
			pstmt.setString(2, gVo.getPassword());
			//실행
			int count = pstmt.executeUpdate();
			//4.결과처리
			System.out.println(count + " 건이 삭제되었습니다.[guestbook]");
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		

		close();
	}
	
	//방명록 리스트 불러오기
	public List<GuestBookVo> getGuestBookList() {
		List<GuestBookVo> guestBookList = new ArrayList<>();
		
		try {
			getConnection();
			//3.
			//문자열
			String query ="";
			query += " select no, ";
			query += " 	 	  name, ";
			query += " 		  password, ";
			query += " 		  content, ";
		  	query += " 		  reg_date ";
			query += " from guestbook ";
			query += " order by reg_date desc ";
			//쿼리문으로
			pstmt = conn.prepareStatement(query);
			//실행
			rs = pstmt.executeQuery();  
			//결과처리
			while(rs.next()) { //			  
				GuestBookVo gVo = new GuestBookVo( 
						rs.getInt(1), rs.getString(2), "", rs.getString(4).replace(" ", "&nbsp;").replace("\n", "<br>"), rs.getString(5) );
				guestBookList.add(gVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		close();
			
		return guestBookList;
	}
	
	//방명록 불러오기(수정용)
	public GuestBookVo getGuestBook(int no) {
		GuestBookVo guestBook = null;
		
		try {
			getConnection();

			String query ="";
			query += " select no, ";
			query += " 	 	  name, ";
			query += " 		  password, ";
			query += " 		  content, ";
			query += " 		  reg_date ";
			query += " from guestbook ";
			query += " where no = ?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();  
			
			rs.next();		  
			guestBook = new GuestBookVo( 
					rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4).replace(" ", "&nbsp;").replace("\n", "<br>"), rs.getString(5) );
			
			

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		close();
			
		return guestBook;
	}
	
}