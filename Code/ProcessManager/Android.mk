LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
LOCAL_PACKAGE_NAME := ProcessManager
LOCAL_SRC_FILES := $(call all-subdir-java-files)
LOCAL_CERTIFICATE := platform
LOCAL_DEX_PREOPT:= true	  
LOCAL_MODULE_PATH := $(TARGET_OUT)/priv-app
include $(BUILD_PACKAGE)