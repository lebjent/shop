package com.shop.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Service
@Log
public class FileService {
	
	
	//파일 업로드를 하는 메서드
	public String uploadFile(String uploadPath,String orginalFileName, byte[]fileData)throws Exception{
		
		UUID uuid = UUID.randomUUID();//서로 다른 개체들을 구별하기 위해서 이름을 부여할때 사용
		
		String extension = orginalFileName.substring(orginalFileName.lastIndexOf("."));
		String savedFileName = uuid.toString() + extension; //UUID로 받은 값과 원래 파일의 이름의 확장자를 조합해서 지정할 파일이름을 만든다.
		
		String fileUploadFullUrl = uploadPath + "/" + savedFileName;
		
		//FileOutputStream 클래스는 바이트 단위의 출력을 내보내는 클래스
		FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
		
		fos.write(fileData); //파일 데이터를 출력스트림에 입력
		fos.close();
		
		return savedFileName;//업로드된 파일이름을 반환
	
	}
	
	//파일 삭제
	public void deleteFile(String filePath) throws Exception{
		File deleteFile = new File(filePath);//파일이 저장된 경로를 이용하여 파일객체를 생성
		
		//해당 파일이 존재하면 파일을 삭제
		if(deleteFile.exists()) {
			deleteFile.delete();
			log.info("파일을 삭제 하였습니다.");
		}else {
			log.info("파일이 존재하지 않습니다.");
		}
		
	}
	
}
