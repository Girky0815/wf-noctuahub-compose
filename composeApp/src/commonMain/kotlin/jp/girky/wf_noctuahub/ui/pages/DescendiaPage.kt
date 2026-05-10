package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.data.api.model.WsDescent
import jp.girky.wf_noctuahub.data.api.model.WsDescentChallenge
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Warning

@Composable
fun DescendiaPage(
  worldState: WorldStateResponse?,
  onLocalize: (String) -> String
) {
  if (worldState == null) {
    jp.girky.wf_noctuahub.ui.components.ui.CenteredLoadingIndicator()
    return
  }

  // worldState.time は秒単位のUNIX時間と推測されるためミリ秒化。未取得の場合は 0L とする。
  val now = (worldState.time ?: 0L) * 1000L
  val activeDescents = worldState.descents?.filter { descent ->
  val start = descent.activation?.epochMillis ?: 0L
  val end = descent.expiry?.epochMillis ?: Long.MAX_VALUE
  now in start..end
  } ?: emptyList()

  LazyColumn(
  modifier = Modifier.fillMaxSize().padding(16.dp)
  ) {
  item {
    Text(
    text = "ディセンディア",
    style = MaterialTheme.typography.displaySmall,
    color = MaterialTheme.colorScheme.onSurface,
    modifier = Modifier.padding(bottom = 16.dp)
    )
    Text(
    text = "21のチャレンジを次々にクリアしていくミッション。",
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = Modifier.padding(bottom = 24.dp)
    )
  }

  if (activeDescents.isEmpty()) {
    item {
    Text(
      text = "現在アクティブなディセンディアはありません",
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      style = MaterialTheme.typography.bodyLarge
    )
    }
  } else {
    items(activeDescents) { descent ->
    DescendiaCard(descent = descent, onLocalize = onLocalize)
    Spacer(modifier = Modifier.height(24.dp))
    }
  }
  }
}

@Composable
fun DescendiaCard(
  descent: WsDescent,
  onLocalize: (String) -> String
) {
  val challenges = descent.challenges ?: emptyList()
  
  Column {
  SectionTitle(title = "現在のローテーション", modifier = Modifier.padding(bottom = 8.dp))
  ListGroup {
    challenges.forEach { challenge ->
    val typeRaw = challenge.type ?: "不明"
    val type = jp.girky.wf_noctuahub.utils.Translations.translateDescendiaMissionType(typeRaw)
    val modifierRaw = challenge.challenge ?: "不明"
    val modifierName = jp.girky.wf_noctuahub.utils.Translations.translateDescendiaModifier(modifierRaw)
    val missionIndex = (challenge.index ?: 0)
    
    val subtitleText = buildString {
      append(modifierName)
      val aurasRaw = challenge.auras ?: emptyList()
      if (aurasRaw.isNotEmpty()) {
      val aurasTranslated = aurasRaw.map { jp.girky.wf_noctuahub.utils.Translations.translateDescendiaModifier(it) }.distinct()
      val uniqueAuras = aurasTranslated.filter { it != modifierName }
      if (uniqueAuras.isNotEmpty()) {
        append("\n")
        append(uniqueAuras.joinToString(", "))
      }
      }
    }
    
    ListTile(
      title = type,
      subtitle = subtitleText,
      leadingIcon = {
      Text(
        text = "$missionIndex",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface
      )
      }
    )
    }
  }
  }
}
