/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ngame.store.core.utils;

/**
 * APP中通用常量
 *
 * @author flan
 * @since 2016年5月3日
 */
public class Constant {

    //public static final String WEB_SITE = "http://192.168.1.231:8080";   //------测试

   public static final String WEB_SITE = "http://opapi.xflqv.cn";    //------正式

    //public static final String WEB_SITE = "http://192.168.0.233:10004";
    //public static final String WEB_SITE = "http://openapi.ngame.cn";
    public static final String WEB_SITE2 = "http://192.168.37.252:20004";      //h5测试
    public static final String WEB_SITE3 = "http://files.ngame.cn";      //h5正式服务器
    public static final String CONFIG_FILE_NAME = "Store.config";
    public static final String CONFIG_FIRST_INSTALL = "FirstInstall";
    public static final String CONFIG_TOKEN = "Token";
    public static final String CONFIG_USER_NAME = "UserName";
    public static final String CONFIG_NICK_NAME = "NickName";
    public static final String CONFIG_USER_PWD = "PassWord";
    public static final String CONFIG_USER_HEAD = "HeadUrl";
    public static final String CFG_RECEIVE_MSG = "ReceiveMsg";
    public static final String CFG_DELETE_APK = "DeleteApk";
    public static final String CFG_ALLOW_4G_LOAD = "AllowLoadBy4G";
    public static final String PUSH_API_KEY = "LUQUlTLy7fybX0oZOVeg9Pwh";       //生产key
    public static final String URL_HOME_GAME_CATEGORY = "/game/gameSubTypeListByParentId";
    public static final String URL_SUBMIT_FEED_BACK_V4 = "/complaint/submitFeedbackV4";
    public static final String URL_DEL_FAVORITE = "/user/delFavorite";
    public static final String URL_ADD_FAVORITE = "/user/addFavorite";
    public static final String URL_VR_LIST = "/app/queryHotInfo";
    public static final String URL_APP_UPDATE = "/app/queryCurrentAppVersion";
    public static final String URL_GAME_CATEGORY = "/game/queryGameCategory";
    public static final String URL_GAME_LAB = "/game/gameSubTypeListByParentId";
    public static final String URL_GAME_LIST = "/game/queryGameByTypeAndLabel";
    public static final String URL_LABEL_GAME_LIST = "/gameDiscovery/queryDiscoveryGameList";
    public static final String URL_GAME_RANK = "/game/gameRankList";
    public static final String URL_GAME_STRATEGY = "/game/queryGameStrategy";
    public static final String URL_GAME_DETAIL = "/game/queryGamesById";
    public static final String URL_GAME_KEY = "/game/queryGameBitMapAndKeyDesc";
    public static final String URL_GAME_CENSUS = "/game/censusGameDownload";
    public static final String URL_VIDEO_LABEL = "/video/queryVideoLabel";
    public static final String URL_VIDEO_TYPE = "/video/queryVideoType";
    public static final String URL_VIDEO_LIST = "/video/queryVideoByTypeAndLabel";
    public static final String URL_VIDEO_RANK_LIST = "/video/videoRankList";
    public static final String URL_VIDEO_DETAIL = "/video/queryVideoById";
    public static final String URL_USER_LOGIN = "/user/userLogin";
    public static final String URL_USER_REGISTER = "/user/userRegistration";
    public static final String URL_COMMENT_ADD_COMMENT = "/comment/addComment";
    public static final String URL_COMMENT_LIST = "/comment/queryCommentList";
    public static final String URL_FEEDBACK = "/complaint/submitFeedback";
    public static final String URL_FEEDBACK_FILE = "/complaint/uploadFeedbackPhoto";
    public static final String URL_BANNER = "/app/queryCarousel";
    public static final String URL_POSTS_LIST = "/gameCircle/getShowPostCategoryList";
    public static final String URL_CIRCLE_POSTS_LIST = "/gameCircle/getShowPostList";
    public static final String URL_BANNER2 = "/app/queryAdvCarousel";
    public static final String URL_PUSH_MSG_DETAIL = "/message/queryMessageById";
    public static final String URL_WATCH_RECORD_QUERY = "/video/queryVideoPlayRecordList";
    public static final String URL_WATCH_RECORD_ADD = "/video/insertVideoPlayRecord";
    public static final String URL_WATCH_RECORD_DELETE = "/video/deletePlayRecord";
    public static final String URL_DELETE_BROWSE = "/gameCircle/deletePostBrowseRecord";
    public static final String URL_UP_LOAD_FILE = "/gameCircle/uploadPostPhoto";
    public static final int NET_STATUS_DISCONNECT = 0x0010;//网络未连接
    public static final int NET_STATUS_4G = 0x0011;// 4G状态连接
    public static final int NET_STATUS_WIFI = 0x0012;//WIFI状态
    public static final String APP_TYPE_ID_0_ANDROID = "0";
    public static final String URL_FORGOT_REGIST_SMS_CODE = "/user/SMSAuthenticationCode";
    public static final String URL_MODIFY_USER_DATA = "/user/modifyUserData";
    public static final String CONFIG_LOGIN_TYPE = "loginType";
    public static final String CONFIG_USER_CODE = "config_user_code";
    public static final String PHONE = "1";
    public static final String QQ = "2";
    public static final String WEIXIN = "3";
    public static final String SINA = "4";//（1手机，2QQ，3微信，4新浪微博）
    public static final String FILE_NAME_SD_CRAD_APP_PKGNAME = "file_name_sd_crad_app_pkgname";
    public static final String SIGN_TOOL_KEY = "sign_tool_key";
}
