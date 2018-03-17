<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>${path}</title>
</head>
<body>

<h1>Directory Index</h1>

<div><a href="/admin/disk/zip/${path}">压缩当前文件夹并下载zip包</a></div>
<ul>
  <#list files as file>
    <li>
        <a href="/admin/disk/${file.getRelativePath()}">${file.name}</a>
    </li>
  </#list>
</ul>

</body>
</html>
