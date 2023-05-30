package com.cos.photogramstart.service;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	@Transactional
	public User 회원프로필(int userId) {
		// SELECT * FROM image WHERE userId = :userId;
		System.out.println("회원 프로필 시작===============");
		User userEntity = userRepository.findById(userId).orElseThrow(()->{
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});
		
		System.out.println("==========================================");
		//System.out.println(userEntity.getImages().size());
		//System.out.println(userEntity.getImages());
		return userEntity;
	}
	
	@Transactional
	public User 회원수정(int id, User user) {
		// 1. 영속화
		User userEntity = userRepository.findById(id).orElseThrow(() ->{return new CustomValidationApiException("찾을 수 없는 id입니다.");});
 		
 		// 2. 영속화된 오브젝트를 수정 -> 더티체킹 (업데이트 완료)
		userEntity.setName(user.getName());
 		
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		
		userEntity.setPassword(encPassword);
 		userEntity.setBio(user.getBio());
 		userEntity.setWebsite(user.getWebsite());
 		userEntity.setPhone(user.getPhone());
 		userEntity.setGender(user.getGender());
		
		return userEntity;
	} // 더티체킹 발생 -> 업데이트 완료
}
