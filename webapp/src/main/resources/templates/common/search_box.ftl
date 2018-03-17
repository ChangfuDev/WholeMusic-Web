<div class="with_margin">
    <form method="GET" action="/song/search">
        <input type="text" value="${query!''}" class="search_input" title="歌曲和歌手同时输入，可提高匹配程度"
               placeholder="歌曲/歌手名 例如: Yellow Coldplay" name="query"/><br/>
        <#if query??>
          <button type="submit" class="start_search">重新搜索</button>
        <#else>
          <button type="submit" class="start_search">开始搜索</button>
        </#if>
    </form>
</div>
