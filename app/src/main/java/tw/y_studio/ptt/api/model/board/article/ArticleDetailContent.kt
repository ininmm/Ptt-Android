package tw.y_studio.ptt.api.model.board.article

import com.google.gson.annotations.SerializedName

data class ArticleDetailContent(
    @SerializedName("color0")
    val color0: ArticleColor0,
    @SerializedName("color1")
    val color1: ArticleColor1,
    @SerializedName("text")
    val text: String
)
