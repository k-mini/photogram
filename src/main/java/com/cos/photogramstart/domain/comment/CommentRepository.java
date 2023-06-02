package com.cos.photogramstart.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

	// @Modifying는 comment 객체를 가져오지 못하고 행이 반영된 개수를 반환하는 int 값만 반환가능하다. 따라서 그냥 save를 써야함
	//@Modifying
	//@Query(value = "INSERT INTO comment(content, imageId, userId, createDate) VALUES(:content, :imageId, :userId, now())", nativeQuery = true)
	//public int  mSave(String content, int imageId , int userId);
}
