package com.example.canvasmemo.view.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class Util {

	public static <T extends View> List<T> findViewsWithClass(View v,
			Class<T> clazz) {
		List<T> views = new ArrayList<T>();
		findViewsWithClass(v, clazz.getName(), views);
		return views;
	}

	private static <T extends View> void findViewsWithClass(View v,
			String clazz, List<T> views) {
		if (v.getClass().getName().equals(clazz)) {
			views.add((T) v);
		}
		if (v instanceof ViewGroup) {
			ViewGroup g = (ViewGroup) v;
			for (int i = 0; i < g.getChildCount(); i++) {
				findViewsWithClass(g.getChildAt(i), clazz, views);
			}
		}
	}

	public static List<View> findViewsWithClassName(View v, String className) {
		List<View> views = new ArrayList<View>();
		findViewsWithClass(v, className, views);
		return views;
	}

	public static void actionBarSetVisiblity(Activity activity) {
		View root = activity.getWindow().getDecorView();
		List<View> views = Util.findViewsWithClassName(root, "com.android.internal.widget.ActionBarContainer");
		for(View v : views){
		  v.setVisibility(View.GONE);
		}
	}

	public final static void actionBarUpsideDown(Activity activity) {
		  View root = activity.getWindow().getDecorView();
		  View firstChild = ((ViewGroup) root).getChildAt(0);
		  if (firstChild instanceof ViewGroup) {
		   ViewGroup viewGroup = (ViewGroup) firstChild;
		   List<View> views = Util.findViewsWithClassName(root, "com.android.internal.widget.ActionBarContainer");
		   if (!views.isEmpty()) {
		    for (View vv : views) {
		     viewGroup.removeView(vv);
		    }
		    for (View vv : views) {
		     viewGroup.addView(vv);
		    }
		   }
		  }
		  else{
		   Log.e("TAG", "first child is not ViewGroup.");
		  }
		 }

 }
