<div>
    <table class='table with_margin'>
        <tr>
            <#if !isCloudDisk??><th class="no_padding font_size_16">♫</th></#if>
            <th>歌 曲</th>
            <#if !isCloudDisk??><th>歌 手</th></#if>
            <th>专 辑</th>
            <th>来 源</th>
            <#if !isCloudDisk??><th>bitrate</th></#if>
            <th>链 接</th>
            <#if !isCloudDisk??><th>操 作</th></#if>
        </tr>
        <#list songs as song>
          <tr>
            <#if String.isInstance(song)>
                <td colspan="999">${song}</td>
            <#else>
                <#if !isCloudDisk??>
                  <td class="padding_1"><a href="${song.getPicUrl()!''}">
                    <img class="cover block_img" src="${song.getPicUrl()!''}"/></a>
                  </td>
                </#if>
                <td class="song_name"><span title="${song.getName()}">${song.getName()}</span></td>
                <#if !isCloudDisk??><td><span>${song.getFormattedArtistsString()}</span></td></#if>
                <td><a href="/album/${song.getMusicProvider().name()}/${(song.getAlbum().getAlbumId())!''}" title="${(song.getAlbum().getName())!''}">${(song.getAlbum().getName())!''}</a></td>
                <td><span>${song.getMusicProvider().toString()}</span></td>
                <#if !isCloudDisk??><td><span>${SongUtils.getReadableBitRate(song.getMusicLink().getBitRate())}</span></td></#if>
                <td>
                  <#if !isCloudDisk??>
                    <a href="${song.getMusicLink().getUrl()}" target="_blank">播放/下载</a>
                  <#else>
                    <a href="/disk/${song.getMusicProvider().name()}/${song.getAlbum().getAlbumId()}/${song.getSongId()}" target="_blank">点击下载</a>
                  </#if>
                </td>
                <#if !isCloudDisk??>
                  <td><a href="/cloud/download/${song.getMusicProvider().name()}/${(song.getAlbum().getAlbumId())!""}/${song.getSongId()}" target="_blank">保存到云盘</a></td>
                </#if>
            </#if>
          </tr>
        </#list>
    </table>
</div>
