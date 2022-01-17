package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.BoardDao;
import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
request.setCharacterEncoding("UTF-8");
		
		//파라미터 가져오기
		String act = request.getParameter("action");
		
		//리스트 가져오기
		if("list".equals(act)) {
		
			BoardDao boardDao = new BoardDao();
			List<BoardVo> bList = boardDao.getBoardList();
			
			UserVo userVo = new UserVo();
			UserDao userDao = new UserDao();
			
			System.out.println(bList);
			
			//값을 넣어놓기
			request.setAttribute("bList", bList);
								
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
				
		}else if("writeForm".equals(act)) {
							
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
				
		}else if("write".equals(act)) {
						
			//파라미터값 가져오기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("no"));
						
			//받아온 값을 넣어줌
			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = new BoardVo();
			boardVo.setTitle(title);
			boardVo.setContent(content);
			boardVo.setUserNo(no);
			boardDao.insert(boardVo);
			
			
			//등록후 리스트로 이동하는 리다이렉트
			WebUtil.redirect(request, response, "/mysite/board?action=list");
					
		}else if("delete".equals(act)){
						
			//파라미터값 가져오기
			int uNo = Integer.parseInt(request.getParameter("no")); 
						
			BoardDao boardDao = new BoardDao();
			
			boardDao.delete(uNo);
			
			//등록후 리스트로 이동하는 리다이렉트
			WebUtil.redirect(request, response, "/mysite/board?action=list");
			
		}else if("read".equals(act)) {

			//파라미터값 받아오기
			int uNo = Integer.parseInt(request.getParameter("no"));

			//유저 정보 출력
			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = boardDao.getBoard(uNo);
			
			//정보 넣기
			request.setAttribute("boardVo", boardVo);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
				
		}else if("modifyForm".equals(act)) {
						
			//파라미터값 불러오기
			int no = Integer.parseInt(request.getParameter("no"));
		
			//해당 정보 출력
			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = boardDao.getBoard(no);
			
			//정보 넣기
			request.setAttribute("boardVo", boardVo);
	
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");
				
		}else if("modify".equals(act)) {
						
			//파라미터값 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			//해당 넘버의 내용 수정하는 메소드 사용			
			//넘버, 제목, 내용 순으로 넣기
			BoardDao boardDao = new BoardDao();
			BoardVo boardVo = new BoardVo();
			
			boardVo.setTitle(title);
			boardVo.setContent(content);
			boardVo.setUserNo(no);
			
			boardDao.update(boardVo);
			
			//리다이렉트
			WebUtil.redirect(request, response, "/mysite/board?action=list");
				
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		doGet(request, response);
	}

}
