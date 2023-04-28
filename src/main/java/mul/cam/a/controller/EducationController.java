package mul.cam.a.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import mul.cam.a.dto.AttendanceEdu;
import mul.cam.a.dto.CFR_User;
import mul.cam.a.dto.EducationDto;
import mul.cam.a.dto.ListParam;
import mul.cam.a.dto.MailDto;
import mul.cam.a.dto.MailParam;
import mul.cam.a.dto.TeacherUserDto;
import mul.cam.a.dto.UserDto;
import mul.cam.a.service.EducationService;
import mul.cam.a.util.RandomCode;

@RestController
public class EducationController {
	
	@Autowired
	EducationService service;
	
	@PostMapping(value="eduAdd")
	public String eduAdd(EducationDto edu) {
		System.out.println("EducationController eduAdd" + new Date());
		
		// 등록된 교육기관인지 확인
		boolean eduDuplicate = service.eduDuplicateCheck(edu);
		if(eduDuplicate) {
			return "duplicate";
		}		
		
		// 교육기관 코드생성
		String eduCode = new RandomCode().eduCode();
		boolean eduCodeChk = service.eduCodeCheck(eduCode);
		
		// 중복코드있으면 다시생성
		while(eduCodeChk) {
			eduCode = new RandomCode().eduCode();
			eduCodeChk = service.eduCodeCheck(eduCode);
		}
		
		// 학원 메인계정 아이디 생성
		UserDto admin = new UserDto();
		admin.setId("@" + eduCode);
		admin.setPassword(eduCode + "1234");
		admin.setName(edu.getEduName());
		admin.setAddress(edu.getEduAddress());
		admin.setPhone(edu.getEduPhone());
		// useredu에 넣기
		EducationDto useredu = new EducationDto();
		useredu.setEduCode(eduCode);
		useredu.setId("@" + eduCode);
		
		// 학원등록
		edu.setEduCode(eduCode);
		boolean isS = service.eduAdd(edu);
		if(isS) {
			// 학원등록 성공 시 메인계정아이디 등록
			boolean b = service.eduAddAdmain(admin);
			boolean u = service.userEduAdd(useredu);
			
			if(b && u) {
				return "success";
			} else {
				return "fail";
			}
		} else {
			return "fail";
		}
	}
	@GetMapping(value="edulist")
	public Map<String, Object> edulist(ListParam param) {
		System.out.println("EducationController edulist()" + new Date());
		
		// 글의 시작과 끝 
		int pn = param.getPageNumber(); // 0 1 2 3 4
		int start = (pn * 12);
		int end = (pn + 1) * 12;
		
		param.setStart(start);
		param.setEnd(end);
		
		System.out.println(param.getEnd());
		List<EducationDto> list = service.getEduList(param);
		
		int len = service.getAllEdu(param);
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("cnt", len);	// react에 보낼 때
		
		return map;
	}
	@GetMapping(value="getEdu")
	public EducationDto getEdu(String eduCode) {
		System.out.println("EducationController getEdu()" + new Date());
		return service.getEdu(eduCode);
	}
	
	@PostMapping(value="eduUpdate")
	public String eduUpdate(EducationDto edu) {
		System.out.println("EducationController update()" + new Date());

		// 등록된 교육기관인지 확인
		boolean eduDuplicate = service.eduDuplicateCheck(edu);
		if(eduDuplicate) {
			return "duplicate";
		}
		
		edu.setId("@"+edu.getEduCode());		
		boolean isS = service.eduUpdate(edu);
		
		if(isS) {
			return "success";
		}
		return "fail";
	}
	@PostMapping(value="eduDelete")
	public String eduDelete(EducationDto edu) {
		System.out.println("EducationController eduDelete()" + new Date());
		
		edu.setId("@"+edu.getEduCode());
		boolean isS = service.eduDelete(edu);
		
		if(isS) {
			return "success";
		}
		return "fail";
	}
	@GetMapping(value="getEduMailList")
	public List<EducationDto> getEduMailList(ListParam param) {
		System.out.println("EducationController getEduMailList()" + new Date());
		
		return service.getEduMailList(param);
	}
	@GetMapping(value="getIdMailList")
	public List<TeacherUserDto> getIdMailList(String id) {
		System.out.println("EducationController getIdMailList()" + new Date());
		
		return service.getIdMailList(id);
	}
	@GetMapping(value="getEduIdMailList")
	public List<TeacherUserDto> getEduIdMailList(String eduCode) {
		System.out.println("EducationController getEduIdMailList()" + new Date());
		System.out.println(eduCode);
		List<TeacherUserDto> id = service.getEduIdMailList(eduCode);
		System.out.println(id.toString());
		return id;
	}
	@GetMapping(value="sendMaillist")
	public Map<String, Object> sendMaillist(MailParam param) {
		System.out.println("EducationController sendMaillist()" + new Date());
		// 글의 시작과 끝
				int pn = param.getPageNumber();  // 0 1 2 3 4
				int start = pn * 10;	// 1  11
				int end = (pn + 1) * 10;	// 10 20 
				
				param.setStart(start);
				param.setEnd(end);
				
				List<MailDto> list = service.sendMaillist(param)
						.stream()
						.skip(param.getStart())
						.limit(param.getEnd())
						.collect(Collectors.toList());
				
				System.out.println(param.toString());
				System.out.println(list.toString());
				
				int len = service.getsendAllMail(param);
				
				System.out.println("len :" + len);
				
				Map<String, Object> map = new HashMap<>();
				map.put("list", list);
				map.put("cnt", len);

				return map;

	}
	@GetMapping(value="getSendMailDetail")
	public MailDto getSendMailDetail(String wdate) {
		System.out.println("EducationController sendMaillist()" + new Date());
		System.out.println(wdate);
		MailDto mail = service.getSendMailDetail(wdate);
		System.out.println(mail.toString());
		return mail;
	}
}

