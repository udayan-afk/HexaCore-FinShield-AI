package com.aissms.finshield.util

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import com.aissms.finshield.model.AppLanguage

object LocalizationManager {
    private const val PREFS_NAME = "finshield_prefs"
    private const val KEY_LANG = "selected_language"

    fun saveLanguage(context: Context, language: AppLanguage) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANG, language.code).apply()
    }

    fun getSavedLanguage(context: Context): AppLanguage {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val code = prefs.getString(KEY_LANG, "en") ?: "en"
        return when (code) {
            "hi" -> AppLanguage.HINDI
            "mr" -> AppLanguage.MARATHI
            else -> AppLanguage.ENGLISH
        }
    }
}

fun tr(key: String, language: AppLanguage): String {
    val map = when (language) {
        AppLanguage.HINDI -> mapOf(
            "app_name" to "फिनशील्ड",
            "home" to "होम",
            "analyze" to "विश्लेषण",
            "compare" to "तुलना",
            "profile" to "प्रोफाइल",
            "dashboard_greeting" to "नमस्ते, उपयोगकर्ता 👋",
            "dashboard_subtitle" to "स्मार्ट लोन सुरक्षा एवं पारदर्शी विश्लेषण",
            "hero_title" to "ऋण समझौते का विश्लेषण करें",
            "hero_sub" to "शुल्क, छिपा हुआ ब्याज और लोन जोखिम तुरंत समझें",
            "hero_btn" to "समझौते का विश्लेषण करें ✨",
            "quick_actions" to "त्वरित कार्य",
            "calc_title" to "ऋण ईएमआई कैलकुलेटर",
            "calc_sub" to "ईएमआई की जांच करें",
            "compare_title" to "ऋण प्रस्तावों की तुलना करें",
            "compare_sub" to "बेहतर विकल्प खोजें",
            "credit_title" to "क्रेडिट स्कोर मार्गदर्शन",
            "credit_sub" to "प्रोफाइल सुधारें",
            "recent_title" to "हाल के विश्लेषण",
            "view_all" to "सभी देखें",
            "choose_lang" to "अपनी भाषा चुनें",
            "choose_lang_sub" to "जिस भाषा में आप सबसे सहज हैं उसे चुनें।",
            "continue" to "आगे बढ़ें",
            "analyze_title" to "समझौते का विश्लेषण करें",
            "upload_pdf" to "पीडीएफ दस्तावेज़ अपलोड करें",
            "scan_camera" to "कैमरे से स्कैन करें",
            "privacy_title" to "आपकी गोपनीयता सबसे पहले",
            "privacy_desc" to "सभी दस्तावेज़ विश्लेषण डिवाइस पर ही किया जाता है।",
            "compare_hdr" to "ऋण प्रस्तावों की तुलना करें",
            "compare_hdr_sub" to "समान ऋण, अधिक स्पष्ट तुलना",
            "edit" to "संपादित करें",
            "sort_by" to "क्रमबद्ध करें:",
            "lowest_cost" to "कम लागत",
            "lowest_rate" to "कम ब्याज दर",
            "lowest_emi" to "कम ईएमआई",
            "prof_title" to "प्रोफाइल और सेटिंग्स",
            "guest_user" to "अतिथि उपयोगकर्ता",
            "sign_out" to "साइन आउट करें"
        )
        AppLanguage.MARATHI -> mapOf(
            "app_name" to "फिनशील्ड",
            "home" to "होम",
            "analyze" to "विश्लेषण",
            "compare" to "तुलना",
            "profile" to "प्रोफाइल",
            "dashboard_greeting" to "नमस्कार, वापरकर्ता 👋",
            "dashboard_subtitle" to "स्मार्ट कर्ज सुरक्षा आणि पारदर्शक विश्लेषण",
            "hero_title" to "कर्ज कराराचे विश्लेषण करा",
            "hero_sub" to "शुल्क, लपलेले व्याज आणि जोखीम त्वरित समजून घ्या",
            "hero_btn" to "कराराचे विश्लेषण करा ✨",
            "quick_actions" to "जलद कृती",
            "calc_title" to "कर्ज ईएमआय कॅल्क्युलेटर",
            "calc_sub" to "ईएमआय तपासा",
            "compare_title" to "कर्ज ऑफरची तुलना करा",
            "compare_sub" to "उत्तम पर्याय शोधा",
            "credit_title" to "क्रेडिट स्कोर मार्गदर्शन",
            "credit_sub" to "प्रोफाइल सुधारा",
            "recent_title" to "अलीकडील विश्लेषण",
            "view_all" to "सर्व पहा",
            "choose_lang" to "आपली भाषा निवडा",
            "choose_lang_sub" to "तुम्हाला सोयीची वाटणारी भाषा निवडा.",
            "continue" to "पुढे जा",
            "analyze_title" to "कराराचे विश्लेषण करा",
            "upload_pdf" to "पीडीएफ दस्तऐवज अपलोड करा",
            "scan_camera" to "कॅमेऱ्याने स्कॅन करा",
            "privacy_title" to "तुमची गोपनीयता सर्वात महत्त्वाची",
            "privacy_desc" to "सर्व दस्तऐवज विश्लेषण डिव्हाइसवरच केले जाते.",
            "compare_hdr" to "कर्ज ऑफरची तुलना करा",
            "compare_hdr_sub" to "समान कर्ज, अधिक स्पष्ट तुलना",
            "edit" to "संपादन करा",
            "sort_by" to "क्रमवारी लावा:",
            "lowest_cost" to "कमी खर्च",
            "lowest_rate" to "कमी व्याजदर",
            "lowest_emi" to "कमी ईएमआय",
            "prof_title" to "प्रोफाइल आणि सेटिंग्ज",
            "guest_user" to "अतिथी वापरकर्ता",
            "sign_out" to "साइन आउट करा"
        )
        else -> mapOf(
            "app_name" to "FinShield",
            "home" to "Home",
            "analyze" to "Analyze",
            "compare" to "Compare",
            "profile" to "Profile",
            "dashboard_greeting" to "Hello, User 👋",
            "dashboard_subtitle" to "Smart loan safety & transparent analysis",
            "hero_title" to "Analyze loan agreement",
            "hero_sub" to "Understand fees, hidden interest, and loan risk instantly",
            "hero_btn" to "Analyze Agreement ✨",
            "quick_actions" to "Quick actions",
            "calc_title" to "Loan\nCalculator",
            "calc_sub" to "Verify your EMI",
            "compare_title" to "Compare\nLoans",
            "compare_sub" to "Find better value",
            "credit_title" to "Credit Score\nGuidance",
            "credit_sub" to "Improve profile",
            "recent_title" to "Recent analyses",
            "view_all" to "View all",
            "choose_lang" to "Choose your language",
            "choose_lang_sub" to "Select the language you’re most comfortable with.",
            "continue" to "Continue",
            "analyze_title" to "Analyze agreement",
            "upload_pdf" to "Upload PDF document",
            "scan_camera" to "Scan with Camera",
            "privacy_title" to "Your privacy comes first",
            "privacy_desc" to "All document analysis & camera OCR is performed on-device.",
            "compare_hdr" to "Compare loan offers",
            "compare_hdr_sub" to "Same loan, clearer comparison",
            "edit" to "Edit",
            "sort_by" to "Sort by:",
            "lowest_cost" to "Lowest Cost",
            "lowest_rate" to "Lowest Rate",
            "lowest_emi" to "Lowest EMI",
            "prof_title" to "Profile & Settings",
            "guest_user" to "Guest User",
            "sign_out" to "Sign Out / Switch Account"
        )
    }
    return map[key] ?: key
}
