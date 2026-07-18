package jp.girky.wf_noctuahub.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun MarkdownText(
  markdown: String,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
    val lines = markdown.lines()
    lines.forEach { line ->
      val trimmed = line.trim()
      when {
        // 見出し (H1, H2, H3, H4)
        trimmed.startsWith("#") -> {
          val hashCount = trimmed.takeWhile { it == '#' }.length
          val text = trimmed.substring(hashCount).trim()
          val style = when (hashCount) {
            1 -> MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            2 -> MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            else -> MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          }
          
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = parseInlineMarkdown(text),
            style = style,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(4.dp))
          // 見出しの下の分割線
          HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp
          )
          Spacer(modifier = Modifier.height(4.dp))
        }
        // 水平線 (---)
        trimmed == "---" || trimmed == "***" -> {
          HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp)
          )
        }
        // 箇条書きリスト item
        trimmed.startsWith("- ") || trimmed.startsWith("* ") -> {
          val text = trimmed.substring(2).trim()
          Row(
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "•",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = parseInlineMarkdown(text),
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
        // 空行
        trimmed.isEmpty() -> {
          Spacer(modifier = Modifier.height(4.dp))
        }
        // 通常の段落
        else -> {
          Text(
            text = parseInlineMarkdown(trimmed),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

// インラインマークダウン (**bold**, `code`) のパース処理
fun parseInlineMarkdown(text: String): AnnotatedString {
  return buildAnnotatedString {
    var cursor = 0
    while (cursor < text.length) {
      val nextBold = text.indexOf("**", cursor)
      val nextCode = text.indexOf("`", cursor)
      
      // 最も近い装飾を探す
      val targets = listOfNotNull(
        if (nextBold != -1) nextBold to "bold" else null,
        if (nextCode != -1) nextCode to "code" else null
      ).sortedBy { it.first }
      
      if (targets.isEmpty()) {
        // 装飾がこれ以上ない場合、残りを平文として追加して終了
        append(text.substring(cursor))
        break
      }
      
      val (nextPos, type) = targets.first()
      // 装飾の手前までの平文を追加
      if (nextPos > cursor) {
        append(text.substring(cursor, nextPos))
      }
      
      if (type == "bold") {
        val endBold = text.indexOf("**", nextPos + 2)
        if (endBold != -1) {
          withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(text.substring(nextPos + 2, endBold))
          }
          cursor = endBold + 2
        } else {
          // 閉じるタグがない場合は通常のテキストとして処理
          append("**")
          cursor = nextPos + 2
        }
      } else if (type == "code") {
        val endCode = text.indexOf("`", nextPos + 1)
        if (endCode != -1) {
          withStyle(SpanStyle(
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
            background = Color.LightGray.copy(alpha = 0.3f),
            fontWeight = FontWeight.Medium
          )) {
            append(text.substring(nextPos + 1, endCode))
          }
          cursor = endCode + 1
        } else {
          append("`")
          cursor = nextPos + 1
        }
      }
    }
  }
}
