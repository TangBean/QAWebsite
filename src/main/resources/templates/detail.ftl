<#include "header.ftl">
<link rel="stylesheet" href="../styles/detail.css">
    <div class="zg-wrap zu-main clearfix with-indention-votebar" itemscope="" itemtype="http://schema.org/Question" id="zh-single-question-page" data-urltoken="36301524" role="main">
        <div class="zu-main-content">
            <div class="zu-main-content-inner">
                <meta itemprop="isTopQuestion" content="false">
                <meta itemprop="visitsCount" content="402">
                <div class="zm-tag-editor zg-section">
                    <div class="zm-tag-editor-labels zg-clear">
                        <a data-tip="t$b$19550730" class="zm-item-tag" href="">新浪微博</a>
                        <a data-tip="t$b$19554412" class="zm-item-tag" href="">网络营销</a>
                        <a data-tip="t$b$19559739" class="zm-item-tag" href="">微博粉丝</a>
                        <a data-tip="t$b$19560290" class="zm-item-tag" href="">僵尸粉</a>
                        <a data-tip="t$b$19565757" class="zm-item-tag" href="">网络水军</a>
                        <a href="javascript:;" class="zu-edit-button" name="edit">
                            <i class="zu-edit-button-icon"></i>修改</a>
                    </div>
                </div>
                <div id="zh-question-title" data-editable="true" class="zm-editable-status-normal">
                    <h2 class="zm-item-title">

                        <span class="zm-editable-content">${question.title}</span>

                    </h2>
                </div>
                <div id="zh-question-detail" class="zm-item-rich-text zm-editable-status-normal" data-resourceid="6727688" data-action="/question/detail">
                    <div class="zm-editable-content">${question.content}</div>
                </div>
                <div class="zm-side-section">
                    <div class="zm-side-section-inner" id="zh-question-side-header-wrap">
                        <#if isFollowed>
                        <button class="follow-button zg-follow zg-btn-white js-follow-question" data-id="${question.id}"
                                data-status="1">
                            取消关注
                        </button>
                        <#else>
                        <button class="follow-button zg-follow zg-btn-green js-follow-question" data-id="${question.id}">
                            关注问题
                        </button>
                        </#if>
                        <div class="zg-btn-white goog-inline-block goog-menu-button" id="zh-question-operation-menu" role="button" aria-expanded="false" tabindex="0" aria-haspopup="true" style="-webkit-user-select: none;">
                            <div class="goog-inline-block goog-menu-button-outer-box">
                                <div class="goog-inline-block goog-menu-button-inner-box">
                                    <div class="goog-inline-block goog-menu-button-caption">
                                        <i class="zg-icon-dropdown-menu zg-icon"></i>
                                        <b class="hide-text">设置</b></div>
                                    <div class="goog-inline-block goog-menu-button-dropdown">&nbsp;</div></div>
                            </div>
                        </div>
                        <div class="goog-menu goog-menu-vertical" role="menu" aria-haspopup="true" style="-webkit-user-select: none; visibility: visible; left: 92px; top: 33px; display: none;">
                            <div class="goog-menuitem" role="menuitem" id=":8" style="-webkit-user-select: none;">
                                <div class="goog-menuitem-content" style="-webkit-user-select: none;">使用匿名身份</div></div>
                            <div class="goog-menuitem" role="menuitem" id=":9" style="-webkit-user-select: none;">
                                <div class="goog-menuitem-content" style="-webkit-user-select: none;">问题重定向</div></div>
                        </div>
                        <div class="zh-question-followers-sidebar">
                            <div class="zg-gray-normal">
                                <a href="/question/${question.id}/followers">
                                    <strong>${followerCount}</strong></a>人关注该问题</div>
                        </div>
                    </div>
                </div>
                <div id="zh-question-answer-wrap" data-pagesize="10" class="zh-question-answer-wrapper navigable" data-widget="navigable" data-navigable-options="{&quot;items&quot;: &quot;&gt;.zm-item-answer&quot;}" data-init="{&quot;params&quot;: {&quot;url_token&quot;: 36301524, &quot;pagesize&quot;: 10, &quot;offset&quot;: 0}, &quot;nodename&quot;: &quot;QuestionAnswerListV2&quot;}">
                  <#list commentVos as vo>
                      <div class="zm-item-answer  zm-item-expanded js-comment">
                          <link itemprop="url" href="">
                          <meta itemprop="answer-id" content="22162611">
                          <meta itemprop="answer-url-token" content="66862039">
                          <a class="zg-anchor-hidden" name="answer-22162611"></a>
                          <div class="zm-votebar goog-scrollfloater js-vote" data-id="${vo.comment.id}">
                              <#if vo.liked == 1>
                              <button class="up js-like pressed" title="赞同">
                              <#else>
                              <button class="up js-like" title="赞同">
                              </#if>
                                  <i class="icon vote-arrow"></i>
                                  <span class="count js-voteCount">${vo.likeCount}</span>
                                  <span class="label sr-only">赞同</span>
                              </button>

                              <#if vo.liked == -1>
                              <button class="down js-dislike pressed" title="反对，不会显示你的姓名">
                              <#else>
                              <button class="down js-dislike" title="反对，不会显示你的姓名">
                              </#if>
                                  <i class="icon vote-arrow"></i>
                                  <span class="label sr-only">反对，不会显示你的姓名</span>
                              </button>
                          </div>
                          <div class="answer-head">
                              <div class="zm-item-answer-author-info">
                                  <a class="zm-item-link-avatar avatar-link" href="" target="_blank" data-tip="p$t$yingxiaodao">
                                      <img src="${vo.user.headUrl}" class="zm-list-avatar avatar"></a>
                                  <a class="author-link" data-tip="p$t$yingxiaodao" target="_blank" href="/user/${vo.user.id}">${vo.user.name}</a>
                                  <span title="爱科学 爱运动" class="bio">，爱科学 爱运动</span></div>
                              <div class="zm-item-vote-info" data-votecount="28" data-za-module="VoteInfo">
                                <span class="voters text">
                                    <a href="" class="more text">
                                        <span class="js-voteCount">${vo.likeCount}</span>&nbsp;人赞同</a></span>
                              </div>
                          </div>
                          <div class="zm-item-rich-text expandable js-collapse-body" data-resourceid="6727688" data-action="/answer/content" data-author-name="营销岛" data-entry-url="/question/36301524/answer/66862039">
                              <div class="zh-summary summary clearfix" style="display:none;">谢邀！网络水军的兴衰起伏，与互联网的进化相互交织。他们是社交时代的镜子，折射的不仅是屏幕上的瞬息万变，还有公众意识与舆论的更替变迁。从论坛时代到微博时代，再到微信时代，水军一直处于舆论的风口浪尖，但他们自称舆论的“弄潮儿”。从人声鼎沸到繁…
                                  <a href="" class="toggle-expand">显示全部</a></div>
                              <div class="zm-editable-content clearfix">
                                  <p>${vo.comment.content}</p>
                              </div>
                          </div>
                          <a class="zg-anchor-hidden ac" name="22162611-comment"></a>
                          <div class="zm-item-meta answer-actions clearfix js-contentActions">
                              <div class="zm-meta-panel">
                                  <a itemprop="url" class="answer-date-link meta-item" target="_blank" href="">发布于 ${vo.comment.createdDate?string('yyyy-MM-dd')}</a>
                                  <a href="" name="addcomment" class="meta-item toggle-comment js-toggleCommentBox">
                                      <i class="z-icon-comment"></i>4 条评论</a>
                                  <a href="" class="meta-item zu-autohide js-thank" data-thanked="false">
                                      <i class="z-icon-thank"></i>感谢</a>

                                  <button class="item-collapse js-collapse" style="transition: none;">
                                      <i class="z-icon-fold"></i>收起</button>
                              </div>
                          </div>
                      </div>
                  </#list>
                </div>
                <a name="draft"></a>
                <form action="/comment/add" method="post" id="commentform">
                    <input type="hidden" name="questionId" value="${question.id}"/>
                    <div id="zh-question-answer-form-wrap" class="zh-question-answer-form-wrap">
                        <div class="zm-editable-editor-wrap" style="">
                            <div class="zm-editable-editor-outer">
                                <div class="zm-editable-editor-field-wrap">
                                    <textarea name="content" id="content" class="zm-editable-editor-field-element editable" style="font-style: italic;width:100%;"></textarea>
                                </div>
                            </div>

                            <div class="zm-command clearfix">
                            <span class=" zg-right">
                                <button type="submit" class="submit-button zg-btn-blue">发布回答</button></span>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
        </div>
    </div>
<#include "js.html">
<script type="text/javascript" src="/scripts/main/site/detail.js"></script>
<#include "footer.html">