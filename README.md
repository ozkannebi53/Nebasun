# Nebasun - Türkçe Kelime Bulmaca Oyunu

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen" />
  <img src="https://img.shields.io/badge/Language-Kotlin-orange" />
  <img src="https://img.shields.io/badge/Min%20SDK-24-blue" />
  <img src="https://img.shields.io/badge/Target%20SDK-33-blue" />
</p>

## Oyun Hakkında

**Nebasun**, Türkçe kelimelerle oynanan, animasyonlu ve eğlenceli bir kelime bulmaca oyunudur. Harfleri çemberde birleştirerek kelimeler oluştur, bölümleri tamamla ve ödüller kazan!

---

## Özellikler

### Oyun Mekaniği
- **Harf Çemberi**: Harfler daire üzerinde dizilir. Parmağını sürükleyerek harfleri seç.
- **Şerit Animasyonu**: Seçilen harfler arasında renkli bir şerit çizilir.
- **Aynı Harfe İki Kez Gitme**: Örneğin "ALFA" kelimesinde A harfine iki kez gidebilirsin.
- **Kelime Kutuları**: Bulunan kelimeler animasyonla kutucuklara yazılır.

### Geri Bildirim Animasyonları
- ✅ **Doğru Cevap**: Renkli emoji + pop animasyonu
- ❌ **Yanlış Cevap**: Üzgün emoji + titreme (shake) animasyonu
- 🎉 **Bölüm Tamamlama**: Konfeti animasyonu + şampanya efekti

### Bölüm Sistemi
- 10 bölüm (genişletilebilir)
- **Sıralı Kilit**: Bir sonraki bölüme geçmek için önceki bölümü tamamlamak zorundasın
- Her bölüm tamamlandığında **XP** ve **Altın** kazanılır

### XP ve Para Sistemi
- Bölüm tamamlandığında XP ve Altın kazanılır
- Kazanılan XP ile **Kutu Açılımı** yapılabilir
- Kutulardan **Altın**, **Elmas** veya **Avatar** çıkabilir

### Profil
- Oyuncu adını kendin belirleyebilirsin
- Toplam XP, Altın ve Elmas takibi
- Tamamlanan bölümler listesi
- Kutu açılımı ile kazanılan avatarlar

### Çok Oyunculu Mod
- **1v1 Birebir Mod**: Yapay zeka rakibe karşı oyna
- 60 saniyelik süre sayacı
- Gerçek zamanlı skor takibi

### Akrep Zeka (Yapay Zeka Asistanı)
- **Günün Sözü**: Her açılışta rastgele bir ilham verici söz
- **Atasözleri**: Her sorduğunda 2 rastgele atasözü
- **Deyimler**: Her sorduğunda 2 rastgele deyim
- **Soru-Cevap**: Oyun hakkında sorular sorabilirsin

---

## Teknik Detaylar

| Özellik | Detay |
|---------|-------|
| Platform | Android (API 24+) |
| Dil | Kotlin |
| UI | Material Design 3 |
| Animasyon | Lottie + Android Animator |
| Veri | SharedPreferences + JSON Assets |
| Mimari | Activity-based MVC |

---

## Kurulum

1. Projeyi klonla:
   ```bash
   git clone https://github.com/ozkannebi53/Nebasun.git
   ```

2. Android Studio'da aç

3. Gradle sync yap

4. Emülatör veya cihazda çalıştır

---

## Renk Paleti

Oyun, mor ve lacivert tonlarından kaçınarak canlı ve parlak renkler kullanır:

| Renk | Kullanım |
|------|----------|
| 🟠 `#FF5722` | Ana renk (turuncu) |
| 🟡 `#FFEB3B` | Vurgu rengi (sarı) |
| 🟢 `#4CAF50` | Başarı rengi |
| 🔴 `#F44336` | Hata rengi |
| 🔵 `#03A9F4` | Bilgi rengi (açık mavi) |

---

## Kelime Havuzu

Oyun, kapsamlı bir Türkçe kelime havuzu içerir:
- Günlük kullanım kelimeleri
- Atasözleri ve deyimler
- İlham verici sözler

---

## Lisans

Bu proje MIT lisansı altında yayınlanmıştır.

---

*Nebasun - Türkçeyi oynayarak öğren! 🎮*
