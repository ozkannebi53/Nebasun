package com.nebasun.game.ai

import android.content.Context
import com.nebasun.game.utils.WordLoader

/**
 * Akrep Zeka - Yapay Zeka Asistanı
 *
 * Özellikler:
 * - Günün sözü (her açılışta rastgele)
 * - Her sorduğunda 2 rastgele atasözü
 * - Her sorduğunda 2 rastgele deyim
 * - Oyun ipuçları
 * - Türkçe dil bilgisi yardımı
 */
class AkrepZeka(private val context: Context) {

    private val wordTips = listOf(
        "💡 Harfleri birleştirerek kelimeler oluştur. Aynı harfe iki kez gidebilirsin!",
        "💡 Kısa kelimeler bulmak daha kolaydır. Önce 3 harfli kelimeleri dene!",
        "💡 Bonus kelimeler ekstra puan kazandırır!",
        "💡 Parmağını hızlıca sürükle, harfler arasındaki çizgiyi takip et.",
        "💡 Bir kelimeyi bulamazsan, harfleri farklı sıralarda dene.",
        "💡 Türkçede sık kullanılan ekler: -lar, -ler, -da, -de, -ta, -te",
        "💡 Fiil kökleri genellikle kısa kelimelerdir.",
        "💡 İsimler ve sıfatlar çoğunlukla 4-6 harflidir."
    )

    fun getDailyQuote(): String {
        return WordLoader.getRandomQuote(context)
    }

    fun getRandomProverbsAndIdioms(): List<String> {
        return WordLoader.getRandomProverbs(context, 4)
    }

    fun getTwoRandomProverbs(): List<String> {
        return WordLoader.getRandomProverbs(context, 2)
    }

    fun getRandomTip(): String {
        return wordTips.random()
    }

    fun answerQuestion(question: String): String {
        val q = question.lowercase().trim()
        return when {
            q.contains("atasözü") || q.contains("atasöz") || q.contains("ata söz") -> {
                val items = WordLoader.getRandomProverbs(context, 2)
                    .filter { it.startsWith("📖") }
                    .ifEmpty { WordLoader.getRandomProverbs(context, 2) }
                "İşte iki atasözü:\n\n${items.take(2).joinToString("\n\n")}"
            }
            q.contains("deyim") -> {
                val items = WordLoader.getRandomProverbs(context, 4)
                    .filter { it.startsWith("💬") }
                    .ifEmpty { WordLoader.getRandomProverbs(context, 2) }
                "İşte iki deyim:\n\n${items.take(2).joinToString("\n\n")}"
            }
            q.contains("söz") || q.contains("alıntı") || q.contains("motivasyon") -> {
                "Günün sözü:\n\n\"${getDailyQuote()}\""
            }
            q.contains("ipucu") || q.contains("yardım") || q.contains("nasıl") -> {
                getRandomTip()
            }
            q.contains("merhaba") || q.contains("selam") || q.contains("hey") -> {
                "Merhaba! 👋 Ben Akrep Zeka. Sana şunları sunabilirim:\n\n• Günün sözü\n• Atasözleri\n• Deyimler\n• Oyun ipuçları\n\nNe öğrenmek istersin? 🤖"
            }
            q.contains("nasılsın") || q.contains("naber") -> {
                "Teşekkürler, iyiyim! Sana yardımcı olmak için buradayım. 😊 Bugün ne öğrenmek istersin?"
            }
            q.contains("xp") || q.contains("puan") || q.contains("ödül") -> {
                "XP sistemi hakkında:\n\n⭐ Her bölüm tamamlandığında XP kazanırsın.\n🎁 100 XP ile kutu açabilirsin.\n💎 Kutulardan altın, elmas veya avatar çıkabilir!"
            }
            q.contains("bölüm") || q.contains("seviye") || q.contains("level") -> {
                "Bölüm sistemi hakkında:\n\n🔒 Bölümler sırayla açılır.\n✅ Bir bölümü bitirmeden sonrakine geçemezsin.\n🏆 Her bölüm daha zor ve daha fazla ödül verir!"
            }
            q.contains("türkçe") || q.contains("kelime") -> {
                val items = getRandomProverbsAndIdioms()
                "Türkçe hakkında bazı güzel ifadeler:\n\n${items.take(3).joinToString("\n\n")}"
            }
            else -> {
                val proverbs = getRandomProverbsAndIdioms()
                "İşte bugün için bazı Türkçe ifadeler:\n\n${proverbs.take(2).joinToString("\n\n")}\n\n💡 'atasözü', 'deyim' veya 'ipucu' yazarak daha fazlasını öğrenebilirsin!"
            }
        }
    }
}
