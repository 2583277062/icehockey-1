package com.icehockey.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icehockey.entity.ClubRecord;
import com.icehockey.entity.TeamRecord;
import com.icehockey.entity.User;
import com.icehockey.service.ClubRecordService;
import com.icehockey.service.TeamRecordService;
import com.icehockey.service.UserService;

/**
 * Servlet implementation class TeachteamServlet
 */
@WebServlet("/TeachteamServlet")
public class TeachteamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TeachteamServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		System.out.println("------teachteam.html----");
		PrintWriter writer = response.getWriter();
		UserService userService = new UserService();
		User user = null;
		Map<String, Object> map  = new HashMap<String, Object>();
		TeamRecordService teamRecordService=new TeamRecordService();
		ClubRecordService ClubRecordService=new ClubRecordService();
		int userId = -1;
		//前端获取传入的data
		String userid = null;
		if (request.getParameter("userid") != null) {
			userid = request.getParameter("userid");
			userId = Integer.parseInt(userid);
		} else {
			map.put("userid", "null");
		}
		
		user = userService.queryUserByUserId(userId);
		if (user != null) {//插入成功
			System.out.println("找到当前用户" + user);
			List<TeamRecord> teamRecords=teamRecordService.queryTeamRecordByUserId(userId);
			if(teamRecords!=null){
				map.put("teamRecords", teamRecords);
			}else{
				System.out.println("未找到执教俱乐部记录");
			}
			List<ClubRecord> clubRecords=ClubRecordService.queryClubRecordByUserId(userId);
			if(clubRecords!=null){
				map.put("clubRecords", clubRecords);
			}else{
				System.out.println("未找到执教俱乐部记录");
			}
			//处理成功返回result=0	
			map.put("result", "0");
			map.put("userId", userId);
			map.put("userid", userId);
			System.out.println("map找到啦..." + map);
		} else {
			System.out.println("map未找到...");
			//第一次登陆返回result=1
			map.put("result", "-1");
		}
		//将转换得到的map转换为json并返回
		ObjectMapper objectMapper = new ObjectMapper();
		String resultJson = objectMapper.writeValueAsString(map);
		//此处直接返回JSON object对象，JSP可直接使用data.key
		resultJson = resultJson.replace("\"", "\\\"");
		resultJson = "\"" + resultJson + "\"";
		//此处返回JSON 字符串 string对象;JSP需要解析才能使用data.key
		System.out.println("resultJson ..." + resultJson);
		writer.print(resultJson);
		writer.flush();
		writer.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
