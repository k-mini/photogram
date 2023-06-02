package com.cos.photogramstart.web;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {

	private final ImageService imageService;
	
	@GetMapping({"/","/image/story"})
	public String story() {
		return "image/story";
	}

	// API 구현한다면 - 이유 - (브라우저에서 요청하는게 아니라, 안드로이드, IOS 에서 요청하게되면)
	@GetMapping("/image/popular")
	public String popular(Model model ) {
		// api는 데이터를 리턴하는 서버!!
		List<Image> images = imageService.인기사진();
		
		model.addAttribute("images",images);
		
		return "image/popular";
	}
	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}
	
	@PostMapping("/image")
	public String imageUplodate(ImageUploadDto imageUploadDto,  @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		if (imageUploadDto.getFile().isEmpty()) {			throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);
		}
		
		imageService.사진업로드(imageUploadDto, principalDetails);
		System.out.println("=======================사진업로드 완료");
		return "redirect:/user/" + principalDetails.getUser().getId();
	}
}






