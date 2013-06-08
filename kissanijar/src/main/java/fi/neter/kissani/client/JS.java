package fi.neter.kissani.client;

public abstract class JS {
	public static native void debug(String msg) /*-{
	  console.debug(msg);
	}-*/;

	public static native String getAccessToken() /*-{
	   return FB.getAuthResponse().accessToken;
	}-*/;
	
	public static native String uid() /*-{
	   return FB.getAuthResponse().userID;
	}-*/;

}
