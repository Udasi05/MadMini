package com.example.myminiproject.data.model

data class Lesson(
    val id: Int,
    val emoji: String,
    val title: String,
    val duration: String,
    val colorHex: Long,
    val borderColorHex: Long,
    val content: String
) {
    companion object {
        fun getAllLessons(): List<Lesson> = listOf(
            Lesson(
                id = 1,
                emoji = "01",
                title = "What is Saving?",
                duration = "5 min read",
                colorHex = 0xFFFEF3C7,
                borderColorHex = 0xFFF59E0B,
                content = "Saving means keeping aside some money for the future instead of spending everything you earn.\n\nEven small savings of ₹10-₹50 per day can add up to ₹3,000-₹18,000 per year!\n\nWhy save?\n• Emergencies (medical, crop failure)\n• Big purchases (tools, seeds, home repairs)\n• Education for children\n• Old age security\n\nTips to save:\n1. Set a goal first — know WHY you are saving\n2. Save immediately when you receive income — don't wait\n3. Use a bank account or post office — money at home gets spent\n4. Start small, be consistent"
            ),
            Lesson(
                id = 2,
                emoji = "02",
                title = "Understanding Interest",
                duration = "6 min read",
                colorHex = 0xFFEFF6FF,
                borderColorHex = 0xFF2563EB,
                content = "Interest is the extra money you earn when you deposit in a bank, or extra money you pay when you borrow a loan.\n\nWhen you save in a bank:\n₹10,000 at 7% per year = ₹700 extra after 1 year!\n\nWhen you take a loan:\n₹10,000 at 12% per year = ₹1,200 extra to pay back.\n\nTypes of interest:\n• Simple Interest — calculated only on the original amount\n• Compound Interest — calculated on amount + previous interest (grows faster!)\n\nRemember: Always compare interest rates before taking a loan. Government schemes offer lower rates than moneylenders."
            ),
            Lesson(
                id = 3,
                emoji = "03",
                title = "Types of Loans",
                duration = "7 min read",
                colorHex = 0xFFF0FDF4,
                borderColorHex = 0xFF10B981,
                content = "Loans help you get money now that you repay later with interest. Choosing the right loan saves money!\n\nGovernment / Bank Loans (LOW interest 4-12%):\n• Kisan Credit Card (KCC) — for farmers\n• PM SVANidhi — for street vendors\n• Mudra Loan — for small businesses\n• SHG Loans — for women's self-help groups\n\nMicrofinance Loans (MEDIUM interest 18-26%):\n• JLG (Joint Liability Group) loans\n\nMoneylenders (HIGH interest 36-120%):\n• Avoid these if possible! Very expensive.\n\nBefore taking any loan:\n1. Check if you can repay monthly EMI\n2. Compare interest rates\n3. Read all documents before signing"
            ),
            Lesson(
                id = 4,
                emoji = "04",
                title = "Why Insurance Matters",
                duration = "8 min read",
                colorHex = 0xFFFAF5FF,
                borderColorHex = 0xFF8B5CF6,
                content = "Insurance protects you from big unexpected losses by paying a small amount (premium) regularly.\n\nGovernment insurance schemes:\n• PMJJBY — Life insurance ₹2 lakh for just ₹330/year\n• PMSBY — Accident insurance ₹2 lakh for just ₹12/year\n• PMFBY — Crop insurance for farmers\n• Ayushman Bharat — Health cover ₹5 lakh free for poor families\n\nWhy is it important?\n• Medical emergency: Can cost ₹50,000 - ₹5 lakh\n• Accident or death: Family loses income\n• Crop failure: Entire year's income lost\n\nAction: Visit your nearest bank or Common Service Center (CSC) to enroll in PMJJBY and PMSBY — both together cost less than ₹350 per year!"
            ),
            Lesson(
                id = 5,
                emoji = "05",
                title = "Emergency Fund Basics",
                duration = "6 min read",
                colorHex = 0xFFFFF1F2,
                borderColorHex = 0xFFF43F5E,
                content = "An emergency fund is money saved specifically for unexpected crises — medical bills, job loss, natural disasters.\n\nHow much should you save?\nTry to save 3-6 months of expenses.\nIf your monthly expenses are ₹8,000, aim for ₹24,000-₹48,000 in emergency savings.\n\nWhere to keep emergency fund:\n• Savings bank account (easy to withdraw)\n• Post Office Savings Account\n• NOT in stocks or fixed deposits (hard to withdraw quickly)\n\nHow to build it:\n1. Save at least 10% of every income\n2. Keep it separate — don't mix with daily expenses\n3. Use it ONLY for real emergencies\n4. Rebuild it after using\n\nRemember: Having even ₹5,000-₹10,000 saved separately can prevent you from taking expensive loans during crisis!"
            )
        )
    }
}
