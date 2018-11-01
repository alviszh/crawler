function onLogin(appversion, yhm) {
	return abcMd5(appversion, yhm);
}
function abcMd5(s, s2) {
	s = s.replace(/\./g, "");
	var hhz = "9853398" + s + "7291166723";
	var len = hhz.length;
	var len2 = s2.length;
		hhz = hhz.substring(6, 9) + s2.substring(4, 7) + hhz.substring(0, 4)
				+ s2.substring(0, 4) + hhz.substring(len - 5)
				+ s2.substring(11, 15) + hhz.substring(3, 7) + s2.substring(11)
				+ hhz.substring(10, 14)
	return hhz
}