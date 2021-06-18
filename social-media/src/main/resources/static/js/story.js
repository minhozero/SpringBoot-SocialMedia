/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */

// (0) 현재 로그인한 사용자의 ID (header.jsp에 정의)
let principalId = $("#principalId").val();

// (1) 스토리 로드하기
let page = 0

function storyLoad() {
	$.ajax({
			url: `/api/image?page=${page}` ,
			dataType : "json"
		}).done(res=> { //httpStatus 상태코드 200번대
			console.log(res);
			res.data.content.forEach((image)=>{
				let storyItem = getStoryItem(image);
				$("#storyList").append(storyItem);
			});
		}).fail(error=>{ //httpStatus 상태코드 200번대가 아닐때.
			console.log("오류",error);
		});
}

storyLoad();

function getStoryItem(image) {
	let item = `<div class="story-list__item">`;
	item += `<div class="sl__item__header">`;
	item += `	<div>`;
	item += `		<img class="profile-image" src="/upload/${image.user.profileImageUrl}"`;
	item += `			onerror="this.src='/images/person.jpeg'" />`;
	item += `	</div>`;
	item += `	<div>${image.user.username}</div>`;
	item += `</div>`;
	item += `<div class="sl__item__img">`;
	item += `	<img src="/upload/${image.postImageUrl}" />`;
	item += `</div>`;
	item += `<div class="sl__item__contents">`;
	item += `	<div class="sl__item__contents__icon">`;
	item += `		<button>`;
		if(image.likeState){
			item += `		<i class="fas fa-heart active" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
		} else {
			item += `		<i class="far fa-heart" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
		}
	item += `		</button>`;
	item += `	</div>`;
	item += `	<span class="like"><b id="storyLikeCount-${image.id}">${image.likeCount} </b>likes</span>`;
	item += `	<div class="sl__item__contents__content">`;
	item += `		<p>${image.caption}</p>`;
	item += `	</div>`;
	item += `	<div id="storyCommentList-${image.id}">`;
	image.comments.forEach((comment)=>{
		item += `		<div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}">`;
		item += `			<p>`;
		item += `				<b>${comment.user.username} :</b> ${comment.content}`;
		item += `			</p>`;
		if(principalId == comment.user.id){
			item += `			<button onclick="deleteComment(${comment.id})">`;
			item += `				<i class="fas fa-times"></i>`;
			item += `			</button>`;
		}
		item += `		</div>`;
	});
	item += `	</div>`;
	item += `	<div class="sl__item__input">`;
	item += `		<input type="text" placeholder="댓글 달기..." id="storyCommentInput-${image.id}" />`;
	item += `		<button type="button" onClick="addComment(${image.id})">게시</button>`;
	item += `	</div>`;
	item += `</div>`;
	item += `</div>`;
	return item;
}

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {
//	console.log("윈도우 scrollTop" , $(window).scrollTop());
//	console.log("문서의 높이" ,$(document).height());
//	console.log("윈도우의 높이" ,$(window).height());
	
	let checkNum = $(window).scrollTop() - ($(document).height() - $(window).height());
	//console.log(checkNum);
	if(checkNum < 1 && checkNum > -1){
		page++;
		storyLoad();
	}
	
});

// (3) 좋아요, 안좋아요
function toggleLike(imageId) {
	let likeIcon = $(`#storyLikeIcon-${imageId}`);
	
	if (likeIcon.hasClass("far")) { // 좋아요를 하겠다.
		$.ajax({
			type: "post",
			url: `/api/image/${imageId}/likes`,
			dataType: "json"
		}).done(res=>{
			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) + 1;
			//console.log("좋아요 카운트 ", likeCount);
			$(`#storyLikeCount-${imageId}`).text(likeCount);
			
			likeIcon.addClass("fas");
			likeIcon.addClass("active");
			likeIcon.removeClass("far");
		}).fail(error=>{
			console.log("오류",error);
		});
	} else { // 좋아요를 취소 하겠다.
		$.ajax({
			type: "delete",
			url: `/api/image/${imageId}/likes`,
			dataType: "json"
		}).done(res=>{
			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) - 1;
			//console.log("좋아요 카운트 ", likeCount);
			$(`#storyLikeCount-${imageId}`).text(likeCount);
		likeIcon.removeClass("fas");
		likeIcon.removeClass("active");
		likeIcon.addClass("far");
		}).fail(error=>{
			console.log("오류",error);
		});
	}
}

// (4) 댓글쓰기
function addComment(imageId) {

	let commentInput = $(`#storyCommentInput-${imageId}`);
	let commentList = $(`#storyCommentList-${imageId}`);

	let data = {
		imageId: imageId,
		content: commentInput.val()
	}
	
	//console.log(data);
	//console.log(JSON.stringify(data));
	
	if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}

	$.ajax({
		type: "post",
		url: "/api/comment",
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
		dataType: "json"
	}).done(res=>{
		let comment = res.data;
		let content = `
		  <div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}"> 
		    <p>
		      <b>${comment.user.username} :</b>
		      ${comment.content}
		    </p>
			<button onclick="deleteComment(${comment.id})"><i class="fas fa-times"></i></button>
		  </div>
		`;
	commentList.prepend(content);
	}).fail(error=>{
		console.log("오류", error.responseJSON.data.content);
		alert(error.responseJSON.data.content);
	});
	commentInput.val("");
}

// (5) 댓글 삭제
function deleteComment(commentId) {
	$.ajax({
		type: "delete",
		url: `/api/comment/${commentId}`,
		dataType: "json"
	}).done(res=>{
		console.log("성공", res);
		$(`#storyCommentItem-${commentId}`).remove();
	}).fail(error=>{
		console.log("실패", error);
	});
}







