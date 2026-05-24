package jp.girky.wf_noctuahub.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Public Export の ExportRecipes_ja.json をパースするためのレスポンスデータモデル
 */
@Serializable
data class ExportRecipesResponse(
  @SerialName("ExportRecipes") val exportRecipes: List<ExportRecipe>? = emptyList()
)

/**
 * 製作設計図（レシピ）の情報
 */
@Serializable
data class ExportRecipe(
  @SerialName("uniqueName") val uniqueName: String,
  @SerialName("resultType") val resultType: String, // 完成品の uniqueName
  @SerialName("ingredients") val ingredients: List<RecipeIngredient>? = emptyList()
)

/**
 * 設計図の必要素材（パーツなど）の情報
 */
@Serializable
data class RecipeIngredient(
  @SerialName("ItemType") val itemType: String, // 素材の uniqueName
  @SerialName("Count") val count: Int = 1 // 必要数
)
