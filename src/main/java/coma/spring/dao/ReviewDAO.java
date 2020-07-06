package coma.spring.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import coma.spring.dto.ReviewDTO;

@Repository
public class ReviewDAO {
	@Autowired
	private SqlSessionTemplate mybatis;
	
	public int insert(ReviewDTO rdto) throws Exception{
		return mybatis.insert("Review.insert",rdto);
	}
	public List<ReviewDTO> selectByPseq(int parent_seq) throws Exception{
		return mybatis.selectList("Review.selectByPseq",parent_seq);
	}
}