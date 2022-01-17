package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/user")
public class UserController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
//		System.out.println(action);

		if ("joinForm".equals(action)) {
			WebUtil.forward(request, response, "WEB-INF/views/user/joinForm.jsp");
		}
		else if ("join".equals(action)) {
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			UserVo uVo = new UserVo(0, id, password, name, gender);
			new UserDao().insert(uVo);

			WebUtil.forward(request, response, "WEB-INF/views/user/joinOk.jsp");
		}
		else if ("loginForm".equals(action)) {
			WebUtil.forward(request, response, "WEB-INF/views/user/loginForm.jsp");

		} 
		else if("login".equals(action)) {
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			UserVo authVo = new UserDao().getUser(id, password);
			System.out.println(authVo);
			
			if(authVo == null) {
				System.out.println("로그인실패");
				WebUtil.redirect(request, response, "/mysite/user?action=loginForm&result=fail");
				
			}
			else {
				System.out.println("로그인성공");
				HttpSession session = request.getSession();
				session.setAttribute("authUser", authVo);
				WebUtil.redirect(request, response, "/mysite/main");
			}
			
		}
		else if("logout".equals(action)) {
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			WebUtil.redirect(request, response, "/mysite/main");
		}
		else if("modifyForm".equals(action)) {
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			UserVo userData = new UserDao().getUserData(authUser.getNo());		
			
			request.setAttribute("userData", userData);
			WebUtil.forward(request, response, "WEB-INF/views/user/modifyForm.jsp");
		}
		else if("modify".equals(action)) {
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender =  request.getParameter("gender");
			
			UserVo modifyVo = new UserVo(0 ,id, password, name, gender);
			new UserDao().modify(modifyVo);
			
			HttpSession session = request.getSession();
			UserVo authVo = new UserDao().getUser(id, password);
			session.setAttribute("authUser", authVo);
			
			WebUtil.redirect(request, response, "/mysite/main");
		}
		else {

		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
