package io.github.danifascio;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BundleManager {

	private static final Map<String, ResourceBundle> bundleMap = new HashMap<>();

	private BundleManager() {

	}

	public static ResourceBundle load(String bundlePath, boolean isXml) {

		if(!bundleMap.containsKey(bundlePath)) {
			if(isXml)
				bundleMap.put(bundlePath, ResourceBundle.getBundle(bundlePath, new XMLResourceBundleControl()));
			else
				bundleMap.put(bundlePath, ResourceBundle.getBundle(bundlePath));
		}

		return bundleMap.get(bundlePath);
	}

	public static void loadAll(String... bundlePaths) {

		for(String bundlePath : bundlePaths)
			if(!bundleMap.containsKey(bundlePath))
				bundleMap.put(bundlePath, ResourceBundle.getBundle(bundlePath));

	}

	public static void loadAllXml(String... bundlePaths) {

		for(String bundlePath : bundlePaths)
			if(!bundleMap.containsKey(bundlePath))
				bundleMap.put(bundlePath, ResourceBundle.getBundle(bundlePath, new XMLResourceBundleControl()));

	}

	public static @Nullable ResourceBundle get(String bundlePath) {

		return bundleMap.get(bundlePath);

	}

}
