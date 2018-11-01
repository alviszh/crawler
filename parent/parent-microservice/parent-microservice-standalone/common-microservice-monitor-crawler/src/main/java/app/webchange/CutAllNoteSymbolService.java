package app.webchange;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * @description:用于去除返回的源码中所有的注释项
 */
@Component
public class CutAllNoteSymbolService {
	// , /* ,  */ , <-- ,<!-- , -->

    private static String s = "//";
    private static String s1 = "/*";
    private static String s2 = "*/";
    private static String s3 = "<--";
    private static String s4 = "<!--";
    private static String s5 = "-->";
    private static String s6 = "/**";
    private static String s7 = "*";

    private static List<String> rexList;

    static {
        rexList = new ArrayList<String>(6);
        rexList.add(s);
        rexList.add(s1);
        rexList.add(s2);
        rexList.add(s3);
        rexList.add(s4);
        rexList.add(s5);
        rexList.add(s6);
        rexList.add(s7);
    }

    public static String replaceAllNoteSymbol(String html) {

        for (String rex : rexList) {
            StringBuffer htmlBuffer = new StringBuffer();
            char[] htmlCharArray = html.toCharArray();
            int htmlLength = htmlCharArray.length;
            char[] regCharArr = rex.toCharArray();
            int rexLen = regCharArr.length;
            char rexStartByte = regCharArr[0];

            for (int i = 0; i < htmlLength; i++) {
                boolean b = true;
                if (rexStartByte == htmlCharArray[i]) {
                    for (int j = 1; j < rexLen; j++) {
                        if ((i + j) >= htmlLength) {
                            htmlBuffer.append(htmlCharArray[i]);
                            break;
                        }
                        if (regCharArr[j] != htmlCharArray[i + j]) {
                            b = false;
                            break;
                        }
                    }
                    if (b) {
                        i += rexLen - 1;
                        continue;
                    }
                }
                htmlBuffer.append(htmlCharArray[i]);
            }
            html = htmlBuffer.toString();
        }
        return html;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String html = "<!--测试-->\n" +
                "\n" +
                "\n" +
                "  /* \n" +
                "         *多行注释\n" +
                " */\n" +
                "\n" +
                "function toregister(){\n" +
                "\t//单行注释\n" +
                "\tscreenWidth = window.screen.width - 9;screenHeight = window.screen.height-85;\n" +
                "\twindow.top.location = \"login.do?method=password\";\n" +
                "\t//window.open('login.do?method=password','_self','left=0,top=0,height='+screenHeight+',width='+screenWidth+',scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no');\n" +
                "}//*";

        String test = replaceAllNoteSymbol(html);
        System.out.println(test);
    }
}
