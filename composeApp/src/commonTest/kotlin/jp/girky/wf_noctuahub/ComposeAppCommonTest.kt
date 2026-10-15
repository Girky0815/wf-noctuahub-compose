package jp.girky.wf_noctuahub

import jp.girky.wf_noctuahub.utils.WikiUtils
import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeAppCommonTest {

  @Test
  fun example() {
    assertEquals(3, 1 + 2)
  }

  @Test
  fun testResurgenceCompanionUrl() {
    assertEquals(
      "https://wikiwiki.jp/warframe/%E3%82%BB%E3%83%B3%E3%83%81%E3%83%8D%E3%83%AB#heliosprime",
      WikiUtils.getResurgenceCompanionUrl("Helios Prime")
    )
    assertEquals(
      "https://wikiwiki.jp/warframe/%E3%82%BB%E3%83%B3%E3%83%81%E3%83%8D%E3%83%AB#shadeprime",
      WikiUtils.getResurgenceCompanionUrl("Shade Prime")
    )
    assertEquals(
      "https://wikiwiki.jp/warframe/%E3%82%BB%E3%83%B3%E3%83%81%E3%83%8D%E3%83%AB#NautilusPrime",
      WikiUtils.getResurgenceCompanionUrl("Nautilus Prime")
    )
    assertEquals(
      "https://wikiwiki.jp/warframe/%E3%82%BB%E3%83%B3%E3%83%81%E3%83%8D%E3%83%AB#wyrmprime",
      WikiUtils.getResurgenceCompanionUrl("Wyrm Prime")
    )
    assertEquals(
      "https://wikiwiki.jp/warframe/%E3%82%BB%E3%83%B3%E3%83%81%E3%83%8D%E3%83%AB#carrierprime",
      WikiUtils.getResurgenceCompanionUrl("Carrier Prime Blueprint")
    )
  }

  @Test
  fun testPrimeItemUrl() {
    assertEquals(
      "https://wikiwiki.jp/warframe/FROST#prime",
      WikiUtils.getPrimeItemUrl("Frost Prime", "WARFRAME")
    )
    assertEquals(
      "https://wikiwiki.jp/warframe/LATRON%20PRIME",
      WikiUtils.getPrimeItemUrl("Latron Prime", "PRIMARY")
    )
    assertEquals(
      "https://wikiwiki.jp/warframe/%E3%82%BB%E3%83%B3%E3%83%81%E3%83%8D%E3%83%AB#shadeprime",
      WikiUtils.getPrimeItemUrl("Shade Prime", "SENTINEL")
    )
  }
}