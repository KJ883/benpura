package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.form.LoginForm;

import jakarta.servlet.http.HttpSession;

@Controller

public class LoginController {
	@Autowired
	private SmartValidator validator;

	// SecurityConfigのloginPageで指定したURL
	@GetMapping("/login") //local host url
	public String loginForm(LoginForm loginForm) {
		// ログイン画面を表示
		
		return "login";//html file
	}

	// SecurityConfigのfailureUrlで指定したURLと?のうしろのパラメータ
	@GetMapping(value = "/login", params = "failure")
	public String loginFail(LoginForm loginForm, BindingResult result, Model model, HttpSession session) {
		//空白や間違ってる時のメッセージ↓↓↓
		model.addAttribute("failureMessage", "ID又はPASSWORDが失敗しました");

		String userName = (String) session.getAttribute("userName");
		String userPass = (String) session.getAttribute("userPass");

		loginForm.setMailAddress(userName);
		loginForm.setPassword(userPass);

		validator.validate(loginForm, result);
		
		// ログイン画面を表示
		return "login";
	}
	 @GetMapping("/logout")
	    public String logout() {
	        return "/logout.html"; // ログアウト後のリダイレクト先
	    }

	//	// SecurityConfigのdefaultSuccessUrlで指定したURL
	//	@GetMapping("calendar") //local host url
	//	public String loginSuccess() {
	//		// ログインに成功したら表示するURL
	//		return "calendar";//html file
	//	}

}
