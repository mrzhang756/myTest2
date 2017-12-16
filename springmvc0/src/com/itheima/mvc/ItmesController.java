package com.itheima.mvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.itheima.pojo.Items;

public class ItmesController implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		List<Items> itemsList = new ArrayList<Items>();
		Items items1 = new Items();
		items1.setName("联想笔记本");
		items1.setPrice(6000f);
		items1.setDetail("ThinkPad T430 联想笔记本电脑！");
		
		Items items2 = new Items();
		items2.setName("戴尔笔记本");
		items2.setPrice(4500f);
		items2.setDetail("ThinkPad T430 戴尔笔记本电脑！");
		itemsList.add(items1);
		itemsList.add(items2);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("itemList", itemsList);
		//modelAndView.setViewName("/WEB-INF/jsp/itemList.jsp");
		modelAndView.setViewName("itemList");
		return modelAndView;
	}

}
