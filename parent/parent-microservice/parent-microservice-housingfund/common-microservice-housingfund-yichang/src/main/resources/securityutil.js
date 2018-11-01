var A=0,v=8;

function a(r,t,n,u,o,a,c){return e(t^n^u,r,t,o,a,c)}
function c(r,t,n,u,o,a,c){return e(n^(t|~u),r,t,o,a,c)}
function d(r){var t=15&r;return"0123456789ABCDEF".charAt(r>>4&15)+"0123456789ABCDEF".charAt(t)}
function e(r,t,n,e,u,o){return f(function(r,t){return r<<t|r>>>32-t}(f(f(t,r),f(e,o)),u),n)}
function f(r,t){var n=(65535&r)+(65535&t);return(r>>16)+(t>>16)+(n>>16)<<16|65535&n}
function t(r){return s(n(i(r),r.length*v))}
function s(r){for(var t=A?"0123456789ABCDEF":"0123456789abcdef",n="",e=0;e<4*r.length;e++)n+=t.charAt(r[e>>2]>>e%4*8+4&15)+t.charAt(r[e>>2]>>e%4*8&15);return n}
function n(r,t){r[t>>5]|=128<<t%32,r[14+(t+64>>>9<<4)]=t;for(var n=1732584193,e=-271733879,i=-1732584194,s=271733878,d=0;d<r.length;d+=16){var h=n,C=e,A=i,v=s;e=c(e=c(e=c(e=c(e=a(e=a(e=a(e=a(e=o(e=o(e=o(e=o(e=u(e=u(e=u(e=u(e,i=u(i,s=u(s,n=u(n,e,i,s,r[d+0],7,-680876936),e,i,r[d+1],12,-389564586),n,e,r[d+2],17,606105819),s,n,r[d+3],22,-1044525330),i=u(i,s=u(s,n=u(n,e,i,s,r[d+4],7,-176418897),e,i,r[d+5],12,1200080426),n,e,r[d+6],17,-1473231341),s,n,r[d+7],22,-45705983),i=u(i,s=u(s,n=u(n,e,i,s,r[d+8],7,1770035416),e,i,r[d+9],12,-1958414417),n,e,r[d+10],17,-42063),s,n,r[d+11],22,-1990404162),i=u(i,s=u(s,n=u(n,e,i,s,r[d+12],7,1804603682),e,i,r[d+13],12,-40341101),n,e,r[d+14],17,-1502002290),s,n,r[d+15],22,1236535329),i=o(i,s=o(s,n=o(n,e,i,s,r[d+1],5,-165796510),e,i,r[d+6],9,-1069501632),n,e,r[d+11],14,643717713),s,n,r[d+0],20,-373897302),i=o(i,s=o(s,n=o(n,e,i,s,r[d+5],5,-701558691),e,i,r[d+10],9,38016083),n,e,r[d+15],14,-660478335),s,n,r[d+4],20,-405537848),i=o(i,s=o(s,n=o(n,e,i,s,r[d+9],5,568446438),e,i,r[d+14],9,-1019803690),n,e,r[d+3],14,-187363961),s,n,r[d+8],20,1163531501),i=o(i,s=o(s,n=o(n,e,i,s,r[d+13],5,-1444681467),e,i,r[d+2],9,-51403784),n,e,r[d+7],14,1735328473),s,n,r[d+12],20,-1926607734),i=a(i,s=a(s,n=a(n,e,i,s,r[d+5],4,-378558),e,i,r[d+8],11,-2022574463),n,e,r[d+11],16,1839030562),s,n,r[d+14],23,-35309556),i=a(i,s=a(s,n=a(n,e,i,s,r[d+1],4,-1530992060),e,i,r[d+4],11,1272893353),n,e,r[d+7],16,-155497632),s,n,r[d+10],23,-1094730640),i=a(i,s=a(s,n=a(n,e,i,s,r[d+13],4,681279174),e,i,r[d+0],11,-358537222),n,e,r[d+3],16,-722521979),s,n,r[d+6],23,76029189),i=a(i,s=a(s,n=a(n,e,i,s,r[d+9],4,-640364487),e,i,r[d+12],11,-421815835),n,e,r[d+15],16,530742520),s,n,r[d+2],23,-995338651),i=c(i,s=c(s,n=c(n,e,i,s,r[d+0],6,-198630844),e,i,r[d+7],10,1126891415),n,e,r[d+14],15,-1416354905),s,n,r[d+5],21,-57434055),i=c(i,s=c(s,n=c(n,e,i,s,r[d+12],6,1700485571),e,i,r[d+3],10,-1894986606),n,e,r[d+10],15,-1051523),s,n,r[d+1],21,-2054922799),i=c(i,s=c(s,n=c(n,e,i,s,r[d+8],6,1873313359),e,i,r[d+15],10,-30611744),n,e,r[d+6],15,-1560198380),s,n,r[d+13],21,1309151649),i=c(i,s=c(s,n=c(n,e,i,s,r[d+4],6,-145523070),e,i,r[d+11],10,-1120210379),n,e,r[d+2],15,718787259),s,n,r[d+9],21,-343485551),n=f(n,h),e=f(e,C),i=f(i,A),s=f(s,v)}return Array(n,e,i,s)}
function i(r){for(var t=Array(),n=(1<<v)-1,e=0;e<r.length*v;e+=v)t[e>>5]|=(r.charCodeAt(e/v)&n)<<e%32;return t}
function u(r,t,n,u,o,a,c){return e(t&n|~t&u,r,t,o,a,c)}
function o(r,t,n,u,o,a,c){return e(t&u|n&~u,r,t,o,a,c)}
function encryptedString(r,n){
	r=function(r)
	{
		for(var t="",n=0;n<r.length;n++)
		{
			var e=r.charAt(n),u=r.charCodeAt(n);t+=u>255?"\\u"+d(u>>8)+d(255&u):e
		}
		return t
	}
	(r);
	var e="",u=t(n),o="";u=u.toUpperCase();
	for(var a=0,c=r.length,f=u.length,i=0;i<c;i++)
		a===f&&(a=0),e+=u.substr(a,1),a++;
	for(var s=0;s<c;s++)
		o+=String.fromCharCode((r[s].charCodeAt()+e[s].charCodeAt())%256);
		return o;
}