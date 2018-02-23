/** RESOURCE가 저장될 jqxGrid id */
var resourceGridId = "#data_resource";
/** 등록할 RESOURCE 정보가 있는 form id */
var resourceFormId = "#resourceForm";

$(document).ready(function(){
	// 그리드 생성
	initResourceGrid();
	
	// RESOURCE 그리드 갱신
	reloadResourceGrid();
});

/**
* 리소스 그리드를 초기화 합니다.
* (리소스 그리드를 생성)
*/
function initResourceGrid(){
	$(resourceGridId).jqxGrid({
		width: '100%',
		height: '400px',              
		pageable: true,
		autoheight: false,
		sortable: false,
		altrows: true,
		enabletooltips: true,
		selectionmode: 'multiplerows',
		pagerButtonsCount: 8,
		editable: true,
		editmode: 'dblclick',
		columns: [
			{
				text: '일련 번호',
				dataField: 'resourceNo',
				cellsalign: 'center',
				align: 'center',
				editable: true,
				width: '8%'/*,
				createeditor: function(row, cellvalue, editor, cellText, width, height){
					// 에디트 모드가 활성화 되면 해당 칸에 커스텀 에디터를 생성합니다.
					editor.jqxNumberInput({
						width: width,
						height: height
					});
				},
				initeditor: function(row, cellvalue, editor, celltext, pressedkey){
					// 커스텀 에디터의 초기값을 설정합니다. 에디터가 보여질때마다 콜백함수가 호출됩니다.
					editor.jqxInput('val', cellvalue);
				},
				geteditorvalue: function(row, cellvalue, editor){
					// 작성을 마친 후 커스텀 에디터의 값을 jqxGrid에게 반환 합니다.
					return editor.val();
				}*/
			},
			{text: '이름', dataField: 'resourceNm', cellsalign: 'center', align: 'center', editable: true, width: '30%'},
			{text: '패턴', dataField: 'resourcePattern', cellsalign: 'left', align: 'center', editable: true, width: '40%'},
			{text: '타입', dataField: 'resourceType', cellsalign: 'center', align: 'center', editable: true, width: '11%'},
			{text: '순서', dataField: 'sortOrder', cellsalign: 'center', align: 'center', editable: true, width: '11%'}
		]
	});
	
	// Cell Begin Edit
	$(resourceGridId).on('cellbeginedit', function(event){
		
		//console.log("Begin 정보 event.args: ", event.args);
		//console.log("Begin 정보 args.row: ", args.row);
		//console.log("Begin 정보 args.value: ", args.value);
		
		$(resourceGridId).jqxGrid("clearselection"); // RESOURCE 그리드의 선택 효과 제거
		$(resourceGridId).jqxGrid('selectrow', event.args.rowindex);	// 편집에 들어간 행에 선택 효과 부여
		
	});
	
	// Cell End Edit
	$(resourceGridId).on('cellendedit', function(event){
		
		/** 편집한 행 번호 */
		var rowIndex = event.args.rowindex;
		/** 편집한 리소스 일련 번호 */
		var resourceNo = event.args.row.resourceNo;
		/** 편집한 컬럼명 */
		var dataField = event.args.datafield;
		/** 기존 값 */
		var oldValue = event.args.oldvalue;
		if((typeof oldValue) != "number"){
			oldValue = oldValue.trim();
		}
		/** 변경 값 */
		var newValue = event.args.value.trim();
		if((typeof newValue) != "number"){
			newValue = newValue.trim();
		}
		
		//console.log("End 정보 event.args: ", event.args);
		//console.log("편집한 행 번호: ", event.args.rowindex);
		//console.log("편집한 리소스 일련 번호: ", event.args.row.resourceNo);
		//console.log("편집한 컬럼 명: ", event.args.datafield);
		//console.log("이전 값: ", event.args.oldvalue);
		//console.log("현재 값: ", event.args.value);
		
		// 변경되지 않았다면 이벤트 종료
		if(oldValue == newValue){
			return;
		}
		
		// 전송할 json 데이터 생성
		var data = {};
		eval("data." + dataField + " = '" + newValue + "'");
		var jsonData = JSON.stringify(data);
		
		// 출력
		console.log("전송할 json 데이터", jsonData);
		
		// 수정 요청 전송
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		$.ajax({
			type: "PATCH",
			url: "/security/resource/" + Number(resourceNo),
			data: jsonData,
			contentType: 'application/json',
			dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
			beforeSend: function(xhr){
				xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
				//xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
			},
			success: function(data, statusText, xhr){
				if(data.result == 'success'){
					console.log("data", data);
					//reloadResourceGrid();
				}else{
					console.log("RESOURCE 수정에 실패했습니다.");
					console.log(data.result);
					
					// 수정 전 값으로 복원
					$(resourceGridId).jqxGrid('setcellvalue', rowIndex, dataField, oldValue);
				}
			},
			error: function(xhr){
				console.log("error", xhr);
			}
		});
	});
}

/**
* 서버로부터 resource목록을 조회해 jqxGrid를 갱신 합니다.
*/
function reloadResourceGrid(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "GET",
		url: "/security/resource",
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			//xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			//console.log('data', data);	// response body
			//console.log('statusText', statusText);	// "success" or ?
			//console.log('xhr', xhr);	// header
			if(data.result == "success"){
				changeResourceGrid(data.list)
			}else{
				console.log("RESOURCE 목록 조회를 실패했습니다.");
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}

/**
* json배열 형식의 리소스 목록을 gridId에 추가 합니다.
*/
function changeResourceGrid(listData){
	$(resourceGridId).jqxGrid("clearselection"); // RESOURCE 그리드의 선택 효과 제거
	$(resourceGridId).jqxGrid("clear"); // RESOURCE 그리드의 데이터 제거
	
	var source = {
		localdata: listData,
		datatype: "array",
		datafields: [
			{name: 'resourceNo', type: 'int'},
			{name: 'resourceNm', type: 'String'},
			{name: 'resourcePattern', type: 'String'},
			{name: 'resourceType', type: 'String'},
			{name: 'sortOrder', type: 'int'}
		]
	};
	
	var dataAdapter = new $.jqx.dataAdapter(source, {
		downloadComplete: function (data, status, xhr) { },
		loadComplete: function (data) { },
		loadError: function (xhr, status, error) { }
	});
	
	// RESOURCE 그리드에 새로운 데이터 삽입
	$(resourceGridId).jqxGrid({
		source: dataAdapter
	});
}

/**
 * 특정 jqxGrid로부터 현재 선택된 행의 특정 column value 목록을 반환합니다.
 * 이때 검색할 column의 value는 반드시 Number 타입이어야 합니다.
 * @param jqxGridId 검색할 jqxGrid 셀렉터
 * @param returnColumnStr 검색할 column 명
 * @returns 현재 선택 상태인 row의 column value 목록
 */
function getSelectedNoArray(jqxGridId, returnColumnStr){
	var selectedRowIndexes = $(jqxGridId).jqxGrid('getselectedrowindexes');
	var selectedRowData = [];
	
	for(var i=0; i<selectedRowIndexes.length; i++){
		selectedRowData[i] = Number($(jqxGridId).jqxGrid('getcellvalue', selectedRowIndexes[i], returnColumnStr));
	}
	
	return selectedRowData;
}

/**
 * 폼에 입력된 RESOURCE를 등록합니다.
 * @returns
 */
function insertResource(){
	// 전송할 json 데이터 생성
	var serializeArrayForm = $(resourceFormId).serializeArray();
	var objectForm = objectifyForm(serializeArrayForm);
	var jsonForm = JSON.stringify(objectForm);
	
	// 출력
	console.log('전송할 json 데이터', jsonForm);
	
	// 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "POST",
		url: "/security/resource/",
		data: jsonForm,
		contentType: 'application/json',
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			//xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == 'success'){
				console.log("data", data);
				reloadResourceGrid();
				$(resourceFormId)[0].reset();
			}else{
				console.log("RESOURCE 등록에 실패했습니다.");
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}

/**
 * $(form selector).serializeArray()로 전달된 객체를 오브젝트로 변환합니다.
 * 이렇게 변환한 객체를 JSON.stringify()에 사용할 수 있습니다.
 */
function objectifyForm(formArray){
	var returnArray = {};
	for(var i=0; i<formArray.length; i++){
		returnArray[formArray[i]['name']] = formArray[i]['value'];
	}
	return returnArray;
}

/**
 * RESOURCE jqxGrid에서 선택된 항목을 제거합니다.
 * @returns
 */
function deleteSelectedResources(){
	// 현재 선택한 리소스와 권한의 일련 번호 구하기
	var selectedResourceNoArray = String(getSelectedNoArray(resourceGridId, 'resourceNo'));
	
	// 선택한 행이 없으면 이벤트 취소
	if(selectedResourceNoArray.length <= 0){
		console.log('제거할 행을 선택해 주세요.');
		return;
	}
	
	// 출력
	console.log('전송할 데이터', selectedResourceNoArray);
	
	// 제거 요청 전송
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	$.ajax({
		type: "DELETE",
		url: "/security/resource/" + selectedResourceNoArray,
		dataType: "json",	// 서버에서 응답한 데이터를 클라이언트에서 읽는 방식
		beforeSend: function(xhr){
			xhr.setRequestHeader("X-Ajax-call", "true");	// CustomAccessDeniedHandler에서 Ajax요청을 구분하기 위해 약속한 값
			//xhr.setRequestHeader(header, token);	// 헤더의 csrf meta태그를 읽어 CSRF 토큰 함께 전송
		},
		success: function(data, statusText, xhr){
			if(data.result == 'success'){
				console.log("data", data);
				reloadResourceGrid();
			}else{
				console.log("다음의 RESOURCE 제거에 실패했습니다.");
				console.log(data.result);
			}
		},
		error: function(xhr){
			console.log("error", xhr);
		}
	});
}













