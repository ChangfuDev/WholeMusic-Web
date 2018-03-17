<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <link href="/css/style.css" rel="stylesheet" type="text/css"/>
    <title>${album.getName()}</title>
</head>
<body>

<h1 class='head'>专辑信息</h1>

<#include "/common/user_header.ftl">

<div>
    <a href="/cloud/download/${album.getMusicProvider().name()}/${album.getAlbumId()}">保存整张专辑到云盘</a>
</div>

<#assign songs=album.getSongs()>
<#include "/common/song_table.ftl">


<#include "/common/search_box.ftl">

</body>
</html>
