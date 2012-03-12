package name.vbraun.lib.pen;

import java.util.ArrayList;

import junit.framework.Assert;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;

public class Hardware {
	private final static String TAG = "PenHardware";
	
	public static final String KEY_OVERRIDE_PEN_TYPE = "override_pen_type";

	public static final String PEN_TYPE_AUTO = "PEN_TYPE_AUTO";
    public static final String PEN_TYPE_CAPACITIVE = "PEN_TYPE_CAPACITIVE";
    public static final String PEN_TYPE_THINKPAD_TABLET = "PEN_TYPE_THINKPAD_TABLET";
    public static final String PEN_TYPE_ICS = "PEN_TYPE_ICS";

	
	private String model;
	private boolean mHasPenDigitizer;
	private boolean mHasPressureSensor;
	private PenEvent mPenEvent;
	
	private static Hardware instance = null;
	
	public static Hardware getInstance(Context context) {
		if (instance == null)
			instance = new Hardware(context);
		return instance;
	}
	
	private static ArrayList<String> tabletMODELwithoutPressure = new ArrayList<String>() {
		private static final long serialVersionUID = 1868225200818950866L; 	{
	    add("K1");   // Lenovo K1
	    add("A500");  // Acer Iconia A500
	    add("A501");  // Acer Iconia A501
	    add("AT100");   add("AT1S0");   // Toshiba thrive 10" and 7"
	    add("GT-P1000"); add("GT-P1000L"); add("GT-P1000N"); add("SGH-T849"); // Galaxy tab 10.1"
	    add("GT-P7510");  // Galaxy tab 10.1"
	    add("GT-P7501");  // Galaxy tab 10.1N"
	    add("GT-P6810");  // Samsung Galaxy Tab 7.7
	    add("GT-P6210");  // Samsung Galaxy Tab 7.0 Plus
	    add("Galaxy Nexus");  // Google Galaxy Nexus
	}};
	
	private Hardware(Context context) {
		forceFromPreferences(context);
		Log.v(TAG, "Model = >"+model+"<, pen digitizer: "+mHasPenDigitizer);
	}
	
	public void autodetect(Context context) {
		model = android.os.Build.MODEL;
		if (model.equalsIgnoreCase("ThinkPad Tablet")) {  // Lenovo ThinkPad Tablet
			forceThinkpadTablet();
		} else {
			// defaults; this works on HTC devices but might be more general
			mHasPenDigitizer = context.getPackageManager().hasSystemFeature("android.hardware.touchscreen.pen");
			mHasPressureSensor = !tabletMODELwithoutPressure.contains(model);
			if (mHasPenDigitizer) 
				mPenEvent = new PenEventICS();
			else
				mPenEvent = new PenEvent();
		}
	}
	
	public void forceThinkpadTablet() {
		mHasPenDigitizer = true;
		mHasPressureSensor = true;
		mPenEvent = new PenEventThinkPadTablet();
	}
	
	public void forceCapacitivePen() {
		mHasPenDigitizer = false;
		mHasPressureSensor = true;
		mPenEvent = new PenEvent();
	}
	
	public void forceICS() {
		mHasPenDigitizer = true;
		mHasPressureSensor = true;
		mPenEvent = new PenEventICS();
	}
	
	public void forceFromPreferences(Context context) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		String penType = settings.getString(KEY_OVERRIDE_PEN_TYPE, PEN_TYPE_AUTO);
		if (penType.equals(PEN_TYPE_AUTO))
			autodetect(context);
		else if (penType.equals(PEN_TYPE_CAPACITIVE))
			forceCapacitivePen();
		else if (penType.equals(PEN_TYPE_THINKPAD_TABLET))
			forceThinkpadTablet();
		else if (penType.equals(PEN_TYPE_ICS))
			forceICS();
		else
			Assert.fail("The preference "+KEY_OVERRIDE_PEN_TYPE+" has invalid value: "+penType);
	}
	
	/**
	 * Test whether the device has an active pen
	 * @return boolean
	 */
	public static boolean hasPenDigitizer() {
		Assert.assertNotNull(instance);
		return instance.mHasPenDigitizer;
	}
	
	/**
	 * Test whether the device has a working pressure sensor
	 * @return
	 */
	public static boolean hasPressureSensor() {
		Assert.assertNotNull(instance);
		return instance.mHasPressureSensor;
	}

	/**
	 * Test whether the event may have been caused by the stylus
	 * @param event A MotionEvent
	 * @return
	 */
	public static boolean isPenEvent(MotionEvent event) {
		Assert.assertNotNull(instance);
		return instance.mPenEvent.isPenEvent(event);
	}
}
