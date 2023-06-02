package com.cos.photogramstart.service;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {

	private final ImageRepository imageRepository;
	private final UserRepository userRepository;
	
	@Transactional(readOnly = true)
	public List<Image> 인기사진(){
		return imageRepository.mPopular();
	}
	
	@Transactional(readOnly = true)  // 영속성 컨텍스트 변경 감지를 해서, 변경되면 더티체킹해서 flush => readonly는 이것을 안함
	public Page<Image> 이미지스토리(int principalId, Pageable pageable){
		Page<Image> images = imageRepository.mStory(principalId,pageable);
		System.out.println("이미지 스토리 정보 가져옴 ============================");
		// 2(cos) 로그인
		// images에 좋아요 상태 담기
		images.forEach((image)->{
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like)->{
				if (like.getUser().getId() == principalId) {
					image.setLikeState(true);
				}
			});
			
		});
		
		return images;
	}
	
	@Value("${file.path}")  // application.yml 의 값
	private String uploadFolder;
	@Transactional
	public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID();
		// ccd8878d-f55f-4404-a6c3-55986cd54616 + _  + 1.jpg
		String imageFileName = uuid + "_" + imageUploadDto.getFile().getOriginalFilename();
		System.out.println("이미지 파일이름" + imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder + imageFileName);
		// 통신, I/O -> 예외가 발생할 수 있다.
		try {
				Files.write(imageFilePath, imageUploadDto.getFile().getBytes() );
				//FileOutputStream fos = new FileOutputStream(uploadFolder+"test");
				//fos.write(imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// image 테이블에 저장
		Image image = imageUploadDto.toEntity(imageFileName,principalDetails.getUser()); //ccd8878d-f55f-4404-a6c3-55986cd54616_1.jpg)
		imageRepository.save(image);
		
		//System.out.println(imageEntity);
	}
}




