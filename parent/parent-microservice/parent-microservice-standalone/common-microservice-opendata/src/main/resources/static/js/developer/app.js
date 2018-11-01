 $('#upload-file').on('change', function () {
	 var reader = new FileReader();
	    var AllowImgFileSize = 2100000; //上传图片最大值(单位字节)（ 2 M = 2097152 B ）超过2M上传失败
	    var input = document.getElementById("upload-file");
		if (!input['value'].match(/.jpg|.gif|.png|.bmp/i)) {
			// 判断上传文件格式
			alert("上传的图片格式不正确，请重新选择");
			return false;
		} else if (input.files[0].size > AllowImgFileSize) {
			alert("上传的图片格式过大，请重新选择");
			return false;
		}
		var file = $("#upload-file")[0].files[0];
	    var imgUrlBase64;
	    if (file) {
	        //将文件以Data URL形式读入页面  
	        imgUrlBase64 = reader.readAsDataURL(file);
	        reader.onload = function (e) {
	          //var ImgFileSize = reader.result.substring(reader.result.indexOf(",") + 1).length;
	          if (AllowImgFileSize != 0 && AllowImgFileSize < reader.result.length) {
	                alert( '上传失败，请上传不大于2M的图片！');
	                return;
	            }else{
	                var img = reader.result;
	                $('.cropped').html('');
	                $('.cropped').append('<img class="smallImgSrc" src="' + img + '" align="absmiddle" style="width:120px;box-shadow:0px 0px 12px #7E7E7E;">');
	                $('.cropped').append('<input value = "' + img + '" type="hidden" class="" name="icon" id="icon" />');
	            }
	        }
	     } 
	 
 });
