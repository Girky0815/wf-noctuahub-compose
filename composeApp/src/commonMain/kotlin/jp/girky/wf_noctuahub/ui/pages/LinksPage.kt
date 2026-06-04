package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import jp.girky.wf_noctuahub.ui.components.ui.ListGroup
import jp.girky.wf_noctuahub.ui.components.ui.ListTile
import jp.girky.wf_noctuahub.ui.components.ui.SectionTitle

/**
 * Origin太陽系を冒険するテンノに役立つ各種公式・コミュニティサイトのリンク集画面
 */
@Composable
fun LinksPage(
  modifier: Modifier = Modifier
) {
  val uriHandler = LocalUriHandler.current
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(24.dp)
  ) {
    // ヘッダー部分
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(bottom = 8.dp)
    ) {
      Text(
        text = "リンク集",
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "Origin 太陽系の冒険に役立つ公式サイトやコミュニティサイトのリンク集です。",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // 1. 公式サイトグループ
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      SectionTitle(title = "公式サイト")
      ListGroup {
        ListTile(
          title = "Warframe 公式Webサイト",
          subtitle = "ゲームのダウンロードやニュースの閲覧はこちら。",
          leadingIcon = { Icon(Icons.Rounded.Language, contentDescription = null) },
          trailingContent = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          onClick = { uriHandler.openUri("https://www.warframe.com/ja") }
        )
        ListTile(
          title = "Warframe 公式フォーラム",
          subtitle = "パッチノートや公式からのアナウンス、ユーザーからのフィードバック。",
          leadingIcon = { Icon(Icons.Rounded.Forum, contentDescription = null) },
          trailingContent = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          onClick = { uriHandler.openUri("https://forums.warframe.com/") }
        )
        ListTile(
          title = "Warframe 公式フォーラム 日本語版",
          subtitle = "翻訳されたパッチノートやアナウンスを見ることができる。",
          leadingIcon = { Icon(Icons.Rounded.Translate, contentDescription = null) },
          trailingContent = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          onClick = { uriHandler.openUri("https://forums.warframe.com/forum/124-%E6%97%A5%E6%9C%AC%E8%AA%9E/") }
        )
        ListTile(
          title = "公式ドロップテーブル",
          subtitle = "DEが公開しているアイテムドロップ率 (検索推奨)。",
          leadingIcon = { Icon(Icons.Rounded.TableChart, contentDescription = null) },
          trailingContent = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          onClick = { uriHandler.openUri("https://warframe-web-assets.nyc3.cdn.digitaloceanspaces.com/uploads/cms/hnfvc0o3jnfvc873njb03enrf56.html") }
        )
      }
    }

    // 2. 攻略Wikiグループ
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      SectionTitle(title = "攻略Wiki")
      ListGroup {
        ListTile(
          title = "Warframe Wiki 日本語版",
          subtitle = "日本語版Wiki。日本人テンノ必携の分厚い教科書。",
          leadingIcon = { Icon(Icons.Rounded.MenuBook, contentDescription = null) },
          trailingContent = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          onClick = { uriHandler.openUri("https://wikiwiki.jp/warframe/") }
        )
        ListTile(
          title = "Warframe Wiki 英語版",
          subtitle = "日本語版よりも詳細かつ最新の仕様・データが入手できる。",
          leadingIcon = { Icon(Icons.Rounded.AutoStories, contentDescription = null) },
          trailingContent = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          onClick = { uriHandler.openUri("https://wiki.warframe.com/") }
        )
      }
    }

    // 3. コミュニティツールグループ
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      SectionTitle(title = "コミュニティツール")
      ListGroup {
        ListTile(
          title = "Overframe",
          subtitle = "装備のビルドシミュレーター。有志のビルドも閲覧可能。",
          leadingIcon = { Icon(Icons.Rounded.Build, contentDescription = null) },
          trailingContent = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          onClick = { uriHandler.openUri("https://overframe.gg/?hl=ja") }
        )
        ListTile(
          title = "Warframe Market",
          subtitle = "プライムパーツやMODのトレード相場確認・相手探し。",
          leadingIcon = { Icon(Icons.Rounded.ShoppingCart, contentDescription = null) },
          trailingContent = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          onClick = { uriHandler.openUri("https://warframe.market/") }
        )
      }
    }

    // 4. SNS・配信チャンネルグループ
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      SectionTitle(title = "SNS・配信チャンネル")
      ListGroup {
        ListTile(
          title = "日本語公式 Twitter (X)",
          subtitle = "@WarframeJPN",
          leadingIcon = { Icon(Icons.Rounded.RssFeed, contentDescription = null) },
          trailingContent = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          onClick = { uriHandler.openUri("https://x.com/WarframeJPN") }
        )
        ListTile(
          title = "日本語公式 Bluesky",
          subtitle = "公式Blueskyアカウント。",
          leadingIcon = { Icon(Icons.Rounded.Cloud, contentDescription = null) },
          trailingContent = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          onClick = { uriHandler.openUri("https://bsky.app/profile/did:plc:34zar7aejni5maxrfgmf4jnj") }
        )
        ListTile(
          title = "公式YouTube アカウント",
          subtitle = "公式トレーラーやアップデート情報動画など。",
          leadingIcon = { Icon(Icons.Rounded.VideoLibrary, contentDescription = null) },
          trailingContent = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          onClick = { uriHandler.openUri("https://www.youtube.com/@Warframe") }
        )
        ListTile(
          title = "公式Twitchチャンネル",
          subtitle = "DevStreamなどの公式生配信。Drops対応。",
          leadingIcon = { Icon(Icons.Rounded.LiveTv, contentDescription = null) },
          trailingContent = { Icon(Icons.Rounded.OpenInNew, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
          onClick = { uriHandler.openUri("https://ja.twitch.tv/Warframe") }
        )
      }
    }

    // ボトムナビゲーション用の余白
    Spacer(modifier = Modifier.height(32.dp))
  }
}
