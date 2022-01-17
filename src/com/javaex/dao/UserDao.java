package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {
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
	
	//회원등록
	public int insert(UserVo userVo) {
		int count=0;
		try {
			getConnection();
			
			// 3. SQL문 준비 / 바인딩 / 실행
			// 문자열
			String query ="";
			query += " insert into users ";
			query += " values(seq_users_no.nextval, ?, ?, ?, ?) " ;
			// 쿼리문으로 바꾸기
			pstmt = conn.prepareStatement(query);
			//바인딩
			pstmt.setString(1, userVo.getId() );    
			pstmt.setString(2, userVo.getPassword());
			pstmt.setString(3, userVo.getName());
			pstmt.setString(4, userVo.getGender());
			//실행
			count = pstmt.executeUpdate(); 
			//4.결과처리
			System.out.println(count + " 건이 등록되었습니다.[UserDao]");
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		close();
		return count;
	}
	
	//회원정보 가져오기(로그인용)
	public UserVo getUser(String id, String password) {
		UserVo userVo = null;
		try {
			getConnection();
			// 3. SQL문 준비 / 바인딩 / 실행
			// 문자열
			String query ="";
			query += " select no,  ";
			query += " 		  name ";
			query += " from users ";
			query += " where id = ? and " ;
			query += " 		 password = ? ";
			//쿼리문으로
			pstmt = conn.prepareStatement(query);
			//바인딩
			pstmt.setString(1, id );    
			pstmt.setString(2, password);
			//실행
			rs = pstmt.executeQuery();  
			//4.결과처리
			while(rs.next()) {
				userVo = new UserVo();
				userVo.setNo(rs.getInt(1));
				userVo.setName(rs.getString(2));
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		close();
		
		return userVo; 
	}
	
	//회원정보 불러오기(회원수정용)
	public UserVo getUserData(int no) {
		UserVo userVo = null;
		try {
			getConnection();
			//3.SQL문 준비/쿼리문/바인딩
			//문자열
			String query ="";
			query += " select id, ";
			query += " 		  password, ";
			query += " 		  name, ";
			query += " 		  gender ";
			query += " from users ";
			query += " where no = ? ";
			//쿼리문
			pstmt = conn.prepareStatement(query);
			//바인딩
			pstmt.setInt(1, no );    
			//실행
			rs = pstmt.executeQuery();  
			//결과처리
			while(rs.next()) {
				userVo = new UserVo(0, rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		close();
		
		return userVo; 
	}
	
	//회원정보 수정
	public void modify(UserVo userVo) {
		try {
			getConnection();
			//3.SQL문 준비/바인딩/실행
			//문자열
			String query ="";
			query += " update users ";
			query += " set password = ?, " ;
			query += "	   name = ?, " ;
			query += "	   gender = ? " ;
			query += " where id = ? ";
			//쿼리문
			pstmt = conn.prepareStatement(query);
			//바인딩
			pstmt.setString(1, userVo.getPassword() );    
			pstmt.setString(2, userVo.getName());
			pstmt.setString(3, userVo.getGender());
			pstmt.setString(4, userVo.getId());
			//실행
			int count = pstmt.executeUpdate();  
			//4.결과처리
			System.out.println(count + " 건이 수정되었습니다.[UserDao]");
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		close();
	}
	
	
}
