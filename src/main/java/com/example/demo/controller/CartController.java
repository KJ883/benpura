package com.example.demo.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.dto.CartDto;
import com.example.demo.entity.Order;
import com.example.demo.service.SettlementServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("")

public class CartController {
	private HttpSession session;
	@Autowired
	SettlementServiceImpl service;

	@Autowired
	public CartController(HttpSession session) {
		// フィールドに代入する
		this.session = session;
	}

	@GetMapping("/cart")
	public String cartShowGet(Model model) {
		List<CartDto> dtoList = (List<CartDto>) this.session.getAttribute("dtoList");
		model.addAttribute("dtoList", dtoList);
		return "settlement/cart";
	}
	@PostMapping("/cart")
	public String cartShow(@ModelAttribute CartDto dto,Model model) {
		List<CartDto> dtoList = (List<CartDto>) this.session.getAttribute("dtoList");
		if(dtoList == null) {
			dtoList = new ArrayList<>();
		}
		
		dtoList.add(dto);
		this.session.setAttribute("dtoList", dtoList);
			
		
		model.addAttribute("dtoList",dtoList);
		return "redirect:/cart";
	}
	@PostMapping("/settlement")
	public String showSettlement(@ModelAttribute CartDto dto,Model model) {
		dto.setNumber(1);
		dto.setShopName(this.session.getAttribute("shopName").toString());
		dto.setPickupTime((LocalDateTime) this.session.getAttribute("orderdatetime"));
		LocalDate today = LocalDate.now();
		model.addAttribute("today", today);
		model.addAttribute("reservations", today);
		model.addAttribute("dto",dto);
		model.addAttribute("shopId",this.session.getAttribute("shopId"));
		return "settlement/settlement";
	}
	@PostMapping("/comp")
	public String compShow(@RequestParam int bn) {
		List<CartDto> list =(List<CartDto>) this.session.getAttribute("dtoList");
		CartDto dto = list.get(bn);
		Order orderDetails = new Order(null,this.session.getAttribute("mailaddress").toString(), dto.getShopName(), dto.getName(), dto.getPrice(), dto.getPickupTime());
		service.insertOrder(orderDetails);
		return "settlement/comp";
	}
	@PostMapping("/delete")
	 //delete用リクエスト受ける
	    public String deleteItem(@RequestParam int bn) {
	        List<CartDto> list =(List<CartDto>) this.session.getAttribute("dtoList");
	        list.remove(bn);
	        this.session.setAttribute("dtoList", list);
	        
	        return "settlement/deleteSuccess";
	    }
	
	@GetMapping("comp")
	public String showComp() {
		return "settlement/comp";
	}
	
	
//	@GetMapping("/reservations")
//    public String getReservations(Model model) {
//        Map<LocalDate, String> reservations = new HashMap<>();
//        // ここで予約情報を取得し、Mapに追加します
//        // 例: reservations.put(LocalDate.of(2024, 8, 12), "予約あり");
//
//        LocalDate today = LocalDate.now();
//        model.addAttribute("today", today);
//        model.addAttribute("reservations", reservations);
//        return "reservations";
//    }
}
