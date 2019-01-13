<#include "header.ftl">
<link rel="stylesheet" media="all" href="../styles/letter.css">
<div id="main">
    <div class="zg-wrap zu-main clearfix ">
        <ul class="letter-chatlist">
            <#list conversationList as conversation>
                <li id="msg-item-4009580">
                    <a class="list-head">
                        <img alt="头像" src="${conversation.fromUser.headUrl}">
                    </a>
                    <div class="tooltip fade right in">
                        <div class="tooltip-arrow"></div>
                        <div class="tooltip-inner letter-chat clearfix">
                            <div class="letter-info">
                                <p class="letter-time">${conversation.message.createdDate?string('yyyy-MM-dd hh:mm:ss')}</p>
                            <#--<a href="javascript:void(0);" id="del-link" name="4009580">删除</a>-->
                            </div>
                            <p class="chat-content">
                                ${conversation.message.content}
                            </p>
                        </div>
                    </div>
                </li>
            </#list>
        </ul>
    </div>
</div>
<#include "js.html">
<#include "footer.html">