<#include "header.ftl">
<link rel="stylesheet" media="all" href="../styles/letter.css">
    <div id="main">
        <div class="zg-wrap zu-main clearfix ">
            <ul class="letter-list">
                <#list conversationList as conversation>
                    <li id="conversation-item-10005_622873">
                        <a class="letter-link" href="/msg/detail?conversationId=${conversation.message.conversationId}&amp;updateRead=1"></a>
                        <div class="letter-info">
                            <span class="l-time">${conversation.message.createdDate?string('yyyy-MM-dd hh:mm:ss')}</span>
                            <div class="l-operate-bar">
                            <#--<a href="javascript:void(0);" class="sns-action-del" data-id="10005_622873">-->
                            <#--删除-->
                            <#--</a>-->
                                <a href="msg-list?conversationId=10005_622873&amp;updateRead=1&amp;msgType=1">
                                    共${conversation.message.id}条会话
                                </a>
                            </div>
                        </div>
                        <div class="chat-headbox">
                            <#if conversation.unreadCount != 0>
                            <span class="msg-num">
                                ${conversation.unreadCount}
                            </span>
                            </#if>
                            <a class="list-head">
                                <img alt="头像" src="${conversation.targetUser.headUrl}">
                            </a>
                        </div>
                        <div class="letter-detail">
                            <a title="通知" class="letter-name level-color-1">
                                ${conversation.targetUser.name}
                            </a>
                            <p class="letter-brief">
                                <a href="/msg/detail?conversationId=${conversation.message.conversationId}&amp;updateRead=1">
                                    ${conversation.message.content}
                                </a>
                            </p>
                        </div>
                    </li>
                </#list>
            </ul>
        </div>
    </div>
<#include "js.html">
<#include "footer.html">