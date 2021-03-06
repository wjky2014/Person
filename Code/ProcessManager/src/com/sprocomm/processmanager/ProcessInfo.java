package com.sprocomm.processmanager;

import android.graphics.drawable.Drawable;

public class ProcessInfo {

	public String name;
	public Drawable icon;
	public String packageName;
	public boolean isAllownRun;
	public boolean isCheck;
	public boolean isSystem;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isAllownRun() {
		return isAllownRun;
	}

	public void setAllownRun(boolean isAllownRun) {
		this.isAllownRun = isAllownRun;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public boolean isSystem() {
		return isSystem;
	}

	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}

	@Override
	public String toString() {
		return "ProcessInfo [name=" + name + ", icon=" + icon
				+ ", packageName=" + packageName + ", isAllownRun="
				+ isAllownRun + ", isCheck=" + isCheck + ", isSystem="
				+ isSystem + "]";
	}

	public ProcessInfo(String name, Drawable icon, String packageName,
			boolean isAllownRun, boolean isCheck, boolean isSystem) {
		super();
		this.name = name;
		this.icon = icon;
		this.packageName = packageName;
		this.isAllownRun = isAllownRun;
		this.isCheck = isCheck;
		this.isSystem = isSystem;
	}

	public ProcessInfo() {
		super();
	}

}
