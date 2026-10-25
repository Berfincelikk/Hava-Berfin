package com.kampplus.hava.model

data class CinemaVenue(
    val id: String,
    val name: String,
    val city: String,
    val district: String,
    val description: String,
    val ticketInfo: String,
    val sampleShowing: String? = null,  // örn. "Bu akşam 20:30 · Örnek program"
    val ticketUrl: String? = null       // programın/biletin olduğu gerçek sayfa
)

/** Demo verisi: mekanlar haberlerden alındı, seanslar örnektir, gerçek program değildir. */
object CinemaVenues {

    private val all = listOf(
        // ---- İstanbul ----
        CinemaVenue(
            id = "zorlu-psm",
            name = "Zorlu PSM Vestel Amfi",
            city = "İstanbul",
            district = "Beşiktaş",
            description = "Yaz aylarında açık hava film gösterimleri.",
            ticketInfo = "Genellikle ücretsiz",
            sampleShowing = "Bu akşam 20:30 "
        ),
        CinemaVenue(
            id = "fisekhane",
            name = "Fişekhane Açık Hava Sineması",
            city = "İstanbul",
            district = "Zeytinburnu",
            description = "Tarihi su kulesi ve fabrika atmosferinde nostaljik gösterimler.",
            ticketInfo = "Mekanın sayfasından kontrol et",
            sampleShowing = "Yarın akşam 21:00 ",
            ticketUrl = "https://www.fisekhane.com/tr/"
        ),
        CinemaVenue(
            id = "yelken-kulubu",
            name = "İstanbul Yelken Kulübü",
            city = "İstanbul",
            district = "Kadıköy",
            description = "Çim alanda deniz manzaralı gösterimler.",
            ticketInfo = "Biletinial",
            sampleShowing = "Bu akşam 21:00 ",
            ticketUrl = "https://biletinial.com/tr-tr/mekan/istanbul-yelken-kulubu-acik-hava-sinemasi"
        ),
        CinemaVenue(
            id = "bomontiada",
            name = "Bomontiada",
            city = "İstanbul",
            district = "Şişli",
            description = "Avluda açık hava sinema geceleri.",
            ticketInfo = "Biletix",
            sampleShowing = "Yarın akşam 20:30 ",
            ticketUrl = "https://www.biletix.com/etkinlik-grup/491615475/TURKIYE/tr/tv-acik-hava-sinema-geceleri"
        ),
        CinemaVenue(
            id = "swissotel-sultanpark",
            name = "Swissôtel The Bosphorus (Sultanpark)",
            city = "İstanbul",
            district = "Beşiktaş",
            description = "Yaz boyunca seçki filmler.",
            ticketInfo = "Mekanın sayfasından kontrol et"
        ),
        CinemaVenue(
            id = "istinyepark-teras",
            name = "İstinyePark Teras",
            city = "İstanbul",
            district = "Sarıyer",
            description = "Çarşamba akşamları teras gösterimi.",
            ticketInfo = "Mekanın sayfasından kontrol et",
            sampleShowing = "Çarşamba 20:30 "
        ),
        CinemaVenue(
            id = "akasya",
            name = "Akasya Açık Hava Sineması",
            city = "İstanbul",
            district = "Üsküdar",
            description = "Yıldızların altında, Togg iş birliğiyle hazırlanan film programı.",
            ticketInfo = "Mekanın sayfasından kontrol et",
            sampleShowing = "Cumartesi 21:00 "
        ),
        CinemaVenue(
            id = "miniaturk",
            name = "Miniatürk",
            city = "İstanbul",
            district = "Beyoğlu",
            description = "Yaz akşamlarında Yeşilçam klasikleri ve animasyonlar.",
            ticketInfo = "Mekanın sayfasından kontrol et",
            sampleShowing = "Yarın akşam 20:30 "
        ),

        // ---- Ankara ----
        CinemaVenue(
            id = "cermodern",
            name = "CerModern Açık Hava Sineması",
            city = "Ankara",
            district = "Altındağ",
            description = "İtalyan ve Fransız sinemasından ödüllü ve klasik yapımlar.",
            ticketInfo = "Mekanın sayfasından kontrol et",
            sampleShowing = "Bu akşam 21:00 "
        ),
        CinemaVenue(
            id = "cankaya-mahalle",
            name = "Çankaya Mahalle Sinemaları",
            city = "Ankara",
            district = "Çankaya",
            description = "Belediyenin mahallelerde kurduğu açık hava gösterimleri.",
            ticketInfo = "Ücretsiz",
            sampleShowing = "Yarın akşam 20:30 "
        ),

        // ---- Denizli ----
        CinemaVenue(
            id = "denizli-acik-hava",
            name = "Denizli Açık Hava Sinema Günleri",
            city = "Denizli",
            district = "Büyükşehir Belediyesi etkinliği",
            description = "Türk sinemasının sevilen yapımları.",
            ticketInfo = "Ücretsiz, biletler internetten alınır",
            sampleShowing = "Bu akşam 21:00 "
        ),
        // ---- İstanbul (yeni) ----
        CinemaVenue(
            id = "sakip-sabanci",
            name = "Sakıp Sabancı Müzesi Açık Hava Sineması",
            city = "İstanbul",
            district = "Sarıyer",
            description = "Boğaz manzarasında film, patlamış mısır ve ikramlar.",
            ticketInfo = "Bilet için müzenin sayfasına bak",
            sampleShowing = "Cumartesi 21:00 "
        ),

        // ---- Sakarya (demo) ----
        CinemaVenue(
            id = "sakarya-sapanca",
            name = "Sapanca Göl Kıyısı Sineması ",
            city = "Sakarya",
            district = "Sapanca",
            description = "Göl manzarasında, çimlerde film gecesi.",
            ticketInfo = "Demo verisi",
            sampleShowing = "Bu akşam 21:00 "
        ),
        CinemaVenue(
            id = "sakarya-adapazari",
            name = "Adapazarı Park Amfi Sineması",
            city = "Sakarya",
            district = "Adapazarı",
            description = "Parkta amfi tiyatro düzeninde açık hava gösterimi.",
            ticketInfo = "Demo verisi",
            sampleShowing = "Yarın akşam 20:30 "
        ),
        CinemaVenue(
            id = "sakarya-ara",
            name = "Sakarya'da açık hava sineması ara",
            city = "Sakarya",
            district = "Adapazarı",
            description = "Haritada bu şehirdeki gösterimleri keşfet.",
            ticketInfo = "Haritada ara",
            ticketUrl = "https://www.google.com/maps/search/?api=1&query=a%C3%A7%C4%B1k+hava+sinemas%C4%B1+Sakarya"
        ),

        // ---- Kocaeli (demo) ----
        CinemaVenue(
            id = "kocaeli-izmit",
            name = "İzmit Körfez Sahil Sineması ",
            city = "Kocaeli",
            district = "İzmit",
            description = "Körfez manzaralı sahilde gün batımından sonra film.",
            ticketInfo = "Demo verisi",
            sampleShowing = "Bu akşam 20:45"
        ),
        CinemaVenue(
            id = "kocaeli-gebze",
            name = "Gebze Açık Hava Sineması ",
            city = "Kocaeli",
            district = "Gebze",
            description = "Yaz akşamlarına özel nostaljik film gösterimleri.",
            ticketInfo = "Demo verisi",
            sampleShowing = "Cumartesi 21:00"
        ),
        CinemaVenue(
            id = "kocaeli-ara",
            name = "Kocaeli'de açık hava sineması ara",
            city = "Kocaeli",
            district = "İzmit",
            description = "Haritada bu şehirdeki gösterimleri keşfet",
            ticketInfo = "Haritada ara",
            ticketUrl = "https://www.google.com/maps/search/?api=1&query=a%C3%A7%C4%B1k+hava+sinemas%C4%B1+Kocaeli"
        )
    )

        fun forCity(cityName: String): List<CinemaVenue> =
        all.filter { it.city.equals(cityName, ignoreCase = true) }

    fun byId(id: String): CinemaVenue? = all.firstOrNull { it.id == id }

    /** Mekanı olan şehirler, listede ilk görüldükleri sırayla. */
    fun cities(): List<String> = all.map { it.city }.distinct()
}
