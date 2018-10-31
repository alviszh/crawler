package org.xvolks.jnative.misc;
import org.xvolks.jnative.exceptions.*;
import org.xvolks.jnative.misc.basicStructures.*;
import org.xvolks.jnative.pointers.*;
import org.xvolks.jnative.pointers.memory.*;

/**
 * $Id: MSG.java,v 1.8 2007/03/16 21:06:14 thubby Exp $
 *
 * TODO : Create each value : createPointer should copy values in memory and back
 * TODO : this should be a BasicData<MSG>
 * <pre>
 * The C structure
 * typedef struct {
 * &nbsp;	HWND hwnd;
 * &nbsp;	 UINT message;
 * &nbsp;	WPARAM wParam;
 * &nbsp;	LPARAM lParam;
 * &nbsp;	DWORD time;
 * &nbsp;	POINT pt;
 * } MSG, *PMSG;</pre>
 * This software is released under the LGPL.
 * @author Created by Marc DENTY - (c) 2006 JNative project
 */
public class MSG implements BasicData<Pointer> {
	
	/**
	 * Method getPointer
	 *
	 * @return   a Pointer
	 *
	 */
	public Pointer getPointer() {
		return mPointer;
	}
	
	
	private Pointer mPointer;
	
	public MSG() throws NativeException {
		createPointer();
	}
	
	/**
	 * Method createPointer
	 *
	 * @return   a MemoryBlock
	 *
	 */
	public Pointer createPointer() throws NativeException {
		mPointer = new Pointer(MemoryBlockFactory.createMemoryBlock(sizeOf()));
		return mPointer;
	}
	
	/**
	 * Method getValueFromPointer
	 *
	 * @return   a T
	 *
	 */
	public Pointer getValueFromPointer() {
		return mPointer;
	}
	
	
	/**
	 * Method getValue
	 *
	 * @return   a T
	 *
	 */
	public Pointer getValue() {
		return mPointer;
	}
	
	/**
	 * Method getSizeOf
	 * @return   the size of this data
	 */
	public int getSizeOf() {
		return sizeOf();
	}
	
	public static int sizeOf() {
		return HWND.sizeOf() + UINT.sizeOf() + WPARAM.sizeOf() + LPARAM.sizeOf() + 4 + Point.sizeOf();
	}
	
	
	/**
	 * Method getMessage()
	 * @return returns the message identifier
	 * @throws NativeException
	 */
	public UINT getMessage() throws NativeException {
		return new UINT(mPointer.getAsShort(4));
	}

	
	public class WindowsConstants {
		/*
		 * ShowWindow() Commands
		 */

		public static final int SW_HIDE             = 0;
		public static final int SW_SHOWNORMAL       = 1;
		public static final int SW_NORMAL           = 1;
		public static final int SW_SHOWMINIMIZED    = 2;
		public static final int SW_SHOWMAXIMIZED    = 3;
		public static final int SW_MAXIMIZE         = 3;
		public static final int SW_SHOWNOACTIVATE   = 4;
		public static final int SW_SHOW             = 5;
		public static final int SW_MINIMIZE         = 6;
		public static final int SW_SHOWMINNOACTIVE  = 7;
		public static final int SW_SHOWNA           = 8;
		public static final int SW_RESTORE          = 9;
		public static final int SW_SHOWDEFAULT      = 10;
		public static final int SW_FORCEMINIMIZE    = 11;
		public static final int SW_MAX              = 11;

		/*
		 * Window Styles
		 */
		public static final int WS_OVERLAPPED       = 0x00000000;
		public static final int WS_POPUP            = 0x80000000;
		public static final int WS_CHILD            = 0x40000000;
		public static final int WS_MINIMIZE         = 0x20000000;
		public static final int WS_VISIBLE          = 0x10000000;
		public static final int WS_DISABLED         = 0x08000000;
		public static final int WS_CLIPSIBLINGS     = 0x04000000;
		public static final int WS_CLIPCHILDREN     = 0x02000000;
		public static final int WS_MAXIMIZE         = 0x01000000;
		public static final int WS_CAPTION          = 0x00C00000;     /* WS_BORDER | WS_DLGFRAME  */
		public static final int WS_BORDER           = 0x00800000;
		public static final int WS_DLGFRAME         = 0x00400000;
		public static final int WS_VSCROLL          = 0x00200000;
		public static final int WS_HSCROLL          = 0x00100000;
		public static final int WS_SYSMENU          = 0x00080000;
		public static final int WS_THICKFRAME       = 0x00040000;
		public static final int WS_GROUP            = 0x00020000;
		public static final int WS_TABSTOP          = 0x00010000;
		
		public static final int WS_MINIMIZEBOX      = 0x00020000;
		public static final int WS_MAXIMIZEBOX      = 0x00010000;
		
		
		public static final int WS_TILED            = WS_OVERLAPPED;
		public static final int WS_ICONIC           = WS_MINIMIZE;
		public static final int WS_SIZEBOX          = WS_THICKFRAME;
		
//		public final static int WM_CHAR = 258;
//		public final static int WM_CHARTOITEM = 47;
//		public final static int WM_CHILDACTIVATE = 34;
//		public final static int WM_CLEAR = 771;
//		public final static int WM_CLOSE = 16;
//		public final static int WM_COMMAND = 273;
//		public final static int WM_COMMNOTIFY = 68;                /* obsolete */
//		public final static int WM_COMPACTING = 65;
//		public final static int WM_COMPAREITEM = 57;
//		public final static int WM_CONTEXTMENU = 123;
//		public final static int WM_COPY = 769;
//		public final static int WM_COPYDATA = 74;
//		public final static int WM_CREATE = 1;
//		public final static int WM_CTLCOLORBTN = 309;
//		public final static int WM_CTLCOLORDLG = 310;
//		public final static int WM_CTLCOLOREDIT = 307;
//		public final static int WM_CTLCOLORLISTBOX = 308;
//		public final static int WM_CTLCOLORMSGBOX = 306;
//		public final static int WM_CTLCOLORSCROLLBAR = 311;
//		public final static int WM_CTLCOLORSTATIC = 312;
//		public final static int WM_CUT = 768;
//		public final static int WM_DEADCHAR = 259;
//		public final static int WM_DELETEITEM = 45;
//		public final static int WM_DESTROY = 2;
//		public final static int WM_DESTROYCLIPBOARD = 775;
//		public final static int WM_DEVICECHANGE = 537;
//		public final static int WM_DEVMODECHANGE = 27;
//		public final static int WM_DISPLAYCHANGE = 126;
//		public final static int WM_DRAWCLIPBOARD = 776;
//		public final static int WM_DRAWITEM = 43;
//		public final static int WM_DROPFILES = 563;
//		public final static int WM_ENABLE = 10;
//		public final static int WM_ENDSESSION = 22;
//		public final static int WM_ENTERIDLE = 289;
//		public final static int WM_ENTERMENULOOP = 529;
//		public final static int WM_ENTERSIZEMOVE = 561;
//		public final static int WM_ERASEBKGND = 20;
//		public final static int WM_EXITMENULOOP = 530;
//		public final static int WM_EXITSIZEMOVE = 562;
//		public final static int WM_FONTCHANGE = 29;
//		public final static int WM_GETDLGCODE = 135;
//		public final static int WM_GETFONT = 49;
//		public final static int WM_GETHOTKEY = 51;
//		public final static int WM_GETICON = 127;
//		public final static int WM_GETMINMAXINFO = 36;
//		public final static int WM_GETTEXT = 13;
//		public final static int WM_GETTEXTLENGTH = 14;
//		/* FIXME/CHECK: Are WM_HANDHEL ={FIRST,LAST} valid for WINVER < 0x400? */
//		public final static int WM_HANDHELDFIRST = 856;
//		public final static int WM_HANDHELDLAST = 863;
//		public final static int WM_HELP = 83;
//		public final static int WM_HOTKEY = 786;
//		public final static int WM_HSCROLL = 276;
//		public final static int WM_HSCROLLCLIPBOARD = 782;
//		public final static int WM_ICONERASEBKGND = 39;
//		public final static int WM_INITDIALOG = 272;
//		public final static int WM_INITMENU = 278;
//		public final static int WM_INITMENUPOPUP = 279;
//		public final static int WM_INPUTLANGCHANGE = 81;
//		public final static int WM_INPUTLANGCHANGEREQUEST = 80;
//		public final static int WM_KEYDOWN = 256;
//		public final static int WM_KEYUP = 257;
//		public final static int WM_KILLFOCUS = 8;
//		public final static int WM_MDIACTIVATE = 546;
//		public final static int WM_MDICASCADE = 551;
//		public final static int WM_MDICREATE = 544;
//		public final static int WM_MDIDESTROY = 545;
//		public final static int WM_MDIGETACTIVE = 553;
//		public final static int WM_MDIICONARRANGE = 552;
//		public final static int WM_MDIMAXIMIZE = 549;
//		public final static int WM_MDINEXT = 548;
//		public final static int WM_MDIREFRESHMENU = 564;
//		public final static int WM_MDIRESTORE = 547;
//		public final static int WM_MDISETMENU = 560;
//		public final static int WM_MDITILE = 550;
//		public final static int WM_MEASUREITEM = 44;
//		//#if (WINVER >= 0x0500)
//		public final static int WM_MENURBUTTONUP = 290;
//		//#endif
//		public final static int WM_MENUCHAR = 288;
//		public final static int WM_MENUCOMMAND = 294;
//		public final static int WM_MENUSELECT = 287;
//		public final static int WM_NEXTMENU = 531;
//		public final static int WM_MOVE = 3;
//		public final static int WM_MOVING = 534;
//		public final static int WM_NCACTIVATE = 134;
//		public final static int WM_NCCALCSIZE = 131;
//		public final static int WM_NCCREATE = 129;
//		public final static int WM_NCDESTROY = 130;
//		public final static int WM_NCHITTEST = 132;
//		public final static int WM_NCLBUTTONDBLCLK = 163;
//		public final static int WM_NCLBUTTONDOWN = 161;
//		public final static int WM_NCLBUTTONUP = 162;
//		public final static int WM_NCMBUTTONDBLCLK = 169;
//		public final static int WM_NCMBUTTONDOWN = 167;
//		public final static int WM_NCMBUTTONUP = 168;
//		//#if (_WIN32_WINNT >= 0x0500)
//		public final static int WM_NCXBUTTONDOWN = 171;
//		public final static int WM_NCXBUTTONUP = 172;
//		public final static int WM_NCXBUTTONDBLCLK = 173;
//		//#endif
//		public final static int WM_NCMOUSEMOVE = 160;
//		public final static int WM_NCMOUSEHOVER =     0x02A0;
//		public final static int WM_NCMOUSELEAVE =     0x02A2;
//		public final static int WM_NCPAINT = 133;
//		public final static int WM_NCRBUTTONDBLCLK = 166;
//		public final static int WM_NCRBUTTONDOWN = 164;
//		public final static int WM_NCRBUTTONUP = 165;
//		public final static int WM_NEXTDLGCTL = 40;
//		public final static int WM_NOTIFY = 78;
//		public final static int WM_NOTIFYFORMAT = 85;
//		public final static int WM_NULL = 0;
//		public final static int WM_PAINT = 15;
//		public final static int WM_PAINTCLIPBOARD = 777;
//		public final static int WM_PAINTICON = 38;
//		public final static int WM_PALETTECHANGED = 785;
//		public final static int WM_PALETTEISCHANGING = 784;
//		public final static int WM_PARENTNOTIFY = 528;
//		public final static int WM_PASTE = 770;
//		public final static int WM_PENWINFIRST = 896;
//		public final static int WM_PENWINLAST = 911;
//		public final static int WM_POWER = 72;
//		public final static int WM_POWERBROADCAST = 536;
//		public final static int WM_PRINT = 791;
//		public final static int WM_PRINTCLIENT = 792;
//		public final static int WM_QUERYDRAGICON = 55;
//		public final static int WM_QUERYENDSESSION = 17;
//		public final static int WM_QUERYNEWPALETTE = 783;
//		public final static int WM_QUERYOPEN = 19;
//		public final static int WM_QUEUESYNC = 35;
//		public final static int WM_QUIT = 18;
//		public final static int WM_RENDERALLFORMATS = 774;
//		public final static int WM_RENDERFORMAT = 773;
//		public final static int WM_SETCURSOR = 32;
//		public final static int WM_SETFOCUS = 7;
//		public final static int WM_SETFONT = 48;
//		public final static int WM_SETHOTKEY = 50;
//		public final static int WM_SETICON = 128;
//		public final static int WM_SETREDRAW = 11;
//		public final static int WM_SETTEXT = 12;
//		public final static int WM_SETTINGCHANGE = 26;
//		public final static int WM_SHOWWINDOW = 24;
//		public final static int WM_SIZE = 5;
//		public final static int WM_SIZECLIPBOARD = 779;
//		public final static int WM_SIZING = 532;
//		public final static int WM_SPOOLERSTATUS = 42;
//		public final static int WM_STYLECHANGED = 125;
//		public final static int WM_STYLECHANGING = 124;
//		public final static int WM_SYSCHAR = 262;
//		public final static int WM_SYSCOLORCHANGE = 21;
//		public final static int WM_SYSCOMMAND = 274;
//		public final static int WM_SYSDEADCHAR = 263;
//		public final static int WM_SYSKEYDOWN = 260;
//		public final static int WM_SYSKEYUP = 261;
//		public final static int WM_TCARD = 82;
//		public final static int WM_TIMECHANGE = 30;
//		public final static int WM_TIMER = 275;
//		public final static int WM_SYSTIMER = 280;
//		public final static int WM_UNDO = 772;
//		public final static int WM_USER = 1024;
//		public final static int WM_USERCHANGED = 84;
//		public final static int WM_VKEYTOITEM = 46;
//		public final static int WM_VSCROLL = 277;
//		public final static int WM_VSCROLLCLIPBOARD = 778;
//		public final static int WM_WINDOWPOSCHANGED = 71;
//		public final static int WM_WINDOWPOSCHANGING = 70;
//		public final static int WM_WININICHANGE = 26;
//		public final static int WM_KEYFIRST = 256;
//		public final static int WM_KEYLAST = 264;
//		public final static int WM_SYNCPAINT =  136;
//		public final static int WM_MOUSEACTIVATE = 33;
//		public final static int WM_MOUSEMOVE = 512;
//		public final static int WM_LBUTTONDOWN = 513;
//		public final static int WM_LBUTTONUP = 514;
//		public final static int WM_LBUTTONDBLCLK = 515;
//		public final static int WM_RBUTTONDOWN = 516;
//		public final static int WM_RBUTTONUP = 517;
//		public final static int WM_RBUTTONDBLCLK = 518;
//		public final static int WM_MBUTTONDOWN = 519;
//		public final static int WM_MBUTTONUP = 520;
//		public final static int WM_MBUTTONDBLCLK = 521;
//		public final static int WM_MOUSEWHEEL = 522;
//		public final static int WM_MOUSEFIRST = 512;
//		//#if (_WIN32_WINNT >= 0x0500)
//		public final static int WM_XBUTTONDOWN = 523;
//		public final static int WM_XBUTTONUP = 524;
//		public final static int WM_XBUTTONDBLCLK = 525;
//		public final static int WM_MOUSELAST = 525;
//		//#endif
//		public final static int WM_MOUSEHOVER = 0x2A1;
//		public final static int WM_MOUSELEAVE = 0x2A3;
//		//#if (_WIN32_WINNT >= 0x0400)
//		/*
//		public final static int WHEEL_DELTA=120;
//		public final static int GET_WHEEL_DELTA_WPARAM(wparam) ((short)HIWORD (wparam))
//		public final static int WHEEL_PAGESCROLL UINT_MAX
//		*/
//		//#endif
//		//#if (_WIN32_WINNT >= 0x0501)
//		public final static int WM_THEMECHANGED = 794;
//		//#endif
//		//#ifndef _WIN32_WCE
//		//#if(_WIN32_WINNT >= 0x0500)
//		public final static int WM_CHANGEUISTATE = 295;
//		public final static int WM_UPDATEUISTATE = 296;
//		public final static int WM_QUERYUISTATE = 297;
//		//#endif

		public final static int GWL_EXSTYLE = -20;
		public final static int GWL_STYLE = -16;
		public final static int GWL_WNDPROC = -4;
		public final static int GWLP_WNDPROC = -4;
		public final static int GWL_HINSTANCE = -6;
		public final static int GWLP_HINSTANCE = -6;
		public final static int GWL_HWNDPARENT = -8;
		public final static int GWLP_HWNDPARENT = -8;
		public final static int GWL_ID = -12;
		public final static int GWLP_ID = -12;
		public final static int GWL_USERDATA = -21;
		public final static int GWLP_USERDATA = -21;
		
		public final static int CW_USEDEFAULT = ((int)0x80000000);
		
		/*
		 * Common Window Styles
		 */
		public static final int WS_OVERLAPPEDWINDOW = (WS_OVERLAPPED  |
		WS_CAPTION        |
		WS_SYSMENU        |
		WS_THICKFRAME     |
		WS_MINIMIZEBOX    |
		WS_MAXIMIZEBOX);
		
		public static final int WS_POPUPWINDOW      = (WS_POPUP          |
		WS_BORDER         |
		WS_SYSMENU);
		
		public static final int WS_CHILDWINDOW      = (WS_CHILD);
		
		public static final int WS_TILEDWINDOW      = WS_OVERLAPPEDWINDOW;
	}
}
