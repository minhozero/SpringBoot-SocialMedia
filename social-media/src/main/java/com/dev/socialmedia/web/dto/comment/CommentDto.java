package com.dev.socialmedia.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

// @NotNull = null값 체크
// @NotEmpty =  빈값이나 null 체크
// @NotBlank = 빈값이나 null 체크 그리고 빈공백(스페이스까지)

@Data
public class CommentDto {
	@NotBlank
	private String content;
	@NotNull
	private Integer imageId;
	
	// toEntity 가 필요 없다.
}
