LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := sqlite3
LOCAL_SRC_FILES := sqlite3.c
include $(BUILD_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := VINDRank
LOCAL_SRC_FILES := VINDRank.c
LOCAL_STATIC_LIBRARIES := sqlite3
include $(BUILD_SHARED_LIBRARY)