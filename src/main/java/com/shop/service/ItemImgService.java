package com.shop.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
	
	@Value("${itemImgLocation}")//application.properties에서 경로를 가져온다.
	private String itemImgLocation;
	
	private final ItemImgRepository itemImgRepository;
	
	private final FileService fileService;
	
	public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile)throws Exception{
		
		String oriImgName = itemImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		
		//파일 업로드
		if(!StringUtils.isEmpty(oriImgName)) {
			//바이트 배열을 파일 업로드 파라미터로 uploadFile메소드를 호출 , 호출결과 로컬에 저장된 파일이름을 imgName에 저장
			imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			
			imgUrl = "/images/item/" + imgName;
		}
		
		//상품 이미지 정보 저장
		
		/* 매개변수 설명
		 * imgName : 실제 로컬에 저장된 상품 이미지 파일의 이름 (uuid+파일이름) 
		 * oriImgName : 업로드 했던 상품 이미지 파일의 원래 이름
		 * imgUrl : 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
		 */
		itemImg.updateItemImg(oriImgName, imgName, imgUrl);
		itemImgRepository.save(itemImg);
		
	}
	
	//상품 이미지 수정
	public void updateItemImg(Long itemImgId,MultipartFile itemImgFile) throws Exception {
		//파일이 없지 않은 경우
		if(!itemImgFile.isEmpty()) {
			ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);
			
			//기존의 이미지 파일 삭제
			if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
				fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
			}
			
			String oriImgName = itemImgFile.getOriginalFilename();//파일 원래 이름
			String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); //업데이트한 상품이미지를 업로드
			
			String imgUrl = "/images/item/"+imgName;
			
			savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
			
		}
	}
	
	
}
