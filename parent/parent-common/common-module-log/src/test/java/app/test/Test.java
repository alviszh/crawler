package app.test;

public class Test {
	 public static void main(String[] args) throws InterruptedException {
		for(int i=0;i<10;i++){
			Thread.sleep(1);
			long currentTimeMillis = System.currentTimeMillis();
			System.out.println(currentTimeMillis);
			//将long型数据转换为String
			String strCurrentTimeMillis = String.valueOf(currentTimeMillis);
			System.out.println(strCurrentTimeMillis);
			String lastSix = strCurrentTimeMillis.substring(strCurrentTimeMillis.length()-6, strCurrentTimeMillis.length());
			System.out.println(lastSix);
			System.out.println("=========================");
		}
	}
}
