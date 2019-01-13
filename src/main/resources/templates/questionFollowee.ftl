<#include "header.ftl">
<link rel="stylesheet" href="../../styles/result.css" />
<link rel="stylesheet" href="../../styles/detail.css">
<div id="main">
    <div class="zg-wrap zu-main clearfix ">
        <div class="zm-profile-section-wrap zm-profile-followee-page">
            <div class="zm-profile-section-head">
                <span class="zm-profile-section-name">
                    <a href="/people/zjuyxy">${curUserName}</a> 关注了 ${curQuestionFolloweeCount} 个问题
                </span>
                <a class="zg-right zg-link-litblue-normal zm-profile-answer-page-return" href="/people/zjuyxy">返回个人主页</a>
            </div>
            <div class="zm-profile-section-list">
                <div id="zh-profile-follows-list">
                    <#list questionInfos as qInfo>
                    <div class="zh-general-list clearfix">
                        <div class="zm-profile-card zm-profile-section-item zg-clear no-hovercard">
                            <#if qInfo.followed>
                            <div class="zg-right">
                                <button class="zg-btn zg-btn-unfollow zm-rich-follow-btn small nth-0
                                    js-follow-question" data-status="1" data-id="${qInfo.question.id}">取消关注</button>
                            </div>
                            <#else>
                            <div class="zg-right">
                                <button class="zg-btn zg-btn-follow zm-rich-follow-btn small nth-0
                                    js-follow-question" data-id="${qInfo.question.id}">关注</button>
                            </div>
                            </#if>
                            <div class="zm-list-content-medium">
                                <h2 class="zm-list-content-title">
                                    <a data-tip="p$t$buaabarty" href="/user/${qInfo.question.id}" class="zg-link" title="Barty">
                                        ${qInfo.question.title}
                                    </a>
                                </h2>

                            <#--<div class="zg-big-gray">计蒜客教研首席打杂</div>-->
                                <div class="details zg-gray">
                                    <a target="_blank" href="/question/${qInfo.question.id}/followers" class="zg-link-gray-normal">${qInfo.followerCount} 关注</a>
                                    /
                                    <a target="_blank" href="#" class="zg-link-gray-normal">${qInfo.commentCount} 回答</a>
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
<script type="text/javascript" src="/scripts/main/site/detail.js"></script>
<#include "footer.html">