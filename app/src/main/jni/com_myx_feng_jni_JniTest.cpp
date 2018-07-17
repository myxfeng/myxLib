

#include "com_myx_feng_jni_JniTest.h"
/*
 * Class:     com_myx_feng_jni_JniTest
 * Method:    getTest
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_myx_feng_jni_JniTest_getTest
  (JNIEnv * env, jobject){
        return env ->NewStringUTF("dasdasdasda");
  }


