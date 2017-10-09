package com.oxy.easilyhttpengine.log;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;

public class Logger {
	public static boolean DEBUG = true;

	private static final String DEFAULT_TAG = "ouxiao";
	private static final boolean RECYCLE_DETAIL = false;
	public static void LOG(Object tag, String message) {

		if (DEBUG) {
			if (tag instanceof String) {
				Log.v((String) tag, message);
				return;
			}
			String t = tag == null ? DEFAULT_TAG : tag.getClass()
					.getSimpleName();
			Log.v(t, message);
		}

	}

	// 打印bean对象的数据，方便调试使用 
	public static String toDataStr(Object bean) {
		if (bean != null) {
			StringBuilder sb = new StringBuilder();
			Class<?> clazz = bean.getClass();
			Field[] fields = clazz.getDeclaredFields();
			sb.append(clazz.getSimpleName());
			sb.append("[");
			for (Field f : fields) {
				sb.append(f.getName());
				sb.append(" = ");
				try {
					f.setAccessible(true);
					sb.append(String.valueOf(f.get(bean)));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				sb.append(",");
			}
			if (sb.lastIndexOf(",") == sb.length() - 1) {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("]");
			return sb.toString();
		}
		return "";
	}

	public static void recycleDebugLoggger(Bitmap bitmap, String tag) {

		if (DEBUG) {
			Log.i("hecao_r",
					"=====" + (bitmap == null ? "null" : bitmap.toString())
							+ " tag : " + tag);
			if (RECYCLE_DETAIL) {
				try {
					throw new RuntimeException();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void d(boolean debug, String msg) {
		if (debug) {
			Log.v(DEFAULT_TAG, msg);
		}
	}

	// /////////////////////////////////////////////////

	// public final static String SEND_PHOTO = "send_photo";
	// public final static String SEND_TEXT = "send_text";
	// public final static String SEND_VOICE = "send_voice";
	// public final static String GET_VOICE = "get_voice";
	// public final static String RECEVICE_MESSAGE = "recevice_message";
	// public final static String INPUT_BAR = "input_bar";
	// public final static String PLAY_VOICE = "play_voice";
	// public final static String RECORD = "record";

	public static void d(String str) {
		if (DEBUG) {
			int len;
			if(!TextUtils.isEmpty(str) && (len = str.length()) > 3900){
				int count = (int) Math.ceil(len * 1.0f / 3900);
				int start = 0;
				int last;
				for (int i = 0 ;i < count ; i++){
					if(i == count - 1){
						last = len;
					}else{
						last = start + 3900;
					}

					Log.i(DEFAULT_TAG, getTAG() + "---" + str.substring(start,last));
					start = last;
				}
			}else{
				Log.i(DEFAULT_TAG, getTAG() + "---" + str);
			}
		}
	}

	public static void d(Throwable e){
		Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        e.printStackTrace(printWriter);
        d(info.toString());
	}

	public static void d(String tag, Throwable e){
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		e.printStackTrace(printWriter);
		d(tag,info.toString());
	}

	public static void d(String tag, String str) {
		if (DEBUG) {
			Log.i(tag, getTAG() + "---" + str);
		}
	}

	public static void errord(String str) {
		Log.e(DEFAULT_TAG, getTAG() + "---" + str);
	}

	public static void errord(String tag, String str) {
		if (DEBUG) {
			Log.e(tag, getTAG() + "---" + str);
		}
	}

	public static void mark() {
		if (DEBUG) {
			Log.w(DEFAULT_TAG, getTAG());
		}
	}

	public static void mark(String tag, String str) {
		if (DEBUG) {
			Log.w(tag, getTAG() + "---" + str);
		}
	}

	public static void traces() {
		if (DEBUG) {
			// StackTraceElement stack[] = (new Throwable()).getStackTrace();
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getClassName() + "." + ste.getMethodName()
						+ "#line=" + ste.getLineNumber() + "的调用：\n");
				for (int i = 4; i < stacks.length && i < 15; i++) {
					ste = stacks[i];
					sb.append((i - 4) + "--" + ste.getClassName() + "."
							+ ste.getMethodName() + "(...)#line:"
							+ ste.getLineNumber() + "\n");
				}
			}
			Log.w(DEFAULT_TAG, getTAG() + "--" + sb.toString());
		}

	}

	public static void traces(String tag) {
		if (DEBUG) {
			// StackTraceElement stack[] = (new Throwable()).getStackTrace();
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getClassName() + "." + ste.getMethodName()
						+ "#line=" + ste.getLineNumber() + "的调用：\n");
				for (int i = 4; i < stacks.length && i < 15; i++) {
					ste = stacks[i];
					sb.append((i - 4) + "--" + ste.getClassName() + "."
							+ ste.getMethodName() + "(...)#line:"
							+ ste.getLineNumber() + "\n");
				}
			}
			Log.w(tag, getTAG() + "--" + sb.toString());
		}

	}

	public static String getTAG() {
		// XXX this not work with proguard, maybe we cannot get the line number
		// with a proguarded jar file.
		// I add a try/catch as a quick fixing.
		try {
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null && stacks.length >= 5) {
				StackTraceElement ste = stacks[4];
				String fileName = ste.getFileName();
				sb.append(fileName.subSequence(0,
						fileName.length() - 5)
						+ "." + ste.getMethodName() + "#" + ste.getLineNumber());
			}
			return sb.toString();
		} catch (NullPointerException e) {
			return "PROGUARDED";
		}
	}

	public static void log(String str, byte[] bytes) {
		if (DEBUG) {
			StringBuilder sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (bytes != null) {
				for (int i = 0; i < bytes.length; i++) {
					sb.append(Integer.toHexString(bytes[i]));
					if (i != bytes.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.i(DEFAULT_TAG, getTAG() + "---" + sb.toString());
		}
	}
	
	public static void log(String str, boolean[] bs) {
		if (DEBUG) {
			StringBuilder sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (bs != null) {
				for (int i = 0; i < bs.length; i++) {
					sb.append(bs[i]);
					if (i != bs.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.i(DEFAULT_TAG, getTAG() + "---" + sb.toString());
		}
	}
	
	public static void errord(String str, boolean[] bs) {
		if (DEBUG) {
			StringBuilder sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (bs != null) {
				for (int i = 0; i < bs.length; i++) {
					sb.append(bs[i]);
					if (i != bs.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.e(DEFAULT_TAG, getTAG() + "---" + sb.toString());
		}
	}

	public static void log(String str, short[] shorts) {
		if (DEBUG) {
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getFileName() + "." + ste.getMethodName() + "#"
						+ ste.getLineNumber());
			}
			String tmpTAG = sb.toString();
			sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (shorts != null) {
				for (int i = 0; i < shorts.length; i++) {
					// sb.append(Integer.toHexString(shorts[i]));
					sb.append(shorts[i]);
					if (i != shorts.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.i(DEFAULT_TAG, tmpTAG + "---" + sb.toString());
		}
	}

	public static void log(String str, int[] ints) {
		if (DEBUG) {
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getFileName() + "." + ste.getMethodName() + "#"
						+ ste.getLineNumber());
			}
			String tmpTAG = sb.toString();
			sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (ints != null) {
				for (int i = 0; i < ints.length; i++) {
					// sb.append(Integer.toHexString(shorts[i]));
					sb.append(ints[i]);
					if (i != ints.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.i(DEFAULT_TAG, tmpTAG + "---" + sb.toString());
		}
	}

	public static void log(String str, String[] strary) {
		if (DEBUG) {
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getFileName() + "." + ste.getMethodName() + "#"
						+ ste.getLineNumber());
			}
			String tmpTAG = sb.toString();
			sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (str != null) {
				for (int i = 0; i < strary.length; i++) {
					// sb.append(Integer.toHexString(shorts[i]));
					sb.append(strary[i]);
					if (i != strary.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.i(DEFAULT_TAG, tmpTAG + "---" + sb.toString());
		}
	}
	
	public static void errord(String str, String[] strary) {
		if (DEBUG) {
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getFileName() + "." + ste.getMethodName() + "#"
						+ ste.getLineNumber());
			}
			String tmpTAG = sb.toString();
			sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (str != null) {
				for (int i = 0; i < strary.length; i++) {
					// sb.append(Integer.toHexString(shorts[i]));
					sb.append(strary[i]);
					if (i != strary.length - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.e(DEFAULT_TAG, tmpTAG + "---" + sb.toString());
		}
	}

	public static void log(String str, List list) {
		if (DEBUG) {
			StackTraceElement stacks[] = Thread.currentThread().getStackTrace();
			StringBuilder sb = new StringBuilder();
			if (stacks != null) {
				StackTraceElement ste = stacks[3];
				sb.append(ste.getFileName() + "." + ste.getMethodName() + "#"
						+ ste.getLineNumber());
			}
			String tmpTAG = sb.toString();
			sb = new StringBuilder();
			sb.append(str).append('=');
			sb.append('[');
			if (list != null) {
				int size = list.size();
				for (int i = 0; i < size; i++) {
					sb.append(list.get(i).toString());
					if (i != size - 1) {
						sb.append(',');
					}
				}
			}
			sb.append(']');
			Log.i(DEFAULT_TAG, tmpTAG + "---" + sb.toString());
		}
	}

	@SuppressLint("NewApi")
	public static void logStrictModeThread() {
		if (DEBUG && Build.VERSION.SDK_INT >= 10) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyLog().build());
		}
	}

}
