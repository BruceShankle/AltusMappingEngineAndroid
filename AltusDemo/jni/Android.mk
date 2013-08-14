LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := AltusDemo
LOCAL_SRC_FILES := AltusDemo.cpp

include $(BUILD_SHARED_LIBRARY)
