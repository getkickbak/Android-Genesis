package com.phonegap.plugins.twitter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.PluginResult;

/**
 * Twitter plugin for Android
 * Inspired of the iOS plugin: https://github.com/phonegap/phonegap-plugins/tree/master/iPhone/Twitter
 * 
 * @see http://regis.decamps.info/blog/2011/06/intent-to-open-twitter-client-on-android/
 * @see http://blogrescue.com/2011/12/android-development-send-tweet-action/
 * @author Julien Roche
 * @version 1.0
 */
public class Twitter extends CordovaPlugin
{
	// Constants
	/** ComposeTweet method's name */
	private static final String        METHOD_COMPOSE_TWEET        = "composeTweet";

	/** IsTwitterAvailable method's name */
	private static final String        METHOD_IS_TWITTER_AVAILABLE = "isTwitterAvailable";

	/** List of Twitter's applications with theirs linked send Activity */
	private static Map<String, String> TWITTER_APPS;

	/** List of available methods */
	private static final String[]      AVAILABLE_METHODS           = new String[] { METHOD_COMPOSE_TWEET,
	      METHOD_IS_TWITTER_AVAILABLE                             };

	public static final int            REQUEST_CODE                = 0x0ba7c0da;
	private CallbackContext            callbackContext;

	static
	{
		TWITTER_APPS = new LinkedHashMap<String, String>();
		TWITTER_APPS.put("Twitter", "com.twitter.android.PostActivity");
		TWITTER_APPS.put("TweetCaster", "com.handmark.tweetcaster.NewTwitActivity");
		TWITTER_APPS.put("TweetCaster2", "com.handmark.tweetcaster.ShareSelectorActivity");
		TWITTER_APPS.put("UberSocial", "com.twidroid.activity.SendTweet");
		TWITTER_APPS.put("TweetDeck", "com.tweetdeck.compose.ComposeActivity");
		TWITTER_APPS.put("Seesmic", "com.seesmic.ui.Composer");
		TWITTER_APPS.put("Plume", "com.levelup.touiteur.appwidgets.TouiteurWidgetNewTweet");
		TWITTER_APPS.put("Twicca", "jp.r246.twicca.statuses.Send");
	}

	/**
	 * @param args
	 * @param callbackId
	 * @return A PluginResult object with a status and message.
	 */
	public void composeTweet(JSONArray args, final CallbackContext callbackContext)
	{
		ResolveInfo resolveInfo = getTwitterResolveInfo();
		String message;

		if (resolveInfo == null)
		{
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Twitter is not available"));
			return;
		}

		if (args.length() <= 0)
		{
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "No parameter was specified"));
			return;
		}

		try
		{
			message = args.getString(0);

		}
		catch (JSONException e)
		{
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Error with the message"));
			return;
		}

		final ActivityInfo activity = resolveInfo.activityInfo;
		final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
		String p = resolveInfo.activityInfo.packageName;

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT, message);
		intent.setType(p != null && p.startsWith("com.twidroid") ? "application/twitter" : "text/plain");
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.setComponent(name);
		this.cordova.startActivityForResult(this, intent, REQUEST_CODE);
		this.callbackContext = callbackContext;
		// this.cordova.startActivity(intent);

	}

	/**
	 * Called when the barcode scanner intent completes
	 * 
	 * @param requestCode
	 *           The request code originally supplied to startActivityForResult(),
	 *           allowing you to identify who this result came from.
	 * @param resultCode
	 *           The integer result code returned by the child activity through its setResult().
	 * @param intent
	 *           An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if (requestCode == REQUEST_CODE)
		{
			Log.i("WebIntent", "Activity Result - (" + resultCode + ")");
			if (resultCode == Activity.RESULT_OK)
			{
				this.callbackContext.success();
			}
			else
			{
				this.callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Activity was not started"));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.phonegap.api.Plugin#execute(java.lang.String, org.json.JSONArray, java.lang.String)
	 */
	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException
	{
		if (action != null && Arrays.binarySearch(AVAILABLE_METHODS, action) >= 0)
		{
			if (METHOD_IS_TWITTER_AVAILABLE.equals(action))
			{
				callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, isTwitterAvailable()));
				return true;
			}
			else if (METHOD_COMPOSE_TWEET.equals(action))
			{
				composeTweet(args, callbackContext);
				return true;
			}
		}

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "This method is not available"));
		return false;
	}

	/**
	 * Get the Twitter's {@link ResolveInfo}
	 * 
	 * @return the Twitter's {@link ResolveInfo}
	 */
	public ResolveInfo getTwitterResolveInfo()
	{
		try
		{
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_TEXT, "Test; please ignore");
			intent.setType("text/plain");

			final PackageManager pm = this.cordova.getActivity().getBaseContext().getPackageManager();
			for (ResolveInfo resolveInfo : pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY))
			{
				ActivityInfo activity = resolveInfo.activityInfo;
				if (TWITTER_APPS.containsValue(activity.name)) { return resolveInfo; }
			}

		}
		finally
		{

		}

		return null;
	}

	/**
	 * Check if the Twitter is available
	 * 
	 * @return true if a Twitter activity is detected
	 */
	public boolean isTwitterAvailable()
	{
		return getTwitterResolveInfo() != null;
	}
}
