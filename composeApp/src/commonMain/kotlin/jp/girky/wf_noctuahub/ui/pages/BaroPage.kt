package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.data.api.model.WorldStateResponse
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.ListItem
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle
import jp.girky.wf_noctuahub.ui.components.ui.EtaText
import jp.girky.wf_noctuahub.utils.Translations
import jp.girky.wf_noctuahub.utils.currentTimeMillis
import jp.girky.wf_noctuahub.utils.WikiUtils
import kotlinx.datetime.Instant
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight

@Composable
fun BaroPage(
  worldState: WorldStateResponse?,
  onLocalize: (String) -> String,
  onGetModDescription: (String) -> String?,
  onGetModCompat: (String) -> String?
) {
  val uriHandler = LocalUriHandler.current
  val baro = worldState?.voidTraders?.firstOrNull() ?: run {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
      Text("Baro Ki'Teer 情報を取得できませんでした。")
    }
    return
  }

  val now = currentTimeMillis()
  val activationLong = baro.activation?.date?.numberLong?.toLongOrNull()
  val expiryLong = baro.expiry?.date?.numberLong?.toLongOrNull()
  val isActive = activationLong != null && expiryLong != null && now >= activationLong && now < expiryLong

  val location = baro.node?.let { Translations.translateRelay(it) } ?: "不明"
  val activationString = activationLong?.let { Instant.fromEpochMilliseconds(it).toString() }
  val expiryString = expiryLong?.let { Instant.fromEpochMilliseconds(it).toString() }

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(16.dp)
  ) {
    item {
      Text(
        text = "Baro Ki'Teer",
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
      )
      if (isActive) {
        Text(
          text = "Baro Ki'Teer 出現中!",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(bottom = 24.dp)
        )
      } else {
        Text(
          text = "Baro Ki'Teer は不在です。",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(bottom = 24.dp)
        )
      }
    }

    item {
      SectionTitle(title = "ステータス", modifier = Modifier.padding(bottom = 8.dp))
      ListGroup {
        ListTile(
          title = "場所",
          subtitle = location
        )
        ListTile(
          title = if (isActive) "離脱まで" else "到着まで",
          trailingContent = {
            val targetString = if (isActive) expiryString else activationString
            if (targetString != null) {
              EtaText(expiryString = targetString)
            }
          }
        )
      }
      Spacer(modifier = Modifier.height(24.dp))
    }

    if (isActive && baro.manifest != null && baro.manifest.isNotEmpty()) {
      val categories = listOf("武器", "MOD", "Void レリック", "装飾品", "外装", "常設")
      val groupedItems = baro.manifest.groupBy { item ->
        val name = onLocalize(item.itemType ?: "")
        Translations.getItemCategory(item.itemType ?: "", name)
      }

      for (category in categories) {
        val itemsInCat = groupedItems[category] ?: emptyList()
        if (itemsInCat.isNotEmpty()) {
          item {
            SectionTitle(title = category, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
            ListGroup {
              for (baroItem in itemsInCat) {
                val name = onLocalize(baroItem.itemType ?: "")
                val ducats = baroItem.primePrice ?: 0
                val credits = baroItem.regularPrice ?: 0
                val formattedCredits = Translations.formatComma(credits)
                val description = if (category == "MOD") {
                  onGetModDescription(baroItem.itemType ?: "")
                } else null
                val compat = if (category == "MOD") {
                  onGetModCompat(baroItem.itemType ?: "")
                } else null
                
                if (description != null) {
                  ListItem {
                    Row(
                      verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                      Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                      )
                      compat?.let {
                        Surface(
                          color = MaterialTheme.colorScheme.secondaryContainer,
                          shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                        ) {
                          Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                          )
                        }
                      }
                    }
                    Text(
                      text = description,
                      style = MaterialTheme.typography.bodyMedium,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                      text = "${ducats} デュカット | ${formattedCredits} Cr",
                      style = MaterialTheme.typography.bodyMedium,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                      modifier = Modifier.padding(top = 2.dp)
                    )
                  }
                } else {
                  val isWeapon = category == "武器"
                  val onClickAction = if (isWeapon) {
                    {
                      try {
                        val url = WikiUtils.getWeaponUrl(name)
                        uriHandler.openUri(url)
                      } catch (e: Exception) {
                        e.printStackTrace()
                      }
                    }
                  } else null

                  val trailingContent: @Composable (() -> Unit)? = if (isWeapon) {
                    {
                      Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = "Wikiを開く",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                      )
                    }
                  } else null

                  ListTile(
                    title = name,
                    subtitle = "${ducats} デュカット | ${formattedCredits} Cr",
                    trailingContent = trailingContent,
                    onClick = onClickAction
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
