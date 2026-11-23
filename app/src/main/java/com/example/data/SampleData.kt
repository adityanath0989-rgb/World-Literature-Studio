package com.example.data

object SampleData {
    val initialDocuments = listOf(
        Document(
            id = "doc_1",
            title = "The Whispering Archives of Alexandria",
            content = "In the heart of the ancient city, beneath layers of sand and forgotten memories, the Great Library still breathed. Scholars whispered of scrolls written in languages lost to time—Sumerian hymns, Sanskrit epics, and Arabic treatises on the stars. Elena brushed dust from a parchment glowing with faint golden ink. As her fingertips touched the script, the words began to rearrange themselves into her native tongue, narrating a story that had waited two millennia to be told...",
            category = "Creative Literature",
            subcategory = "Short Story",
            language = "English",
            wordCount = 84,
            characterCount = 520,
            readingTimeMinutes = 1,
            isFavorite = true,
            updatedAt = System.currentTimeMillis() - 3600000
        ),
        Document(
            id = "doc_2",
            title = "মেঘের চিঠি (Letters from the Clouds)",
            content = "সারাবেলা বৃষ্টি ঝরছে টিনের চালে। জানালার ধারে বসে মেঘালয়ের পাহাড়গুলোর দিকে তাকালে মনে হয় প্রকৃতি যেন আজ কোনো এক অজানা ভাষায় তার ডায়েরির পাতা ওল্টাচ্ছে। দূর গ্রামের বাউল গান ভেসে আসে বাতাসে, যেখানে সুরের কোনো সীমানা নেই, কোনো ভাষা নেই—শুধু হৃদয়ের গভীরতম অনুভূতির প্রকাশ।",
            category = "Traditional & Regional Literature",
            subcategory = "Poetry & Prose",
            language = "Bengali",
            wordCount = 42,
            characterCount = 285,
            readingTimeMinutes = 1,
            isFavorite = true,
            updatedAt = System.currentTimeMillis() - 7200000
        ),
        Document(
            id = "doc_3",
            title = "भारतीय शास्त्रीय साहित्य की धरोहर (Heritage of Classical Indian Literature)",
            content = "भारतीय साहित्य की परंपरा वेदों, उपनिषदों और कालिदास के महाकाव्यों से होती हुई आधुनिक युग तक प्रवाहित होती है। इसमें मानवीय संवेदनाओं, दर्शन और प्रकृति के साथ तन्मयता का अद्भुत समन्वय देखने को मिलता है। हर भाषा और बोली इस विशाल वृक्ष की एक हरी-भरी शाखा है जो सांस्कृतिक विविधता को समृद्ध करती है।",
            category = "Academic & Nonfiction",
            subcategory = "Research Article",
            language = "Hindi",
            wordCount = 52,
            characterCount = 372,
            readingTimeMinutes = 1,
            isFavorite = false,
            updatedAt = System.currentTimeMillis() - 14400000
        ),
        Document(
            id = "doc_4",
            title = "ألف ليلة وليلة المعاصرة (Modern One Thousand and One Nights)",
            content = "في زمن تتسارع فيه نبضات العصر الرقمي، تظل الحكايات القديمة تحافظ على سحرها الخاص. كانت شهرزاد تقص القصص لا لتبدد وحشة الليل فحسب، بل لتوقظ العقول وتفتح أبواب الخيال على مصراعيها في عالم يمتد من بغداد إلى الأندلس.",
            category = "Traditional & Regional Literature",
            subcategory = "Folktale",
            language = "Arabic",
            wordCount = 38,
            characterCount = 264,
            readingTimeMinutes = 1,
            isFavorite = true,
            updatedAt = System.currentTimeMillis() - 28800000
        ),
        Document(
            id = "doc_5",
            title = "Les Voix du Temps Perdu",
            content = "Éسcrire, c'est laisser une empreinte indélébile sur le sable mouvant de l'existence. Chaque mot choisi avec soin est une étoile qui éclaire le firmament de la pensée humaine. Dans ce sanctuaire littéraire, toutes les langues résonnent à l'unisson.",
            category = "Creative Literature",
            subcategory = "Memoir",
            language = "French",
            wordCount = 41,
            characterCount = 280,
            readingTimeMinutes = 1,
            isFavorite = false,
            updatedAt = System.currentTimeMillis() - 50000000
        )
    )

    val initialBooks = listOf(
        Book(
            id = "book_1",
            title = "Chronicles of the Multiverse",
            description = "An epic science fantasy novel exploring parallel dimensions where languages shape reality.",
            genre = "Novel",
            language = "English",
            targetWordCount = 80000,
            currentWordCount = 18450
        )
    )

    val initialChapters = listOf(
        Chapter(id = "ch_1", bookId = "book_1", title = "Chapter 1: The First Lexicon", content = "The air in the observatory smelled of ozone and ancient parchment...", orderIndex = 1, wordCount = 3200),
        Chapter(id = "ch_2", bookId = "book_1", title = "Chapter 2: Echoes in Babel", content = "Across the shimmering bridge, the dialect shifted from harsh consonants to fluid vowels...", orderIndex = 2, wordCount = 4500)
    )
}
