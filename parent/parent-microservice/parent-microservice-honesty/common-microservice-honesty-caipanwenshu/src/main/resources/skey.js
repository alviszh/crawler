 function getskey(skeys) {
       
	 var skey= skeys;
	 var hash = 5381; 
	 for (var i = 0, len = skey.length;i < len;++i) { 
		 hash += (hash << 5) + skey.charCodeAt(i); 
		 } 
	 return hash & 2147483647;
}
