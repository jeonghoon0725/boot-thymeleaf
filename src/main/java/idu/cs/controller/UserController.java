package idu.cs.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import idu.cs.domain.User;
import idu.cs.exception.ResourceNotFoundException;
import idu.cs.repository.UserRepository;

@Controller //@Component 가장 일반적, @Service, @Repository
// Spring Framework에게 이 클래스로부터 작성된 객체는 Controller 역할을 함을 알려줌
// Spring 이 이 클래스로 부터 Bean 객체를 생성해서 등록할 수 있음
public class UserController {
	@Autowired UserRepository userRepo; // Dependency Injection

	@GetMapping("/")	//첫 화면
	public String home(Model model) {
		return "index";
	}
	@GetMapping("/user-login-form")		//로그인
	public String getLoginForm(Model model) {
		return "login";
	}
	@PostMapping("/login")		//로그인
	public String loginUser(@Valid User user, HttpSession session ) {
		System.out.println("login process : " + user.getUserId());
		User sessionUser = userRepo.findByUserId(user.getUserId());
		if(sessionUser == null) {
			System.out.println("id error : ");
			return "redirect:/user-login-form";
		}
		if(!sessionUser.getUserPw().equals(user.getUserPw())) {
			System.out.println("pw error : ");
			return "redirect:/uesr-login-form";
		}
		session.setAttribute("user", sessionUser);
		return "redirect:/";
	}
	@GetMapping("/logout")
	public String logoutUser(HttpSession session) {
		session.removeAttribute("user");
		//session.invalidate();  세션에 남아있는 모든 데이터를 삭제
		return "redirect:/";
	}
	@GetMapping("/user-register-form")		//회원가입
	public String getRegForm(Model model) {
		return "register";
	}
	@GetMapping("/users")					//회원목록 보기
	public String getAllUser(Model model) {
		model.addAttribute("users", userRepo.findAll());
		return "userlist";
	}
	@PostMapping("/users")					//회원 등록
	public String createUser(@Valid User user, Model model) {
		if(userRepo.save(user) != null)
			System.out.println("Database 등록 성공");
		else 
			System.out.println("Database 등록 실패");
			
		model.addAttribute("users", userRepo.findAll());
		return "redirect:/users";
	}
	
	
	@GetMapping("/users/{id}")
	public String getUserById(@PathVariable(value = "id") Long userId, Model model, HttpSession session)	throws ResourceNotFoundException {
		User user = userRepo.findById(userId).get();
		model.addAttribute("user", user);
		return "user";
	}
	@PutMapping("/users/{id}") //@PatchMapping 수정한 부분만 업데이트시킬 수 있는 매핑
	public String updateUser(@PathVariable(value = "id") Long userId, @Valid User userDetails, HttpSession session) {
		User user = userRepo.findById(userId).get();//DB로 부터 읽어온 객체
		user.setUserId(userDetails.getUserId());
		user.setUserPw(userDetails.getUserPw());
		user.setName(userDetails.getName());// userDetails는 전송한 객체
		user.setCompany(userDetails.getCompany());
		userRepo.save(user);
		return "redirect:/users"; //redirect는 페이지를 바꾼다.  users로 가겠다
	}
	
	
	/* 
	@GetMapping("/users/{id}")
	public String getUserById(@PathVariable(value = "id") Long userId, Model model)	throws ResourceNotFoundException {
		User user = userRepo.findById(userId).get();//.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		model.addAttribute("user", user);
		return "user";
		//return ResponseEntity.ok().body(user);
	}
	@GetMapping("/users/fn")
	public String getUserByName(@Param(value="name") String name, Model model)
			throws ResourceNotFoundException {
		List<User> users = userRepo.findByName(name);//.orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		model.addAttribute("users", users);
		return "userlist";
		//return ResponseEntity.ok().body(user);
	}
	@PutMapping("/users/{id}") //@PatchMapping 수정한 부분만 업데이트시킬 수 있는 매핑
	public String updateUser(@PathVariable(value = "id") Long userId, @Valid User userDetails, Model model) {
		User user = userRepo.findById(userId).get();//DB로 부터 읽어온 객체
		user.setName(userDetails.getName());// userDetails는 전송한 객체
		user.setCompany(userDetails.getCompany());
		userRepo.save(user);
		return "redirect:/users"; //redirect는 페이지를 바꾼다.  users로 가겠다
	}
	@DeleteMapping("/users/{id}")
	public String deleteUser(@PathVariable(value = "id") Long userId, Model model) {
		User user = userRepo.findById(userId).get();
		userRepo.delete(user);
		model.addAttribute("name", user.getName());
		return "user-deleted";
	}
	*/
}
