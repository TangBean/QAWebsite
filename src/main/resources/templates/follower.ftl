<#include "header.ftl">
<link rel="stylesheet" href="../../styles/result.css" />
<link rel="stylesheet" href="../../styles/detail.css">
<div id="main">
    <div class="zg-wrap zu-main clearfix ">
        <div class="zm-profile-section-wrap zm-profile-followee-page">
            <div class="zm-profile-section-head">
                <span class="zm-profile-section-name">
                    <a href="/people/zjuyxy">${curUserName}</a> 粉丝 ${curFollowerCount} 人
                </span>
                <a class="zg-right zg-link-litblue-normal zm-profile-answer-page-return" href="/people/zjuyxy">返回个人主页</a>
            </div>
            <div class="zm-profile-section-list">
                <div id="zh-profile-follows-list">
                    <#list userInfos as userInfo>
                    <div class="zh-general-list clearfix">
                        <div class="zm-profile-card zm-profile-section-item zg-clear no-hovercard">
                            <#if userInfo.followed>
                            <div class="zg-right">
                                <button class="zg-btn zg-btn-unfollow zm-rich-follow-btn small nth-0
                                    js-follow-user" data-status="1" data-id="${userInfo.user.id}">取消关注</button>
                            </div>
                            <#else>
                            <div class="zg-right">
                                <button class="zg-btn zg-btn-follow zm-rich-follow-btn small nth-0
                                    js-follow-user" data-id="${userInfo.user.id}">关注</button>
                            </div>
                            </#if>
                            <a title="Barty" class="zm-item-link-avatar" href="/people/buaabarty">
                                <img src="${userInfo.user.headUrl}" class="zm-item-img-avatar">
                            </a>
                            <div class="zm-list-content-medium">
                                <h2 class="zm-list-content-title">
                                    <a data-tip="p$t$buaabarty" href="/user/${userInfo.user.id}" class="zg-link" title="Barty">
                                        ${userInfo.user.name}
                                    </a>
                                </h2>

                            <#--<div class="zg-big-gray">计蒜客教研首席打杂</div>-->
                                <div class="details zg-gray">
                                    <a target="_blank" href="/user/${userInfo.user.id}/followers" class="zg-link-gray-normal">${userInfo.followerCount} 粉丝</a>
                                    /
                                    <a target="_blank" href="/user/${userInfo.user.id}/followees" class="zg-link-gray-normal">${userInfo.followeeCount} 关注</a>
                                    /
                                    <a target="_blank" href="#" class="zg-link-gray-normal">${userInfo.commentCount} 回答</a>
                                    /
                                    <a target="_blank" href="#" class="zg-link-gray-normal">548 赞同</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    </#list>
                    <a aria-role="button" class="zg-btn-white zu-button-more">更多</a>
                </div>
            </div>
        </div>
    </div>
</div>
<#include "js.html">
<script type="text/javascript" src="/scripts/main/site/follow.js"></script>
<#include "footer.html">