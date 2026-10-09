package com.aissms.finshield.model

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val subtitle: String
) {
    ENGLISH("en", "English", "English", "Default language for agreement analysis"),
    HINDI("hi", "Hindi", "हिंदी", "ऋण समझौते, शर्तें और जोखिम विश्लेषण"),
    MARATHI("mr", "Marathi", "मराठी", "कर्ज करार, अटी आणि जोखीम विश्लेषण")
}
