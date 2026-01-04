# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.google.appengine.api.urlfetch.FetchOptions$Builder
-dontwarn com.google.appengine.api.urlfetch.FetchOptions
-dontwarn com.google.appengine.api.urlfetch.HTTPHeader
-dontwarn com.google.appengine.api.urlfetch.HTTPMethod
-dontwarn com.google.appengine.api.urlfetch.HTTPRequest
-dontwarn com.google.appengine.api.urlfetch.HTTPResponse
-dontwarn com.google.appengine.api.urlfetch.URLFetchService
-dontwarn com.google.appengine.api.urlfetch.URLFetchServiceFactory
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn reactor.blockhound.integration.BlockHoundIntegration
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean
-dontwarn kotlin.Unit
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-dontwarn javax.annotation.**
-dontwarn java.beans.**
# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

-dontwarn org.apache.log4j.Level
-dontwarn org.apache.log4j.Logger
-dontwarn org.apache.log4j.Priority
-dontwarn org.apache.logging.log4j.Level
-dontwarn org.apache.logging.log4j.LogManager
-dontwarn org.apache.logging.log4j.Logger
-dontwarn org.apache.logging.log4j.message.MessageFactory
-dontwarn org.apache.logging.log4j.spi.ExtendedLogger
-dontwarn org.apache.logging.log4j.spi.ExtendedLoggerWrapper
-dontwarn org.eclipse.jetty.alpn.ALPN$ClientProvider
-dontwarn org.eclipse.jetty.alpn.ALPN$Provider
-dontwarn org.eclipse.jetty.alpn.ALPN$ServerProvider
-dontwarn org.eclipse.jetty.alpn.ALPN
-dontwarn org.eclipse.jetty.npn.NextProtoNego$ClientProvider
-dontwarn org.eclipse.jetty.npn.NextProtoNego$Provider
-dontwarn org.eclipse.jetty.npn.NextProtoNego$ServerProvider
-dontwarn org.eclipse.jetty.npn.NextProtoNego
-dontwarn com.google.appengine.api.datastore.Blob
-dontwarn com.google.appengine.api.datastore.DatastoreService
-dontwarn com.google.appengine.api.datastore.DatastoreServiceFactory
-dontwarn com.google.appengine.api.datastore.Entity
-dontwarn com.google.appengine.api.datastore.EntityNotFoundException
-dontwarn com.google.appengine.api.datastore.Key
-dontwarn com.google.appengine.api.datastore.KeyFactory
-dontwarn com.google.appengine.api.datastore.PreparedQuery
-dontwarn com.google.appengine.api.datastore.Query
-dontwarn com.google.appengine.api.memcache.Expiration
-dontwarn com.google.appengine.api.memcache.MemcacheService
-dontwarn com.google.appengine.api.memcache.MemcacheServiceFactory
-dontwarn javax.naming.InvalidNameException
-dontwarn javax.naming.NamingException
-dontwarn javax.naming.directory.Attribute
-dontwarn javax.naming.directory.Attributes
-dontwarn javax.naming.ldap.LdapName
-dontwarn javax.naming.ldap.Rdn
-dontwarn org.ietf.jgss.GSSContext
-dontwarn org.ietf.jgss.GSSCredential
-dontwarn org.ietf.jgss.GSSException
-dontwarn org.ietf.jgss.GSSManager
-dontwarn org.ietf.jgss.GSSName
-dontwarn org.ietf.jgss.Oid
-dontwarn autovalue.shaded.com.**
-dontwarn com.google.auto.value.**
-dontwarn autovalue.shaded.com.google.**

-keepnames class * extends java.io.Serializable

# Hilt 난독화 해제
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class javax.annotation.** { *; }
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel
-keepclassmembers class * {
    @android.annotation.Keep *;
}

# Kotlin Coroutine (옵션)
-keepclassmembers class kotlinx.** {
    volatile <fields>;
}

# kotlin Serialization 난독화 해제
-keep class kotlinx.serialization.** { *; }

# Google Cloud Translate API 클래스 보호
-keep class com.google.cloud.translate.** { *; }
# Google Cloud Service 관련 클래스 보호
-keep class com.google.api.services.translate.** { *; }
# API 키 관련 클래스 보호
-keep class com.google.cloud.translate.TranslateOptions { *; }

# Google API Client 관련 클래스 보호
-keep class com.google.api.client.util.** { *; }
-keep class com.google.api.client.json.** { *; }
-keep class com.google.api.client.http.** { *; }

# Gson 관련 클래스 보호
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# OAuth2 관련 클래스 보호
-keep class com.google.api.client.auth.oauth2.** { *; }

# 리플렉션 및 어노테이션 관련 필드 보호
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault
-keepclassmembers class * {
    @com.google.api.client.util.Key <fields>;
}

# Ktor
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.atomicfu.**
-dontwarn io.netty.**
-dontwarn com.typesafe.**
-dontwarn org.slf4j.**
-keep class kotlin.reflect.jvm.internal.** { *; }

# Kotlin Symbol Processing (KSP)와 관련된 설정
-keep class com.google.devtools.ksp.processing.** { *; }
-keep class com.google.devtools.ksp.symbol.** { *; }
-keep class com.google.devtools.ksp.processing.SymbolProcessorProvider { *; }


# 개인 프로젝트 난독화 해제
-keep class devgyu.koreAi.presentation.annotation.DefaultPreview
-keepnames class devgyu.koreAi.presentation.model.navigation.** { *;}
-keepnames class devgyu.koreAi.presentation.screen.** { *; }
-keepnames class devgyu.koreAi.presentation.viewmodel.base.** { *; }

-keep class devgyu.koreAi.presentation.model.** {*;}
-keep class devgyu.koreAi.data.api.** { *; }
-keep class devgyu.koreAi.data.dto.** { *; }
-keep class devgyu.koreAi.domain.model.** {*;}
-keep class devgyu.koreAi.domain.entity.** {*;}
-keep class devgyu.koreAi.data.ktor.** { *; }