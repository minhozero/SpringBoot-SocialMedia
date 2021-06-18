// (1) 회원정보 수정
function update(userId, event) {
	event.preventDefault(); // 폼태그 막기
	let data = $("#profileUpdate").serialize(); //form 태그의 key=value 데이터 보낼때. serialize
	console.log(data);
	$.ajax({
		type: "put",
		url: `/api/user/${userId}`,
		data: data,
		contentType : "application/x-www-form-urlencoded; charset=utf-8",
		dataType: "json"
	}).done(res=>{ //httpStatus 상태코드 200번대
		console.log("성공", res);
		location.href = '/user/'+userId;
	}).fail(error=>{ //httpStatus 상태코드 200번대가 아닐때.
		if(error.data == null){
			alert(error.responseJSON.message);
		} else {
			alert(JSON.stringify(error.responseJSON.data));
			//alert(error.responseJSON.data.name);
			//console.log("실패", error.responseJSON.data.name);
		}
	});
}