package tw.y_studio.ptt.api.model.board.article

import com.google.gson.annotations.SerializedName

data class ArticleColor1(
    @SerializedName("background")
    val background: Int,
    @SerializedName("blink")
    val blink: Boolean,
    @SerializedName("foreground")
    val foreground: Int,
    @SerializedName("highlight")
    val highlight: Boolean,
    @SerializedName("reset")
    val reset: Boolean
)
