<div class="font_size_14 float_right">
  <a href="/">超级曲库首页</a>
  <#if !is_logged_in>
    <!-- 未登录 -->
    <a href="${login_link}">${link_text}</a>
  <#else>
    <span>欢迎: ${nickname}(${weibo_uid})</span>
    <a href="/home">我的云盘</a>
    <a href="http://weibo.com/u/${weibo_uid}">微博主页</a>
    <a href="/logout">退出登录</a>
  </#if>
</div>