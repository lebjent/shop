package com.shop.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@EntityListeners(value = {AuditingEntityListener.class})//Auditing을 적용하기 위해서 @EntityListner 어노테이션을 추가
@MappedSuperclass
@Getter
@Setter
public abstract class BaseTimeEntity {
	
	@CreatedDate//엔티티가 생성되어 저장될 때 시간을 자동으로 저장한다.
	@Column(updatable = false)
	private LocalDateTime regTime;
	
	@LastModifiedDate//엔티티의 값을 변경할 때 시간을 자동으로 저장한다.
	private LocalDateTime updateTime;
	
	
}
