
/**
 * 回调测试
 * @param val 第val项
 */
function testAsk(e){
	var _flag = e.name;
	var cModule = $("input[name='cModule"+_flag+"']").val();
	var cType = $("input[name='cType"+_flag+"']").val();
	var cValue = $("span[name='cValue"+_flag+"']").text();
	
	var ajaxParam = {
		cModule : cModule,
		cType : cType,
		cValue : cValue
	}
	$.ajax({  
	    url:'/carrier/cbtest/ask',
	    data:ajaxParam,  
	    type:'post',  
	    dataType:'json',  
	    success:function(data) {  
	        if(data.result == "true"){
	        	$("#detail_response_status"+_flag).text(data.responseStatus);
        		$("#detail_response_result"+_flag).text(data.responseResult);
        		if(typeof data.responseBody == 'object'){
        			$("#detail_response_body"+_flag).text(JSON.stringify(data.responseBody));
        		}else{
        			$("#detail_response_body"+_flag).text(data.responseBody);
        		}
	        	
        		if(typeof data.requestHeaders == 'object'){
        			$("#detail_request_headers"+_flag).text(JSON.stringify(data.requestHeaders));
        		}else{
        			$("#detail_request_headers"+_flag).text(data.requestHeaders);
        		}
        		
    			if(typeof data.requestBody == 'object'){
    				$("#detail_request_body"+_flag).text(JSON.stringify(data.requestBody));
        		}else{
        			$("#detail_request_body"+_flag).text(data.requestBody);
        		}
	        	
	        	$("#test_detail_div"+_flag).show();
	        }else{  
	        	$("#infoText").text(data.msg);
	        	$("#infoModal").modal();
	        }  
	     },  
	     error : function() {  
	    	$("#infoText").text("回调测试时发生异常!");
        	$("#infoModal").modal();
	     }  
	});
}