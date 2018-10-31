/**
 * 
 */
package org.xvolks.jnative.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import javax.swing.ImageIcon;
import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.misc.basicStructures.BITMAP;
import org.xvolks.jnative.misc.basicStructures.BITMAPINFOHEADER;
import org.xvolks.jnative.misc.basicStructures.DC;
import org.xvolks.jnative.misc.basicStructures.DWORD;
import org.xvolks.jnative.misc.basicStructures.ICONINFO;
import org.xvolks.jnative.misc.basicStructures.LONG;
import org.xvolks.jnative.pointers.Pointer;

/**
 * @author Marc DENTY (mdt) - 14 nov. 06
 * $Id: Gdi32.java,v 1.6 2007/04/27 18:42:03 thubby Exp $
 *
 */
public class Gdi32 {

	public static final String DLL_NAME = "Gdi32.dll";
    
    public static final int DIB_RGB_COLORS = 0;
	public static final int SRCCOPY = 0xCC0020;

    /**
     * <pre>
     * GetStockObject

	The GetStockObject function retrieves a handle to one of the stock pens, brushes, fonts, or palettes.

	HGDIOBJ GetStockObject(
	  int fnObject   // stock object type
	);
     * Parameters

fnObject
    [in] Specifies the type of stock object. This parameter can be one of the following values.
    Value 	Meaning
    BLACK_BRUSH 	Black brush.
    DKGRAY_BRUSH 	Dark gray brush.
    DC_BRUSH 	Windows 2000/XP: Solid color brush. The default color is white. The color can be changed by using the SetDCBrushColor function. For more information, see the Remarks section.
    GRAY_BRUSH 	Gray brush.
    HOLLOW_BRUSH 	Hollow brush (equivalent to NULL_BRUSH).
    LTGRAY_BRUSH 	Light gray brush.
    NULL_BRUSH 	Null brush (equivalent to HOLLOW_BRUSH).
    WHITE_BRUSH 	White brush.
    BLACK_PEN 	Black pen.
    DC_PEN 	Windows 2000/XP: Solid pen color. The default color is white. The color can be changed by using the SetDCPenColor function. For more information, see the Remarks section.
    WHITE_PEN 	White pen.
    ANSI_FIXED_FONT 	Windows fixed-pitch (monospace) system font.
    ANSI_VAR_FONT 	Windows variable-pitch (proportional space) system font.
    DEVICE_DEFAULT_FONT 	Windows NT/2000/XP: Device-dependent font.
    DEFAULT_GUI_FONT 	Default font for user interface objects such as menus and dialog boxes. This is MS Sans Serif. Compare this with SYSTEM_FONT.
    OEM_FIXED_FONT 	Original equipment manufacturer (OEM) dependent fixed-pitch (monospace) font.
    SYSTEM_FONT 	System font. By default, the system uses the system font to draw menus, dialog box controls, and text.

    Windows 95/98 and Windows NT: The system font is MS Sans Serif.

    Windows 2000/XP: The system font is Tahoma
    SYSTEM_FIXED_FONT 	Fixed-pitch (monospace) system font. This stock object is provided only for compatibility with 16-bit Windows versions earlier than 3.0.
    DEFAULT_PALETTE 	Default palette. This palette consists of the static colors in the system palette.

Return Values

If the function succeeds, the return value is a handle to the requested logical object.

If the function fails, the return value is NULL.

Windows NT/2000/XP: To get extended error information, call GetLastError.</pre>
     * @throws IllegalAccessException 
     * @throws NativeException 
*/
    public static LONG GetStockObject(int fnObject) throws NativeException, IllegalAccessException {
    	JNative getStockObject = new JNative(DLL_NAME, "GetStockObject");
    	getStockObject.setRetVal(Type.INT);
    	
    	getStockObject.setParameter(0, fnObject);
    	getStockObject.invoke();
    	
    	return new LONG(getStockObject.getRetValAsInt());
    }
   /* 
        The GetObject function retrieves information for the specified graphics object.

        int GetObject(
          HGDIOBJ hgdiobj,  // handle to graphics object
          int cbBuffer,     // size of buffer for object information
          LPVOID lpvObject  // buffer for object information
        );
    **/
    public static int GetObject(int handle, int size, BITMAP info) throws NativeException, IllegalAccessException {
    	JNative GetObject = new JNative(DLL_NAME, "GetObjectA");
    	GetObject.setRetVal(Type.INT);
    	
    	GetObject.setParameter(0, handle);
        GetObject.setParameter(1, size);
        GetObject.setParameter(2, info.getPointer());
    	GetObject.invoke();
    	
    	return GetObject.getRetValAsInt();
    }
    
    /*
    BOOL DeleteDC(
      HDC hdc   // handle to DC
    );
     */
    public static boolean DeleteDC(DC hdc)
			throws NativeException, IllegalAccessException {
		JNative DeleteDC = new JNative(DLL_NAME, "DeleteDC");
        try {
            DeleteDC.setRetVal(Type.INT);
            DeleteDC.setParameter(0, hdc.getValue());
            DeleteDC.invoke();
            return (DeleteDC.getRetValAsInt() != 0);
        } finally {
            if(DeleteDC != null)
                DeleteDC.dispose();
        }
	}
    
     /*
    HDC CreateCompatibleDC(
        HDC hdc   // handle to DC
    );
     */
    public static DC CreateCompatibleDC(DC hdc)
			throws NativeException, IllegalAccessException {
		JNative CreateCompatibleDC = new JNative(DLL_NAME, "CreateCompatibleDC");
        try {
            CreateCompatibleDC.setRetVal(Type.INT);
            CreateCompatibleDC.setParameter(0, hdc.getValue());
            CreateCompatibleDC.invoke();
            return new DC(CreateCompatibleDC.getRetValAsInt());
        } finally {
            if(CreateCompatibleDC != null)
                CreateCompatibleDC.dispose();
        }
	}    
    
    /*
    int GetDIBits(
      HDC hdc,           // handle to DC
      HBITMAP hbmp,      // handle to bitmap
      UINT uStartScan,   // first scan line to set
      UINT cScanLines,   // number of scan lines to copy
      LPVOID lpvBits,    // array for bitmap bits
      LPBITMAPINFO lpbi, // bitmap data buffer
      UINT uUsage        // RGB or palette index
    );
     */
    public static int GetDIBits(DC hdc,           
                              int hbmp,         
                              int uStartScan,   
                              int cScanLines,  
                              Pointer lpvBits,   
                              BITMAPINFOHEADER lpbi,
                              int uUsage)  
                    throws NativeException, IllegalAccessException {
        JNative GetDIBits = new JNative(DLL_NAME, "GetDIBits");
        try {
            GetDIBits.setRetVal(Type.INT);
            int i = 0;

            GetDIBits.setParameter(i++, hdc.getValue());
            GetDIBits.setParameter(i++, hbmp);
            GetDIBits.setParameter(i++, uStartScan);
            GetDIBits.setParameter(i++, lpvBits);
            GetDIBits.setParameter(i++, lpbi.getPointer());
            GetDIBits.setParameter(i++, uUsage);
            GetDIBits.invoke();
            return GetDIBits.getRetValAsInt();
        } finally {
            if(GetDIBits != null)
                GetDIBits.dispose();
        }
    }
	
	/*
	BOOL BitBlt(
	  HDC hdcDest, // handle to destination DC
	  int nXDest,  // x-coord of destination upper-left corner
	  int nYDest,  // y-coord of destination upper-left corner
	  int nWidth,  // width of destination rectangle
	  int nHeight, // height of destination rectangle
	  HDC hdcSrc,  // handle to source DC
	  int nXSrc,   // x-coordinate of source upper-left corner
	  int nYSrc,   // y-coordinate of source upper-left corner
	  DWORD dwRop  // raster operation code
	);
	 */
    public static boolean BitBlt(DC hdcDest,
							  int nXDest,  
							  int nYDest, 
							  int nWidth,  
							  int nHeight, 
							  DC hdcSrc, 
							  int nXSrc, 
							  int nYSrc, 
							  int dwRop) 
								throws NativeException, IllegalAccessException {
		JNative BitBlt = new JNative(DLL_NAME, "BitBlt");
        try {
            BitBlt.setRetVal(Type.INT);
            int i = 0;

            BitBlt.setParameter(i++, hdcDest.getValue());
            BitBlt.setParameter(i++, nXDest);
			BitBlt.setParameter(i++, nYDest);
			BitBlt.setParameter(i++, nWidth);
			BitBlt.setParameter(i++, nHeight);
			BitBlt.setParameter(i++, hdcSrc.getValue());
			BitBlt.setParameter(i++, nXSrc);
			BitBlt.setParameter(i++, nYSrc);
			BitBlt.setParameter(i++, dwRop);
			
            BitBlt.invoke();
            return (BitBlt.getRetValAsInt() != 0);
        } finally {
            if(BitBlt != null)
                BitBlt.dispose();
        }
		
	}    
			
			
    /*
    HGDIOBJ SelectObject(
        HDC hdc,          // handle to DC
        HGDIOBJ hgdiobj   // handle to object
    );
     **/
    public static int SelectObject(DC hdc,           
                              int hgdiobj)  
                    throws NativeException, IllegalAccessException {
        JNative SelectObject = new JNative(DLL_NAME, "SelectObject");
        try {
            SelectObject.setRetVal(Type.INT);
            int i = 0;

            SelectObject.setParameter(i++, hdc.getValue());
            SelectObject.setParameter(i++, hgdiobj);
            SelectObject.invoke();
            return SelectObject.getRetValAsInt();
        } finally {
            if(SelectObject != null)
                SelectObject.dispose();
        }
    }
    
    /*
    BOOL DeleteObject(
      HGDIOBJ hObject   // handle to graphic object
    );
    */
    public static boolean DeleteObject(int hObject) throws NativeException, IllegalAccessException {
        JNative DeleteObject = new JNative(DLL_NAME, "DeleteObject");
        try {
            DeleteObject.setRetVal(Type.INT);
            int i = 0;

            DeleteObject.setParameter(i++, hObject);
            DeleteObject.invoke();
            return (DeleteObject.getRetValAsInt() != 0);
        } finally {
            if(DeleteObject != null)
                DeleteObject.dispose();
        }
    }
    
    
    /*
    HBITMAP CreateCompatibleBitmap(
        HDC hdc,        // handle to DC
        int nWidth,     // width of bitmap, in pixels
        int nHeight     // height of bitmap, in pixels
    );
     */
     public static int CreateCompatibleBitmap(DC hdc,           
                              int nWidth, int nHeight)  
                    throws NativeException, IllegalAccessException {
        JNative CreateCompatibleBitmap = new JNative(DLL_NAME, "CreateCompatibleBitmap");
        try {
            CreateCompatibleBitmap.setRetVal(Type.INT);
            int i = 0;

            CreateCompatibleBitmap.setParameter(i++, hdc.getValue());
            CreateCompatibleBitmap.setParameter(i++, nWidth);
            CreateCompatibleBitmap.setParameter(i++, nHeight);
            CreateCompatibleBitmap.invoke();
            return CreateCompatibleBitmap.getRetValAsInt();
        } finally {
            if(CreateCompatibleBitmap != null)
                CreateCompatibleBitmap.dispose();
        }
    }
    /*
    LONG GetBitmapBits(
      HBITMAP hbmp,      // handle to bitmap
      LONG cbBuffer,     // number of bytes to copy
      LPVOID lpvBits     // buffer to receive bits
    );
     */
    public static int GetBitmapBits(int hbmp,           
                              int cbBuffer, Pointer lpvBits)  
                    throws NativeException, IllegalAccessException {
        JNative GetBitmapBits = new JNative(DLL_NAME, "GetBitmapBits");
        try {
            GetBitmapBits.setRetVal(Type.INT);
            int i = 0;

            GetBitmapBits.setParameter(i++, hbmp);
            GetBitmapBits.setParameter(i++, cbBuffer);
            GetBitmapBits.setParameter(i++, lpvBits);
            GetBitmapBits.invoke();
            return GetBitmapBits.getRetValAsInt();
        } finally {
            if(GetBitmapBits != null)
                GetBitmapBits.dispose();
        }
    }
    
    public static ImageIcon iconHandleToImageIcon(LONG hIcon, boolean destroyIconHandle) throws NativeException, IllegalAccessException {
		
        // get the handles to the icon bitmaps
        final ICONINFO info = new ICONINFO();
        if(!User32.GetIconInfo(hIcon, info)) {
			throw new RuntimeException("User32.GetIconInfo: Could not retrieve IconInfo!");
		}
        // destroy icon
        if(destroyIconHandle)
            User32.DestroyIcon(hIcon);

        // get information about size of the icon
        // also contains a conveniance method for getting a pointer to buffer
        // that is large enough to copy the bitmap-bytes into...
        final BITMAP hBitmap = new BITMAP();
		Gdi32.GetObject(info.getBitmapColor(), hBitmap.getSizeOf(), hBitmap);
		
        // load the icon bitmap into buffer
        final Pointer bitmap = hBitmap.createBitmapBuffer();
        Gdi32.GetBitmapBits(info.getBitmapColor(), bitmap.getSize(), bitmap);

        // Note: dont use bitmap.getAsInt(i*4)! This is very slow as everytime you call getAsInt() 
        // it seems to fetch the complete native buffer which takes a lot of time.
        // So better grab the whole native memory first with .getMemory() and do the processing manually.
        int[] pixels = new int[hBitmap.getRealBitmapSize()];
        byte[] b_Bitmap = bitmap.getMemory();

        // convert the buffer "bytes" to java ints
        for(int i = 0; i<=pixels.length-4; i++) {
            pixels[i] = StructConverter.bytesIntoInt(b_Bitmap, i*4);
        }

        // now create a new image from our converted icon-bytes
        MemoryImageSource mis = new MemoryImageSource(hBitmap.getWidth(), hBitmap.getHeight(), pixels, 0, hBitmap.getWidth());
        final Image _image = Toolkit.getDefaultToolkit().createImage(mis); 
        mis = null;
        pixels = null;
        b_Bitmap = null;
		info.dispose();
        bitmap.dispose();
        hBitmap.getPointer().dispose();
		
        return new ImageIcon(_image);    
    }
    
}
