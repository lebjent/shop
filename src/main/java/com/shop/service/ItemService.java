package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
	
	private final ItemRepository itemRepository;
	
	private final ItemImgService itemImgService;
	
	private final ItemImgRepository itemImgRepository;
	
	public Long saveItem(ItemFormDto itemFormDto,List<MultipartFile> itemImgFileList)throws Exception {
		//상품등록
		Item item = itemFormDto.createItem();
		itemRepository.save(item);
		
		//이미지 등록
		for(int i=0; i<itemImgFileList.size();i++) {
			ItemImg itemImg = new ItemImg();
			itemImg.setItem(item);
			//첫번쨰 등록한 이미지가 대표이미지로 저장된다.
			if(i==0)
				itemImg.setRepimgYn("Y");
			else
				itemImg.setRepimgYn("N");
			
			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
			
		}
		
		return item.getId();
		
	}
	
	//상품을 불러오는 함수
	@Transactional(readOnly = true) //트랜잭션을 읽기전용으로 설정
	public ItemFormDto getItemDtl(Long itemId) {
		//해당 상품의 이미지를 오름차순으로 가져온다.
		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
		
		//상품정보를 담일 리스트
		List<ItemImgDto> itemImgDtoList = new ArrayList<>();
		
		//테이블조회 결과를 DTO에담아 이미지 리스트에 추가해준다.
		for(ItemImg itemImg : itemImgList) {
			ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
			System.out.println(itemImgDto);
			itemImgDtoList.add(itemImgDto);
		}
		//상품 아이디를 통해 상품의 엔티티를 조회한다.
		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
		//상품형식에 맞는 DTO에 상품엔티티결과를 넣어준다.
		ItemFormDto itemFormDto = ItemFormDto.of(item);
		//조회한 상품 이미지 리스트를 넣어준다.
		itemFormDto.setItemImgDtoList(itemImgDtoList);
		
		//최종적으로 반환
		return itemFormDto;
	}
	
	//상품을 수정하는 함수
	public Long updateItem(ItemFormDto itemFormDto,List<MultipartFile> itemImgFileList)throws Exception{
		
		//상품 수정
		Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		System.out.println(item);
		item.updateItem(itemFormDto);
		
		List<Long> itemImgIds = itemFormDto.getItemImgIds();
		System.out.println(itemImgIds);
		//이미지 등록
		for(int i=0; i<itemImgFileList.size();i++) {
			System.out.println(itemImgIds.get(i));
			itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
		}
		return item.getId();
	}
	
	//상품 관리 페이지 상품정보
	@Transactional(readOnly = true)
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto,Pageable pageable){
		
		return itemRepository.getAdminItemPage(itemSearchDto, pageable);
		
	}
	
	//상품 메인페이지 가져오기
	@Transactional(readOnly = true)
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
		return itemRepository.getMainItemPage(itemSearchDto, pageable);
		
	}
	
}
