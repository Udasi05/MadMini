package com.example.myminiproject.data.model

data class Scheme(
    val id: Int,
    val name: String,
    val fullName: String,
    val category: String,
    val emoji: String,
    val benefit: String,
    val benefitColorHex: Long,
    val description: String,
    val eligibility: String,
    val howToApply: String,
    val documents: List<String>,
    val tag: String,
    val tagColorHex: Long
) {
    companion object {
        val categories = listOf("All", "Farmers", "Business", "Health", "Housing", "Women")

        fun getAllSchemes(): List<Scheme> = listOf(
            Scheme(
                id = 1, name = "PM-KISAN", fullName = "Pradhan Mantri Kisan Samman Nidhi",
                category = "Farmers", emoji = "🌾", benefit = "₹6,000/year", benefitColorHex = 0xFF10B981,
                description = "Direct income support to small and marginal farmers. ₹6,000 per year is given in 3 installments of ₹2,000 each directly to bank accounts.",
                eligibility = "All landholding farmers with less than 2 hectares land",
                howToApply = "Visit nearest Common Service Centre (CSC) or apply online at pmkisan.gov.in",
                documents = listOf("Aadhaar Card", "Land records (Khasra/Khatauni)", "Bank account details"),
                tag = "Popular", tagColorHex = 0xFF10B981
            ),
            Scheme(
                id = 2, name = "Kisan Credit Card", fullName = "Kisan Credit Card (KCC)",
                category = "Farmers", emoji = "💳", benefit = "Loan at 4%", benefitColorHex = 0xFF2563EB,
                description = "Short-term credit facility for farmers to meet agricultural and personal needs. Interest subvention makes it effectively 4% per annum.",
                eligibility = "All farmers, fishermen, and animal husbandry farmers",
                howToApply = "Visit any nationalized bank or Regional Rural Bank (RRB) with land documents",
                documents = listOf("Aadhaar Card", "Land records", "Passport photo"),
                tag = "Low Interest", tagColorHex = 0xFF2563EB
            ),
            Scheme(
                id = 3, name = "PM Mudra Loan", fullName = "Pradhan Mantri MUDRA Yojana",
                category = "Business", emoji = "🏪", benefit = "Up to ₹10 lakh", benefitColorHex = 0xFF8B5CF6,
                description = "Collateral-free loans for small businesses. Three categories: Shishu (up to ₹50K), Kishore (₹50K-5L), Tarun (₹5L-10L).",
                eligibility = "Non-farm micro and small enterprises",
                howToApply = "Apply at any bank, NBFC, or MFI. Online at mudra.org.in",
                documents = listOf("Aadhaar Card", "PAN Card", "Business proof", "Bank statement"),
                tag = "No Collateral", tagColorHex = 0xFF8B5CF6
            ),
            Scheme(
                id = 4, name = "PM SVANidhi", fullName = "PM Street Vendor's AtmaNirbhar Nidhi",
                category = "Business", emoji = "🛒", benefit = "₹10K - ₹50K", benefitColorHex = 0xFFF59E0B,
                description = "Collateral-free working capital loans for street vendors. Start with ₹10,000, scale up to ₹50,000 with timely repayment.",
                eligibility = "Street vendors with vending certificate",
                howToApply = "Apply on pmsvanidhi.mohua.gov.in or through city's Urban Local Body",
                documents = listOf("Aadhaar Card", "Vendor ID/Certificate", "Bank account"),
                tag = "Street Vendors", tagColorHex = 0xFFF59E0B
            ),
            Scheme(
                id = 5, name = "Ayushman Bharat", fullName = "Ayushman Bharat PM-JAY",
                category = "Health", emoji = "🏥", benefit = "₹5 lakh/year", benefitColorHex = 0xFFEF4444,
                description = "Free health cover of ₹5 lakh per family per year for secondary and tertiary hospitalization. Covers 1,500+ medical procedures.",
                eligibility = "Poor and vulnerable families as per SECC 2011 data",
                howToApply = "Visit empanelled hospital with Aadhaar. Check eligibility at pmjay.gov.in",
                documents = listOf("Aadhaar Card", "Ration Card"),
                tag = "Free Healthcare", tagColorHex = 0xFFEF4444
            ),
            Scheme(
                id = 6, name = "PMJJBY", fullName = "Pradhan Mantri Jeevan Jyoti Bima Yojana",
                category = "Health", emoji = "🛡️", benefit = "₹2 lakh cover", benefitColorHex = 0xFF10B981,
                description = "Life insurance cover of ₹2 lakh for death due to any cause. Premium is only ₹330 per year — less than ₹1 per day!",
                eligibility = "Age 18-50 years with bank account",
                howToApply = "Apply at your bank branch or through net banking",
                documents = listOf("Aadhaar Card", "Bank account"),
                tag = "₹330/year only", tagColorHex = 0xFF10B981
            ),
            Scheme(
                id = 7, name = "PMAY-Gramin", fullName = "Pradhan Mantri Awas Yojana - Gramin",
                category = "Housing", emoji = "🏠", benefit = "₹1.2-1.3 lakh", benefitColorHex = 0xFF0891B2,
                description = "Financial assistance for construction of pucca houses for rural poor. ₹1.2 lakh in plains and ₹1.3 lakh in hilly areas.",
                eligibility = "Houseless or those living in kutcha houses as per SECC data",
                howToApply = "Contact your Gram Panchayat or Block Development Officer",
                documents = listOf("Aadhaar Card", "Bank account", "Land documents"),
                tag = "Housing", tagColorHex = 0xFF0891B2
            ),
            Scheme(
                id = 8, name = "Ujjwala Yojana", fullName = "Pradhan Mantri Ujjwala Yojana",
                category = "Women", emoji = "🔥", benefit = "Free LPG connection", benefitColorHex = 0xFFF97316,
                description = "Free LPG connection to women from BPL households. Includes a free gas stove and first cylinder refill.",
                eligibility = "Women from BPL families not having existing LPG connection",
                howToApply = "Visit nearest LPG distributor with KYC documents",
                documents = listOf("Aadhaar Card", "BPL Ration Card", "Bank account"),
                tag = "Free for Women", tagColorHex = 0xFFF97316
            ),
            Scheme(
                id = 9, name = "Jan Dhan Yojana", fullName = "Pradhan Mantri Jan Dhan Yojana",
                category = "Women", emoji = "🏦", benefit = "Zero balance account", benefitColorHex = 0xFF6366F1,
                description = "Free basic bank account with zero minimum balance. Includes free RuPay debit card with ₹1 lakh accident insurance.",
                eligibility = "Any Indian citizen above 10 years of age",
                howToApply = "Visit any bank with Aadhaar card",
                documents = listOf("Aadhaar Card", "Passport photo"),
                tag = "Everyone", tagColorHex = 0xFF6366F1
            )
        )
    }
}
