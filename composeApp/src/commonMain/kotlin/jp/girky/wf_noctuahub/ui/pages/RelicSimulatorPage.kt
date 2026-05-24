package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.ExportRelic
import jp.girky.wf_noctuahub.data.api.model.RelicReward
import jp.girky.wf_noctuahub.data.repository.WarframeRepository
import jp.girky.wf_noctuahub.ui.components.ui.ExpressiveButtonGroup
import jp.girky.wf_noctuahub.ui.components.ui.ExpressiveButtonOption
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import kotlin.random.Random

/**
 * 疑似開封および確率確認を行う「レリック・シミュレーター」ページ。
 */
@Composable
fun RelicSimulatorPage(
  repository: WarframeRepository,
  onLocalize: (String) -> String
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedRelic by remember { mutableStateOf<ExportRelic?>(null) }
  var selectedRefinement by remember { mutableStateOf(0) } // 0: Intact, 1: Exceptional, 2: Flawless, 3: Radiant
  var simulationResult by remember { mutableStateOf<String?>(null) }

  val allRelics = remember { repository.getAllRelics() }
  val filteredRelics = remember(searchQuery) {
    if (searchQuery.isBlank()) {
      emptyList()
    } else {
      allRelics.filter { 
        it.uniqueName.endsWith("Bronze") && (
          it.name.contains(searchQuery, ignoreCase = true) || 
          it.relicRewards?.any { r -> onLocalize(r.rewardName).contains(searchQuery, ignoreCase = true) } == true
        )
      }.take(10)
    }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(16.dp)
  ) {
    item {
      Text(
        text = "レリック・シミュレーター",
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
      )
      Text(
        text = "レリックの報酬リストとドロップ率を確認し、開封シミュレーションを行うことができます。",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 24.dp)
      )
    }

    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { 
          searchQuery = it 
          if (it.isBlank()) selectedRelic = null
        },
        label = { Text("レリック名や報酬で検索 (例: Lith, Gauss, Axi N19)") },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        singleLine = true
      )
    }

    if (selectedRelic == null) {
      if (filteredRelics.isEmpty() && searchQuery.isNotBlank()) {
        item {
          Text(
            text = "該当するレリックが見つかりませんでした。", 
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
          )
        }
      } else {
        items(filteredRelics) { relic ->
          val isActive = repository.isRelicActive(relic.uniqueName)
          ListTile(
            title = relic.name,
            subtitle = if (isActive) "現在出現中 (入手可能)" else "Vault保管中 (入手不可)",
            titleColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            onClick = {
              selectedRelic = relic
              selectedRefinement = 0
              simulationResult = null
            }
          )
          Spacer(modifier = Modifier.height(8.dp))
        }
      }
    } else {
      val relic = selectedRelic!!
      val isActive = repository.isRelicActive(relic.uniqueName)

      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = relic.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
              )
              Box(
                modifier = Modifier
                  .background(
                    color = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp)
                  )
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = if (isActive) "出現中" else "Vault保管中",
                  color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold
                )
              }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = relic.description ?: "ボイド亀裂ミッションで使用可能。",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      item {
        SectionTitle(title = "精錬度", modifier = Modifier.padding(bottom = 8.dp))
        ExpressiveButtonGroup(
          options = listOf(
            ExpressiveButtonOption("無傷 (Intact)") { selectedRefinement = 0 },
            ExpressiveButtonOption("特別 (Excep)") { selectedRefinement = 1 },
            ExpressiveButtonOption("完璧 (Flaw)") { selectedRefinement = 2 },
            ExpressiveButtonOption("光輝 (Radi)") { selectedRefinement = 3 }
          ),
          selectedIndex = selectedRefinement,
          modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
      }

      if (simulationResult != null) {
        item {
          Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
          ) {
            Column(
              modifier = Modifier.padding(16.dp).fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "開封結果",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = simulationResult!!,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
      }

      item {
        Button(
          onClick = {
            val rewards = relic.relicRewards ?: emptyList()
            if (rewards.isNotEmpty()) {
              simulationResult = onLocalize(simulateRelicOpening(rewards, selectedRefinement))
            }
          },
          modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
          Text("1回開封する", fontWeight = FontWeight.Bold)
        }
      }

      item {
        SectionTitle(title = "報酬と確率", modifier = Modifier.padding(bottom = 8.dp))
      }

      val rewards = relic.relicRewards ?: emptyList()
      items(rewards) { reward ->
        val chance = getRewardChance(reward.rarity, selectedRefinement, countRarityInRewards(rewards, reward.rarity))
        val rarityName = when (reward.rarity) {
          "RARE" -> "レア"
          "UNCOMMON" -> "アンコモン"
          else -> "コモン"
        }
        val rarityColor = when (reward.rarity) {
          "RARE" -> Color(0xFFFBC02D)
          "UNCOMMON" -> Color(0xFF78909C)
          else -> Color(0xFF8D6E63)
        }

        ListTile(
          title = onLocalize(reward.rewardName),
          subtitle = "$rarityName - 確率: ${String.format("%.2f", chance)}%",
          trailingContent = {
            Box(
              modifier = Modifier
                .background(rarityColor, RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = reward.rarity,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
              )
            }
          }
        )
        Spacer(modifier = Modifier.height(8.dp))
      }

      item {
        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = { selectedRelic = null },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
          modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
        ) {
          Text("検索に戻る")
        }
      }
    }
  }
}

private fun countRarityInRewards(rewards: List<RelicReward>, rarity: String): Int {
  return rewards.count { it.rarity == rarity }
}

private fun getRewardChance(rarity: String, refinement: Int, totalInRarity: Int): Double {
  if (totalInRarity <= 0) return 0.0
  return when (rarity) {
    "RARE" -> {
      val baseChance = when (refinement) {
        0 -> 2.0
        1 -> 4.0
        2 -> 6.0
        else -> 10.0
      }
      baseChance / totalInRarity
    }
    "UNCOMMON" -> {
      val baseChance = when (refinement) {
        0 -> 11.0
        1 -> 13.0
        2 -> 17.0
        else -> 20.0
      }
      baseChance / totalInRarity
    }
    else -> { // COMMON
      val baseChance = when (refinement) {
        0 -> 25.33
        1 -> 23.33
        2 -> 20.00
        else -> 16.67
      }
      baseChance / totalInRarity
    }
  }
}

private fun simulateRelicOpening(rewards: List<RelicReward>, refinement: Int): String {
  val rand = Random.nextDouble(100.0)
  var accumulatedChance = 0.0

  for (reward in rewards) {
    val totalInRarity = countRarityInRewards(rewards, reward.rarity)
    val chance = getRewardChance(reward.rarity, refinement, totalInRarity)
    accumulatedChance += chance
    if (rand <= accumulatedChance) {
      return reward.rewardName
    }
  }
  return rewards.lastOrNull()?.rewardName ?: "不明"
}
