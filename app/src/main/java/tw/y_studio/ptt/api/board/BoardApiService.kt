package tw.y_studio.ptt.api.board

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tw.y_studio.ptt.api.model.board.article.ArticleDetail
import tw.y_studio.ptt.api.model.board.article.ArticleList
import tw.y_studio.ptt.api.model.board.hot_board.HotBoard

interface BoardApiService {
    @GET("api/boards/popular")
    suspend fun getPopularBoard(): HotBoard

    @GET("api/board/{bid}/articles")
    suspend fun getArticles(
        @Path("bid") boardId: String,
        @Query("title") title: String,
        @Query("start_idx") startIndex: String,
        @Query("limit") limit: Int,
        @Query("desc") desc: Boolean
    ): ArticleList

    @GET("/api/board/{bid}/article/{aid}")
    suspend fun getArticleDetail(
        @Path("bid") boardId: String,
        @Path("aid") articleId: String
    ): ArticleDetail
}
