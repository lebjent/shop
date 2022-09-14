package com.shop.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {
	
	//상품을 등록하기 위한 서비스 등록
	private final ItemService itemService;
	
	
	//상품등록 페이지 이동
	@GetMapping(value = "/admin/item/new")
	public String itemForm(Model model) {
		model.addAttribute("itemFormDto", new ItemFormDto());
		return "/item/itemForm";
		
	}
	
	//상품등록
	@PostMapping(value = "/admin/item/new")
	public String itemNew(@Valid ItemFormDto itemFormDto,BindingResult bindingResult,Model model,
			@RequestParam("itemImgFile")List<MultipartFile>itemImgFileList) {
		
		if(bindingResult.hasErrors()) {
			return "item/itemForm";
		}
		
		if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId()== null) {
			model.addAttribute("errorMessage", "첫번째 이미지는 필수 입력 값입니다.");
			return "item/itemForm";
		}
		
		try {
			itemService.saveItem(itemFormDto, itemImgFileList);
		}catch (Exception e) {
			model.addAttribute("errirMessage","상품 등록 중 에러가 발생하였습니다.");
			return "item/itemForm";
		}
		
		return "redirect:/";
		
	}
	
	//상품 수정하기시 상품 정보를 Model에 담아주기
	@GetMapping(value = "/admin/item/{itemId}")
	public String itemDtl(@PathVariable("itemId")Long itemId, Model model) {
		
		//조회한 상품 데이터를 모델에 담아서 뷰로 전달
		try {
			ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
			model.addAttribute("itemFormDto", itemFormDto);
		} catch (EntityNotFoundException e) {
			model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
			model.addAttribute("itemFormDto", new ItemFormDto());
			return "item/itemForm";
		}
		return "item/itemForm";
	}
	
	//상품수정 업데이트 처리
	@PostMapping(value = "/admin/item/{itemId}")
	public String itemUpdate(@Valid ItemFormDto itemFormDto,BindingResult bindingResult
			,@RequestParam("itemImgFile")List<MultipartFile>itemImgFIleList,Model model) {
		
		if(bindingResult.hasErrors()) {
			return "item/itemForm";
		}
		
		//파일리스트 첫번째에 파일이 없을경우 에러메시지 발생
		if(itemImgFIleList.get(0).isEmpty()&& itemFormDto.getId()==null) {
			model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
			return "item/itemForm";
		}
		
		//상품 수정을 시도 에러가 발생하면 에러 메시지 발생
		try {
			itemService.updateItem(itemFormDto, itemImgFIleList);
		} catch (Exception e) {
			model.addAttribute("errorMessage","상품 수정 중 에러가 발생 하였습니다.");
			return "item/itemForm";
		}
		return "redirect:/";
	
	}
	
	//상품 관리 페이지 이동
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
    	
    	//상품 노출 개수를 나타내주는 기능 - 우선 3개로 조정
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);//최대 페이지 개수

        return "item/itemMng";
    }
	
    //상품 상세보기
    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId")Long itemId) {
    	ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
    	model.addAttribute("item", itemFormDto);
    	return "item/itemDtl";
    }
    
    
}