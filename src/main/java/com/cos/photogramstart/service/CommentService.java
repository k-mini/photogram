package com.cos.photogramstart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public Comment 댓글쓰기(String content, int imageId, int userId) {
		
		// Tip (객체를 만들 때 id값만 담아서 insert 할 수 있다)
		// 대신 return 시에 image객체와 user객체는 id값만 가지고 있는 빈 객체를 리턴 받는다.
		Image image = new Image();
		image.setId(imageId);
		
		User userEntity = userRepository.findById(userId).orElseThrow(()->{
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
		});
//		User user = new User();
//		user.setId(userId);
//		user.setUsername("umaru2");
//		user.setName("우마루22");
//		user.setPassword("1234");
//		user.setEmail("um@nate.com");
//		System.out.println("userENtity : "+System.identityHashCode(userEntity));
//		System.out.println("user : " +System.identityHashCode(user));
//		System.out.println("Username : " + user.getUsername());
//		System.out.println("Name : " + user.getName());
//		System.out.println("Password : " + user.getPassword());
//		System.out.println("Email : " + user.getEmail());
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		comment.setUser(userEntity);
		System.out.println("save 이전 comment : "+System.identityHashCode(comment));
		comment = commentRepository.save(comment);
		System.out.println("save 이후 comment : "+System.identityHashCode(comment));
//		comment.getUser().setName("우마루2");
//		System.out.println(System.identityHashCode(comment.getUser()));
//		System.out.println("Username : " + comment.getUser().getUsername());
//		System.out.println("Name : " + comment.getUser().getName());
//		System.out.println("Password : " + comment.getUser().getPassword());
//		System.out.println("Email : " + comment.getUser().getEmail());
		return comment;
	}
	
	@Transactional
	public void 댓글삭제(int id) {
		try {
			commentRepository.deleteById(id);
		} catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
		
		
	}
}
