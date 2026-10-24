# Release derlemesinde R8 kullanilmayan kodu siler ve isimleri kisaltir.
# Asagidaki kurallar, calisma aninda yansima (reflection) ile erisilen seyleri korur.

# --- Gson ---
# Gson, JSON alanlarini model siniflarina yansima ile doldurur. Alan adlari
# kisaltilirsa eslestirme bozulur ve butun alanlar null gelir.
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.kampplus.hava.model.** { *; }
-keep class com.kampplus.hava.data.CitySearchResponse { *; }
-keep class com.kampplus.hava.data.ForecastResponse { *; }
-keep class com.kampplus.hava.data.CurrentResponse { *; }
-keep class com.kampplus.hava.data.HourlyResponse { *; }
-keep class com.kampplus.hava.data.DailyResponse { *; }

# @SerializedName ile isaretlenmis alanlar hicbir kosulda yeniden adlandirilmamali.
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Gson'un jenerik tip bilgisini calisma aninda okuyabilmesi icin.
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# --- Retrofit ---
# Servis arayuzleri yansima ile uygulanir.
-keep,allowobfuscation interface com.kampplus.hava.data.CityApi
-keep,allowobfuscation interface com.kampplus.hava.data.ForecastApi
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
  @retrofit2.http.* <methods>;
}

# Retrofit'in opsiyonel bagimliliklarina dair uyarilar derlemeyi kesmesin.
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
