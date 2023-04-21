package mul.cam.a.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mul.cam.a.dto.LectureDto;
import mul.cam.a.dto.LectureParam;
import mul.cam.a.service.LectureService;

@RestController
public class LectureController {

	@Autowired
	LectureService service;
	
	@ResponseBody
	@GetMapping(value="lecturelist")
	public List<LectureDto> lectureList(LectureParam param) {
		 System.out.println("LectureController lectureList " + new Date());
		
		 List<LectureDto> lecturelist = service.lectureList(param);
		
		 // System.out.println(list);
		return lecturelist;

	}
	
	@ResponseBody
	@PostMapping(value="writeLecture")
	public String writeLecture(LectureDto bbs) {
		System.out.println("LectureController writeLecture " + new Date());
		
		boolean b = service.writeLecture(bbs);
		
		if(b == false) {
			return "NO";
		}

		return "YES";
	}
	

	

}
