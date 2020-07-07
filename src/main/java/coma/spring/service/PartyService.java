package coma.spring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coma.spring.dao.PartyDAO;
import coma.spring.dto.PartyDTO;
import coma.spring.dto.PartySearchListDTO;
import coma.spring.statics.PartyConfiguration;


@Service
public class PartyService {
	@Autowired
	private PartyDAO pdao;
	
	public int partyInsert(PartyDTO dto) {
		int seq = pdao.getNextVal();
		dto.setSeq(seq);
		pdao.insert(dto);
		return seq;
	}
	
	
	public PartyDTO selectBySeq(int seq) throws Exception {
		PartyDTO dto = pdao.selectBySeq(seq); // 읽어오기
		return dto;
	}
	

	public int delete(String seq) throws Exception{
		int result=pdao.delete(seq);
		return result;
	}
	
	public int update(PartyDTO dto) throws Exception{
		int result= pdao.update(dto);
		return result;
	}
	
	// 태훈 그냥 모임 글 보기
//	public List<PartyDTO> selectList() throws Exception {
//		List<PartyDTO> list = pdao.selectList();
//		return list;
//	}
	// 태훈 모임 20개씩
	public List<PartyDTO> selectList(int cpage) throws Exception {
		List<PartyDTO> list = pdao.selectList(cpage);
		return list;
	}
	// 태훈 페이지 네비
	public String getPageNaviTH(int currentPage) throws Exception{
		int recordTotalCount = pdao.getListCount(); 
		int pageTotalCount = 0; 
		if(recordTotalCount % PartyConfiguration.SEARCH_COUNT_PER_PAGE > 0) {
			pageTotalCount = recordTotalCount / PartyConfiguration.SEARCH_COUNT_PER_PAGE + 1;			
		}else {
			pageTotalCount = recordTotalCount / PartyConfiguration.SEARCH_COUNT_PER_PAGE;
		}
		if(currentPage < 1) {
			currentPage = 1;
		}else if(currentPage > pageTotalCount) {
			currentPage = pageTotalCount;
		}
		int startNavi = (currentPage - 1) / PartyConfiguration.NAVI_COUNT_PER_PAGE * PartyConfiguration.NAVI_COUNT_PER_PAGE + 1;
		int endNavi = startNavi + PartyConfiguration.NAVI_COUNT_PER_PAGE - 1;
		if(endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		}
		boolean needPrev = true; // <
		boolean needNext = true; // >
		StringBuilder sb = new StringBuilder();
		if(startNavi == 1) {needPrev = false;}
		if(endNavi == pageTotalCount) {needNext = false;}

		if(needPrev) {
			sb.append("<li class='page-item'><a class='page-link' href='partylistBy?cpage="+(startNavi-1)+" aria-label=\"Previous\"> <span aria-hidden=\"true\">&laquo;</span> </a></li>");
		}
		else {
			sb.append("<li class='page-item disabled'><a class='page-link' aria-label=\"Previous\"> <span aria-hidden=\"true\">&laquo;</span> </a></li>");
		}
		for(int i = startNavi;i <= endNavi;i++) {
			if(currentPage == i) {
				sb.append("<li class='page-item active' aria-current=\"page\"><span class=\"page-link\">" + i +"<span class=\"sr-only\">(current)</span></span></li>");
			}
			else {
				sb.append("<li class='page-item'><a class='page-link' href=\"partylist?cpage="+i+"\">" + i + "</a></li>");
			}	
		}
		if(needNext) {
			sb.append("<li class='page-item'><a class='page-link' href='partylistBy?cpage="+(endNavi-1)+" aria-label=\"Next\"> <span aria-hidden=\"true\">&raquo;</span> </a></li>");
		}
		else {
			sb.append("<li class='page-item disabled'><a class='page-link' aria-label=\"Next\"> <span aria-hidden=\"true\">&raquo;</span> </a></li>");
		}
		
		return sb.toString();
	}
	// 태훈 모임 글 통합 검색
	public List<PartyDTO> partySearch(PartySearchListDTO pdto) throws Exception{	
		List<PartyDTO> list = pdao.partySearch(this.searchKey(pdto));
		return list;
	}
	// 태훈 검색 키워드 가공
	public Map<String, Object> searchKey(PartySearchListDTO pdto) throws Exception{
		
		Map<String, Object> param = new HashMap<
				>();
	 
		// 지역 정보
		if(pdto.getSido().equals("시/도 선택")) {
			param.put("address", "");
		}
		else {
			param.put("address",pdto.getSido() + " " + pdto.getGugun());
		}
		// 성별 정보
		param.put("gender",pdto.getGender());
		// 나이 정보
		List<String> ageList = new ArrayList<String>();
		if(pdto.getAge() != null) {
			for(int i=0; i<pdto.getAge().size();i++) {
				ageList.add(Integer.toString(pdto.getAge().get(i)));
			}
		}
		param.put("ageList.size", ageList.size());
		param.put("ageList", ageList);
		// 음주 정보
		param.put("drinking",pdto.getDrinking());
		// 키워드 검색
		String title = "", writer = "", content = "", both = ""; 
		if(pdto.getText().equals("title")) {
			title = pdto.getSearch();
			
		}
		else if(pdto.getText().equals("writer")){
			writer = pdto.getSearch();
			System.out.println("W"+writer);
		}
		else if(pdto.getText().equals("content")) {
			content = pdto.getSearch();
			System.out.println("C"+content);
		}
		else if(pdto.getText().equals("both")) {
			both = pdto.getSearch();
		}
		param.put("title", title);
		param.put("writer", writer);
		param.put("content", content);
		param.put("both", both);

		return param;
	}
	
	public Map<String,String> partyCountById(){
		List<String> list =  pdao.partyCountById();
		Map<String, String> param = new HashMap<>();
		for (int i=0; i<5; i++) {
			param.put("top"+(i+1),list.get(i));
		}
		return param;
	}
	// 예지 
	public List<PartyDTO> selectByPlace_id(int place_id) throws Exception{
		List<PartyDTO> result = pdao.selectByPlace_id(place_id);
		return result;
	}
	// 예지 페이지 글 10개 씩
	public List<PartyDTO> selectByPageNo(int cpage, int place_id) throws Exception{
		return pdao.selectByPageNo(cpage, place_id);
	}
	// 예지 페이지 네비
	public String getPageNavi(int currentPage, int place_id) throws Exception{
		int recordTotalCount = pdao.getArticleCount(place_id); 
		int pageTotalCount = 0; 
		if(recordTotalCount % PartyConfiguration.RECORD_COUNT_PER_PAGE > 0) {
			pageTotalCount = recordTotalCount / PartyConfiguration.RECORD_COUNT_PER_PAGE + 1;			
		}else {
			pageTotalCount = recordTotalCount / PartyConfiguration.RECORD_COUNT_PER_PAGE;
		}
		if(currentPage < 1) {
			currentPage = 1;
		}else if(currentPage > pageTotalCount) {
			currentPage = pageTotalCount;
		}
		int startNavi = (currentPage - 1) / PartyConfiguration.NAVI_COUNT_PER_PAGE * PartyConfiguration.NAVI_COUNT_PER_PAGE + 1;
		int endNavi = startNavi + PartyConfiguration.NAVI_COUNT_PER_PAGE - 1;
		if(endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		}
		boolean needPrev = true; // <
		boolean needNext = true; // >
		if(startNavi == 1) {needPrev = false;}
		if(endNavi == pageTotalCount) {needNext = false;}

		StringBuilder sb = new StringBuilder();
		if(needPrev) {sb.append("<li class='page-item'><a class='page-link' href='selectMarkerInfo?cpage="+(startNavi-1)+"' tabindex='-1' aria-disabled='true'><i class=\"fas fa-chevron-left\"></i> </a></li>");}
		for(int i = startNavi;i <= endNavi;i++) {
			sb.append("<li class='page-item'><a class='page-link' href='selectMarkerInfo?cpage="+i+"&place_id="+place_id+"'>" + i + "</a></li>");
		}
		if(needNext) {sb.append("<li class='page-item'><a class='page-link' href='selectMarkerInfo?cpage="+(endNavi+1)+"'><i class=\"fas fa-chevron-right\"></i></a></li>");}
		return sb.toString();
	}
	// 예지 음식점 이미지 클롤링
	public String clew(String str) throws Exception{
		return pdao.clew(str);
	}
	// 예지
	public int stopRecruit(String seq) throws Exception {
		return pdao.stopRecruit(seq);
	}
}
