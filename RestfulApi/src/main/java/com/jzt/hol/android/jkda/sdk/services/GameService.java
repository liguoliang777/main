package com.jzt.hol.android.jkda.sdk.services;


import com.jzt.hol.android.jkda.sdk.bean.admin.AdminGameUpdateBody;
import com.jzt.hol.android.jkda.sdk.bean.admin.QuestionListBean;
import com.jzt.hol.android.jkda.sdk.bean.admin.SystemMsgBean;
import com.jzt.hol.android.jkda.sdk.bean.classification.AllClassifyBean;
import com.jzt.hol.android.jkda.sdk.bean.game.GameListBody;
import com.jzt.hol.android.jkda.sdk.bean.game.GameRankListBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.AddCommentBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.AddGameBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.AddPointBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.AppCarouselBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.BrowseHistoryBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentListBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentListBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameListBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.MsgDetailBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.MsgDetailBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.NormalDataBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.PostDetailBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.PostMsgBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.PostSearchListBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.ReportPostBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.VoteListBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.VoteRankBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.main.DiscoverListBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBean;
import com.jzt.hol.android.jkda.sdk.bean.main.YunduanBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBean;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBody;
import com.jzt.hol.android.jkda.sdk.bean.rank.RankListBody;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBean;
import com.jzt.hol.android.jkda.sdk.bean.recommend.RecommendListBody;
import com.jzt.hol.android.jkda.sdk.bean.search.RequestSearchBean;
import com.jzt.hol.android.jkda.sdk.bean.search.SearchBean;
import com.jzt.hol.android.jkda.sdk.bean.search.SearchBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.search.SearchGameVideoBean;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by gp on 2017/2/21.
 */
public interface GameService {

    // app广告
    @POST("app/queryCarousel")
    Observable<AppCarouselBean> queryCarousel(@Header("content-type") String type,
                                              @Body BrowseHistoryBodyBean bean);

    // 查询搜索首页
    @POST("app/queryHotSearchGameAndVideo")
    Observable<SearchGameVideoBean> getSearchGV(@Header("content-type") String type,
                                                @Body RequestSearchBean bean);

    // 搜索
    @POST("app/appSearch")
    Observable<SearchBean> runSearch(@Header("content-type") String type,
                                     @Body SearchBodyBean bean);

    // 发贴
    @POST("gameCircle/addPost")
    Observable<NormalDataBean> addPost(@Header("content-type") String type,
                                       @Body PostMsgBodyBean bean);

    // 游戏列表
    @POST("gameCircle/queryVoteGameList")
    Observable<GameListBean> getGameList(@Header("content-type") String type,
                                         @Body PostMsgBodyBean bean);

    // 提交游戏
    @POST("gameCircle/addVoteGame")
    Observable<NormalDataBean> addVoteGame(@Header("content-type") String type,
                                           @Body AddGameBodyBean bean);

    // 游戏是否投过票
    @POST("gameCircle/isExistVoteGame")
    Observable<NormalDataBean> isAddGame(@Header("content-type") String type,
                                         @Body PostMsgBodyBean bean);

    // game圈首页列表
    @POST("gameCircle/getShowPostHome")
    Observable<GameHubMainBean> circleList(@Header("content-type") String type,
                                           @Body GameHubMainBodyBean bean);

    // 攻略列表
    @POST("gameCircle/queryRaiderPostList")
    Observable<GameHubMainBean> raiderPostList(@Header("content-type") String type,
                                               @Body GameHubMainBodyBean bean);

    // 求助列表
    @POST("gameCircle/querySeekHelpPostList")
    Observable<GameHubMainBean> helpPostList(@Header("content-type") String type,
                                             @Body GameHubMainBodyBean bean);

    // 投票-帖子列表
    @POST("gameCircle/queryPostVoteList")
    Observable<VoteListBean> queryPostVoteList(@Header("content-type") String type,
                                               @Body GameHubMainBodyBean bean);

    // 投票-排行列表
    @POST("gameCircle/queryPostVoteRankList")
    Observable<VoteListBean> queryPostVoteRankList(@Header("content-type") String type,
                                                   @Body VoteRankBodyBean bean);

    // 举报帖子
    @POST("gameCircle/reportPost")
    Observable<NormalDataBean> reportPost(@Header("content-type") String type,
                                          @Body ReportPostBodyBean bean);

    // 帖子详情
    @POST("gameCircle/getShowPostById")
    Observable<PostDetailBean> queryPostDetail(@Header("content-type") String type,
                                               @Body MsgDetailBodyBean bean);
    // 帖子详情
    @POST("gameCircle/getShowPostById")
    Observable<MsgDetailBean> queryMsgDetail(@Header("content-type") String type,
                                              @Body MsgDetailBodyBean bean);
    // 点赞、投票
    @POST("gameCircle/addPointORVote")
    Observable<NormalDataBean> addPoint(@Header("content-type") String type,
                                        @Body AddPointBodyBean bean);

    // 添加帖子评论
    @POST("comment/addComment")
    Observable<NormalDataBean> addComment(@Header("content-type") String type,
                                          @Body AddCommentBodyBean bean);

    // 帖子详情 评论列表
    @POST("comment/queryPostCommentList")
    Observable<CommentListBean> queryCommentList(@Header("content-type") String type,
                                                 @Body CommentListBodyBean bean);

    // 通知 浏览列表
    @POST("gameCircle/queryPostBrowseRecordList")
    Observable<VoteListBean> browseList(@Header("content-type") String type,
                                        @Body CommentBodyBean bean);

    // 通知 帖子列表
    @POST("gameCircle/queryMYPost")
    Observable<VoteListBean> postList(@Header("content-type") String type,
                                      @Body CommentBodyBean bean);

    // 通知 评论列表
    @POST("comment/queryMYCommentList")
    Observable<CommentBean> commentList(@Header("content-type") String type,
                                        @Body CommentBodyBean bean);

    // 帖子 浏览记录
    @POST("gameCircle/addPostBrowseRecord")
    Observable<NormalDataBean> browseHistory(@Header("content-type") String type,
                                             @Body BrowseHistoryBodyBean bean);

    // 排行 下载榜游戏列表
    @POST("game/queryDownloadList")
    Observable<GameRankListBean> rankDownloadList(@Header("content-type") String type,
                                                  @Body GameListBody bean);
    // 排行 好评榜游戏列表
    @POST("gameDiscovery/queryGameListByCategoryAndDownload")
    Observable<LikeListBean> rankCommentList(@Header("content-type") String type,
                                                 @Body RankListBody bean);

    // 帖子搜索
    @POST("gameCircle/gameCircleSearch")
    Observable<PostSearchListBean> searchPost(@Header("content-type") String type,
                                              @Body SearchBodyBean bean);

    // 首页-云端适配-原生手柄
    @POST("game/gameSubTypeListByParentId")
    Observable<YunduanBean> gameType(@Header("content-type") String type,
                                     @Body YunduanBodyBean bean);

    //
    @POST("user/listFavorite")
    Observable<LikeListBean> listFavorite(@Header("content-type") String type,
                                          @Body LikeListBody bean);

    // 首页-每日精选、MOBA精选、枪战精选、新平尝鲜、品牌游戏列表（12个）
    @POST("gameDiscovery/queryDiscoveryHome")
    Observable<DiscoverListBean> queryHomeGame(@Header("content-type") String type,
                                               @Body RecommendListBody bean);
    // 首页-热门攻略
    @POST("gameCircle/queryHomeHotRaiderPost")
    Observable<GameHubMainBean> queryHomeRaider(@Header("content-type") String type,
                                                @Body BrowseHistoryBodyBean bean);

    // 分类首页
    @POST("gameDiscovery/queryDiscoveryGameCategoryList")
    Observable<AllClassifyBean> queryClassifiHome(@Header("content-type") String type,
                                                  @Body YunduanBodyBean bean);

    // 管理-游戏是否需要更新
    @POST("game/queryGameISUpdate")
    Observable<GameRankListBean> queryGameIsUpdate(@Header("content-type") String type,
                                               @Body AdminGameUpdateBody bean);

    // 帮助与反馈-问题列表
    @POST("appHelp/queryHelpHomeHotProblem")
    Observable<QuestionListBean> queryHelpHomeHotProblem(@Header("content-type") String type,
                                                   @Body AdminGameUpdateBody bean);

    // 帮助分类页面
    @POST("appHelp/queryHelpCategoryProblem")
    Observable<QuestionListBean> queryHelpCategoryProblem(@Header("content-type") String type,
                                                         @Body AdminGameUpdateBody bean);

    // 专题精选 - 游戏列表
    @POST("game/queryGameByTypeAndLabel")
    Observable<GameRankListBean> queryGameForSelect(@Header("content-type") String type,
                                                          @Body GameListBody bean);

    // 系统消息
    @POST("message/querySystemMessList")
    Observable<SystemMsgBean> querySystemMessList(@Header("content-type") String type,
                                                 @Body AdminGameUpdateBody bean);

    // 推荐 游戏列表
    @POST("gameRecommend/queryGameRecommendList")
    Observable<RecommendListBean> queryGameRecommendList(@Header("content-type") String type,
                                                         @Body RecommendListBody bean);
}
