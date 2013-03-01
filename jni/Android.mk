LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := sqlite4java-android-armv7
LOCAL_SRC_FILES := libsqlite4java-android-armv7.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := VINDRank
LOCAL_SRC_FILES := VINDRank.c
LOCAL_SHARED_LIBRARIES := sqlite4java-android-armv7
include $(BUILD_SHARED_LIBRARY)