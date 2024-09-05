package com.example.demo.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.formLogin(form -> form
				.loginPage("/login") // ログイン画面のURL
				.loginProcessingUrl("/authenticate")// ユーザー名・パスワードの送信先URL
				.defaultSuccessUrl("/calendar",true) // ログイン成功後のリダイレクト先URL
				.failureUrl("/login?failure") // ログイン失敗後のリダイレクト先URL
				.usernameParameter("mailAddress") // ログイン画面のユーザー名のフィールド
				.permitAll() // ログイン画面は未ログインでもアクセス可能
		).logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/logout") // ログアウト成功後のリダイレクト先URL
				.permitAll()
		).authorizeHttpRequests(authz -> authz
				.requestMatchers("/login").permitAll()// url
				.requestMatchers("/logout").permitAll()// url
				.requestMatchers("/register").permitAll()// url
				.requestMatchers("/confirm").permitAll()// url
				.requestMatchers("/videos/**").permitAll()// url
				.requestMatchers("/image/**").permitAll()// url
				.requestMatchers("/success").permitAll()// url
				.requestMatchers("/css/**").permitAll()
				.anyRequest().authenticated() // 他のURLはログイン後のみアクセス可能

		);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		//System.out.println(new BCryptPasswordEncoder().encode());
		return new BCryptPasswordEncoder();
	}

	// ログイン失敗したときに呼び出すメソッド
	private AuthenticationFailureHandler failureHandler() {
		return new AuthenticationFailureHandler() {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				// ログイン画面からフォームの値を取得
				String userName = request.getParameter("userName");
				String userPass = request.getParameter("userPass");

				// セッションでデータを渡す
				HttpSession session = request.getSession();

				session.setAttribute("userName", userName);
				session.setAttribute("userPass", userPass);

				// 失敗したときに飛ぶURL
				response.sendRedirect("/login?failure");

			}
		};
	}

}//
